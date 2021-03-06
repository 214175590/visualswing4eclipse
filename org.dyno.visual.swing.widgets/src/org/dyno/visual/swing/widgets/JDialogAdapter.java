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
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.designborder.DialogBorder;
import org.eclipse.core.commands.operations.IUndoableOperation;

@SuppressWarnings("unchecked")
public class JDialogAdapter extends RootPaneContainerAdapter {
	private JPanelAdapter contentAdapter;
	private JComponent contentPane;
	private JRootPane rootPane;
	public JDialogAdapter() {
		super(null);
		createContentAdapter();
	}
	public WidgetAdapter getContentAdapter(){
		return contentAdapter;
	}
	@Override
	public void setWidget(Component widget) {
		super.setWidget(widget);
		createContentAdapter();
	}
	public boolean needGenBoundCode(Component child) {
		if (child instanceof JMenuBar)
			return false;
		return contentAdapter.needGenBoundCode(child);
	}

	private void createContentAdapter() {
		contentAdapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
		contentAdapter.setDelegate(this);
		JDialog me = (JDialog) getWidget();
		JavaUtil.layoutContainer(me);
		contentPane = (JComponent) me.getContentPane();
		rootPane = me.getRootPane();
		contentAdapter.setWidget(contentPane);
		contentAdapter.setDelegate(this);
	}

	public void doLayout() {
		contentAdapter.doLayout();
	}
	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if (child instanceof JMenuBar) {
			JDialog jdialog = (JDialog) getWidget();
			jdialog.setJMenuBar((JMenuBar) child);
		} else
			contentAdapter.addChildByConstraints(child, constraints);
	}

	@Override
	public void clearSelection() {
		setSelected(false);
		JDialog jdialog = (JDialog) getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			jmbAdapter.clearSelection();
		}
		contentAdapter.clearSelection();
	}	

	@Override
	protected Component createWidget() {
		return new JDialog();
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
		JDialog jframe = (JDialog) getWidget();
		boolean undec = jframe.isUndecorated();
		if (undec)
			return super.getDesignBorder();
		DialogBorder frameBorder = new DialogBorder((JDialog)getWidget());
		return frameBorder;
	}

	@Override
	public Rectangle getDesignBounds() {
		Rectangle bounds = this.rootPane.getBounds();
		if (bounds.width <= 0)
			bounds.width = 320;
		if (bounds.height <= 0)
			bounds.height = 240;
		JDialog jframe = (JDialog) getWidget();
		boolean undec = jframe.isUndecorated();
		bounds.y = undec?24:44;
		bounds.x = 24;
		return bounds;
	}

	@Override
	protected Component newWidget() {
		return new JDialog();
	}

	@Override
	public Component cloneWidget() {
		JRootPane jrp = new JRootPane();
		JDialog jdialog = (JDialog) getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
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
		JDialog jdialog = (JDialog) getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getChild(index);
		else if (index == 0)
			return jmb;
		else
			return contentAdapter.getChild(index - 1);
	}

	@Override
	public int getChildCount() {
		JDialog jdialog = (JDialog) getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
		int count = contentAdapter.getChildCount();
		return jmb == null ? count : (count + 1);
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
		JDialog jdialog = (JDialog) getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getIndexOfChild(child);
		else if (jmb == child)
			return 0;
		else
			return contentAdapter.getIndexOfChild(child) + 1;
	}

	@Override
	public boolean allowChildResize(Component child) {
		if (child instanceof JMenuBar)
			return false;
		return contentAdapter.allowChildResize(child);
	}

	public Point convertToGlobal(Point p) {
		JDialog jdialog = (JDialog) getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
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

	public boolean removeChild(Component child) {
		if (child instanceof JMenuBar) {
			JDialog jdialog = (JDialog) getWidget();
			jdialog.setJMenuBar(null);
			return true;
		} else
			return contentAdapter.removeChild(child);
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
		return JDialog.class;
	}
	@Override
	public Object getAdapter(Class adapterClass) {
		if(adapterClass==MouseInputListener.class){
			LayoutAdapter adapter=contentAdapter.getLayoutAdapter();
			if(adapter!=null)
				return adapter.getAdapter(adapterClass);
			else
				return null;
		}else
			return super.getAdapter(adapterClass);
	}

}

