package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriberData {
	static final Logger logger = LoggerFactory.getLogger(SubscriberData.class);
	private DbManager dbManager;
	
	public SubscriberData(){
		dbManager = DbManager.getInstance();
	}
	
	public List<String> getSubscriberPhoneNumbersForMatch(Match matchId){
		List<String> users = null;
		try{
			users = dbManager.getUsersByMatch(matchId.getMatchId());
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
			dbManager.addCodeForUser(code, phoneNum);
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getLastCodeRequestTime(String phoneNum){
		String date = "2014-07-19 11:00:00", tmpDate;
		try{
			tmpDate = dbManager.getLatestCodeRequestDate(phoneNum);
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
			dbManager.addMatchForUser(matchId, phoneNum);
		}
		catch(SQLException ex) {
			logger.error(ex.toString());
		}
	}
	
	public boolean userSubscribedTo(String phoneNum, String matchId){
		boolean alreadySubscribed = false;
		try {
			alreadySubscribed = dbManager.matchIsAlreadyAddedForUser(matchId, phoneNum);
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		return alreadySubscribed;
	}
	
	public String getCodeByUser(String phoneNum){
		String code = "000000";
		try {
			code = dbManager.getCodeByUser(phoneNum);
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		return code;
	}
	
	public String getLastSentMsgDateByUser(String user){
		String date = null;
		try {
			date = dbManager.getLastSentMessageDate(user);
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		return date;
	}
	
	public int getSentMsgsTodayByUser(String user){
		int num = 0;
		try {
			num = dbManager.getQuantityOfSentMessagesForToday(user);
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		return num;
	}
	
	public void removeMatchForUser(String user, String matchId){
		try {
			dbManager.removeMatchForUser(matchId, user);
		} catch (SQLException e) {
			logger.error(e.toString());
		}
	}
	
}
