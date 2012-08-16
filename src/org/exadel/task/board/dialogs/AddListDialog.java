
package org.exadel.task.board.dialogs;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.exadel.task.board.model.*;

public class AddListDialog extends TitleAreaDialog {

	private final CardList list;

	private Text title;


	public AddListDialog(Shell parentShell, CardList list) {
		super(parentShell);
		this.list = list;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Create List");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		final GridData labelGridData = new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING);

		GridData textGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		textGridData.grabExcessHorizontalSpace = true;

		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		final Label titleLabel = new Label(parent, SWT.NONE);
		titleLabel.setText("Title");
		titleLabel.setLayoutData(labelGridData);

		title = new Text(parent, SWT.BORDER);
		title.setText(list.getTitle());
		title.setLayoutData(textGridData);

		

		
	

		return parent;
	}

	
	


	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		((GridLayout) parent.getLayout()).numColumns++;

		final GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);

		createOkButton(parent, OK, "Ok", true);

		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);

		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {

		final Button button = new Button(parent, SWT.PUSH);
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
		
		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void saveInput() {
		list.setTitle(title.getText());
		
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public CardList getList() {
		return list;
	}

}