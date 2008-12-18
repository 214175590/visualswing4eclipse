
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

public class InsetsCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		String string = (String) value;
		string = string.trim();
		if (string.length() < 9)
			return "Incorrect format: (top, left, bottom, right)";
		char c = string.charAt(0);
		if (c != '(')
			return "Incorrect format: (top, left, bottom, right)";
		c = string.charAt(string.length() - 1);
		if (c != ')')
			return "Incorrect format: (top, left, bottom, right)";
		string=string.substring(1, string.length()-1);
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		String token = tokenizer.nextToken().trim();
		try{
			int x = Integer.parseInt(token);
			if(x<0)
				return "top gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		token = tokenizer.nextToken().trim();
		try{
			int y = Integer.parseInt(token);
			if(y<0)
				return "left gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		token = tokenizer.nextToken().trim();
		try{
			int w = Integer.parseInt(token);
			if(w<0)
				return "bottom gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		token = tokenizer.nextToken().trim();
		try{
			int h = Integer.parseInt(token);
			if(h<0)
				return "right gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		return null;
	}
}

