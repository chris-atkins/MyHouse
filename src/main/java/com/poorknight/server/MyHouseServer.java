package com.poorknight.server;

import com.poorknight.echo.housecommand.HouseCommandMessager;
import com.poorknight.rest.EchoEndpoint;
import com.poorknight.rest.HouseEndpoint;
import com.poorknight.server.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import com.poorknight.timedlights.OutsideLightDesiredStateDecider;
import com.poorknight.timedlights.OutsideLightsController;
import com.poorknight.web.HelloWorldWebPageHandler;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MyHouseServer {

	private static final int PORT = 8443;
	private static final String HTTPS_SCHEME = "https";

	public static void main(final String[] args) {
		setupLogging();

		final Server server = new Server();

		final SslConnectionFactory sslConnectionFactory = setupSSL();
		final HttpConnectionFactory httpConnectionFactory = setupHttps();
		final ServerConnector serverConnector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);

		// ServerConnector serverConnector = new ServerConnector(server, setupHttp());

		serverConnector.setPort(PORT);
		server.addConnector(serverConnector);

		final ContextHandler webContext = new ContextHandler();
		webContext.setContextPath("/web/hello");
		webContext.setHandler(new HelloWorldWebPageHandler());

		final ServletContextHandler echoContextHandler = buildEchoContextHandler("/");
		final ServletContextHandler houseContextHandler = buildHouseContextHandler("/house");

		final ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { webContext, echoContextHandler, houseContextHandler });
		server.setHandler(contexts);

		FixedScheduleTaskManager fixedScheduleTaskManager = prepareFixedScheduleTasks();

		try {
			server.start();
			server.join();
			fixedScheduleTaskManager.startAllTasks();

			Logger.getLogger(this.getClass()).info("SERVER STARTED.");


		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

			fixedScheduleTaskManager.stopAllTasks();
			server.destroy();
		}
	}

	private static FixedScheduleTaskManager prepareFixedScheduleTasks() {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		final HouseCommandMessager houseCommandMessager = new HouseCommandMessager();
		final OutsideLightDesiredStateDecider decider = new OutsideLightDesiredStateDecider();
		final OutsideLightsController controller = new OutsideLightsController(decider, houseCommandMessager);
		return new FixedScheduleTaskManager(executor, new OutsideLightControllerRunnable(controller));
	}

	private static void setupLogging() {
		org.apache.log4j.BasicConfigurator.configure();
	}

	private static SslConnectionFactory setupSSL() {
		return SSLConnectionFactoryBuilder.build();
	}

	private static HttpConnectionFactory setupHttps() {
		final HttpConfiguration httpConf = new HttpConfiguration();
		httpConf.setSecurePort(PORT);
		httpConf.setSecureScheme(HTTPS_SCHEME);
		httpConf.addCustomizer(new SecureRequestCustomizer());
		final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConf);
		return httpConnectionFactory;
	}

	private static ServletContextHandler buildEchoContextHandler(final String rootPath) {
		final ServletContextHandler echoApiContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		echoApiContextHandler.setContextPath(rootPath);
		final ServletHolder holder = echoApiContextHandler.addServlet(ServletContainer.class, "/*");
		holder.setInitParameter("jersey.config.server.provider.classnames", "" + EchoEndpoint.class.getCanonicalName() + "," + JacksonFeature.class.getCanonicalName());
		return echoApiContextHandler;
	}

	private static ServletContextHandler buildHouseContextHandler(final String rootPath) {
		final ServletContextHandler houseApiContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		houseApiContext.setContextPath(rootPath);
		final ServletHolder houseHolder = houseApiContext.addServlet(ServletContainer.class, "/*");
		houseHolder.setInitParameter("jersey.config.server.provider.classnames", "" + HouseEndpoint.class.getCanonicalName() + "," + JacksonFeature.class.getCanonicalName());
		return houseApiContext;
	}

	@SuppressWarnings("unused")
	private static HttpConnectionFactory setupHttp() {
		final HttpConfiguration httpConf = new HttpConfiguration();
		httpConf.setSecurePort(PORT);
		final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConf);
		return httpConnectionFactory;
	}
}