/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.widgets.Composite;

public class BooleanEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	public BooleanEditor() {
	}

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new CheckboxCellEditor(parent);
	}

	@Override
	public Object decodeValue(Object value) {
		return value;
	}

	@Override
	public Object encodeValue(Object value) {
		return value;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "false";
		return value.toString();
	}
}