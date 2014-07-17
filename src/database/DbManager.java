package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
	private static DbManager dbManager;

	public static DbManager getInstance() {
		if (dbManager == null) {
			dbManager = new DbManager();
		}
		return dbManager;
	}

	private DbManager() {

	}

	public static void main(String[] args) {
		ConnectionPooler.InitializePooler();

		try {
			Connection conn = ConnectionPooler.GetConnection();
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS foo (x INTEGER, y INTEGER)");
			stmt.execute("INSERT INTO foo VALUES(1,1)");

			System.out.println("Connected to the database");
			DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
			System.out.println("Driver name: " + dm.getDriverName());
			System.out.println("Driver version: " + dm.getDriverVersion());
			System.out.println("Product name: " + dm.getDatabaseProductName());
			System.out.println("Product version: " + dm.getDatabaseProductVersion());
			conn.close();

			ConnectionPooler.ReleaseConnection(conn);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		ConnectionPooler.ReleasePooler();
	}
}
