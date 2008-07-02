package org.dyno.visual.swing.plugin.spi;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class VarNameDialog extends Dialog {
	private String message;
	private String input;
	private Text text;
	protected VarNameDialog(Shell parentShell) {
		super(parentShell);
	}
	public void setPromptMessage(String message){
		this.message = message;
	}
	public void setInput(String input){
		this.input = input;
	}
	public String getInput(){
		return this.input;
	}
	
	@Override
	protected void okPressed() {
		this.input = text.getText();
		super.okPressed();
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		super.getShell().setText("Change variable name");
		Composite area = (Composite) super.createDialogArea(parent);
		Label label = new Label(area, SWT.NONE);
		label.setText(message);
		GridData data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData(data);
		text = new Text(area, SWT.SINGLE|SWT.BORDER);
		text.setText(input);
		text.selectAll();
		text.setFocus();
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		text.setLayoutData(data);
		return area;
	}

}
