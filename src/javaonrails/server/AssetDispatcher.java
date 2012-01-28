package javaonrails.server;

import javaonrails.JORResourceProvider;

import com.sun.net.httpserver.HttpExchange;

/**
 * Dispatcher which handles requests for static assets.
 * @author rbuckheit
 */
public class AssetDispatcher implements JORDispatcher {

	private final JORResourceProvider resourceProvider;
	
	public AssetDispatcher(final JORResourceProvider provider) {
		this.resourceProvider = provider;
	}
	
	@Override
	public boolean routeExchange(final HttpExchange exchange) {
		// TODO Auto-generated method stub
		return false;
	}

}
