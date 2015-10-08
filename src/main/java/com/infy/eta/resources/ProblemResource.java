package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeCategoriesEntity;
import com.infy.eta.databeans.JudgeProblemsEntity;
import com.infy.eta.databeans.JudgeSubcategoriesEntity;
import com.infy.eta.utils.DoInTransaction;
import com.infy.eta.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
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
import java.util.List;

/**
 * Created by Amit Joshi on 10/4/2015.
 */
@Path("/problem")
public class ProblemResource {

	private static Logger logger = Logger.getLogger(ProblemResource.class);

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response submitProblem(@FormParam("title") String title, @FormParam("category") String category, @FormParam("subCategory") String subCategory, @FormParam("problemStatement") String problemStatement, @FormParam("input") String input, @FormParam("output") String output, @FormParam("constraints") String constraints) {
		logger.info("Received submit problem request.");
		Response response;
		try {
			if (input != null && !input.isEmpty() && output != null && !output.isEmpty()) {
				logger.info("All form parameters were received. Initializing hibernate session.");
				JudgeProblemsEntity problemsEntity = new DoInTransaction<JudgeProblemsEntity>() {
					@Override
					protected JudgeProblemsEntity doWork() {
						JudgeProblemsEntity problemsEntity = new JudgeProblemsEntity();
						problemsEntity.setProblemStatement(problemStatement);
						problemsEntity.setInput(input);
						problemsEntity.setOutput(output);
						problemsEntity.setTitle(title);
						problemsEntity.setConstraints(constraints);
						problemsEntity.setCategory(category);
						problemsEntity.setSubcategory(subCategory);
						session.saveOrUpdate(problemsEntity);
						logger.info("Save or Update completed successfully");
						return problemsEntity;
					}
				}.execute();
				logger.info("Sending response now");
				response = Response.ok(problemsEntity.getId())
				                   .header("Access-Control-Allow-Headers", "Content-Type")
				                   .header("Access-Control-Allow-Methods", "POST, OPTIONS")
				                   .header("Access-Control-Allow-Origin", "*")
				                   .build();
			} else {
				logger.info("Some of the Form parameters were null. Sending BAD REQUEST response.");
				response = Response.ok(new JSONObject("{success: false}").toString())
				                   .header("Access-Control-Allow-Headers", "Content-Type")
				                   .header("Access-Control-Allow-Methods", "POST, OPTIONS")
				                   .header("Access-Control-Allow-Origin", "*")
				                   .build();
			}
		} catch (Exception e) {
			logger.error("Exception occurred while saving problem to the database: Exception Message " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "POST, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		}
		return response;
	}

	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProblems() {
		logger.info("Received response");
		Response response;
		try {
			List<JudgeProblemsEntity> list = new DoInTransaction<List<JudgeProblemsEntity>>() {
				@Override
				protected List<JudgeProblemsEntity> doWork() {
					Criteria criteria = session.createCriteria(JudgeProblemsEntity.class);
					return criteria.list();
				}
			}.execute();
			System.out.println("##########" + list);
			response = Response.ok(list)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		} catch (Exception e) {
			logger.error("Exception occurred while getting all problems. Exception message: " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		}
		return response;
	}

	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProblem(@PathParam("id") String id) {
		logger.info("received request to get the problem data for id "+ id);
		Response response;
		try {
			Integer problemId = Integer.parseInt(id.substring(1));
			List<JudgeProblemsEntity> problemList = new DoInTransaction<List<JudgeProblemsEntity>>() {
				@Override
				protected List<JudgeProblemsEntity> doWork() {
					Criteria criteria = session.createCriteria(JudgeProblemsEntity.class);
					criteria.add(Restrictions.eq("id", problemId));
					List<JudgeProblemsEntity> list = criteria.list();
					logger.info("Problem found "+ list.get(0).getTitle());
					return list;
				}
			}.execute();
			response = Response.ok(problemList.get(0))
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		} catch (Exception e) {
			logger.error("Exception occured while processing request. Exception Message: " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		}
		return response;
	}

}
