package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
	final String USER_LIST_BY_MATCH = "select user_num from match_user where match_id = ?";
	final String ADD_MATCH_FOR_USER = "insert into match_user (match_id, user_num) values(?,?)";
	final String CODE_BY_USER = "select code, time_stamp from user_code where user_num = ?";
	final String ADD_CODE_FOR_USER = "insert into user_code (code, user_num, time_stamp) values (?,?, datetime('now','localtime'))";
	final String SELECT_REQUEST_DATES_FOR_USR = "select time_stamp from user_code where user_num = ?"; 
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
	    if (stmt != null) try { stmt.close(); } catch (SQLException quiet) {}
	    if (conn != null) try { conn.close(); } catch (SQLException quiet) {}
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
	
	public void addMatchForUser(String matchId, String user) throws SQLException{
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(ADD_MATCH_FOR_USER);
		prepareStatement(stmt, matchId, user);
		
		stmt.executeUpdate();
	    if (stmt != null) try { stmt.close(); } catch (SQLException quiet) {}
	    if (conn != null) try { conn.close(); } catch (SQLException quiet) {}
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
	    if (stmt != null) try { stmt.close(); } catch (SQLException quiet) {}
	    if (conn != null) try { conn.close(); } catch (SQLException quiet) {}
		return code;
	}
	
	public void addCodeForUser(String code, String user) throws SQLException{
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(ADD_CODE_FOR_USER);
		prepareStatement(stmt, code, user);
		
		stmt.executeUpdate();
	    if (stmt != null) try { stmt.close(); } catch (SQLException quiet) {}
	    if (conn != null) try { conn.close(); } catch (SQLException quiet) {}
	}
	
//	private ResultSet execute(String query, String...args) throws SQLException{
//		PreparedStatement stmt = prepareStatement(query, args);
//		return stmt.executeQuery();
//	}
//	
//	private void update(String query, String...args) throws SQLException{
//		PreparedStatement stmt = prepareStatement(query, args);
//		stmt.executeUpdate();
//	}

	private void prepareStatement(PreparedStatement stmt, String...args) throws SQLException{
		for(int i=1; i<=args.length; i++){
			stmt.setString(i, args[i-1]);
		}
	}
}