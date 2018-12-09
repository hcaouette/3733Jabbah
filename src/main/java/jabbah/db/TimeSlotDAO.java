package jabbah.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
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
            ps.setString(3, slot.getParticipant());
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

        @SuppressWarnings("deprecation")
        public List<TimeSlot> getSpecificTimeSlot(String accessCode, String month, String monthDay, String startingTime,
            String weekday, String year) throws Exception {
        List<TimeSlot> allTimeSlots = new ArrayList<TimeSlot>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM TimeSlot";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                TimeSlot slot = generateTimeSlot(resultSet);
                //filter out any timeslots on the wrong day of week
                Calendar c = Calendar.getInstance();
                c.setTime(slot.getDate());
                int dayOfWeekNum = c.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeekNum != 1 && dayOfWeekNum != 7) {
                    //don't get timeslots for saturday and sunday
                    String dayOfWeek;
                    if (dayOfWeekNum == 2) {
                        dayOfWeek = "Monday";
                    } else if (dayOfWeekNum == 3) {
                        dayOfWeek = "Tuesday";
                    }
                    else if (dayOfWeekNum == 4) {
                        dayOfWeek = "Wednesday";
                    }
                    else if (dayOfWeekNum == 5) {
                        dayOfWeek = "Thursday";
                    }
                    else if (dayOfWeekNum == 6) {
                        dayOfWeek = "Friday";
                    }
                    else {
                        dayOfWeek = "Should never get here";
                    }
                  java.sql.Date dat = slot.getDate();
                  //create calander instance and get required params
                  Calendar cal = Calendar.getInstance();
                  cal.setTime(dat);
                  int monthh = cal.get(Calendar.MONTH) + 1;
                  int day = cal.get(Calendar.DAY_OF_MONTH);
                  int yearh = cal.get(Calendar.YEAR);
                  String yearZZ = Integer.toString(yearh);
                    if (accessCode.equals(slot.getOrgAccessCode()) &&
                            (weekday.equals("") || weekday.equals(dayOfWeek)) &&
                            (startingTime.equals("") || startingTime.equals(slot.getTime())) &&
                            (month.equals("") || month.equals(Integer.toString(monthh))) &&
                            (year.equals("") || year.equals(yearZZ)) &&
                            (monthDay.equals("") || monthDay.equals(Integer.toString(day))) &&
                                    (slot.isOpen())) {
                        //include only timeslots that fit the request
                        allTimeSlots.add(slot);
                    }
                }
            }
            resultSet.close();
            statement.close();
            return allTimeSlots;

        } catch (Exception e) {
            throw new Exception("Failed in getting time slots: " + e.getMessage());
        }
    }
}

