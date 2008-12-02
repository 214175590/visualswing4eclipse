/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJInternalFrameValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJInternalFrameValue() {
		put("defaultCloseOperation", 2);
		put("title", "");
		put("visible", false);
		put("verifyInputWhenFocusTarget", true);
		put("focusable", true);
		put("enabled", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("focusCycleRoot", true);
		put("opaque", true);
	}
}