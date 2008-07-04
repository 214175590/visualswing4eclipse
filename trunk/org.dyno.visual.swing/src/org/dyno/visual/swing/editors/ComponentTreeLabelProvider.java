/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.util.HashMap;

import javax.swing.JComponent;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
/**
 * 
 * ComponentTreeLabelProvider
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ComponentTreeLabelProvider implements ILabelProvider {
	private static String OTHER_COMPONENT_IMAGE = "/icons/other.png";
	private static String WIDGETS_FORM_ROOT = "/icons/root.png";
	private static String EVENT_DESC_ICON = "/icons/events.png";
	private static String EVENT_SET_ICON = "/icons/eventset.png";
	private static String EVENT_METHOD_ICON = "/icons/eventmethod.png";
	private HashMap<String, Image> images;

	public ComponentTreeLabelProvider() {
		images = new HashMap<String, Image>();
	}

	@Override
	public Image getImage(Object element) {
		if (element == null) {
			return null;
		} else if (element instanceof ComponentTreeInput) {
			return VisualSwingPlugin.getSharedImage(WIDGETS_FORM_ROOT);
		} else if (element instanceof VisualDesigner) {
			return VisualSwingPlugin.getSharedImage(WIDGETS_FORM_ROOT);
		} else if (element instanceof JComponent) {
			JComponent component = (JComponent) element;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(component);
			return adapter.getIconImage();
		} else if (element instanceof String) {
			return VisualSwingPlugin.getSharedImage(OTHER_COMPONENT_IMAGE);
		} else if (element instanceof EventDesc) {
			return VisualSwingPlugin.getSharedImage(EVENT_DESC_ICON);
		} else if (element instanceof EventSet) {
			return VisualSwingPlugin.getSharedImage(EVENT_SET_ICON);
		} else if (element instanceof EventMethod) {
			return VisualSwingPlugin.getSharedImage(EVENT_METHOD_ICON);
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element == null) {
			return "";
		} else if (element instanceof ComponentTreeInput) {
			return "root";
		} else if (element instanceof VisualDesigner) {
			return "Form";
		} else if (element instanceof JComponent) {
			JComponent component = (JComponent) element;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(component);
			return (adapter.isRoot() ? "" : adapter.getName()) + "[" + adapter.getWidgetName() + "]";
		} else
			return element.toString();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
		for (String key : images.keySet()) {
			Image image = images.get(key);
			image.dispose();
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
