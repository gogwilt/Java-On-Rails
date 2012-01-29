package javaonrails.server;

import java.io.IOException;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.ruby.RubyProvider;

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

	private final ScriptEngineManager manager;

	private final ApplicationResourceProvider application;
	private final SystemResourceProvider system;
	private final RubyProvider rubyProvider;

	public ControllerDispatcher(final ApplicationResourceProvider applicationProvider,
			final SystemResourceProvider systemProvider, final RubyProvider rubyProvider) {
		
		this.manager = new ScriptEngineManager();
		
		this.system = systemProvider;
		this.application = applicationProvider;
		this.rubyProvider = rubyProvider;
		
	}

	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		
		final String routeRequest = exchange.getRequestURI().toString();
		
		/*
		System.out.println(exchange.getRequestHeaders().keySet());
		InputStream body = exchange.getRequestBody();
		BufferedReader reader = new BufferedReader(new InputStreamReader(body));
		while(reader.ready()) System.out.println(reader.readLine());
		*/
		
		final ScriptingContainer container = rubyProvider.getScriptingContainer();
		
		// query the system for for the route
		container.put("route_request", routeRequest);
		container.runScriptlet("route_result = JORController::Routing::Routes.get(route_request)");
		final String result = (String)container.get("route_result");
		
		// process valid routes
		if (result != null) {
			System.out.println("Controller dispatcher routed request to: " + result);
			container.runScriptlet("result = JORController::Routing::Routes.process(route_request)");
			
			
			return true;
		}
				
		return false;
	}
	
}