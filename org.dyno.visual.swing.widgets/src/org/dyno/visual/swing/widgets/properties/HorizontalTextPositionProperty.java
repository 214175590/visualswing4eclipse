
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

package org.dyno.visual.swing.widgets.properties;

import org.dyno.visual.swing.base.WidgetProperty;
import org.eclipse.jface.viewers.IStructuredSelection;

public class HorizontalTextPositionProperty extends WidgetProperty {
	@Override
	public boolean isPropertySet(String lnfClassname, IStructuredSelection bean) {
		return false;
	}

}

