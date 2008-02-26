/**
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sf.jdbcwrappers;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Base class for {@link PreparedStatement} wrappers.
 * 
 * @author Andreas Veithen
 * @version $Id$
 */
public class PreparedStatementWrapper extends AbstractWrapper implements PreparedStatement, HasConnection {
	private WrapperFactory wrapperFactory;
	private StatementWrapper statementWrapper;
	private PreparedStatement parent;
	private String sql;
	
	final void init(WrapperFactory wrapperFactory, ConnectionWrapper connectionWrapper, PreparedStatement parent, String sql) throws SQLException {
		this.wrapperFactory = wrapperFactory;
		statementWrapper = wrapperFactory.wrapStatement(connectionWrapper, parent);
		this.parent = parent;
		this.sql = sql;
		init();
	}
	
	/**
	 * Get the SQL statement used to construct the {@link PreparedStatement} object. 
	 * 
	 * @return the SQL statement
	 */
	protected final String getSql() {
		return sql;
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#addBatch(String sql)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void addBatch(String sql) throws SQLException {
		statementWrapper.addBatch(sql);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#cancel()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void cancel() throws SQLException {
		statementWrapper.cancel();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#clearBatch()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void clearBatch() throws SQLException {
		statementWrapper.clearBatch();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#clearWarnings()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void clearWarnings() throws SQLException {
		statementWrapper.clearWarnings();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#close()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void close() throws SQLException {
		statementWrapper.close();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#execute(String, int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return statementWrapper.execute(sql, autoGeneratedKeys);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#execute(String, int[]}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return statementWrapper.execute(sql, columnIndexes);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#execute(String, String[]}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return statementWrapper.execute(sql, columnNames);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#execute(String)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public boolean execute(String sql) throws SQLException {
		return statementWrapper.execute(sql);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeBatch()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int[] executeBatch() throws SQLException {
		return statementWrapper.executeBatch();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeQuery(String)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return wrapperFactory.wrapResultSet(ResultSetType.QUERY, this, statementWrapper.executeQuery(sql));
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeUpdate(String, int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return statementWrapper.executeUpdate(sql, autoGeneratedKeys);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeUpdate(String, int[])}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return statementWrapper.executeUpdate(sql, columnIndexes);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeUpdate(String, String[])}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return statementWrapper.executeUpdate(sql, columnNames);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeUpdate(String)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int executeUpdate(String sql) throws SQLException {
		return statementWrapper.executeUpdate(sql);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getConnection()}.
	 * This method returns the {@link ConnectionWrapper} object that
	 * created this wrapper. For consistency reasons, it can't be
	 * overridden.
	 */
	public final Connection getConnection() throws SQLException {
		return statementWrapper.getConnection();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getFetchDirection()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getFetchDirection() throws SQLException {
		return statementWrapper.getFetchDirection();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getFetchSize()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getFetchSize() throws SQLException {
		return statementWrapper.getFetchSize();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getGeneratedKeys()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		return statementWrapper.getGeneratedKeys();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getMaxFieldSize()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getMaxFieldSize() throws SQLException {
		return statementWrapper.getMaxFieldSize();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getMaxRows()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getMaxRows() throws SQLException {
		return statementWrapper.getMaxRows();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getMoreResults()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public boolean getMoreResults() throws SQLException {
		return statementWrapper.getMoreResults();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getMoreResults(int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public boolean getMoreResults(int current) throws SQLException {
		return statementWrapper.getMoreResults(current);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getQueryTimeout()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getQueryTimeout() throws SQLException {
		return statementWrapper.getQueryTimeout();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getResultSet()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public ResultSet getResultSet() throws SQLException {
		return statementWrapper.getResultSet();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getResultSetConcurrency()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getResultSetConcurrency() throws SQLException {
		return statementWrapper.getResultSetConcurrency();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getResultSetHoldability()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getResultSetHoldability() throws SQLException {
		return statementWrapper.getResultSetHoldability();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getResultSetType()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getResultSetType() throws SQLException {
		return statementWrapper.getResultSetType();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getUpdateCount()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public int getUpdateCount() throws SQLException {
		return statementWrapper.getUpdateCount();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getWarnings()}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public SQLWarning getWarnings() throws SQLException {
		return statementWrapper.getWarnings();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setCursorName(String)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void setCursorName(String name) throws SQLException {
		statementWrapper.setCursorName(name);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setEscapeProcessing(boolean)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		statementWrapper.setEscapeProcessing(enable);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setFetchDirection(int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void setFetchDirection(int direction) throws SQLException {
		statementWrapper.setFetchDirection(direction);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setFetchSize(int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void setFetchSize(int rows) throws SQLException {
		statementWrapper.setFetchSize(rows);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setMaxFieldSize(int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		statementWrapper.setMaxFieldSize(max);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setMaxRows(int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void setMaxRows(int max) throws SQLException {
		statementWrapper.setMaxRows(max);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setQueryTimeout(int)}.
	 * This method delegates to the linked {@link StatementWrapper} object.
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		statementWrapper.setQueryTimeout(seconds);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#addBatch()}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void addBatch() throws SQLException {
		parent.addBatch();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#clearParameters()}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void clearParameters() throws SQLException {
		parent.clearParameters();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#execute()}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public boolean execute() throws SQLException {
		return parent.execute();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeQuery()}.
	 * This method delegated to the underlying {@link PreparedStatement} object
	 * and wraps the {@link ResultSet} object using
	 * {@link WrapperFactory#wrapResultSet(ResultSetType, ResultSet)} with
	 * {@link ResultSetType#QUERY}.
	 */
	public ResultSet executeQuery() throws SQLException {
		return wrapperFactory.wrapResultSet(ResultSetType.QUERY, this, parent.executeQuery());
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#executeUpdate()}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public int executeUpdate() throws SQLException {
		return parent.executeUpdate();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getMetaData()}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return parent.getMetaData();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#getParameterMetaData()}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return parent.getParameterMetaData();
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setArray(int, Array)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setArray(int i, Array x) throws SQLException {
		parent.setArray(i, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setAsciiStream(int, InputStream, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		parent.setAsciiStream(parameterIndex, x, length);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setBigDecimal(int, BigDecimal)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		parent.setBigDecimal(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setBinaryStream(int, InputStream, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		parent.setBinaryStream(parameterIndex, x, length);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setBlob(int, Blob)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setBlob(int i, Blob x) throws SQLException {
		parent.setBlob(i, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setBoolean(int, boolean)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		parent.setBoolean(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setByte(int, byte)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setByte(int parameterIndex, byte x) throws SQLException {
		parent.setByte(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setBytes(int, byte[])}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		parent.setBytes(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setCharacterStream(int, Reader, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		parent.setCharacterStream(parameterIndex, reader, length);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setClob(int, Clob)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setClob(int i, Clob x) throws SQLException {
		parent.setClob(i, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setDate(int, Date, Calendar)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		parent.setDate(parameterIndex, x, cal);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setDate(int, Date)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setDate(int parameterIndex, Date x) throws SQLException {
		parent.setDate(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setDouble(int, double)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setDouble(int parameterIndex, double x) throws SQLException {
		parent.setDouble(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setFloat(int, float)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setFloat(int parameterIndex, float x) throws SQLException {
		parent.setFloat(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setInt(int, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setInt(int parameterIndex, int x) throws SQLException {
		parent.setInt(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setLong(int, long)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setLong(int parameterIndex, long x) throws SQLException {
		parent.setLong(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setNull(int, int, String)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		parent.setNull(paramIndex, sqlType, typeName);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setNull(int, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		parent.setNull(parameterIndex, sqlType);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setObject(int, Object, int, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		parent.setObject(parameterIndex, x, targetSqlType, scale);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setObject(int, Object, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		parent.setObject(parameterIndex, x, targetSqlType);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setObject(int, Object)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setObject(int parameterIndex, Object x) throws SQLException {
		parent.setObject(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setRef(int, Ref)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setRef(int i, Ref x) throws SQLException {
		parent.setRef(i, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setShort(int, short)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setShort(int parameterIndex, short x) throws SQLException {
		parent.setShort(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setString(int, String)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setString(int parameterIndex, String x) throws SQLException {
		parent.setString(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setTime(int, Time, Calendar)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		parent.setTime(parameterIndex, x, cal);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setTime(int, Time)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setTime(int parameterIndex, Time x) throws SQLException {
		parent.setTime(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setTimestamp(int, Timestamp, Calendar)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		parent.setTimestamp(parameterIndex, x, cal);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setTimestamp(int, Timestamp)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		parent.setTimestamp(parameterIndex, x);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setUnicodeStream(int, InputStream, int)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	@SuppressWarnings("deprecation")
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		parent.setUnicodeStream(parameterIndex, x, length);
	}
	
	/**
	 * Delegate method for {@link PreparedStatement#setURL(int, URL)}.
	 * This method delegated to the underlying {@link PreparedStatement} object.
	 */
	public void setURL(int parameterIndex, URL x) throws SQLException {
		parent.setURL(parameterIndex, x);
	}
}
