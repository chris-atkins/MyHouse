package com.poorknight.endpoints;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.endpoints.houseip.HouseIpRequest;
import com.poorknight.endpoints.houseip.HouseIpResponse;
import com.poorknight.endpoints.notification.NotifyRequest;
import com.poorknight.endpoints.notification.NotifyResponse;
import com.poorknight.server.WebResourceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class HouseEndpoint {

	@POST
	@Path("/notification")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public NotifyResponse notifyMe(final NotifyRequest notifyRequest) {
		TextMessageAlerter.instance().sendTextMessage(notifyRequest.getMessageContent());
		return new NotifyResponse("Message sent.");
	}

	@POST
	@Path("/ip")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HouseIpResponse updateHouseUrl(HouseIpRequest houseIpRequest) {
		WebResourceFactory.setHouseIp(houseIpRequest.getHouseIp());
		return new HouseIpResponse("Successfully updated house ip to " + houseIpRequest.getHouseIp());
	}
}
