package javaonrails.server;

import java.io.IOException;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.ruby.DefaultRubyProvider;

import javax.script.ScriptEngineManager;

import org.jruby.embed.ScriptingContainer;

import com.sun.net.httpserver.HttpExchange;

/**
 * Dispatcher which instantiates ruby controllers in response to dynamic content
 * requests.
 * 
 * @author rbuckheit
 */
public class ControllerDispatcher implements JORDispatcher {

	private final ScriptingContainer container;
	private final ScriptEngineManager manager;

	private final ApplicationResourceProvider application;
	private final SystemResourceProvider system;

	public ControllerDispatcher(final ApplicationResourceProvider provider,
			final SystemResourceProvider systemProvider) {
		
		this.manager = new ScriptEngineManager();
		
		this.system = systemProvider;
		this.application = provider;
		
		this.container = new DefaultRubyProvider(system, application).getScriptingContainer();
	}

	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		final String s = String.format("Routing exchange: %s %s %s", exchange.getProtocol(),
				exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());
		System.out.println(s);
		
		final String routeRequest = exchange.getRequestURI().toString();
		
		container.put("route_request", routeRequest);
		container.runScriptlet("route_result = JORController::Routing::Routes.get(route_request)");
		final String result = (String)container.get("route_result");
		
		System.out.println("Routing result: " + result);
		
		return false;
	}
	
}