package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JScrollBar;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.IEditor;

public class TransparentScrollBarEditor implements IEditor {
	private JScrollBar scrollBar;
	public TransparentScrollBarEditor(JScrollBar js){
		scrollBar = js;
	}
	@Override
	public void addChangeListener(ChangeListener l) {
	}

	@Override
	public Component getComponent() {
		return scrollBar;
	}

	@Override
	public Object getValue() {
		return scrollBar.getValue();
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
	}

	@Override
	public void setFocus() {
		scrollBar.requestFocus();
		scrollBar.setToolTipText("Drag scroll bar to adjust its value!");
	}

	@Override
	public void setFont(Font f) {
		scrollBar.setFont(f);
	}

	@Override
	public void setValue(Object v) {
		int value = v==null?0:((Number)v).intValue();
		scrollBar.setValue(value);
	}

	@Override
	public void validateValue() throws Exception {
	}
}