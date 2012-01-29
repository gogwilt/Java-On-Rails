package javaonrails.server;

import java.io.IOException;

import javaonrails.JORResourceProvider;

import com.sun.net.httpserver.HttpExchange;

/**
 * Handles all incoming requests.
 * @author rbuckheit
 */
public class BaseDispatcher implements JORDispatcher {

	private AssetDispatcher assetDispatcher;
	private ControllerDispatcher controllerDispatcher;
	private final JORResourceProvider resourceProvider;
	
	public BaseDispatcher(final JORResourceProvider provider) {
		this.assetDispatcher = new AssetDispatcher(provider);
		this.controllerDispatcher = new ControllerDispatcher(provider);
		this.resourceProvider = provider;
	}
	
	/**
	 * Determines if the exchange should be routed to the asset dispatcher or the
	 * controller dispatcher and delegates appropriately.
	 * @param exchange
	 */
	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		if (controllerDispatcher.routeExchange(exchange)) {
			return true;
		} else if (assetDispatcher.routeExchange(exchange)) {
			return true;
		}
		return false;
	}
	
}
