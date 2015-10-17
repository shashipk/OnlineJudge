package com.infy.eta;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * RestServer class.
 */
public class RestServer {
	// Base URI the Grizzly HTTP server will listen on
	private static final String BASE_URI = "http://localhost:1221";
	private static       Logger logger   = Logger.getLogger(RestServer.class);

	/**
	 * RestServer method.
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final HttpServer server = startServer();
		logger.info(String.format("Jersey app started with WADL available at %sapplication.wadl\nHit enter to stop it...", BASE_URI));
		System.in.read();
		server.shutdown();
	}

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 *
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and providers
		// in com.infy.eta package
		final ResourceConfig rc = new ResourceConfig().packages("com.infy.eta");

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}
}

