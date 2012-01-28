package javaonrails.server;

import com.sun.net.httpserver.HttpExchange;

/**
 * Handles all incoming requests.
 * @author rbuckheit
 */
public class BaseDispatcher implements JORDispatcher {

	private AssetDispatcher assetDispatcher;
	private ControllerDispatcher controllerDispatcher;
	
	public BaseDispatcher() {
		this.assetDispatcher = new AssetDispatcher();
		this.controllerDispatcher = new ControllerDispatcher();
	}
	
	/**
	 * Determines if the exchange should be routed to the asset dispatcher or the
	 * controller dispatcher and delegates appropriately.
	 * @param exchange
	 */
	@Override
	public boolean routeExchange(final HttpExchange exchange) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
