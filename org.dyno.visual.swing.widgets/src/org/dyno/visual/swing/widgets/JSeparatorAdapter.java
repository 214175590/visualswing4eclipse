
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

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class JSeparatorAdapter extends WidgetAdapter {
	public JSeparatorAdapter() {
		super(null);
	}

	@Override
	protected Component createWidget() {
		JSeparator separator = new JSeparator();
		Dimension size = new Dimension(100, 10);
		separator.setSize(size);
		separator.doLayout();
		separator.validate();
		return separator;
	}

	public CompositeAdapter getParentAdapter() {
		Component me = getWidget();
		Component parent = me.getParent();
		if (parent instanceof JPopupMenu) {
			JPopupMenu popup = (JPopupMenu) parent;
			JMenu jmenu = (JMenu) popup.getInvoker();
			if (jmenu != null)
				return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(jmenu);
		}
		return super.getParentAdapter();
	}

	@Override
	protected Component newWidget() {
		return new JSeparator();
	}
	@Override
	public Class getWidgetClass() {
		return JSeparator.class;
	}
	
}

