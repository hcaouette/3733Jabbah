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
public class ExtendScheduleTest {

    Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testCreateAndChangeSchedule() throws IOException {
        CreateSchedule handler = new CreateSchedule();
        ExtendSchedule handlerTwo = new ExtendSchedule();

        int rnd = (int) (Math.random() * 10000000);
        int rndTwo = (int) (Math.random() * 10000000);
        String rndS = Integer.toString(rnd);
        String rndSTwo = Integer.toString(rndTwo);
        CreateScheduleRequest ar = new CreateScheduleRequest(rndS, "12:12", "13:27", 15, "1888-08-08", "1888-09-09", "zimm", 1544744104, rndSTwo); //

        String ccRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));

        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        System.out.println(resp);

        //Assert.assertEquals("Successfully defined Schedule:" + rndS, resp.response);

        // now change
        ExtendScheduleRequest arp = new ExtendScheduleRequest("1888-08-01", rndS);

        ccRequest = new Gson().toJson(arp);
        jsonRequest = new Gson().toJson(new PostRequest(ccRequest));

        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();

        handlerTwo.handleRequest(input, output, createContext("create"));

        post = new Gson().fromJson(output.toString(), PostResponse.class);
        ExtendScheduleResponse respa = new Gson().fromJson(post.body, ExtendScheduleResponse.class);
        System.out.println(respa);

        Assert.assertEquals("Succesfully extended schedule to date: " + "1888-08-01", respa.response);
    }

}
