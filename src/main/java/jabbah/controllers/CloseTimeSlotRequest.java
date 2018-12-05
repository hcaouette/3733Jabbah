package jabbah.controllers;

import java.sql.Date;

public class CloseTimeSlotRequest {
	String startTime;
	String idDay;
	String scheduleID;
	
	public CloseTimeSlotRequest(String sT, String day, String id) {
		this.startTime = sT;
		this.idDay = day;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Close(" + startTime + " on " + idDay + ")";
	}
}
