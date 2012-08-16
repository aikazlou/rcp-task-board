package org.exadel.task.board.parts;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.exadel.task.board.model.*;
import org.exadel.task.board.service.*;
import org.exadel.task.board.composites.CardListComposite;

public class MainPart {

	private final TaskService service = new TaskService();

	@PostConstruct
	public void createControls(Composite parent) {

		final List<CardList> lists = service.getLists();

		final RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		parent.setLayout(layout);

		for (CardList list : lists) {

			new CardListComposite(parent, SWT.BORDER, list);
		}
	}

}