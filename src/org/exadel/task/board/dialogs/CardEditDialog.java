package org.exadel.task.board.dialogs;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.exadel.task.board.model.Card;

public class CardEditDialog extends TitleAreaDialog {

	private final Card card;
	private Text title;
	private Text type;
	private Text content;

	public CardEditDialog(Shell parentShell, Card card) {
		super(parentShell);
		this.card = card;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Card editor");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		
		final GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		final Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Title");

		title = new Text(parent, SWT.BORDER);
		title.setText(card.getTitle());
		title.setLayoutData(gridData);

		final Label label2 = new Label(parent, SWT.NONE);
		label2.setText("Type");

		type = new Text(parent, SWT.BORDER);
		type.setText(card.getType());
		type.setLayoutData(gridData);
		
		final Label label3 = new Label(parent, SWT.NONE);
		label3.setText("Content");
		
		content = new Text(parent, SWT.BORDER | SWT.MULTI);
		content.setText(card.getContent());
		content.setLayoutData(gridData);
		
		return parent;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		final GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "Ok", true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;
		if (title.getText().length() == 0) {
			setErrorMessage("Please maintain the title");
			valid = false;
		}
		if (type.getText().length() == 0) {
			setErrorMessage("Please maintain the type");
			valid = false;
		}
		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// Coyy textFields because the UI gets disposed
	// and the Text Fields are not accessible any more.
	private void saveInput() {
		card.setTitle(title.getText());
		card.setType(type.getText());
		card.setContent(content.getText());
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public Card getCard() {
		return card;
	}
	
}