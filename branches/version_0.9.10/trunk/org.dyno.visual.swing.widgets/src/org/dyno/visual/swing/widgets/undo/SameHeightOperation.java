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

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class SameHeightOperation extends AbstractOperation {
	private List<WidgetAdapter>selection;
	private List<Integer> boundheight;
	public SameHeightOperation(List<WidgetAdapter>selected) {
		super(Messages.SameHeightOperation_Same_Height);
		selection = new ArrayList<WidgetAdapter>();
		for(WidgetAdapter adapter:selected){
			selection.add(adapter);
		}
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		int y = -1;
		boundheight = new ArrayList<Integer>();
		for (int i=0;i<selection.size();i++) {
			WidgetAdapter childAdapter = selection.get(i);				
			Component child = childAdapter.getWidget();
			if (y == -1){
				boundheight.add(child.getX());
				y = child.getHeight();
			} else {
				Rectangle bounds = child.getBounds();
				boundheight.add(bounds.height);
				bounds.height = y;
				child.setBounds(bounds);
			}
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (int i=0;i<selection.size();i++) {
			WidgetAdapter childAdapter = selection.get(i);				
			Component child = childAdapter.getWidget();
			Rectangle bounds = child.getBounds();
			bounds.height = boundheight.get(i);
			child.setBounds(bounds);
		}
		return Status.OK_STATUS;
	}
}

