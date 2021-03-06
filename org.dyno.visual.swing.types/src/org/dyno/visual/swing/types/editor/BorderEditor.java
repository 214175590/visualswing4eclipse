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

package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class BorderEditor implements ICellEditorFactory {

	public BorderEditor() {
		super();
	}

	
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new BorderCellEditor(bean, parent);
	}

	
	public Object decodeValue(Object value) {
		return value;
	}

	
	public Object encodeValue(Object value) {
		return value;
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return null;
		BorderAdapter adapter = BorderAdapter
				.getBorderAdapter(value.getClass());
		return adapter.getInitJavaCode(value, imports);
	}

	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		BorderAdapter adapter = BorderAdapter
				.getBorderAdapter(value.getClass());
		if (adapter != null)
			return adapter.getJavaCode(value, imports);
		else
			return "null";
	}
}
