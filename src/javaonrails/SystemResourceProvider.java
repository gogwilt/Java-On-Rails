package javaonrails;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javaonrails.ApplicationResourceProvider.ApplicationResource;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

/**
 * Provides an interface for accessing system-level resources
 * (routing/configuration ruby files).
 * 
 * @author rbuckheit
 */
public class SystemResourceProvider {

	public enum SystemResource implements JORResource {
		ROOT("");

		private final String directoryPath;

		private SystemResource(final String directoryPath) {
			this.directoryPath = directoryPath;
		}

		public String getDirectoryPath() {
			return directoryPath;
		}
	}

	private Class<?> resourceClass;

	public SystemResourceProvider(final Class<?> resourceClass) {
		this.resourceClass = resourceClass;
	}

	/* TODO refactor shared code w/ ApplicationResourceProvider */
	
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