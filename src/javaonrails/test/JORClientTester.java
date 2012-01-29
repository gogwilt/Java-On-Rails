package javaonrails.test;

import java.net.URISyntaxException;

import javaonrails.client.JavaOnRailsClient;
import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.server.DefaultJORServer;
import javaonrails.server.JavaOnRailsServer;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import rails.JORSystem;
import sampleapp.SampleJORApp;

public class JORClientTester {

	private final Display display;
	private final Shell shell;
	private final JavaOnRailsClient client;
	private final JavaOnRailsServer server;

	public JORClientTester() {
		display = new Display();
		shell = new Shell(display);
		shell.setText("Browser Example");
		shell.setSize(1100, 800);

		server = new DefaultJORServer(new ApplicationResourceProvider(SampleJORApp.class),
				new SystemResourceProvider(JORSystem.class));
		client = new JavaOnRailsClient(shell, server);

		final Browser browser = client.getBrowser();
		browser.setBounds(0, 0, 1100, 800);
	}

	public void run() {
		shell.open();
		client.goToUrl("http://localhost:3000/test/test_method");
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		client.dispose();
	}

	static JORClientTester jorct;

	public static void main(String[] args) throws URISyntaxException {
		jorct = new JORClientTester();
		jorct.run();
	}
}
