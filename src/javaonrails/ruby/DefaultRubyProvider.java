package javaonrails.ruby;

import java.net.URL;
import java.util.List;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.ApplicationResourceProvider.ApplicationResource;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.resource.SystemResourceProvider.SystemResource;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.jruby.javasupport.JavaEmbedUtils.EvalUnit;
import org.jruby.runtime.builtin.IRubyObject;

import com.google.common.collect.Lists;

/**
 * Ruby provider which creates new disposable scripting containers for each
 * call.
 * 
 * @author rbuckheit
 */
public class DefaultRubyProvider implements RubyProvider {

	private final List<String> containerLoadPath;
	private final List<String> loadScripts;

	private final ApplicationResourceProvider application;
	private final SystemResourceProvider system;

	public DefaultRubyProvider(final SystemResourceProvider system,
			final ApplicationResourceProvider application) {
		containerLoadPath = Lists.newArrayList();
		loadScripts = Lists.newArrayList();

		this.application = application;
		this.system = system;

		setSystemLoadPaths();
		setSystemLoadScripts();

		setApplicationLoadPaths();
		setApplicationLoadScripts();
	}

	@Override
	public ScriptingContainer getScriptingContainer() {
		final ScriptingContainer sc = new ScriptingContainer(LocalContextScope.SINGLETON,
				LocalVariableBehavior.PERSISTENT);
		sc.setLoadPaths(containerLoadPath);
		runLoadScripts(sc);
		return sc;
	}

	@Override
	public void addToLoadPath(String path) {
		containerLoadPath.add(path);
	}

	private void setSystemLoadPaths() {
		for (final SystemResource sysRes : SystemResource.values())
			addToLoadPath(system.getResourceLoadPath(sysRes).getPath());
	}

	private void setApplicationLoadPaths() {
		for (final ApplicationResource appRes : ApplicationResource.values()) {
			final URL url = application.getResourceLoadPath(appRes);
			if (url != null && url.getPath() != null) {
				addToLoadPath(url.getPath());
			}
		}
	}

	private void setSystemLoadScripts() {
		// TODO - possible auto-loading
		addLoadScript("require 'routing'");
	}

	private void setApplicationLoadScripts() {
		addLoadScript("require 'routes'");
	}

	@Override
	public void addLoadScript(String script) {
		loadScripts.add(script);
	}

	private void runLoadScripts(final ScriptingContainer sc) {
		for (final String script : loadScripts) {
			final EvalUnit un = sc.parse(script);
			final IRubyObject res = un.run();
			System.out.println("Ran: " + script + "\tresult: " + res);
		}
	}

}
