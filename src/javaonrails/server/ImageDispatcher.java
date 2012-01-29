package javaonrails.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

import sun.misc.IOUtils;

import javaonrails.JORResourceProvider;
import javaonrails.JORResourceProvider.ResourceType;
import javaonrails.JORUtils;

import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ImageDispatcher implements JORDispatcher {
	
	private static final Pattern IMAGE_FILE_PATTERN = 
		Pattern.compile(AssetDispatcher.BASE_PATH + ".*\\.(png|gif|jpeg|jpg)", 
				Pattern.CASE_INSENSITIVE);
	
	private final JORResourceProvider resourceProvider;
	
	public ImageDispatcher(JORResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public boolean routeExchange(HttpExchange exchange) throws IOException {
		if (!JORUtils.isGetRequest(exchange)) {
			return false;
		}
		final String path = exchange.getRequestURI().getPath();
		if (IMAGE_FILE_PATTERN.matcher(path).matches()) {
			final URL image = resourceProvider.getResource(ResourceType.IMAGES, AssetDispatcher.removeBasePath(path));
			if (image == null) {
				JORUtils.replyWith404(exchange);
			} else {
				replyWithImage(image, exchange);
			}
			return true;
		}
		return false;
	}
	
	private static void replyWithImage(URL imageUrl, HttpExchange exchange) throws IOException {
		
		File file;
		try {
			file = new File(imageUrl.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", contentType(exchange.getRequestURI().getPath()));
		exchange.sendResponseHeaders(200, 0);
		
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(ByteStreams.toByteArray(new FileInputStream(file)));
		responseBody.close();
	}

	private static String contentType(String path) {
		if (path.endsWith(".gif")) {
			return "image/gif";
		} else if (path.endsWith(".png")) {
			return "image/png";
		} else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		return "image";
	}
	
	
}
