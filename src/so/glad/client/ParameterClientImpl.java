package so.glad.client;

import java.util.Map;
import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.URLWrapper;
/**
 *	Provide multi-modal service method realization
 *
 * @author Palmtale
 * 2015-3-25
 * 
 */
public class ParameterClientImpl extends BaseClient implements ParameterClient {
    private String httpMethod = HttpConnection.METHOD_GET;

    public ParameterClientImpl() {
    	this.setContentType(HttpConnection.DEFAULT_CONTENT_TYPE);
	}
    
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * assign http connection method 
     * 
     * @param httpMethod
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * service to post/get request to server without http connection context
     * 
     * @param wrapper	   				<code>URLWrapper</code> url with alias
     * @param parameters   				<code>Map</code> key and value of http request parameters
     */
    public String service(URLWrapper wrapper, Map<String, Object> parameters) {
		return service(wrapper, parameters, new HttpConnectionContext());
	}

    /**
     * service to post/get request to server with http connection context
     * 
     * @param wrapper	   				<code>URLWrapper</code> url with alias
     * @param parameters   				<code>Map</code> key and value of http request parameters
     * @param httpConnectionContext		<code>HttpConnectionContext</code>	http connection context contain session name and session id 
     */
	public String service(URLWrapper wrapper, Map<String, Object> parameters,
			HttpConnectionContext httpConnectionContext) {
		return this.service(wrapper, parameters, this.httpMethod, httpConnectionContext);
	}

	/**
	 * service to post/get request to server without http connection context
	 * 
	 * @param urlWrapper				<code>URLWrapper</code> url with alias
	 * @param parameters				<code>Map</code> key and value of http request parameters
	 * @param httpMethod				<code>String</code> http connection method
	 */
    @Override
    public String service(URLWrapper urlWrapper, Map<String, Object> parameters, String httpMethod) throws WebServiceInvocationException {
        return service(urlWrapper, parameters, httpMethod, new HttpConnectionContext());
    }

    /**
     * service to post/get request to server with http connection context
     * 
     * @param urlWrapper				<code>URLWrapper</code> url with alias
     * @param parameters				<code>Map</code> key and value of http request parameters
     * @param httpMethod				<code>String</code> http connection method
     * @param httpConnectionContext		<code>HttpConnectionContext</code>	http connection context contain session name and session id
     */
    @Override
    public String service(URLWrapper urlWrapper, Map<String, Object> parameters, String httpMethod, HttpConnectionContext httpConnectionContext) throws WebServiceInvocationException {
        ParameterClientTemplate parameterClientTemplate = new ParameterClientTemplate(parameters, httpMethod);
        parameterClientTemplate.setLog(Const.getStreamLog(urlWrapper.getAlias()));
        parameterClientTemplate.setLogEnabled(this.isLogEnabled());
        Object object = execute(urlWrapper, httpConnectionContext, parameterClientTemplate);
        return object.toString();
    }
}
