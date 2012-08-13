package org.exadel.task.board.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;

public class ExitHandler {

	@Execute
	public void execute(IWorkbench workbench) {
		workbench.close();
	}

	// Technically not needed
	// will default to true
	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
