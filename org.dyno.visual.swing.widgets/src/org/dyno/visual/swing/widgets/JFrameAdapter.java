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

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.designborder.FrameBorder;
import org.eclipse.core.commands.operations.IUndoableOperation;

@SuppressWarnings("unchecked")
public class JFrameAdapter extends RootPaneContainerAdapter {
	private JPanelAdapter contentAdapter;
	private JComponent contentPane;
	private JRootPane rootPane;

	public JFrameAdapter() {
		super(null);
		createContentAdapter();
	}

	public WidgetAdapter getContentAdapter() {
		return contentAdapter;
	}

	public void selectChildren() {
		if (isContentDesigned()) {
			JFrame jframe = (JFrame) getWidget();
			JMenuBar jmb = jframe.getJMenuBar();
			if (jmb != null) {
				WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
				if (jmbAdapter != null) {
					jmbAdapter.setSelected(true);
					((CompositeAdapter) jmbAdapter).selectChildren();
				}
			}
			CompositeAdapter cAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(jframe.getContentPane());
			cAdapter.setSelected(false);
			cAdapter.selectChildren();
		} else
			super.selectChildren();
	}

	public boolean isChildMoveable(Component child) {
		if (isContentDesigned() && !(child instanceof JMenuBar)) {
			return false;
		}
		return true;
	}

	@Override
	public void setWidget(Component widget) {
		super.setWidget(widget);
		createContentAdapter();
	}

	private void createContentAdapter() {
		JFrame me = (JFrame) getWidget();
		Component contentPane = me.getContentPane();
		if (isContentDesigned()) {
			contentAdapter = (JPanelAdapter) WidgetAdapter.getWidgetAdapter(contentPane);
		} else {
			contentAdapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
			contentAdapter.setDelegate(this);
			JavaUtil.layoutContainer(me);
			contentPane = (JComponent) me.getContentPane();
			rootPane = me.getRootPane();
			contentAdapter.setWidget(contentPane);
			contentAdapter.setDelegate(this);
		}
	}

	public void doLayout() {
		contentAdapter.doLayout();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if (child instanceof JMenuBar) {
			JFrame jframe = (JFrame) getWidget();
			jframe.setJMenuBar((JMenuBar) child);
		} else
			contentAdapter.addChildByConstraints(child, constraints);
	}

	public boolean needGenBoundCode(Component child) {
		if (child instanceof JMenuBar)
			return false;
		return contentAdapter.needGenBoundCode(child);
	}

	@Override
	public void clearSelection() {
		setSelected(false);
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			jmbAdapter.clearSelection();
		}
		if (isContentDesigned()) {
			WidgetAdapter.getWidgetAdapter(jframe.getContentPane()).clearSelection();
		} else {
			contentAdapter.clearSelection();
		}
	}

	@Override
	protected Component createWidget() {
		return new JFrame();
	}

	@Override
	public Object getChildConstraints(Component child) {
		if (child instanceof JMenuBar)
			return null;
		return contentAdapter.getChildConstraints(child);
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public Component getRootPane() {
		return rootPane;
	}

	@Override
	public Border getDesignBorder() {
		JFrame jframe = (JFrame) getWidget();
		boolean undec = jframe.isUndecorated();
		if (undec)
			return super.getDesignBorder();
		FrameBorder frameBorder = new FrameBorder((JFrame) getWidget());
		return frameBorder;
	}

	@Override
	public Rectangle getDesignBounds() {
		Rectangle bounds = this.rootPane.getBounds();
		if (bounds.width <= 0)
			bounds.width = 320;
		if (bounds.height <= 0)
			bounds.height = 240;
		JFrame jframe = (JFrame) getWidget();
		boolean undec = jframe.isUndecorated();
		bounds.y = undec ? 24 : 44;
		bounds.x = 24;
		return bounds;
	}

	@Override
	protected Component newWidget() {
		return new JFrame();
	}

	@Override
	public Component cloneWidget() {
		JRootPane jrp = new JRootPane();
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			JMenuBar jmenubar = (JMenuBar) jmbAdapter.cloneWidget();
			jrp.setJMenuBar(jmenubar);
		}
		Container container = (Container) contentAdapter.cloneWidget();
		jrp.setContentPane(container);
		return jrp;
	}

	public Component getParentContainer() {
		return contentAdapter.getWidget();
	}

	@Override
	public void addAfter(Component hovering, Component dragged) {
		contentAdapter.addAfter(hovering, dragged);
	}

	@Override
	public void addBefore(Component hovering, Component dragged) {
		contentAdapter.addBefore(hovering, dragged);
	}

	@Override
	public void addChild(Component widget) {
		contentAdapter.addChild(widget);
	}

	@Override
	public boolean doAlignment(String id) {
		return contentAdapter.doAlignment(id);
	}

	@Override
	public IUndoableOperation doKeyPressed(KeyEvent e) {
		return contentAdapter.doKeyPressed(e);
	}

	@Override
	public Component getChild(int index) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (isContentDesigned()) {
			if (jmb == null)
				return jframe.getContentPane();
			else if (index == 0)
				return jmb;
			else
				return jframe.getContentPane();
		} else {
			if (jmb == null)
				return contentAdapter.getChild(index);
			else if (index == 0)
				return jmb;
			else
				return contentAdapter.getChild(index - 1);
		}
	}

	@Override
	public int getChildCount() {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (isContentDesigned()) {
			return jmb == null ? 1 : 2;
		} else {
			int count = contentAdapter.getChildCount();
			return jmb == null ? count : (count + 1);
		}
	}

	private boolean isContentDesigned() {
		JFrame jframe = (JFrame) getWidget();
		Component content = jframe.getContentPane();
		return WidgetAdapter.getWidgetAdapter(content) != null && WidgetAdapter.getWidgetAdapter(content).getID() != null;
	}

	public String toString() {
		if (isRoot()) {
			return "[" + getWidgetName() + "]";
		} else {
			return getName() + " [" + getWidgetName() + "]";
		}
	}

	@Override
	public int getIndexOfChild(Component child) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (isContentDesigned()) {
			if (jmb == null)
				return 0;
			else if (jmb == child)
				return 0;
			else
				return 1;
		} else {
			if (jmb == null)
				return contentAdapter.getIndexOfChild(child);
			else if (jmb == child)
				return 0;
			else
				return contentAdapter.getIndexOfChild(child) + 1;
		}
	}

	@Override
	public boolean allowChildResize(Component child) {
		if (child instanceof JMenuBar)
			return false;
		if (isContentDesigned()) {
			return false;
		} else
			return contentAdapter.allowChildResize(child);
	}

	public Point convertToGlobal(Point p) {
		JFrame jframe = (JFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb == null)
			return contentAdapter.convertToGlobal(p);
		else {
			p = SwingUtilities.convertPoint(rootPane, p, contentPane);
			return contentAdapter.convertToGlobal(p);
		}
	}

	public Point convertToLocal(Point p) {
		return contentAdapter.convertToLocal(p);
	}

	public WidgetAdapter getRootAdapter() {
		return this;
	}

	@Override
	public Object getAdapter(Class adapterClass) {
		if (adapterClass == MouseInputListener.class) {
			LayoutAdapter adapter = contentAdapter.getLayoutAdapter();
			if (adapter != null)
				return adapter.getAdapter(adapterClass);
			else
				return null;
		} else
			return super.getAdapter(adapterClass);
	}

	public boolean removeChild(Component child) {
		JFrame jframe = (JFrame) getWidget();
		if (!isContentDesigned() || child != jframe.getContentPane()) {
			if (child instanceof JMenuBar) {
				jframe.setJMenuBar(null);
				return true;
			} else
				return contentAdapter.removeChild(child);
		} else
			return false;
	}

	@Override
	public boolean isChildVisible(Component child) {
		return contentAdapter.isChildVisible(child);
	}

	@Override
	public void showChild(Component widget) {
		contentAdapter.showChild(widget);
	}

	@Override
	public void adjustLayout(Component widget) {
		contentAdapter.adjustLayout(widget);
	}

	@Override
	public boolean isSelectionAlignResize(String id) {
		return contentAdapter.isSelectionAlignResize(id);
	}

	@Override
	public Class getWidgetClass() {
		return JFrame.class;
	}

	public IWidgetPropertyDescriptor[] getConstraintsProperties(Component widget) {
		if (isContentDesigned())
			return null;
		else if (widget instanceof JMenuBar) {
			return null;
		} else {
			return contentAdapter.getConstraintsProperties(widget);
		}
	}
}
