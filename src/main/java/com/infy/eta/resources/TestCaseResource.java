package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeTestCases;
import com.infy.eta.utils.DoInTransaction;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amit Joshi on 10/7/2015.
 */
@Path("/testCase")
public class TestCaseResource {

	private final Logger logger = Logger.getLogger(TestCaseResource.class);

	@GET
	@Path("/getAll/{problemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTestCasesForProblem(@PathParam("problemId") String problemId) {
		logger.info("Received request to get all test cases for problem " + problemId);
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeTestCases> testCases = new DoInTransaction<List<JudgeTestCases>>() {
				@Override
				protected List<JudgeTestCases> doWork() {
					return new DoInTransaction<List<JudgeTestCases>>() {
						@Override
						protected List<JudgeTestCases> doWork() {
							Criteria criteria = session.createCriteria(JudgeTestCases.class);
							return criteria.add(Restrictions.eq("problemId", Integer.parseInt(problemId))).list();
						}
					}.execute();
				}
			}.execute();
			if (!testCases.isEmpty()) {
				map.put("success", true);
				map.put("object", testCases);
			} else {
				map.put("success", false);
				map.put("error", "NO PROBLEMS FOUND WITH PROBLEM ID " + problemId);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request with message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request with message " + e.getMessage());
		}
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

	@GET
	@Path("/getOne/{problemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOneTestCaseForProblem(@PathParam("problemId") String problemId) {
		logger.info("Received request to get one test case for problem " + problemId);
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeTestCases> testCases = new DoInTransaction<List<JudgeTestCases>>() {
				@Override
				protected List<JudgeTestCases> doWork() {
					return new DoInTransaction<List<JudgeTestCases>>() {
						@Override
						protected List<JudgeTestCases> doWork() {
							Criteria criteria = session.createCriteria(JudgeTestCases.class);
							return criteria.add(Restrictions.eq("problemId", Integer.parseInt(problemId))).list();
						}
					}.execute();
				}
			}.execute();
			if (!testCases.isEmpty()) {
				map.put("success", true);
				map.put("object", testCases);
			} else {
				map.put("success", false);
				map.put("error", "NO TEST CASES WERE FOUND FOR THIS PROBLEM ID " + problemId);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request with message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request with message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addTestCaseForProblem(@FormParam("problemId") String problemId, @FormParam("description") String description,
	                                      @FormParam("input") String input, @FormParam("output") String output, @FormParam("points") String points) {
		logger.info("Received request to add test case for problem " + problemId);
		HashMap<String, Object> map = new HashMap<>();
		try {
			JudgeTestCases testCase = new DoInTransaction<JudgeTestCases>() {
				@Override
				protected JudgeTestCases doWork() {
					JudgeTestCases testCase = new JudgeTestCases();
					testCase.setDescription(description);
					testCase.setInput(input);
					testCase.setOutput(output);
					testCase.setProblemId(Integer.parseInt(problemId));
					testCase.setPoints(Integer.parseInt(points));
					session.save(testCase);
					return testCase;
				}
			}.execute();
			if (testCase != null && testCase.getId() != null) {
				map.put("success", true);
				map.put("object", testCase);
			} else {
				map.put("success", false);
				map.put("error", "COULD NOT SAVE THE TEST CASE");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing save test case request with message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing save test case request with message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}
}
