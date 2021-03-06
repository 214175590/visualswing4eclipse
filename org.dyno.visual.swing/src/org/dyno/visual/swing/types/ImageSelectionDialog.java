package org.dyno.visual.swing.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ImageSelectionDialog extends Dialog {
	private TreeViewer view;
	private Label label;

	private class ProjectTreeContent implements ITreeContentProvider {
		
		public Object[] getChildren(Object parentElement) {
			try {
				if (parentElement instanceof IJavaProject) {
					IJavaProject prj = (IJavaProject) parentElement;
					IJavaElement[] children = prj.getChildren();
					List<Object> list = new ArrayList<Object>();
					for (IJavaElement jElement : children) {
						if (jElement instanceof IPackageFragmentRoot) {
							IPackageFragmentRoot pkgRoot = (IPackageFragmentRoot) jElement;
							if (!pkgRoot.isArchive())
								list.add(jElement);
						}
					}
					return list.toArray();
				} else if (parentElement instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot packageRoot = (IPackageFragmentRoot) parentElement;
					IJavaElement[] children = packageRoot.getChildren();
					Object[]nonJavaResources=packageRoot.getNonJavaResources();
					List<Object>list = new ArrayList<Object>();
					if(children!=null){
						for(Object child:children){
							list.add(child);
						}
					}
					if(nonJavaResources!=null){
						for(Object resource:nonJavaResources){
							if(resource instanceof IFile){
								IFile file = (IFile)resource;
								String name = file.getName();
								name=name.toLowerCase();
								if(name.endsWith(".png")||name.endsWith(".gif")||name.endsWith(".jpg")){
									list.add(file);
								}
							}
						}
					}
					return list.toArray();					
				} else if (parentElement instanceof IPackageFragment) {
					IPackageFragment pkg = (IPackageFragment) parentElement;
					Object[] nonJavaResources = pkg.getNonJavaResources();
					List<Object> list = new ArrayList<Object>();
					for (Object resource : nonJavaResources) {
						if (resource instanceof IFile) {
							IFile file = (IFile) resource;
							String name = file.getName();
							if (name != null) {
								name = name.toLowerCase();
								if (name.endsWith(".gif")
										|| name.endsWith(".png")
										|| name.endsWith(".jpg")) {
									list.add(file);
								}
							}
						}
					}
					return list.toArray();
				}
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
			return new Object[0];
		}

		
		public Object getParent(Object element) {
			return null;
		}

		
		public boolean hasChildren(Object element) {
			try {
				IJavaElement[] children = null;
				if (element instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot packageRoot = (IPackageFragmentRoot) element;
					children = packageRoot.getChildren();
					Object[]nonJavaResources=packageRoot.getNonJavaResources();
					List<Object>list = new ArrayList<Object>();
					if(children!=null){
						for(Object child:children){
							list.add(child);
						}
					}
					if(nonJavaResources!=null){
						for(Object resource:nonJavaResources){
							if(resource instanceof IFile){
								IFile file = (IFile)resource;
								String name = file.getName();
								name=name.toLowerCase();
								if(name.endsWith(".png")||name.endsWith(".gif")||name.endsWith(".jpg")){
									list.add(file);
								}
							}
						}
					}
					return !list.isEmpty();
				} else if (element instanceof IPackageFragment) {
					IPackageFragment pkg = (IPackageFragment) element;
					Object[] nonJavaResources = pkg.getNonJavaResources();
					for (Object resource : nonJavaResources) {
						if (resource instanceof IFile) {
							IFile file = (IFile) resource;
							String name = file.getName();
							if (name != null) {
								name = name.toLowerCase();
								if (name.endsWith(".gif")
										|| name.endsWith(".png")
										|| name.endsWith(".jpg")) {
									return true;
								}
							}
						}
					}
					return false;
				}
				return children != null && children.length > 0;
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
			return false;
		}

		
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		
		public void dispose() {
		}

		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	class ProjectLabelProvider extends LabelProvider implements ILabelProvider {
		
		public Image getImage(Object element) {
			if (element == null)
				return null;
			if (element instanceof IPackageFragmentRoot) {
				return JavaUI.getSharedImages().getImage(
						ISharedImages.IMG_OBJS_PACKFRAG_ROOT);
			} else if (element instanceof IPackageFragment) {
				return JavaUI.getSharedImages().getImage(
						ISharedImages.IMG_OBJS_PACKAGE);
			} else if (element instanceof IFile) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
			}
			return null;
		}

		
		public String getText(Object element) {
			if (element == null)
				return "";
			if (element instanceof IJavaElement) {
				IJavaElement java = (IJavaElement) element;
				String name = java.getElementName();
				if (name == null || name.trim().length() == 0) {
					if (java instanceof IPackageFragment) {
						return "(default package)";
					}
				}
				return name;
			} else if (element instanceof IFile) {
				IFile file = (IFile) element;
				String name = file.getName();
				return name;
			}
			return element.toString();
		}

		
		public void dispose() {
		}
	}

	protected ImageSelectionDialog(Shell parentShell) {
		super(parentShell);
	}

	
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Select image");
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		area.setLayout(layout);
		view = new TreeViewer(area, SWT.BORDER);
		GridData data = new GridData();
		data.widthHint = 350;
		data.heightHint = 200;
		view.getTree().setLayoutData(data);
		view.setUseHashlookup(true);
		view.setContentProvider(new ProjectTreeContent());
		view.setLabelProvider(new ProjectLabelProvider());
		view.setInput(VisualSwingPlugin.getCurrentProject());
		view.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				view_selectionChanged(event);
			}
		});
		Group group = new Group(area, SWT.NONE);
		group.setText("Preview");
		group.setLayout(new FillLayout());
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.heightHint = 50;
		group.setLayoutData(data);
		label = new Label(group, SWT.CENTER);
		return parent;
	}

	private boolean buildPath(Object target, Object root, Stack<Object> stack) {
		stack.push(root);
		if (root.equals(target)) {
			return true;
		}
		ITreeContentProvider provider = (ITreeContentProvider) view
				.getContentProvider();
		Object[] children = provider.getChildren(root);
		for (Object child : children) {
			if (buildPath(target, child, stack))
				return true;
		}
		stack.pop();
		return false;
	}

	
	protected Control createContents(Composite parent) {
		Control ctrl = super.createContents(parent);
		if (imgFile != null) {
			IJavaProject prj = VisualSwingPlugin.getCurrentProject();
			Stack<Object> stack = new Stack<Object>();
			if (buildPath(imgFile, prj, stack)) {
				TreePath path = new TreePath(stack.toArray());
				view.expandToLevel(path, 0);
				view.expandAll();
				view.setSelection(new StructuredSelection(imgFile));
			}
		}
		return ctrl;
	}

	private void view_selectionChanged(SelectionChangedEvent event) {
		TreeSelection sel = (TreeSelection) event.getSelection();
		Object selected = sel.getFirstElement();
		if (selected != null) {
			if (selected instanceof IFile) {
				IFile file = (IFile) selected;
				String name = file.getName();
				name = name.toLowerCase();
				if (name.endsWith(".png") || name.endsWith(".gif")
						|| name.endsWith(".jpg")) {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					this.imgFile = file;
					updatePicture();
					return;
				}
			}
		}
		Button btn = getButton(IDialogConstants.OK_ID);
		if (btn != null)
			btn.setEnabled(false);
	}

	private Image image;

	private void updatePicture() {
		if (imgFile != null) {
			try {
				ImageDescriptor d = ImageDescriptor.createFromURL(imgFile
						.getLocationURI().toURL());
				if (image != null)
					image.dispose();
				image = d.createImage();
				label.setImage(image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public boolean close() {
		if (image != null)
			image.dispose();
		return super.close();
	}

	
	protected void cancelPressed() {
		imgFile = null;
		super.cancelPressed();
	}

	public IFile getImageFile() {
		return imgFile;
	}

	public void setImageFile(IFile file) {
		this.imgFile = file;
	}

	private IFile imgFile;
}
