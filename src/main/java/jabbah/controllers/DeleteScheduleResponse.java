package jabbah.controllers;

public class DeleteScheduleResponse {
    public final String response;
    public final int httpCode;

    public DeleteScheduleResponse (String s, int code) {
        this.response = s;
        this.httpCode = code;
    }

    // 200 means success
    public DeleteScheduleResponse (String s) {
        this.response = s;
        this.httpCode = 200;
    }

    @Override
    public String toString() {
        return "Response(" + response + ")";
    }
}