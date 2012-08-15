package org.exadel.task.board.dialogs;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.exadel.task.board.composites.CommentComposite;
import org.exadel.task.board.model.*;

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
		title.setText(card.getTitle());
		title.setLayoutData(textGridData);

		final Label typeLabel = new Label(parent, SWT.NONE);
		typeLabel.setText("Type");
		typeLabel.setLayoutData(labelGridData);

		type = new Text(parent, SWT.BORDER);
		type.setText(card.getType());
		type.setLayoutData(textGridData);

		final Label contentLabel = new Label(parent, SWT.NONE);
		contentLabel.setText("Content");
		contentLabel.setLayoutData(labelGridData);

		content = new Text(parent, SWT.WRAP | SWT.BORDER);
		content.setText(card.getContent());
		textGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		textGridData.grabExcessHorizontalSpace = true;
		textGridData.heightHint = content.computeSize(SWT.DEFAULT, SWT.DEFAULT).y * 3;
		content.setLayoutData(textGridData);

		setCommentGroup(parent);

		return parent;
	}

	private void setCommentGroup(Composite parent) {

		final GridData groupData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		groupData.grabExcessHorizontalSpace = true;
		groupData.horizontalSpan = 3;

		final GridData compositeData = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL);
		compositeData.grabExcessHorizontalSpace = true;

		final Group group = new Group(parent, SWT.NONE);
		group.setText("Comments");

		final GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 1;
		group.setLayout(groupLayout);

		for (Comment comment : card.getComments()) {
			createDeleteComment(group, comment).setLayoutData(compositeData);
		}
		createAddComment(group).setLayoutData(compositeData);

		group.setLayoutData(groupData);
	}
	
	private Composite createDeleteComment(Composite parent, Comment comment) {
		
		final CommentComposite composite = new CommentComposite(parent,
				SWT.NONE, comment);
		composite.setEditable(false);
		createDeleteButton(composite);
		return composite;
	}
	
	private Composite createAddComment(Composite parent) {

		final CommentComposite composite = new CommentComposite(parent,
				SWT.NONE, new Comment(card.getUser(), ""));
		createAddButton(composite);
		return composite;
	}

	private void createDeleteButton(final CommentComposite parent) {

		final Button deleteButton = new Button(parent, SWT.PUSH);
		deleteButton.setText("Delete");
		deleteButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				card.removeComment(parent.getComment());
				parent.dispose();
			}
			
		});
		deleteButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	}

	private void createAddButton(final CommentComposite parent) {

		final Button addButton = new Button(parent, SWT.PUSH);
		addButton.setText("Add comment");
		addButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				card.addComment(parent.getComment());
				parent.dispose();
				
				createDeleteComment(parent.getParent(), parent.getComment());
				createAddComment(parent.getParent());
			}
			
		});
		addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
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