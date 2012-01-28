package javaonrails.server;

import com.sun.net.httpserver.HttpExchange;

/**
 * Dispatcher which instantiates ruby controllers in response to dynamic content requests.
 * @author rbuckheit
 */
public class ControllerDispatcher implements JORDispatcher {

	@Override
	public boolean routeExchange(HttpExchange exchange) {
		// TODO Auto-generated method stub
		return false;
	}

}
