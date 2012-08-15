package org.exadel.task.board.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.exadel.task.board.model.*;

public class CommentComposite extends Composite {
	
	private final Comment comment;
	private Text content;

	public CommentComposite(Composite parent, int style, Comment comment) {
		super(parent, style);
		
		this.comment = comment;

		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		this.setLayout(layout);
		
		createArea();
	}
	
	private void createArea() {
		
		final GridData gridData = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL);
		gridData.grabExcessHorizontalSpace = true;
		
		final Label label = new Label(this, SWT.NONE);
		label.setText(comment.getUser().getName());

		content = new Text(this, SWT.WRAP | SWT.BORDER
				| SWT.H_SCROLL);
		content.setBackground(new Color(null, 255, 255, 255));
		content.setText(comment.getContent());

		content.setLayoutData(gridData);		
		
	}
	
	public Comment getComment() {
		comment.setContent(content.getText());
		return comment;
	}
	
	public void setEditable(boolean editable) {
		content.setEditable(editable);
	}

}
