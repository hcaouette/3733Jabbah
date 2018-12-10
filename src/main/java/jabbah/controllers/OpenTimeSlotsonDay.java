package jabbah.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import jabbah.db.TimeSlotDAO;
import jabbah.model.TimeSlot;

public class OpenTimeSlotsonDay implements RequestStreamHandler{

	public LambdaLogger logger = null;

	boolean openTimeSlotsonDay(String day, String id) throws Exception{
		if(logger != null) { logger.log("in createTimeSlot");}
		TimeSlotDAO dao = new TimeSlotDAO();
		
		List<TimeSlot> allTimeSlots = dao.getAllTimeSlot();
		
		int count = 0;
		
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat dayFormat = new SimpleDateFormat(pattern);
		String date = "";
		
		/*
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date idDay = null;
        idDay = sdf.parse(day);
        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
		*/
		
		Calendar cal = Calendar.getInstance();
		
		for(TimeSlot s: allTimeSlots) {
			//cal.setTime(s.getDate());
			//cal.add(Calendar.DAY_OF_YEAR, 1);
			
			//java.sql.Date next = new java.sql.Date(cal.getTimeInMillis());
			
			//check for all time slot with given schedule id and given starting time
			date = dayFormat.format(s.getDate());
			//if(s.getScheduleID().equals(id) && date.equals(day)) {
			if(s.getScheduleID().equals(id) && date.equals(day)) {
				count++;
				s.openSlot();
				if(!dao.updateTimeSlot(s))
					return false;
			}
		}
		if(count > 0) // ensure there is a least one time slot on the given day in that schedule
			return true;
		else
			return false; // there was no such schedule or no time slots on the given day
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

		OpenTimeSlotResponse response = null;

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
			response = new OpenTimeSlotResponse("Bad REquest: " + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			OpenTimeSlotsonDayRequest req = new Gson().fromJson(body, OpenTimeSlotsonDayRequest.class);
			logger.log(req.toString());

			OpenTimeSlotResponse resp;

			try {
				if(openTimeSlotsonDay(req.day, req.scheduleID)) {
					resp = new OpenTimeSlotResponse("Successfully opened all time slots on: " + req.day);
				}
				else {
					resp = new OpenTimeSlotResponse("Unable to open all time slots on: " + req.day, 422);
				}
			}catch (Exception e) {
				resp = new OpenTimeSlotResponse("Unable to open all time slots on: " + req.day + "(" + e.getMessage() + ")", 403);
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
