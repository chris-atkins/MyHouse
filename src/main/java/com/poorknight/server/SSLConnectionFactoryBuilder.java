package com.poorknight.server;

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
		sslContextFactory.setIncludeCipherSuites("SSL_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_RSA_WITH_RC4_128_SHA", "TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA", "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA", "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", "TLS_ECDH_RSA_WITH_RC4_128_SHA", "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_RSA_WITH_RC4_128_SHA");
		return sslConnectionFactory;
	}
}
