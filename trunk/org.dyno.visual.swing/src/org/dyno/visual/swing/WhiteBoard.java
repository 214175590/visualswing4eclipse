/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing;

import java.util.List;

import org.dyno.visual.swing.designer.Event;
import org.dyno.visual.swing.designer.Listener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.IJavaProject;
/**
 * 
 * WhiteBoard
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class WhiteBoard {
	private static IJavaProject project;
	private static Listener editorListener;
	private static List<WidgetAdapter> selectedWidget;

	public static void setSelectedWidget(List<WidgetAdapter> adapter) {
		selectedWidget = adapter;
	}

	public static List<WidgetAdapter> getSelectedWidget() {
		return selectedWidget;
	}

	public static void setCurrentProject(IJavaProject proj) {
		project = proj;
	}

	public static IJavaProject getCurrentProject() {
		return project;
	}

	public static void setEditorListener(Listener l) {
		editorListener = l;
	}

	public static void sendEvent(Event e) {
		if (editorListener != null)
			editorListener.onEvent(e);
	}
}
