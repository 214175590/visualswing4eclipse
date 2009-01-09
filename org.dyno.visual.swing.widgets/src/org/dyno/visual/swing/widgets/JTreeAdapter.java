
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
import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TreeModelEditor;

public class JTreeAdapter extends ComplexWidgetAdapter {
	public JTreeAdapter() {
		super(null);
	}

	protected Component createWidget() {
		JTree jtc = new JTree();
		jtc.setSize(getInitialSize());
		jtc.doLayout();
		jtc.validate();
		return jtc;
	}

	@Override
	protected Dimension getInitialSize() {
		return new Dimension(150, 200);
	}

	@Override
	public IEditor getEditorAt(int x, int y) {
		CompositeAdapter parent = getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane)
			return new TreeModelEditor((JScrollPane) parent.cloneWidget());
		else
			return null;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		CompositeAdapter parent = getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane) {
			Rectangle bounds = parent.getWidget().getBounds();
			bounds.x = 0;
			bounds.y = 0;
			return bounds;
		}
		Rectangle bounds = getWidget().getBounds();
		bounds.x = 0;
		bounds.y = 0;
		return bounds;
	}

	@Override
	public Object getWidgetValue() {
		JTree tree = (JTree) getWidget();
		return tree.getModel();
	}

	@Override
	public void setWidgetValue(Object value) {
		JTree tree = (JTree) getWidget();
		tree.setModel((TreeModel) value);
	}

	@Override
	protected Component newWidget() {
		return new JTree();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JTree.class;
	}
}

