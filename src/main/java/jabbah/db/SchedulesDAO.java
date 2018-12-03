package jabbah.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jabbah.model.Schedule;

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

    public boolean deleteSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Schedules WHERE orgAccessCode=?;");
            ps.setString(1, schedule.getOrgAccessCode());
            int numAffected = ps.executeUpdate();
            ps.close();

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
