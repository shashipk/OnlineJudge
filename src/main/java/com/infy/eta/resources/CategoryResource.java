package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeCategoriesEntity;
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
 * Created by Amit Joshi on 10/4/2015.
 */
@Path("/category")
public class CategoryResource {

	private final Logger logger = Logger.getLogger(CategoryResource.class);

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCategory(@FormParam("category") String category, @FormParam("description") String description) {
		logger.info("Received add category request.");
		HashMap<String, Object> map = new HashMap<>();
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
				if (entity != null && entity.getId() != null) {
					logger.info("Save complete with id " + entity.getId());
					map.put("success", true);
					map.put("object", entity);
				} else {
					map.put("success", false);
					map.put("error", "COULD NOT SAVE CATEGORY");
				}
			} else {
				logger.info("Parameters were not received or they were empty. BAD REQUEST");
				map.put("success", false);
				map.put("error", "Parameters were not received or they were empty. BAD REQUEST");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing the request. Exception Message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing the request. Exception Message " + e.getMessage());
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
	@Path("/get")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategory() {
		logger.info("Received get categories request.");
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeCategoriesEntity> list = new DoInTransaction<List<JudgeCategoriesEntity>>() {
				@Override
				protected List<JudgeCategoriesEntity> doWork() {
					Criteria criteria = session.createCriteria(JudgeCategoriesEntity.class);
					return criteria.list();
				}
			}.execute();
			if (!list.isEmpty()) {
				logger.info("Fetched " + list.size() + " categories from the database.");
				map.put("success", true);
				map.put("object", list);
			} else {
				map.put("success", false);
				map.put("error", "COULD NOT GET CATEGORIES");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request. Exception Message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request. Exception Message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));

	}

	@GET
	@Path("/get/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategory(@PathParam("id") String id) {
		HashMap<String, Object> map = new HashMap<>();
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
			if (!list.isEmpty()) {
				map.put("success", true);
				map.put("object", list);
			} else {
				map.put("success", false);
				map.put("error", "COULD NOT GET CATEGORY WITH ID " + id);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request. Exception Message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request. Exception Message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

}
