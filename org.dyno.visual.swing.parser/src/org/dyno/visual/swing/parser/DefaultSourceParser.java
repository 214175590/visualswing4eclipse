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

package org.dyno.visual.swing.parser;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.beans.EventSetDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.ISyncUITask;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.parser.spi.IFieldParser;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.parser.spi.IWidgetASTParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.ParserException;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * DefaultSourceParser
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
class DefaultSourceParser implements ISourceParser, IConstants {
	private static final String FIELD_PARSER_EXTENSION_ID = "org.dyno.visual.swing.parser.fieldParser"; //$NON-NLS-1$
	private DefaultParserFactory factory;
	private List<IFieldParser> fieldParsers;

	DefaultSourceParser(DefaultParserFactory factory) {
		this.factory = factory;
		fieldParsers = new ArrayList<IFieldParser>();
		parseFieldParsers();
	}

	private void parseFieldParsers() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(FIELD_PARSER_EXTENSION_ID);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseFieldParser(extensions[i]);
				}
			}
		}
	}

	private void parseFieldParser(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("parser")) { //$NON-NLS-1$
					try {
						fieldParsers.add((IFieldParser) configs[i].createExecutableExtension("class")); //$NON-NLS-1$
					} catch (CoreException e) {
						ParserPlugin.getLogger().error(e);
					}
				}
			}
		}
	}

	
	public WidgetAdapter parse(ICompilationUnit unit, IProgressMonitor monitor) throws ParserException {
		try {
			IType[] types = unit.getPrimary().getAllTypes();
			for (IType type : types) {
				if (type.isClass() && Flags.isPublic(type.getFlags())) {
					WidgetAdapter result = processType(unit.getPrimary(), type);
					if (result != null)
						return result;
				}
			}
		} catch (JavaModelException jme) {
			ParserPlugin.getLogger().error(jme);
			throw new ParserException(jme);
		}
		return null;
	}
	private static synchronized Object runWithLnf(LookAndFeel lnf,
			ISyncUITask task) throws Throwable {
		UIManager.setLookAndFeel(lnf);
		return task.doTask();
	}
	private WidgetAdapter processType(ICompilationUnit unit, IType type) throws ParserException {
		try {
			IJavaProject java_project = type.getJavaProject();
			String className = type.getFullyQualifiedName();
			final Class<?> beanClass = JavaUtil.getProjectClassLoader(java_project).loadClass(className);
			if (Component.class.isAssignableFrom(beanClass)) {
				String lnf = getBeanClassLnf(beanClass);
				ILookAndFeelAdapter lnfAdapter = ExtensionRegistry.getLnfAdapter(lnf);
				if (lnfAdapter != null) {
					LookAndFeel newlnf = lnfAdapter.getLookAndFeelInstance();
					Component bean = (Component) runWithLnf(newlnf, new ISyncUITask() {
						
						public Object doTask() throws Throwable {
							return createBeanFromClass(beanClass);
						}
					});
					WidgetAdapter beanAdapter = ExtensionRegistry.createWidgetAdapter(bean);
					ASTParser parser = ASTParser.newParser(AST.JLS3);
					parser.setSource(unit);
					CompilationUnit cunit = (CompilationUnit) parser.createAST(null);
					parseEventListener(cunit, beanAdapter);
					initDesignedWidget(cunit, bean);
					parsePropertyValue(lnf, cunit, beanAdapter);
					beanAdapter.clearDirty();
					return beanAdapter;
				}
			} else {
				throw new ParserException("This is not a swing class!");
			}
		} catch (ParserException pe) {
			throw pe;
		} catch (Throwable e) {
			ParserPlugin.getLogger().error(e);
			throw new ParserException(e);
		}
		return null;
	}

	private Object createBeanFromClass(Class beanClass) throws Throwable {
		try {
			Constructor cons = beanClass.getConstructor(new Class[0]);
			cons.setAccessible(true);
			return cons.newInstance();
		} catch (NoSuchMethodException e) {
			try {
				Constructor cons = beanClass.getConstructor(new Class[] { Frame.class });
				cons.setAccessible(true);
				return cons.newInstance(new Frame());
			} catch (NoSuchMethodException ex) {
				try {
					Constructor cons = beanClass.getConstructor(new Class[] { String.class });
					cons.setAccessible(true);
					return cons.newInstance(new String());
				} catch (NoSuchMethodException exx) {
					return null;
				}
			}
		}
	}

	private void parsePropertyValue(String lnfClassname, CompilationUnit cunit, WidgetAdapter adapter) {
		TypeDeclaration type = (TypeDeclaration) cunit.types().get(0);
		IWidgetASTParser widgetParser = (IWidgetASTParser) adapter.getAdapter(IWidgetASTParser.class);
		widgetParser.parse(lnfClassname, type);
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int count = compositeAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = compositeAdapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				parsePropertyValue(lnfClassname, cunit, childAdapter);
			}
		}
	}

	private void initDesignedWidget(CompilationUnit cunit, Component bean) throws ParserException {
		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object fieldValue = field.get(bean);
				if (fieldValue != null) {
					if (Component.class.isAssignableFrom(field.getType())) {
						parseField(cunit, bean, field);
					} else {
						for (IFieldParser parser : fieldParsers) {
							parser.parseField(cunit, bean, field);
						}
					}
				}
			} catch (ParserException e) {
				throw e;
			} catch (Exception e) {
				ParserPlugin.getLogger().error(e);
				throw new ParserException(e);
			}
		}
	}

	private void parseField(CompilationUnit cunit, Component bean, Field field) throws ParserException {
		Class clazz = bean.getClass();
		String fieldName = field.getName();
		field.setAccessible(true);
		Object fieldValue = null;
		try {
			fieldValue = field.get(bean);
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
			throw new ParserException(e);
		}

		JComponent fieldComponent = (JComponent) fieldValue;
		WidgetAdapter adapter = ExtensionRegistry.createWidgetAdapter(fieldComponent, true);
		if (adapter.getWidget() != fieldComponent && fieldComponent instanceof JPopupMenu && adapter.getWidget() instanceof JPopupMenu) {
			JComponent jcomp = findPopupInvoker((JPopupMenu) fieldComponent, bean);
			if (jcomp != null) {
				jcomp.setComponentPopupMenu((JPopupMenu) adapter.getWidget());
			}
		}
		adapter.setName(fieldName);
		adapter.setLastName(fieldName);
		int flags = field.getModifiers();
		setAdapterFieldAccess(adapter, flags);

		String getName = NamespaceUtil.getGetMethodName(fieldName);
		Method getMethod = null;
		try {
			getMethod = clazz.getDeclaredMethod(getName);
		} catch (NoSuchMethodException nsme) {
			getName = NamespaceUtil.getGetMethodName(cunit, fieldName);
			if (getName == null)
				throw new ParserException("Method " + NamespaceUtil.getGetMethodName(fieldName) + "() is not found!\n" + "Please define it to initialize " + fieldName);
			try {
				getMethod = clazz.getDeclaredMethod(getName);
				WidgetAdapter ba = WidgetAdapter.getWidgetAdapter(fieldComponent);
				ba.setProperty("getMethodName", getName);
			} catch (NoSuchMethodException e) {
				throw new ParserException("Method " + getName + "() is not found!\n" + "Please define it to initialize " + fieldName);
			}
		}

		flags = getMethod.getModifiers();
		setAdapterGetMethodAccess(adapter, flags);
		parseEventListener(cunit, adapter);
	}

	private JComponent findPopupInvoker(JPopupMenu popup, Component bean) {
		if (bean instanceof Container) {
			if (bean instanceof JComponent) {
				JComponent jcomp = (JComponent) bean;
				if (JavaUtil.getComponentPopupMenu(jcomp) == popup)
					return jcomp;
			}
			Container container = (Container) bean;
			int count = container.getComponentCount();
			for (int i = 0; i < count; i++) {
				Component child = container.getComponent(i);
				JComponent p = findPopupInvoker(popup, child);
				if (p != null)
					return p;
			}
		}
		return null;
	}

	private void setAdapterGetMethodAccess(WidgetAdapter adapter, int flags) {
		if (Modifier.isPrivate(flags)) {
			adapter.setGetAccess(WidgetAdapter.ACCESS_PRIVATE);
		} else if (Modifier.isProtected(flags)) {
			adapter.setGetAccess(WidgetAdapter.ACCESS_PROTECTED);
		} else if (Modifier.isPublic(flags)) {
			adapter.setGetAccess(WidgetAdapter.ACCESS_PUBLIC);
		} else {
			adapter.setGetAccess(WidgetAdapter.ACCESS_DEFAULT);
		}
	}

	private void setAdapterFieldAccess(WidgetAdapter adapter, int flags) {
		if (Modifier.isPrivate(flags)) {
			adapter.setFieldAccess(WidgetAdapter.ACCESS_PRIVATE);
		} else if (Modifier.isProtected(flags)) {
			adapter.setFieldAccess(WidgetAdapter.ACCESS_PROTECTED);
		} else if (Modifier.isPublic(flags)) {
			adapter.setFieldAccess(WidgetAdapter.ACCESS_PUBLIC);
		} else {
			adapter.setFieldAccess(WidgetAdapter.ACCESS_DEFAULT);
		}
	}

	private static String getBeanClassLnf(Class beanClass) {
		try {
			Field field = beanClass.getDeclaredField("PREFERRED_LOOK_AND_FEEL"); //$NON-NLS-1$
			if (field.getType() == String.class) {
				field.setAccessible(true);
				String lnf = (String) field.get(null);
				String className = UIManager.getCrossPlatformLookAndFeelClassName();
				if (lnf == null) {
					lnf = className;
				}
				return lnf;
			}
		} catch (NoSuchFieldException nsfe) {
		} catch (Exception e) {
			ParserPlugin.getLogger().warning(e);
		}
		return UIManager.getCrossPlatformLookAndFeelClassName();
	}

	private void parseEventListener(CompilationUnit cunit, WidgetAdapter adapter) throws ParserException {
		EventSetDescriptor[] esds = adapter.getBeanInfo().getEventSetDescriptors();
		TypeDeclaration type = (TypeDeclaration) cunit.types().get(0);
		if (esds != null && esds.length > 0) {
			for (EventSetDescriptor esd : esds) {
				factory.parseEventListener(adapter, type, esd);
			}
		}
	}

	private IType getUnitMainType(ICompilationUnit unit) {
		String unit_name = unit.getElementName();
		int dot = unit_name.lastIndexOf('.');
		if (dot != -1)
			unit_name = unit_name.substring(0, dot);
		IType type = unit.getType(unit_name);
		return type;
	}

	public static ImportRewrite createImportRewrite(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit);
		parser.setResolveBindings(false);
		parser.setFocalPosition(0);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return CodeStyleConfiguration.createImportRewrite(cu, true);
	}

	
	public ICompilationUnit generate(WidgetAdapter root, IProgressMonitor monitor) {
		try {
			IParser parser = (IParser) root.getAdapter(IParser.class);
			if (parser == null)
				return null;
			ICompilationUnit unit = root.getCompilationUnit();
			ICompilationUnit copy = unit.getWorkingCopy(monitor);
			IType type = getUnitMainType(copy);
			if (type != null) {
				ImportRewrite imports = createImportRewrite(copy);
				boolean success = parser.generateCode(type, imports, monitor);
				if (!success)
					return null;
				removeRemovedComponent(root, monitor, unit, type);
				createPreferredLnf(root, monitor, type, imports);
				if (success) {
					TextEdit edit = imports.rewriteImports(monitor);
					JavaUtil.applyEdit(copy, edit, true, monitor);
				}
				if (copy.isWorkingCopy()) {
					copy.commitWorkingCopy(true, monitor);
					copy.discardWorkingCopy();
				}
				IWorkbenchPartSite site = getEditorSite();
				if (site != null) {
					OrganizeImportsAction action = new OrganizeImportsAction(site);
					action.run(unit);
				}
				type = getUnitMainType(unit);
				rename(type, root);
				if (unit.isWorkingCopy()) {
					unit.commitWorkingCopy(true, monitor);
				}
				return unit;
			} else
				return null;
		} catch (Exception e) {
			ParserPlugin.getLogger().error(e);
			return null;
		}
	}

	private void createPreferredLnf(WidgetAdapter root, IProgressMonitor monitor, IType type, ImportRewrite imports) throws JavaModelException {
		IField lnfField = type.getField("PREFERRED_LOOK_AND_FEEL"); //$NON-NLS-1$		
		String className = (String) root.getPreferredLookAndFeel();
		if (lnfField.exists()) {
			lnfField.delete(false, monitor);
			createLnfField(monitor, type, imports, className);
		} else if (!isCross(className)) {
			createLnfField(monitor, type, imports, className);
		}
	}

	private void createLnfField(IProgressMonitor monitor, IType type, ImportRewrite imports, String className) throws JavaModelException {
		String newfield = "private static final " //$NON-NLS-1$
				+ imports.addImport("java.lang.String") //$NON-NLS-1$
				+ " PREFERRED_LOOK_AND_FEEL = " //$NON-NLS-1$
				+ (className == null ? "null" : "\"" + className + "\"") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ";\n"; //$NON-NLS-1$
		type.createField(newfield, null, false, monitor);
	}

	private boolean isCross(String className) {
		String cross = UIManager.getCrossPlatformLookAndFeelClassName();
		return className == null || className.equals(cross);
	}

	private void removeRemovedComponent(WidgetAdapter root, IProgressMonitor monitor, ICompilationUnit unit, IType type) {
		List<String> removedNames = (List<String>) root.getProperty("removed.components");
		if (removedNames != null) {
			List<String> nonExistingFields = new ArrayList<String>();
			for (String name : removedNames) {
				IField field = type.getField(name);
				if (field == null || !field.exists())
					nonExistingFields.add(name);
			}
			for (String nonfield : nonExistingFields) {
				removedNames.remove(nonfield);
			}
			ASTParser astparser = ASTParser.newParser(AST.JLS3);
			astparser.setSource(unit);
			CompilationUnit cunit = (CompilationUnit) astparser.createAST(null);
			List<String> getmethods = new ArrayList<String>();
			for (int i = 0; i < removedNames.size(); i++) {
				String removed_name = removedNames.get(i);
				String getmethod = NamespaceUtil.getGetMethodName(cunit, removed_name);
				getmethods.add(getmethod);
			}
			for (int i = 0; i < removedNames.size(); i++) {
				String removed_name = removedNames.get(i);
				String getmethod = getmethods.get(i);
				removeField(type, removed_name, getmethod, monitor);
			}
		}
	}

	private void rename(IType type, WidgetAdapter root) {
		if (root.getLastName() != null && root.getName() != null && !root.getLastName().equals(root.getName())) {
			IParser parser = (IParser) root.getAdapter(IParser.class);
			if (parser != null) {
				parser.renameField(type, null);
			}
		}
		if (root.isRoot()) {
			for (InvisibleAdapter invisible : root.getInvisibles()) {
				renameInvisible(type, invisible);
			}
		}
		if (root instanceof CompositeAdapter) {
			CompositeAdapter container = (CompositeAdapter) root;
			int count = container.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = container.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				rename(type, childAdapter);
			}
		}
	}

	private void renameInvisible(IType type, InvisibleAdapter root) {
		if (root.getLastName() != null && root.getName() != null && !root.getLastName().equals(root.getName())) {
			IParser parser = (IParser) root.getAdapter(IParser.class);
			if (parser != null) {
				parser.renameField(type, null);
			}
		}
	}

	private IWorkbenchPartSite getEditorSite() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IEditorPart editor = page.getActiveEditor();
					if (editor != null)
						return editor.getSite();
				}
			}
		}
		return null;
	}

	private void removeField(IType type, String fieldName, String methodName, IProgressMonitor monitor) {
		IField field = type.getField(fieldName);
		if (field != null && field.exists()) {
			try {
				field.delete(true, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				return;
			}
		}
		IMethod method = type.getMethod(methodName, new String[0]);
		if (method != null && method.exists()) {
			try {
				method.delete(true, monitor);
				return;
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
			}
		} else {
			for (IFieldParser parser : fieldParsers) {
				if (parser.removeField(type, fieldName, monitor))
					return;
			}
		}
	}
}
