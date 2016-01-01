package com.poorknight.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.servlet.ServletContainer;

import com.amazon.speech.Sdk;
import com.poorknight.rest.EchoEndpoint;
import com.poorknight.web.HelloWorldWebPageHandler;

public class MyHouseServer {

    private static final int PORT = 8443;
    private static final String HTTPS_SCHEME = "https";
	
	public static void main(String[] args) {
		setupLogging();
	
		Server server = new Server();
		
		SslConnectionFactory sslConnectionFactory = setupSSL();
		HttpConnectionFactory httpConnectionFactory = setupHttps();
        ServerConnector serverConnector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);

//		ServerConnector serverConnector = new ServerConnector(server, setupHttp());
        
		serverConnector.setPort(PORT);
        Connector[] connectors = {serverConnector};
        server.setConnectors(connectors);

        
        ContextHandler webContext = new ContextHandler();
        webContext.setContextPath("/web/hello");
        webContext.setHandler(new HelloWorldWebPageHandler());
        
        ServletContextHandler apiContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        apiContext.setContextPath("/");
        ServletHolder holder = apiContext.addServlet(ServletContainer.class, "/*");
        holder.setInitParameter("jersey.config.server.provider.classnames", 
        		"" + EchoEndpoint.class.getCanonicalName() +
        		"," + JacksonFeature.class.getCanonicalName());
        
        
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {webContext, apiContext});
        server.setHandler(contexts);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			server.destroy();
		}
	}
	
	private static void setupLogging() {
		org.apache.log4j.BasicConfigurator.configure();
	}
	
	private static SslConnectionFactory setupSSL() {
		SslConnectionFactory sslConnectionFactory = new SslConnectionFactory();
		SslContextFactory sslContextFactory = sslConnectionFactory.getSslContextFactory();
		sslContextFactory.setKeyStorePath("/Users/chrisatkins/myssl/keystore");
		sslContextFactory.setKeyStorePassword("hiitsme5");
		sslContextFactory.setIncludeCipherSuites(Sdk.SUPPORTED_CIPHER_SUITES);
		return sslConnectionFactory;
	}

	private static HttpConnectionFactory setupHttps() {
		HttpConfiguration httpConf = new HttpConfiguration();
        httpConf.setSecurePort(PORT);
        httpConf.setSecureScheme(HTTPS_SCHEME);
        httpConf.addCustomizer(new SecureRequestCustomizer());
        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConf);
		return httpConnectionFactory;
	}
	
	private static HttpConnectionFactory setupHttp() {
		HttpConfiguration httpConf = new HttpConfiguration();
        httpConf.setSecurePort(PORT);
        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConf);
		return httpConnectionFactory;
	}
}