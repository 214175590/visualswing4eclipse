

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

import javax.swing.ComboBoxModel;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class ComboBoxModelWrapper implements ICodeGen {

	public ComboBoxModelWrapper() {
	}
	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		String className = imports.addImport("javax.swing.DefaultComboBoxModel");
		StringBuilder builder=new StringBuilder();
		builder.append("new "+className+"(new Object[]{\n");
		ComboBoxModel model = (ComboBoxModel)value;
		for(int i=0;i<model.getSize();i++){
			if(i!=0)
				builder.append(",");
			Object obj=model.getElementAt(i);
			if(obj==null){
				builder.append("null");
			}else{
				TypeAdapter adapter=ExtensionRegistry.getTypeAdapter(obj.getClass());
				if(adapter!=null&&adapter.getCodegen()!=null){
					builder.append(adapter.getCodegen().getJavaCode(obj, imports));
				}else{
					builder.append(obj.toString());
				}				
			}
		}
		builder.append("})");
		return builder.toString();
	}
	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}

