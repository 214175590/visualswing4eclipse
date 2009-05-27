
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

package org.dyno.visual.swing.widgets.layout.constraints;

import java.awt.BorderLayout;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class BorderLayoutConstraintItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { 
		new Item("Center", BorderLayout.CENTER, "java.awt.BorderLayout.CENTER"),
		new Item("North", BorderLayout.NORTH, "java.awt.BorderLayout.CENTER"),
		new Item("East", BorderLayout.EAST, "java.awt.BorderLayout.CENTER"),
		new Item("West", BorderLayout.WEST, "java.awt.BorderLayout.CENTER"),
		new Item("South", BorderLayout.SOUTH, "java.awt.BorderLayout.CENTER")
	};

	public BorderLayoutConstraintItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

