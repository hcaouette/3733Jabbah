package jabbah.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import jabbah.db.TimeSlotDAO;
import jabbah.model.TimeSlot;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */

public class CreateMeetingTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreateMeetingTrue() throws Exception{
		
		//create a schedule
        CreateSchedule handler = new CreateSchedule();

        int rnd = (int) (Math.random() * 10000000);
        int rndTwo = (int) (Math.random() * 10000000);
        String rndS = Integer.toString(rnd);
        String rndSTwo = Integer.toString(rndTwo);
        CreateScheduleRequest ar = new CreateScheduleRequest("w" + rndS, "12:00", "12:50", 10, "2000-08-15", "2000-08-20", "hi", 1, rndSTwo);

        String ccRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));

        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        System.out.println(resp);
		
        //try to book slot - should succeed
		CreateMeeting hand = new CreateMeeting();

		CreateMeetingRequest cmr = new CreateMeetingRequest(rndSTwo, "12:10", "2000-08-17", "jeff", "12345");

		String otsRequest = new Gson().toJson(cmr);
		jsonRequest = new Gson().toJson(new PostRequest(otsRequest));

		input = new ByteArrayInputStream(jsonRequest.getBytes());
		output = new ByteArrayOutputStream();

		hand.handleRequest(input, output, createContext("create"));

        post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateMeetingResponse cmresp = new Gson().fromJson(post.body, CreateMeetingResponse.class);
        System.out.println(cmresp);

        Assert.assertEquals("Successfully booked time slot: 12:10", cmresp.response);
	}
}
