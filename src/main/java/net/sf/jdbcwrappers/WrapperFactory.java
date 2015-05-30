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
package net.sf.jdbcwrappers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

/**
 * Wrapper factory base class.
 */
public class WrapperFactory {
	
    private boolean allowUnwrap;

    public synchronized boolean isAllowUnwrap() {
        return allowUnwrap;
    }

    public synchronized void setAllowUnwrap(boolean allowUnwrap) {
        this.allowUnwrap = allowUnwrap;
    }
    
    public class WrapResultsInvocationHandler<T> implements InvocationHandler {
    	
    	private T target;
    	
    	public WrapResultsInvocationHandler(T wrappedObject) {
			this.target = wrappedObject;
		}
    	
    	public T getTarget() {
    		return target;
    	}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if ("unwrap".equals(method.getName()) && args.length == 1 && WrappedJdbcObject.class.equals(args[0])) {
				return target;
			} else if ("isWrapperFor".equals(method.getName()) && args.length == 1 && WrappedJdbcObject.class.equals(args[0])) {
				return true;
			}

			Object result;
			try {
				result = method.invoke(target, args);
			} catch (InvocationTargetException e) {
				// rethrow any exception from the method invocation
				throw e.getCause();
			}
			// order of the classes is important: subclasses come before classes
			List<Class<?>> classesToWrap = new ArrayList<Class<?>>();
			Collections.addAll(classesToWrap, Connection.class,
					DataSource.class, DatabaseMetaData.class,
					CallableStatement.class, PreparedStatement.class,
					Statement.class, ResultSet.class);

			for (Class<?> clazz : classesToWrap) {
				if (clazz.isInstance(result)) {
					// avoid wrapping the object twice
					if (result instanceof WrappedJdbcObject<?>) {
						return result;
					}  else {
						return wrapIt(clazz, result);
					}
				}
			}

			// TODO: check the weird things: prepared statement has sql
			// attribute, something had unwrapped connection
			return result;

		}
    }
    
    @SuppressWarnings("unchecked")
	public<T> T wrapIt(Class<T> clazz, Object target) {
    	WrapResultsInvocationHandler<T> h = new WrapResultsInvocationHandler<T>((T) target);
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz, WrappedJdbcObject.class}, h);
    }
    
}
