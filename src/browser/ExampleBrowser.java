package browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Example copied from:
 * http://stackoverflow.com/questions/3749154/swt-awt-bridge-swt-in-swing-problem
 *
 */
public class ExampleBrowser {
	public static void main(String[] args) {
	    Display display = new Display();
	    final Shell shell = new Shell(display);
	    shell.setText("Browser Example");
	    shell.setSize(1100, 800);

	    ToolBar toolbar = new ToolBar(shell, SWT.NONE);
	    toolbar.setBounds(5, 5, 200, 30);

	    ToolItem goButton = new ToolItem(toolbar, SWT.PUSH);
	    goButton.setText("Go");

	    ToolItem backButton = new ToolItem(toolbar, SWT.PUSH);
	    backButton.setText("Back");

	    ToolItem stopButton = new ToolItem(toolbar, SWT.PUSH);
	    stopButton.setText("Stop");

	    final Text text = new Text(shell, SWT.BORDER);
	    text.setBounds(5, 35, 400, 25);

	    final Browser browser = new Browser(shell, SWT.NONE);
	    browser.setBounds(5, 75, 1000, 800);

	    Listener listener = new Listener() {
	      @Override
		public void handleEvent(Event event) {
	        ToolItem item = (ToolItem) event.widget;
	        String string = item.getText();
	        if (string.equals("Back"))
	          browser.back();
	        else if (string.equals("Stop"))
	          browser.stop();
	        else if (string.equals("Go"))
	          browser.setUrl(text.getText());
	      }
	    };

	    goButton.addListener(SWT.Selection, listener);
	    backButton.addListener(SWT.Selection, listener);
	    stopButton.addListener(SWT.Selection, listener);

	    text.addListener(SWT.DefaultSelection, new Listener() {
	      @Override
		public void handleEvent(Event e) {
	        browser.setUrl(text.getText());
	      }
	    });

	    shell.open();
	    browser.setUrl("http://www.google.com");
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	  }
}
