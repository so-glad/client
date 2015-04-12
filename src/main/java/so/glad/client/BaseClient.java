package so.glad.client;

import so.glad.client.http.Constant;
import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLWrapper;

/**
 * classes such as ParameterClientImpl , SimpleClientImpl , SessionAwareParameterClient, WebServiceClient can extends this base class
 * to execute http request
 *
 * @author Palmtale
 * 2015-3-25
 */
public abstract class BaseClient implements Client {

	//default content type "text/xml"
	private String contentType = Const.CONTENT_TYPE_TEXT_XML;
	//default encoding type "utf-8"
	private String encoding = Const.DEFAULT_ENCODING;
	//default url encoding "US-ASCII"
	private String urlEncoding = Constant.HTTP_META_DEFAULT_CHARSET;

	private HttpConnectionFactory httpConnectionFactory;

	private Executor executor = new BaseExecutor();

    private boolean logEnabled = true;

	/**
	 * Get singleton HttpConnectionFactory instance . It will be used to generate the http connection
	 * instance could be (for examples:HttpSocketConnectionFactory/PoolableHttpSocketConnectionFactory/FixedURLConnectionFactory)
	 *
	 * @return singleton HttpConnectionFactory instance
	 */
	public HttpConnectionFactory getHttpConnectionFactory() {
		if (null == httpConnectionFactory) {
			httpConnectionFactory = Const.getDefaultHttpConnectionFactory();
		}
		return httpConnectionFactory;
	}

	/**
	 * set the HttpConnectionFactory instance
	 *
	 * @param httpConnectionFactory
	 *            instance such as
	 *            HttpSocketConnectionFactory/PoolableHttpSocketConnectionFactory/FixedURLConnectionFactory
	 */
	public void setHttpConnectionFactory(
			HttpConnectionFactory httpConnectionFactory) {
		this.httpConnectionFactory = httpConnectionFactory;
	}

	/**
	 * all subclass base to use this execute method to send parameters to specific url
	 *
	 * @param wrapper					url with alias
	 * @param httpConnectionContext	http connection context
	 * @param httpConnectionTemplate	http connection (request header , http method , content type , url encoding,parameters) ready
	 * @return response for object
	 */

	protected Object execute(URLWrapper wrapper,
			HttpConnectionContext httpConnectionContext,
			HttpConnectionTemplate httpConnectionTemplate) {
		httpConnectionTemplate.setContentType(getContentType());
		httpConnectionTemplate.setEncoding(getEncoding());
		httpConnectionTemplate.setUrlEncoding(getUrlEncoding());
		return executor.execute(wrapper, httpConnectionContext,
				getHttpConnectionFactory(), httpConnectionTemplate);
	}

	/**
	 * get the content type
	 *
	 * @return content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * set the contentType for http client, for example: text/xml
	 *
	 * @param contentType the content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * get the encoding for http parameters
	 *
	 * @return encoding charset
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
     * set the encoding for the http body stream
     *
     * @param encoding the encoding charset
     */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * get the encoding for http url
	 *
	 * @return url encoding charset
	 */
	public String getUrlEncoding() {
		return urlEncoding;
	}

	/**
     * set the encoding for http url, especially for the parameters in the url
     *
     * @param urlEncoding the encoding charset
     */
	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

	/**
	 * get the control execution from http client
	 *
	 * @return executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
     * set the executor to control execution of the http client
     *
     * @param executor the executor
     */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }
}
