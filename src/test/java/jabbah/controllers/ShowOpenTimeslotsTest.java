package jabbah.controllers;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.model.ObjectMetadata;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ShowOpenTimeslotsTest {

    private final String CONTENT_TYPE = "image/jpeg";
    private S3Event event;
/*
    @Mock
    private AmazonS3 s3Client;
    @Mock
    private S3Object s3Object;

    @Captor
    private ArgumentCaptor<GetObjectRequest> getObjectRequest;
    */

    @Before
    public void setUp() throws IOException {
        event = TestUtils.parse("/s3-event.put.json", S3Event.class);

        // TODO: customize your mock logic for s3 client
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(CONTENT_TYPE);
        //when(s3Object.getObjectMetadata()).thenReturn(objectMetadata);
        //when(s3Client.getObject(getObjectRequest.capture())).thenReturn(s3Object);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testShowOpenTimeslots() {
        //ShowOpenTimeslots handler = new ShowOpenTimeslots(s3Client);
        Context ctx = createContext();

        //String output = handler.handleRequest(event, ctx);

        // TODO: validate output here if needed.
        //Assert.assertEquals(CONTENT_TYPE, output);
    }
}
