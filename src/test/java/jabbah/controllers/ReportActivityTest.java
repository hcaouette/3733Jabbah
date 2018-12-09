package jabbah.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
public class ReportActivityTest {

    private final String CONTENT_TYPE = "image/jpeg";
    private S3Event event;
    Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }

    @Test
    public void testReportActivity() throws Exception {
        ReportActivity handler = new ReportActivity();
        ReportActivityRequest q = new ReportActivityRequest("0", "1");
        String otsRequest = new Gson().toJson(q);
        String jsonRequest = new Gson().toJson(new PostRequest(otsRequest));
        InputStream input = new ByteArrayInputStream(otsRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("list"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        ReportActivityResponse resp = new Gson().fromJson(post.body, ReportActivityResponse.class);

        Assert.assertEquals(200, resp.httpCode);
    }
}

