package javaonrails.server;

import java.io.IOException;

import javaonrails.JORUtils;
import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;

import com.sun.net.httpserver.HttpExchange;

public class DefaultJORServer implements JavaOnRailsServer {

	private final BaseDispatcher baseDispatch;
	private final ApplicationResourceProvider applicationProvider;
	private final SystemResourceProvider systemProvider;

	public DefaultJORServer(final ApplicationResourceProvider applicationProvider,
			final SystemResourceProvider systemProvider) {
		baseDispatch = new BaseDispatcher(applicationProvider, systemProvider);
		this.applicationProvider = applicationProvider;
		this.systemProvider = systemProvider;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!baseDispatch.routeExchange(exchange)) {
			JORUtils.replyWith404(exchange);
		}
	}

	@Override
	public ApplicationResourceProvider getResourceProvider() {
		return applicationProvider;
	}

}
