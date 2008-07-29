/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.layout;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;

public class FlowLayoutAlignmentRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public FlowLayoutAlignmentRenderer() {
		super(new FlowLayoutAlignmentItems());
	}

}
