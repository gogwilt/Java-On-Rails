/**
 * 
 */
package javaonrails.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import javaonrails.server.JavaOnRailsServer;

import com.sun.net.httpserver.HttpServer;

/**
 * @author cgogwilt
 *
 */
public class JORLocalServer {
	private final HttpServer httpServer;
	private final JavaOnRailsServer jorServer;
	
	public JORLocalServer(JavaOnRailsServer jorServer, int port) {
		this.jorServer = jorServer;
		httpServer = initializeHttpServer(port);
		
		httpServer.createContext("/", jorServer);
	}
	
	public HttpServer getHttpServer() {
		return httpServer;
	}
	
	public HttpServer initializeHttpServer(int port) {
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(port), -1);
		} catch (IOException e) {
			// This is bad.
			throw new RuntimeException(e);
		}
		return server;
	}
}
