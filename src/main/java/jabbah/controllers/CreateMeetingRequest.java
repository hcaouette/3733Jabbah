package jabbah.controllers;

public class CreateMeetingRequest {
	String accessCode;
	String startTime;
	String day;
	String scheduleID;
    public String name;

	public CreateMeetingRequest(String code, String sT, String day, String id, String name) {
		this.accessCode = code;
		this.startTime = sT;
		this.day = day;
		this.scheduleID = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Create Meeting(" + name + "," + accessCode + ", " + startTime + " on " + day + ")";
	}
}
