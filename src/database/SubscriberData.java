package database;

import java.util.ArrayList;
import java.util.List;

import model.Match;

public class SubscriberData {
	public List<String> getSubscriberPhoneNumbersForMatch(Match matchId){
		List<String> list = new ArrayList<String>();
		//TODO: implement database methods
		list.add("+995595150038");
		return list;
	}
	
	public void saveCodeForUser(String phoneNum, String code) {
		
	}
	
	public String getLastCodeRequestTime(String phoneNum){
		return "2014-07-19 11:00:00";
	}
	
	public void saveMatchForUser(String phoneNum, String matchId){
		
	}
}
