package browser;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class BrowserView {

	private final HttpServer server;
	private final JComponent display;
	private final Canvas canvas;
	private Browser browser;
	private Shell shell;
	private Display swtDisplay;
	private final Thread swtThread;
	private volatile boolean browserInitialized = false;
	
	private Runnable swtDisplayAndBrowserCreateAndRun = new Runnable() {
		public void run() {
			BrowserView.this.swtDisplay = new Display();
			initializeBrowserSwtAwtBridge();
			while (true) {
				if (!swtDisplay.readAndDispatch()) swtDisplay.sleep();
			}
		}
	};

	public BrowserView(HttpHandler handler) {
		this(handler, 3000);
	}
	
	public BrowserView(HttpHandler handler, int port) {
		server = initializeHttpServer(handler, port);
		display = initializeDisplay();
		
		canvas = new Canvas();
		display.add(canvas);
		
		display.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// do nothing
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// do nothing				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				if (browserInitialized) {
					swtDisplay.asyncExec(new Runnable() {
						public void run() {
							Dimension size = display.getSize();
							shell.setSize(size.width, size.height);
							browser.setSize(size.width, size.height);
						}
					});
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// do nothing				
			}
		});
		
		swtThread = new Thread(swtDisplayAndBrowserCreateAndRun);
		swtThread.start();
	}

	private void initializeBrowserSwtAwtBridge() {		
		swtDisplay.asyncExec(new Runnable() {
			public void run() {
				Dimension size = display.getSize();
				
				shell = SWT_AWT.new_Shell(swtDisplay, canvas);
				shell.setSize(size.width, size.height);
				browser = new Browser(shell, SWT.NONE);
				browser.setLayoutData(new GridData(GridData.FILL_BOTH));
				browser.setSize(size.width, size.height);
				browser.setUrl("http://localhost:3000/test.html");
				shell.open();

				// TODO: some flag to indicate that this step is over.
				browserInitialized = true;
			}
		});
	}
	
	private JComponent initializeDisplay() {
		JPanel component = new JPanel(new FillLayout());
		component.setBackground(Color.red);
		return component;
	}

	public void goToUrl(String url) {
		browser.setUrl(url);
	}
	
	public JComponent getDisplayComponent() {
		return display;
	}
	
	public void dispose() {
		haltServer();
		try {
			swtThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private HttpServer initializeHttpServer(HttpHandler handler, int port) {
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(port), -1);
			server.createContext("/", handler);
			server.start();
		} catch (IOException e) {
			// This is bad.
			throw new RuntimeException(e);
		}
		return server;
	}

	private void haltServer() {
		server.stop(0);
	}
}
