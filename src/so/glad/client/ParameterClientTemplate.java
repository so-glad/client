package so.glad.client;


import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpUtils;
import so.glad.client.http.StreamableString;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * template of ParameterClient to prepare some parameters for http connection and get response from server
 *
 * @author Palmtale
 * 2015-3-25
 */
public class ParameterClientTemplate extends BaseHttpConnectionTemplate
		implements HttpConnectionTemplate {

    private Map<String, Object> parameters;

	/**
	 * Construction of ParameterClientTemplate with parameters and http method
	 * 
	 * @param parameters	<code>Map</code> key and value of http request parameters
	 * @param httpMethod	<code>String</code> http connection method
	 */
	public ParameterClientTemplate(Map<String, Object> parameters,
			String httpMethod) {
        super();
        this.parameters = parameters;
        this.setHttpMethod(httpMethod);
    }

	/**
	 * get request parameters 
	 * 
	 * @return parameters      <code>Map</code> key and value of http request parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * set request parameters  
	 * 
	 * @param parameters	<code>Map</code> key and value of http request parameters
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * set http method and request parameters
	 * 
	 * @param httpConnection <code>HttpConnection</code> http connection can post/get request parameter to server,instance such as link HttpSocketConnection/PoolableHttpSocketConnection/FixedURLConnection
	 */
	public void beforeExecute(HttpConnection httpConnection) {
        this.setStreamable(null);
        try {
            if(HttpConnection.METHOD_GET.equals(getHttpMethod())) {
                httpConnection.setRequestParameters(parameters);
                if (getLog().isInfoEnabled()) {
                    getLog().info(this.getRequestId() + " request >| " + HttpUtils.getQueryString(parameters, this.getUrlEncoding()));
                }
            } else if(HttpConnection.METHOD_POST.equals(getHttpMethod())) {
                String queryString = HttpUtils.getQueryString(parameters, this.getEncoding());
                this.setStreamable(new StreamableString(queryString, this.getEncoding()));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Illegal charset", e);
        }

        super.beforeExecute(httpConnection);
	}

}
