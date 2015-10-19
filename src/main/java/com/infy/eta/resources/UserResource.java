package com.infy.eta.resources;

import com.infy.eta.databeans.JudgeUsersEntity;
import com.infy.eta.utils.DoInTransaction;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amit Joshi on 10/5/2015.
 */
@Path("/user")
public class UserResource {

	private final Logger logger = Logger.getLogger(UserResource.class);

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("username") String username, @FormParam("password") String password) {
		logger.info("Received login info for user " + username);
		HashMap<String, Object> map = new HashMap<>();
		try {
			//convert password into a MD5 hash
			String hashedPassword = createHashedPassword(password);
			List<JudgeUsersEntity> list = new DoInTransaction<List<JudgeUsersEntity>>() {
				@Override
				protected List<JudgeUsersEntity> doWork() {
					Criteria criteria = session.createCriteria(JudgeUsersEntity.class);
					criteria.add(Restrictions.eq("username", username.toUpperCase()));
					criteria.add(Restrictions.eq("password", hashedPassword));
					return criteria.list();
				}
			}.execute();
			if (list.size() == 1 && list.get(0).getUsername().equalsIgnoreCase(username)) {
				logger.info("login successful" + list.get(0).getUsername());
				map.put("success", true);
				map.put("object", list);
			} else {
				logger.info("login failed");
				map.put("success", false);
				map.put("error", "LOGIN FAILED, BAD USERNAME OR PASSWORD");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while checking login credentials. Exception message is " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while checking login credentials. Exception message is " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	private String createHashedPassword(String password) {
		logger.info("About to create MD5 hash of given password.");
		String passwordHash;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Add password bytes to digest
			md.update(password.getBytes());
			//Get the hash's bytes
			byte[] bytes = md.digest();
			//This bytes[] has bytes in decimal format;
			//Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (byte aByte : bytes) {
				sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			passwordHash = sb.toString();
			logger.info("Hash created.");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Exception occured while creating hash for password. Exception Message " + e.getMessage(), e);
			throw new RuntimeException("Could not create hash of the password.");
		}
		return passwordHash;
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

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(@FormParam("username") String username, @FormParam("password") String password
			, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName) {
		logger.info("Received request to add the user with username " + username);
		HashMap<String, Object> map = new HashMap<>();
		try {
			//convert password into a MD5 hash
			String hashedPassword = createHashedPassword(password);
			JudgeUsersEntity usersEntity = new DoInTransaction<JudgeUsersEntity>() {
				@Override
				protected JudgeUsersEntity doWork() {
					JudgeUsersEntity usersEntity = new JudgeUsersEntity();
					usersEntity.setUsername(username.toUpperCase());
					usersEntity.setPassword(hashedPassword);
					usersEntity.setFirstName(firstName);
					usersEntity.setLastName(lastName);
					usersEntity.setInZ(new Timestamp(new Date().getTime()));
					DateTime outZ = new DateTime(2099, 1, 1, 12, 00);
					usersEntity.setOutZ(new Timestamp(outZ.getMillis()));
					session.save(usersEntity);
					return usersEntity;
				}
			}.execute();
			if (usersEntity != null && usersEntity.getId() != null) {
				map.put("success", true);
				map.put("object", usersEntity);
			} else {
				map.put("success", false);
				map.put("error", "COULD NOT SAVE USER DETAILS. PLEASE TRY AGAIN");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while trying to add user. Exception message is " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while trying to add user. Exception message is " + e.getCause().getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		logger.info("Received request to get all users");
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeUsersEntity> list = new DoInTransaction<List<JudgeUsersEntity>>() {
				@Override
				protected List<JudgeUsersEntity> doWork() {
					return session.createCriteria(JudgeUsersEntity.class).list();
				}
			}.execute();
			if (!list.isEmpty()) {
				map.put("success", true);
				map.put("object", list);
			} else {
				map.put("success", false);
				map.put("error", "NO USERS FOUND");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while fetching all users. Exception message is " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while fetching all registered users. Exception message is " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

	@GET
	@Path("/get/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserData(@PathParam("username") String username) {
		logger.info("Received request to get user data for " + username);
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<JudgeUsersEntity> list = new DoInTransaction<List<JudgeUsersEntity>>() {
				@Override
				protected List<JudgeUsersEntity> doWork() {
					return session.createCriteria(JudgeUsersEntity.class).add(Restrictions.eq("username", username.toUpperCase())).list();
				}
			}.execute();
			if (!list.isEmpty()) {
				map.put("success", true);
				map.put("object", list);
			} else {
				map.put("success", false);
				map.put("error", "NO USERS FOUND WITH USERNAME " + username.toUpperCase());
			}
		} catch (Exception e) {
			logger.error("Exception occurred while fetching user with username " + username.toUpperCase() + ". Exception message is " + e.getMessage(), e);
			map.put("success", false);
			map.put("error", "Exception occurred while fetching user with username " + username.toUpperCase() + ". Exception message is " + e.getMessage());
		}
		return getResponse(new JSONObject(map));
	}

}
