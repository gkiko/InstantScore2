package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Utils;

public class SubscriberData {
	static final Logger logger = LoggerFactory.getLogger(SubscriberData.class);
	
	public List<String> getSubscriberPhoneNumbersForMatch(Match matchId){
		List<String> users = null;
		try{
			users = DbManager.getInstance().getUsersByMatch(matchId.getMatchId());
		}
		catch(SQLException ex) {
			logger.error(ex.toString());
		}
		if(users == null){
			users = new ArrayList<String>();
		}
		return users;
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
		String date = "2014-07-19 11:00:00", tmpDate;
		try{
			tmpDate = DbManager.getInstance().getLatestCodeRequestDate("'"+phoneNum+"'");
			if (tmpDate != null) {
				date = tmpDate;
			}
		}
		catch(SQLException ex) {
			logger.error(ex.toString());
		}
		return date;
	}
	
	public void saveMatchForUser(String phoneNum, String matchId){
		try {
			DbManager.getInstance().addMatchForUser(matchId, "'"+phoneNum+"'");
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getCodeByUser(String phoneNum){
		String code = "000000";
		try {
			code = DbManager.getInstance().getCodeByUser("'"+phoneNum+"'");
			code = Utils.removeQuotesAround(code);
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		return code;
	}

}
