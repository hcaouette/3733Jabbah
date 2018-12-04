package jabbah.controllers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import jabbah.db.SchedulesDAO;
import jabbah.db.UserDAO;
import jabbah.model.Schedule;
import jabbah.model.User;

	/**
	 * Found gson JAR file from
	 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
	 */
	public class CreateUser implements RequestStreamHandler {

	    public LambdaLogger logger = null;

	    /** Load from RDS, if it exists
	     *
	     * @throws Exception
	     */
	    boolean createUser(String name,String AccessCode,String ScheduleID) throws Exception {
	        if (logger != null) { logger.log("in createUser"); }
	        UserDAO dao = new UserDAO();

	        // check if present
	        User exist = dao.getUser(AccessCode);
	        //have to parse strings to dates
	        User createdUser = new User (name,AccessCode,ScheduleID);
	        if (exist == null) {
	            return dao.addUser(createdUser);
	        } else {
	            //wont update schedule if one exists with the same info,
	            //instead try again with a new random access code
	            //return createSchedule(generateAccessCode(), endTime, timeSlotLength, startDate, endDate);
	           //return dao.updateSchedule(createdSchedule);
	            return false;
	            //return createSchedule(accessCode, startTime, endTime, timeSlotLength, startDate, endDate);
	        }
	    }

	    @Override
	    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
	        logger = context.getLogger();
	        logger.log("Loading Java Lambda handler to create Schedule");

	        JSONObject headerJson = new JSONObject();
	        headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
	        headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	        headerJson.put("Access-Control-Allow-Origin", "*");

	        JSONObject responseJson = new JSONObject();
	        responseJson.put("headers", headerJson);

	        CreateUserResponse response = null;

	        // extract body from incoming HTTP POST request. If any error, then return 422 error
	        String body;
	        boolean processed = false;
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	            JSONParser parser = new JSONParser();
	            JSONObject event = (JSONObject) parser.parse(reader);
	            logger.log("event:" + event.toJSONString());

	            String method = (String) event.get("httpMethod");
	            if (method != null && method.equalsIgnoreCase("OPTIONS")) {
	                logger.log("Options request");
	                response = new CreateUserResponse("name", 200);  // OPTIONS needs a 200 response
	                responseJson.put("body", new Gson().toJson(response));
	                processed = true;
	                body = null;
	            } else {
	                body = (String)event.get("body");
	                if (body == null) {
	                    body = event.toJSONString();  // this is only here to make testing easier
	                }
	            }
	        }
	        catch (ParseException pe) {
	            logger.log(pe.toString());
	            response = new CreateUserResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	            responseJson.put("body", new Gson().toJson(response));
	            processed = true;
	            body = null;
	        }

	        if (!processed) {
	            CreateUserRequest req = new Gson().fromJson(body, CreateUserRequest.class);
	            logger.log(req.toString());
	            CreateUserResponse resp;

	            try {
	                if (createUser(req.name, req.accessCode, req.id)) {
	                    resp = new CreateUserResponse("Successfully defined User:" + req.name);
	                } else {
	                    resp = new CreateUserResponse("Unable to create User: " + req.name, 422);
	                }
	            } catch (Exception e) {
	                resp = new CreateUserResponse("Unable to create User: " + req.name + "(" + e.getMessage() + ")", 403);
	            }

	            // compute proper response
	            responseJson.put("body", new Gson().toJson(resp));
	        }

	        logger.log("end result:" + responseJson.toJSONString());
	        logger.log(responseJson.toJSONString());
	        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
	        writer.write(responseJson.toJSONString());
	        writer.close();
	    }
	}