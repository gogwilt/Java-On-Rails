package javaonrails.server;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.ApplicationResourceProvider.ApplicationResource;
import javaonrails.JORUtils;

import com.sun.net.httpserver.HttpExchange;

public class ImageDispatcher implements JORDispatcher {
	
	private static final Pattern IMAGE_FILE_PATTERN = 
		Pattern.compile(AssetDispatcher.BASE_PATH + ".*\\.(png|gif|jpeg|jpg)", 
				Pattern.CASE_INSENSITIVE);
	
	private final ApplicationResourceProvider resourceProvider;
	
	public ImageDispatcher(ApplicationResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public boolean routeExchange(HttpExchange exchange) throws IOException {
		if (!JORUtils.isGetRequest(exchange)) {
			return false;
		}
		final String path = exchange.getRequestURI().getPath();
		if (IMAGE_FILE_PATTERN.matcher(path).matches()) {
			final URL image = resourceProvider.getResource(ApplicationResource.IMAGES, AssetDispatcher.removeBasePath(path));
			if (image == null) {
				JORUtils.replyWith404(exchange);
			} else {
				JORUtils.replyWithFile(exchange, image);
			}
			return true;
		}
		return false;
	}
}
