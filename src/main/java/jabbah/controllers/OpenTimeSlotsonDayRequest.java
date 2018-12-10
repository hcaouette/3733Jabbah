package jabbah.controllers;

public class OpenTimeSlotsonDayRequest {
	String day;
	String scheduleID;
	
	public OpenTimeSlotsonDayRequest(String day, String id) {
		this.day = day;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Close all on(" + day +")";
	}
}
