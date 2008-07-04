/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.designer;

import java.util.EventObject;
/**
 * 
 * Event
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class Event extends EventObject {
	private static final long serialVersionUID = -5573382627639787984L;

	public static final int EVENT_SELECTION = 1;
	public static final int EVENT_SHOW_POPUP = 2;
	public static final int EVENT_SHOW_SOURCE = 3;
	private int id;
	private Object parameter;

	public Event(Object source, int id, Object param) {
		super(source);
		this.id = id;
		this.parameter = param;
	}

	public int getId() {
		return id;
	}

	public Object getParameter() {
		return parameter;
	}
}
