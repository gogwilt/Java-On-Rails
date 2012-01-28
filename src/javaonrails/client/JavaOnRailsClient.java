package javaonrails.client;

import javaonrails.httpserver.JORLocalServer;
import javaonrails.server.JavaOnRailsServer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Shell;

public class JavaOnRailsClient {

	public static int PORT = 3000;
	
	private final Browser browser;
	private final JavaOnRailsServer server;
	
	private final JORLocalServer httpServer;
	
	public JavaOnRailsClient(Shell shell, JavaOnRailsServer server) {
		browser = new Browser(shell, SWT.NONE);
		this.server = server;
		this.httpServer = new JORLocalServer(server, PORT);
		this.httpServer.getHttpServer().start();
	}
	
	public void goToUrl(String url) {
		browser.setUrl(url);
	}
	
	public Browser getBrowser() {
		return browser;
	}
	
	public void dispose() {
		httpServer.getHttpServer().stop(0);
	}
}
