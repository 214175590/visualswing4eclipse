/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;
/**
 * 
 * WidgetSelection
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class WidgetSelection extends ArrayList<Component> implements IStructuredSelection {
	private static final long serialVersionUID = -7010445264838695570L;

	public WidgetSelection(Component comp) {
		addSelection(comp);
	}

	private void addSelection(Component comp) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
		if (adapter == null)
			return;
		if (adapter.isSelected())
			add(comp);
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int count = compositeAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				comp = compositeAdapter.getChild(i);
				addSelection(comp);
			}
		}
	}

	@Override
	public Object getFirstElement() {
		return isEmpty()?null:get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List toList() {
		return this;
	}
}