package jabbah.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import jabbah.db.DaysInScheduleDAO;
import jabbah.db.SchedulesDAO;
import jabbah.db.TimeSlotDAO;
import jabbah.model.DaysInSchedule;
import jabbah.model.Schedule;
import jabbah.model.TimeSlot;

public class ExtendSchedule implements RequestStreamHandler {

        public LambdaLogger logger = null;
        
        @Override
        public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
            logger = context.getLogger();
            logger.log("Loading Java Lambda handler to create constant");

            JSONObject headerJson = new JSONObject();
            headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
            headerJson.put("Access-Control-Allow-Methods", "DELETE,GET,POST,OPTIONS");
            headerJson.put("Access-Control-Allow-Origin",  "*");

            JSONObject responseJson = new JSONObject();
            responseJson.put("headers", headerJson);

            DeleteScheduleResponse response = null;

            // extract body from incoming HTTP DELETE request. If any error, then return 422 error
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
                response = new DeleteScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
                responseJson.put("body", new Gson().toJson(response));
                processed = true;
                body = null;
            }

            if (!processed) {
                ExtendScheduleRequest req = new Gson().fromJson(body, ExtendScheduleRequest.class);
                logger.log(req.toString());

                SchedulesDAO dao = new SchedulesDAO();

                // See how awkward it is to call delete with an object, when you only
                // have one part of its information?
                ExtendScheduleResponse resp;
                try {
                    if (dao.extendSchedule(req.date,req.scheduleID)) {
                        resp = new ExtendScheduleResponse("Succesfully extended schedule to date: " + req.date);
                    } else {
                        resp = new ExtendScheduleResponse("Unable to extend schedule to date: " + req.date, 422);
                    }
                } catch (Exception e) {
                    resp = new ExtendScheduleResponse("Unable to extend schedule to date: " + req.date + "(" + e.getMessage() + ")", 403);
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