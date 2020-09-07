package com.poorknight.server.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldWebPageHandler extends AbstractHandler {

	private static final Logger logger = LoggerFactory.getLogger(HelloWorldWebPageHandler.class);

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.info("Serving response for GET request at {}", "/web/hello");

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		response.getWriter().println("<h1>Hello World, it's me!!</h1>");
	}

}
