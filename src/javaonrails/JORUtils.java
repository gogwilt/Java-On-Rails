package javaonrails;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class JORUtils {
	
	public static void replyWith404(final HttpExchange exchange) throws IOException {
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/html");
		exchange.sendResponseHeaders(404, 0);
		
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write("<html><body><h1>404: Page Not Found</h1></body></html>".getBytes());
		responseBody.close();
	}
	
	public static boolean isGetRequest(final HttpExchange exchange) {
		return exchange.getProtocol().equalsIgnoreCase("GET");
	}
}
