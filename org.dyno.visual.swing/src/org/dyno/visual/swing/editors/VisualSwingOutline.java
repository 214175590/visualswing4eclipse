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

package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IContextCustomizer;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * 
 * VisualSwingOutline
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VisualSwingOutline extends ContentOutlinePage implements ISelectionListener {
	private ComponentTreeInput input;
	private VisualDesigner designer;

	public VisualSwingOutline(VisualDesigner designer) {
		assert designer != null;
		this.designer = designer;
		this.input = new ComponentTreeInput(designer);
	}

	public void init(IPageSite pageSite) {
		super.init(pageSite);
		pageSite.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer treeView = getTreeViewer();
		getSite().setSelectionProvider(treeView);
		treeView.setContentProvider(new ComponentTreeContentProvider());
		treeView.setLabelProvider(new ComponentTreeLabelProvider());
		treeView.setInput(input);
		treeView.expandToLevel(2);
		Tree tree = (Tree) treeView.getTree();
		tree.addMenuDetectListener(new MenuDetectListener() {

			public void menuDetected(MenuDetectEvent e) {
				_showMenu(e);
			}
		});
		tree.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				_mouseDoubleClicked(e);
			}
		});
		tree.addListener(SWT.MeasureItem, new org.eclipse.swt.widgets.Listener() {

			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				event.height = 18;
			}
		});
		new OutlineViewDnD(designer).attach(treeView);
	}

	private void _mouseDoubleClicked(MouseEvent e) {
		Tree tree = (Tree) getTreeViewer().getTree();
		TreeItem item = tree.getItem(new Point(e.x, e.y));
		if (item != null) {
			if (item.getExpanded()) {
				getTreeViewer().collapseToLevel(item.getData(), 1);
			} else {
				getTreeViewer().expandToLevel(item.getData(), 1);
			}
			if (item.getData() instanceof EventMethod) {
				EventMethod eMethod = (EventMethod) item.getData();
				eMethod.editCode();
			}
		}
	}

	private List<Component> getSelectedComponent(TreeItem[] items) {
		List<Component> selected = new ArrayList<Component>();
		for (TreeItem item : items) {
			Object object = item.getData();
			if (object instanceof Component) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter((Component) object);
				if (adapter != null) {
					selected.add((Component) object);
				}
			}
		}
		return selected;
	}

	private List<IAdapter> getSelectedAdapters(TreeItem[] items) {
		List<IAdapter> selected = new ArrayList<IAdapter>();
		for (TreeItem item : items) {
			Object object = item.getData();
			if ((object instanceof IAdapter) && !(object instanceof InvisibleAdapter)) {
				selected.add((IAdapter) object);
			}
		}
		return selected;
	}

	private List<InvisibleAdapter> getSelectedInvisibles(TreeItem[] items) {
		List<InvisibleAdapter> selected = new ArrayList<InvisibleAdapter>();
		for (TreeItem item : items) {
			Object object = item.getData();
			if (object instanceof InvisibleAdapter) {
				selected.add((InvisibleAdapter) object);
			}
		}
		return selected;
	}

	private boolean isInvisibleRootSelected(TreeItem[] items) {
		for (TreeItem item : items) {
			Object object = item.getData();
			if (object != null && object instanceof String) {
				return true;
			}
		}
		return false;
	}

	private void _showMenu(MenuDetectEvent e) {
		Tree tree = (Tree) getTreeViewer().getTree();
		if (tree == null)
			return;
		TreeItem[] items = tree.getSelection();
		if (items == null || items.length == 0)
			return;
		List<Component> selected = getSelectedComponent(items);
		if (!selected.isEmpty()) {
			designer.showPopup(new java.awt.Point(e.x, e.y), selected, false);
		} else {
			fillInvisibleMenuItems(e);
		}
	}

	private void fillInvisibleMenuItems(MenuDetectEvent e) {
		Tree tree = (Tree) getTreeViewer().getTree();
		TreeItem[] items = tree.getSelection();
		MenuManager manager = new MenuManager("#OUTLINE_TREE_POPUP");
		if (isInvisibleRootSelected(items)) {
			List<IContextCustomizer> menuCustomizers = ExtensionRegistry.getContextCustomizers();
			for (IContextCustomizer context : menuCustomizers) {
				context.fillInvisibleRootMenu(manager, input.getRootAdapter());
			}
		}
		List<InvisibleAdapter> invisibles = getSelectedInvisibles(items);
		if (!invisibles.isEmpty()) {
			WidgetAdapter rootAdapter = input.getRootAdapter();
			List<IContextCustomizer> menuCustomizers = ExtensionRegistry.getContextCustomizers();
			for (IContextCustomizer context : menuCustomizers) {
				context.fillInvisibleAdapterMenu(manager, rootAdapter, invisibles);
			}
		}
		List<IAdapter> iadapters = getSelectedAdapters(items);
		if (!iadapters.isEmpty()) {
			WidgetAdapter rootAdapter = input.getRootAdapter();
			List<IContextCustomizer> menuCustomizers = ExtensionRegistry.getContextCustomizers();
			for (IContextCustomizer context : menuCustomizers) {
				context.fillIAdapterMenu(manager, rootAdapter, iadapters);
			}
		}
		int size = manager.getSize();
		if (size != 0) {
			Menu menu = manager.createContextMenu(tree);
			menu.setLocation(e.x, e.y);
			menu.setVisible(true);
		}
	}

	void refreshTree() {
		getTreeViewer().refresh();
	}

	private TreePath[] getTreePath(List<Component> components) {
		List<TreePath> paths = new ArrayList<TreePath>();
		for (Component component : components) {
			paths.add(buildTreePath(component));
		}
		TreePath[] array = new TreePath[paths.size()];
		return paths.toArray(array);
	}

	private TreePath buildTreePath(Component component) {
		List<Object> objects = new ArrayList<Object>();
		objects.add(input);
		objects.add(designer);
		addPathObject(objects, component);
		Object[] array = new Object[objects.size()];
		objects.toArray(array);
		TreePath treePath = new TreePath(array);
		return treePath;
	}

	private void addPathObject(List<Object> objects, Component component) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(component);
		if (adapter.isRoot()) {
			objects.add(component);
		} else {
			WidgetAdapter parent = adapter.getParentAdapter();
			addPathObject(objects, parent.getWidget());
			objects.add(component);
		}
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof VisualSwingEditor && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof WidgetSelection&&!getTreeViewer().getTree().isDisposed()) {
				getTreeViewer().refresh();
				TreePath[] paths = getTreePath((WidgetSelection) element);
				TreeSelection sel = new TreeSelection(paths);
				setSelection(sel);
			}
		}
	}
}
