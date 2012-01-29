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

	private final JORResourceProvider resourceProvider;
	
	public AssetDispatcher(final JORResourceProvider provider) {
		this.resourceProvider = provider;
	}
	
	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		if (exchange.getRequestURI().getPath().startsWith("assets/")) {
			JORUtils.replyWith404(exchange);
			return true;
		}
		return false;
	}
	
}
