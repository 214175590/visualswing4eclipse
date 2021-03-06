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

package org.dyno.visual.swing.editors;

import java.io.InputStream;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.contentTypes.VisualSwingContentDescriber;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

/**
 * 
 * AbstractDesignerEditor
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class AbstractDesignerEditor extends CompilationUnitEditor {
	@Override
	public boolean isSaveOnCloseNeeded() {		
		return super.isSaveOnCloseNeeded();
	}

	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setSite(site);
		setInput(input);
		if (shouldSwitchToJavaEditor() || !isSwingComponent()) {
			switchToJavaEditor();
		}
	}

	private boolean shouldSwitchToJavaEditor() throws PartInitException {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IProject proj = ((IFileEditorInput) input).getFile().getProject();
			try {
				if (!proj.hasNature(NATURE_ID)) {
					return true;
				}
			} catch (CoreException e) {
				VisualSwingPlugin.getLogger().error(e);
				return true;
			}
		} else
			throw new PartInitException(Messages.AbstractDesignerEditor_Illegal_File);
		return false;
	}

	public Display getDisplay() {
		return getShell().getDisplay();
	}

	public Shell getShell() {
		return getEditorSite().getShell();
	}

	protected boolean isSwingComponent() {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFileEditorInput file = (IFileEditorInput) input;
			IContentTypeManager contentTypeManager = Platform
					.getContentTypeManager();
			InputStream stream = null;
			try {
				stream = file.getFile().getContents();
				IContentType[] contentTypes = contentTypeManager
						.findContentTypesFor(stream, file.getName());
				for (IContentType contentType : contentTypes) {
					if (contentType.getId().equals(
							VisualSwingContentDescriber.CONTENT_TYPE_ID_VS)) {
						return true;
					}
				}
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (Exception e) {
						VisualSwingPlugin.getLogger().error(e);
					}
			}
		}
		return false;
	}

	protected void switchToJavaEditor() {
		getDisplay().asyncExec(new Runnable() {

			
			public void run() {
				IEditorInput input = getEditorInput();
				IWorkbenchPage page = getEditorSite().getWorkbenchWindow()
						.getActivePage();
				if (page != null) {
					page.closeEditor(AbstractDesignerEditor.this, false);
					try {
						page.openEditor(input, JAVA_EDITOR_ID);
						IFileEditorInput file_editor_input = (IFileEditorInput) input;
						IFile file = file_editor_input.getFile();
						IDE.setDefaultEditor(file, JAVA_EDITOR_ID);
					} catch (PartInitException e) {
						VisualSwingPlugin.getLogger().error(e);
					}
				}
			}
		});
	}

	protected void openRelatedView() {
		IWorkbenchPage page = getEditorSite().getPage();
		if (page != null) {
			for (String viewId : RELATED_VIEW_IDS) {
				try {
					page.showView(viewId);
				} catch (Exception e) {
					VisualSwingPlugin.getLogger().error(e);
				}
			}
		}
	}

	protected void closeMe() {
		getDisplay().asyncExec(new Runnable() {

			
			public void run() {
				IWorkbenchPage page = getEditorSite().getWorkbenchWindow()
						.getActivePage();
				if (page != null) {
					page.closeEditor(AbstractDesignerEditor.this, false);
				}
			}
		});
	}

	protected void closeRelatedView() {
		IWorkbenchPage page = getEditorSite().getPage();
		if (page != null) {
			IEditorReference[] editorRef = page.getEditorReferences();
			for (IEditorReference ref : editorRef) {
				try {
					IEditorPart editor = ref.getEditor(true);
					if (editor instanceof VisualSwingEditor)
						return;
				} catch (Exception e) {
				}
			}
			try {
				IViewPart part = page.findView(PaletteView.ID);
				page.hideView(part);
			} catch (Exception e) {
			}
		}
	}

	private static final String[] RELATED_VIEW_IDS = {
			PaletteView.ID, //$NON-NLS-1$
			"org.eclipse.ui.views.PropertySheet", //$NON-NLS-1$
			"org.eclipse.ui.views.ContentOutline" }; //$NON-NLS-1$
	private static final String JAVA_EDITOR_ID = "org.eclipse.jdt.ui.CompilationUnitEditor"; //$NON-NLS-1$
	private static final String NATURE_ID = "org.eclipse.jdt.core.javanature"; //$NON-NLS-1$
}
