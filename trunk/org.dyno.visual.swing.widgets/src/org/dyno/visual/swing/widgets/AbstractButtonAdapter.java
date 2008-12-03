
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

package org.dyno.visual.swing.widgets;

import java.awt.Component;

import javax.swing.AbstractButton;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class AbstractButtonAdapter extends WidgetAdapter {

	public AbstractButtonAdapter() {
	}

	@Override
	protected Component createWidget() {
		return null;
	}

	@Override
	protected Component newWidget() {
		return null;
	}

	@Override
	public Class getWidgetClass() {
		return AbstractButton.class;
	}
}

