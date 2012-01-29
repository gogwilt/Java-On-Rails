package javaonrails.server;

import javaonrails.resource.ApplicationResourceProvider;

import com.sun.net.httpserver.HttpHandler;

public interface JavaOnRailsServer extends HttpHandler {
	
	public ApplicationResourceProvider getResourceProvider();

}
