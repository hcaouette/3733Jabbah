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

public class CloseTimeSlotsonDayTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	@Test
	public void testCloseTimeSlot() throws Exception{
		
		//create time slots with different times on the same day
		TimeSlotDAO dao = new TimeSlotDAO();
		
		int rnd = (int)(Math.random() * 1000000);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date idDay = null;
		
		int i = 0;
		while (i < 5) {
	        idDay = sdf.parse("2018-12-20");
	        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
	        TimeSlot s = new TimeSlot("0" + i + ":11", 20, idDayParsed, "w" + rnd);
	        s.openSlot();
	        dao.addTimeSlot(s);
	        i++;
	    }
		
		// create a couple time slot with a different day to show that it won't 
		// close time slots on these days
		i = 0;
		while (i < 3) {
	        idDay = sdf.parse("2018-12-08");
	        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
	        TimeSlot s = new TimeSlot("0" + i + ":11", 20, idDayParsed, "w" + rnd);
	        s.openSlot();
	        dao.addTimeSlot(s);
	        i++;
	    }
		
        //close created all time slot on given day
		CloseTimeSlotsonDay handler = new CloseTimeSlotsonDay();
		
		CloseTimeSlotsonDayRequest otsr = new CloseTimeSlotsonDayRequest("2018-12-19", "w" + rnd);
		
		String otsRequest = new Gson().toJson(otsr);
		String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		handler.handleRequest(input, output, createContext("create"));
		
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CloseTimeSlotResponse resp = new Gson().fromJson(post.body, CloseTimeSlotResponse.class);
        System.out.println(resp);

        Assert.assertEquals("Successfully closed all time slots on: 2018-12-19", resp.response);
	}
}
