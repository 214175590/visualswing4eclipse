/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.lnfs.windowsxp;

import java.awt.Insets;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJMenuBarValue extends WidgetValue {
	private static final long serialVersionUID = 1L;

	public ClassicJMenuBarValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("borderPainted", true);
		put("opaque", true);
		put("margin", new Insets(0, 0, 0, 0));
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("requestFocusEnabled", true);
	}

}