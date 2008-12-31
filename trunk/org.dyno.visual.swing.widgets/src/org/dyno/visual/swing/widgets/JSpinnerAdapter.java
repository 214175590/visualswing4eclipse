
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
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JSpinner;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JSpinnerAdapter extends WidgetAdapter {
	public JSpinnerAdapter() {
		super(null);
	}

	@Override
	protected Component createWidget() {
		JSpinner spinner = new JSpinner();
		Dimension size = spinner.getPreferredSize();
		spinner.setSize(size);
		spinner.doLayout();
		spinner.validate();
		return spinner;
	}

	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new IntegerTextEditor();
		}
		return iEditor;
	}

	@Override
	public Object getWidgetValue() {
		return ((JSpinner) getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JSpinner) getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

	@Override
	protected Component newWidget() {
		return new JSpinner();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JSpinner.class;
	}

}

