package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJTextAreaValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJTextAreaValue() {
		put("editable", true);
		put("tabSize", 8);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);		
	}
}