package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJMenuValue extends WidgetValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XpJMenuValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("horizontalAlignment", 10);
		put("contentAreaFilled", true);
		put("focusable", true);
		put("enabled", true);
		put("iconTextGap", 4);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("opaque", true);
		put("rolloverEnabled", true);
	}
}