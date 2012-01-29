package javaonrails.server;

import java.io.IOException;

import javaonrails.JORUtils;
import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.ruby.DefaultRubyProvider;
import javaonrails.ruby.RubyProvider;

import com.sun.net.httpserver.HttpExchange;

public class DefaultJORServer implements JavaOnRailsServer {

	private final BaseDispatcher baseDispatch;
	private final ApplicationResourceProvider applicationProvider;
	private final SystemResourceProvider systemProvider;
	private final RubyProvider rubyProvider;

	public DefaultJORServer(final ApplicationResourceProvider applicationProvider,
			final SystemResourceProvider systemProvider) {
		this.applicationProvider = applicationProvider;
		this.systemProvider = systemProvider;
		this.rubyProvider = new DefaultRubyProvider(systemProvider, applicationProvider);
		
		baseDispatch = new BaseDispatcher(applicationProvider, systemProvider, rubyProvider);
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
