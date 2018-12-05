package jabbah.controllers;

public class GetScheduleRequest {

    String accessCode;

    public GetScheduleRequest (String accessCode) {

    	this.accessCode = accessCode;
        
    }

    @Override
    public String toString() {
        return "Get(" + accessCode + ")";
    }
}
