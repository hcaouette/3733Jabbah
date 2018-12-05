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
    String initialParticipantAccessCode;
    long timeCreated;
  //  ArrayList<User> participantList;
    //User scheduleOrganizer;
    //ArrayList<DaysInSchedule> dayList;

    public Schedule (String code, String sT, String eT, int t, Date sD, Date eD, String name, long tc, String p) {
        this.name = name;
        this.startTime = sT;
        this.endTime = eT;
        this.timeSlotLength = t;
        this.startDate = sD;
        this.endDate = eD;
        this.orgAccessCode = code;
        this.timeCreated = tc;
        this.initialParticipantAccessCode = p;

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
  
  /*
	public User getScheduleOrganizer() {
		return scheduleOrganizer;
	}

	public void setScheduleOrganizer(User scheduleOrganizer) {
		this.scheduleOrganizer = scheduleOrganizer;
	}
  */
    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getInitialParticipantAccessCode() {
        return initialParticipantAccessCode;
    }

    public void setInitialParticipantAccessCode(String partAccessCode) {
        this.initialParticipantAccessCode = partAccessCode;
    }

}
