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

package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.grouplayout.GroupLayoutAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class BottomAlignmentOperation extends AlignmentOperation {

	public BottomAlignmentOperation(JComponent container, GroupLayoutAdapter glAdapter) {
		super(Messages.BottomAlignmentOperation_Align_Bottom, container, glAdapter);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		WidgetAdapter post = widgets.get(0);
		Component postChild = post.getWidget();
		Constraints postConstraints = layout.getConstraints(postChild);
		Alignment postAlignment = postConstraints.getVertical();
		compcons = new ArrayList<CompCons>();
		if (postAlignment instanceof Trailing || postAlignment instanceof Bilateral) {
			int postTrail;
			if (postAlignment instanceof Trailing)
				postTrail = ((Trailing) postAlignment).getTrailing();
			else
				postTrail = ((Bilateral) postAlignment).getTrailing();
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				Alignment alignment = constraints.getVertical();
				if (alignment instanceof Trailing) {
					Trailing trailing = (Trailing) alignment.clone();
					trailing.setTrailing(postTrail);
					constraints = new Constraints(constraints.getHorizontal(), trailing);
				} else if (alignment instanceof Bilateral) {
					Bilateral bilateral = (Bilateral) alignment.clone();
					bilateral.setTrailing(postTrail);
					constraints = new Constraints(constraints.getHorizontal(), bilateral);
				} else if (alignment instanceof Leading) {
					Bilateral bilateral = new Bilateral(((Leading)alignment).getLeading(), postTrail, ((Leading) alignment).getSize());
					constraints = new Constraints(constraints.getHorizontal(), bilateral);
				}
				layout.setConstraints(child, constraints);
			}
		} else if (postAlignment instanceof Leading) {
			Leading postLeading = (Leading) postAlignment;
			int postLead = postLeading.getLeading() + postChild.getHeight();
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				Alignment alignment = constraints.getVertical();
				if (alignment instanceof Trailing) {
					int l = postLead - child.getHeight();
					Leading trailing = new Leading(l, 10, child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), trailing);
				} else if (alignment instanceof Bilateral) {
					int l = postLead - child.getHeight();
					Leading leading = new Leading(l, 10, child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), leading);
				} else if (alignment instanceof Leading) {
					Leading leading = (Leading) alignment.clone();
					leading.setLeading(postLead - child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), leading);
				}
				layout.setConstraints(child, constraints);
			}
		}
		container.invalidate();
		containerAdapter.doLayout();
		containerAdapter.setDirty(true);
		containerAdapter.repaintDesigner();
		return Status.OK_STATUS;
	}
}

