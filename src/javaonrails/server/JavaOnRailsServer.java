package javaonrails.server;

import javaonrails.JORResourceProvider;

import com.sun.net.httpserver.HttpHandler;

public interface JavaOnRailsServer extends HttpHandler {
	
	public JORResourceProvider getResourceProvider();

}
