package jabbah.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
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
	public void testCloseTimeSlotFalse() throws IOException{
		
		// close slot to test to make sure participant cannot book a closed time slot
		CloseTimeSlot handler = new CloseTimeSlot();
		
		CloseTimeSlotRequest otsr = new CloseTimeSlotRequest("12:12", "1888-08-17", "9066827");
		
		String otsRequest = new Gson().toJson(otsr);
		String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		handler.handleRequest(input, output, createContext("create"));
		
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CloseTimeSlotResponse resp = new Gson().fromJson(post.body, CloseTimeSlotResponse.class);
        System.out.println(resp);

        Assert.assertEquals("Successfully closed time slot: 12:12", resp.response);
		
        //try to book slot - should fail
		CreateMeeting hand = new CreateMeeting();
		
		int rnd = (int)(Math.random() * 1000000);
		CreateMeetingRequest cmr = new CreateMeetingRequest(rnd + "P", "12:12", "1888-08-17", "9066827");
		
		otsRequest = new Gson().toJson(cmr);
		jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
		
		input = new ByteArrayInputStream(jsonRequest.getBytes());
		output = new ByteArrayOutputStream();
		
		hand.handleRequest(input, output, createContext("create"));
		
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateMeetingResponse cmresp = new Gson().fromJson(post.body, CreateMeetingResponse.class);
        System.out.println(cmresp);

        Assert.assertEquals("Unable to book time slot: 12:12", cmresp.response);
	}
}
