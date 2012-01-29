package javaonrails.server;

import java.io.IOException;
import java.net.URL;

import javaonrails.JORResourceProvider;
import javaonrails.JORResourceProvider.ResourceType;
import javaonrails.JORUtils;

import com.sun.net.httpserver.HttpExchange;

public class StaticPageDispatcher implements JORDispatcher {

	private final JORResourceProvider resourceProvider;
	
	public StaticPageDispatcher(JORResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public boolean routeExchange(HttpExchange exchange) throws IOException {
		if (!JORUtils.isGetRequest(exchange)) {
			return false;
		}
		final URL url = resourceProvider.getResource(ResourceType.STATIC_PAGES, exchange.getRequestURI().getPath());
		if (url == null) {
			return false;
		} else {
			JORUtils.replyWithFile(exchange, url);
			return true;
		}
	}

}