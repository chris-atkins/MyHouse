package com.poorknight.echo.housemode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.MediaType;

import static com.poorknight.echo.housemode.HouseMode.AT_WORK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WebResourceFactory.class)
public class HouseModeMessagerTest {

	private final static String houseStatusPath = "/house";

	@Mock
	private Client client;

	@Mock
	private WebResource.Builder webResource;

	@Mock
	private WebResource.Builder builder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private HouseModeMessager messager;

	@Before
	public void setup() {
		PowerMockito.mockStatic(WebResourceFactory.class);
		PowerMockito.when(WebResourceFactory.buildSecuredHomeWebResource(houseStatusPath)).thenReturn(webResource);
		messager = new HouseModeMessager();
	}

	@Test
	public void putsDesiredMode() throws Exception {
		when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);

		messager.requestHouseMode(AT_WORK);
		verify(builder).put(eq(JsonNode.class), captor.capture());

		JsonNode sentRequest = captor.getValue();

		final JsonNode expectedRequest = JsonNodeFactory.instance.objectNode().put("mode", AT_WORK.getModeAsString());
		assertThat(sentRequest).isEqualTo(expectedRequest);
	}
}