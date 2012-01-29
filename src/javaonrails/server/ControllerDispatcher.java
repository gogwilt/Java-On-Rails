package javaonrails.server;

import java.io.IOException;

import javaonrails.ApplicationResourceProvider;

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
	private final ApplicationResourceProvider resourceProvider;
	
	public ControllerDispatcher(final ApplicationResourceProvider provider) {
		container = new ScriptingContainer();
		resourceProvider = provider;
		
		container.runScriptlet("@object = [1,2,3,4]; puts @object.join(\" hello world \")");
		final EvalUnit unit = container.parse("puts @object.class; puts @object.inspect");
		unit.run();
	}

	@Override
	public boolean routeExchange(final HttpExchange exchange) throws IOException {
		final String s = String.format("Routing exchange: %s %s %s", exchange.getProtocol(),
				exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());
		System.out.println(s);

		return false;
	}
	
	public void loadRoutes() {
	}

}
