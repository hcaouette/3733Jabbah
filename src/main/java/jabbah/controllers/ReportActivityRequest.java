package jabbah.controllers;

public class ReportActivityRequest {

    String hour;
    String currentTime;

    public ReportActivityRequest(String s, String c) {
        hour = s;
        currentTime = c;
    }

    @Override
    public String toString() {
        return "Get(" + hour + "," + currentTime + ")";
    }
}