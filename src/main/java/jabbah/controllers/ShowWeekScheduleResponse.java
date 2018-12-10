package jabbah.controllers;

import java.util.ArrayList;
import java.util.List;

import jabbah.model.TimeSlot;

public class ShowWeekScheduleResponse {
	List<TimeSlot> week;
	String startingDay;
	int interval;
	int httpCode;

	// not used yet maybe later
	public ShowWeekScheduleResponse(List<TimeSlot> week, String startingDay, int interval, int code) {
		this.week = week;
		this.startingDay = startingDay;
		this.interval = interval;
		this.httpCode = code;
	}

	public ShowWeekScheduleResponse(int code) {
		this.week = new ArrayList<TimeSlot>();
		this.startingDay = null;
		this.interval = 0;
		this.httpCode = code;
	}

	@Override
    public String toString() {
		if(week == null)
			return "Given date is not within this Schedule's boundaries";
		return "Response(" + week + "," + startingDay + "," + interval + ")";
	}
}
