package jabbah.controller;

import java.sql.*;

public class CreateScheduleRequest {
    String name;
    String startTime;
    String endTime;
    int timeSlotLength;
    Date startDate;
    Date endDate;
    
    public CreateScheduleRequest (String n, String sT, String eT, int t, Date sD, Date eD) {
        this.name = n;
        this.startTime = sT;
        this.endTime = eT;
        this.timeSlotLength = t;
        this.startDate = sD;
        this.endDate = eD;
    }
    
    public String toString() {
        return "Create(" + name + "," + startTime + "," +
    endTime + "," + timeSlotLength + "," + startDate + "," + endDate + ")";
    }
}
