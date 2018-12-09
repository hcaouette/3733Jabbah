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

public class OpenTimeSlotsatTimeTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	@Test
	public void testOpenTimeSlot() throws Exception{
		
		//create time slots on 5 consecutive days of the same starting time and within same schedule
		TimeSlotDAO dao = new TimeSlotDAO();
		
		int rnd = (int)(Math.random() * 1000000);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date idDay = null;
		
		int i = 0;
		while (i < 5) {
	        idDay = sdf.parse("2018-12-0" + (i+1));
	        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
	        TimeSlot s = new TimeSlot("01:50", 20, idDayParsed, "w" + rnd);
	        s.closeSlot();
	        dao.addTimeSlot(s);
	        i++;
	    }
		
		i = 0;
		while (i < 3) {
	        idDay = sdf.parse("2018-12-0" + (i+1));
	        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
	        TimeSlot s = new TimeSlot("01:" + i + "0", 20, idDayParsed, "w" + rnd);
	        s.closeSlot();
	        dao.addTimeSlot(s);
	        i++;
	    }
		
        //open created all time slot
		OpenTimeSlotsatTime handler = new OpenTimeSlotsatTime();
		
		OpenTimeSlotsatTimeRequest otsr = new OpenTimeSlotsatTimeRequest("01:50", "w" + rnd);
		
		String otsRequest = new Gson().toJson(otsr);
		String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		handler.handleRequest(input, output, createContext("create"));
		
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        OpenTimeSlotResponse resp = new Gson().fromJson(post.body, OpenTimeSlotResponse.class);
        System.out.println(resp);

        Assert.assertEquals("Successfully opened all time slots: 01:50", resp.response);
	}
}
