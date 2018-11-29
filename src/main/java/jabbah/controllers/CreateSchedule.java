package jabbah.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import jabbah.db.SchedulesDAO;
import jabbah.model.Schedule;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class CreateSchedule implements RequestStreamHandler {

    public LambdaLogger logger = null;

    /** Load from RDS, if it exists
     *
     * @throws Exception
     */
    boolean createSchedule(String accessCode,String startTime,String endTime,int timeSlotLength,Date startDate,Date endDate) throws Exception {
        if (logger != null) { logger.log("in createSchedule"); }
        SchedulesDAO dao = new SchedulesDAO();

        // check if present
        Schedule exist = dao.getSchedule(accessCode);
        Schedule createdSchedule = new Schedule (accessCode, startTime, endTime, timeSlotLength, startDate, endDate);
        if (exist == null) {
            return dao.addSchedule(createdSchedule);
        } else {
            //wont update schedule if one exists with the same info,
           // return dao.updateSchedule(createdSchedule);
        	return createSchedule(accessCode, startTime, endTime, timeSlotLength, startDate, endDate);
        }
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler to create Schedule");

        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
        headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        headerJson.put("Access-Control-Allow-Origin",  "*");

        JSONObject responseJson = new JSONObject();
        responseJson.put("headers", headerJson);

        CreateScheduleResponse response = null;

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
                response = new CreateScheduleResponse("name", 200);  // OPTIONS needs a 200 response
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
            response = new CreateScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
            responseJson.put("body", new Gson().toJson(response));
            processed = true;
            body = null;
        }

        if (!processed) {
            CreateScheduleRequest req = new Gson().fromJson(body, CreateScheduleRequest.class);
            logger.log(req.toString());

            CreateScheduleResponse resp;
            try {
                if (createSchedule(req.getAccessCode(),req.getStartTime(), body, 0, null, null)) {
                    resp = new CreateScheduleResponse("Successfully defined Schedule:" + req.getAccessCode());
                } else {
                    resp = new CreateScheduleResponse("Unable to create Schedule: " + req.getAccessCode(), 422);
                }
            } catch (Exception e) {
                resp = new CreateScheduleResponse("Unable to create Schedule: " + req.getAccessCode() + "(" + e.getMessage() + ")", 403);
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