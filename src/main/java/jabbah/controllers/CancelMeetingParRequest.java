package jabbah.controllers;

public class CancelMeetingParRequest {
	String accessCode;
	String startTime;
	String day;
	String scheduleID;
	
	public CancelMeetingParRequest(String code, String sT, String day, String id) {
		this.accessCode = code;
		this.startTime = sT;
		this.day = day;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Cancel Meeting(" + accessCode + ", " + startTime + " on " + day + ")";
	}
}
