package com.poorknight.server;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.sun.jersey.api.client.WebResource;
import org.glassfish.jersey.SslConfigurator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebResourceFactoryTest {

	@Mock
	private TextMessageAlerter textMessageAlerter;

	@Test
	public void overridesIpFromEnvironmentVariable_WithAMethod() throws Exception {
		try (MockedStatic<SslConfigurator> mockedSSLConfig = mockStatic(SslConfigurator.class)) {
			try (MockedStatic<TextMessageAlerter> staticTextMessageAlerter = mockStatic(TextMessageAlerter.class)) {
				staticTextMessageAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

				setUpMocksForSslConfig(mockedSSLConfig);
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
		}
	}

	@Test
	public void whenOverridingIp_AndItDoesNotMatchEnv_SendsATextMessage() throws Exception {
		try (MockedStatic<TextMessageAlerter> staticTextMessageAlerter = mockStatic(TextMessageAlerter.class)) {
			staticTextMessageAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

			WebResourceFactory.setHouseIp("6.6.6.6");
			verify(textMessageAlerter).sendTextMessage("House IP address changed: 6.6.6.6");
		}
	}

	@Test
	public void whenOverridingIpMultipleTimes_AndItDoesNotMatchEnv_OnlySendsOneTextMessage() throws Exception {
		try (MockedStatic<TextMessageAlerter> staticTextMessageAlerter = mockStatic(TextMessageAlerter.class)) {
			staticTextMessageAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

			Map<String, String> env = new ImmutableMap.Builder<String, String>().put("HOUSE_URL", "1.2.3.4").build();
			setEnv(env);

			WebResourceFactory.setHouseIp("7.7.7.7");
			WebResourceFactory.setHouseIp("7.7.7.7");
			WebResourceFactory.setHouseIp("7.7.7.7");
			WebResourceFactory.setHouseIp("7.7.7.7");
			verify(textMessageAlerter, times(1)).sendTextMessage("House IP address changed: 7.7.7.7");
		}
	}

	@Test
	public void whenOverridingIpMultipleTimes_AndItDoesNotMatchPrevious_SendsOneTextMessage() throws Exception {
		try (MockedStatic<TextMessageAlerter> staticTextMessageAlerter = mockStatic(TextMessageAlerter.class)) {
			staticTextMessageAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

			Map<String, String> env = new ImmutableMap.Builder<String, String>().put("HOUSE_URL", "1.2.3.4").build();
			setEnv(env);

			WebResourceFactory.setHouseIp("1.2.3.4");
			WebResourceFactory.setHouseIp("1.2.3.4");
			WebResourceFactory.setHouseIp("3.3.3.3");
			WebResourceFactory.setHouseIp("3.3.3.3");
			verify(textMessageAlerter, times(1)).sendTextMessage("House IP address changed: 3.3.3.3");
		}
	}

	private void setUpMocksForSslConfig(MockedStatic<SslConfigurator> mockedSSLConfig) {
		SslConfigurator sslConfigurator = mock(SslConfigurator.class);
		mockedSSLConfig.when(SslConfigurator::newInstance).thenReturn(sslConfigurator);
		when(sslConfigurator.trustStoreFile(anyString())).thenReturn(sslConfigurator);
		when(sslConfigurator.trustStorePassword(anyString())).thenReturn(sslConfigurator);
		when(sslConfigurator.createSSLContext()).thenReturn(mock(SSLContext.class));
	}

	@SuppressWarnings("unchecked")
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