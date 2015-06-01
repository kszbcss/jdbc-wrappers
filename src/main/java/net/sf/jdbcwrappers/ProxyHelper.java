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

public class ProxyHelper {
	
	// order of the classes is important: subclasses come before classes
	// TODO: RowSet not supported at the moment
	private static final List<Class<?>> CLASSES_TO_WRAP = new ArrayList<Class<?>>();
	static {
		Collections.addAll(CLASSES_TO_WRAP, Connection.class,
			DataSource.class, DatabaseMetaData.class,
			CallableStatement.class, PreparedStatement.class,
			Statement.class, ResultSet.class);
	}
	
	/**
	 * Helper class representing a method invocation 
	 */
	public static class MethodInvocation {
		private final Object target;
		private final Method method;
		private final Object[] args;

		public MethodInvocation(Object target, Method method, Object[] args) {
			this.target = target;
			this.method = method;
			this.args = args;
		}

		public Object getTarget() {
			return target;
		}

		public Method getMethod() {
			return method;
		}

		public Object[] getArgs() {
			return args;
		}
		
		/**
		 * Invoke the method
		 * @return return value of the method call
		 * @throws Throwable Any exception propagated of the underlying method call, or a runtime exception when making the call (e.g. method doesn't exist) 
		 */
		public Object invoke() throws Throwable {
			try {
				return method.invoke(target, args);
			} catch(IllegalAccessException e) {
				// illegal access exception is checked and isn't in the original method's signature, so rethrow it as a runtime exception
				throw new IllegalStateException(e);
			} catch (InvocationTargetException e) {
				// rethrow any exception from the method invocation
				throw e.getCause();
			}
		}
	}
	
	/**
	 * @return If target is an instance of a jdbc class, return the class, otherwise null.   
	 */
	public static Class<?> getJdbcClass(Object target) {
		for (Class<?> clazz : CLASSES_TO_WRAP) {
			if (clazz.isInstance(target)) {
				return clazz;
			}
		}
		return null;
	}
	
	/**
	 * Create proxy object implementing a single class.
	 *
	 * @param clazz The interface implemented by the proxy
	 * @param h  InvocationHandler with the proxy logic
	 * @return proxy object
	 */
	@SuppressWarnings("unchecked")
	public static <T>  T createProxy(Class<T> clazz, InvocationHandler h) {
		//loading proxy on the invocation handler's classloader should be best, as that class loader should have access to all necessary dependent classes 
		return (T) Proxy.newProxyInstance(h.getClass().getClassLoader(), new Class[]{clazz}, h);
	}
	
	/**
	 * Check whether the target object is a proxy instance which is using the given InvocationHandler. 
	 */
	public static boolean isWrapped(Object target, Class<? extends InvocationHandler> invocationHandlerClazz) {
		if(target instanceof Proxy) {
			InvocationHandler h = Proxy.getInvocationHandler((Proxy) target);
			return invocationHandlerClazz.isInstance(h);  
		} else {
			return false;
		}
	}
	
}
