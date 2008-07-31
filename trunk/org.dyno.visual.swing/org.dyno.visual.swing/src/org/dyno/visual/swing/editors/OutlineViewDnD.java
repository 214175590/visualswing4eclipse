/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

class OutlineViewDnD extends DropTargetAdapter implements DragSourceListener {
	private TreeViewer treeView;
	private Tree tree;
	private TreeItem[] treeItems;
	private Display display;
	private VisualDesigner designer;

	public OutlineViewDnD(VisualDesigner designer) {
		this.designer = designer;
	}

	public void attach(TreeViewer treeView) {
		this.treeView = treeView;
		this.tree = treeView.getTree();
		display = tree.getDisplay();

		Transfer[] types = new Transfer[] { new JComponentTransfer() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		final DragSource source = new DragSource(tree, operations);
		source.setTransfer(types);
		treeItems = new TreeItem[1];
		source.addDragListener(this);

		DropTarget target = new DropTarget(tree, operations);
		target.setTransfer(types);
		target.addDropListener(this);
	}

	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE) {
			for (TreeItem item : treeItems)
				item.dispose();
			treeView.refresh();
			designer.repaint();
			designer.publishSelection();
		}
		treeItems = null;
	}

	public void dragSetData(DragSourceEvent event) {
		JComponent[] components = new JComponent[treeItems.length];
		for (int i = 0; i < treeItems.length; i++) {
			components[i] = (JComponent) treeItems[i].getData();
		}
		event.data = components;
	}

	public void dragStart(DragSourceEvent event) {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0) {
			Container parent = null;
			for (TreeItem item : selection) {
				Object object = item.getData();
				if (!(object instanceof JComponent)) {
					event.doit = false;
					return;
				} else {
					JComponent comp = (JComponent) object;
					WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
					if (adapter.isRoot()) {
						event.doit = false;
						return;
					} else {
						if (parent == null)
							parent = comp.getParent();
						else if (parent != comp.getParent()) {
							event.doit = false;
							return;
						}
					}
				}
			}
			treeItems = selection;
			event.doit = true;
		} else {
			event.doit = false;
		}
	}

	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
		if (event.item != null) {
			TreeItem item = (TreeItem) event.item;
			Point pt = display.map(null, tree, event.x, event.y);
			Rectangle bounds = item.getBounds();
			if (pt.y < bounds.y + bounds.height / 4) {
				event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
			} else if (pt.y > bounds.y + 3 * bounds.height / 4) {
				event.feedback |= DND.FEEDBACK_INSERT_AFTER;
			} else {
				event.feedback |= DND.FEEDBACK_SELECT;
			}
		}
	}

	private void moveToSelectedNode(CompositeAdapter parent_adapter, JComponent target_comp, DropTargetEvent event, JComponent[] components) {
		if (parent_adapter.getWidget() == target_comp) {
			event.detail = DND.DROP_NONE;
		} else {
			WidgetAdapter target_adapter = WidgetAdapter.getWidgetAdapter(target_comp);
			CompositeAdapter target_parent = target_adapter.getParentAdapter();
			if (target_parent == parent_adapter) {
				if (!(target_adapter instanceof CompositeAdapter)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (JComponent component : components) {
				if (component == target_comp) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (SwingUtilities.isDescendingFrom(target_comp, component)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (JComponent component : components) {
				parent_adapter.removeChild(component);
				if (target_adapter instanceof CompositeAdapter)
					((CompositeAdapter) target_adapter).addChild(component);
			}
			target_adapter.getRootAdapter().getWidget().validate();
		}
	}

	private void moveBeforeSelectedNode(CompositeAdapter parent_adapter, JComponent target_comp, DropTargetEvent event, JComponent[] components) {
		if (parent_adapter.getWidget() == target_comp) {
			event.detail = DND.DROP_NONE;
		} else {
			WidgetAdapter target_adapter = WidgetAdapter.getWidgetAdapter(target_comp);
			CompositeAdapter target_parent = target_adapter.getParentAdapter();
			for (JComponent component : components) {
				if (component == target_comp) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (SwingUtilities.isDescendingFrom(target_comp, component)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (JComponent component : components) {
				parent_adapter.removeChild(component);
				target_parent.addBefore(target_comp, component);
			}
		}
	}

	private void moveAfterSelectedNode(CompositeAdapter parent_adapter, JComponent target_comp, DropTargetEvent event, JComponent[] components) {
		if (parent_adapter.getWidget() == target_comp) {
			event.detail = DND.DROP_NONE;
		} else {
			WidgetAdapter target_adapter = WidgetAdapter.getWidgetAdapter(target_comp);
			CompositeAdapter target_parent = target_adapter.getParentAdapter();
			for (JComponent component : components) {
				if (component == target_comp) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (SwingUtilities.isDescendingFrom(target_comp, component)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
			for (JComponent component : components) {
				parent_adapter.removeChild(component);
				target_parent.addAfter(target_comp, component);
			}
		}
	}

	public void drop(DropTargetEvent event) {
		if (event.data == null) {
			event.detail = DND.DROP_NONE;
			return;
		} else if (event.item == null) {
			event.detail = DND.DROP_NONE;
		} else {
			TreeItem target_item = (TreeItem) event.item;
			Object data = target_item.getData();
			if (!(data instanceof JComponent)) {
				event.detail = DND.DROP_NONE;
			} else {
				JComponent[] components = (JComponent[]) event.data;
				if (components.length == 0) {
					event.detail = DND.DROP_NONE;
				} else {
					JComponent first = components[0];
					WidgetAdapter first_adapter = WidgetAdapter.getWidgetAdapter(first);
					CompositeAdapter parent_adapter = first_adapter.getParentAdapter();
					JComponent target_comp = (JComponent) data;
					if ((event.feedback & DND.FEEDBACK_SELECT) != 0) {
						moveToSelectedNode(parent_adapter, target_comp, event, components);
					} else if ((event.feedback & DND.FEEDBACK_INSERT_AFTER) != 0) {
						moveBeforeSelectedNode(parent_adapter, target_comp, event, components);
					} else if ((event.feedback & DND.FEEDBACK_INSERT_BEFORE) != 0) {
						moveAfterSelectedNode(parent_adapter, target_comp, event, components);
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
			}
		}
	}
}