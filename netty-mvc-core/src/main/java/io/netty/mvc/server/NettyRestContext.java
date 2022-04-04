package io.netty.mvc.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.mvc.annotation.NettyRequestMapping;
import io.netty.mvc.annotation.component.NettyInterceptor;
import io.netty.mvc.annotation.component.NettyListener;
import io.netty.mvc.bind.NettyAppListener;
import io.netty.mvc.bind.NettyMvcException;
import io.netty.mvc.bind.NettyMvcInterceptor;
import io.netty.mvc.config.Constants;
import io.netty.mvc.config.NettyMvcExceptions;
import io.netty.mvc.utils.StringUtils;

/**
 * 
 * @author shihu
 * @Date 2019年5月10日
 *
 */
public class NettyRestContext {
	private static Logger logger = LoggerFactory.getLogger(NettyRestContext.class);
	private final static List<NettyMvcInterceptorWrapper> intercepters = new ArrayList<>();
	private final static List<NettyListenerWrapper> listeners = new ArrayList<>();
	private final static Map<String, Map<String, List<NettyReqUriProp>>> requestPathMap = new HashMap<>();

	public NettyReqUriProp getRequestMethod(String url, HttpMethod method) throws NettyMvcException {
		try {
			List<NettyReqUriProp> propList = null;
			Map<String, List<NettyReqUriProp>> reqMethodMap = null;
			if (NettyReqUriProp.getSingleUrlCount() > 0) {
				reqMethodMap = requestPathMap.get(url);
				if (reqMethodMap != null) {
					propList = reqMethodMap.get(method.name());
				}
			}

			if (propList == null && NettyReqUriProp.getPathValURLCount() > 0) {

				String maxPrefixPathStr = NettyReqUriProp.subPrePathCountChar(url, '/',
						NettyReqUriProp.getMaxSlashCount());
				if (maxPrefixPathStr.length() <= NettyReqUriProp.getMinSlashIdx()) {
					throw NettyMvcExceptions.NOT_FOUND_SOURCE;
				}
				reqMethodMap = requestPathMap.get(maxPrefixPathStr);
				if (reqMethodMap != null) {
					propList = reqMethodMap.get(method.name());
				}
				if (propList == null) {
					int maxCount = NettyReqUriProp.countPrePathSlash(maxPrefixPathStr) - 1;

					do {
						maxPrefixPathStr = NettyReqUriProp.subPrePathCountChar(url, '/', maxCount);
						if (maxPrefixPathStr.length() <= NettyReqUriProp.getMinSlashIdx()) {
							throw NettyMvcExceptions.NOT_FOUND_SOURCE;
						}

						reqMethodMap = requestPathMap.get(maxPrefixPathStr);
						if (reqMethodMap != null) {
							propList = reqMethodMap.get(method.name());
						}

						if (propList != null) {
							break;
						}
						maxCount--;

					} while (propList == null);
				}
			}

			for (NettyReqUriProp prop : propList) {
				if (prop.pathMatch(url)) {
					return prop;
				}
			}
		} catch (Exception ex) {
			throw NettyMvcExceptions.NOT_FOUND_SOURCE;
		}
		return null;
	}

	public Map<String, Map<String, List<NettyReqUriProp>>> getRequestPathMap() {
		return requestPathMap;
	}

	public List<NettyMvcInterceptorWrapper> getIntercepters() {
		return intercepters;
	}

	public List<NettyListenerWrapper> getListeners() {
		return listeners;
	}

	public void initRequestMap(Map<String, Object> objs) throws NettyMvcException {
		for (Object bean : objs.values()) {

			logger.info("the controller name-" + bean.getClass().getName());
			Method[] methods = bean.getClass().getDeclaredMethods();
			logger.info("the controller method size-" + methods.length);
			for (int i = 0; i < methods.length; i++) {
				Method reqMethodObj = methods[i];
				NettyRequestMapping mapping = reqMethodObj.getAnnotation(NettyRequestMapping.class);

				if (mapping == null) {
					Class<?>[] superclasses = bean.getClass().getInterfaces();
					for (Class<?> superclass : superclasses) {
						try {
							Method supperMethodObj = superclass.getDeclaredMethod(reqMethodObj.getName(),
									reqMethodObj.getParameterTypes());
							mapping = supperMethodObj.getAnnotation(NettyRequestMapping.class);
							// logger.info("interface Annotation path"+ mapping.value());
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							mapping = null;
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							mapping = null;
						}
						if (mapping != null) {
							break;
						}
					}
				}

				if (mapping != null) {
					String reqHttpPath = mapping.value();
					String reqHttpMethod = mapping.method();
					logger.info("init maping url--" + reqHttpPath + "-----" + reqHttpMethod);
					Entry<Method, Object> entry = new Node<Method, Object>(reqMethodObj, bean);

					processReqPathVal(requestPathMap, reqHttpPath, reqHttpMethod, entry);

				}

			}

		}
	}

	private void processReqPathVal(Map<String, Map<String, List<NettyReqUriProp>>> requestUriMapList, String uri,
			String method, Entry<Method, Object> methodObj) throws NettyMvcException {

		String prefix = null;
		boolean pathValFlag = NettyReqUriProp.isPattern(uri);
		if (pathValFlag) {
			prefix = uri.substring(0, uri.indexOf(Constants.PATH_VAL_PREFIX));
		} else {
			prefix = uri;
		}
		Map<String, List<NettyReqUriProp>> preMap = requestUriMapList.get(prefix);
		if (preMap != null) {
			List<NettyReqUriProp> propList = preMap.get(method);
			if (propList == null) {
				if (pathValFlag) {
					propList = new ArrayList<>();
				} else {
					propList = new ArrayList<>(1);
				}
				NettyReqUriProp nrup = NettyReqUriProp.newInstance(uri, pathValFlag, methodObj);
				propList.add(nrup);
				preMap.put(method, propList);
			} else {
				NettyReqUriProp nrup = NettyReqUriProp.newInstance(uri, pathValFlag, methodObj);
				propList.add(nrup);
			}

		} else {
			List<NettyReqUriProp> newPropList = null;
			if (pathValFlag) {
				newPropList = new ArrayList<>();
			} else {
				newPropList = new ArrayList<>(1);
			}

			NettyReqUriProp nrup = NettyReqUriProp.newInstance(uri, pathValFlag, methodObj);
			newPropList.add(nrup);
			Map<String, List<NettyReqUriProp>> methodMap = new HashMap<>();
			methodMap.put(method, newPropList);
			requestUriMapList.put(prefix, methodMap);
		}

	}

	public void initNettyMvcInterceptorWrapper(Map<String, Object> objs) {
		for (Object bean : objs.values()) {
			if (bean instanceof NettyMvcInterceptor) {

				NettyInterceptor nettyInterceptor = bean.getClass().getAnnotation(NettyInterceptor.class);
				if (nettyInterceptor != null) {
					intercepters
							.add(new NettyMvcInterceptorWrapper(nettyInterceptor.sort(), (NettyMvcInterceptor) bean));
				}
			}
		}

		Collections.sort(intercepters);
	}

	public void initNettyListenerWrapper(Map<String, Object> objs) {
		for (Object bean : objs.values()) {
			if (bean instanceof NettyAppListener) {

				NettyListener nettyListener = bean.getClass().getAnnotation(NettyListener.class);
				if (nettyListener != null) {
					listeners.add(new NettyListenerWrapper(bean.getClass().getName(), nettyListener.sort(),
							(NettyAppListener) bean));

				}
			}
		}

		Collections.sort(listeners);
	}

	static class Node<K, V> implements Map.Entry<K, V> {

		final K key;
		V value;

		Node(K key, V value) {

			this.key = key;
			this.value = value;
		}

		public final K getKey() {
			return key;
		}

		public final V getValue() {
			return value;
		}

		public final String toString() {
			return key + "=" + value;
		}

		public final int hashCode() {
			return Objects.hashCode(key) ^ Objects.hashCode(value);
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		public final boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof Map.Entry) {
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
				if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue()))
					return true;
			}
			return false;
		}
	}

}
