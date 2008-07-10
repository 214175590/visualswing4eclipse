package org.dyno.visual.swing.widgets.undo;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class KeyDownOperation extends MoveOperation {

	public KeyDownOperation(WidgetAdapter adapter) {
		super(adapter);
	}

	@Override
	protected int getStepSize() {
		return 5;
	}

	@Override
	protected boolean isVertical() {
		return true;
	}

	@Override
	protected String getName() {
		return "Move Down";
	}

}
