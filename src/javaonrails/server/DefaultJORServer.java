package javaonrails.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class DefaultJORServer implements JavaOnRailsServer {

	private final BaseDispatcher baseDispatch;
	
	public DefaultJORServer() {
		baseDispatch = new BaseDispatcher();
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if(!baseDispatch.routeExchange(exchange)) {
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/html");
			exchange.sendResponseHeaders(404, 0);
			
			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write("<html><body><h1>404: Page Not Found</h1></body></html>".getBytes());
			responseBody.close();
		}
	}

}
