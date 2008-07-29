/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.VerticalScrollBarPolicyItems;

public class VerticalScrollBarPolicyRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public VerticalScrollBarPolicyRenderer() {
		super(new VerticalScrollBarPolicyItems());
	}

}

