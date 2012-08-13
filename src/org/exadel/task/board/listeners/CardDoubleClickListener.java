package org.exadel.task.board.listeners;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.exadel.task.board.dialogs.CardEditDialog;
import org.exadel.task.board.model.Card;

public class CardDoubleClickListener implements IDoubleClickListener {

	@Override
	public void doubleClick(DoubleClickEvent event) {
		
		final ListViewer viewer = (ListViewer) event.getViewer();
		Card card = (Card) ((StructuredSelection) event.getSelection())
				.getFirstElement();

		final Control control = viewer.getControl();
		
		final CardEditDialog dialog = new CardEditDialog(control.getShell(), card);
		dialog.create();
		dialog.open();
		
		viewer.update(card, null);
		
		control.setSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		control.getParent().layout(true, true);
		
	}
}
