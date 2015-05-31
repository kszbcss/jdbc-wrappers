/*
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
package net.sf.jdbcwrappers.trim;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import net.sf.jdbcwrappers.ProxyHelper;
import net.sf.jdbcwrappers.ProxyHelper.MethodInvocation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link java.sql.ResultSet} wrapper that automatically trims strings retrieved
 * from <tt>CHAR</tt> columns.
 * Note that trimming is strictly limited to <tt>CHAR</tt> columns; values
 * retrieved from <tt>VARCHAR</tt> columns remain unchanged.
 * The wrapper relies on {@link ResultSetMetaData} to determine the column
 * type.
 * 
 * @author Andreas Veithen
 * @author Peter Van den Bosch
 * @version $Id:TrimmingResultSetWrapper.java 24 2008-06-21 15:08:14Z veithen $
 */
public class TrimmingResultSetInvocationHandler implements InvocationHandler {
    
	private final static Log LOG = LogFactory.getLog(TrimmingResultSetInvocationHandler.class);
	private Set<String> charColumns;
	private boolean[] isCharColumn;
	private ResultSet target;
	
	public TrimmingResultSetInvocationHandler(ResultSet wrappedObject) {
		this.target = wrappedObject;
	}
	
	@Override
	/**
	 * Modifies getObject and getString behavior. 
	 * If the column is of type <tt>CHAR</tt>, the returned object is
	 * a {@link String} holding the column value without trailing spaces.
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		MethodInvocation methodInvocation = new MethodInvocation(target, method, args);
		Object result = methodInvocation.invoke();

		if(result instanceof String && ("getObject".equals(method.getName()) || "getString".equals(method.getName()))) {
			String stringResult = (String) result;
			
			if(method.getParameterCount() <= 1) {
				throw new IllegalStateException("Method signature not expected on class ResultSet: " + method.toGenericString());
			}
					
			if(int.class.equals(method.getParameterTypes()[0])) {
				int columnIndex = (int) args[0];
		        return isCharColumn(columnIndex) ? trim(stringResult) : stringResult;
			} else if (String.class.equals(method.getParameterTypes()[0])) {
				String columnName = (String) args[0];
		        return isCharColumn(columnName) ? trim(stringResult) : stringResult;
			} else {
				throw new IllegalStateException("Method signature not expected on class ResultSet: " + method.toGenericString());
			}
		} else {
			Class<?> clazz = ProxyHelper.getJdbcClass(result);
			if (clazz != null
					&& !ProxyHelper.isWrapped(result, TrimmingResultSetInvocationHandler.class) 
					&& !ProxyHelper.isWrapped(result, TrimmingDelegateInvocationHandler.class)) {
				return ProxyHelper.createProxy(clazz, new TrimmingDelegateInvocationHandler<>(result));
			} else {
				return result;
			}
		}
	}
    
    private void fetchCharColumns() throws SQLException {
        if (charColumns == null) {
            ResultSetMetaData metadata = target.getMetaData();
            int columnCount = metadata.getColumnCount();
            charColumns = new HashSet<String>();
            isCharColumn = new boolean[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                if (metadata.getColumnType(i) == Types.CHAR) {
                    charColumns.add(metadata.getColumnName(i).toUpperCase());
                    isCharColumn[i-1] = true;
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("CHAR columns: " + charColumns);
            }
        }
    }
    
    private boolean isCharColumn(int columnIndex) throws SQLException {
        fetchCharColumns();
        return isCharColumn[columnIndex-1];
    }
    
    private boolean isCharColumn(String columnName) throws SQLException {
        fetchCharColumns();
        return charColumns.contains(columnName.toUpperCase());
    }
    
    private String trim(String string) {
        int length = string.length();
        int trimmedLength = length;
        while (trimmedLength > 0 && string.charAt(trimmedLength-1) == ' ') {
            trimmedLength--;
        }
        return trimmedLength == length ? string : string.substring(0, trimmedLength);
    }
    
}
