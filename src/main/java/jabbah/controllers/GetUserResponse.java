package jabbah.controllers;

public class GetUserResponse {
    public final String response;
    public final int httpCode;

    public GetUserResponse (String s, int code) {
        this.response = s;
        this.httpCode = code;
    }

    // 200 means success
    public GetUserResponse (String s) {
        this.response = s;
        this.httpCode = 200;
    }

    @Override
    public String toString() {
        return "Response(" + response + ")";
    }
}