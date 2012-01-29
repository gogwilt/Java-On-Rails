package javaonrails.server;

import java.io.IOException;

import javaonrails.ApplicationResourceProvider;
import javaonrails.JORUtils;

import com.sun.net.httpserver.HttpExchange;

public class DefaultJORServer implements JavaOnRailsServer {

	private final BaseDispatcher baseDispatch;
	private final ApplicationResourceProvider resourceProvider;
	
	public DefaultJORServer(final ApplicationResourceProvider provider) {
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
	public ApplicationResourceProvider getResourceProvider() {
		return resourceProvider;
	}

}
