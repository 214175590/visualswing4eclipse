/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class JTextAreaAdapter extends J2DTextComponentAdapter {
	public JTextAreaAdapter() {
		super("jTextArea");
	}

	@Override
	protected JTextComponent createTextComponent() {
		return new JTextArea();
	}

	@Override
	protected Component newWidget() {
		return new JTextArea();
	}
	@Override
	public Class getWidgetClass() {
		return JTextArea.class;
	}
}