package jabbah.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

public class ShowOpenTimeslots implements RequestStreamHandler {

    public LambdaLogger logger = null;

    /** Load from RDS, if it exists
     * @param monthDay
     * @param month
     * @param accessCode
     * @param year
     * @param weekday
     * @param startingTime
     *
     * @throws Exception
     */

    List<TimeSlot> getTimeSlots(String accessCode, String month, String monthDay, String startingTime, String weekday, String year) throws Exception {
        if (logger != null) { logger.log("in getTimeSlots"); }
        TimeSlotDAO dao = new TimeSlotDAO();
        return dao.getSpecificTimeSlot(accessCode, month, monthDay, startingTime, weekday, year);
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler to create constant");

        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
        headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        headerJson.put("Access-Control-Allow-Origin",  "*");

        JSONObject responseJson = new JSONObject();
        responseJson.put("headers", headerJson);

        ShowOpenTimeslotsResponse response = null;

        // extract body from incoming HTTP POST request. If any error, then return 422 error
        String body;
        boolean processed = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            JSONParser parser = new JSONParser();
            JSONObject event = (JSONObject) parser.parse(reader);
            logger.log("event:" + event.toJSONString());

            body = (String)event.get("body");
            if (body == null) {
                body = event.toJSONString();  // this is only here to make testing easier
            }
        } catch (ParseException pe) {
            logger.log(pe.toString());
            response = new ShowOpenTimeslotsResponse(422);  // unable to process input
            responseJson.put("body", new Gson().toJson(response));
            processed = true;
            body = null;
        }

        if (!processed) {
            ShowOpenTimeslotsRequest req = new Gson().fromJson(body, ShowOpenTimeslotsRequest.class);
            logger.log(req.toString());

            ShowOpenTimeslotsResponse resp;
            try {
                List<TimeSlot> list = getTimeSlots(req.accessCode, req.month, req.monthDay,
                        req.startingTime, req.weekday, req.year);
                resp = new ShowOpenTimeslotsResponse(list, 200);
            } catch (Exception e) {
                resp = new ShowOpenTimeslotsResponse(403);
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