package javaonrails;

import java.net.URL;

/**
 * Provides an interface for accessing resources such as ruby, css, images, etc.
 * @author rbuckheit
 */
public class JORResourceProvider {

	private final Class<?> resourceClass;

	public JORResourceProvider(Class<?> cl) {
		this.resourceClass = cl;
	}

	public URL getResource(final String path) {
		return resourceClass.getResource(path);
	}

	public URL getController(final String controllerName) {
		return getResource("controllers/" + controllerName);
	}
	
	public URL getConfigFile(final String fileName) {
		return getResource("config/" + fileName);
	}

}
