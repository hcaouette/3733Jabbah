package jabbah.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jabbah.model.DaysInSchedule;
import jabbah.model.Schedule;
import jabbah.model.TimeSlot;

public class SchedulesDAO {

	java.sql.Connection conn;

    public SchedulesDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }

    public Schedule getSchedule(String orgAccessCode) throws Exception {

        try {
            Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE orgAccessCode=?;");
            ps.setString(1,  orgAccessCode);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                schedule = generateSchedule(resultSet);
            }
            resultSet.close();
            ps.close();

            return schedule;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting schedule: " + e.getMessage());
        }
    }

    public Schedule getScheduleParticipant(String initialParticipantAccessCode) throws Exception {

        try {
            Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE initialParticipantAccessCode=?;");
            ps.setString(1,  initialParticipantAccessCode);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                schedule = generateSchedule(resultSet);
            }
            resultSet.close();
            ps.close();

            return schedule;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting schedule: " + e.getMessage());
        }
    }

    public boolean deleteSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedules WHERE orgAccessCode=?;");
            ps.setString(1, schedule.getOrgAccessCode());
            int numAffected = ps.executeUpdate();
            ps.close();

            DaysInScheduleDAO d = new DaysInScheduleDAO();
            TimeSlotDAO t = new TimeSlotDAO();
            d.deleteDay(new DaysInSchedule(null, schedule.getOrgAccessCode()));
            t.deleteTimeSlot(new TimeSlot(null, 0, null, schedule.getOrgAccessCode()));

            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete schedule: " + e.getMessage());
        }
    }
/*
    public boolean updateSchedule(Schedule schedule) throws Exception {
        try {
        	String query = "UPDATE Schedules SET value=? WHERE orgAccessCode=?;";
        	PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, schedule.getOrgAccessCode());
            ps.setString(2, schedule.getStartTime());
            ps.setString(3, schedule.getEndTime());
            ps.setInt(4, schedule.getTimeSlotLength());
            ps.setDate(5, schedule.getStartDate());
            ps.setDate(6, schedule.getEndDate());
           // ps.setDouble(1, constant.value);
       //     ps.setString(2, constant.name);
            int numAffected = ps.executeUpdate();
            ps.close();

            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("Failed to update report: " + e.getMessage());
        }
    }
*/

    public boolean addSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE orgAccessCode=?;");
            ps.setString(1, schedule.getOrgAccessCode());
            ResultSet resultSet = ps.executeQuery();

            // already present?
            while (resultSet.next()) {
                Schedule s = generateSchedule(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO Schedules (orgAccessCode,startTime,endTime,timeSlotLength,startDate,endDate,name,timeCreated,initialParticipantAccessCode) values(?,?,?,?,?,?,?,?,?);");

            ps.setString(1, schedule.getOrgAccessCode());
            ps.setString(2, schedule.getStartTime());
            ps.setString(3, schedule.getEndTime());
            ps.setInt(4, schedule.getTimeSlotLength());
            ps.setDate(5, schedule.getStartDate());
            ps.setDate(6, schedule.getEndDate());
            ps.setString(7, schedule.getName());
            ps.setLong(8, schedule.getTimeCreated());
            ps.setString(9, schedule.getInitialParticipantAccessCode());
            ps.execute();
            //add in dates
            /* deprecated:
             * DateTime start = new DateTime(new GregorianCalendar(schedule.getStartDate().getYear(), schedule.getStartDate().getMonth(), schedule.getStartDate().getDay()).getTime());
            DateTime end = new DateTime(new GregorianCalendar(schedule.getEndDate().getYear(), schedule.getEndDate().getMonth(), schedule.getEndDate().getDay()).getTime());

            int numberOfDays = Days.daysBetween(start.toDateMidnight(), end.toDateMidnight()).getDays(); */
            long diff = schedule.getEndDate().getTime() - schedule.getStartDate().getTime();
            long numberOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (numberOfDays < 1) {
                return false;
            }

            int totalTime;
            int startHour = Integer.parseInt(schedule.getStartTime().substring(0, 2));
            int startMinute = Integer.parseInt(schedule.getStartTime().substring(3, 5));
            int endHour = Integer.parseInt(schedule.getEndTime().substring(0, 2));
            int endMinute = Integer.parseInt(schedule.getEndTime().substring(3, 5));
            int startTime = (startHour * 60) + startMinute; //starting time in minutes
            int endTime = (endHour * 60) + endMinute; //endingTime in minutes
            totalTime = endTime - startTime; //the total time in minutes per day
            if (totalTime % schedule.getTimeSlotLength() != 0) {
                //if the timeslots don't line up properly, fail
                return false;
            }
            int totalTimeslots = totalTime / schedule.getTimeSlotLength();

            DaysInScheduleDAO d = new DaysInScheduleDAO();
            TimeSlotDAO t = new TimeSlotDAO();
            Date currentDay = schedule.getStartDate();
            //add a DayInSchedule,
            //then add all TimeSlots for that day,
            //repeat until all days are added and have all timeslots added
            for (int i = 0; i< (numberOfDays + 1); i++) {
                d.addDay(new DaysInSchedule(currentDay, schedule.getOrgAccessCode()));
                int currentTime = startTime; //for each new day, reset to starting time
                for (int j = 0; j< totalTimeslots; j++) {
                    //first parse the current time for the timeslot as a string
                    int currentHour = ((currentTime - (currentTime % 60)) / 60);
                    int currentMinute = (currentTime % 60);
                    String timeString = currentHour + ":" + currentMinute;
                    if (currentMinute < 10) {
                        timeString = currentHour + ":0" + currentMinute;
                    }
                    if (currentHour < 10) {
                        timeString = "0" + currentHour + ":" + currentMinute;
                    }
                    if (currentHour < 10 && currentMinute < 10) {
                        timeString = "0" + currentHour + ":0" + currentMinute;
                    }
                    //now add the timeslot for the current time
                    t.addTimeSlot(new TimeSlot(timeString, schedule.getTimeSlotLength(), currentDay, schedule.getOrgAccessCode()));
                    currentTime = currentTime + schedule.getTimeSlotLength();
                }
                //increment by a day by adding the number of milliseconds
                currentDay = new Date((currentDay.getTime() + (1 * 24 * 60 * 60 * 1000)));
            }
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to insert schedule: " + e.getMessage());
        }
    }

    public List<Schedule> getAllSchedules() throws Exception {

        List<Schedule> allSchedules = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Schedules";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Schedule s = generateSchedule(resultSet);
                allSchedules.add(s);
            }
            resultSet.close();
            statement.close();
            return allSchedules;

        } catch (Exception e) {
            throw new Exception("Failed in getting schedules: " + e.getMessage());
        }
    }
    public String getOrgAccessCode(String initialParticipantAccessCode) throws Exception
    {
    	try {
            Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Schedules WHERE initialParticipantAccessCode=?;");
            ps.setString(1,  initialParticipantAccessCode);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                schedule = generateSchedule(resultSet);
            }
            resultSet.close();
            ps.close();

            return schedule.getOrgAccessCode();

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting schedule: " + e.getMessage());
        }
    }

    public List<Schedule> getNewSchedules(String hour, String currentHour) throws Exception {

        List<Schedule> allSchedules = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Schedules";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Schedule s = generateSchedule(resultSet);
                //only add to list to return if timeslot
                //is created within the given number of hours
                if(s.getTimeCreated() + (Long.parseLong(hour) * 60 * 60 * 1000) >= Long.parseLong(currentHour))
                allSchedules.add(s);
            }
            resultSet.close();
            statement.close();
            return allSchedules;

        } catch (Exception e) {
            throw new Exception("Failed in getting schedules: " + e.getMessage());
        }
    }

    private Schedule generateSchedule(ResultSet resultSet) throws Exception {
        String accessCode  = resultSet.getString("orgAccessCode");
        String startTime = resultSet.getString("startTime");
        String endTime = resultSet.getString("startTime");
        int timeSlotLength = resultSet.getInt("timeSlotLength");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = resultSet.getString("startDate");
        String endDateString = resultSet.getString("endDate");
        String name = resultSet.getString("name");
        long timeCreated = resultSet.getLong("timeCreated");
        String initialParticipantAccessCode = resultSet.getString("initialParticipantAccessCode");

        //need to check dates are correct format
        java.util.Date startDateUtil = null;
        java.util.Date endDateUtil = null;
        try { //try to parse our dates to correct format
        startDateUtil = sdf.parse(startDateString);
        endDateUtil = sdf.parse(endDateString);
        } catch (ParseException p1) {
            p1.printStackTrace();
        }
        java.sql.Date startDate = new java.sql.Date(startDateUtil.getTime());
        java.sql.Date endDate = new java.sql.Date(endDateUtil.getTime());
        return new Schedule (accessCode, startTime,endTime,timeSlotLength,startDate,endDate,name,timeCreated,initialParticipantAccessCode);
    }

    public boolean deleteOldSchedules(String day, String currentTime) throws Exception {
        try { 
        	
        	// converts Strings to numbers
        	int numDay = Integer.parseInt(day);
        	long curTime = Long.parseLong(currentTime);

        	// grabs all existing schedules
        	List<Schedule> allSchedules = getAllSchedules();
        	
        	long difference = 0;
        	int hours = 0;
        	int daysOld = 0;
        	
        	int counter = 0;
        	int numAffected = 0;
        	
        	for(Schedule s: allSchedules) {
        		//loop through all schedules to find difference in time
        		difference = curTime - s.getTimeCreated(); 
        		//convert difference in time in terms of days
        		hours = (int) difference / 3600000;
        		daysOld = hours / 24;
        		
        		//if the schedule is older than the given day, delete
        		if(daysOld > numDay) {
        			counter++; // count number of schedule that are more than numDay old
        			
        			if(deleteSchedule(s))
        				numAffected++; // count number of successfully deleted schedules
        		}
        	}
        	
        	//counter and numAffected should match or else that means that not all schedules 
        	//that needed to be deleted are deleted
            return (numAffected == counter);

        } catch (Exception e) {
            throw new Exception("Failed to delete schedule: " + e.getMessage());
        }
    }

	public boolean extendSchedule(String date, String scheduleID) {
		try {
			Schedule schedule = getSchedule(scheduleID);
            long originalDiff =  schedule.getEndDate().getTime()-schedule.getStartDate().getTime();
            //currently gives the wrong number of used days by including Saturdays and Sundays
            Date scheduleStart = schedule.getStartDate();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(schedule.getStartDate());
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(schedule.getEndDate());
            int workDays = 1;
            while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
                if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++workDays;
                }
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            }
            long numberOfDaysOrigin = TimeUnit.DAYS.convert(originalDiff, TimeUnit.MILLISECONDS);
            int numberOfDaysOriginParsed = (int) numberOfDaysOrigin;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date startDateUtil = null;
	        startDateUtil = sdf.parse(date);
	        java.sql.Date dateParsed = new java.sql.Date(startDateUtil.getTime());
	        if (dateParsed.compareTo(schedule.getEndDate()) > 0) {
	           //"NewDate is after EndingDate"
	        	String queryOne = "UPDATE Schedules SET endDate=? WHERE orgAccessCode=?;";
	        	PreparedStatement ps = conn.prepareStatement(queryOne);
				ps.setDate(1, dateParsed);
				ps.setString(2, scheduleID);
				int numAffected = ps.executeUpdate();
				ps.close();

	            long diff = dateParsed.getTime() - schedule.getEndDate().getTime();
	            long numberOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

	            if (numberOfDays < 1) {
	                return false;
	            }

	            int totalTime;
	            int startHour = Integer.parseInt(schedule.getStartTime().substring(0, 2));
	            int startMinute = Integer.parseInt(schedule.getStartTime().substring(3, 5));
	            //for what seems like no reason at all, getEndTime() in this function
                //returns the starting time, despite functioning correctly in addSchedule()
                //so instead, must find the number of timeslots per day in another way
	            TimeSlotDAO t2 = new TimeSlotDAO();
	            List<TimeSlot> list = t2.getSpecificTimeSlot(schedule.getOrgAccessCode(), "", "", "", "", "");
	            int totalTimeslotsTwo = list.size();
	            int totalTimeslotsPerDay = totalTimeslotsTwo / workDays;
	            int endHour = Integer.parseInt(schedule.getEndTime().substring(0, 2));
	            int endMinute = Integer.parseInt(schedule.getEndTime().substring(3, 5));
	            int startTime = (startHour * 60) + startMinute; //starting time in minutes
	            int endTime = (endHour * 60) + endMinute; //endingTime in minutes
	            totalTime = endTime - startTime; //the total time in minutes per day
	            if (totalTime % schedule.getTimeSlotLength() != 0) {
	                //if the timeslots don't line up properly, fail
	                return false;
	            }
	            int totalTimeslots = totalTime / schedule.getTimeSlotLength();

	            DaysInScheduleDAO d = new DaysInScheduleDAO();
	            TimeSlotDAO t = new TimeSlotDAO();
	            Date currentDay = schedule.getEndDate();
	            // add a date and start at one because you don't start on the current day
	            currentDay = new Date((currentDay.getTime() + (1 * 24 * 60 * 60 * 1000)));
	            //add a DayInSchedule,
	            //then add all TimeSlots for that day,
	            //repeat until all days are added and have all timeslots added
	            for (int i = 0; i< numberOfDays; i++) {
	                d.addDay(new DaysInSchedule(currentDay, schedule.getOrgAccessCode()));
	                int currentTime = startTime; //for each new day, reset to starting time
	                for (int j = 0; j< totalTimeslotsPerDay; j++) {
	                    //first parse the current time for the timeslot as a string
	                    int currentHour = ((currentTime - (currentTime % 60)) / 60);
	                    int currentMinute = (currentTime % 60);
	                    String timeString = currentHour + ":" + currentMinute;
	                    if (currentMinute < 10) {
	                        timeString = currentHour + ":0" + currentMinute;
	                    }
	                    if (currentHour < 10) {
	                        timeString = "0" + currentHour + ":" + currentMinute;
	                    }
	                    if (currentHour < 10 && currentMinute < 10) {
	                        timeString = "0" + currentHour + ":0" + currentMinute;
	                    }
	                    //now add the timeslot for the current time
	                    t.addTimeSlot(new TimeSlot(timeString, schedule.getTimeSlotLength(), currentDay, schedule.getOrgAccessCode()));
	                    currentTime = currentTime + schedule.getTimeSlotLength();
	                }
	                //increment by a day by adding the number of milliseconds
	                currentDay = new Date((currentDay.getTime() + (1 * 24 * 60 * 60 * 1000)));

	            }
	            return numAffected == 1;
	        } else if (dateParsed.compareTo(schedule.getStartDate()) < 0) {
	            //NewDate is before startingDate
	        	String queryTwo = "UPDATE Schedules SET startDate=? WHERE orgAccessCode=?;";
	        	PreparedStatement ps = conn.prepareStatement(queryTwo);
				ps.setDate(1, dateParsed);
				ps.setString(2, scheduleID);
				int numAffected = ps.executeUpdate();
				ps.close();
				long diff =  schedule.getStartDate().getTime()-dateParsed.getTime();
	            long numberOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

	            if (numberOfDays < 1) {
	                return false;
	            }

	            int totalTime;
	            int startHour = Integer.parseInt(schedule.getStartTime().substring(0, 2));
	            int startMinute = Integer.parseInt(schedule.getStartTime().substring(3, 5));
	            //for what seems like no reason at all, getEndTime() in this function
	            //returns the starting time, despite functioning correctly in addSchedule()
	            //so instead, must find the number of timeslots per day in another way
	            TimeSlotDAO t2 = new TimeSlotDAO();
	            List<TimeSlot> list = t2.getSpecificTimeSlot(schedule.getOrgAccessCode(), "", "", "", "Monday", "");
	            List<TimeSlot> listTwo = t2.getSpecificTimeSlot(schedule.getOrgAccessCode(), "", "", "", "Tuesday", "");
	            List<TimeSlot> listThree = t2.getSpecificTimeSlot(schedule.getOrgAccessCode(), "", "", "", "Wednesday", "");
	            List<TimeSlot> listFour = t2.getSpecificTimeSlot(schedule.getOrgAccessCode(), "", "", "", "Thursday", "");
	            List<TimeSlot> listFive = t2.getSpecificTimeSlot(schedule.getOrgAccessCode(), "", "", "", "Friday", "");
	            int totalTimeslotsTwo = list.size() + listTwo.size() + listThree.size() + listFour.size() + listFive.size(); //all timeslots in the schedule before the extension
	            //need to get the number of days in the schedule:
	            int totalTimeslotsPerDay = totalTimeslotsTwo / workDays;
	            int endHour = Integer.parseInt(schedule.getEndTime().substring(0, 2));
	            int endMinute = Integer.parseInt(schedule.getEndTime().substring(3, 5));
	            int startTime = (startHour * 60) + startMinute; //starting time in minutes
	            int endTime = (endHour * 60) + endMinute; //endingTime in minutes
	            totalTime = endTime - startTime; //the total time in minutes per day
	            if (totalTime % schedule.getTimeSlotLength() != 0) {
	                //if the timeslots don't line up properly, fail
	                return false;
	            }
	            int totalTimeslots = totalTime / schedule.getTimeSlotLength(); //supposed to be total
	            //timeslots per day

	            DaysInScheduleDAO d = new DaysInScheduleDAO();
	            TimeSlotDAO t = new TimeSlotDAO();
	            Date currentDay = dateParsed;

	            //add a DayInSchedule,
	            //then add all TimeSlots for that day,
	            //repeat until all days are added and have all timeslots added
	            //is one shorter than normal so that it won't include the start date of the schedule
	            for (int i = 0; i< numberOfDays; i++) {
	                d.addDay(new DaysInSchedule(currentDay, schedule.getOrgAccessCode()));
	                int currentTime = startTime; //for each new day, reset to starting time
	                //for (int j = 0; j< totalTimeslots; j++) {
	                for (int j = 0; j< totalTimeslotsPerDay; j++) {
	                    //first parse the current time for the timeslot as a string
	                    int currentHour = ((currentTime - (currentTime % 60)) / 60);
	                    int currentMinute = (currentTime % 60);
	                    String timeString = currentHour + ":" + currentMinute;
	                    if (currentMinute < 10) {
	                        timeString = currentHour + ":0" + currentMinute;
	                    }
	                    if (currentHour < 10) {
	                        timeString = "0" + currentHour + ":" + currentMinute;
	                    }
	                    if (currentHour < 10 && currentMinute < 10) {
	                        timeString = "0" + currentHour + ":0" + currentMinute;
	                    }
	                    //now add the timeslot for the current time
	                    t.addTimeSlot(new TimeSlot(timeString, schedule.getTimeSlotLength(), currentDay, schedule.getOrgAccessCode()));
	                    currentTime = currentTime + schedule.getTimeSlotLength();
	                }
	                //increment by a day by adding the number of milliseconds
	                currentDay = new Date((currentDay.getTime() + (1 * 24 * 60 * 60 * 1000)));
	            }


	        	return numAffected == 1;
	        } else {
	            return false;
	        }

			}
			 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
}
