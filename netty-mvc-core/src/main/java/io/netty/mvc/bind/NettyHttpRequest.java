package io.netty.mvc.bind;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

public class NettyHttpRequest implements FullHttpRequest {

    private FullHttpRequest realRequest;
    private Map<String, String> pathValParameterMap;
    private Map<String, List<String>> parameterMap;

    public NettyHttpRequest(FullHttpRequest request, Map<String, List<String>> parameterMap, Map<String, String> pathValParameterMap){
        this.realRequest = request;
        this.parameterMap = parameterMap;
        this.pathValParameterMap = pathValParameterMap;
    }
    
    
    public NettyHttpRequest(FullHttpRequest request, Map<String, List<String>> parameterMap){
        this.realRequest = request;
        this.parameterMap = parameterMap;
        this.pathValParameterMap = new LinkedHashMap<>();
    }
    /**
     * Returns a java.util.Map of the parameters of this request. Request
     * parameters are extra information sent with the request. parameters are contained in the query string or posted form
     * data.
     *
     */
    public Map<String, List<String>> getParameterAndPathValMap(){
    	for(Entry<String, String> entry : pathValParameterMap.entrySet()) {
    		parameterMap.put(entry.getKey(), Arrays.asList(entry.getValue()));
    	}
    	return this.parameterMap;
    }
    
    public Map<String, String> getPathValParameterMap() {
		return this.pathValParameterMap;
	}
    
    public Map<String, List<String>> getParameterMap(){
    	return this.parameterMap;
    }
    
    /**
     * Returns the value of a request parameter as a <code>String</code>, or
     * <code>null</code> if the parameter does not exist. Request parameters are
     * extra information sent with the request. For HTTP servlets, parameters
     * are contained in the query string or posted form data.
     * <p>
     * You should only use this method when you are sure the parameter has only
     * one value. If the parameter might have more than one value, use
     * {@link #getParameterValues}.
     * <p>
     * If you use this method with a multivalued parameter, the value returned
     * is equal to the first value in the array returned by
     * <code>getParameterValues</code>.
     * <p>
     * If the parameter data was sent in the request body, such as occurs with
     * an HTTP POST request, then reading the body directly via
     * {@link #getInputStream} or {@link #getReader} can interfere with the
     * execution of this method.
     *
     * @param name
     *            a <code>String</code> specifying the name of the parameter
     * @return a <code>String</code> representing the single value of the
     *         parameter
     * @see #getParameterValues
     */
    public String getParameter(String name) {
    	List<String> params = parameterMap.get(name);
    	if(params != null) {
    		return params.get(0);
    	}else {
    		return pathValParameterMap.get(name);
    	}
    	
    	
    	
    }
    
    public List<String> getParameterValues(String name) {
    	String param = pathValParameterMap.get(name);
    	if(param != null) {
    	  return Arrays.asList(param);
    	}else {
    		return parameterMap.get(name);
    	}
    }
    
    
    


    public String contentText(){
        return content().toString(Charset.forName("UTF-8"));
    }

    @Override
    public ByteBuf content() {
        return realRequest.content();
    }

    @Override
    public HttpHeaders trailingHeaders() {
        return realRequest.trailingHeaders();
    }

    @Override
    public FullHttpRequest copy() {
        return realRequest.copy();
    }

    @Override
    public FullHttpRequest duplicate() {
        return realRequest.duplicate();
    }

    @Override
    public FullHttpRequest retainedDuplicate() {
        return realRequest.retainedDuplicate();
    }

    @Override
    public FullHttpRequest replace(ByteBuf byteBuf) {
        return realRequest.replace(byteBuf);
    }

    @Override
    public FullHttpRequest retain(int i) {
        return realRequest.retain(i);
    }

    @Override
    public int refCnt() {
        return realRequest.refCnt();
    }

    @Override
    public FullHttpRequest retain() {
        return realRequest.retain();
    }

    @Override
    public FullHttpRequest touch() {
        return realRequest.touch();
    }

    @Override
    public FullHttpRequest touch(Object o) {
        return realRequest.touch(o);
    }

    @Override
    public boolean release() {
        return realRequest.release();
    }

    @Override
    public boolean release(int i) {
        return realRequest.release(i);
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return realRequest.protocolVersion();
    }

    @Override
    public HttpVersion protocolVersion() {
        return realRequest.protocolVersion();
    }

    @Override
    public FullHttpRequest setProtocolVersion(HttpVersion httpVersion) {
        return realRequest.setProtocolVersion(httpVersion);
    }

    @Override
    public HttpHeaders headers() {
        return realRequest.headers();
    }

    @Override
    public HttpMethod getMethod() {
        return realRequest.getMethod();
    }

    @Override
    public HttpMethod method() {
        return realRequest.method();
    }

    @Override
    public FullHttpRequest setMethod(HttpMethod httpMethod) {
        return realRequest.setMethod(httpMethod);
    }

    @Override
    public String getUri() {
        return realRequest.getUri();
    }

    @Override
    public String uri() {
        return realRequest.uri();
    }

    @Override
    public FullHttpRequest setUri(String s) {
        return realRequest.setUri(s);
    }

    @Override
    public DecoderResult getDecoderResult() {
        return realRequest.getDecoderResult();
    }

    @Override
    public DecoderResult decoderResult() {
        return realRequest.decoderResult();
    }

    @Override
    public void setDecoderResult(DecoderResult decoderResult) {
        realRequest.setDecoderResult(decoderResult);
    }

}
