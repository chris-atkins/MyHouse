package com.poorknight.hello;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;

@Path("/")
public class HelloWorldEndpoint {

	   @GET
	   @Produces(MediaType.TEXT_PLAIN)
	   public String test() {
	       return "hi";
	   }
	   
	   @POST
	   @Consumes(MediaType.APPLICATION_JSON)
	   @Produces(MediaType.APPLICATION_JSON)
	   public HelloWorldResponse response(JsonNode request) {
		   System.out.println(request.toString());
		   return new HelloWorldResponse();
	   }
}