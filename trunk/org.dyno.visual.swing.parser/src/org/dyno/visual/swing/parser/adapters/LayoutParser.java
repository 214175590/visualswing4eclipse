package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.parser.spi.ILayoutParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapterContext;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public abstract class LayoutParser implements ILayoutParser, IAdapterContext{
	@Override
	public String createCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(layoutAdapter.getContainer());
		if (adapter.isRoot())
			builder.append("setLayout(");
		else
			builder.append(getFieldName(adapter.getName()) + ".setLayout(");
		builder.append(getNewInstanceCode(imports));
		builder.append(");\n");
		CompositeAdapter conAdapter = (CompositeAdapter) adapter;
		int count = conAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = conAdapter.getChild(i);
			builder.append(getAddChildCode(layoutAdapter, child, imports));
		}
		return builder.toString();
	}

	protected LayoutAdapter layoutAdapter;
	@Override
	public void setAdapter(IAdapter adapter) {
		this.layoutAdapter = (LayoutAdapter)adapter;
	}



	protected String getChildConstraints(Component child, ImportRewrite imports) {
		return null;
	}

	protected String getFieldName(String name) {
		return NamespaceManager.getInstance().getFieldName(name);
	}

	protected abstract String getNewInstanceCode(ImportRewrite imports);

	protected String getAddChildCode(LayoutAdapter adapter, Component child, ImportRewrite imports) {
		String constraints = getChildConstraints(child, imports);
		StringBuilder builder = new StringBuilder();
		WidgetAdapter conAdapter = WidgetAdapter.getWidgetAdapter(adapter.getContainer());
		WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
		if (conAdapter.isRoot()) {
			builder.append("add(");
		} else {
			builder.append(getFieldName(conAdapter.getName()) + ".add(");
		}
		builder.append(childAdapter.getCreationMethodName()+"()");
		if (constraints != null) {
			builder.append(", " + constraints);
		}
		builder.append(");\n");
		return builder.toString();
	}
}
