package org.exadel.task.board.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

public class RemoveBoardHandler {

	@Execute
	public void execute() {
		System.out.println("Called");
	}

	// Technically not needed
	// will default to true
	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
