package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
	final String userListByMatch = "select user_num from match_user where match_id = ?";
	final String addMatchForUser = "insert into match_user (match_id, user_num) values(?,?)";
	final String userByCode = "select code, time_stamp from user_code where user_num = ?";
	final String addCodeForUser = "insert into user_code (code, user_num, time_stamp) values(?,?, datetime('now','localtime'))";
	private static DbManager dbManager;
	
	public static DbManager getInstance(){
		if(dbManager == null){
			dbManager = new DbManager();
		}
		return dbManager;
	}
	
	private DbManager(){
		
	}
	
	public ResultSet getUsersByMatch(String matchId) throws SQLException{
		return execute(userListByMatch, matchId);
	}
	
	public void addMatchForUser(String matchId, String user) throws SQLException{
		update(addMatchForUser, matchId, user);
	}
	
	public ResultSet getCodeByUser(String user) throws SQLException{
		return execute(userByCode, user);
	}
	
	public void addCodeForUser(String code, String user) throws SQLException{
		update(addCodeForUser, code, user);
	}
	
	private ResultSet execute(String query, String...args) throws SQLException{
		PreparedStatement stmt = prepareStatement(query, args);
		return stmt.executeQuery();
	}
	
	private void update(String query, String...args) throws SQLException{
		PreparedStatement stmt = prepareStatement(query, args);
		stmt.executeUpdate();
	}

	private PreparedStatement prepareStatement(String query, String...args) throws SQLException{
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		for(int i=1;i<=args.length;i++){
			stmt.setString(i, args[i-1]);
		}
		return stmt;
	}
	
	public static void main(String[] args) {
		ConnectionPooler.InitializePooler();
		
        try {
            Connection conn = ConnectionPooler.GetConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS foo (x INTEGER, y INTEGER)");
            stmt.execute("INSERT INTO foo VALUES(1,1)");
            
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                conn.close();
            }
            
            ConnectionPooler.ReleaseConnection(conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ConnectionPooler.ReleasePooler();
    }
}