package org.dyno.visual.swing.lnfs.lib;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.LookAndFeel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.lnfs.LnfPlugin;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISystemValue;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LookAndFeelLib implements IClasspathContainer, ILookAndFeelAdapter {
	private static final String LAF_FILE = "laf.xml";
	private static Map<String, LookAndFeelLib> lnfLibs = new HashMap<String, LookAndFeelLib>();
	private static class SystemValue implements ISystemValue{
		private static final long serialVersionUID = 1L;
	}
	private static SystemValue SYSTEM_VALUE=new SystemValue();

	public static LookAndFeelLib getLnfLib(String dir) {
		LookAndFeelLib icc = lnfLibs.get(dir);
		if (icc == null) {
			icc = new LookAndFeelLib(dir);
			lnfLibs.put(dir, icc);
		}
		return icc;
	}

	private String libName;
	private IPath libPath;
	private IPath[] libJars;
	private IPath[] libSrcs;
	private String lnfClassname;
	private Map<String, Map<String, Object>> values;
	private LookAndFeel lnfInstance;
 
	private LookAndFeelLib(String dir) {
		this.libPath = JavaUtil.VS_LIBRARY.append(
				LookAndFeelLibrary.LOOK_AND_FEEL_EXT).append(dir);
		IPath path = getLafLibDir();
		path = path.append(dir);
		initLaf(path);
	}
	public static IPath getLafLibDir(){
		IPath path = Platform.getLocation();
		path = path.append(".metadata");
		path = path.append(".plugins");
		path = path.append(LnfPlugin.getPluginID());
		path = path.append(LookAndFeelLibrary.LOOK_AND_FEEL_LIB_DIR);
		return path;
	}
	private void initLaf(IPath path) {
		IPath laf_path = path.append(LAF_FILE);
		File laf_file = laf_path.toFile();
		if (laf_file.exists()) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder newDocumentBuilder = factory
						.newDocumentBuilder();
				Document document = newDocumentBuilder.parse(laf_file);
				Element root = document.getDocumentElement();
				this.libName = root.getAttribute("name");
				this.lnfClassname = root.getAttribute("class");
				NodeList nodes = root.getElementsByTagName("classpath");
				if(nodes!=null&&nodes.getLength()>0){
					List<IPath>jars=new ArrayList<IPath>();
					List<IPath>srcs=new ArrayList<IPath>();
					for(int i=0;i<nodes.getLength();i++){
						Element classpathNode=(Element)nodes.item(i);
						String strJar = classpathNode.getAttribute("jar");
						String strSrc = classpathNode.getAttribute("src");
						jars.add(path.append(strJar));
						if(strSrc!=null&&strSrc.trim().length()>0){
							srcs.add(path.append(strSrc));
						}else{
							srcs.add(null);
						}
					}
					this.libJars = new IPath[jars.size()];
					this.libSrcs = new IPath[srcs.size()];
					jars.toArray(libJars);
					srcs.toArray(libSrcs);
				}else{
					this.libJars = new IPath[0];
					this.libSrcs = new IPath[0];
				}
				values = new HashMap<String, Map<String, Object>>();
				nodes = root.getElementsByTagName("component");
				if (nodes != null && nodes.getLength() > 0) {
					for (int i = 0; i < nodes.getLength(); i++) {
						Element node = (Element) nodes.item(i);
						extractComponentValue(node);
					}
				}
			} catch (Exception e) {
				LnfPlugin.getLogger().error(e);
			}
		} else {
			throw new IllegalArgumentException("Cannot find laf.xml under:"
					+ path.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void extractComponentValue(Element component)
			throws ClassNotFoundException, IntrospectionException {
		String strClass = component.getAttribute("class");
		Class clazz = Class.forName(strClass);
		Map<String, Object> compValues = values.get(strClass);
		if (compValues == null) {
			compValues = new HashMap<String, Object>();
			values.put(strClass, compValues);
		}
		NodeList nodes = component.getElementsByTagName("property");
		if (nodes != null && nodes.getLength() > 0) {
			for (int i = 0; i < nodes.getLength(); i++) {
				Element node = (Element) nodes.item(i);
				extractPropertyValue(compValues, node, clazz);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void extractPropertyValue(Map<String, Object> values, Element node,
			Class clazz) throws IntrospectionException {
		String name = node.getAttribute("name");
		String value = node.getAttribute("default");
		if (value != null && value.trim().length() > 0&&!value.equals("null")) {
			if(value.equals("SYSTEM_VALUE")){
				values.put(name, SYSTEM_VALUE);
			} else {
				PropertyDescriptor pd = new PropertyDescriptor(name, clazz);
				Class type = pd.getPropertyType();
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				if (ta != null && ta.getEndec() != null) {
					Object v = ta.getEndec().decode(value);
					values.put(name, v);
				}
			}
		}
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {
		IClasspathEntry[] libs=new IClasspathEntry[libJars.length];
		for(int i=0;i<libs.length;i++)
			libs[i]=JavaCore.newLibraryEntry(libJars[i], libSrcs[i], null, true);
		return libs;
	}

	@Override
	public String getDescription() {
		return libName;
	}

	@Override
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	@Override
	public IPath getPath() {
		return libPath;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof LookAndFeelLib) {
			LookAndFeelLib lnfLib = (LookAndFeelLib) o;
			return lnfLib.libPath.equals(libPath);
		} else
			return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getDefaultValue(Class widgetClass, String propertyName) {
		Map<String, Object> map = values.get(widgetClass.getName());
		if (map != null) {
			return map.get(propertyName);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LookAndFeel getLookAndFeelInstance() {
		if (lnfInstance == null) {
			try {
				URL[]urls=new URL[libJars.length];
				for(int i=0;i<libJars.length;i++){
					urls[i]=libJars[i].toFile().toURI().toURL();
				}
				URLClassLoader urlLoader = new URLClassLoader(urls,	getClass().getClassLoader());
				Class lnfClass = urlLoader.loadClass(lnfClassname);
				lnfInstance = (LookAndFeel) lnfClass.newInstance();
				lnfInstance = new DelegateLookAndFeel(lnfInstance);
			} catch (Exception e) {
				LnfPlugin.getLogger().error(e);
			}
		}
		return lnfInstance;
	}
	@Override
	public String getClassname() {
		return lnfClassname;
	}

	@Override
	public String getName() {
		return libName;
	}
}