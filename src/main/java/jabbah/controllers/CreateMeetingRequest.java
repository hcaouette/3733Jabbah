package jabbah.controllers;

public class CreateMeetingRequest {
	String accessCode;
	String startTime;
	String day;
	String scheduleID;
	
	public CreateMeetingRequest(String code, String sT, String day, String id) {
		this.accessCode = code;
		this.startTime = sT;
		this.day = day;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Create Meeting(" + accessCode + " for " + startTime + " on " + day + ")";
	}
}
