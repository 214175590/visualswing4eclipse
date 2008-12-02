/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.parser.spi;

import java.awt.Component;
import java.lang.reflect.Field;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;

public interface IFieldParser {
	void parseField(CompilationUnit cunit, Component bean, Field field);
	boolean isDesigningField(IType type, IField field);
	boolean removeField(IType type, String fieldName, IProgressMonitor monitor);
}