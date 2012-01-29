package javaonrails.ruby;

import org.jruby.embed.ScriptingContainer;

/**
 * @author rbuckheit
 */
public interface RubyProvider {
	
	public ScriptingContainer getScriptingContainer();
	
	public void addToLoadPath(final String path);
	
	public void addLoadScript(final String script);

}
