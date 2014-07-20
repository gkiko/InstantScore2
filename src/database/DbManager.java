package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DbManager {
	final String USER_LIST_BY_MATCH = "select user_num from match_user where match_id = ?";
	final String ADD_MATCH_FOR_USER = "insert into match_user (match_id, user_num) values(?,?)";
	final String USER_BY_CODE = "select code, time_stamp from user_code where user_num = ?";
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
		ConnectionPooler.InitializePooler();
	}
	
	public String getLatestCodeRequestDate(String userPhoneNumber) throws SQLException {
		ResultSet rset = execute(SELECT_REQUEST_DATES_FOR_USR, userPhoneNumber);
		if(rset.next()) {
			String date = rset.getString("time_stamp");
			return date;
		}
		return null;
	}
	
	public ResultSet getUsersByMatch(String matchId) throws SQLException{
		return execute(USER_LIST_BY_MATCH, matchId);
	}
	
	public void addMatchForUser(String matchId, String user) throws SQLException{
		update(ADD_MATCH_FOR_USER, matchId, user);
	}
	
	public ResultSet getCodeByUser(String user) throws SQLException{
		return execute(USER_BY_CODE, user);
	}
	
	public void addCodeForUser(String code, String user) throws SQLException{
		update(ADD_CODE_FOR_USER, code, user);
	}
	
	private ResultSet execute(String query, String...args) throws SQLException{
		System.out.println("executing...");
		PreparedStatement stmt = prepareStatement(query, args);
		System.out.println("went down...");
		return stmt.executeQuery();
	}
	
	private void update(String query, String...args) throws SQLException{
		System.out.println("UPDATE "+query+" "+Arrays.toString(args));
		PreparedStatement stmt = prepareStatement(query, args);
		stmt.executeUpdate();
	}

	private PreparedStatement prepareStatement(String query, String...args) throws SQLException{
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		for(int i=1; i<=args.length; i++){
			stmt.setString(i, args[i-1]);
		}
		return stmt;
	}
	
	public static void main(String[] args) {
//		ConnectionPooler.InitializePooler();
//		
//        try {
//        	
//            Connection conn = ConnectionPooler.GetConnection();
//            Statement stmt = conn.createStatement();
//            stmt.execute("CREATE TABLE IF NOT EXISTS foo (x INTEGER, y INTEGER)");
//            stmt.execute("INSERT INTO foo VALUES(1,1)");
//            
//            if (conn != null) {
//                System.out.println("Connected to the database");
//                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
//                System.out.println("Driver name: " + dm.getDriverName());
//                System.out.println("Driver version: " + dm.getDriverVersion());
//                System.out.println("Product name: " + dm.getDatabaseProductName());
//                System.out.println("Product version: " + dm.getDatabaseProductVersion());
//                conn.close();
//            }
		try{
			ConnectionPooler.InitializePooler();
			System.out.println("updating...");
            DbManager.getInstance().update("insert into user_code (code, user_num, time_stamp) values (?,?, datetime('now','localtime'))", "'code'", "'phone_num'");
            
		}
		catch(SQLException ex) {
			
		}
//            ConnectionPooler.ReleaseConnection(conn);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        ConnectionPooler.ReleasePooler();
    }
}