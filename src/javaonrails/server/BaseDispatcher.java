package javaonrails.server;

import java.io.IOException;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.ruby.RubyProvider;

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
	private final RubyProvider rubyProvider;

	public BaseDispatcher(final ApplicationResourceProvider applicationProvider,
			final SystemResourceProvider systemProvider, final RubyProvider rubyProvider) {
		this.staticDispatcher = new StaticPageDispatcher(applicationProvider);
		this.assetDispatcher = new AssetDispatcher(applicationProvider);
		this.controllerDispatcher = new ControllerDispatcher(applicationProvider, systemProvider, rubyProvider);
		
		this.systemProvider = systemProvider;
		this.applicationProvider = applicationProvider;
		this.rubyProvider = rubyProvider;
	}

	/**
	 * Determines if the exchange should be routed to the asset dispatcher or
	 * the controller dispatcher and delegates appropriately.
	 * 
	 * @param exchange
	 */
	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		
		System.out.println(String.format("Routing exchange: %s %s %s", exchange.getProtocol(),
				exchange.getRequestMethod(), exchange.getRequestURI()));
		
		if (staticDispatcher.routeExchange(exchange)) {
			return true;
		}
		if (assetDispatcher.routeExchange(exchange)) {
			return true;
		}
		if (controllerDispatcher.routeExchange(exchange)) {
			return true;
		}
		return false;
	}

}
