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

package org.dyno.visual.swing.undo;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class CutOperation extends AbstractOperation {
	private List<Component> selection;
	private List<CompositeAdapter>parents;
	public CutOperation(List<Component> selection) {
		super(Messages.CutOperation_Cut_Components);
		this.selection = selection;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		parents = new ArrayList<CompositeAdapter>();
		for (Component child : selection) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			CompositeAdapter parentAdapter=adapter.getParentAdapter();
			boolean success = parentAdapter.removeChild(child);
			if (success)
				parentAdapter.setDirty(true);
			parents.add(parentAdapter);
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for(int i=0;i<selection.size();i++){
			Component child = selection.get(i);
			CompositeAdapter parentAdapter = parents.get(i);
			parentAdapter.addChild(child);
			parentAdapter.setDirty(true);
			parentAdapter.repaintDesigner();
		}
		return Status.OK_STATUS;
	}

}

