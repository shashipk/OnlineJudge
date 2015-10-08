package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeSubcategoriesEntity;
import com.infy.eta.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

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
@Path("/subcategory")
public class SubCategoryResource {

	private Session session = HibernateUtil.getSessionFactory().openSession();
	private Logger  logger  = Logger.getLogger(SubCategoryResource.class);


	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCategory(@FormParam("subcategory") String subCategory, @FormParam("description") String description) {
		logger.info("Received add category request.");
		Response response;
		try {
			if (subCategory != null && !subCategory.isEmpty()) {
				logger.info("All parameters are valid. Saving category.");
				Transaction transaction = session.beginTransaction();
				transaction.begin();
				JudgeSubcategoriesEntity entity = new JudgeSubcategoriesEntity();
				entity.setDescription(description);
				entity.setSubcategory(subCategory);
				session.saveOrUpdate(entity);
				transaction.commit();
				logger.info("Save complete with id " + entity.getId());
				response = Response.ok(entity.getId())
				                   .header("Access-Control-Allow-Origin", "*")
				                   .build();
			} else {
				logger.info("Parameters were not received or they were empty. BAD REQUEST");
				response = Response.status(Response.Status.BAD_REQUEST)
				                   .header("Access-Control-Allow-Origin", "*")
				                   .build();
			}
		} catch (Exception e) {
			logger.error("Exception occured while processing the request. Exception Message " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
			logger.info("Initializing database access now");
			Transaction transaction = session.beginTransaction();
			transaction.begin();
			Criteria criteria = session.createCriteria(JudgeSubcategoriesEntity.class);
			List<JudgeSubcategoriesEntity> list = criteria.list();
			logger.info("Fetched "+list.size()+" categories from the database.");
			transaction.commit();
			response = Response.ok(list)
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		} catch (Exception e) {
			logger.error("Exception occured while processing request. Exception Message " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
			Integer subCategoryId = Integer.parseInt(id);
			Transaction transaction = session.beginTransaction();
			transaction.begin();
			Criteria criteria = session.createCriteria(JudgeSubcategoriesEntity.class);
			criteria.add(Restrictions.eq("id", subCategoryId));
			List<JudgeSubcategoriesEntity> list = criteria.list();
			transaction.commit();
			response = Response.ok(list)
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		} catch (Exception e) {
			logger.error("Exception occured while processing request. Exception Message " + e.getMessage(), e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                   .header("Access-Control-Allow-Origin", "*")
			                   .build();
		}
		return response;
	}

}
