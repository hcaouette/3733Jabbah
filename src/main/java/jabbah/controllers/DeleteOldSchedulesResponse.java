package jabbah.controllers;

public class DeleteOldSchedulesResponse {
    public final String response;
    public final int httpCode;

    public DeleteOldSchedulesResponse (String s, int code) {
        this.response = s;
        this.httpCode = code;
    }

    // 200 means success
    public DeleteOldSchedulesResponse (String s) {
        this.response = s;
        this.httpCode = 200;
    }

    @Override
    public String toString() {
        return "Response(" + response + ")";
    }
}