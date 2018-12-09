package jabbah.controllers;

public class CloseTimeSlotsonDayRequest {
	String day;
	String scheduleID;
	
	public CloseTimeSlotsonDayRequest(String day, String id) {
		this.day = day;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Close all on(" + day +")";
	}
}
