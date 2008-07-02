package org.dyno.visual.swing.lnfs.nimbus;

import java.awt.Insets;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JToolBarValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JToolBarValue() {
		put("floatable", true);
		put("visible", true);
		put("borderPainted", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("margin", new Insets(0, 0, 0, 0));
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("requestFocusEnabled", true);
	}
}
