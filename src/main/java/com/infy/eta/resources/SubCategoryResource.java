package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeSubcategories;
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
@Path("/subcategory")
public class SubCategoryResource {

	private final Logger logger = Logger.getLogger(SubCategoryResource.class);

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSubCategory(@FormParam("subcategory") String subCategory, @FormParam("description") String description) {
		logger.info("Received request to add a subcategory.");
		HashMap<String, Object> map = new HashMap<>();
		try {
			if (subCategory != null && !subCategory.isEmpty()) {
				logger.info("All parameters are valid. Saving sub category.");
				JudgeSubcategories entity = new DoInTransaction<JudgeSubcategories>() {
					@Override
					protected JudgeSubcategories doWork() {
						JudgeSubcategories entity = new JudgeSubcategories();
						entity.setDescription(description);
						entity.setSubcategory(subCategory);
						session.saveOrUpdate(entity);
						return entity;
					}
				}.execute();
				if (entity != null && entity.getId() != 0) {
					logger.info("Save complete with id " + entity.getId());
					map.put("success", true);
					map.put("object", entity);
				} else {
					logger.error("Save Failed");
					map.put("success", false);
					map.put("error", "SAVE FAILED. Please try again");
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
	public Response getSubCategory() {
		logger.info("Received get sub categories request.");
		HashMap<String, Object> map = new HashMap<>();
		try {
			logger.info("Initializing database access now");
			List<JudgeSubcategories> list = new DoInTransaction<List<JudgeSubcategories>>() {
				@Override
				protected List<JudgeSubcategories> doWork() {
					Criteria criteria = session.createCriteria(JudgeSubcategories.class);
					return criteria.list();
				}
			}.execute();
			logger.info("Fetched " + list.size() + " categories from the database.");
			if (!list.isEmpty()) {
				map.put("success", true);
				map.put("object", list);
			} else {
				map.put("success", false);
				map.put("error", "NO CATEGORIES FOUND IN THE DATABASE");
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
	public Response getSubCategory(@PathParam("id") String id) {
		logger.info("Received request to get subcategory details for id " + id);
		HashMap<String, Object> map = new HashMap<>();
		try {
			Integer subCategoryId = Integer.parseInt(id);
			List<JudgeSubcategories> list = new DoInTransaction<List<JudgeSubcategories>>() {
				@Override
				protected List<JudgeSubcategories> doWork() {
					Criteria criteria = session.createCriteria(JudgeSubcategories.class);
					criteria.add(Restrictions.eq("id", subCategoryId));
					return criteria.list();
				}
			}.execute();
			if (!list.isEmpty()) {
				map.put("success", true);
				map.put("object", list);
			} else {
				map.put("success", false);
				map.put("error", "NO SUB CATEGORIES FOUND FOR ID " + id);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while processing request. Exception Message " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while processing request. Exception Message " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}
}
