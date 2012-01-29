package javaonrails.server;

import java.io.IOException;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;

import com.sun.net.httpserver.HttpExchange;

/**
 * Handles all incoming requests.
 * 
 * @author rbuckheit
 */
public class BaseDispatcher implements JORDispatcher {

	private StaticPageDispatcher staticDispatcher;
	private AssetDispatcher assetDispatcher;
	private ControllerDispatcher controllerDispatcher;

	private final ApplicationResourceProvider applicationProvider;
	private final SystemResourceProvider systemProvider;

	public BaseDispatcher(final ApplicationResourceProvider applicationProvider,
			final SystemResourceProvider systemProvider) {
		this.staticDispatcher = new StaticPageDispatcher(applicationProvider);
		this.assetDispatcher = new AssetDispatcher(applicationProvider);
		this.controllerDispatcher = new ControllerDispatcher(applicationProvider, systemProvider);
		
		this.systemProvider = systemProvider;
		this.applicationProvider = applicationProvider;
	}

	/**
	 * Determines if the exchange should be routed to the asset dispatcher or
	 * the controller dispatcher and delegates appropriately.
	 * 
	 * @param exchange
	 */
	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		if (staticDispatcher.routeExchange(exchange)) {
			return true;
		}
		if (assetDispatcher.routeExchange(exchange)) {
			return true;
		} else if (controllerDispatcher.routeExchange(exchange)) {
			return true;
		}
		return false;
	}

}
