package com.poorknight.server;

import com.poorknight.server.settings.Environment;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
public class SSLConnectionFactoryBuilderTest {

    private static final String expectedKeyStorePath = RandomStringUtils.random(30);
    private static final String expectedKeyStorePassword = RandomStringUtils.random(8);

    private final SslContextFactory mockContextFactory = mock(SslContextFactory.class);

    @Test
    public void setsUpConnectionFactoryCorrectly() {
        try (MockedStatic<Environment> mockedEnvironment = Mockito.mockStatic(Environment.class)) {
            try (MockedConstruction<SslConnectionFactory> mockedConstruction = mockConstruction(SslConnectionFactory.class, (mock, context) -> {
                when(mock.getSslContextFactory()).thenReturn(mockContextFactory);
            })) {
                mockedEnvironment.when(() -> Environment.getEnvironmentVariable("SSL_KEYSTORE_PATH")).thenReturn(expectedKeyStorePath);
                mockedEnvironment.when(() -> Environment.getEnvironmentVariable("SSL_KEYSTORE_PASSWORD")).thenReturn(expectedKeyStorePassword);

                when(mockContextFactory.getExcludeCipherSuites()).thenReturn(new String[]{"hi"});

                final SslConnectionFactory result = SSLConnectionFactoryBuilder.build();

                assertThat(mockedConstruction.constructed().size()).isEqualTo(1);
                assertThat(result).isSameAs(mockedConstruction.constructed().get(0));
                Mockito.verify(mockContextFactory).setKeyStorePath(expectedKeyStorePath);
                Mockito.verify(mockContextFactory).setKeyStorePassword(expectedKeyStorePassword);
                Mockito.verify(mockContextFactory).setIncludeCipherSuites(".*");
                Mockito.verify(mockContextFactory).setExcludeCipherSuites("hi", "^TLS_DHE.*$");
            }
        }
    }
}