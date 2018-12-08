package jabbah.controllers;

import java.sql.Date;

public class CancelMeetingOrganizerRequest {
	String startTime;
	int duration;
	String idDay;
	String scheduleID;
	
	public CancelMeetingOrganizerRequest(String sT, int duration, String day, String id) {
		this.startTime = sT;
		this.duration = duration;
		this.idDay = day;
		this.scheduleID = id;
	}
	
	@Override
	public String toString() {
		return "Cancel(" + startTime + "," + duration + "," + idDay + "," + scheduleID +")";
	}
}