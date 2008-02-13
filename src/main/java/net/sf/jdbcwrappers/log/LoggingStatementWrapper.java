package net.sf.jdbcwrappers.log;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.jdbcwrappers.StatementWrapper;

public class LoggingStatementWrapper extends StatementWrapper {
	private final Logger logger;
	
	public LoggingStatementWrapper(Logger logger) {
		this.logger = logger;
	}

	private void log(String method, String sql) {
		logger.log(this, method, sql);
	}
	
	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		log("Statement#execute", sql);
		return super.execute(sql, autoGeneratedKeys);
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		log("Statement#execute", sql);
		return super.execute(sql, columnIndexes);
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		log("Statement#execute", sql);
		return super.execute(sql, columnNames);
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		log("Statement#execute", sql);
		return super.execute(sql);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		log("Statement#executeQuery", sql);
		return super.executeQuery(sql);
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		log("Statement#executeUpdate", sql);
		return super.executeUpdate(sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		log("Statement#executeUpdate", sql);
		return super.executeUpdate(sql, columnIndexes);
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		log("Statement#executeUpdate", sql);
		return super.executeUpdate(sql, columnNames);
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		log("Statement#executeUpdate", sql);
		return super.executeUpdate(sql);
	}
}
