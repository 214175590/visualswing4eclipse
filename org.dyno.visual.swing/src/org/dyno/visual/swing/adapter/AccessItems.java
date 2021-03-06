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

package org.dyno.visual.swing.adapter;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class AccessItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = {
			new Item("private", WidgetAdapter.ACCESS_PRIVATE, "private"),
			new Item("package", WidgetAdapter.ACCESS_DEFAULT, ""),
			new Item("protected", WidgetAdapter.ACCESS_PROTECTED, "protected"),
			new Item("public", WidgetAdapter.ACCESS_PUBLIC, "public") };

	public AccessItems() {
	}

	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

