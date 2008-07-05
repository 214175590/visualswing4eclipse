/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.designer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.dyno.visual.swing.plugin.spi.WhiteBoard;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
/**
 * 
 * VisualDesigner
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VisualDesigner extends JComponent implements KeyListener {
	private static final long serialVersionUID = -8003291919574427325L;
	private Rectangle rootBounds;
	private GlassPlane glass;
	private JComponent container;
	private Border designBorder;
	private JComponent root;
	private List<WidgetAdapter> clipboard;

	private VisualSwingEditor editor;
	private Composite parent;

	@SuppressWarnings("serial")
	public VisualDesigner(VisualSwingEditor editor, Composite parent) {
		this.editor = editor;
		this.parent = parent;

		this.clipboard = new ArrayList<WidgetAdapter>();

		setFocusable(true);
		setLayout(new DesignerLayout());

		glass = new GlassPlane(this);
		add(glass);

		glass.addKeyListener(this);

		container = new JComponent() {
		};
		add(container);
		setFocusCycleRoot(true);
		setFocusTraversalPolicy(new DesignerFocusTraversalPolicy());
	}
	public String getLnfClassname(){
		return editor.getLnfClassname();
	}
	public Shell getShell() {
		return editor.getEditorSite().getShell();
	}

	public GlassPlane getGlass() {
		return glass;
	}

	public boolean editComponent(JComponent hovered) {
		return glass.editComponent(hovered);
	}

	public JComponent getRoot() {
		return root;
	}
	public void setLnfClassname(String lnfClassname){
		editor.setLnfClassname(lnfClassname);
		setLnfChanged(true);
	}
	public void selectWidgets(Rectangle selectionRegion) {
		if (root != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(root);
			if (_selectWidget(selectionRegion, adapter)) {
				WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(root);
				if (rootAdapter != null) {
					rootAdapter.setSelected(false);
				}
				publishSelection();
				repaint();
			}
		}
	}

	private boolean _selectWidget(Rectangle sel, WidgetAdapter adapter) {
		boolean selected = false;
		Rectangle bounds = adapter.getWidget().getBounds();
		if (sel.contains(bounds)) {
			adapter.setSelected(true);
			selected = true;
		}
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			Rectangle newsel = new Rectangle(sel.x - bounds.x, sel.y - bounds.y, sel.width, sel.height);
			int size = compositeAdapter.getChildCount();
			for (int i = 0; i < size; i++) {
				JComponent child = compositeAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				if (childAdapter != null && _selectWidget(newsel, childAdapter)) {
					selected = true;
				}
			}
		}
		return selected;
	}

	void trigPopup(final Point p) {
		editor.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				trigPopupAt(p);
			}
		});
	}

	private void trigPopupAt(Point p) {
		JComponent hovered = componentAt(p, 0);
		Point dp = new Point(p);
		SwingUtilities.convertPointToScreen(dp, glass);
		showPopup(dp, hovered);
	}

	private void showPopup(Point dp, JComponent hovered) {
		MenuManager manager = new MenuManager("#EDIT");
		if (hovered != null)
			addContextSensitiveMenu(manager, hovered);
		IEditorSite site = editor.getEditorSite();
		IWorkbenchWindow window = site.getWorkbenchWindow();
		IWorkbenchPage page = site.getPage();
		page.getActiveEditor();
		for (EditorAction action : editor.getActions()) {
			if (action == null) {
				manager.add(new Separator());
			} else if (action.isRetargetable()) {
				IWorkbenchAction workbenchAction = action.getActionFactory().create(window);
				if (workbenchAction instanceof RetargetAction) {
					RetargetAction retargetAction = (RetargetAction) workbenchAction;
					page.addPartListener(retargetAction);
					retargetAction.partActivated(editor);
				}
				manager.add(workbenchAction);
			} else {
				manager.add(action);
			}
		}
		Menu menu = manager.createContextMenu(parent);
		menu.setLocation(dp.x, dp.y);
		menu.setVisible(true);
	}

	private void addContextSensitiveMenu(MenuManager menu, JComponent hovered) {
		WidgetAdapter hoveredAdapter = WidgetAdapter.getWidgetAdapter(hovered);
		hoveredAdapter.fillContextAction(menu);
		menu.add(new Separator());
	}

	public void clearSelection() {
		if (root != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(root);
			if (adapter != null) {
				adapter.clearSelection();
			}
		}

	}

	public JComponent componentAt(Point p, int offset) {
		if (root != null) {
			Point mp = SwingUtilities.convertPoint(this, p, root);
			if (isPointInRoot(mp, offset))
				return root;
			return _getComponentAt(root, mp, offset);
		} else
			return null;
	}

	private boolean isPointInRoot(Point p, int offset) {
		int w = root.getWidth();
		int h = root.getHeight();
		if (p.x < -offset || p.y < -offset || p.x >= w + offset || p.y >= h + offset)
			return false;
		if (p.x >= offset && p.y >= offset && p.x < w - offset && p.y < h - offset)
			return false;
		return true;
	}

	private JComponent _getComponentAt(JComponent comp, Point p, int ad) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
		if (adapter == null)
			return null;
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compAdapter = (CompositeAdapter) adapter;
			if (compAdapter.isEnclosingContainer()) {
				if (compAdapter.interceptPoint(p, ad))
					return comp;
			}
			int count = compAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				JComponent child = compAdapter.getChild(i);
				if (child.isVisible()) {
					Point location = SwingUtilities.convertPoint(comp, p, child);
					JComponent jcomp = _getComponentAt(child, location, ad);
					if (jcomp != null)
						return jcomp;
				}
			}
		}
		CompositeAdapter compParent = adapter.getParentAdapter();
		if (compParent != null && compParent.isEnclosingContainer()) {
			Rectangle bounds = compParent.getVisibleRect(comp);
			if (bounds != null) {
				if (p.x >= bounds.x - ad && p.x <= bounds.width + ad && p.y >= bounds.y - ad && p.y <= bounds.height + ad)
					return comp;
			}
			return null;
		} else {
			if (p.x >= -ad && p.y >= -ad && p.x < comp.getWidth() + ad && p.y < comp.getHeight() + ad)
				return comp;
			else
				return null;
		}
	}

	private Event createEvent(int id, Object param) {
		return new Event(this, id, param);
	}

	JComponent getRootWidget() {
		return root;
	}

	public void initRootWidget(WidgetAdapter adapter) {
		if (root != null)
			remove(root);
		if (adapter != null) {
			root = adapter.getWidget();
			rootBounds = adapter.getDesignBounds();
			root.setSize(rootBounds.width, rootBounds.height);
			add(root);
			designBorder = adapter.getDesignBorder();
			container.setBorder(designBorder);
			validateContent();
			setLnfChanged(false);
			setFocus();
		}
	}

	public void publishSelection() {
		WhiteBoard.sendEvent(createEvent(Event.EVENT_SELECTION, new WidgetSelection(root)));
	}

	public void showPopup(Event event) {
		Object[] param = (Object[]) event.getParameter();
		Point p = (Point) param[0];
		JComponent hovered = (JComponent) param[1];
		swt_show_popup(p, hovered);
	}

	private void swt_show_popup(final Point p, final JComponent hovered) {
		editor.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				showPopup(p, hovered);
			}
		});
	}

	public void validateContent() {
		editor.validateContent();
	}

	public void changeFocused() {
		CompositeAdapter focused = getFocused();
		glass.setFocusedComposite(focused);
		repaint();
	}

	public CompositeAdapter getFocused() {
		if (root != null) {
			WidgetSelection selected = new WidgetSelection(root);
			WidgetAdapter parent = null;
			for (int i = 0; i < selected.size(); i++) {
				JComponent comp = selected.get(i);
				WidgetAdapter child = WidgetAdapter.getWidgetAdapter(comp);
				WidgetAdapter par = child.getParentAdapter();
				if (parent == null) {
					parent = par;
				} else if (parent != par)
					return null;
			}
			if (parent == null)
				return null;
			else
				return (CompositeAdapter) parent;
		} else
			return null;
	}

	public boolean isWidgetEditing() {
		return glass.isWidgetEditing();
	}

	public void setActionState(IAction action) {
		String id = action.getId();
		WidgetSelection selection = new WidgetSelection((JComponent) root);
		WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter((JComponent) root);
		if (id.equals(ActionFactory.CUT.getId())) {
			action.setEnabled(!selection.isEmpty() && !rootAdapter.isSelected());
		} else if (id.equals(ActionFactory.COPY.getId())) {
			action.setEnabled(!selection.isEmpty() && !rootAdapter.isSelected());
		} else if (id.equals(ActionFactory.PASTE.getId())) {
			action.setEnabled(!clipboard.isEmpty());
		} else if (id.equals(EditorAction.DUPLICATE)) {
			action.setEnabled(!selection.isEmpty() && !rootAdapter.isSelected());
		} else if (id.equals(ActionFactory.DELETE.getId())) {
			action.setEnabled(!selection.isEmpty() && !rootAdapter.isSelected());
		} else if (id.equals(ActionFactory.SELECT_ALL.getId())) {
			action.setEnabled(((CompositeAdapter) rootAdapter).getChildCount() > 0);
		} else if (id.equals(EditorAction.PREVIEW)) {
			action.setEnabled(true);
		} else if (id.equals(EditorAction.SOURCE)) {
			action.setEnabled(true);
		} else {
			action.setEnabled(isAlignResize(1, id));
		}
	}

	private boolean isAlignResize(int count, String id) {
		WidgetSelection selection = new WidgetSelection((JComponent) root);
		if (selection.size() > count) {
			WidgetAdapter parentAdapter = null;
			for (JComponent selected : selection) {
				WidgetAdapter selectedAdapter = WidgetAdapter.getWidgetAdapter(selected);
				WidgetAdapter selectedParent = selectedAdapter.getParentAdapter();
				if (parentAdapter == null) {
					parentAdapter = selectedParent;
				} else if (parentAdapter != selectedParent) {
					return false;
				}
			}
			if (parentAdapter == null) {
				return false;
			} else {
				for (JComponent selected : selection) {
					WidgetAdapter selectedAdapter = WidgetAdapter.getWidgetAdapter(selected);
					if (!selectedAdapter.isResizable()) {
						return false;
					}
				}
				return ((CompositeAdapter) parentAdapter).isSelectionAlignResize(id);
			}
		} else {
			return false;
		}
	}

	public void doAction(IAction action) {
		String id = action.getId();
		WidgetSelection selection = new WidgetSelection((JComponent) root);
		CompositeAdapter rootAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter((JComponent) root);
		if (id.equals(ActionFactory.CUT.getId())) {
			clipboard.clear();
			for (JComponent child : selection) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
				CompositeAdapter parentAdapter = (CompositeAdapter) adapter.getParentAdapter();
				boolean success = parentAdapter.removeChild(child);
				if (success)
					parentAdapter.setDirty(true);
				clipboard.add(adapter);
			}
			rootAdapter.doLayout();
			root.validate();
			publishSelection();
		} else if (id.equals(ActionFactory.COPY.getId())) {
			clipboard.clear();
			for (JComponent child : selection) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
				clipboard.add((WidgetAdapter) adapter.clone());
			}
			publishSelection();
		} else if (id.equals(ActionFactory.PASTE.getId())) {
			for (WidgetAdapter childAdapter : clipboard) {
				WidgetAdapter adapter = (WidgetAdapter) childAdapter.clone();
				rootAdapter.addChild(adapter.getWidget());
			}
			rootAdapter.doLayout();
			clipboard.clear();
			root.validate();
			publishSelection();
		} else if (id.equals(EditorAction.DUPLICATE)) {
			for (JComponent child : selection) {
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				WidgetAdapter adapter = (WidgetAdapter) childAdapter.clone();
				CompositeAdapter parentAdapter = (CompositeAdapter) childAdapter.getParentAdapter();
				parentAdapter.addChild(adapter.getWidget());
			}
			rootAdapter.doLayout();
			root.validate();
			publishSelection();
		} else if (id.equals(ActionFactory.DELETE.getId())) {
			for (JComponent child : selection) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
				CompositeAdapter parentAdapter = (CompositeAdapter) adapter.getParentAdapter();
				boolean success = parentAdapter.removeChild(child);
				if (success)
					parentAdapter.setDirty(true);
			}
			rootAdapter.doLayout();
			root.validate();
			publishSelection();
		} else if (id.equals(ActionFactory.SELECT_ALL.getId())) {
			rootAdapter.setSelected(false);
			rootAdapter.selectChildren();
			publishSelection();
		} else if (id.equals(EditorAction.PREVIEW)) {
			JComponent contentComponent = rootAdapter.cloneWidget();
			contentComponent.setPreferredSize(rootAdapter.getComponent().getSize());
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.add(contentComponent, BorderLayout.CENTER);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		} else {
			JComponent child = selection.get(0);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			CompositeAdapter parentAdapter = (CompositeAdapter) childAdapter.getParentAdapter();
			parentAdapter.doAlignment(action.getId());
		}
		repaint();
	}

	class DesignerLayout implements LayoutManager {
		public void addLayoutComponent(String name, Component comp) {
		}

		public void layoutContainer(Container parent) {
			if (root != null && rootBounds != null && designBorder != null) {
				int w = root == null ? 0 : root.getWidth();
				w = w <= 0 ? rootBounds.width : w;
				int h = root == null ? 0 : root.getHeight();
				h = h <= 0 ? rootBounds.height : h;
				Insets insets = designBorder.getBorderInsets(parent);
				container.setBounds(rootBounds.x - insets.left, rootBounds.y - insets.top, w + insets.left + insets.right, h + insets.top + insets.bottom);
				if (root != null) {
					root.setBounds(rootBounds.x, rootBounds.y, w, h);
				}
			}
			int w = parent.getWidth();
			int h = parent.getHeight();
			glass.setBounds(0, 0, w, h);
		}

		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(2 * rootBounds.x, 2 * rootBounds.y);
		}

		public Dimension preferredLayoutSize(Container parent) {
			int w = root == null ? rootBounds.width : root.getWidth();
			int h = root == null ? rootBounds.height : root.getHeight();
			w += 2 * rootBounds.x;
			h += 2 * rootBounds.y;
			return new Dimension(w, h);
		}

		public void removeLayoutComponent(Component comp) {
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (isAlignResize(0, null)) {
			boolean isMoved = false;
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
				isMoved = true;
			else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
				isMoved = true;
			if (isMoved) {
				WidgetSelection selection = new WidgetSelection((JComponent) root);
				JComponent child = selection.get(0);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				CompositeAdapter parentAdapter = (CompositeAdapter) childAdapter.getParentAdapter();
				parentAdapter.doKeyPressed(e);
				repaint();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public boolean isDirty() {
		if (root == null)
			return false;
		WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter((JComponent) root);
		return rootAdapter.isDirty();
	}

	public void fireDirty() {
		editor.fireDirty();
	}

	public void clearDirty() {
		if (root != null) {
			WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter((JComponent) root);
			rootAdapter.clearDirty();
			fireDirty();
		}
	}

	public void setLnfChanged(boolean b) {
		lnfChanged = b;
		if (lnfChanged)
			fireDirty();
	}

	public boolean isLnfChanged() {
		return lnfChanged;
	}

	private boolean lnfChanged;

	public void setFocus() {
		glass.setFocus();
	}
}
