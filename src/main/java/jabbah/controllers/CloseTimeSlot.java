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

import jabbah.db.TimeSlotDAO;
import jabbah.model.TimeSlot;

public class CloseTimeSlot implements RequestStreamHandler {
	
	public LambdaLogger logger = null;
	
	boolean createTimeSlot(String sT, int dur, String day, String id) throws Exception{
		if(logger != null) { logger.log("in createTimeSlot");}
		TimeSlotDAO dao = new TimeSlotDAO();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date idDay = null;
        idDay = sdf.parse(day);
        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
        
        TimeSlot slot = new TimeSlot (sT, dur, idDayParsed, id);
        slot.closeSlot();
        
        return dao.updateTimeSlot(slot);
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException{
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestStreamHandler");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CloseTimeSlotResponse response = null;
		
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
				responseJson.put("body", "{}");  // nothing needs to be sent back.
		        processed = true;
		        body = null;
			} else {
				body = (String)event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new CloseTimeSlotResponse("Bad Request: " + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			CloseTimeSlotRequest req = new Gson().fromJson(body, CloseTimeSlotRequest.class);
			logger.log(req.toString());
			
			CloseTimeSlotResponse resp;
			
			try {
				if(createTimeSlot(req.startTime, 0, req.idDay, req.scheduleID)) {
					resp = new CloseTimeSlotResponse("Successfully closed time slot: " + req.startTime);
				}
				else {
					resp = new CloseTimeSlotResponse("Unable to close time slot: " + req.startTime, 422);
				}
			}catch (Exception e) {
				resp = new CloseTimeSlotResponse("Unable to close time slot: " + req.startTime + "(" + e.getMessage() + ")", 403);   
			}
			//compute proper response
			responseJson.put("body", new Gson().toJson(resp));
			
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
}