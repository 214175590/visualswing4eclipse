package org.dyno.visual.swing.widgets.actions;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.dyno.visual.swing.widgets.undo.SetLayoutOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class SetLayoutAction extends Action {
	private IConfigurationElement config;
	private JPanelAdapter jpaneladapter;
	private String oldClassname;
	private String newClassname;

	public SetLayoutAction(JPanelAdapter adapter, IConfigurationElement config) {
		super(config.getAttribute("name"), AS_RADIO_BUTTON);
		this.config = config;
		this.jpaneladapter = adapter;
		JPanel jpanel = (JPanel) jpaneladapter.getWidget();
		LayoutManager layout = jpanel.getLayout();
		if (layout == null)
			oldClassname = "null";
		newClassname = config.getAttribute("layoutClass");
	}

	public void run() {
		if (!newClassname.equals(oldClassname)) {
			IUndoableOperation operation = new SetLayoutOperation(config,
					jpaneladapter);
			operation.addContext(jpaneladapter.getUndoContext());
			IOperationHistory operationHistory = PlatformUI.getWorkbench()
					.getOperationSupport().getOperationHistory();
			try {
				operationHistory.execute(operation, null, null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}