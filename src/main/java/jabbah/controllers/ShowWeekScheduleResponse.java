package jabbah.controllers;

import jabbah.model.*;
import java.util.ArrayList;
import java.util.List;

public class ShowWeekScheduleResponse {
	List<DaysInSchedule> week;
	int httpCode;
	
	public ShowWeekScheduleResponse(List<DaysInSchedule> week, int code) {
		this.week = week;
		this.httpCode = code;
	}
	
	public ShowWeekScheduleResponse(int code) {
		this.week = new ArrayList<DaysInSchedule>();
		this.httpCode = code;
	}
	
	public String toString() {
		if(week == null)
			return "Given date is not within this Schedule's boundaries";
		return "Week";
	}
}
