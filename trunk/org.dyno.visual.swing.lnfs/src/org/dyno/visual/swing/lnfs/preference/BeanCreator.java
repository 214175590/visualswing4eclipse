package org.dyno.visual.swing.lnfs.preference;

import java.awt.Component;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;


@SuppressWarnings("unchecked")
public interface BeanCreator {
	Class getBeanClass();
	Component createComponent();
	void dispose();
	abstract class AbstractScrollPaneCreator implements BeanCreator{
		private JFrame jframe;
		private Component content;
		@Override
		public Component createComponent() {
			jframe = new JFrame();
			jframe.setSize(100, 100);
			jframe.setLocation(-100, -100);
			JScrollPane jsp = new JScrollPane();
			try {
				Class clazz = getBeanClass();
				content = (Component) clazz.newInstance();
				jsp.setViewportView(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jframe.add(jsp);
			jsp.setVisible(true);
			return content;
		}
		@Override
		public void dispose() {
			jframe.setVisible(false);
		}
	}	
	abstract class AbstractBeanCreator implements BeanCreator{
		private JFrame jframe;
		private Component content;
		@Override
		public Component createComponent() {
			jframe = new JFrame();
			try {
				Class clazz = getBeanClass();
				content = (Component) clazz.newInstance();
				jframe.add(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jframe.setSize(100, 100);
			jframe.setLocation(10, 10);
			jframe.setVisible(true);
			return content;
		}
		@Override
		public void dispose() {
			jframe.setVisible(false);
		}
	}
	BeanCreator[]COMPONENTS={
			new BeanCreator(){
				private JFrame jframe;
				@Override
				public Component createComponent() {
					jframe = new JFrame();
					jframe.setSize(100, 100);
					jframe.setLocation(-100, -100);
					jframe.setVisible(true);
					return jframe;
				}

				@Override
				public void dispose() {
					jframe.setVisible(false);
				}

				@Override
				public Class getBeanClass() {
					return JFrame.class;
				}
			},
			new BeanCreator(){
				private JDialog jdialog;
				@Override
				public Component createComponent() {
					JFrame jframe = new JFrame();
					jdialog = new JDialog(jframe);
					jdialog.setSize(100, 100);
					jframe.setLocation(-100, -100);
					jdialog.setVisible(true);
					return jdialog;
				}

				@Override
				public void dispose() {
					jdialog.setVisible(false);
				}

				@Override
				public Class getBeanClass() {
					return JDialog.class;
				}
				
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JApplet.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JInternalFrame.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JPanel.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JButton.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JCheckBox.class;
				}
			},
			new BeanCreator(){
				@Override
				public Class getBeanClass() {
					return JCheckBoxMenuItem.class;
				}

				@Override
				public Component createComponent() {
					return new JCheckBoxMenuItem();
				}

				@Override
				public void dispose() {
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JComboBox.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JDesktopPane.class;
				}
			},
			new AbstractScrollPaneCreator(){
				@Override
				public Class getBeanClass() {
					return JEditorPane.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JFormattedTextField.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JLabel.class;
				}
			},
			new AbstractScrollPaneCreator(){
				@Override
				public Class getBeanClass() {
					return JList.class;
				}
			},
			new BeanCreator(){
				@Override
				public Class getBeanClass() {
					return JMenuBar.class;
				}

				@Override
				public Component createComponent() {
					return new JMenuBar();
				}

				@Override
				public void dispose() {
				}
			},
			new BeanCreator(){
				@Override
				public Class getBeanClass() {
					return JMenuItem.class;
				}

				@Override
				public Component createComponent() {
					return new JMenuItem();
				}

				@Override
				public void dispose() {
				}
			},
			new BeanCreator(){
				@Override
				public Class getBeanClass() {
					return JMenu.class;
				}

				@Override
				public Component createComponent() {
					return new JMenu();
				}

				@Override
				public void dispose() {
					
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JPasswordField.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JProgressBar.class;
				}
			},
			new BeanCreator(){
				@Override
				public Class getBeanClass() {
					return JRadioButtonMenuItem.class;
				}

				@Override
				public Component createComponent() {
					return new JRadioButtonMenuItem();
				}

				@Override
				public void dispose() {
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JRadioButton.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JScrollBar.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JScrollPane.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JSeparator.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JSlider.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JSpinner.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JSplitPane.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JTabbedPane.class;
				}
			},
			new AbstractScrollPaneCreator(){
				@Override
				public Class getBeanClass() {
					return JTextArea.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JTextField.class;
				}
			},
			new AbstractScrollPaneCreator(){
				@Override
				public Class getBeanClass() {
					return JTextPane.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JToggleButton.class;
				}
			},
			new AbstractBeanCreator(){
				@Override
				public Class getBeanClass() {
					return JToolBar.class;
				}
			},
			new AbstractScrollPaneCreator(){
				@Override
				public Class getBeanClass() {
					return JTable.class;
				}
			},
			new AbstractScrollPaneCreator(){
				@Override
				public Class getBeanClass() {
					return JTree.class;
				}
			},
	};
}