package javaonrails.server;

import java.io.IOException;

import javaonrails.JORResourceProvider;

import com.sun.net.httpserver.HttpExchange;

public class JavaScriptDispatcher implements JORDispatcher {
	
	private final JORResourceProvider resourceProvider;
	
	public JavaScriptDispatcher(JORResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public boolean routeExchange(HttpExchange exchange) throws IOException {
		return false;
	}
	

}