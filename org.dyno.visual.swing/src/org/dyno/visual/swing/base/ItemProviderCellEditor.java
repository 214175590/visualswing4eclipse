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

package org.dyno.visual.swing.base;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;
/**
 * 
 * ItemProviderCellEditor
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ItemProviderCellEditor extends ComboBoxCellEditor {
	public ItemProviderCellEditor(Composite parent, ItemProvider provider){
		Item[]items=provider.getItems();
		String[]names=new String[items.length];
		for(int i=0;i<items.length;i++){
			names[i]=items[i].getName();
		}
		create(parent);
		setItems(names);
		setValidator(new ItemProviderCellEditorValidator(provider));
	}
}

