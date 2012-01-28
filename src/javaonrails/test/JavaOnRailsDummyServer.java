package javaonrails.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javaonrails.JORResourceProvider;
import javaonrails.server.JavaOnRailsServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class JavaOnRailsDummyServer implements JavaOnRailsServer {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.err.println("Handling : " + exchange.getRequestMethod());
		
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, 0);

			OutputStream responseBody = exchange.getResponseBody();
			Headers requestHeaders = exchange.getRequestHeaders();
			Set<String> keySet = requestHeaders.keySet();
			Iterator<String> iter = keySet.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				List values = requestHeaders.get(key);
				String s = key + " = " + values.toString() + "\n";
				responseBody.write(s.getBytes());
			}
			responseBody.write("Hello world\n".getBytes());
			responseBody.close();
		}
	}

	@Override
	public JORResourceProvider getResourceProvider() {
		return null;
	}

}
