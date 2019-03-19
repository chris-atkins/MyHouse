package com.poorknight.server;

import com.poorknight.settings.Environment;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SSLConnectionFactoryBuilder {

	private SSLConnectionFactoryBuilder() {
		//private constructor to avoid ever being instantiated
	}

	static SslConnectionFactory build() {
		final SslConnectionFactory sslConnectionFactory = new SslConnectionFactory();
		final SslContextFactory sslContextFactory = sslConnectionFactory.getSslContextFactory();
		sslContextFactory.setKeyStorePath(Environment.getEnvironmentVariable("SSL_KEYSTORE_PATH"));
		sslContextFactory.setKeyStorePassword(Environment.getEnvironmentVariable("SSL_KEYSTORE_PASSWORD"));
		sslContextFactory.setIncludeCipherSuites(".*");

		String[] cipherSuites = buildCipherSuitesToExclude(sslContextFactory);
		sslContextFactory.setExcludeCipherSuites(cipherSuites);

		return sslConnectionFactory;
	}

	private static String[] buildCipherSuitesToExclude(SslContextFactory sslContextFactory) {
		String[] excludeCipherSuites = sslContextFactory.getExcludeCipherSuites();
		List<String> excluded = new LinkedList<>(Arrays.asList(excludeCipherSuites));
		excluded.add("^TLS_DHE.*$");
		return excluded.toArray(new String[excluded.size()]);
	}
}
