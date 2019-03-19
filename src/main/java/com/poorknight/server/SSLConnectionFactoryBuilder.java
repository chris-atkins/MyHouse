package com.poorknight.server;

import com.poorknight.settings.Environment;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class SSLConnectionFactoryBuilder {

	private SSLConnectionFactoryBuilder() {
		//private constructor to avoid ever being instantiated
	}

	static SslConnectionFactory build() {

//		try {
//			String[] supportedCipherSuites = SSLContext.getDefault().createSSLEngine().getSupportedCipherSuites();
//			System.out.println("SUPPORTED CIPHER SUITES");
//			for (String cipherSuite : supportedCipherSuites) {
//				System.out.println(cipherSuite);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}

		final SslConnectionFactory sslConnectionFactory = new SslConnectionFactory();
		final SslContextFactory sslContextFactory = sslConnectionFactory.getSslContextFactory();
		sslContextFactory.setKeyStorePath(Environment.getEnvironmentVariable("SSL_KEYSTORE_PATH"));
		sslContextFactory.setKeyStorePassword(Environment.getEnvironmentVariable("SSL_KEYSTORE_PASSWORD"));
		sslContextFactory.setIncludeCipherSuites(".*");
		sslContextFactory.setExcludeCipherSuites("TLS_DHE.*");
//		String[] excludeCipherSuites = sslContextFactory.getExcludeCipherSuites();
//		System.out.println("\n\nEXCLUDED CIPHER SUITES");
//		for (String excludedCipherSuite : excludeCipherSuites) {
//			System.out.println(excludedCipherSuite);
//		}
		return sslConnectionFactory;
	}
}
