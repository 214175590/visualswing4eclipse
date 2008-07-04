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
 * RightAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class RightAlignAction extends EditorAction {
	private static String RIGHT_ACTION_ICON = "/icons/right_align.png";

	public RightAlignAction() {
		setId(ALIGNMENT_RIGHT);
		setText("Right Alignment in Column");
		setToolTipText("Right Alignment in Column");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(RIGHT_ACTION_ICON));
		setEnabled(false);
	}
}
