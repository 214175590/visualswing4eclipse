
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.MenuManager;

public class JAppletAdapter extends RootPaneContainerAdapter {
	private JPanelAdapter contentAdapter;
	private JComponent rootPane;
	private JRootPane jrootPane;
	public JAppletAdapter() {
		super(null);
		createContentAdapter();
	}

	@Override
	public void setWidget(Component widget) {
		super.setWidget(widget);
		createContentAdapter();
	}

	private void createContentAdapter() {
		contentAdapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
		contentAdapter.setDelegate(this);
		JApplet me = (JApplet) getWidget();
		layoutContainer(me);
		rootPane = (JComponent) me.getContentPane();
		jrootPane = me.getRootPane();
		contentAdapter.setWidget(rootPane);
		contentAdapter.setDelegate(this);
	}

	public void doLayout() {
		contentAdapter.doLayout();
	}


	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if (child instanceof JMenuBar) {
			JApplet japplet = (JApplet) getWidget();
			japplet.setJMenuBar((JMenuBar) child);
		} else
			contentAdapter.addChildByConstraints(child, constraints);
	}

	@Override
	public void clearSelection() {
		setSelected(false);
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			jmbAdapter.clearSelection();
		}
		contentAdapter.clearSelection();
	}

	@Override
	public int getCursorLocation(Point p) {
		int w = jrootPane.getWidth();
		int h = jrootPane.getHeight();
		int x = p.x;
		int y = p.y;
		if (x < -ADHERE_PAD) {
			return OUTER;
		} else if (x < ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return LEFT_TOP;
			} else if (y < h - ADHERE_PAD) {
				return LEFT;
			} else if (y < h + ADHERE_PAD) {
				return LEFT_BOTTOM;
			} else {
				return OUTER;
			}
		} else if (x < w - ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return TOP;
			} else if (y < h - ADHERE_PAD) {
				return INNER;
			} else if (y < h + ADHERE_PAD) {
				return BOTTOM;
			} else {
				return OUTER;
			}
		} else if (x < w + ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return RIGHT_TOP;
			} else if (y < h - ADHERE_PAD) {
				return RIGHT;
			} else if (y < h + ADHERE_PAD) {
				return RIGHT_BOTTOM;
			} else {
				return OUTER;
			}
		} else {
			return OUTER;
		}
	}

	@Override
	protected Component createWidget() {
		return new JApplet();
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
		return jrootPane;
	}

	@Override
	public Rectangle getDesignBounds() {
		Rectangle bounds = this.jrootPane.getBounds();
		if (bounds.width <= 0)
			bounds.width = 400;
		if (bounds.height <= 0)
			bounds.height = 300;
		bounds.y = 24;
		bounds.x = 24;
		return bounds;
	}

	@Override
	protected Component newWidget() {
		return new JApplet();
	}

	@Override
	public Component cloneWidget() {
		JRootPane jrp = new JRootPane();
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			JMenuBar jmenubar = (JMenuBar) jmbAdapter.cloneWidget();
			jrp.setJMenuBar(jmenubar);
		}
		Container container = (Container) contentAdapter.cloneWidget();
		jrp.setContentPane(container);
		return jrp;
	}

	public Component getComponent() {
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
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getChild(index);
		else if (index == 0)
			return jmb;
		else
			return contentAdapter.getChild(index - 1);
	}

	@Override
	public int getChildCount() {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
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
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getIndexOfChild(child);
		else if (jmb == child)
			return 0;
		else
			return contentAdapter.getIndexOfChild(child) + 1;
	}

	@Override
	public boolean allowChildResize() {
		return contentAdapter.allowChildResize();
	}

	@Override
	public boolean dragOver(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			setMascotLocation(p);
			dropStatus = DROPPING_FORBIDDEN;
			return true;
		} else if (isDroppingMenuBar()) {
			setMascotLocation(p);
			dropStatus = DROPPING_PERMITTED;
			return true;
		} else
			return contentAdapter.dragOver(p);
	}

	@Override
	public boolean dragEnter(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			setMascotLocation(p);
			dropStatus = DROPPING_FORBIDDEN;
			return true;
		} else if (isDroppingMenuBar()) {
			setMascotLocation(p);
			dropStatus = DROPPING_PERMITTED;
			return true;
		} else
			return contentAdapter.dragEnter(p);
	}

	private int getJMenuBarHeight() {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		return jmb.getHeight();
	}

	private int dropStatus;
	private static final int NOOP = 0;
	private static final int DROPPING_PERMITTED = 1;
	private static final int DROPPING_FORBIDDEN = 2;

	private boolean isDroppingForbbiden() {
		return isDroppingMenu() || isDroppingMenuBar() && hasMenuBar();
	}

	@Override
	public boolean dragExit(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			setMascotLocation(p);
			dropStatus = NOOP;
			return true;
		} else if (isDroppingMenuBar()) {
			setMascotLocation(p);
			dropStatus = NOOP;
			return true;
		} else
			return contentAdapter.dragExit(p);
	}

	@Override
	public boolean drop(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			setMascotLocation(p);
			dropStatus = NOOP;
			Toolkit.getDefaultToolkit().beep();
			return true;
		} else if (isDroppingMenuBar()) {
			setMascotLocation(p);
			WidgetAdapter target = getDropWidget().get(0);
			JMenuBar jmb = (JMenuBar) target.getWidget();
			JApplet japplet = (JApplet) getWidget();
			japplet.setJMenuBar(jmb);
			target.requestNewName();
			japplet.validate();
			doLayout();
			validateContent();
			clearAllSelected();
			target.setSelected(true);
			setDirty(true);
			addNotify();
			repaintDesigner();
			dropStatus = NOOP;
			return true;
		} else
			return contentAdapter.drop(p);
	}

	private boolean isDroppingMenu() {
		List<WidgetAdapter> targets = getDropWidget();
		if(targets.size()!=1)
			return false;
		Component drop = targets.get(0).getWidget();
		return drop != null
				&& (drop instanceof JMenu || drop instanceof JMenuItem || drop instanceof JPopupMenu);
	}

	private boolean hasMenuBar() {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		return jmb != null;
	}

	@Override
	public void paintHovered(Graphics clipg) {
		if (dropStatus == NOOP) {
			JApplet japplet = (JApplet) getWidget();
			JMenuBar jmb = japplet.getJMenuBar();
			if (jmb != null) {
				Rectangle bounds = rootPane.getBounds();
				bounds.x = bounds.y = 0;
				bounds = SwingUtilities.convertRectangle(rootPane, bounds,
						jrootPane);
				clipg = clipg.create(bounds.x, bounds.y, bounds.width,
						bounds.height);
			}
			contentAdapter.paintHovered(clipg);
			if (jmb != null) {
				clipg.dispose();
			}
		} else if (dropStatus == DROPPING_FORBIDDEN) {
			Rectangle bounds = rootPane.getBounds();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(0, 0, bounds.width, 22);
		} else if (dropStatus == DROPPING_PERMITTED) {
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(GREEN_COLOR);
			Rectangle bounds = rootPane.getBounds();
			g2d.drawRect(0, 0, bounds.width, 22);
		}
	}

	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
	}

	public Point convertToGlobal(Point p) {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb == null)
			return contentAdapter.convertToGlobal(p);
		else {
			p = SwingUtilities.convertPoint(jrootPane, p, rootPane);
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
	public void paintHint(Graphics clipg) {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = rootPane.getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(rootPane, bounds,
					jrootPane);
			clipg = clipg.create(bounds.x, bounds.y, bounds.width,
					bounds.height);
		}
		contentAdapter.paintHint(clipg);
		if (jmb != null) {
			clipg.dispose();
		}
	}
	public boolean removeChild(Component child) {
		if (child instanceof JMenuBar) {
			JApplet japplet = (JApplet) getWidget();
			japplet.setJMenuBar(null);
			return true;
		} else
			return contentAdapter.removeChild(child);
	}
	public WidgetAdapter getContentAdapter(){
		return contentAdapter;
	}
	@Override
	protected boolean isChildVisible(Component child) {
		return contentAdapter.isChildVisible(child);
	}

	@Override
	public void showChild(Component widget) {
		contentAdapter.showChild(widget);
	}

	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		contentAdapter.fillSetLayoutAction(menu);
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		if (!(child instanceof JMenuBar))
			contentAdapter.fillConstraintsAction(menu, child);
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
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JApplet.class;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapterClass) {
		Object adaptable = super.getAdapter(adapterClass);
		if(adaptable==null&&adapterClass==MouseInputListener.class){
			LayoutAdapter adapter=contentAdapter.getLayoutAdapter();
			if(adapter!=null)
				return adapter.getAdapter(adapterClass);
			else
				return null;
		}else
			return adaptable;
		
	}	
	public void paintGrid(Graphics clipg) {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = rootPane.getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(rootPane, bounds,
					jrootPane);
			clipg = clipg.create(bounds.x, bounds.y, bounds.width,
					bounds.height);
		}
		contentAdapter.paintGrid(clipg);
		if (jmb != null) {
			clipg.dispose();
		}
	}	
	public void paintAnchor(Graphics g) {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb != null) {
			Rectangle bounds = rootPane.getBounds();
			bounds.x = bounds.y = 0;
			bounds = SwingUtilities.convertRectangle(rootPane, bounds,
					jrootPane);
			g = g.create(bounds.x, bounds.y, bounds.width,
					bounds.height);
		}
		contentAdapter.paintAnchor(g);
		if (jmb != null) {
			g.dispose();
		}
	}		
}

