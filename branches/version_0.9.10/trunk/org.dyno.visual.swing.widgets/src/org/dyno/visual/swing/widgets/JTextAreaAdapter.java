
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

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

@SuppressWarnings("unchecked")
public class JTextAreaAdapter extends J2DTextComponentAdapter {
	public JTextAreaAdapter() {
		super(null);
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

