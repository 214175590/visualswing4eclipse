/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;

public class MetaJScrollBarValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJScrollBarValue() {
		put("blockIncrement", 10);
		put("maximum", 100);
		put("orientation", 1);
		put("unitIncrement", 1);
		put("visibleAmount", 10);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("doubleBuffered", false);
		put("requestFocusEnabled", false);
	}
}