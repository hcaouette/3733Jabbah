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
public class CreateScheduleTest {

    Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testCreateAndChangeSchedule() throws IOException {
        CreateScheduleHandler handler = new CreateScheduleHandler();

        int rnd = (int) (Math.random() * 1000000);
        CreateScheduleRequest ar = new CreateScheduleRequest(null, null, null, rnd, null, null);

        String ccRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));

        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        System.out.println(resp);

        Assert.assertEquals("Successfully defined Schedule:x" + rnd, resp.response);

        // now change

        ar = new CreateScheduleRequest(jsonRequest, jsonRequest, jsonRequest, rnd, null, null);

        ccRequest = new Gson().toJson(ar);
        jsonRequest = new Gson().toJson(new PostRequest(ccRequest));

        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));

        post = new Gson().fromJson(output.toString(), PostResponse.class);
        resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        System.out.println(resp);

        Assert.assertEquals("Successfully defined Schedule:x" + rnd, resp.response);
    }

}
