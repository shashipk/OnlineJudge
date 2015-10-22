package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeContests;
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
 * Created by Amit Joshi on 10/22/2015.
 */
@Path("/contest")
public class ContestResource {
	
	private static final Logger logger = Logger.getLogger(ContestResource.class);
	
	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContest(@PathParam("id") String id) {
		logger.info("Received request to get the contest data for id " + id);
		HashMap<String, Object> map = new HashMap<>();
		try {
			Integer contestId = Integer.parseInt(id.substring(1));
			List<JudgeContests> contestList = new DoInTransaction<List<JudgeContests>>() {
				@Override
				protected List<JudgeContests> doWork() {
					Criteria criteria = session.createCriteria(JudgeContests.class);
					criteria.add(Restrictions.eq("contestId", contestId));
					List<JudgeContests> list = criteria.list();
					logger.info("Contest found " + list.get(0).getContestDescription());
					return list;
				}
			}.execute();
			if (!contestList.isEmpty()) {
				map.put("success", true);
				map.put("object", contestList);
			} else {
				map.put("success", false);
				map.put("error", "Could not get Contest data with id " + id);
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


	/*TO DO: add addedBy Column to the Contest Table to be able to get contests by username*/
	
	/*@GET
	@Path("/getByUsername/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContestsByUsername(@PathParam("username") String username) {
		logger.info("received request to get the contests submitted by user " + username);
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeContests> contestList = new DoInTransaction<List<JudgeContests>>() {
				@Override
				protected List<JudgeContests> doWork() {
					Criteria criteria = session.createCriteria(JudgeContests.class);
					criteria.add(Restrictions.eq("addedBy", username));
					return criteria.list();
				}
			}.execute();
			if (!contestList.isEmpty()) {
				logger.info("Contest found " + contestList.get(0).getTitle());
				map.put("success", true);
				map.put("object", contestList);
			} else {
				map.put("success", false);
				map.put("error", "Could not find any contests for username" + username);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request to get contests for username " + username.toUpperCase() + ". Exception  Message: " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request to get contests for username " + username.toUpperCase() + ". Exception Message: " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}*/
	
	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllContests() {
		logger.info("Received request to get all contests");
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeContests> list = new DoInTransaction<List<JudgeContests>>() {
				@Override
				protected List<JudgeContests> doWork() {
					Criteria criteria = session.createCriteria(JudgeContests.class);
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
			logger.error("Exception occurred while getting all contests. Exception message: " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while getting all contests. Exception message: " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	/*TO DO : add category and subcategory column to the contests table*/
	@POST
	@Path("/getContests")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContests(@FormParam("category") String category, @FormParam("subCategory") String subCategory) {
		logger.info("Received request to get all contests by category and subcategory");
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeContests> list = new DoInTransaction<List<JudgeContests>>() {
				@Override
				protected List<JudgeContests> doWork() {
					Criteria criteria = session.createCriteria(JudgeContests.class);
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
			logger.error("Exception occurred while processing request to get contests by category and subcategory with message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request to get contests by category and subcategory with message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response submitContest(@FormParam("contestName") String contestName, @FormParam("contestDuration") String contestDuration,
	                              @FormParam("contestDescription") String contestDescription, @FormParam("contestOrganizer") String contestOrganizer,
	                              @FormParam("contestPrize") String contestPrize, @FormParam("username") String username) {
		logger.info("Received request to add a contest.");
		HashMap<String, Object> map = new HashMap<>();
		try {
			if (contestName != null && !contestDescription.isEmpty() && !contestDuration.isEmpty() && !contestDescription.isEmpty() && !contestOrganizer.isEmpty() && contestPrize != null && !username.isEmpty()) {
				logger.info("All form parameters were received. Initializing hibernate session.");
				JudgeContests contests = new DoInTransaction<JudgeContests>() {
					@Override
					protected JudgeContests doWork() {
						JudgeContests contests = new JudgeContests();
						contests.setContestName(contestName);
						contests.setContestDescription(contestDescription);
						contests.setContestDuration(Integer.valueOf(contestDuration));
						contests.setContestOrganizer(contestOrganizer);
						contests.setContestPrize(contestPrize);

						logger.info("Add request was sent by " + username);
						Timestamp time = new Timestamp(new Date().getTime());
						contests.setInZ(time);
						session.saveOrUpdate(contests);
						logger.info("Save or Update completed successfully");
						return contests;
					}
				}.execute();
				logger.info("Sending response now");
				if (contests != null && contests.getContestId() != null) {
					map.put("success", true);
					map.put("object", contests);
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
			logger.error("Exception occurred while saving contest to the database: Exception Message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while saving contest to the database: Exception Message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}
}

