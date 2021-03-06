package org.dyno.visual.swing.widgets.editoradapter;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JMenu;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.IEditor;

public class JMenuEditorAdapter extends CompositeEdtiorAdapter {

	private LabelEditor editor;

	@Override
	public IEditor getEditorAt() {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		Component me = adaptable.getWidget();
		JMenu jmi = (JMenu) me;
		return jmi.getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		Component me = adaptable.getWidget();
		JMenu jmi = (JMenu) me;
		jmi.setText(value == null ? "" : value.toString()); //$NON-NLS-1$
	}

	@Override
	public Rectangle getEditorBounds() {
		int w = adaptable.getWidget().getWidth();
		int h = adaptable.getWidget().getHeight();
		Component widget = adaptable.getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		Component me = adaptable.getWidget();
		JMenu jmi = (JMenu) me;
		int fw = fm.stringWidth(jmi.getText()) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}

	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;
}
