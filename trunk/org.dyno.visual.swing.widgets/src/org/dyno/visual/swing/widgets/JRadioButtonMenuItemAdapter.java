package org.dyno.visual.swing.widgets;

import java.awt.Component;

import javax.swing.JRadioButtonMenuItem;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JRadioButtonMenuItemAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;
	public JRadioButtonMenuItemAdapter(){
		super("jRadioButtonMenuItem" + (VAR_INDEX++));
	}

	@Override
	protected Component createWidget() {
		JRadioButtonMenuItem jmi = new JRadioButtonMenuItem();
		jmi.setText("radio button item");
		jmi.setSize(jmi.getPreferredSize());
		jmi.doLayout();
		return jmi;
	}

	@Override
	protected Component newWidget() {
		return new JRadioButtonMenuItem();
	}

	@Override
	public Component cloneWidget() {
		JRadioButtonMenuItem jmi = (JRadioButtonMenuItem) super.cloneWidget();
		JRadioButtonMenuItem origin = (JRadioButtonMenuItem)getWidget();
		jmi.setText(origin.getText());
		jmi.setSelected(origin.isSelected());
		return jmi;
	}
	
}
