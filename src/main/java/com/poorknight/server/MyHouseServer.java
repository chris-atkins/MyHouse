package com.poorknight.server;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.echo.housecommand.HouseCommandMessager;
import com.poorknight.housestatus.*;
import com.poorknight.endpoints.EchoEndpoint;
import com.poorknight.endpoints.HouseEndpoint;
import com.poorknight.server.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import com.poorknight.thermostat.ThermostatMessager;
import com.poorknight.timedlights.OutsideLightDesiredStateDecider;
import com.poorknight.timedlights.OutsideLightsController;
import com.poorknight.timedtemp.AutomatedHouseTemperatureController;
import com.poorknight.time.TimeFinder;
import com.poorknight.web.HelloWorldWebPageHandler;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.poorknight.server.FixedScheduleTaskManager.*;

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

		prepareDatabase();

		FixedScheduleTaskManager fixedScheduleTaskManager = prepareFixedScheduleTasks();
		try {
			fixedScheduleTaskManager.startAllTasks();

			server.start();
			server.join();

		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

			fixedScheduleTaskManager.stopAllTasks();
			server.destroy();
		}
	}

	private static void prepareDatabase() {
		DatabaseConnector connector = new DatabaseConnector();
		MySqlConnectionParameters mysqlConnectionParameters = connector.getMysqlConnectionParameters();
		String jdbcUrl = mysqlConnectionParameters.getJdbcUrl();
		String user = mysqlConnectionParameters.getConnectionProps().getProperty("user");
		String password = mysqlConnectionParameters.getConnectionProps().getProperty("password");

		Flyway flyway = Flyway.configure().dataSource(jdbcUrl, user, password).load();
		flyway.migrate();
	}

	private static FixedScheduleTaskManager prepareFixedScheduleTasks() {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);

		final OutsideLightControllerRunnable outsideLightscontrollerRunnable = buildOutsideLightControllerRunnable();
		final AutomatedHouseTemperatureControllerRunnable automatedTempControllerRunnable = buildAutomatedHouseTemperatureControllerRunnable();
		final HouseStatusRecorderRunnable houseStatusRecorderRunnable = buildHouseStatusRecorderRunnable();

		return new FixedScheduleTaskManager(executor, outsideLightscontrollerRunnable, automatedTempControllerRunnable, houseStatusRecorderRunnable);
	}

	private static OutsideLightControllerRunnable buildOutsideLightControllerRunnable() {
		final HouseCommandMessager houseCommandMessager = new HouseCommandMessager();
		final OutsideLightDesiredStateDecider decider = new OutsideLightDesiredStateDecider();
		final OutsideLightsController outsideLightsController = new OutsideLightsController(decider, houseCommandMessager);
		return new OutsideLightControllerRunnable(outsideLightsController);
	}

	private static AutomatedHouseTemperatureControllerRunnable buildAutomatedHouseTemperatureControllerRunnable() {
		final TimeFinder timeFinder = new TimeFinder();
		final ThermostatMessager thermostatMessager = new ThermostatMessager();
		final AutomatedHouseTemperatureController automatedTempController = new AutomatedHouseTemperatureController(timeFinder, thermostatMessager);
		return new AutomatedHouseTemperatureControllerRunnable(automatedTempController);
	}

	private static HouseStatusRecorderRunnable buildHouseStatusRecorderRunnable() {
		final TimeFinder timeFinder = new TimeFinder();
		final ThermostatMessager thermostatMessager = new ThermostatMessager();
		final DatabaseConnector databaseConnector = new DatabaseConnector();
		final HouseStatusRepository houseStatusRepository = new HouseStatusRepository(databaseConnector);
		final WeatherRetriever weatherRetriever = new WeatherRetriever();
		final HouseStatusRecorder houseStatusRecorder = new HouseStatusRecorder(timeFinder, thermostatMessager, weatherRetriever, houseStatusRepository);
		final TextMessageAlerter textMessageAlerter = TextMessageAlerter.instance();
		return new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);
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