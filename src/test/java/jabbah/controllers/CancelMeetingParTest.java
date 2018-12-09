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

public class CancelMeetingParTest {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	@Test
	public void testCloseTimeSlotTrue() throws Exception{

		//create time slot
		TimeSlotDAO dao = new TimeSlotDAO();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date idDay = null;
        idDay = sdf.parse("2025-08-17");
        java.sql.Date idDayParsed = new java.sql.Date(idDay.getTime());

        int rnd = (int)(Math.random() * 1000000);
		int rnd2 = (int)(Math.random() * 1000000);

        TimeSlot s = new TimeSlot("19:00", 60, idDayParsed, "x" + rnd);
        s.book(rnd2 + "P", "jeff");

        dao.addTimeSlot(s);

		CancelMeetingPar hand = new CancelMeetingPar();

		CancelMeetingParRequest cmr = new CancelMeetingParRequest(rnd2 + "P", "19:00", "2025-08-17", "x" + rnd);

		String otsRequest = new Gson().toJson(cmr);
		String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));

		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();

		hand.handleRequest(input, output, createContext("create"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CancelMeetingParResponse cmresp = new Gson().fromJson(post.body, CancelMeetingParResponse.class);
        System.out.println(cmresp);

        Assert.assertEquals("Successfully cancelled meeting at: 19:00", cmresp.response);
	}
}
