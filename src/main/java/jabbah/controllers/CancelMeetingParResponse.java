package jabbah.controllers;

public class CancelMeetingParResponse {
	String response;
	int httpCode;
	
	public CancelMeetingParResponse(String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public CancelMeetingParResponse(String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
