/*
 * ComboBoxModelWrapper.java
 *
 * Created on 2007-8-28, 0:16:54
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.types.endec;

import javax.swing.ListModel;

import org.dyno.visual.swing.plugin.spi.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.TypeAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class ListModelWrapper implements ICodeGen {

	public ListModelWrapper() {
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		return "listModel";
	}

	@Override
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
				if (adapter != null && adapter.getEndec() != null) {
					builder
							.append("listModel.addElement("+adapter.getEndec()
									.getJavaCode(obj, imports)+");\n");
				} else {
					builder.append("listModel.addElement("+obj.toString()+");\n");
				}
			}
		}
		return builder.toString();
	}
}