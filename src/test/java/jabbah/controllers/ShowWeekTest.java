package jabbah.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ShowWeekTest {

    Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testShowWeekOnEdge() throws IOException {
        CreateSchedule handler = new CreateSchedule();
        ShowWeekScheduleHandler showHandler = new ShowWeekScheduleHandler();
        int rnd = (int) (Math.random() * 10000000);
        int rndTwo = (int) (Math.random() * 10000000);
        String rndS = Integer.toString(rnd);
        String rndSTwo = Integer.toString(rndTwo);
        CreateScheduleRequest ac = new CreateScheduleRequest(rndS, "12:12", "12:27", 15, "2019-02-06", "2019-02-08", "zimm", 1, rndSTwo);
        ShowWeekScheduleRequest ar = new ShowWeekScheduleRequest("2019-02-07",rndS);
        String ccRequest = new Gson().toJson(ac);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        String ccRequest2 = new Gson().toJson(ar);
        String jsonRequest2 = new Gson().toJson(new PostRequest(ccRequest2));

        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        InputStream input1 = new ByteArrayInputStream(jsonRequest2.getBytes());
        OutputStream output2 = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));
        showHandler.handleRequest(input1, output2, createContext("create"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        PostResponse post2 = new Gson().fromJson(output2.toString(), PostResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post2.body, CreateScheduleResponse.class);
        System.out.println(resp);	

        Assert.assertEquals("Successfully found week:" + "[2019-02-06,2019-02-07,2019-02-08]", resp.response);
    }
}