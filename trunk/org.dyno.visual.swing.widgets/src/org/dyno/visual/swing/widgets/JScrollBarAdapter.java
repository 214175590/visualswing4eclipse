/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JScrollBar;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JScrollBarAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JScrollBarAdapter() {
		super("jScrollBar" + (VAR_INDEX++));
	}

	@Override
	protected Component createWidget() {
		JScrollBar bar = new JScrollBar();
		Dimension size = bar.getPreferredSize();
		bar.setSize(size);
		bar.doLayout();
		bar.validate();
		return bar;
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
		return ((JScrollBar) getWidget()).getValue();
	}

	@Override
	public void setWidgetValue(Object value) {
		((JScrollBar) getWidget()).setValue(((Number) value).intValue());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		int oritention = ((JScrollBar) getWidget()).getOrientation();
		if (oritention == Adjustable.HORIZONTAL) {
			return new Rectangle((w - 40) / 2, 0, 40, 23);
		} else {
			return new Rectangle(0, (h - 23) / 2, w, 23);
		}
	}

	@Override
	protected Component newWidget() {
		return new JScrollBar();
	}
	@Override
	public Class getWidgetClass() {
		return JScrollBar.class;
	}
}