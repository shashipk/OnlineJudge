package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeProblems;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amit Joshi on 10/4/2015.
 */
@Path("/problem")
public class ProblemResource {

	private static final Logger logger = Logger.getLogger(ProblemResource.class);

	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProblem(@PathParam("id") String id) {
		logger.info("Received request to get the problem data for id " + id);
		HashMap<String, Object> map = new HashMap<>();
		try {
			Integer problemId = Integer.parseInt(id.substring(1));
			List<JudgeProblems> problemList = new DoInTransaction<List<JudgeProblems>>() {
				@Override
				protected List<JudgeProblems> doWork() {
					Criteria criteria = session.createCriteria(JudgeProblems.class);
					criteria.add(Restrictions.eq("id", problemId));
					List<JudgeProblems> list = criteria.list();
					logger.info("Problem found " + list.get(0).getTitle());
					return list;
				}
			}.execute();
			if (!problemList.isEmpty()) {
				map.put("success", true);
				map.put("object", problemList);
			} else {
				map.put("success", false);
				map.put("error", "Could not get Problem data with id " + id);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request. Exception Message: " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request. Exception Message: " + e.getMessage());
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
	@Path("/getByUsername/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProblemsByUsername(@PathParam("username") String username) {
		logger.info("received request to get the problems for username " + username);
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeProblems> problemList = new DoInTransaction<List<JudgeProblems>>() {
				@Override
				protected List<JudgeProblems> doWork() {
					Criteria criteria = session.createCriteria(JudgeProblems.class);
					criteria.add(Restrictions.eq("addedBy", username));
					return criteria.list();
				}
			}.execute();
			if (!problemList.isEmpty()) {
				logger.info("Problem found " + problemList.get(0).getTitle());
				map.put("success", true);
				map.put("object", problemList);
			} else {
				map.put("success", false);
				map.put("error", "Could not find any problems for username" + username);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request to get problems for username " + username.toUpperCase() + ". Exception  Message: " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request to get problems for username " + username.toUpperCase() + ". Exception Message: " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProblems() {
		logger.info("Received request to get all problems");
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeProblems> list = new DoInTransaction<List<JudgeProblems>>() {
				@Override
				protected List<JudgeProblems> doWork() {
					Criteria criteria = session.createCriteria(JudgeProblems.class);
					return criteria.list();
				}
			}.execute();
			if (!list.isEmpty()) {
				map.put("success", true);
				map.put("object", list);
			} else {
				map.put("success", false);
				map.put("error", "THERE ARE NO PROBLEMS IN DATABASE");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while getting all problems. Exception message: " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while getting all problems. Exception message: " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	@POST
	@Path("/getProblems")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProblems(@FormParam("category") String category, @FormParam("subCategory") String subCategory) {
		logger.info("Received request to get all problems by category and subcategory");
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeProblems> list = new DoInTransaction<List<JudgeProblems>>() {
				@Override
				protected List<JudgeProblems> doWork() {
					Criteria criteria = session.createCriteria(JudgeProblems.class);
					if (!category.equals("")) {
						criteria.add(Restrictions.eq("category", category));
					}
					if (!subCategory.equals("")) {
						criteria.add(Restrictions.eq("subcategory", subCategory));
					}
					return criteria.list();
				}
			}.execute();
			map.put("success", true);
			map.put("object", list);
		} catch (Exception e) {
			logger.error("Exception occurred while processing request to get problems by category and subcategory with message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request to get problems by category and subcategory with message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response submitProblem(@FormParam("title") String title, @FormParam("category") String category,
	                              @FormParam("subCategory") String subCategory, @FormParam("problemStatement") String problemStatement,
	                              @FormParam("input") String input, @FormParam("output") String output,
	                              @FormParam("constraints") String constraints, @FormParam("username") String username) {
		logger.info("Received request to add a problem.");
		HashMap<String, Object> map = new HashMap<>();
		try {
			if (input != null && !input.isEmpty() && output != null && !output.isEmpty()) {
				logger.info("All form parameters were received. Initializing hibernate session.");
				JudgeProblems problems = new DoInTransaction<JudgeProblems>() {
					@Override
					protected JudgeProblems doWork() {
						JudgeProblems problems = new JudgeProblems();
						problems.setProblemStatement(problemStatement);
						problems.setInput(input);
						problems.setOutput(output);
						problems.setTitle(title);
						problems.setConstraints(constraints);
						problems.setCategory(category);
						problems.setSubcategory(subCategory);
						problems.setAddedBy(username.toUpperCase());
						logger.info("Add request was sent by " + username);
						Timestamp time = new Timestamp(new Date().getTime());
						problems.setAddedOn(time);
						problems.setModifiedOn(time);
						session.saveOrUpdate(problems);
						logger.info("Save or Update completed successfully");
						return problems;
					}
				}.execute();
				logger.info("Sending response now");
				if (problems != null && problems.getId() != null) {
					map.put("success", true);
					map.put("object", problems);
				} else {
					map.put("success", false);
					map.put("error", "COULD NOT ADD THIS PROBLEM");
				}
			} else {
				logger.info("Some of the Form parameters were null. Sending BAD REQUEST response.");
				map.put("success", false);
				map.put("error", "Some of the Form parameters were null. BAD REQUEST ");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while saving problem to the database: Exception Message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while saving problem to the database: Exception Message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}
}
