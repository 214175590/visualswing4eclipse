/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets.undo;

import java.util.List;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.IRenamingListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.ButtonGroupAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Shell;

public class ButtonGroupRenamingOperation extends AbstractOperation {
	private WidgetAdapter adapter;
	private ICellEditorValidator validator;
	private String lastName;
	private String lastLastName;
	private ButtonGroupAdapter group;

	public ButtonGroupRenamingOperation(WidgetAdapter adapter, ButtonGroupAdapter group) {
		super(Messages.ButtonGroupRenamingOperation_Change_Var);
		this.adapter = adapter;
		this.validator = new BeanNameValidator(adapter);
		this.group = group;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		while (true) {
			Shell parent = JavaUtil.getEclipseShell();			
			ButtonGroupNameDialog dialog = new ButtonGroupNameDialog(parent);
			dialog.setPromptMessage(Messages.ButtonGroupRenamingOperation_New_Var_Name);
			dialog.setInput(group.getName());
			if (dialog.open() == Dialog.OK) {
				String name = dialog.getInput();
				String message = validator.isValid(name);
				if (message != null) {
					MessageDialog.openError(parent, Messages.ButtonGroupRenamingOperation_Invalid_ID, message);
				} else {
					this.lastName = group.getName();
					this.lastLastName = group.getLastName();
					group.setName(name);
					group.setLastName(lastName);
					adapter.lockDesigner();
					List<IRenamingListener> listeners = ExtensionRegistry.getRenamingListeners();
					for(IRenamingListener listener:listeners){
						listener.adapterRenamed(adapter.getCompilationUnit(), group);
					}
					adapter.addNotify();
					break;
				}
			} else
				break;
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		group.setName(lastName);
		group.setLastName(lastLastName);
		adapter.addNotify();
		adapter.lockDesigner();
		List<IRenamingListener> listeners = ExtensionRegistry.getRenamingListeners();
		for(IRenamingListener listener:listeners){
			listener.adapterRenamed(adapter.getCompilationUnit(), group);
		}
		return Status.OK_STATUS;
	}

}

