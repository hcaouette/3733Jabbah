package jabbah.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.google.gson.Gson;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class DeleteOldSchedulesTest {
	
    Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
    
    @Test
    public void testDeleteOldSchedules() throws IOException {
    	//create a couple of schedules to delete 
        CreateSchedule handler = new CreateSchedule();

        int rnd = (int) (Math.random() * 10000000);
        int rndTwo = (int) (Math.random() * 10000000);
        
        int rnd2 = (int) (Math.random() * 10000000);
        int rndTwo2 = (int) (Math.random() * 10000000);
        
        //Schedule created Friday, November 30, 2018
        CreateScheduleRequest ar = new CreateScheduleRequest("w" + rnd, "12:00", "14:00", 30, "2003-01-01", "2003-01-03", "BOO", 1543554000, "w" + rndTwo);
        String ccRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(input, output, createContext("create"));
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        System.out.println(resp);
        
        //Schedule created Wednesday, November 28, 2018
        ar = new CreateScheduleRequest("w" + rnd2, "12:00", "14:00", 30, "2003-02-01", "2003-02-03", "BRRR", 1543381200, "w" + rndTwo2);
        ccRequest = new Gson().toJson(ar);
        jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();
        handler.handleRequest(input, output, createContext("create"));
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        System.out.println(resp);
        
        //Delete all schedule that are more than 10 days old with current time being Tuesday, December 11, 2018 
        //timestamp = 1544504400000
        
		DeleteOldSchedules deleteHandler = new DeleteOldSchedules();
		
		DeleteOldSchedulesRequest dosr = new DeleteOldSchedulesRequest("10", "1544504400000");
		
		String dosrRequest = new Gson().toJson(dosr);
		jsonRequest = new Gson().toJson(new PostRequest(dosrRequest));
		
		input = new ByteArrayInputStream(jsonRequest.getBytes());
		output = new ByteArrayOutputStream();
		
		deleteHandler.handleRequest(input, output, createContext("create"));
		
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        DeleteOldSchedulesResponse deleteResp = new Gson().fromJson(post.body, DeleteOldSchedulesResponse.class);
        System.out.println(deleteResp);

        Assert.assertEquals("Deleted schedules that are 10 days old: 1544504400000", deleteResp.response);

        
    }
}
