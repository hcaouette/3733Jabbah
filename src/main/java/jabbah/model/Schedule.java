package jabbah.model;

import java.sql.Date;
import java.util.Random;
import java.util.random;

public class Schedule {
    String name;
    String startTime;
    String endTime;
    int timeSlotLength;
    Date startDate;
    Date endDate;
    String orgAccessCode;
    String initialParticipantAccessCode;
    //ArrayList<User> participantList;
    //User scheduleOrganizer;
    //ArrayList<DaysInSchedule> dayList;
    
    public Schedule (String n, String sT, String eT, int t, Date sD, Date eD) {
        this.name = n;
        this.startTime = sT;
        this.endTime = eT;
        this.timeSlotLength = t;
        this.startDate = sD;
        this.endDate = eD;
        //this.orgAccessCode = generateAccessCode(); decide upon random generation to fill these in
        //this.initialParticipantAccessCode = generateAccessCode();
        
        //create new objects for organizer and participants:
        this.scheduleOrganizer = new User();
        this.participantList = new ArrayList<User>();
        
        //generate a new list of days in the schedule
        //and timeslots for each day
        //this.dayList = new ArrayList<DaysInSchedule>();
        
    }
    
}
