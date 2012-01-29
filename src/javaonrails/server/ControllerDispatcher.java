package javaonrails.server;

import java.io.IOException;

import javax.script.ScriptEngineManager;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.resource.ApplicationResourceProvider.ApplicationResource;
import javaonrails.resource.SystemResourceProvider.SystemResource;

import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.jruby.javasupport.JavaEmbedUtils.EvalUnit;

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
		
		this.container = new ScriptingContainer(LocalVariableBehavior.PERSISTENT);
		this.manager = new ScriptEngineManager();
		
		this.system = systemProvider;
		this.application = provider;
	}

	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		final String s = String.format("Routing exchange: %s %s %s", exchange.getProtocol(),
				exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());
		System.out.println(s);
		
		final String routeRequest = exchange.getRequestURI().toString();
		
		/* Load system routing and routes */
		
		String routingFile = system.loadFile(SystemResource.CONTROLLER, "routing.rb");
		container.runScriptlet(routingFile);
		
		String routesFile = application.loadFile(ApplicationResource.CONFIG, "routes.rb");
		container.runScriptlet(routesFile);
		
		/* Query for route */
		container.put("route_request", routeRequest);
		container.put("route_result", null);
		container.runScriptlet("route_result = JORController::Routing::Routes.get(route_request)");
		final String result = (String)container.get("route_result");
		System.out.println("Routing result: " + result);
		
		// container.runScriptlet("puts JORController::Routing::Routes.dump()");
		
		return false;
	}
	
	/*
	public void hackz() {
		container.runScriptlet("@object = [1,2,3,4]; puts @object.join(\" hello world \")");
		final EvalUnit unit = container.parse("puts @object.class; puts @object.inspect");
		unit.run();
	}*/

}
