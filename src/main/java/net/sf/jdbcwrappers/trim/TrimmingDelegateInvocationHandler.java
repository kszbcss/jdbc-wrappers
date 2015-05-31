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
import java.sql.Statement;

import net.sf.jdbcwrappers.ProxyHelper;
import net.sf.jdbcwrappers.ProxyHelper.MethodInvocation;

/**
 * Proxy for jdbc classes which wraps any jdbc objects returned by its methods.
 * ResultSet objects are wrapped with the {@link TrimmingResultSetInvocationHandler}.
 *  
 * @author Peter Van den Bosch
 *
 * @param <T> The jdbc class of the target object
 */
public class TrimmingDelegateInvocationHandler<T> implements InvocationHandler {
	private final T target;
	
	
	public TrimmingDelegateInvocationHandler(T target) {
		this.target = target;
	}
	
	public T getTarget() {
		return target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodInvocation methodInvocation = new MethodInvocation(target, method, args);
		Object result = methodInvocation.invoke();
		
		Class<?> clazz = ProxyHelper.getJdbcClass(result);
		if (clazz != null
			&& !ProxyHelper.isWrapped(result, TrimmingResultSetInvocationHandler.class) 
			&& !ProxyHelper.isWrapped(result, TrimmingDelegateInvocationHandler.class)) {
				if(target instanceof Statement && ResultSet.class.equals(clazz) && !"getGeneratedKeys".equals(method.getName())) {
					return ProxyHelper.createProxy(ResultSet.class, new TrimmingResultSetInvocationHandler((ResultSet) result));
				} else {
					return ProxyHelper.createProxy(clazz, new TrimmingDelegateInvocationHandler<>(result));
				}
		} 

		return result;
		
	}
	
}