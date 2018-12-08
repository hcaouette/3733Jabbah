package jabbah.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jabbah.model.TimeSlot;

public class TimeSlotDAO {

	java.sql.Connection conn;

	public TimeSlotDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
	}

	public TimeSlot getTimeSlot(String time, Date day, String id) throws Exception{
		try {
			TimeSlot slot = null;
	        PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlot WHERE startTime=? AND idDays = ? AND orgAccessCode =?;");
	        ps.setString(1,  time);
	        ps.setDate(2, day);
	        ps.setString(3, id);
	        ResultSet resultSet = ps.executeQuery();

	        while (resultSet.next()) {
	        	slot = generateTimeSlot(resultSet);
	        }
	        resultSet.close();
	        ps.close();

	        return slot;
		}catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting time slot: " + e.getMessage());
		}
	}

	public boolean deleteTimeSlot(TimeSlot slot) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM TimeSlot WHERE orgAccessCode=?;");
			ps.setString(1, slot.getScheduleID());
			int numAffected = ps.executeUpdate();

			return numAffected >= 1;

		} catch (Exception e) {
			throw new Exception("Failed to delete time slot: " + e.getMessage());
		}
	}

	public boolean updateTimeSlot(TimeSlot slot) throws Exception {
        try {
        	String query = "UPDATE TimeSlot SET isOpen=? WHERE startTime=? AND idDays=? AND orgAccessCode=?;";
        	PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, slot.open());
            ps.setString(2, slot.getTime());
            ps.setDate(3, slot.getDate());
            ps.setString(4, slot.getScheduleID());
            int numAffected = ps.executeUpdate();
            ps.close();

            return numAffected == 1;
        } catch (Exception e) {
            throw new Exception("Failed to update time slot: " + e.getMessage());
        }
    }

	public boolean updateParticipant(TimeSlot slot)throws Exception{
		try {
			String query = "UPDATE TimeSlot SET participant=? WHERE startTime=? AND idDays=? AND orgAccessCode=?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, slot.getParticipant());
			ps.setString(2, slot.getTime());
			ps.setDate(3, slot.getDate());
			ps.setString(4, slot.getScheduleID());
			int numAffected = ps.executeUpdate();
			ps.close();

			return numAffected == 1;
		} catch (Exception e) {
			throw new Exception("Failed to update time slot: " + e.getMessage());
		}
	}

    public boolean addTimeSlot(TimeSlot slot) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlot WHERE startTime = ? AND idDays=? AND orgAccessCode=?;");
            ps.setString(1, slot.getTime());
            ps.setDate(2, slot.getDate());
            ps.setString(3, slot.getScheduleID());
            ResultSet resultSet = ps.executeQuery();

            // already present?
            TimeSlot s;
            while (resultSet.next()) {
                s = generateTimeSlot(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO TimeSlot (startTime, duration, participant, isOpen, idDays, orgAccessCode) values(?,?,?,?,?,?);");
            ps.setString(1,  slot.getTime());
            ps.setInt(2,  slot.getDuration());
            ps.setString(3, slot.getParticipant()); //
            ps.setBoolean(4,  slot.open());
            ps.setDate(5, slot.getDate());
            ps.setString(6,  slot.getScheduleID());
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to insert time slot: " + e.getMessage());
        }
    }

    public List<TimeSlot> getAllTimeSlot() throws Exception {

        List<TimeSlot> allTimeSlots = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM TimeSlot";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                TimeSlot slot = generateTimeSlot(resultSet);
                allTimeSlots.add(slot);
            }
            resultSet.close();
            statement.close();
            return allTimeSlots;

        } catch (Exception e) {
            throw new Exception("Failed in getting all time slots: " + e.getMessage());
        }
    }

	private TimeSlot generateTimeSlot(ResultSet resultSet) throws Exception{
		int duration = resultSet.getInt("duration");
		String startTime = resultSet.getString("startTime");
		Date day = resultSet.getDate("idDays");
		String id = resultSet.getString("orgAccessCode");
		boolean open = resultSet.getBoolean("isOpen");
		String participant = resultSet.getString("participant");

		TimeSlot s = new TimeSlot(startTime, duration, day, id);

		// make sure isOpen and participant matches what's in the database
		if(open)
			s.openSlot();
		else
			s.closeSlot();

		s.book(participant);

		return s;
	}

        public List<TimeSlot> getSpecificTimeSlot(String accessCode, String month, String monthDay, String startingTime,
            String weekday, String year) throws Exception {
        if (month.equals("")) {
            month = null;
        }
        if (monthDay.equals("")) {
            monthDay = null;
        }
        if (weekday.equals("")) {
            weekday = null;
        }
        if (year.equals("")) {
            year = null;
        }
        if (startingTime.equals("")) {
            startingTime = null;
        }
        List<TimeSlot> allTimeSlots = new ArrayList<>();
        try {
            String query = "SELECT * FROM TimeSlot WHERE startTime =? AND idDays=? AND orgAccessCode=?";
            PreparedStatement ps = conn.prepareStatement(query);
            if (!startingTime.equals(null)) {
                ps.setString(1, startingTime);
            } else {
                ps.setString(1, "*");
            }
            ps.setString(3, accessCode);

            //for each timeslot found,
            //filter by all optional values
            //as part of date, and weekday too
            if (!month.equals(null) && !year.equals(null) &&
                    !monthDay.equals(null) && !weekday.equals(null)) { //all four values are given

            }
            else if (month.equals(null) && !year.equals(null) &&
                    !monthDay.equals(null) && !weekday.equals(null)) { //all values are given
                //except for month

            }
            else if (!month.equals(null) && year.equals(null) && !monthDay.equals(null) && !weekday.equals(null)) { //all values are given
                //except for year
            }
            else if (!month.equals(null) && !year.equals(null) && monthDay.equals(null) && !weekday.equals(null)) { //all values are given
                //except for monthDay
            }
            else if (!month.equals(null) && !year.equals(null) && !monthDay.equals(null) && weekday.equals(null)) { //all values are given
                java.sql.Date date = new Date(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(monthDay));
                date.toString();
                //except for weekDay
            }
            else if (month.equals(null) && year.equals(null) && !monthDay.equals(null) && !weekday.equals(null)) {
                //monthDay and weekDay given
            }
            else if (month.equals(null) && !year.equals(null) && monthDay.equals(null) && !weekday.equals(null)) {
                //year and weekDay given
            }
            else if (month.equals(null) && !year.equals(null) && !monthDay.equals(null) && weekday.equals(null)) {
                //year and monthDay given
            }
            else if (!month.equals(null) && !year.equals(null) && monthDay.equals(null) && weekday.equals(null)) {
                //month and year given
            }
            else if (!month.equals(null) && year.equals(null) && !monthDay.equals(null) && weekday.equals(null)) {
                //month and monthDay given
            }
            else if (!month.equals(null) && year.equals(null) && monthDay.equals(null) && !weekday.equals(null)) {
                //month and weekday given
            }

            else if (!month.equals(null) && year.equals(null) && monthDay.equals(null) && weekday.equals(null)) {
                //month given
            }
            else if (month.equals(null) && !year.equals(null) && monthDay.equals(null) && weekday.equals(null)) {
                //year given
            }
            else if (month.equals(null) && year.equals(null) && !monthDay.equals(null) && weekday.equals(null)) {
                //monthDay given
            }
            else if (month.equals(null) && year.equals(null) && monthDay.equals(null) && !weekday.equals(null)) {
                //weekday given
            }
            else { //no optional parameters for date or weekday given
            ps.setString(2, "*");
            }
            ResultSet resultSet = ps.executeQuery(query);
            while (resultSet.next()) {
                TimeSlot slot = generateTimeSlot(resultSet);
                allTimeSlots.add(slot);
            }
            resultSet.close();
            ps.close();
            return allTimeSlots;

        } catch (Exception e) {
            throw new Exception("Failed in getting time slots: " + e.getMessage());
        }
    }
}

