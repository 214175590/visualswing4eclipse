package org.dyno.visual.swing.lnfs;

import java.awt.Component;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.dyno.visual.swing.lnfs.windows.XpJButtonValue;
import org.dyno.visual.swing.lnfs.windows.XpJCheckBoxValue;
import org.dyno.visual.swing.lnfs.windows.XpJComboBoxValue;
import org.dyno.visual.swing.lnfs.windows.XpJLabelValue;
import org.dyno.visual.swing.lnfs.windows.XpJPanelValue;
import org.dyno.visual.swing.lnfs.windows.XpJRadioButtonValue;
import org.dyno.visual.swing.lnfs.windows.XpJToggleButtonValue;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;

public class WindowsLookAndFeelAdapter implements ILookAndFeelAdapter {
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> xpValues;
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> classicValues;

	@SuppressWarnings("unchecked")
	public WindowsLookAndFeelAdapter() {
		xpValues = new HashMap<Class, WidgetValue>();
		classicValues = new HashMap<Class, WidgetValue>();
		
		xpValues.put(JLabel.class, new XpJLabelValue());
		xpValues.put(JPanel.class, new XpJPanelValue());
		xpValues.put(JButton.class, new XpJButtonValue());
		xpValues.put(JToggleButton.class, new XpJToggleButtonValue());
		xpValues.put(JCheckBox.class, new XpJCheckBoxValue());
		xpValues.put(JRadioButton.class,new XpJRadioButtonValue());
		xpValues.put(JComboBox.class, new XpJComboBoxValue());
	}

	private static boolean isXP() {
		Boolean xp = (Boolean) Toolkit.getDefaultToolkit().getDesktopProperty(
				"win.xpstyle.themeActive");
		return xp != null && xp.booleanValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getDefaultValue(Class widgetClass, String propertyName) {
		WidgetValue widgetValue = getWidgetValue(widgetClass);
		if (widgetValue != null)
			return widgetValue.get(propertyName);
		else
			return null;
	}
	@SuppressWarnings("unchecked")
	private WidgetValue getWidgetValue(Class widgetClass){
		WidgetValue widgetValue;
		if (isXP()) {
			widgetValue = xpValues.get(widgetClass);
		} else {
			widgetValue = classicValues.get(widgetClass);
		}
		if(widgetValue==null&&widgetClass!=Component.class){
			return getWidgetValue(widgetClass.getSuperclass());
		}else{
			return widgetValue;
		}
	}
}
