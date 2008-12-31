
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

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class DimensionCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		String string = (String) value;
		string = string.trim();
		if (string.length() < 5)
			return "Incorrect format: (width, height)";
		char c = string.charAt(0);
		if (c != '(')
			return "Incorrect format: (width, height)";
		c = string.charAt(string.length() - 1);
		if (c != ')')
			return "Incorrect format: (width, height)";
		string=string.substring(1, string.length()-1);
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (width, height)";
		String token = tokenizer.nextToken().trim();
		try{
			int w = Integer.parseInt(token);
			if(w<0)
				return "width must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (width, height)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (width, height)";
		token = tokenizer.nextToken().trim();
		try{
			int h = Integer.parseInt(token);
			if(h<0)
				return "height must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (width, height)";
		}
		return null;
	}
}
