package org.exadel.task.board.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.databinding.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.exadel.task.board.listeners.CardDoubleClickListener;
import org.exadel.task.board.model.*;
import org.exadel.task.board.controller.*;

public class MainPart {

	private final CardDoubleClickListener cardDoubleClickListener = new CardDoubleClickListener();
	private final DataManager manager = new DataManager();
	private final Map<ListViewer, Integer> boardView = new HashMap<>();

	private Card movedCard;
	private ListViewer draggedViewer;

	@PostConstruct
	public void createControls(Composite parent) {

		final List<CardList> lists = manager.getCardLists();

		final GridLayout layout = new GridLayout();
		layout.numColumns = lists.size();
		parent.setLayout(layout);

		for (final CardList list : lists) {
			final Label label = new Label(parent, SWT.CENTER);
			label.setText(list.getTitle());
		}

		for (CardList list : lists) {
			final GridData gridData = new GridData();
			gridData.verticalAlignment = GridData.BEGINNING;

			final ListViewer viewer = new ListViewer(parent, SWT.NONE);
			viewer.getControl().setLayoutData(gridData);

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

			addViewerListeners(viewer);

			boardView.put(viewer, list.getId());
		}

	}

	private void addViewerListeners(final ListViewer viewer) {
		addDragAndDrop(viewer);
		viewer.addDoubleClickListener(cardDoubleClickListener);
	}

	private void resizeViewer(final ListViewer viewer) {

		final Control control = viewer.getControl();

		control.setSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}

	private void addDragAndDrop(final ListViewer viewer) {

		final Transfer[] transferTypes = new Transfer[] { TextTransfer
				.getInstance() };

		final DragSourceListener dragListener = new DragSourceAdapter() {

			@Override
			public void dragSetData(DragSourceEvent event) {
				movedCard = (Card) ((StructuredSelection) viewer.getSelection())
						.getFirstElement();
				event.data = movedCard.toString();
				draggedViewer = viewer;
			}

		};

		final DropTargetListener dropListener = new DropTargetAdapter() {

			@Override
			public void drop(DropTargetEvent event) {
				final CardList fromList = manager.getCardList(boardView
						.get(draggedViewer));
				final CardList toList = manager.getCardList(boardView
						.get(viewer));
				if (manager.moveCard(movedCard, fromList, toList)) {
					draggedViewer.remove(movedCard);
					resizeViewer(draggedViewer);
					viewer.add(movedCard);
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

}