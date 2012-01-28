package javaonrails.server;

import com.sun.net.httpserver.HttpExchange;

public interface JORDispatcher {

	public boolean routeExchange(final HttpExchange exchange);
	
}
