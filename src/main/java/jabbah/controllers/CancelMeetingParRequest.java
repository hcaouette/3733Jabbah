package jabbah.controllers;

public class CancelMeetingParRequest {
	String initialAccessCode;
	String startTime;
	String day;
	String participantCode;
	
	public CancelMeetingParRequest(String code, String sT, String day, String parID) {
		this.initialAccessCode = code;
		this.startTime = sT;
		this.day = day;
		this.participantCode = parID;
	}
	
	@Override
	public String toString() {
		return "Cancel Meeting(" + initialAccessCode + ", " + startTime + " on " + day + ")";
	}
}
