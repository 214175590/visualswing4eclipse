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

package org.dyno.visual.swing.base;

import org.dyno.visual.swing.plugin.spi.IConstants;

/**
 * 
 * Azimuth
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface Azimuth extends IConstants{
	int STATE_MOUSE_HOVER = 0;
	int STATE_MOUSE_DRAGGING = 1;
	int STATE_BEAN_TOBE_HOVER = 2;
	int STATE_BEAN_HOVER = 3;
	int STATE_SELECTION = 4;
	int STATE_BEAN_RESIZE_LEFT = 5;
	int STATE_BEAN_RESIZE_LEFT_TOP = 6;
	int STATE_BEAN_RESIZE_TOP = 7;
	int STATE_BEAN_RESIZE_RIGHT_TOP = 8;
	int STATE_BEAN_RESIZE_RIGHT = 9;
	int STATE_BEAN_RESIZE_RIGHT_BOTTOM = 10;
	int STATE_BEAN_RESIZE_BOTTOM = 11;
	int STATE_BEAN_RESIZE_LEFT_BOTTOM = 12;
	int STATE_BEAN_TOBE_RESIZED_RIGHT = 13;
	int STATE_BEAN_TOBE_RESIZED_RIGHT_BOTTOM = 14;
	int STATE_BEAN_TOBE_RESIZED_BOTTOM = 15;
	int STATE_BEAN_TOBE_RESIZED_LEFT_BOTTOM = 16;
	int STATE_BEAN_TOBE_RESIZED_LEFT = 17;
	int STATE_BEAN_TOBE_RESIZED_LEFT_TOP = 18;
	int STATE_BEAN_TOBE_RESIZED_TOP = 19;
	int STATE_BEAN_TOBE_RESIZED_RIGHT_TOP = 20;
	int STATE_ROOT_RESIZE_RIGHT = 21;
	int STATE_ROOT_RESIZE_RIGHT_BOTTOM = 22;
	int STATE_ROOT_RESIZE_BOTTOM = 23;
	int DND_THRESHOLD = 5;
}

