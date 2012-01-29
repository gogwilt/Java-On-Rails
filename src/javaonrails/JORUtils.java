package javaonrails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import com.google.common.io.ByteStreams;
import com.sun.istack.internal.NotNull;
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
	
	public static void replyWithFile(final HttpExchange exchange, @NotNull URL url) throws IOException {
		File file;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", JORUtils.getContentType(exchange.getRequestURI().getPath()));
		exchange.sendResponseHeaders(200, 0);
		
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(ByteStreams.toByteArray(new FileInputStream(file)));
		responseBody.close();
	}
	
	public static boolean isGetRequest(final HttpExchange exchange) {
		return exchange.getRequestMethod().equalsIgnoreCase("GET");
	}
	
	public static String removeBasePath(String base, String path) {
		if (path.startsWith(base)) {
			return path.substring(base.length());
		} else {
			return path;
		}
	}
	
	public static String getContentType(String path) {
		String type = getContentTypeIfImage(path);
		if (type != null) {
			return type;
		}
		if (path.endsWith(".html") || path.endsWith(".htm")) {
			return "text/html";
		}
		if (path.endsWith(".css")) {
			return "text/css";
		}
		if (path.endsWith(".js")) {
			return "application/javascript";
		}
		return "text/plain";
	}
	
	private static String getContentTypeIfImage(String path) {
		if (path.endsWith(".gif")) {
			return "image/gif";
		} else if (path.endsWith(".png")) {
			return "image/png";
		} else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		return null;
	}
}
