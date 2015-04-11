package so.glad.client;

import so.glad.client.http.HttpConnectionFactory;

/**
 * The base interface for Http Client
 * @author Palmtale
 * 2015-3-25
 */
public interface Client {
    /**
     * set the contentType for http client, for example: text/xml
     *
     * @param contentType the content type
     */
    void setContentType(String contentType);

    /**
     * get the content type
     * 
     * @return content type
     */
    String getContentType();
    /**
     * set the encoding for the http body stream
     *
     * @param encoding the encoding charset
     */
    void setEncoding(String encoding);

    /**
     * get the encoding for http parameters
     * 
     * @return encoding
     */
    String getEncoding();
    /**
     * set the encoding for http url, especially for the parameters in the url
     *
     * @param urlEncoding the encoding charset
     */
    void setUrlEncoding(String urlEncoding);

    /**
     * get the encoding for http url
     * 
     * @return urlEncoding
     */
    String getUrlEncoding();
    
    /**
     * set the http connection factory. It will be used to generate the http connection
     *
     * @param httpConnectionFactory the http connection factory
     */
    void setHttpConnectionFactory(HttpConnectionFactory httpConnectionFactory);

    /**
     * set the executor to control execution of the http client
     *
     * @param executor the executor
     */
	void setExecutor(Executor executor);
}
