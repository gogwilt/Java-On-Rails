package javaonrails.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.server.DefaultJORServer;
import javaonrails.server.JavaOnRailsServer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rails.JORSystem;
import sampleapp.SampleJORApp;
import browser.BrowserView;
import browser.SingleThreadBrowserView;


public class BrowserViewTester {
	public static void main(String args[]) {		
		final JavaOnRailsServer server = new DefaultJORServer(new ApplicationResourceProvider(SampleJORApp.class),
				new SystemResourceProvider(JORSystem.class));
		final BrowserView view = new SingleThreadBrowserView(server);
		view.goToUrl("/test.html");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame mainFrame = new JFrame("Main Window");
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JPanel mainPanel = new JPanel();
				
				mainPanel.setLayout(new BorderLayout());				
				mainPanel.add(view.getDisplayComponent(), BorderLayout.CENTER);

				mainFrame.setSize(500, 300);
				mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
				mainFrame.setVisible(true);		
			}
		});
	}
}
