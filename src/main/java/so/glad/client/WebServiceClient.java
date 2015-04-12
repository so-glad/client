package so.glad.client;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.URLWrapper;
import so.glad.serializer.StreamSerializer;

/**
 * Object client realization is used to get some response by get or post
 *
 * @author Palmtale
 * 2015-03-25
 * */
public class WebServiceClient extends BaseClient implements ObjectClient {
	private StreamSerializer streamSerializer;

	/**
	 * construction of WebServiceClient to initialize executor
	 */
    public WebServiceClient() {
        this.setContentType(Const.CONTENT_TYPE_TEXT_XML);
    }

    /**
     * service to post/get request to server without http connection context
     * 
     * @param urlWrapper	   				<code>URLWrapper</code> url with alias
     * @param request   				<code>Object</code> request content usually is xml file
     */
    public Object service(URLWrapper urlWrapper, Object request) {
		return service(urlWrapper, request, new HttpConnectionContext());
	}

    /**
     * service to post/get request to server with http connection context
     * 
     * @param urlWrapper	   				<code>URLWrapper</code> url with alias
     * @param request   				<code>Object</code> request content usually is xml file
     * @param httpConnectionContext		<code>HttpConnectionContext</code>	http connection context contain session name and session id 
     */
	public Object service(URLWrapper urlWrapper, Object request,
			HttpConnectionContext httpConnectionContext) {
		HttpConnectionTemplate httpConnectionTemplate = createHttpConnectionTemplate(request);
        httpConnectionTemplate.setLog(Const.getStreamLog(urlWrapper.getAlias()));
        httpConnectionTemplate.setLogEnabled(this.isLogEnabled());
        return execute(urlWrapper, httpConnectionContext, httpConnectionTemplate);
	}

	/**
	 * create http connection template instance
	 * 
	 * @param request					<code>Object</code> request content usually is xml file
	 * @return webServiceClientTemplate
	 */
    public HttpConnectionTemplate createHttpConnectionTemplate(Object request) {
        return new WebServiceClientTemplate(streamSerializer, request);
    }

    /**
     * get streamserializer
     *
     * @return streamSerializer
     */
	public StreamSerializer getStreamSerializer() {
		return streamSerializer;
	}

	/**
	 * set streamserializer
	 * 
	 * @param streamSerializer
	 */
	public void setStreamSerializer(StreamSerializer streamSerializer) {
		this.streamSerializer = streamSerializer;
	}
}
