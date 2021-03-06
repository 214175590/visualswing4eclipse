
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

package org.dyno.visual.swing.parser.listener;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

@SuppressWarnings("unchecked")
public abstract class AbstractClassModel implements IEventListenerModel, IConstants {
	protected EventSetDescriptor eventSet;
	protected WidgetAdapter adapter;

	
	public void init(WidgetAdapter adapter, EventSetDescriptor eventSet) {
		this.adapter = adapter;
		this.eventSet = eventSet;
	}

	public boolean parse(TypeDeclaration type){
		MethodDescriptor[] mListeners = eventSet.getListenerMethodDescriptors();
		boolean success = false;
		for (MethodDescriptor mListener : mListeners) {
			if (createEventMethod(adapter, eventSet, mListener, type))
				success = true;
		}
		return success;
	}

	private boolean createEventMethod(WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, TypeDeclaration type) {
		MethodDeclaration[] mds = type.getMethods();
		boolean success = false;
		for (MethodDeclaration md : mds) {
			String mdName = md.getName().getFullyQualifiedName();
			if (adapter.isRoot()) {
				if (mdName.equals(INIT_METHOD_NAME)) {
					if (createEventMethodForWidget(type, adapter, esd, mListener, md))
						success = true;
					break;
				}
			} else {
				String getName = NamespaceUtil.getGetMethodName(adapter, adapter.getID());
				if (mdName.equals(getName)) {
					if (createEventMethodForWidget(type, adapter, esd, mListener, md))
						success = true;
					break;
				}
			}
		}
		return success;
	}

	
	private boolean createEventMethodForWidget(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodDeclaration md) {
		Block body = md.getBody();
		List statements = body.statements();
		if (!adapter.isRoot()&&!statements.isEmpty()) {
			Object firstStatement = statements.get(0);
			if (firstStatement instanceof IfStatement) {
				IfStatement ifstatement = (IfStatement) firstStatement;
				Statement thenstatement = ifstatement.getThenStatement();
				if (thenstatement instanceof Block) {
					statements = ((Block) thenstatement).statements();
				}
			}
		}
		boolean success = false;
		for (Object stmt : statements) {
			Statement statement = (Statement) stmt;
			if (statement instanceof ExpressionStatement) {
				if (processWidgetCreationStatement(type, adapter, esd, mListener, statement))
					success = true;
			}
		}
		return success;
	}

	private boolean processWidgetCreationStatement(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, Statement statement) {
		ExpressionStatement expressionStatement = (ExpressionStatement) statement;
		Expression expression = expressionStatement.getExpression();
		if (expression instanceof MethodInvocation) {
			MethodInvocation mi = (MethodInvocation) expression;
			if (adapter.isRoot()) {
				return createAddMethod(type, adapter, esd, mListener, mi);
			} else {
				Expression optionalExpression = mi.getExpression();
				if (optionalExpression instanceof SimpleName) {
					SimpleName simplename = (SimpleName) optionalExpression;
					String idName = simplename.getFullyQualifiedName();
					if (idName.equals(adapter.getID()))
						return createAddMethod(type, adapter, esd, mListener, mi);
					else
						return false;
				} else
					return false;
			}
		} else
			return false;
	}

	private boolean createAddMethod(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodInvocation mi) {
		Method addm = esd.getAddListenerMethod();
		String addmName = addm.getName();
		String mName = mi.getName().getFullyQualifiedName();
		if (mName.equals(addmName)) {
			return processAddListenerStatement(type, adapter, esd, mListener, mi);
		} else
			return false;
	}
	protected abstract boolean processAddListenerStatement(TypeDeclaration type, WidgetAdapter adapter, EventSetDescriptor esd, MethodDescriptor mListener, MethodInvocation mi);
}

