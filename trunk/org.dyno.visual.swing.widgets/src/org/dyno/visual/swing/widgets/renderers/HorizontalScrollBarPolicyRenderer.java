package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.HorizontalScrollBarPolicyItems;

public class HorizontalScrollBarPolicyRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public HorizontalScrollBarPolicyRenderer() {
		super(new HorizontalScrollBarPolicyItems());
	}

}
