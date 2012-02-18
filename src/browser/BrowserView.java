package browser;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class BrowserView {
	static {
		System.err.println("Setting xembedserver to true");
		System.setProperty("sun.awt.xembedserver", "true");
	}
	
	private static int lastPort = 3000;

	private final HttpServer server;
	private final JComponent display;
	private final Canvas canvas;
	private Browser browser;
	private Shell shell;
	private Display swtDisplay;
	private final Thread swtThread;
	private volatile boolean browserInitialized = false;
	private final int port;
	
	private final Lock initialUrlLock = new ReentrantLock();
	private String initialUrl = null;
	
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
		this(handler, true);
	}
	
	public BrowserView(HttpHandler handler, boolean runSwtLoop) {
		this(handler, lastPort++, runSwtLoop);
	}
	
	public BrowserView(HttpHandler handler, int port, final boolean runSwtLoop) {
		this.port = port;
		server = initializeHttpServer(handler, port);
		display = initializeDisplay();
		
		canvas = new Canvas();
		display.add(canvas);
		swtThread = new Thread(swtDisplayAndBrowserCreateAndRun);
		
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
							
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									canvas.repaint();
									
									display.revalidate();
									display.repaint();
								}
							});
						}
					});
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// do nothing				
			}
		});
		
		display.addAncestorListener(new AncestorListener() {
			
			@Override
			public void ancestorRemoved(AncestorEvent arg0) {
			}
			
			@Override
			public void ancestorMoved(AncestorEvent arg0) {
			}
			
			@Override
			public void ancestorAdded(AncestorEvent arg0) {
				if (!swtThread.isAlive() && runSwtLoop) {
					swtThread.start();
				}
			}
		});
	}
	
	public void runSwtLoop() {
		swtThread.run();
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
				shell.open();
				
				initialUrlLock.lock();
				try {
					if (initialUrl != null) {
						browser.setUrl(initialUrl);
					}
				} finally {
					browserInitialized = true;
					initialUrlLock.unlock();
				}
			}
		});
	}
	
	private JComponent initializeDisplay() {
		JPanel component = new JPanel(new FillLayout());
		component.setBackground(Color.red);
		return component;
	}

	public void goToUrl(final String url) {
		initialUrlLock.lock();
		try {
			if (browserInitialized) {
				swtDisplay.asyncExec(new Runnable() {
					public void run() {
						browser.setUrl(url);
					}
				});
			} else {
				initialUrl = url;
			}
		} finally {
			initialUrlLock.unlock();
		}
	}
	
	public JComponent getDisplayComponent() {
		return display;
	}
	
	public void dispose() {
		haltServer();
		try {
			swtThread.join(50);
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

	public int getPort() {
		return port;
	}
}
