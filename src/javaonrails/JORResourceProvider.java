package javaonrails;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

/**
 * Provides an interface for accessing resources such as ruby, css, images, etc.
 * 
 * @author rbuckheit
 */
public class JORResourceProvider {
	
	public enum ResourceType {
		CONFIG("config"), 
		CONTROLLER("controllers");
		
		private final String directoryPath;
		
		private ResourceType(final String directoryPath) {
			this.directoryPath = directoryPath;
		}
		
		public String getDirectoryPath() {
			return directoryPath;
		}
	}

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

	public String loadFile(final ResourceType type, final String name) throws IOException {
		final URL fileURL = getResource(type.getDirectoryPath() + File.separator + name);
		return loadFile(fileURL);
	}
	
	public static String loadFile(final URL fromUrl) throws IOException {
		try {
			final List<String> fileLines = Files.readLines(new File(fromUrl.toURI()), Charset.defaultCharset());
			return Joiner.on("\n").join(fileLines);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
