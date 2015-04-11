package so.glad.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

/**
 * HttpConnection interface
 *
 * @author Palmtale
 * 2015-03-25
 */
public interface HttpConnection {
    String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    String HEADER_COOKIE = "Cookie";
	String HEADER_CONTENT_TYPE = "Content-Type";
	String HEADER_CONTENT_LENGTH = "Content-Length";
	String HEADER_CONNECTION = "Connection";
    String HEADER_KEEP_ALIVE = "Keep-Alive";
	String HEADER_LOCATION = "Location";
	String HEADER_HOST = "Host";
	String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";
	String HEADER_AUTHORIZATION = "Authorization";
	String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	String HEADER_ACCEPT = "Accept";
	String HEADER_USER_AGENT = "User-Agent";
	String HEADER_CONTENT_ENCODING = "Content-Encoding";
	String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
	String HEADER_SOAP_ACTION = "SOAPAction";

	String HEADER_VALUE_CONNECTION_KEEP_ALIVE = "Keep-Alive";
    String HEADER_VALUE_CONNECTION_CLOSE = "Close";
    String HEADER_VALUE_KEEP_ALIVE_CLOSED = "Closed";
	String HEADER_VALUE_CONTENT_ENCODING_GZIP = "gzip";
	String HEADER_VALUE_TRANSFER_ENCODING_CHUNK = "chunked";
	String HEADER_VALUE_ACCEPT_ALL = "*/*";
	String HEADER_VALUE_USER_AGENT_D_CLIENT = "D Client / 1.0";

	String METHOD_GET = "GET";
	String METHOD_POST = "POST";
	String METHOD_PUT = "PUT";
	String METHOD_DELETE = "DELETE";

	int STATUS_CONTINUE = 100;
	int STATUS_SWITCHING_PROTOCOLS = 101;
	int STATUS_OK = 200;
	int STATUS_CREATED = 201;
	int STATUS_ACCEPTED = 202;
	int STATUS_NON_AUTHORITATIVE_INFORMATION = 203;
	int STATUS_NO_CONTENT = 204;
	int STATUS_RESET_CONTENT = 205;
	int STATUS_PARTIAL_CONTENT = 206;
	int STATUS_MULTIPLE_CHOICES = 300;
	int STATUS_MOVED_PERMANENTLY = 301;
	int STATUS_MOVED_TEMPORARILY = 302;
	int STATUS_FOUND = 302;
	int STATUS_SEE_OTHER = 303;
	int STATUS_NOT_MODIFIED = 304;
	int STATUS_USE_PROXY = 305;
	int STATUS_TEMPORARY_REDIRECT = 307;
	int STATUS_BAD_REQUEST = 400;
	int STATUS_UNAUTHORIZED = 401;
	int STATUS_PAYMENT_REQUIRED = 402;
	int STATUS_FORBIDDEN = 403;
	int STATUS_NOT_FOUND = 404;
	int STATUS_METHOD_NOT_ALLOWED = 405;
	int STATUS_NOT_ACCEPTABLE = 406;
	int STATUS_PROXY_AUTHENTICATION_REQUIRED = 407;
	int STATUS_REQUEST_TIMEOUT = 408;
	int STATUS_CONFLICT = 409;
	int STATUS_GONE = 410;
	int STATUS_LENGTH_REQUIRED = 411;
	int STATUS_PRECONDITION_FAILED = 412;
	int STATUS_REQUEST_ENTITY_TOO_LARGE = 413;
	int STATUS_REQUEST_URI_TOO_LONG = 414;
	int STATUS_UNSUPPORTED_MEDIA_TYPE = 415;
	int STATUS_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	int STATUS_EXPECTATION_FAILED = 417;
	int STATUS_INTERNAL_SERVER_ERROR = 500;
	int STATUS_NOT_IMPLEMENTED = 501;
	int STATUS_BAD_GATEWAY = 502;
	int STATUS_SERVICE_UNAVAILABLE = 503;
	int STATUS_GATEWAY_TIMEOUT = 504;
	int STATUS_HTTP_VERSION_NOT_SUPPORTED = 505;

	String CHARSET_TOKEN = "; charset=";

	/**
	 * set the http method
	 * 
	 * @param httpMethod
	 */
	void setHttpMethod(String httpMethod);

	/**
	 * set a request header
	 * 
	 * @param name
	 *            header name
	 * @param value
	 *            header value
	 */
	void setRequestHeader(String name, String value);

	/**
	 * set a request parameter
	 * 
	 * @param name
	 * @param value
	 */
	void setRequestParameter(String name, Object value);

	/**
	 * set the request stream, the method should be "Post" or "Put"
	 * 
	 * @param requestStream the request data stream
	 */
	void setRequestStream(Streamable requestStream);

	/**
	 * get the response stream
	 * 
	 * @return
	 */
	InputStream getResponseStream();

	/**
	 * get the response reader
	 * 
	 * @return
	 */
	Reader getResponseReader();

	/**
	 * get the headers in the response
	 * 
	 * @return
	 */
	Properties getResponseHeaders();

	/**
	 * get header value
	 * 
	 * @param name
	 * @return
	 */
	String getResponseHeader(String name);

	/**
	 * execute the http request
	 */
	int execute() throws IOException;

	/**
	 * close the http connection, and close all the stream
	 */
	void close();

	String getResponseCharset();

	int getResponseLength();

	void setRequestContentType(String contentType, String charset);

	String getResponseContentType();

	String getPath();

	void setRequestParameters(Map<String, Object> requestParameters);
	
	void setUrlEncoding(String urlEncoding);
	
	String getUrlEncoding();
}
