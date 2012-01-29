package javaonrails.server;

import java.io.IOException;

import javaonrails.ApplicationResourceProvider;

import com.sun.net.httpserver.HttpExchange;

public class JavaScriptDispatcher implements JORDispatcher {
	
	private final ApplicationResourceProvider resourceProvider;
	
	public JavaScriptDispatcher(ApplicationResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public boolean routeExchange(HttpExchange exchange) throws IOException {
		return false;
	}
	

}