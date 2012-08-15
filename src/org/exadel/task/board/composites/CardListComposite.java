package org.exadel.task.board.composites;

import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.exadel.task.board.dialogs.CardEditDialog;
import org.exadel.task.board.model.*;
import org.exadel.task.board.service.TaskService;

public class CardListComposite extends Composite {

	private final TaskService service = new TaskService();

	private final CardList list;

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
		viewer.setContentProvider(new ObservableListContentProvider() {

		});

		viewer.add(list.getCards().toArray());

		addDoubleClickListener();
		addDragAndDrop();
	}

	private void addDoubleClickListener() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				final Card card = (Card) ((StructuredSelection) event
						.getSelection()).getFirstElement();

				final Control control = viewer.getControl();

				final CardEditDialog dialog = new CardEditDialog(control
						.getShell(), card);
				dialog.create();
				if (dialog.open() == Window.OK) {

					if (service.updateCard(card)) {
						viewer.update(card, null);
					} else {

						final String message = card
								+ " has been deleted in other session";
						MessageDialog.openError(viewer.getControl().getShell(),
								"Error", message);
						viewer.remove(card);
					}

					control.setSize(control.computeSize(SWT.DEFAULT,
							SWT.DEFAULT));
					control.getParent().layout(true, true);
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
				
				if (service.moveCard(card, fromList, toList)) {
					fromViewer.remove(card);
					resizeViewer(fromViewer);

					viewer.add(card);
					resizeViewer(viewer);

					viewer.getControl().getParent().layout(true, true);
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

	}

	public ListViewer getViewer() {
		return viewer;
	}

}
