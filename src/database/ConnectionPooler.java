package database;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class ConnectionPooler {
	final static Logger logger = LoggerFactory.getLogger(ConnectionPooler.class);
	// /Users/Lasha/Documents/workEE/InstantScore2/product.db
	// /Users/gkiko/Workspace/InstantScore2/product.db
	// /racxa/product.db
	final static String dbURL = "jdbc:sqlite:/racxa/product.db";
	private static BoneCP connectionPool;

	public static void InitializePooler() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception ex) {
			logger.error("failed", ex);
		}

		try {
			BoneCPConfig config = new BoneCPConfig();

			config.setJdbcUrl(dbURL);

			// config.setUsername("dblogin");

			// config.setPassword("dbpassword");

			config.setMinConnectionsPerPartition(5);

			config.setMaxConnectionsPerPartition(20);

			config.setPartitionCount(1);

			connectionPool = new BoneCP(config);
		} catch (Exception ex) {
			logger.error("failed", ex);
		}
	}

	public static void ReleasePooler() {
		if (connectionPool != null) {
			connectionPool.shutdown();
		}
	}

	public static Connection GetConnection() {
		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}

		return connection;
	}

	public static void ReleaseConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception ex) {
			logger.error("failed", ex);
		}
	}
}