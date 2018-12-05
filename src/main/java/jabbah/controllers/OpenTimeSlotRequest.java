package jabbah.controllers;

import java.sql.Date;

public class OpenTimeSlotRequest {
	String startTime;
	String idDay;
	String scheduleID;
	
	public OpenTimeSlotRequest(String sT, String day, String id) {
		this.startTime = sT;
		this.idDay = day;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Open(" + startTime + " on " + idDay + ")";
	}
}
