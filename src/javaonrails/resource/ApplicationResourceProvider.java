package javaonrails.resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

/**
 * Provides an interface for accessing application-level resource files.
 * @author rbuckheit
 */
public class ApplicationResourceProvider {
	
	public enum ApplicationResource implements JORResource {
		CONFIG("config"), 
		CONTROLLER("controllers"),
		IMAGES("assets" + File.separator + "images"),
		JAVASCRIPTS("assets" + File.separator + "javascripts"),
		STYLESHEETS("assets" + File.separator + "stylesheets"),
		STATIC_PAGES("public");
		
		private final String directoryPath;
		
		private ApplicationResource(final String directoryPath) {
			this.directoryPath = directoryPath;
		}
		
		public String getDirectoryPath() {
			return directoryPath;
		}
	}

	private final Class<?> resourceClass;

	public ApplicationResourceProvider(Class<?> cl) {
		this.resourceClass = cl;
	}

	public URL getResourceLoadPath(final ApplicationResource type) {
		return resourceClass.getResource(type.getDirectoryPath());
	}
	
	public URL getResource(final String path) {
		return resourceClass.getResource(path);
	}
	
	public URL getResource(final ApplicationResource type, final String path) {
		String relpath = type.getDirectoryPath() + File.separator + path;
		return getResource(relpath);
	}

	public String loadFile(final ApplicationResource type, final String name) throws IOException {
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
