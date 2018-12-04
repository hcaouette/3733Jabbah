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
	public class GetSchedule implements RequestStreamHandler {

	    public LambdaLogger logger = null;

	    /** Load from RDS, if it exists
	     *
	     * @throws Exception
	     */
	    boolean getSchedule(String AccessCode) throws Exception {
	        if (logger != null) { logger.log("in getUser"); }
	        SchedulesDAO dao = new SchedulesDAO();
	        //checks if the accessCode is a parctipant or organizer AccessCode
	        if(AccessCode.endsWith("O")) {
	        // check if present
	        Schedule exist = dao.getSchedule(AccessCode);
	        if (exist == null) {
	            return false;
	        } else {
	            
	            return true;
	        }
	        }
	        else 
	        {
	        Schedule exist = dao.getScheduleParticipant(AccessCode);
	        if (exist == null) {
	            return false;
	        } else {
	            
	            return true;
	        }
	        }
	    }

	    @Override
	    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
	        logger = context.getLogger();
	        logger.log("Loading Java Lambda handler to get Schedule");
	        UserDAO dao = new UserDAO();
	        JSONObject headerJson = new JSONObject();
	        headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
	        headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	        headerJson.put("Access-Control-Allow-Origin", "*");

	        JSONObject responseJson = new JSONObject();
	        responseJson.put("headers", headerJson);

	        GetScheduleResponse response = null;

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
	                response = new GetScheduleResponse("name", 200);  // OPTIONS needs a 200 response
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
	            response = new GetScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	            responseJson.put("body", new Gson().toJson(response));
	            processed = true;
	            body = null;
	        }

	        if (!processed) {
	            GetScheduleRequest req = new Gson().fromJson(body, GetScheduleRequest.class);
	            logger.log(req.toString());
	            GetScheduleResponse resp;

	            try {
	                if (getSchedule(req.accessCode)) {
	                    resp = new GetScheduleResponse("Successfully found Schedule with accessCode:" +req.accessCode);
	                } else {
	                    resp = new GetScheduleResponse("Unable to find Schedule with accessCode: " + req.accessCode, 422);
	                }
	            } catch (Exception e) {
	                resp = new GetScheduleResponse("Unable to find Schedule with accessCode: " + req.accessCode + "(" + e.getMessage() + ")", 403);
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
