
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

package org.dyno.visual.swing.borders;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.borders.action.SoftBevelBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

@SuppressWarnings("unchecked")
public class SoftBevelBorderAdapter extends BorderAdapter {
	
	public Class getBorderClass() {
		return SoftBevelBorder.class;
	}

	
	public String getBorderName() {
		return "SoftBevelBorder";
	}

	
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty bevelTypeProperty = new FieldProperty("bevelType", "bevelType", SoftBevelBorder.class, new ItemProviderLabelProviderFactory(new BevelBorderTypeItems()), new ItemProviderCellEditorFactory(new BevelBorderTypeItems()));
		FieldProperty highlightOuterProperty = new FieldProperty("highlightOuter", "highlightOuter", SoftBevelBorder.class);
		FieldProperty highlightInnerProperty = new FieldProperty("highlightInner", "highlightInner", SoftBevelBorder.class);
		FieldProperty shadowInnerProperty = new FieldProperty("shadowInner", "shadowInner", SoftBevelBorder.class);
		FieldProperty shadowOuterProperty = new FieldProperty("shadowOuter", "shadowOuter", SoftBevelBorder.class);
		return new IWidgetPropertyDescriptor[] {bevelTypeProperty, 
				highlightOuterProperty, 
				highlightInnerProperty, 
				shadowInnerProperty,
				shadowOuterProperty
			};
	}

	
	public IAction getContextAction(JComponent widget) {
		return new SoftBevelBorderSwitchAction(widget);
	}

	
	public Object newInstance(Object bean) {
		return new SoftBevelBorder(BevelBorder.LOWERED);
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		String strBf=imports.addImport("javax.swing.border.SoftBevelBorder");
		SoftBevelBorder bevelBorder = (SoftBevelBorder)value;
		int type = bevelBorder.getBevelType();
		Color highlightInnerColor = bevelBorder.getHighlightInnerColor();
		Color highlightOuterColor = bevelBorder.getHighlightOuterColor();
		Color shadowInnerColor = bevelBorder.getShadowInnerColor();
		Color shadowOuterColor = bevelBorder.getShadowOuterColor();
		StringBuilder builder = new StringBuilder();
		builder.append("new "+strBf+"(");
		builder.append(encodeValue(new BevelBorderTypeItems(), type, imports));
		builder.append(", ");
		builder.append(encodeValue(highlightOuterColor, imports));
		builder.append(", ");
		builder.append(encodeValue(highlightInnerColor, imports));
		builder.append(", ");
		builder.append(encodeValue(shadowOuterColor, imports));
		builder.append(", ");
		builder.append(encodeValue(shadowInnerColor, imports));
		builder.append(")");
		return builder.toString();
	}

}

