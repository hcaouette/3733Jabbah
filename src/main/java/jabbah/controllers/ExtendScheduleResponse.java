package jabbah.controllers;

public class ExtendScheduleResponse {
    public final String response;
    public final int httpCode;

    public ExtendScheduleResponse (String s, int code) {
        this.response = s;
        this.httpCode = code;
    }

    // 200 means success
    public ExtendScheduleResponse (String s) {
        this.response = s;
        this.httpCode = 200;
    }

    @Override
    public String toString() {
        return "Response(" + response + ")";
    }
}