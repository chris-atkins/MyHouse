package com.poorknight.server;

import com.poorknight.settings.Environment;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.glassfish.jersey.SslConfigurator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class WebResourceFactory {

	public static WebResource buildSecuredHomeWebResource(final String path) {
		final String url = Environment.getEnvironmentVariable("HOUSE_URL") + path;
		final HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
		final ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, buildSSLContext()));
		final Client client = Client.create(config);

		return client.resource(url);
	}

	private static SSLContext buildSSLContext() {
		final SslConfigurator sslConfigurator = SslConfigurator.newInstance()
				.trustStoreFile(Environment.getEnvironmentVariable("HOUSE_TRUSTSTORE_PATH"))
				.trustStorePassword(Environment.getEnvironmentVariable("HOUSE_TRUSTSTORE_PASSWORD"));

		return sslConfigurator.createSSLContext();
	}
}
