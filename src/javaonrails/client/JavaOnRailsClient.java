package javaonrails.client;

import javaonrails.server.JavaOnRailsServer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Shell;

public class JavaOnRailsClient {

	private final Browser browser;
	private final JavaOnRailsServer server;
	
	public JavaOnRailsClient(Shell shell, JavaOnRailsServer server) {
		browser = new Browser(shell, SWT.NONE);
		this.server = server;
	}
	
	public void goToUrl(String url) {
		browser.setUrl(url);
	}
	
	public Browser getBrowser() {
		return browser;
	}
	
}
