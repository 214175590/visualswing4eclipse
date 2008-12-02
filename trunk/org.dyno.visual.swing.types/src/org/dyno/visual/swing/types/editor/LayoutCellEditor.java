/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.awt.BorderLayout;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

public class LayoutCellEditor extends ComboBoxCellEditor {
	public LayoutCellEditor(Composite parent) {
		super.create(parent);
		super.setItems(new String[] { "null", "BorderLayout" });
	}

	@Override
	protected Object doGetValue() {
		Integer index = (Integer) super.doGetValue();
		int i = index.intValue();
		if (i == 1) {
			return new BorderLayout();
		}
		return null;
	}

	@Override
	protected void doSetValue(Object value) {
		if (value == null)
			super.doSetValue(Integer.valueOf(0));
		else if (value instanceof BorderLayout) {
			super.doSetValue(Integer.valueOf(1));
		}
	}

}