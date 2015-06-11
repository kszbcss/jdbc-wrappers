package net.sf.jdbcwrappers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.sf.jdbcwrappers.ProxyHelper.MethodInvocation;

/**
 * Exampple Invocation handler to wrap a jdbc object.
 * It wraps recursively any jdbc object returned from a method.
 * 
 * @author Peter Van den Bosch
 *
 * @param <T> type of the JDBC object being wrapped
 */
public class RecursiveProxy<T> implements InvocationHandler {
	
	private final T target;
	
	public RecursiveProxy(T wrappedObject) {
		this.target = wrappedObject;
	}
	
	public T getTarget() {
		return target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		MethodInvocation methodInvocation = new MethodInvocation(target, method, args);
		
		Object result = methodInvocation.invoke();
		
		Class<?> clazz = ProxyHelper.getJdbcClass(result);
		if (clazz != null && !ProxyHelper.isWrapped(result, RecursiveProxy.class)) {
			return ProxyHelper.createProxy(clazz, new RecursiveProxy<Object>(result));
		}

		return result;
	}
	
}