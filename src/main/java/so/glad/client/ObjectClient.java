package so.glad.client;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.URLWrapper;
import so.glad.serializer.StreamSerializer;

/**
 * ObjectClient is used to send request object to get response object
 * @author Palmtale
 * 2015-3-25
 */
public interface ObjectClient extends Client {
    /**
     * send a request object to a url to get response object
     * @param urlWrapper url with alias
     * @param request the request
     * @return the response object
     */
    public Object service(URLWrapper urlWrapper, Object request);

    /**
     * send a request object to a url to get response object with http
     * connection context
     * @param urlWrapper url with alias
     * @param request the request
     * @param httpConnectionContext the http connection context
     * @return the response object
     */
    public Object service(URLWrapper urlWrapper, Object request,
                          HttpConnectionContext httpConnectionContext);

    /**
     * set the stream serializer for object serializing
     * @param streamSerializer the object stream serializer
     */
	void setStreamSerializer(StreamSerializer streamSerializer);
}
