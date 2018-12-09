package jabbah.controllers;

import java.util.ArrayList;
import java.util.List;

import jabbah.model.DaysInSchedule;

public class ShowWeekScheduleResponse {
	List<DaysInSchedule> week;
	int httpCode;

	// not used yet maybe later
	public ShowWeekScheduleResponse(List<DaysInSchedule> week, int code) {
		this.week = week;
		this.httpCode = code;
	}

	public ShowWeekScheduleResponse(int code) {
		this.week = new ArrayList<DaysInSchedule>();
		this.httpCode = code;
	}

	@Override
    public String toString() {
		if(week == null)
			return "Given date is not within this Schedule's boundaries";
		return "Week";
	}
}
