/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class ContainerAdapter extends WidgetAdapter {

	@Override
	protected JComponent createWidget() {
		return null;
	}

	@Override
	protected JComponent newWidget() {
		return null;
	}

}
