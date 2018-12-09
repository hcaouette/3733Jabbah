package jabbah.controllers;

public class CloseTimeSlotsatTimeRequest {
	String startTime;
	String scheduleID;
	
	public CloseTimeSlotsatTimeRequest(String sT, String id) {
		this.startTime = sT;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Close all(" + startTime +")";
	}
}
