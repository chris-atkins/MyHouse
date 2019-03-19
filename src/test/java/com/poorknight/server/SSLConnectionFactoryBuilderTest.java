package com.poorknight.server;

import com.poorknight.settings.Environment;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Environment.class, SSLConnectionFactoryBuilder.class})
public class SSLConnectionFactoryBuilderTest {

	private static String expectedKeyStorePath = RandomStringUtils.random(30);
	private static String expectedKeyStorePassword = RandomStringUtils.random(8);

	private SslConnectionFactory mockSslConnectionFactory = mock(SslConnectionFactory.class);
	private SslContextFactory mockContextFactory = mock(SslContextFactory.class);

	@Before
	public void setup() throws Exception {
		PowerMockito.mockStatic(Environment.class);
		when(Environment.getEnvironmentVariable("SSL_KEYSTORE_PATH")).thenReturn(expectedKeyStorePath);
		when(Environment.getEnvironmentVariable("SSL_KEYSTORE_PASSWORD")).thenReturn(expectedKeyStorePassword);

		PowerMockito.whenNew(SslConnectionFactory.class).withNoArguments().thenReturn(mockSslConnectionFactory);

		when(mockSslConnectionFactory.getSslContextFactory()).thenReturn(mockContextFactory);
	}

	@Test
	public void setsUpConnectionFactoryCorrectly() throws Exception {
		when(mockContextFactory.getExcludeCipherSuites()).thenReturn(new String[]{"hi"});

		final SslConnectionFactory result = SSLConnectionFactoryBuilder.build();

		assertThat(result).isSameAs(mockSslConnectionFactory);
		Mockito.verify(mockContextFactory).setKeyStorePath(expectedKeyStorePath);
		Mockito.verify(mockContextFactory).setKeyStorePassword(expectedKeyStorePassword);
		Mockito.verify(mockContextFactory).setIncludeCipherSuites(".*");
		Mockito.verify(mockContextFactory).setExcludeCipherSuites("hi","^TLS_DHE.*$");
	}

}