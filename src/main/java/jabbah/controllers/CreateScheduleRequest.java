package jabbah.controllers;

import java.sql.Date;

public class CreateScheduleRequest {
    String name;
    String startTime;
    String endTime;
    int timeSlotLength;
    Date startDate;
    Date endDate;
    String accessCode;

    public CreateScheduleRequest (String n, String sT, String eT, int t, Date sD, Date eD) {
        //this.name = n;
        this.startTime = sT;
        this.endTime = eT;
        this.timeSlotLength = t;
        this.startDate = sD;
        this.endDate = eD;
        this.accessCode = n;
    }

    @Override
    public String toString() {
        return "Create(" + accessCode + "," + startTime + "," +
    endTime + "," + timeSlotLength + "," + startDate + "," + endDate + ")";
    }

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getTimeSlotLength() {
		return timeSlotLength;
	}

	public void setTimeSlotLength(int timeSlotLength) {
		this.timeSlotLength = timeSlotLength;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
