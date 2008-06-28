package org.dyno.visual.swing.base;

import java.awt.Component;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.Editor;

public class LabelEditor extends TextField implements Editor, ActionListener {
	private static final long serialVersionUID = -4403435758517308113L;
	private ArrayList<ChangeListener> listeners;

	public LabelEditor() {
		listeners = new ArrayList<ChangeListener>();
		addActionListener(this);
	}

	@Override
	public void addChangeListener(ChangeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public Object getValue() {
		return getText();
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		if (listeners.contains(l))
			listeners.remove(l);
	}

	@Override
	public void setValue(Object v) {
		setText(v == null ? "" : v.toString());
	}

	@Override
	public void setFocus() {
		int count = 0;
		while (!isFocusOwner() && count < FOCUSE_REQUEST_LIMIT) {
			requestFocus();
			count++;
		}
		selectAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ChangeEvent ce = new ChangeEvent(e.getSource());
		fireStateChanged(ce);
	}

	private void fireStateChanged(ChangeEvent e) {
		for (ChangeListener l : listeners) {
			l.stateChanged(e);
		}
	}

	private static final int FOCUSE_REQUEST_LIMIT = 2;

	@Override
	public void validateValue() throws Exception {
	}
}
