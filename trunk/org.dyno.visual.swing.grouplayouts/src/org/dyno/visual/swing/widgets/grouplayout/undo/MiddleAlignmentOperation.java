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
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class MiddleAlignmentOperation extends AlignmentOperation {

	public MiddleAlignmentOperation(JComponent container) {
		super("Align Middle", container);
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
		if (postAlignment instanceof Leading) {
			int postCenter = postChild.getY() + postChild.getHeight() / 2;
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				int l = postCenter - child.getHeight() / 2;
				int t = container.getHeight() - postCenter - child.getHeight() / 2;
				Leading leading = new Leading(l, child.getHeight(), 10, t);
				constraints = new Constraints(constraints.getHorizontal(), leading);
				layout.setConstraints(child, constraints);
			}
		} else if (postAlignment instanceof Bilateral) {
			int postCenter = postChild.getY() + postChild.getHeight() / 2;
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				int l = postCenter - child.getHeight() / 2;
				int t = container.getHeight() - postCenter - child.getHeight()/2;
				Bilateral bilateral = new Bilateral(l, t, 10);
				constraints = new Constraints(constraints.getHorizontal(), bilateral);
				layout.setConstraints(child, constraints);
			}
		} else if (postAlignment instanceof Trailing) {
			int postCenter = postChild.getY() + postChild.getHeight()/2;
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				int l = postCenter - child.getHeight()/2;
				int t = container.getHeight() - postCenter - child.getHeight()/2;
				Trailing trailing= new Trailing(t, child.getHeight(), 10, l);
				constraints = new Constraints(constraints.getHorizontal(), trailing);
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
