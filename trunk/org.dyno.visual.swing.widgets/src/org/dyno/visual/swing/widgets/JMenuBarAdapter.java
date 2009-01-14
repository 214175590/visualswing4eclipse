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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuBarAdapter extends CompositeAdapter {
	public JMenuBarAdapter() {
		super(null);
	}

	@Override
	public Component cloneWidget() {
		JMenuBar copy = (JMenuBar) super.cloneWidget();
		JMenuBar origin = (JMenuBar) getWidget();
		int count = origin.getMenuCount();
		for (int i = 0; i < count; i++) {
			Component child = getChild(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}

	public boolean isResizable() {
		return false;
	}
	@Override
	public boolean isMoveable() {
		return true;
	}

	@Override
	public int getChildCount() {
		JMenuBar origin = (JMenuBar) getWidget();
		return origin.getMenuCount();
	}

	@Override
	public Component getChild(int index) {
		JMenuBar origin = (JMenuBar) getWidget();
		return origin.getMenu(index);
	}

	@Override
	public void addChild(Component widget) {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.add(widget);
	}

	@Override
	public void removeAllChild() {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.removeAll();
	}

	@Override
	public boolean removeChild(Component child) {
		JMenuAdapter jmenuAdapter=(JMenuAdapter)WidgetAdapter.getWidgetAdapter(child);
		jmenuAdapter.hidePopup();
		JMenuBar origin = (JMenuBar) getWidget();		
		origin.remove(child);
		origin.doLayout();
		origin.repaint();
		Dimension size=child.getPreferredSize();		
		child.setSize(size);
		child.doLayout();
		return true;
	}

	@Override
	protected Component createWidget() {
		JMenuBar jmb = new JMenuBar();
		WidgetAdapter menuAdapter = ExtensionRegistry
				.createWidgetAdapter(JMenu.class);
		JMenu jmenu = (JMenu) menuAdapter.getWidget();
		jmb.add(jmenu);
		jmb.setSize(100, 25);
		jmb.doLayout();
		return jmb;
	}

	@Override
	protected Component newWidget() {
		return new JMenuBar();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}
	private int insert_x;
	@Override
	public boolean dragEnter(Point p) {
		setMascotLocation(p);
		if (isDroppingMenu()) {
			dropStatus = DROPPING_PERMITTED;
			insert_x=calculateInsert(p);
		} else {
			dropStatus = DROPPING_FORBIDDEN;
		}
		return true;
	}
	private int calculateInsert(Point p){
		JMenuBar jmb=(JMenuBar)getWidget();
		int count=jmb.getMenuCount();
		int calx=0;
		for(int i=0;i<count;i++){
			JMenu jmu=jmb.getMenu(i);
			if(p.x>=calx&&p.x<calx+jmu.getWidth()){
				return calx;
			}
			calx+=jmu.getWidth();
		}
		return calx;
	}
	@Override
	public boolean dragExit(Point p) {
		setMascotLocation(p);
		dropStatus = NOOP;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		setMascotLocation(p);
		if (isDroppingMenu()) {
			dropStatus = DROPPING_PERMITTED;
			insert_x=calculateInsert(p);
		} else {
			dropStatus = DROPPING_FORBIDDEN;
		}
		return true;
	}

	@Override
	public boolean drop(Point p) {
		setMascotLocation(p);
		dropStatus=NOOP;
		if (isDroppingMenu()) {
			WidgetAdapter menuAdapter = getDropWidget().get(0);
			JMenu jmenu = (JMenu) menuAdapter.getWidget();
			JMenuBar jmb = (JMenuBar) getWidget();
			jmb.add(jmenu);
			jmb.validate();
			jmb.doLayout();
			clearAllSelected();
			menuAdapter.requestNewName();
			menuAdapter.setSelected(true);
			addNotify();
			repaintDesigner();
		} else {
		}
		return true;
	}

	private int dropStatus;
	private static final int NOOP = 0;
	private static final int DROPPING_PERMITTED = 1;
	private static final int DROPPING_FORBIDDEN = 2;

	@Override
	public void paintHovered(Graphics clipg) {
		if (dropStatus == DROPPING_FORBIDDEN) {
			JMenuBar jmenubar = (JMenuBar) getWidget();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(0, 0, jmenubar.getWidth(), jmenubar.getHeight());
		} else if (dropStatus == DROPPING_PERMITTED) {
			JMenuBar jmenubar = (JMenuBar) getWidget();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setStroke(STROKE);
			g2d.setColor(GREEN_COLOR);
			g2d.drawRect(0, 0, jmenubar.getWidth(), jmenubar.getHeight());
			g2d.drawLine(insert_x, 0, insert_x, jmenubar.getHeight());
		}
	}

	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
	}
	private boolean isDroppingMenu() {
		List<WidgetAdapter> targets = getDropWidget();
		if(targets.size()!=1)
			return false;
		Component drop = targets.get(0).getWidget();
		return drop instanceof JMenu;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JMenuBar.class;
	}

}

