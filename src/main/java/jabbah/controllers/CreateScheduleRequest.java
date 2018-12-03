package jabbah.controllers;

public class CreateScheduleRequest {
    String name;
    String startTime;
    String endTime;
    int timeSlotLength;
    String startDate; //need to use strings here, as gson Date format is not the same
    String endDate; //as java.sql.date format!
    String orgAccessCode;
    long timeCreated;
    String initialParticipantAccessCode;

    public CreateScheduleRequest (String n, String sT, String eT, int t, String sD, String eD, String name, long timeCreated, String initialParticipantAccessCode) {
        this.name = name;
        this.startTime = sT;
        this.endTime = eT;
        this.timeSlotLength = t;
        this.startDate = sD;
        this.endDate = eD;
        this.orgAccessCode = n;
        this.timeCreated = timeCreated;
        this.initialParticipantAccessCode = initialParticipantAccessCode;
    }

    @Override
    public String toString() {
        return "Create(" + orgAccessCode + "," + startTime + "," +
    endTime + "," + timeSlotLength + "," + startDate + "," + endDate + "," +
                name + "," + timeCreated + "," + initialParticipantAccessCode + ")";
    }

	public String getAccessCode() {
		return orgAccessCode;
	}

	public void setAccessCode(String accessCode) {
		this.orgAccessCode = accessCode;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
