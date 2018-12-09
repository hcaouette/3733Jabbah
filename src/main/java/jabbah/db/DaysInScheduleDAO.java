package jabbah.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jabbah.model.DaysInSchedule;

public class DaysInScheduleDAO {

	java.sql.Connection conn;

	public DaysInScheduleDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
	}

    public DaysInSchedule getDay(Date day, String id) throws Exception {

        try {
            DaysInSchedule date = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM DaysInSchedule WHERE idDays=? AND orgAccessCode =?;");
            ps.setDate(1,  day);
            ps.setString(2, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                date = generateDate(resultSet);
            }
            resultSet.close();
            ps.close();

            return date;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting a day in schedule: " + e.getMessage());
        }
    }
   
    public List<DaysInSchedule> getWeek(Date day, String id) throws Exception{
    	List<DaysInSchedule> weekOfDays = new ArrayList<>();
    	Calendar Cal = Calendar.getInstance();
    	Cal.setTime(day);
    	Date sqlDay;
    	sqlDay = new java.sql.Date(Cal.getTimeInMillis());
    	PreparedStatement ps = conn.prepareStatement("SELECT * FROM DaysInSchedule WHERE idDays=? AND orgAccessCode =?;");
    	try {
    		// Calendar goes from Sunday to Saturday 1-7
    		
    		int dow = Cal.get(Calendar.DAY_OF_WEEK);
    		Cal.roll(Calendar.DAY_OF_WEEK, -(dow-2));
    		
    		/*int firstCount = 0;
    		int numMissingDays =0;
    		while(firstCount<5)
    		{
    			ps = conn.prepareStatement("SELECT * FROM DaysInSchedule WHERE idDays=? AND orgAccessCode =?;");
    			
    			sqlday.setTime(Cal.getTimeInMillis());
    			CheckCal.setTime(sqlday);
    			Cal.roll(Calendar.DAY_OF_WEEK, 1);
    			firstCount++;
    			if(!ps.execute())
    			{
    				numMissingDays = 0;
    			}
    			
    		}*/
    		int count = 0;
    		while(count<5)
    		{
    		sqlDay.setTime(Cal.getTimeInMillis());
    		ps = conn.prepareStatement("SELECT * FROM DaysInSchedule WHERE idDays=? AND orgAccessCode =?;");
       		count++;
    		ps.setDate(1, sqlDay);
    		ps.setString(2, id);
    		//Always returns true but is handled later
    		if(ps.execute())
    		{
    		weekOfDays.add(getDay(sqlDay, id));    	
    		}
    		Cal.roll(Calendar.DAY_OF_WEEK, 1);
            }
            return weekOfDays;
    		}
    	catch (Exception e) {
    		throw new Exception ("Failed to make a week of days: " + e.getMessage());
    	}
    	
    }
    public boolean deleteDay(DaysInSchedule day) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM DaysInSchedule WHERE orgAccessCode=?;");
            ps.setString(1, day.getScheduleID());
            int numAffected = ps.executeUpdate();
            ps.close();

            return numAffected >= 1;

        } catch (Exception e) {
            throw new Exception("Failed to insert day: " + e.getMessage());
        }
    }

    public boolean addDay(DaysInSchedule day) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM DaysInSchedule WHERE idDays=? AND orgAccessCode=?;");
            ps.setDate(1, day.getDate());
            ps.setString(2, day.getScheduleID());
            ResultSet resultSet = ps.executeQuery();

            // already present?
            while (resultSet.next()) {
                DaysInSchedule d = generateDate(resultSet);
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO DaysInSchedule (idDays,orgAccessCode) values(?,?);");
            ps.setDate(1,  day.getDate());
            ps.setString(2,  day.getScheduleID());
            ps.execute();
            ps.close();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to insert the day: " + e.getMessage());
        }
    }

    public List<DaysInSchedule> getAllDays() throws Exception {

        List<DaysInSchedule> allDays = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM DaysInSchedule";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                DaysInSchedule d = generateDate(resultSet);
                allDays.add(d);
            }
            resultSet.close();
            statement.close();
            return allDays;

        } catch (Exception e) {
            throw new Exception("Failed in getting days: " + e.getMessage());
        }
    }

    private DaysInSchedule generateDate(ResultSet resultSet) throws Exception {
        Date day  = resultSet.getDate("idDays");
        String id = resultSet.getString("orgAccessCode");
        return new DaysInSchedule(day, id);
    }
}
