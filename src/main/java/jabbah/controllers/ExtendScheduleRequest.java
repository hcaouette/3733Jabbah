package jabbah.controllers;

public class ExtendScheduleRequest {
	String date;
	String scheduleID;

	public ExtendScheduleRequest(String date, String id) {
		this.date = date;
		this.scheduleID = id;
	}

	@Override
    public String toString() {
		return "ShowWeek(" + date +
				" within schedule: " + scheduleID + ")";
	}
}
