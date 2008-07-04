/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;
/**
 * 
 * SameWidthAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class SameWidthAction extends EditorAction {
	private static String SAME_WIDTH_ACTION_ICON = "/icons/same_width.png";

	public SameWidthAction() {
		setId(SAME_WIDTH);
		setText("Same Width");
		setToolTipText("Same Width");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(SAME_WIDTH_ACTION_ICON));
		setEnabled(false);
	}
}
