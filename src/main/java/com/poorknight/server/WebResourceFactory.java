package com.poorknight.server;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.server.settings.Environment;
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

	// Alexa allows a skill ~8s to respond. Failing within that window lets the skill answer
	// "couldn't reach the house" itself instead of Alexa's generic invalid-response error,
	// and stops a hung Pi from wedging the scheduled-task threads forever.
	static final int CONNECT_TIMEOUT_MILLIS = 2000;
	static final int READ_TIMEOUT_MILLIS = 5000;

	private static String houseIp = null;

	public static void setHouseIp(String newIp) {
		if (ipAddressHasChanged(newIp)) {
			TextMessageAlerter.instance().sendTextMessage("House IP address changed: " + newIp);
			System.out.println("House IP address changed: " + newIp);
		}
		houseIp = newIp;
	}

	private static boolean ipAddressHasChanged(String newIp) {
		if (houseIp == null) {
			String house_url = Environment.getEnvironmentVariable("HOUSE_URL");
			return newIp != null && !newIp.equals(house_url);
		}
		return !houseIp.equals(newIp);
	}

	public static WebResource.Builder buildSecuredHomeWebResource(final String path) {
		final String url = findHouseUrl() + path;
		final String authSecret = Environment.getEnvironmentVariable("AUTHENTICATION_SECRET");

		final HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
		final ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, buildSSLContext()));
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, CONNECT_TIMEOUT_MILLIS);
		config.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, READ_TIMEOUT_MILLIS);
		final Client client = Client.create(config);

		return client.resource(url).header("auth-secret", authSecret);
	}

	private static String findHouseUrl() {
		if (houseIp != null) {
			return houseIp;
		}
		return Environment.getEnvironmentVariable("HOUSE_URL");
	}

	private static SSLContext buildSSLContext() {
		final SslConfigurator sslConfigurator = SslConfigurator.newInstance()
				.trustStoreFile(Environment.getEnvironmentVariable("HOUSE_TRUSTSTORE_PATH"))
				.trustStorePassword(Environment.getEnvironmentVariable("HOUSE_TRUSTSTORE_PASSWORD"));

		return sslConfigurator.createSSLContext();
	}
}
