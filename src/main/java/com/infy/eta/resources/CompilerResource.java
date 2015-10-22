package com.infy.eta.resources;

import com.infy.eta.compilers.JavaCompiler;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("compile")
public class CompilerResource {

	private static final Logger logger = Logger.getLogger(CompilerResource.class);

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response compileProgram(@FormParam("data") String data, @FormParam("pId") String problemId) {
		logger.info("Request received to compile " + data + " for problem " + problemId);
		HashMap<String, Object> map = new HashMap<>();
		try {
			String output = new JavaCompiler(data, Integer.valueOf(problemId)).invoke();
			map.put("success", true);
			map.put("object", output.replace("\u0000", "").trim());
		} catch (Exception e) {
			map.put("success", false);
			map.put("error", "Exception occurred while processing request with message " + e.getMessage());
		}
		logger.info("Request processing is complete. Sending the response.");
		return getResponse(new JSONObject(map));
	}

	private Response getResponse(JSONObject jsonObject) {
		Response response;
		response = Response.ok(jsonObject.toString())
		                   .header("Access-Control-Allow-Headers", "Content-Type")
		                   .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
		                   .header("Access-Control-Allow-Origin", "*")
		                   .build();
		return response;
	}


}
