package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbManager {
	final static Logger logger = LoggerFactory.getLogger(DbManager.class);
	final String INSERT_USER_INTO_MESSAGES_1 = "insert or replace into message_user (id, user_num, quantity, last_date) values(coalesce((select id from message_user where user_num=?),null), ?, coalesce((select quantity from message_user where user_num=?)+1, 1), datetime('now', 'localtime'))";
	final String USER_LIST_BY_MATCH = "select match_user.user_num from match_user left join message_user on message_user.user_num = match_user.user_num where match_user.match_id = ? and ifnull(message_user.quantity,0) < ?";
	final String ADD_MATCH_FOR_USER = "insert into match_user (match_id, user_num) values(?,?)";
	final String CODE_BY_USER = "select code, time_stamp from user_code where user_num = ?";
	final String ADD_CODE_FOR_USER = "insert or replace into user_code (id, code, user_num, time_stamp) values ((select id from user_code where user_num = ?), ?, ?, datetime('now','localtime'))";
	final String SELECT_REQUEST_DATES_FOR_USR = "select time_stamp from user_code where user_num = ?";
	final String SELECT_MATCH_BY_ID_FOR_USER = "select * from match_user where match_id = ? and user_num = ?";
	final String DELETE_MATCH_FOR_USER = "delete from match_user where match_id = ? and user_num = ?";
	final String DELETE_ALL_MSG_COUNTS = "delete from message_user";

	private static DbManager dbManager;

	public static DbManager getInstance() {
		if (dbManager == null) {
			dbManager = new DbManager();
		}
		return dbManager;
	}

	private DbManager() {

	}

	public String getLatestCodeRequestDate(String userPhoneNumber)
			throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn
				.prepareStatement(SELECT_REQUEST_DATES_FOR_USR);
		prepareStatement(stmt, userPhoneNumber);
		ResultSet rSet = stmt.executeQuery();

		String date = null;
		if (rSet.next()) {
			date = rSet.getString("time_stamp");
		}
		closeQuietly(conn, stmt, rSet);
		return date;
	}

	public List<String> getUsersByMatch(String matchId, int msgLimit) throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(USER_LIST_BY_MATCH);
		prepareStatement(stmt, matchId, msgLimit);

		ResultSet rSet = stmt.executeQuery();

		String user;
		List<String> users = new ArrayList<String>();
		while (rSet.next()) {
			user = rSet.getString("user_num");
			users.add(user);
		}
		closeQuietly(conn, stmt, rSet);
		return users;
	}

	public boolean matchIsAlreadyAddedForUser(String matchId, String user)
			throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn
				.prepareStatement(SELECT_MATCH_BY_ID_FOR_USER);
		prepareStatement(stmt, matchId, user);
		ResultSet rSet = stmt.executeQuery();

		boolean found = rSet.next();
		closeQuietly(conn, stmt, rSet);
		return found;
	}

	public void addMatchForUser(String matchId, String user)
			throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(ADD_MATCH_FOR_USER);
		prepareStatement(stmt, matchId, user);

		stmt.executeUpdate();
		closeQuietly(conn, stmt, null);
	}

	public void removeMatchForUser(String matchId, String user)
			throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(DELETE_MATCH_FOR_USER);
		prepareStatement(stmt, matchId, user);

		stmt.executeUpdate();
		closeQuietly(conn, stmt, null);
	}

	public String getCodeByUser(String user) throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(CODE_BY_USER);
		prepareStatement(stmt, user);
		ResultSet rSet = stmt.executeQuery();

		String code = null;
		if (rSet.next()) {
			code = rSet.getString("code");
		}
		closeQuietly(conn, stmt, null);
		return code;
	}

	public void addCodeForUser(String code, String user) throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(ADD_CODE_FOR_USER);
		prepareStatement(stmt, user, code, user);

		stmt.executeUpdate();
		closeQuietly(conn, stmt, null);
	}

	private void prepareStatement(PreparedStatement stmt, Object... args)
			throws SQLException {
		for (int i = 1; i <= args.length; i++) {
			Object o = args[i - 1];
			if(o instanceof String) stmt.setString(i, (String) o);
			if(o instanceof Integer) stmt.setInt(i, (int) o);
		}
	}

	public void countMessageForUser(String userPhoneNumber)
			throws SQLException {
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = conn.prepareStatement(INSERT_USER_INTO_MESSAGES_1);
		prepareStatement(stmt, userPhoneNumber, userPhoneNumber, userPhoneNumber);

		stmt.executeUpdate();
		closeQuietly(conn, stmt, null);
	}
	
	public void clearTables(){
		Connection conn = ConnectionPooler.GetConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_ALL_MSG_COUNTS);
			stmt.execute();
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		closeQuietly(conn, stmt, null);
	}

	private void closeQuietly(Connection conn, PreparedStatement stmt,
			ResultSet rSet) {
		if (rSet != null)
			try {
				rSet.close();
			} catch (SQLException quiet) {
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException quiet) {
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException quiet) {
			}
	}

}