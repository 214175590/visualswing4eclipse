/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.swt_awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.LayoutFocusTraversalPolicy;
/**
 * 
 * EmbeddedChildFocusTraversalPolicy
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class EmbeddedChildFocusTraversalPolicy extends LayoutFocusTraversalPolicy {

	private static final long serialVersionUID = -7708166698501335927L;
	private final AwtFocusHandler awtHandler;

	EmbeddedChildFocusTraversalPolicy(AwtFocusHandler handler) {
		assert handler != null;
		awtHandler = handler;
	}

	private Component searchFirstViewport(Container container) {
		if (container instanceof JScrollPane) {
			JScrollPane jsp = (JScrollPane) container;
			return jsp.getViewport().getView();
		} else {
			int count = container.getComponentCount();
			for (int i = 0; i < count; i++) {
				Component comp = container.getComponent(i);
				if (comp instanceof Container) {
					Component viewport = searchFirstViewport((Container) comp);
					if (viewport != null)
						return viewport;
				}
			}
			return null;
		}
	}

	@Override
	public Component getFirstComponent(Container container) {
		if (container instanceof Frame) {
			if (container.getComponentCount() > 0) {
				Component content = container.getComponent(0);
				if (content instanceof JApplet) {
					JApplet applet = (JApplet) content;
					Container containerPane = applet.getContentPane();
					if (containerPane != null) {
						Component comp = searchFirstViewport(containerPane);
						if (comp != null)
							return comp;
						else
							return containerPane;
					} else
						return applet;
				} else
					return content;
			} else
				return container;
		} else
			return super.getFirstComponent(container);
	}

	public Component getComponentAfter(Container container, Component component) {
		assert container != null;
		assert component != null;
		assert awtHandler != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		if (component.equals(getLastComponent(container))) {
			// Instead of cycling around to the first component, transfer to the
			// next SWT component
			awtHandler.transferFocusNext();
			return null;
		} else {
			return super.getComponentAfter(container, component);
		}
	}

	public Component getComponentBefore(Container container, Component component) {
		assert container != null;
		assert component != null;
		assert awtHandler != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		if (component.equals(getFirstComponent(container))) {
			// Instead of cycling around to the last component, transfer to the
			// previous SWT component
			awtHandler.transferFocusPrevious();
			return null;
		} else {
			return super.getComponentBefore(container, component);
		}
	}

	public Component getDefaultComponent(Container container) {
		assert container != null;
		assert awtHandler != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		// This is a hack which depends on knowledge of current JDK
		// implementation to
		// work. The implementation above of getComponentBefore/After
		// properly returns null when transferring to SWT. However, the calling
		// AWT container
		// will then immediately try this method to find the next recipient of
		// focus. But we don't want *any* AWT component to receive focus... it's
		// just
		// been transferred to SWT. So, this method must return null when AWT
		// does
		// not own the focus. When AWT *does* own the focus, behave normally.
		if (awtHandler.awtHasFocus()) {
			// System.out.println("getDefault: super");
			return super.getDefaultComponent(container);
		} else {
			// System.out.println("getDefault: null");
			return null;
		}
	}

	public Component getCurrentComponent(Container container) {
		assert container != null;
		assert awtHandler != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		Component currentAwtComponent = awtHandler.getCurrentComponent();
		if ((currentAwtComponent != null) && container.isAncestorOf(currentAwtComponent)) {
			return currentAwtComponent;
		} else {
			return getDefaultComponent(container);
		}
	}
}
