package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JCheckBoxValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JCheckBoxValue() {
		put("borderPainted", false);
		put("horizontalAlignment", 10);
		put("opaque", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("focusPainted", true);
		put("contentAreaFilled", true);
		put("focusable", true);
		put("enabled", true);
		put("iconTextGap", 4);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("rolloverEnabled", false);		
	}
}
