package so.glad.client;

import java.io.IOException;

import org.slf4j.Logger;

import so.glad.client.http.HttpConnection;
/**
 * @author Palmtale
 * 2015-3-25
 */
public interface HttpConnectionTemplate {

    int RESPONSE_DATA_TYPE_STRING = 1;
    int RESPONSE_DATA_TYPE_STREAM = 2;
    int RESPONSE_DATA_TYPE_READER = 3;

	/**
	 * file MIME type
	 * 
	 * @param contentType
	 */
	void setContentType(String contentType);

	/**
	 * post method params encoding Charset 
	 * 
	 * @param encoding
	 */

	void setEncoding(String encoding);

	/**
	 * get method URL encoding Charset 
	 * 
	 * @param urlEncoding
	 */
	void setUrlEncoding(String urlEncoding);

	/**
	 * prepare httpConnection's params(RequestHeader,RequestContentType,URLEncoding,HttpMethod,data) before execute
	 * 
	 * @param httpConnection           socket connection 
	 */
	void beforeExecute(HttpConnection httpConnection);
	/**
	 * get response and server's session(If necessary) from server 
	 * 
	 * @param httpConnection 			socket connection 
	 * @return always string 
	 * @throws IOException
	 */
	Object afterExecute(HttpConnection httpConnection) throws IOException;
	/**
	 * 
	 * @param log a new log instance binding with URL
	 */
	void setLog(Logger log);

    void setLogEnabled(boolean logEnabled);
}
