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

public class LeftAlignmentOperation extends AlignmentOperation {

	public LeftAlignmentOperation(JComponent container, GroupLayoutAdapter glAdapter) {
		super(Messages.LeftAlignmentOperation_Align_Left, container, glAdapter);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		WidgetAdapter post = widgets.get(0);
		Component postChild = post.getWidget();
		Constraints postConstraints = layout.getConstraints(postChild);
		Alignment postAlignment = postConstraints.getHorizontal();
		compcons = new ArrayList<CompCons>();
		if (postAlignment instanceof Leading || postAlignment instanceof Bilateral) {
			int postLead;
			if (postAlignment instanceof Leading)
				postLead = ((Leading) postAlignment).getLeading();
			else
				postLead = ((Bilateral) postAlignment).getLeading();
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				Alignment alignment = constraints.getHorizontal();
				if (alignment instanceof Leading) {
					Leading leading = (Leading) alignment.clone();
					leading.setLeading(postLead);
					constraints = new Constraints(leading, constraints.getVertical());
				} else if (alignment instanceof Bilateral) {
					Bilateral bilateral = (Bilateral) alignment.clone();
					bilateral.setLeading(postLead);
					constraints = new Constraints(bilateral, constraints.getVertical());
				} else if (alignment instanceof Trailing) {
					Bilateral bilateral = new Bilateral(postLead, ((Trailing) alignment).getTrailing(), ((Trailing) alignment).getSize());
					constraints = new Constraints(bilateral, constraints.getVertical());
				}
				layout.setConstraints(child, constraints);
			}
		} else if (postAlignment instanceof Trailing) {
			Trailing postTrailing = (Trailing) postAlignment;
			int postTrail = postTrailing.getTrailing() + postChild.getWidth();
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				Alignment alignment = constraints.getHorizontal();
				if (alignment instanceof Leading) {
					int t = postTrail - child.getWidth();
					Trailing trailing = new Trailing(t, 10, child.getWidth());
					constraints = new Constraints(trailing, constraints.getVertical());
				} else if (alignment instanceof Bilateral) {
					int t = postTrail - child.getWidth();
					Trailing trailing = new Trailing(t, 10, child.getWidth());
					constraints = new Constraints(trailing, constraints.getVertical());
				} else if (alignment instanceof Trailing) {
					Trailing trailing = (Trailing) alignment.clone();
					trailing.setTrailing(postTrail - child.getWidth());
					constraints = new Constraints(trailing, constraints.getVertical());
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

