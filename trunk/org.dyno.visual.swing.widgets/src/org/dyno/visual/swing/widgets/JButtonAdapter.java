/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;

import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;

public class JButtonAdapter extends TextWidgetAdapter {
	@Override
	public Class getWidgetClass() {
		return JButton.class;
	}

	@Override
	public IAdapter getParent() {
		JButton jb = (JButton) getWidget();
		DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
		ButtonGroup bg = dbm.getGroup();
		for (InvisibleAdapter invisible : getRootAdapter().getInvisibles()) {
			if (invisible instanceof ButtonGroupAdapter) {
				if (bg == ((ButtonGroupAdapter) invisible).getButtonGroup())
					return invisible;
			}
		}
		return super.getParent();
	}
}