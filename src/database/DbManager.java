package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbManager {
	final String LAST_MESSAGE_DATE = "select last_date from message_user where user_num = ?";
	final String SENT_MESSAGES_QUANTITY = "select quantity from message_user where user_num = ?";
	final String UPDATE_MESSAGES_QUANTITY = "update message_user set quantity = ? , last_date = ?  where user_num = ?";
	final String INSERT_USER_INTO_MESSAGES = "insert into message_user (user_num, quantity, last_date) values (?, 1, ?)";
	final String USER_LIST_BY_MATCH = "select user_num from match_user where match_id = ?";
	final String ADD_MATCH_FOR_USER = "insert into match_user (match_id, user_num) values(?,?)";
	final String CODE_BY_USER = "select code, time_stamp from user_code where user_num = ?";
	final String ADD_CODE_FOR_USER = "insert into user_code (code, user_num, time_stamp) values (?,?, datetime('now','localtime'))";
	final String SELECT_REQUEST_DATES_FOR_USR = "select time_stamp from user_code where user_num = ?";
	final String SELECT_MATCH_BY_ID_FOR_USER = "select * from match_user where match_id = ? and user_num = ?";
	
	private static DbManager dbManager;
	
	public static DbManager getInstance(){
		if(dbManager == null){
			dbManager = new DbManager();
		}
		return dbManager;
	}
	
	private DbManager(){

	}
	
	public String getLatestCodeRequestDate(String userPhoneNumber) throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(SELECT_REQUEST_DATES_FOR_USR);
		prepareStatement(stmt, userPhoneNumber);
		ResultSet rSet = stmt.executeQuery();
		
		String date = null;
		if(rSet.next()) {
			date = rSet.getString("time_stamp");
		}
		if (rSet != null) try { rSet.close(); } catch (SQLException quiet) {}
	    closeConnectionAndStatement(conn, stmt);
		return date;
	}
	
	public List<String> getUsersByMatch(String matchId) throws SQLException{
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(USER_LIST_BY_MATCH);
		prepareStatement(stmt, matchId);
		ResultSet rSet = stmt.executeQuery();
		
		String user;
		List<String> users = new ArrayList<String>();
		while(rSet.next()) {
			user = rSet.getString("user_num");
			users.add(user);
		}
		return users;
	}
	
	public boolean matchIsAlreadyAddedForUser(String matchId, String user) throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(SELECT_MATCH_BY_ID_FOR_USER);
		prepareStatement(stmt, matchId, user);
		
		ResultSet rSet = stmt.executeQuery();
		boolean found = rSet.next();
		closeConnectionAndStatement(conn, stmt);
		try { rSet.close(); } catch(SQLException quiet) {}
		return found;
	}
	
	public void addMatchForUser(String matchId, String user) throws SQLException{
		if(matchIsAlreadyAddedForUser(matchId, user)) {
			return; // no need to add the same match again
		}
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(ADD_MATCH_FOR_USER);
		prepareStatement(stmt, matchId, user);
		
		stmt.executeUpdate();
		closeConnectionAndStatement(conn, stmt);
	}
	
	public String getCodeByUser(String user) throws SQLException{
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(CODE_BY_USER);
		prepareStatement(stmt, user);
		ResultSet rSet = stmt.executeQuery();
		
		String code = null;
		if(rSet.next()){
			code = rSet.getString("code");
		}
		if (rSet != null) try { rSet.close(); } catch (SQLException quiet) {}
		closeConnectionAndStatement(conn, stmt);
		return code;
	}
	
	public void addCodeForUser(String code, String user) throws SQLException{
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(ADD_CODE_FOR_USER);
		prepareStatement(stmt, code, user);
		
		stmt.executeUpdate();
		closeConnectionAndStatement(conn, stmt);
	}
	
	private void prepareStatement(PreparedStatement stmt, String...args) throws SQLException{
		for(int i=1; i<=args.length; i++){
			stmt.setString(i, args[i-1]);
		}
	}
	
	private String getLastSentMessageDate(String phoneNumber) throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(LAST_MESSAGE_DATE);
		prepareStatement(stmt, "'"+phoneNumber+"'");
		
		ResultSet rSet = stmt.executeQuery();
		String lastDate = null;
		if(rSet.next()) {
			lastDate = rSet.getString("last_date");
		}
	    closeConnectionAndStatement(conn, stmt);
	    try { rSet.close(); } catch (SQLException quiet) {}
		return lastDate;
	}
	
	private void incrementMessageForUser(String userPhoneNumber) throws SQLException {
		int numSent = getQuantityOfSentMessagesForToday(userPhoneNumber);
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt;
		if(numSent==0) {
			System.out.println("inserting for the first time "+userPhoneNumber);
			stmt = conn.prepareStatement(INSERT_USER_INTO_MESSAGES);
			prepareStatement(stmt, "'"+userPhoneNumber+"'", getCurrentDayDate());
		}
		else {
			System.out.println("updating already existing one for "+userPhoneNumber+" with "+(numSent+1));
			numSent++;
			stmt = conn.prepareStatement(UPDATE_MESSAGES_QUANTITY);
			prepareStatement(stmt, ""+numSent, getCurrentDayDate(), "'"+userPhoneNumber+"'");
		}
		
		stmt.executeUpdate();
		closeConnectionAndStatement(conn, stmt);
	}
	
	private boolean areSameDays(String date1, String date2) {
		if(date1==null || date2==null) {
			return false;
		}
		return date1.equals(date2);
	}
	
	private int getQuantityOfSentMessagesForToday(String phoneNumber) throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(SENT_MESSAGES_QUANTITY);
		prepareStatement(stmt, "'"+phoneNumber+"'");
		
		ResultSet rset = stmt.executeQuery();
		int quantity = 0;
	    if(rset.next()) {
	    	quantity = Integer.parseInt(rset.getString("quantity"));
	    }
	    
	    closeConnectionAndStatement(conn, stmt);
	    try { rset.close(); } catch(SQLException quiet) { }
		
	    return quantity;
	}
	
	public boolean isMessageLimitFullForUser(String userPhoneNumber) {
		try{
			String lastMessageDate = getLastSentMessageDate(userPhoneNumber);
			String todayDate = "";
			int numSent = getQuantityOfSentMessagesForToday(userPhoneNumber);
			if((lastMessageDate==null || areSameDays(todayDate, lastMessageDate)) && numSent < notifier.MsgSender.MESSAGE_LIMIT_PER_DAY) {
				return false;
			}
		}
		catch(SQLException quiet) {
			return false;
		}
		return true;
	}
	
	public boolean checkForMessageLimitAndUpdate(String userPhoneNumber) {
		try{
			String lastMessageDate = getLastSentMessageDate(userPhoneNumber);
			String todayDate = "";
			int numSent = getQuantityOfSentMessagesForToday(userPhoneNumber);
			if((lastMessageDate==null || areSameDays(todayDate, lastMessageDate)) && numSent < notifier.MsgSender.MESSAGE_LIMIT_PER_DAY) {
				incrementMessageForUser(userPhoneNumber);
				return true;
			}
		}
		catch(SQLException quiet) {
			return true;
		}
		return false;
	}
	
	public String getCurrentDayDate() throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement("select datetime('now', 'localtime')");
		prepareStatement(stmt);
		
		ResultSet rset = stmt.executeQuery();
		String date = null;
	    if(!rset.next()) {
	    	try{ rset.close(); } catch(SQLException quiet) {}
	    	closeConnectionAndStatement(conn, stmt);
			return null;
		}
	    date = rset.getString(1);
	    date = date.substring(0, date.indexOf(" ")); // for example, changes 2014-07-23 16:19 to just 2014-07-23

	    closeConnectionAndStatement(conn, stmt);
		try{ rset.close(); } catch(SQLException quiet) {}
		
		return date;
	}
	
	private void closeConnectionAndStatement(Connection conn, PreparedStatement stmt) {
		if (stmt != null) try { stmt.close(); } catch (SQLException quiet) {}
	    if (conn != null) try { conn.close(); } catch (SQLException quiet) {}
	}
	
	public static void main(String[] args) throws SQLException, InterruptedException {
		System.out.println(new Date().toString());
    	ConnectionPooler.InitializePooler();
    	for(int i=0; i<3; i++)
    		DbManager.getInstance().addMatchForUser("a vs b", "+995");
	}
	
}