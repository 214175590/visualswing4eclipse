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

package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
/**
 * 
 * MiddleAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class MiddleAlignAction extends EditorAction {
	private static String MIDDLE_ACTION_ICON = "/icons/middle_align.png"; //$NON-NLS-1$

	public MiddleAlignAction() {
		setId(ALIGNMENT_MIDDLE);
		setText(Messages.MiddleAlignAction_Center_Alignment);
		setToolTipText(Messages.MiddleAlignAction_Center_Alignment);
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(MIDDLE_ACTION_ICON));
		setEnabled(false);
	}
}

