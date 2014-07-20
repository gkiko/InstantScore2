package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Match;

public class SubscriberData {
	public List<String> getSubscriberPhoneNumbersForMatch(Match matchId){
		List<String> list = new ArrayList<String>();
		try{
			ResultSet rset = DbManager.getInstance().getUsersByMatch(matchId.getMatchId());
			while(rset.next()) {
				String phoneNumber = rset.getString("user_num");
				list.add("'"+phoneNumber+"'");
			}
		}
		catch(SQLException ex) {
			list.add("'+995595150038'");
		}
		return list;
	}
	
	public void saveCodeForUser(String phoneNum, String code) {
		try {
			DbManager.getInstance().addCodeForUser("'"+code+"'", "'"+phoneNum+"'");
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getLastCodeRequestTime(String phoneNum){
		try{
			String date = DbManager.getInstance().getLatestCodeRequestDate("'"+phoneNum+"'");
			return date;
		}
		catch(SQLException ex) {
			// default value
			return "2014-07-19 11:00:00";
		}
	}
	
	public void saveMatchForUser(String phoneNum, String matchId){
		try {
			DbManager.getInstance().addMatchForUser(matchId, "'"+phoneNum+"'");
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DbManager db = DbManager.getInstance();
		try{
			db.addCodeForUser("CODE", "GELA_NUMBER");
			db.addCodeForUser("CODE2", "GELA_NUMBER2");
		}
		catch(SQLException ex) {
			
		}
	}
	
}
