package com.poorknight.server;

import com.amazon.speech.Sdk;
import com.poorknight.settings.Environment;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class SSLConnectionFactoryBuilder {

	private SSLConnectionFactoryBuilder() {
		//private constructor to avoid ever being instantiated
	}

	static SslConnectionFactory build() {
		final SslConnectionFactory sslConnectionFactory = new SslConnectionFactory();
		final SslContextFactory sslContextFactory = sslConnectionFactory.getSslContextFactory();
		sslContextFactory.setKeyStorePath(Environment.getEnvironmentVariable("SSL_KEYSTORE_PATH"));
		sslContextFactory.setKeyStorePassword(Environment.getEnvironmentVariable("SSL_KEYSTORE_PASSWORD"));
		sslContextFactory.setIncludeCipherSuites(Sdk.SUPPORTED_CIPHER_SUITES);
		return sslConnectionFactory;
	}
}
