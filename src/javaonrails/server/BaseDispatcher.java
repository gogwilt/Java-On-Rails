package javaonrails.server;

import java.io.IOException;

import javaonrails.ApplicationResourceProvider;

import com.sun.net.httpserver.HttpExchange;

/**
 * Handles all incoming requests.
 * @author rbuckheit
 */
public class BaseDispatcher implements JORDispatcher {

	private StaticPageDispatcher staticDispatcher;
	private AssetDispatcher assetDispatcher;
	private ControllerDispatcher controllerDispatcher;
	private final ApplicationResourceProvider resourceProvider;
	
	public BaseDispatcher(final ApplicationResourceProvider provider) {
		this.staticDispatcher = new StaticPageDispatcher(provider);
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
		if (staticDispatcher.routeExchange(exchange)) {
			return true;
		}
		if (assetDispatcher.routeExchange(exchange)) {
			return true;
		}
		else if (controllerDispatcher.routeExchange(exchange)) {
			return true;
		}
		return false;
	}
	
}
