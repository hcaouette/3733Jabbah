package jabbah.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.google.gson.Gson;

import jabbah.db.TimeSlotDAO;
import jabbah.model.TimeSlot;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ShowOpenTimeslotsTest {

    private final String CONTENT_TYPE = "image/jpeg";
    private S3Event event;
    Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testShowTwoOpen() throws Exception {
        TimeSlotDAO dao = new TimeSlotDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = "2018-12-07";

        //need to check dates are correct format
        java.util.Date dateUtil = null;
        try { //try to parse our dates to correct format
        dateUtil = sdf.parse(startDateString);
        } catch (ParseException p1) {
            p1.printStackTrace();
        }
        java.sql.Date startDate = new java.sql.Date(dateUtil.getTime());
        int rnd = (int)(Math.random() * 1000000);
        TimeSlot s = new TimeSlot("12:13", 20, startDate, "h" + rnd);
        dao.addTimeSlot(s);
        TimeSlot p = new TimeSlot("12:33", 20, startDate, "h" + rnd);
        dao.addTimeSlot(p);
        ShowOpenTimeslots handler = new ShowOpenTimeslots();
        //ShowOpenTimeslotsRequest q = new ShowOpenTimeslotsRequest(s.getOrgAccessCode(), "7", "12:13", "Thursday", "2018", "12");
        //should be able to get both
        ShowOpenTimeslotsRequest q = new ShowOpenTimeslotsRequest(s.getOrgAccessCode(), "", "", "", "", "");
        //String accessCode, String monthDay, String startingTime,
        //String weekday, String year, String month
        // no input needed
        String otsRequest = new Gson().toJson(q);
        String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
        InputStream input = new ByteArrayInputStream(otsRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("list"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        ShowOpenTimeslotsResponse resp = new Gson().fromJson(post.body, ShowOpenTimeslotsResponse.class);

        //boolean hasPi = false;
        //for (TimeSlot c : resp.list) {
        //    if (c.equals("pi")) { hasPi = true; break; }
        //}
        //Assert.assertTrue(hasPi);
        Assert.assertEquals(200, resp.httpCode);
    }
}
