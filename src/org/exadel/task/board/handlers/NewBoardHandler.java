package org.exadel.task.board.handlers;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.window.Window;

public class NewBoardHandler {

	@Execute
	public void execute(MWindow window) {
		System.out.println("Called");
	}

}