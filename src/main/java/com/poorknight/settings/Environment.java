package com.poorknight.settings;

public class Environment {

	public static String getEnvironmentVariable(final String name) {
		return System.getenv(name);
	}
}
