
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

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.types.endec.SpinnerModelWrapper;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class SpinnerModelEditor extends SpinnerModelWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new SpinnerModelCellEditor(parent);
	}

	
	public Object decodeValue(Object value) {
		return value;
	}

	
	public Object encodeValue(Object value) {
		return value;
	}
}

