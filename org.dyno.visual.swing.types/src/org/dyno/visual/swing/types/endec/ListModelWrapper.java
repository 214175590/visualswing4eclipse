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

package org.dyno.visual.swing.types.endec;

import javax.swing.ListModel;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class ListModelWrapper implements ICodeGen {

	public ListModelWrapper() {
	}

	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		return "listModel";
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return null;
		String className = imports.addImport("javax.swing.DefaultListModel");
		StringBuilder builder = new StringBuilder();
		builder.append(className+" listModel = new " + className + "();\n");
		ListModel model = (ListModel) value;
		for (int i = 0; i < model.getSize(); i++) {
			Object obj = model.getElementAt(i);
			if (obj == null) {
				builder.append("listModel.addElement(null);\n");
			} else {
				TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(obj
						.getClass());
				if (adapter != null && adapter.getCodegen() != null) {
					builder
							.append("listModel.addElement("+adapter.getCodegen()
									.getJavaCode(obj, imports)+");\n");
				} else {
					builder.append("listModel.addElement("+obj.toString()+");\n");
				}
			}
		}
		return builder.toString();
	}
}

