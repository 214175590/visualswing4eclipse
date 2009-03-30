package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Rectangle;

public class JPasswordFieldEditorAdapter extends TextWidgetEditorAdapter {

	@Override
	public Rectangle getEditorBounds() {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}
}
