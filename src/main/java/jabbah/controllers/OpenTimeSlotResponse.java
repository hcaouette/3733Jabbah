package jabbah.controllers;

public class OpenTimeSlotResponse {
	String response;
	int httpCode;
	
	public OpenTimeSlotResponse(String res, int code) {
		this.response = res;
		this.httpCode = code;
	}
	
	// 200 mean success
	public OpenTimeSlotResponse(String res) {
		this.response = res;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
