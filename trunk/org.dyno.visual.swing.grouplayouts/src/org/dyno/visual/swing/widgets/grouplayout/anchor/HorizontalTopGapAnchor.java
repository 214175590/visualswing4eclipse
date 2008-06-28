package org.dyno.visual.swing.widgets.grouplayout.anchor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

public class HorizontalTopGapAnchor extends HorizontalAnchor {
	public HorizontalTopGapAnchor(JComponent target) {
		super(target);
	}

	@Override
	public Alignment createHoveredAxis(Component me, Rectangle bounds) {
		Container parent = target.getParent();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		Constraints constraints = layout.getConstraints(target);
		Alignment vertical = constraints.getVertical();
		if (vertical instanceof Leading) {
			return createVerticalLeading(me, bounds, parent);
		} else if (vertical instanceof Trailing) {
			return createVerticalTrailing(me, bounds, parent);
		} else if (vertical instanceof Bilateral) {
			return createVerticalLeading(me, bounds, parent);
		}
		return null;
	}

	@Override
	public Alignment createBottomAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		Container parent = target.getParent();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		Constraints constraints = layout.getConstraints(target);
		Alignment vertical = constraints.getVertical();
		if (vertical instanceof Leading) {
			return createVerticalLeading(me, bounds, parent);
		} else if (vertical instanceof Trailing) {
			if (lastAxis instanceof Leading) {
				return createVerticalSpring(me, bounds, parent);
			} else if (lastAxis instanceof Trailing) {
				return createVerticalTrailing(me, bounds, parent);
			} else if (lastAxis instanceof Bilateral) {
				return createVerticalSpring(me, bounds, parent);
			}
		} else if (vertical instanceof Bilateral) {
			return createVerticalLeading(me, bounds, parent);
		}
		return null;
	}

	@Override
	public Alignment createTopAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		Container parent = target.getParent();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		Constraints constraints = layout.getConstraints(target);
		Alignment vertical = constraints.getVertical();
		if (vertical instanceof Leading) {
			return createVerticalLeading(me, bounds, parent);
		} else if (vertical instanceof Trailing) {
			return createVerticalTrailing(me, bounds, parent);
		} else if (vertical instanceof Bilateral) {
			return createVerticalLeading(me, bounds, parent);
		}
		return null;
	}

}
