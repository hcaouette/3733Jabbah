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
public class DeleteScheduleTest {

    private final String CONTENT_TYPE = "image/jpeg";
    private S3Event event;
    Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testDeleteSchedule() throws IOException {
        CreateSchedule handler = new CreateSchedule();
        DeleteSchedule handlerTwo = new DeleteSchedule();

        int rnd = (int) (Math.random() * 10000000);
        int rndTwo = (int) (Math.random() * 10000000);
        String rndS = Integer.toString(rnd);
        String rndSTwo = Integer.toString(rndTwo);
        CreateScheduleRequest ar = new CreateScheduleRequest(rndS, "12:12", "12:27", 15, "1888-08-08", "1888-09-09", "zimm", 1544744104, rndSTwo);

        DeleteScheduleRequest arTwo = new DeleteScheduleRequest(rndS);
        String ccRequest = new Gson().toJson(ar);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));

        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
        //System.out.println(resp);

        String ccRequestTwo = new Gson().toJson(arTwo);
        String jsonRequestTwo = new Gson().toJson(new PostRequest(ccRequestTwo));

        InputStream inputTwo = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream outputTwo = new ByteArrayOutputStream();

        handlerTwo.handleRequest(inputTwo, outputTwo, createContext("delete"));

        PostResponse postTwo = new Gson().fromJson(outputTwo.toString(), PostResponse.class);
        DeleteScheduleResponse respTwo = new Gson().fromJson(postTwo.body, DeleteScheduleResponse.class);
        System.out.println(respTwo);

        Assert.assertEquals("Successfully deleted schedule:" + rndS, respTwo.response);

        //resp = new DeleteScheduleResponse("Successfully deleted schedule:" + rndS, resp.response);
    }
}
