package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import org.dyno.visual.swing.base.ResourceIcon;
import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

@SuppressWarnings("unchecked")
public class JTabbedPaneASTParser extends CompositeASTParser {
	@Override
	protected void parseAddStatement(String lnfClassname, MethodInvocation mi) {
		String addMethodName = mi.getName().getFullyQualifiedName();
		if (addMethodName.equals("addTab")) {
			JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
			List arguments = mi.arguments();
			if (arguments.size() > 2) {
				Component child = getTabComponent(mi, arguments);
				int index = jtp.indexOfComponent(child);
				if (index != -1) {
					Icon icon = jtp.getIconAt(index);
					if (icon != null) {
						Expression exp = (Expression) arguments.get(1);
						icon = parseIcon(icon, exp);
						jtp.setIconAt(index, icon);
					}
				}
			}
		}
	}

	private Component getTabComponent(MethodInvocation methodInvocation,
			List arguments) {
		Expression exp = (Expression) arguments.get(2);
		if (exp != null && exp instanceof MethodInvocation) {
			MethodInvocation mi = (MethodInvocation) exp;
			String getMethodName = mi.getName().getFullyQualifiedName();
			String fieldName = getFieldNameFromGetMethodName(methodInvocation,
					getMethodName);
			if (fieldName != null) {
				CompositeAdapter tabAdapter = (CompositeAdapter) adaptable;
				int count = tabAdapter.getChildCount();
				for (int i = 0; i < count; i++) {
					Component child = tabAdapter.getChild(i);
					WidgetAdapter childAdapter = WidgetAdapter
							.getWidgetAdapter(child);
					if (childAdapter.getName().equals(fieldName))
						return child;
				}
			}
		}
		return null;
	}

	private String getFieldNameFromGetMethodName(
			MethodInvocation methodInvocation, String getMethodName) {
		ASTNode node = methodInvocation;
		while (node != null && !(node instanceof TypeDeclaration)) {
			node = node.getParent();
		}
		if (node == null)
			return NamespaceUtil.getFieldNameFromGetMethodName(getMethodName);
		TypeDeclaration type = (TypeDeclaration) node;
		MethodDeclaration[] methods = type.getMethods();
		for (MethodDeclaration method : methods) {
			String methodName = method.getName().getFullyQualifiedName();
			if (!method.isConstructor()&&methodName.equals(getMethodName)) {
				String fName = NamespaceUtil.findReturnFieldName(method);
				return fName;
			}
		}
		return null;
	}
	private Icon parseIcon(Icon oldValue, Expression arg) {
		if (oldValue != null && !(oldValue instanceof ResourceIcon)) {
			Icon icon = (Icon) oldValue;
			if (arg instanceof ClassInstanceCreation) {
				ClassInstanceCreation instanceCreation = (ClassInstanceCreation) arg;
				List args = instanceCreation.arguments();
				arg = (Expression) args.get(0);
				if (arg instanceof MethodInvocation) {
					MethodInvocation mi = (MethodInvocation) arg;
					args = mi.arguments();
					arg = (Expression) args.get(0);
					if (arg instanceof StringLiteral) {
						StringLiteral sl = (StringLiteral) arg;
						String path = sl.getLiteralValue();
						return new ResourceIcon(icon, path);
					}
				}
			}
		}
		return oldValue;
	}
}
