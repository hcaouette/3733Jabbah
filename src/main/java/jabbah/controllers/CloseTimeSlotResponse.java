package jabbah.controllers;

public class CloseTimeSlotResponse {
	String response;
	int httpCode;
	
	public CloseTimeSlotResponse(String res, int code) {
		this.response = res;
		this.httpCode = code;
	}
	
	// 200 mean success
	public CloseTimeSlotResponse(String res) {
		this.response = res;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}

}
