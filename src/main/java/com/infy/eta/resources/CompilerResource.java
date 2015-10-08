package com.infy.eta.resources;

import com.infy.eta.compilers.JavaCompiler;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("compile")
public class CompilerResource {

	private static Logger logger = Logger.getLogger(CompilerResource.class);

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response compileProgram(@FormParam("data") String data, @FormParam("pId") String problemId) {
		logger.info("Request received to compile "+ data +" for problem "+ problemId);
		String buffer = new JavaCompiler(data, Integer.valueOf(problemId.substring(1))).invoke();
		logger.info("Request processing is complete. Sending the response.");
		String output = buffer.replace("\u0000", "").trim();
		logger.info(output);
		return Response.ok(output)
		               .header("Access-Control-Allow-Headers", "Content-Type")
		               .header("Access-Control-Allow-Methods", "POST, OPTIONS")
		               .header("Access-Control-Allow-Origin", "*")
		               .build();
	}


}
