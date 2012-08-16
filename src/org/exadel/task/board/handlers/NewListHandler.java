package org.exadel.task.board.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.exadel.task.board.service.TaskService;
import org.exadel.task.board.composites.CardListComposite;
import org.exadel.task.board.dialogs.AddListDialog;
import org.exadel.task.board.model.CardList;

@SuppressWarnings("restriction")
public class NewListHandler {
	@Execute
	public void execute(Composite parent) {

		System.out.println("new list called");

		TaskService service = new TaskService();

		CardList list = new CardList(service.getUser("admin"), "New List");
		service.createList(list);

		final AddListDialog dialog = new AddListDialog(parent.getShell(), list);

		dialog.create();
		if (dialog.open() == Window.OK) {

			new CardListComposite(parent, SWT.BORDER, list);
			
			parent.layout();
		}
	}

}