package jabbah.controllers;

public class CreateMeetingRequest {
	String initialAccessCode;
	String startTime;
	String day;
    public String name;
    String newParticipantCode;

	public CreateMeetingRequest(String code, String sT, String day, String name, String newCode) {
		this.initialAccessCode = code;
		this.startTime = sT;
		this.day = day;
		this.name = name;
		this.newParticipantCode = newCode;
	}

	@Override
	public String toString() {
		return "Create Meeting(" + name + "," + initialAccessCode + ", " + startTime + " on " + day + ")";
	}
}
