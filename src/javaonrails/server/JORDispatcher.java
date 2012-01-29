package javaonrails.server;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public interface JORDispatcher {

	public boolean routeExchange(final HttpExchange exchange) throws IOException;
	
}
