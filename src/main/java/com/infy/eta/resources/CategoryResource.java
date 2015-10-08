package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeCategoriesEntity;
import com.infy.eta.utils.DoInTransaction;
import com.infy.eta.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Amit Joshi on 10/4/2015.
 */
@Path("/category")
public class CategoryResource {

	private Logger logger = Logger.getLogger(CategoryResource.class);

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCategory(@FormParam("category") String category, @FormParam("description") String description) {
		logger.info("Received add category request.");
		Response response;
		try {
			if (category != null && !category.isEmpty()) {
				logger.info("All parameters are valid. Saving category " + category);
				JudgeCategoriesEntity entity = new DoInTransaction<JudgeCategoriesEntity>() {
					@Override
					protected JudgeCategoriesEntity doWork() {
						JudgeCategoriesEntity entity = new JudgeCategoriesEntity();
						entity.setDescription(description);
						entity.setCategory(category);
						entity.setId(null);
						logger.info("Category to be save " + entity.getCategory());
						session.save(entity);
						return entity;
					}
				}.execute();
				logger.info("Save complete with id " + entity.getId());
				response = Response.ok(entity.getId())
				                   .header("Access-Control-Allow-Headers", "Content-Type")
				                   .header("Access-Control-Allow-Methods", "POST, OPTIONS")
				                   .header("Access-Control-Allow-Origin", "*")
				                   .build();
			} else {
				logger.info("Parameters were not received or they were empty. BAD REQUEST");
				response = Response.ok(new JSONObject(new JudgeCategoriesEntity()).toString())
				                   .header("Access-Control-Allow-Headers", "Content-Type")
				                   .header("Access-Control-Allow-Methods", "POST, OPTIONS")
				                   .header("Access-Control-Allow-Origin", "*")
				                   .build();
			}
		} catch (Exception e) {
			logger.error("Exception occured while processing the request. Exception Message " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "POST, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		}
		return response;
	}

	@GET
	@Path("/get")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategory() {
		logger.info("Received get categories request.");
		Response response;
		try {
			List<JudgeCategoriesEntity> list = new DoInTransaction<List<JudgeCategoriesEntity>>() {
				@Override
				protected List<JudgeCategoriesEntity> doWork() {
					Criteria criteria = session.createCriteria(JudgeCategoriesEntity.class);
					return criteria.list();
				}
			}.execute();
			logger.info("Fetched " + list.size() + " categories from the database.");
			response = Response.ok(list)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		} catch (Exception e) {
			logger.error("Exception occured while processing request. Exception Message " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		}
		return response;
	}

	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategory(@FormParam("id") String id) {
		Response response;
		try {
			List<JudgeCategoriesEntity> list = new DoInTransaction<List<JudgeCategoriesEntity>>() {
				@Override
				protected List<JudgeCategoriesEntity> doWork() {
					Integer  categoryId = Integer.parseInt(id);
					Criteria criteria   = session.createCriteria(JudgeCategoriesEntity.class);
					criteria.add(Restrictions.eq("id", categoryId));
					return criteria.list();
				}
			}.execute();
			response = Response.ok(list)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		} catch (Exception e) {
			logger.error("Exception occured while processing request. Exception Message " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                   .header("Access-Control-Allow-Headers", "Content-Type")
			                   .header("Access-Control-Allow-Methods", "GET, OPTIONS")
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		}
		return response;
	}

}
