
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

package org.dyno.visual.swing.borders;

import javax.swing.JComponent;

import org.dyno.visual.swing.borders.action.NullBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

@SuppressWarnings("unchecked")
public class NullBorderAdapter extends BorderAdapter {

	@Override
	public IAction getContextAction(JComponent widget) {
		return new NullBorderSwitchAction(widget);
	}

	@Override
	public Class getBorderClass() {
		return null;
	}

	@Override
	public String getBorderName() {
		return "null";
	}

	@Override
	public Object newInstance(Object bean) {
		return null;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		return "null";
	}
}

