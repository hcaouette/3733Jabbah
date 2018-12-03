package jabbah.model;

import java.sql.Date;

public class Schedule {
    String name;
    String startTime;
    String endTime;
    int timeSlotLength;
    Date startDate;
    Date endDate;
    String orgAccessCode;
    long timeCreated;
    String initialParticipantAccessCode;
    //ArrayList<User> participantList;
    //User scheduleOrganizer;
    //ArrayList<DaysInSchedule> dayList;

    public Schedule (String n, String sT, String eT, int t, Date sD, Date eD, String code, long time, String initialCode) {
        this.name = n;
        this.startTime = sT;
        this.endTime = eT;
        this.timeSlotLength = t;
        this.startDate = sD;
        this.endDate = eD;
        this.orgAccessCode = code;
        this.timeCreated = time;
        this.initialParticipantAccessCode = initialCode;
        //this.initialParticipantAccessCode = generateAccessCode();

        //create new objects for organizer and participants:
        //this.scheduleOrganizer = new User();
        //this.participantList = new ArrayList<User>();

        //generate a new list of days in the schedule
        //and time slots for each day
        //this.dayList = new ArrayList<DaysInSchedule>();

    }

	public String getOrgAccessCode() {
		return orgAccessCode;
	}

	public void setOrgAccessCode(String orgAccessCode) {
		this.orgAccessCode = orgAccessCode;
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
	
	public long getTimeCreated() {
		return this.timeCreated;
	}
	
	public String getInitialCode() {
		return this.initialParticipantAccessCode;
	}
	
	/*
	public User getScheduleOrganizer() {
		return scheduleOrganizer;
	}

	public void setScheduleOrganizer(User scheduleOrganizer) {
		this.scheduleOrganizer = scheduleOrganizer;
	}*/



}
