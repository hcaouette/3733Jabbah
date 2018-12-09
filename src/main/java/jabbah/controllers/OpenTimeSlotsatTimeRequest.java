package jabbah.controllers;

public class OpenTimeSlotsatTimeRequest {
	String startTime;
	String scheduleID;
	
	public OpenTimeSlotsatTimeRequest(String sT, String id) {
		this.startTime = sT;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Open all(" + startTime +")";
	}
}
