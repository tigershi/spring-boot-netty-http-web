package io.netty.mvc.bind;
/**
 * 
 * @author shihu
 * @Date  2019年5月10日
 *
 */
public interface NettyHttpMethods {

	 public static final String OPTIONS = "OPTIONS";

    /**
     * The GET method means retrieve whatever information (in the form of an entity) is identified
     * by the Request-URI.  If the Request-URI refers to a data-producing process, it is the
     * produced data which shall be returned as the entity in the response and not the source text
     * of the process, unless that text happens to be the output of the process.
     */
    public static final String GET = "GET";

    /**
     * The HEAD method is identical to GET except that the server MUST NOT return a message-body
     * in the response.
     */
    public static final String HEAD = "HEAD";

    /**
     * The POST method is used to request that the origin server accept the entity enclosed in the
     * request as a new subordinate of the resource identified by the Request-URI in the
     * Request-Line.
     */
    public static final String POST = "POST";

    /**
     * The PUT method requests that the enclosed entity be stored under the supplied Request-URI.
     */
    public static final String PUT = "PUT";

    /**
     * The PATCH method requests that a set of changes described in the
     * request entity be applied to the resource identified by the Request-URI.
     */
    public static final String PATCH = "PATCH";

    /**
     * The DELETE method requests that the origin server delete the resource identified by the
     * Request-URI.
     */
    public static final String DELETE = "DELETE";

    /**
     * The TRACE method is used to invoke a remote, application-layer loop- back of the request
     * message.
     */
    public static final String TRACE = "TRACE";

    /**
     * This specification reserves the method name CONNECT for use with a proxy that can dynamically
     * switch to being a tunnel
     */
    public static final String CONNECT = "CONNECT";


}
