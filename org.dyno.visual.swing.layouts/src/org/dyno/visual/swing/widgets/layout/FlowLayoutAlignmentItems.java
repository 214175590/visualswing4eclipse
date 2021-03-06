/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets.layout;

import java.awt.FlowLayout;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class FlowLayoutAlignmentItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { 
		new Item("LEADING", FlowLayout.LEADING, "java.awt.FlowLayout.LEADING"),
		new Item("LEFT", FlowLayout.LEFT, "java.awt.FlowLayout.LEFT"),
		new Item("CENTER", FlowLayout.CENTER, "java.awt.FlowLayout.CENTER"),
		new Item("RIGHT", FlowLayout.RIGHT, "java.awt.FlowLayout.RIGHT"),
		new Item("TRAILING", FlowLayout.TRAILING, "java.awt.FlowLayout.TRAILING"),
	};

	public FlowLayoutAlignmentItems() {
	}
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

