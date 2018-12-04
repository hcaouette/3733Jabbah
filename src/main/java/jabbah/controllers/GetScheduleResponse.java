package jabbah.controllers;

public class GetScheduleResponse {
    public final String response;
    public final int httpCode;

    public GetScheduleResponse (String s, int code) {
        this.response = s;
        this.httpCode = code;
    }

    // 200 means success
    public GetScheduleResponse(String s) {
        this.response = s;
        this.httpCode = 200;
    }

    @Override
    public String toString() {
        return "Response(" + response + ")";
    }
}