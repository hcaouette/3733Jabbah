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
			PreparedStatement ps = conn.prepareStatement("DELETE FROM TimeSlot WHERE startTime=? AND idDays=? AND orgAccessCode=?;");
			ps.setString(1, slot.getTime());
			ps.setDate(2, slot.getDate());
			ps.setString(3, slot.getScheduleID());
			int numAffected = ps.executeUpdate();

			return (numAffected == 1);

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

        	query = "UPDATE TimeSlot SET participant=? WHERE startTime=? AND idDays=? AND orgAccessCode=?;";
        	ps = conn.prepareStatement(query);
            ps.setString(1, slot.getParticipant());
            ps.setString(2, slot.getTime());
            ps.setDate(3, slot.getDate());
            ps.setString(4, slot.getScheduleID());
            int numAffected2 = ps.executeUpdate();
            ps.close();

            return (numAffected == 1 && numAffected2 == 1);
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

		return new TimeSlot(startTime, duration, day, id);
	}

}
