package browser;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

public class SingleThreadBrowserView implements BrowserView {
	private static final Thread SWT_THREAD = new Thread(new Runnable() {
		public void run() {
			swtDisplay = new Display();
			while (true) {
				if (!swtDisplay.readAndDispatch()) swtDisplay.sleep();
			}
		}
	});
	private static Display swtDisplay = null;
	
	private final HttpServer server;
	private final JComponent displayComponent;
	private final Canvas canvas;
	
	private Browser browser;
	private Shell shell;
	private Lock browserInitLock = new ReentrantLock();
	private boolean browserInitStarted = false;

	private String initialUrl = null;
	
	public SingleThreadBrowserView(HttpHandler handler) {
		this(handler, true);
	}
	
	public SingleThreadBrowserView(HttpHandler handler, boolean runSwtLoop) {
		server = initializeHttpServer(handler);
		
		displayComponent = new JPanel(new FillLayout());
		canvas = new Canvas();
		displayComponent.add(canvas);
		
		initializeListeners();
		
		// One of the listeners listens to the hierarchy, and initializes the
		// SWT browser when canvas is added to the hierarchy.
		
		if (runSwtLoop && !SWT_THREAD.isAlive()) {
			SWT_THREAD.start();
		}
	}

	private void initializeListeners() {
		
		canvas.addHierarchyListener(new HierarchyListener() {
			
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				if (displayComponent.getTopLevelAncestor() == null || !displayComponent.getTopLevelAncestor().isShowing()) {
					return;
				}
				if (browserInitLock.tryLock()) {
					try {
						if (browser == null && !browserInitStarted) {
							browserInitStarted = true;
							initializeSwtBrowser();
						}
					} finally {
						browserInitLock.unlock();
					}
				}
			}

		});
		
		displayComponent.addComponentListener(new ComponentListener() {

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
				if (swtDisplay != null && browser != null && shell != null) {
					swtDisplay.asyncExec(new Runnable() {
						public void run() {
							Dimension size = displayComponent.getSize();
							shell.setSize(size.width, size.height);
							browser.setSize(size.width, size.height);
							
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									canvas.repaint();
									
									displayComponent.revalidate();
									displayComponent.repaint();
								}
							});
						}
					});
				}
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// do nothing
			}
		});
	}

	private boolean initializeSwtBrowserCalledAlready = false;
	
	private void initializeSwtBrowser() {
		// Contract: this function will only be called once by a given view.
		assert ! initializeSwtBrowserCalledAlready : "Initialization function of SWT Browser should only be called once.";
		initializeSwtBrowserCalledAlready = true;
		
		if (swtDisplay == null) {
			// Kick off thread that polls swt display until it is ready.
			Thread pollingThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (swtDisplay == null) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					createSwtBrowserAndShellWithBridge();
				}
			});
			pollingThread.start();
		} else {
			createSwtBrowserAndShellWithBridge();
		}
	}
	
	private void createSwtBrowserAndShellWithBridge() {
		swtDisplay.asyncExec(new Runnable() {
			public void run() {
				Dimension size = displayComponent.getSize();
				
				shell = SWT_AWT.new_Shell(swtDisplay, canvas);
				shell.setSize(size.width, size.height);
				browser = new Browser(shell, SWT.NONE);
				browser.setLayoutData(new GridData(GridData.FILL_BOTH));
				browser.setSize(size.width, size.height);
				shell.open();
				
				if (initialUrl != null) {
					browser.setUrl(initialUrl);
				}
			}
		});		
	}

	private HttpServer initializeHttpServer(HttpHandler handler) {
		HttpServer server = null;
		int tries = 0;
		while (server == null && tries < 200) {
			int port = 3000 + tries;
			tries++;
			try {
				server = HttpServer.create(new InetSocketAddress(port), -1);
				server.createContext("/", handler);
				server.start();
			} catch (IOException e) {
				// do nothing, just try a new port
			}
		}
		assert server != null : "Http Server was not initialized, even though we tried all ports between 3000 and 3200.";
		return server;
	}

	@Override
	public void runSwtDispatchLoop() {
		SWT_THREAD.run();
	}

	@Override
	public JComponent getDisplayComponent() {
		return displayComponent;
	}

	@Override
	public void dispose() {
		server.stop(0);
	}

	@Override
	public void goToUrl(String url) {
		if (!url.startsWith("http://")) {
			if (url.startsWith("/")) {
				url = "http://localhost:" + getPort() + url;
			} else {
				url = "http://localhost:" + getPort() + "/" + url;
			}
		}
		
		// Set the initial url in case the browser has not been initialized.
		// This should not produce a race condition.
		initialUrl = url;
		if (browser != null) {
			browser.setUrl(url);
		}
	}
	
	public int getPort() {
		return server.getAddress().getPort();
	}

}
