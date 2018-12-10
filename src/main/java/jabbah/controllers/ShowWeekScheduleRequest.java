package jabbah.controllers;

public class ShowWeekScheduleRequest {
	String date;
	String scheduleID;

	public ShowWeekScheduleRequest(String date, String id) {
		this.date = date;
		this.scheduleID = id;
	}

	@Override
    public String toString() {
		return "ShowWeek(" + date +
				" within schedule: " + scheduleID + ")";
	}
}
