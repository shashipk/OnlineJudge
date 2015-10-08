package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeTestCasesEntity;
import com.infy.eta.utils.DoInTransaction;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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
 * Created by Amit Joshi on 10/7/2015.
 */
@Path("/testcase")
public class TestCaseResource {

	private Logger logger = Logger.getLogger(TestCaseResource.class);

	@GET
	@Path("/getAll/{problemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTestCasesForProblem(@PathParam("problemId") String problemId){
		logger.info("Received request to get test cases for problem "+problemId);
		Response response;
		try{
			List<JudgeTestCasesEntity> testCases = new DoInTransaction<List<JudgeTestCasesEntity>>() {
				@Override
				protected List<JudgeTestCasesEntity> doWork() {
					List<JudgeTestCasesEntity> testCases = new DoInTransaction<List<JudgeTestCasesEntity>>() {
						@Override
						protected List<JudgeTestCasesEntity> doWork() {
							Criteria criteria = session.createCriteria(JudgeTestCasesEntity.class);
							return criteria.add(Restrictions.eq("problemId", Integer.parseInt(problemId))).list();
						}
					}.execute();
					return  testCases;
				}
			}.execute();
			response = Response.ok(testCases)
			        .header("Access-Control-Allow-Headers", "Content-Type")
			        .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			        .header("Access-Control-Allow-Origin", "*")
			        .build();
		} catch (Exception e){
			logger.error("Exception occured while processing request with message "+ e.getMessage(),e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			        .header("Access-Control-Allow-Headers", "Content-Type")
			        .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			        .header("Access-Control-Allow-Origin", "*")
			        .build();
		}
		return  response;
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addTestCaseForProblem(@FormParam("problemId") String problemId, @FormParam("description")String description,
	                                      @FormParam("input") String input, @FormParam("output") String output, @FormParam("points") String points){
		logger.info("Received request to add test case for problem "+ problemId);
		Response response;
		try{
			JudgeTestCasesEntity testCase = new DoInTransaction<JudgeTestCasesEntity>() {
				@Override
				protected JudgeTestCasesEntity doWork() {
					JudgeTestCasesEntity testCase = new JudgeTestCasesEntity();
					testCase.setDescription(description);
					testCase.setInput(input);
					testCase.setOutput(output);
					testCase.setProblemId(Integer.parseInt(problemId));
					testCase.setPoints(Integer.parseInt(points));
					session.save(testCase);
					return testCase;
				}
			}.execute();
			response = Response.ok(testCase)
			        .header("Access-Control-Allow-Headers", "Content-Type")
			        .header("Access-Control-Allow-Methods", "POST, OPTIONS")
			        .header("Access-Control-Allow-Origin", "*")
			        .build();
		} catch (Exception e){
			logger.error("Exception occured while processing request with message "+ e.getMessage(),e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			        .header("Access-Control-Allow-Headers", "Content-Type")
			        .header("Access-Control-Allow-Methods", "POST, OPTIONS")
			        .header("Access-Control-Allow-Origin", "*")
			        .build();
		}
		return  response;
	}
}
