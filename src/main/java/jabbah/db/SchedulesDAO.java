package jabbah.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
}
