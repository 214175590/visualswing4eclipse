/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.parser.listener;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.IEditorPart;

public interface IEventMethod {
	String getDisplayName();
	void editCode(IEditorPart editor);
	String createEventMethod(IType type, ImportRewrite imports);
	String createAddListenerCode();
}