package so.glad.client;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLWrapper;
import so.glad.client.http.impl.HttpConnectionContextWrapper;

/**
 * classes extends this base class have define method ,example:ControlExecutor
 *
 * @author Palmtale
 * 2015-3-25
 */
public class BaseExecutor implements Executor {

	static Set<Integer> VALID_STATUS_SET = new HashSet<Integer>();

    static {
        VALID_STATUS_SET.add(HttpConnection.STATUS_OK);
        VALID_STATUS_SET.add(HttpConnection.STATUS_CREATED);
        VALID_STATUS_SET.add(HttpConnection.STATUS_ACCEPTED);
        VALID_STATUS_SET.add(HttpConnection.STATUS_NON_AUTHORITATIVE_INFORMATION);
        VALID_STATUS_SET.add(HttpConnection.STATUS_NO_CONTENT);
        VALID_STATUS_SET.add(HttpConnection.STATUS_RESET_CONTENT);
        VALID_STATUS_SET.add(HttpConnection.STATUS_PARTIAL_CONTENT);
        VALID_STATUS_SET.add(HttpConnection.STATUS_FOUND);
    }

	/**
	 * get http connection without http context 
	 * 
	 * @param urlWrapper						<code>URLWrapper</code> url with alias
	 * @param httpConnectionFactory				<code>HttpConnectionFactory</code> be used to generate http connection ,instance such as HttpSocketConnectionFactory/PoolableHttpSocketConnectionFactory/FixedURLConnectionFactory
	 * @return	httpConnection 					<code>HttpConnection</code> http connection can post/get request parameter to server,instance such as link HttpSocketConnection/PoolableHttpSocketConnection/FixedURLConnection
	 * @throws WebServiceInvocationException	runtime exception
	 */
	private HttpConnection getHttpConnection(URLWrapper urlWrapper,
			HttpConnectionFactory httpConnectionFactory)
			throws WebServiceInvocationException {
		if (urlWrapper == null) {
			throw new IllegalArgumentException("the argument 'url' is required");
		}
		try {
			return httpConnectionFactory.getHttpConnection(urlWrapper);
		} catch (SocketException e) {
            throw new WebServiceInvocationException(urlWrapper.toString(), e);
        } catch (IOException e) {
			throw new WebServiceInvocationException(urlWrapper.toString(), e);
		}
	}
	
	/**
	 * get http connection with http context
	 * 
	 * @param urlWrapper					<code>URLWrapper</code> url with alias
	 * @param httpConnectionContext			<code>HttpConnectionContext</code>http connection context 
	 * @param httpConnectionFactory			<code>HttpConnectionFactory</code> be used to generate http connection ,instance such as HttpSocketConnectionFactory/PoolableHttpSocketConnectionFactory/FixedURLConnectionFactory
	 * @return httpConnectionWithContext	<code>HttpConnectionContextWrapper</code> be used to store sessionid in it
	 * @throws WebServiceInvocationException
	 * @throws IOException
	 */
	private HttpConnectionContextWrapper getHttpConnectionWithContext(
			URLWrapper urlWrapper, HttpConnectionContext httpConnectionContext,
			HttpConnectionFactory httpConnectionFactory)
			throws WebServiceInvocationException, IOException {
		HttpConnection httpConnection = httpConnectionFactory
				.getHttpConnection(urlWrapper);
        HttpConnectionContextWrapper httpConnectionWithContext = new HttpConnectionContextWrapper(
				httpConnection, httpConnectionContext);
		return httpConnectionWithContext;
	}

	/**
	 * specific base execute method realization ,all client use this method to post/get parameters to server 
	 * 
	 * @param wrapper						<code>URLWrapper</code> url with alias
	 * @param httpConnectionContext			<code>HttpConnectionContext</code>http connection context 
	 * @param httpConnectionFactory			<code>HttpConnectionFactory</code> be used to generate http connection ,instance such as HttpSocketConnectionFactory/PoolableHttpSocketConnectionFactory/FixedURLConnectionFactory
	 * @param httpConnectionTemplate		<code>HttpConnectionTemplate</code>http connection (request header , http method , content type , url encoding,parameters) ready 
	 * @return response object
	 */
	public Object execute(URLWrapper wrapper,
			HttpConnectionContext httpConnectionContext,
			HttpConnectionFactory httpConnectionFactory,
			HttpConnectionTemplate httpConnectionTemplate) {
		int statusCode;
		Object o = null;
		HttpConnection httpConnection = null;
		try {
			httpConnection = httpConnectionContext == null ? getHttpConnection(
					wrapper, httpConnectionFactory)
					: getHttpConnectionWithContext(wrapper,
							httpConnectionContext, httpConnectionFactory);
			if (null != httpConnectionTemplate) {
				httpConnectionTemplate.beforeExecute(httpConnection);
			}
			statusCode = httpConnection.execute();
            if (!VALID_STATUS_SET.contains(statusCode)) {
                String content;
                try {
                    content = IOUtils.toString(httpConnection
							.getResponseReader());
                } catch (IOException e) {
                    content = "unknown";
                }
                throw new WebServiceInvocationException(wrapper.toString(), statusCode, content);
            }
            if (null != httpConnectionTemplate) {
				o = httpConnectionTemplate.afterExecute(httpConnection);
			}

		} catch (SocketException e) {
            throw new WebServiceInvocationException(wrapper.toString(), e);
        } catch (IOException e) {
			throw new WebServiceInvocationException(wrapper.toString(), e);
		} finally {
			if (httpConnection != null) {
				try {
					httpConnection.close();
				} catch (Exception e) {
					Const.LOG.error("Close httpConnection ["
							+ httpConnection + "] failed", e);
				}
			}
		}
		return o;
	}
}
