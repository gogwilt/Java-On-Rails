package javaonrails.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javaonrails.resource.ApplicationResourceProvider;
import javaonrails.resource.SystemResourceProvider;
import javaonrails.ruby.DefaultRubyProvider;

import org.jruby.embed.ScriptingContainer;
import org.jruby.javasupport.JavaEmbedUtils.EvalUnit;

import rails.JORSystem;
import sampleapp.SampleJORApp;

/**
 * IRB-like testing shell for ScriptContainer.
 * @author rbuckheit
 */
public class ScriptContainerShell {

	public static void main(String[] args) throws IOException {

		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		final ScriptingContainer sc = new DefaultRubyProvider(new SystemResourceProvider(
				JORSystem.class), new ApplicationResourceProvider(SampleJORApp.class))
				.getScriptingContainer();

		EvalUnit unit = sc.parse("require 'rubygems'");
		unit.run();

		unit = sc.parse("require 'rails'");
		unit.run();
		
		unit = sc.parse("puts \"Rails Version is #{Rails::VERSION::STRING}\"");
		System.out.println(unit.run());

		// run shell
		System.out.println("\nStarting shell...");
		while (true) {
			System.out.print("> ");
			try {
				final String input = br.readLine();
				final EvalUnit inputUnit = sc.parse(input);
				System.out.println(inputUnit.run());
			} catch (final Throwable t) {
				t.printStackTrace();
			}
			System.out.println("");
			System.err.flush();
			System.out.flush();
		}

	}
}
