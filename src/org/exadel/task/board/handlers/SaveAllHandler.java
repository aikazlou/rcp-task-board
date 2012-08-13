package org.exadel.task.board.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

public class SaveAllHandler {

	@Execute
	public void execute() {
		System.out.println("Save All handler called");
	}

}
