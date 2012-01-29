package javaonrails.server;

import java.io.IOException;

import javaonrails.JORResourceProvider;

import com.sun.net.httpserver.HttpExchange;

public class StylesheetDispatcher implements JORDispatcher {
	
	private final JORResourceProvider resourceProvider;
	
	public StylesheetDispatcher(JORResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public boolean routeExchange(HttpExchange exchange) throws IOException {
		return false;
	}
	

}
