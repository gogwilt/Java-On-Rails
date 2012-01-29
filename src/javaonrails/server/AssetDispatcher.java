package javaonrails.server;

import java.io.IOException;

import javaonrails.JORResourceProvider;
import javaonrails.JORUtils;

import com.sun.net.httpserver.HttpExchange;

/**
 * Dispatcher which handles requests for assets such as images, javascripts,
 * and stylesheets.
 * @author rbuckheit
 */
public class AssetDispatcher implements JORDispatcher {

	public static final String BASE_PATH = "/assets/";
	
	private final JORResourceProvider resourceProvider;
	private final ImageDispatcher imageDispatcher;
	
	public AssetDispatcher(final JORResourceProvider provider) {
		this.resourceProvider = provider;
		imageDispatcher = new ImageDispatcher(provider);
	}
	
	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		if (JORUtils.isGetRequest(exchange)) {
			if (exchange.getRequestURI().getPath().startsWith(BASE_PATH)) {
				if (imageDispatcher.routeExchange(exchange)) {
					return true;
				}
				JORUtils.replyWith404(exchange);
				return true;
			}
		}
		return false;
	}
	
	public static String removeBasePath(String path) {
		if (path.startsWith(BASE_PATH)) {
			return path.substring(BASE_PATH.length());
		} else {
			return path;
		}
	}
}
