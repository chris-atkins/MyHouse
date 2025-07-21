package com.poorknight.server;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.sun.jersey.api.client.WebResource;
import org.glassfish.jersey.SslConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SslConfigurator.class, TextMessageAlerter.class})
public class WebResourceFactoryTest {

	@Mock
	private TextMessageAlerter textMessageAlerter;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(TextMessageAlerter.class);
		when(TextMessageAlerter.instance()).thenReturn(textMessageAlerter);
	}

	@After
	public void tearDown() {
		WebResourceFactory.setHouseIp(null);
	}

	@Test
	public void overridesIpFromEnvironmentVariable_WithAMethod() throws Exception {
		setUpMocksForSslConfig();
		Map<String, String> env = new ImmutableMap.Builder<String, String>()
				.put("HOUSE_URL", "1.2.3.4")
				.put("HOUSE_TRUSTSTORE_PATH", "nothing")
				.put("HOUSE_TRUSTSTORE_PASSWORD", "really").build();
		setEnv(env);

		WebResource.Builder webResourceBuilder = WebResourceFactory.buildSecuredHomeWebResource("/theBestPath");
		String url = getUrl(webResourceBuilder);
		assertThat(url).isEqualTo("1.2.3.4/theBestPath");

		WebResourceFactory.setHouseIp("5.5.5.5");

		webResourceBuilder = WebResourceFactory.buildSecuredHomeWebResource("/theBestPath");
		url = getUrl(webResourceBuilder);
		assertThat(url).isEqualTo("5.5.5.5/theBestPath");
	}

	@Test
	public void whenOverridingIp_AndItDoesNotMatchEnv_SendsATextMessage() throws Exception {
		Map<String, String> env = new ImmutableMap.Builder<String, String>().put("HOUSE_URL", "1.2.3.4").build();
		setEnv(env);

		WebResourceFactory.setHouseIp("5.5.5.5");
		verify(textMessageAlerter).sendTextMessage("House IP address changed: 5.5.5.5");
	}

	@Test
	public void whenOverridingIpMultipleTimes_AndItDoesNotMatchEnv_OnlySendsOneTextMessage() throws Exception {
		Map<String, String> env = new ImmutableMap.Builder<String, String>().put("HOUSE_URL", "1.2.3.4").build();
		setEnv(env);

		WebResourceFactory.setHouseIp("5.5.5.5");
		WebResourceFactory.setHouseIp("5.5.5.5");
		WebResourceFactory.setHouseIp("5.5.5.5");
		WebResourceFactory.setHouseIp("5.5.5.5");
		verify(textMessageAlerter, times(1)).sendTextMessage("House IP address changed: 5.5.5.5");
	}

	@Test
	public void whenOverridingIpMultipleTimes_AndItDoesNotMatchPrevious_SendsOneTextMessage() throws Exception {
		Map<String, String> env = new ImmutableMap.Builder<String, String>().put("HOUSE_URL", "1.2.3.4").build();
		setEnv(env);

		WebResourceFactory.setHouseIp("1.2.3.4");
		WebResourceFactory.setHouseIp("1.2.3.4");
		WebResourceFactory.setHouseIp("3.3.3.3");
		WebResourceFactory.setHouseIp("3.3.3.3");
		verify(textMessageAlerter, times(1)).sendTextMessage("House IP address changed: 3.3.3.3");
	}

	private void setUpMocksForSslConfig() {
		PowerMockito.mockStatic(SslConfigurator.class);
		SslConfigurator sslConfigurator = mock(SslConfigurator.class);
		PowerMockito.when(SslConfigurator.newInstance()).thenReturn(sslConfigurator);
		when(sslConfigurator.trustStoreFile(anyString())).thenReturn(sslConfigurator);
		when(sslConfigurator.trustStorePassword(anyString())).thenReturn(sslConfigurator);
		when(sslConfigurator.createSSLContext()).thenReturn(mock(SSLContext.class));
	}

	private static void setEnv(Map<String, String> newenv) throws Exception {
		try {
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
			env.putAll(newenv);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
			cienv.putAll(newenv);
		} catch (NoSuchFieldException e) {
			Class[] classes = Collections.class.getDeclaredClasses();
			Map<String, String> env = System.getenv();
			for(Class cl : classes) {
				if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
					Field field = cl.getDeclaredField("m");
					field.setAccessible(true);
					Object obj = field.get(env);
					Map<String, String> map = (Map<String, String>) obj;
					map.clear();
					map.putAll(newenv);
				}
			}
		}
	}

	private String getUrl(WebResource.Builder webResourceBuilder) throws Exception {
		Field field = webResourceBuilder.getClass().getDeclaredField("this$0");
		field.setAccessible(true);
		return field.get(webResourceBuilder).toString();
	}
}