package io.netty.mvc.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.mvc.annotation.NettyMvcExceptionHandler;
import io.netty.mvc.annotation.NettyPathVal;
import io.netty.mvc.annotation.NettyReqParam;
import io.netty.mvc.annotation.NettyRequestBody;
import io.netty.mvc.bind.NettyHttpRequest;
import io.netty.mvc.bind.NettyHttpResponse;
import io.netty.mvc.bind.NettyMvcException;
import io.netty.mvc.config.NettyMvcExceptions;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author shihu
 * @Date 2019年5月10日
 *
 */
public class NettyMvcDispatcher {
	private static Logger logger = LoggerFactory.getLogger(NettyMvcDispatcher.class);
	private final NettyRestContext context;

	public NettyMvcDispatcher(NettyRestContext context) {
		this.context = context;
	}

	public void service(FullHttpRequest fullHttpRequest, FullHttpResponse response) throws Exception {
		QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
		String url = decoder.rawPath();

		Map<String, List<String>> urlParamsMap = getBodyParams(fullHttpRequest, decoder.parameters());

		logger.debug("query url--" + url + "---method--" + fullHttpRequest.method().name());

		NettyReqUriProp propUri = null;
		try {
			propUri = context.getRequestMethod(url, fullHttpRequest.method());

		} catch (NettyMvcException ex) {
			ByteBuf byteBuf = Unpooled.copiedBuffer(ex.getExceptionJsonMsg(), CharsetUtil.UTF_8);
			response.content().writeBytes(byteBuf);
			byteBuf.release();
			response.setStatus(ex.getResponseStatus());
			return;
		}

		Map<String, String> pathVals = null;
		// int pathval
		if (propUri.isPathVal()) {
			pathVals = propUri.extractUriTemplateVariables(url);
		} else {
			pathVals = new LinkedHashMap<>();
		}

		NettyHttpRequest nettyHttpReq = new NettyHttpRequest(fullHttpRequest, urlParamsMap, pathVals);
		NettyHttpResponse nettyHttpResp = new NettyHttpResponse(response);

		List<NettyMvcInterceptorWrapper> wrappers = context.getIntercepters();
		for (NettyMvcInterceptorWrapper wraper : wrappers) {
			if (!wraper.getIntercepter().preHandle(nettyHttpReq, nettyHttpResp)) {
				return;
			}
		}

		Entry<Method, Object> entry = propUri.getMapMethod();
		Method reqMethod = entry.getKey();
		Parameter[] parameters = reqMethod.getParameters();
		Object[] paramObjs = bindParamObjs(parameters, nettyHttpReq, nettyHttpResp);

		Object resultObj = null;
		try {
			if (paramObjs != null) {
				resultObj = reqMethod.invoke(entry.getValue(), paramObjs);
			} else {
				resultObj = reqMethod.invoke(entry.getValue());
			}

		} catch (Exception e) {

			if (e.getCause().getClass().getAnnotation(NettyMvcExceptionHandler.class) != null) {
				Throwable cause = e.getCause();
				if (cause instanceof NettyMvcException && (((NettyMvcException) cause).getExceptionJsonMsg() != null)) {
					ByteBuf byteBuf = Unpooled.copiedBuffer(((NettyMvcException) cause).getExceptionJsonMsg(),
							CharsetUtil.UTF_8);
					response.content().writeBytes(byteBuf);
					byteBuf.release();
					response.headers().set("Content-Type", "application/json;charset=UTF-8");
					if (((NettyMvcException) cause).getResponseStatus() != null) {
						response.setStatus(((NettyMvcException) cause).getResponseStatus());
					} else {
						response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
					}
					logger.error(cause.getMessage(), cause);
					return;
				}
			}

			throw e;

		}

		int index = wrappers.size() - 1;
		while (index > -1) {
			wrappers.get(index).getIntercepter().postHandle(nettyHttpReq, nettyHttpResp, resultObj);
			index--;
		}

		if (resultObj == null) {
			ByteBuf byteBuf = Unpooled.copiedBuffer("Not Content reply for this request", CharsetUtil.UTF_8);
			response.content().writeBytes(byteBuf);
			byteBuf.release();
			response.headers().set("Content-Type", "text/plain;charset=UTF-8");
			response.setStatus(HttpResponseStatus.NO_CONTENT);
			return;
		} else if (resultObj instanceof String) {

			ByteBuf byteBuf = Unpooled.copiedBuffer((String) resultObj, CharsetUtil.UTF_8);
			response.content().writeBytes(byteBuf);
			byteBuf.release();
			response.headers().set("Content-Type", "text/plain;charset=UTF-8");
			response.setStatus(HttpResponseStatus.OK);
			return;
		}

		String resultContent = JSON.toJSONString(resultObj, true);
		ByteBuf byteBuf = Unpooled.copiedBuffer(resultContent, CharsetUtil.UTF_8);
		response.content().writeBytes(byteBuf);
		byteBuf.release();
		response.headers().set("Content-Type", "application/json;charset=UTF-8");
		response.setStatus(HttpResponseStatus.OK);

	}

	private Object[] bindParamObjs(Parameter[] parameters, NettyHttpRequest request, NettyHttpResponse response)
			throws NettyMvcException {
		Object[] paramObjs = null;
		if (parameters.length > 0) {
			paramObjs = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i].getAnnotation(NettyRequestBody.class) != null) {
					paramObjs[i] = getRequestBodyJson(request, parameters[i].getType());
				} else if (parameters[i].getAnnotation(NettyReqParam.class) != null) {
					NettyReqParam nprAnno = parameters[i].getAnnotation(NettyReqParam.class);
					String paramName = nprAnno.name();
					List<String> paramList = request.getParameterMap().get(paramName);
					String paramVal = null;
					if (paramList != null) {
						paramVal = paramList.get(0);
					}

					if (paramVal != null) {
						paramObjs[i] = convert2ParamType(paramVal, parameters[i].getType());
					} else if (nprAnno.required()) {
						throw NettyMvcExceptions.PARAM_NOT_NULL;
					}

				} else if (parameters[i].getAnnotation(NettyPathVal.class) != null) {
					String pathValName = parameters[i].getAnnotation(NettyPathVal.class).name();
					String pathVal = request.getPathValParameterMap().get(pathValName);
					if (pathVal != null) {
						paramObjs[i] = convert2ParamType(pathVal, parameters[i].getType());
					}

				} else {
					if (parameters[i].getType().isAssignableFrom(NettyHttpRequest.class)) {
						paramObjs[i] = request;
					} else if (parameters[i].getType().isAssignableFrom(NettyHttpResponse.class)) {
						paramObjs[i] = response;
					}
				}

			}
		}
		return paramObjs;
	}

	private <T> T getRequestBodyJson(FullHttpRequest req, Class<T> clazz) {
		String contentType = req.headers().get(HttpHeaderNames.CONTENT_TYPE);
		if (contentType != null && contentType.equals("application/json")) {
			ByteBuf jsonBuf = req.content();
			String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
			if (jsonStr != null && !jsonStr.equals("")) {
				return JSON.parseObject(jsonStr, clazz);
			} else {
				return null;
			}
		}
		return null;

	}

	private Object convert2ParamType(String value, Class<?> clazz) {

		String ftype = clazz.getName();
		String fstype = clazz.getSimpleName();
		try {
			if (clazz == String.class)
				return value.toString();
			else if (ftype.indexOf("java.lang.") == 0) {
				// java.lang下面类型通用转换函数
				Class<?> class1 = Class.forName(ftype);
				Method method = class1.getMethod("parse" + fixparse(fstype), String.class);
				if (method != null) {
					Object ret = method.invoke(null, value.toString());
					if (ret != null)
						return ret;
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}

	private static String fixparse(String fstype) {
		switch (fstype) {
		case "Integer":
			return "Int";
		default:
			return fstype;
		}
	}

	private static Map<String, List<String>> getBodyParams(FullHttpRequest request, Map<String, List<String>> map)
			throws IOException {
		Map<String, List<String>> paramsMap = new LinkedHashMap<String, List<String>>();
		String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
		if (!contentType.contains("json")
				&& (request.method().equals(HttpMethod.POST) || request.method().equals(HttpMethod.PUT))) {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
			List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
			for (InterfaceHttpData data : postData) {
				if (data.getHttpDataType().equals(HttpDataType.Attribute)) {
					MemoryAttribute attr = (MemoryAttribute) data;

					String name = attr.getName();
					List<String> values = paramsMap.get(name);
					if (values == null) {
						values = new ArrayList<String>(1); // Often there's only 1 value.
						paramsMap.put(name, values);
					}
					values.add(attr.getValue());
				}
			}

			decoder.destroy();
		}

		paramsMap.putAll(map);
		return paramsMap;

	}

}
