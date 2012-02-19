package browser;

import javax.swing.JComponent;

public interface BrowserView {
	public void runSwtDispatchLoop();
	public JComponent getDisplayComponent();
	void dispose();
	void goToUrl(String url);
}
