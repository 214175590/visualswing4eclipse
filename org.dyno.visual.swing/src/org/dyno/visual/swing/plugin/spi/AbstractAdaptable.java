package org.dyno.visual.swing.plugin.spi;

import java.util.HashMap;
import java.util.Map;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;

@SuppressWarnings("unchecked")
public abstract class AbstractAdaptable implements IAdaptable {

	protected Map<String, Object> adapters = new HashMap<String, Object>();
	protected Map<String, Object> properties = new HashMap<String, Object>();
	
	protected abstract Class getObjectClass();

	public void setProperty(String key, Object value) {
		if (value == null)
			properties.remove(key);
		else
			properties.put(key, value);
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	
	public Object getAdapter(Class adapterClass) {
		Object object = adapters.get(adapterClass.getName());
		if (object == null) {
			String adapterClassname = adapterClass.getName();
			IConfigurationElement config = getConfig(adapterClassname, getObjectClass());
			boolean isDefault=false;
			if (config == null){
				config = ExtensionRegistry.getAdapterConfig(adapterClassname, "default");
				isDefault=true;
			}
			if (config == null)
				return null;
			try {
				object = config.createExecutableExtension(isDefault?"default":"implementation");
				if (object == null)
					return null;
				if (object instanceof IAdaptableContext) {
					((IAdaptableContext) object).setAdaptable(this);
				}
				adapters.put(adapterClass.getName(), object);
				return object;
			} catch (CoreException e) {
				VisualSwingPlugin.getLogger().error(e);
				return null;
			}
		} else
			return object;
	}

	
	private IConfigurationElement getConfig(String adapter, Class type) {
		if (type == null)
			return null;
		IConfigurationElement config = ExtensionRegistry.getAdapterConfig(
				adapter, type.getName());
		if (config == null) {
			if (type == Object.class)
				return null;
			else {
				config = getConfig(adapter, type.getSuperclass());
				if (config != null)
					return config;
				else {
					Class[] interfaces = type.getInterfaces();
					for (Class inter : interfaces) {
						config = getConfig(adapter, inter);
						if (config != null)
							return config;
					}
					return null;
				}
			}
		} else
			return config;
	}
}
