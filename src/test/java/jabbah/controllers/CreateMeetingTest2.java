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

public class CreateMeetingTest2 {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }	
	@Test
	public void testCloseTimeSlotTrue() throws Exception{
		
		//make sure participant is null
		TimeSlotDAO dao = new TimeSlotDAO();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date idDay = null;
        idDay = sdf.parse("2020-08-17");
        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
		
        int rnd = (int)(Math.random() * 1000000);
        
        TimeSlot s = new TimeSlot("05:45", 0, idDayParsed, "x" + rnd);
        //open time slot
        s.openSlot();
        dao.addTimeSlot(s);
		
		CreateMeeting hand = new CreateMeeting();
		
		int rnd2 = (int)(Math.random() * 1000000);
		CreateMeetingRequest cmr = new CreateMeetingRequest(rnd2 + "P", "05:45", "2020-08-17", "x" + rnd);
		
		String otsRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		hand.handleRequest(input, output, createContext("create"));
		
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateMeetingResponse cmresp = new Gson().fromJson(post.body, CreateMeetingResponse.class);
        System.out.println(cmresp);

        Assert.assertEquals("Successfully booked time slot: 05:45", cmresp.response);        
	}
}
