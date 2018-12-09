package jabbah.controllers;

public class DeleteOldSchedulesRequest {

    String day;
    String currentTime;

    public DeleteOldSchedulesRequest(String s, String c) {
        day = s;
        currentTime = c;
    }

    @Override
    public String toString() {
        return "Get(" + day + "," + currentTime + ")";
    }
}