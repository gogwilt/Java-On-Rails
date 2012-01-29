package javaonrails.server;

import java.io.IOException;

import javaonrails.JORResourceProvider;
import javaonrails.JORUtils;

import com.sun.net.httpserver.HttpExchange;

public class DefaultJORServer implements JavaOnRailsServer {

	private final BaseDispatcher baseDispatch;
	private final JORResourceProvider resourceProvider;
	
	public DefaultJORServer(final JORResourceProvider provider) {
		baseDispatch = new BaseDispatcher(provider);
		resourceProvider = provider;
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if(!baseDispatch.routeExchange(exchange)) {
			JORUtils.replyWith404(exchange);
		}
	}

	@Override
	public JORResourceProvider getResourceProvider() {
		return resourceProvider;
	}

}
