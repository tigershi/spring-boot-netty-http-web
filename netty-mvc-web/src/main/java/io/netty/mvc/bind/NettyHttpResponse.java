package io.netty.mvc.bind;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class NettyHttpResponse implements FullHttpResponse  {
	
	private FullHttpResponse fullHttpResponse;
	
	public NettyHttpResponse(FullHttpResponse fullHttpResponse) {
		this.fullHttpResponse = fullHttpResponse;
	}

	@Override
	public HttpResponseStatus getStatus() {
		// TODO Auto-generated method stub
		return fullHttpResponse.getStatus();
	}

	@Override
	public HttpResponseStatus status() {
		// TODO Auto-generated method stub
		return fullHttpResponse.status();
	}

	@Override
	public HttpVersion getProtocolVersion() {
		// TODO Auto-generated method stub
		return fullHttpResponse.getProtocolVersion();
	}

	@Override
	public HttpVersion protocolVersion() {
		// TODO Auto-generated method stub
		return fullHttpResponse.protocolVersion();
	}

	@Override
	public HttpHeaders headers() {
		// TODO Auto-generated method stub
		return fullHttpResponse.headers();
	}

	@Override
	public DecoderResult getDecoderResult() {
		// TODO Auto-generated method stub
		return fullHttpResponse.getDecoderResult();
	}

	@Override
	public DecoderResult decoderResult() {
		// TODO Auto-generated method stub
		return fullHttpResponse.decoderResult();
	}

	@Override
	public void setDecoderResult(DecoderResult result) {
		// TODO Auto-generated method stub
		fullHttpResponse.setDecoderResult(result);
		
	}

	@Override
	public HttpHeaders trailingHeaders() {
		// TODO Auto-generated method stub
		return fullHttpResponse.trailingHeaders();
	}

	@Override
	public ByteBuf content() {
		// TODO Auto-generated method stub
		return fullHttpResponse.content();
	}

	@Override
	public int refCnt() {
		// TODO Auto-generated method stub
		return fullHttpResponse.refCnt();
	}

	@Override
	public boolean release() {
		// TODO Auto-generated method stub
		return fullHttpResponse.release();
	}

	@Override
	public boolean release(int decrement) {
		// TODO Auto-generated method stub
		return fullHttpResponse.release(decrement);
	}

	@Override
	public FullHttpResponse copy() {
		// TODO Auto-generated method stub
		return fullHttpResponse.copy();
	}

	@Override
	public FullHttpResponse duplicate() {
		// TODO Auto-generated method stub
		return fullHttpResponse.duplicate();
	}

	@Override
	public FullHttpResponse retainedDuplicate() {
		// TODO Auto-generated method stub
		return fullHttpResponse.retainedDuplicate();
	}

	@Override
	public FullHttpResponse replace(ByteBuf content) {
		// TODO Auto-generated method stub
		return fullHttpResponse.replace(content);
	}

	@Override
	public FullHttpResponse retain(int increment) {
		// TODO Auto-generated method stub
		return fullHttpResponse.retain(increment);
	}

	@Override
	public FullHttpResponse retain() {
		// TODO Auto-generated method stub
		return fullHttpResponse.retain();
	}

	@Override
	public FullHttpResponse touch() {
		// TODO Auto-generated method stub
		return fullHttpResponse.touch();
	}

	@Override
	public FullHttpResponse touch(Object hint) {
		// TODO Auto-generated method stub
		return fullHttpResponse.touch(hint);
	}

	@Override
	public FullHttpResponse setProtocolVersion(HttpVersion version) {
		// TODO Auto-generated method stub
		return fullHttpResponse.setProtocolVersion(version);
	}

	@Override
	public FullHttpResponse setStatus(HttpResponseStatus status) {
		// TODO Auto-generated method stub
		return fullHttpResponse.setStatus(status);
	}

}
