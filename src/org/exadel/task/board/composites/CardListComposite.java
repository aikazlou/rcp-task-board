package org.exadel.task.board.composites;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.exadel.task.board.dialogs.CardEditDialog;
import org.exadel.task.board.model.*;
import org.exadel.task.board.service.TaskService;
import org.exadel.task.board.service.exceptions.ServiceException;

public class CardListComposite extends Composite {

	private final TaskService service = new TaskService();

	private CardList list;

	private ListViewer viewer;

	private static ListViewer fromViewer;
	private static int fromListId;

	public CardListComposite(Composite parent, int style, CardList list) {
		super(parent, style);

		this.list = list;

		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		this.setLayout(layout);

		createArea();
	}

	private void createArea() {

		final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);

		final Label label = new Label(this, SWT.NONE);
		label.setLayoutData(gridData);
		label.setText(list.getTitle());

		createViewer();

		final Button addCard = new Button(this, SWT.PUSH);
		addCard.setText("Add Card");

		final Button deleteCard = new Button(this, SWT.PUSH);
		deleteCard.setText("Delete Card");
		deleteCard.setToolTipText("Delete Selected Card");

		final Button deleteList = new Button(this, SWT.PUSH);
		deleteList.setText("Delete List");

		addSelectListener(this, addCard);
		addDeleteCardListener(this, deleteCard);
		addDeleteListListener(this, deleteList);
	}

	private void addDeleteListListener(final Composite parent, Button b) {
		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				service.deleteList(list);
				final Composite grand = parent.getParent();
				parent.dispose();
				grand.layout();
			}

		});
	}

	private void addDeleteCardListener(final Composite parent, Button b) {
		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				final StructuredSelection selection = (StructuredSelection) viewer
						.getSelection();
				if (!selection.isEmpty()) {
					final Card card = (Card) selection.getFirstElement();
					list.removeCard(card);
					try {
						list = service.updateList(list);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					viewer.remove(card);
					resizeViewer(viewer);
				}
			}

		});
	}

	private void addSelectListener(final Composite parent, Button b) {

		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Card card = new Card(list.getUser(), "Title", "Type", "Content");

				final CardEditDialog dialog = new CardEditDialog(parent
						.getShell(), card);
				dialog.create();
				if (dialog.open() == Window.OK) {

					list.addCard(card);

					try {
						list = service.updateList(list);

					} catch (ServiceException e1) {
						e1.printStackTrace();
						list.removeCard(card);
					}

					int index = list.getCards().indexOf(card);
					card = list.getCards().get(index);
					viewer.add(card);
					resizeViewer(viewer);
				}

			}

		});

	}

	private void createViewer() {
		viewer = new ListViewer(this, SWT.BORDER);

		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Card) {
					return ((Card) element).toString();
				} else
					return super.getText(element);
			}
		});

		viewer.add(list.getCards().toArray());

		addDoubleClickListener();
		addDragAndDrop();
	}

	private void addDoubleClickListener() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				Card card = (Card) ((StructuredSelection) event.getSelection())
						.getFirstElement();

				final Control control = viewer.getControl();

				final CardEditDialog dialog = new CardEditDialog(control
						.getShell(), card);
				dialog.create();
				if (dialog.open() == Window.OK) {

					try {
						card = service.updateCard(card);
						viewer.update(card, null);
					} catch (ServiceException e) {
						e.printStackTrace();

						final String message = card
								+ " has been deleted in other session";
						MessageDialog.openError(viewer.getControl().getShell(),
								"Error", message);
						viewer.remove(card);
					}

					control.setSize(control.computeSize(SWT.DEFAULT,
							SWT.DEFAULT));
					control.getParent().layout();
				}
			}
		});
	}

	private void addDragAndDrop() {

		final Transfer[] transferTypes = new Transfer[] { TextTransfer
				.getInstance() };

		final DragSourceListener dragListener = new DragSourceAdapter() {

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = ((StructuredSelection) viewer.getSelection())
						.getFirstElement().toString();

				fromViewer = viewer;
				fromListId = list.getId();
			}

		};

		final DropTargetListener dropListener = new DropTargetAdapter() {

			@Override
			public void drop(DropTargetEvent event) {

				final Card card = (Card) ((StructuredSelection) fromViewer
						.getSelection()).getFirstElement();
				final CardList fromList = service.getList(fromListId);
				final CardList toList = service.getList(list.getId());

				try {
					service.moveCard(card, fromList, toList);
					fromViewer.remove(card);
					resizeViewer(fromViewer);
					viewer.add(card);
					resizeViewer(viewer);
					viewer.getControl().getParent().getParent().layout();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}

		};

		viewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE, transferTypes,
				dragListener);

		viewer.addDropSupport(DND.DROP_COPY | DND.DROP_MOVE, transferTypes,
				dropListener);

	}

	private void resizeViewer(final ListViewer viewer) {

		final Control control = viewer.getControl();
		control.setSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		control.getParent().layout();

	}

	public ListViewer getViewer() {
		return viewer;
	}

}
