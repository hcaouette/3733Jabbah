package jabbah.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

public class OpenTimeSlotTest {
	
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	@Test
	public void testOpenTimeSlot() throws Exception{
		
		//create a time slot
		TimeSlotDAO dao = new TimeSlotDAO();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date idDay = null;
        idDay = sdf.parse("2018-12-01");
        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());
		
        int rnd = (int)(Math.random() * 1000000);
        
        TimeSlot s = new TimeSlot("12:12", 20, idDayParsed, "x" + rnd);
        //close time slot
        s.closeSlot();
        dao.addTimeSlot(s);
		
        //open created time slot
		OpenTimeSlot handler = new OpenTimeSlot();

		OpenTimeSlotRequest otsr = new OpenTimeSlotRequest("12:12", "2018-12-01", "x" + rnd);
		
		String otsRequest = new Gson().toJson(otsr);
		String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
		
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		handler.handleRequest(input, output, createContext("create"));
		
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        OpenTimeSlotResponse resp = new Gson().fromJson(post.body, OpenTimeSlotResponse.class);
        System.out.println(resp);

        Assert.assertEquals("Successfully opened time slot: 12:12", resp.response);
	}
}
