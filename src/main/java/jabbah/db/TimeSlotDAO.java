package jabbah.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jabbah.model.*;

public class TimeSlotDAO {
	
	java.sql.Connection conn;
	
	public TimeSlotDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
	}
	
	public TimeSlot getTimeSlot(String time) throws Exception{
		try {
			TimeSlot slot = null;
	        PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlot WHERE startTime=?;");
	        ps.setString(1,  time);
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
			PreparedStatement ps = conn.prepareStatement("DELETE FROM TimeSlot WHERE startTime=?;");
			ps.setString(1, slot.getTime());
			int numAffected = ps.executeUpdate();
			
			return (numAffected == 1);
			
		} catch (Exception e) {
			throw new Exception("Failed to delete time slot: " + e.getMessage());
		}
	}
	
	public boolean updateTimeSlot(TimeSlot slot) throws Exception {
        try {
        	String query = "UPDATE TimeSlot SET isOpen=? WHERE startTime=?;";
        	PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, slot.open());
            ps.setString(2, slot.getTime());
            int numAffected = ps.executeUpdate();
            ps.close();
            
        	query = "UPDATE TimeSlot SET participantID=? WHERE startTime=?;";
        	ps = conn.prepareStatement(query);
            ps.setString(1, slot.getParticipant().getCode());
            ps.setString(2, slot.getTime());
            int numAffected2 = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1 && numAffected2 == 1);
        } catch (Exception e) {
            throw new Exception("Failed to update time slot: " + e.getMessage());
        }
    }
    
    public boolean addTimeSlot(TimeSlot slot, Date day, String ScheduleID) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeSlot WHERE startTime = ?;");
            ps.setString(1, slot.getTime());
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            TimeSlot s;
            while (resultSet.next()) {
                s = generateTimeSlot(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO TimeSlot (startTime, duration, participantID, isOpen, idDays, ScheduleID) values(?,?,?,?,?,?);");
            ps.setString(1,  slot.getTime());
            ps.setInt(2,  slot.getDuration());
            ps.setString(3, slot.getParticipant().getCode());
            ps.setBoolean(4,  slot.open());
            ps.setDate(5, day);
            ps.setString(6,  ScheduleID);
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
		
		return new TimeSlot(startTime, duration);
	}

}
