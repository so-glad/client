package so.glad.client;

import java.util.Map;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.URLWrapper;

/**
 * ParameterClient is used to send parameters to get response data
 * @author Palmtale
 * 2015-3-25
 */
public interface ParameterClient extends Client {
    /**
     * set the http method for the client to send parameters,
     * when the value is 'GET', the parameters will be sent in the url
     * when the value is 'POST'. the parameters will be sent in the body stream
     * the default value is 'POST'
     * @param httpMethod the http method used to send parameters
     */
    void setHttpMethod(String httpMethod);

    /**
     * send the parameters to a url
     * @param urlWrapper url with alias
     * @param parameters the parameters to send
     * @return the response string
     */
    String service(URLWrapper urlWrapper, Map<String, Object> parameters);

    /**
     * send the parameters to a url with a http connection context
     * @param urlWrapper url with alias
     * @param parameters the parameters to send
     * @param httpConnectionContext the http connection context
     * @return the response string
     */
    String service(URLWrapper urlWrapper, Map<String, Object> parameters,
                   HttpConnectionContext httpConnectionContext);

    /**
     * send the parameters to a url by a specific http method
     * @param urlWrapper url with alias
     * @param parameters the parameters to send
     * @param httpMethod the specific http method
     * @return the response string
     */
    String service(URLWrapper urlWrapper, Map<String, Object> parameters, String httpMethod);

    /**
     * send the parameters to a url with a http connection context by a specific http method
     * @param urlWrapper url with alias
     * @param parameters the parameters to send
     * @param httpMethod the specific http method
     * @param httpConnectionContext the http connection context
     * @return the response string
     */
    String service(URLWrapper urlWrapper, Map<String, Object> parameters, String httpMethod,
                   HttpConnectionContext httpConnectionContext);
}
