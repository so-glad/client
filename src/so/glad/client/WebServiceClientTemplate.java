package so.glad.client;

import so.glad.client.http.HttpConnection;
import so.glad.serializer.StreamSerializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * template of WebServiceClient to prepare some parameters for http 
 * connection and get response from server,finish marshal and unmarshal
 *
 * @author Palmtale
 * 2015-03-25
 */
public class WebServiceClientTemplate extends BaseHttpConnectionTemplate implements HttpConnectionTemplate {

	private StreamSerializer streamSerializer;

	private Object request;

	/**
	 * construction of WebServiceClientTemplate to initialize streamSerializer and content type
	 * 
	 * @param streamSerializer
	 * @param request
	 */
    public WebServiceClientTemplate(StreamSerializer streamSerializer,Object request) {
		this.streamSerializer = streamSerializer;
		this.request = request;
        this.setContentType(Const.CONTENT_TYPE_TEXT_XML);
        this.setResponseDataType(RESPONSE_DATA_TYPE_STREAM);
	}

    /**
	 * set http method and request parameters
	 * 
	 * @param httpConnection <code>HttpConnection</code> http connection can post/get request parameter to server,instance such as link HttpSocketConnection/PoolableHttpSocketConnection/FixedURLConnection
	 */
	@Override
	public void beforeExecute(HttpConnection httpConnection) {
        this.setHttpMethod(HttpConnection.METHOD_POST);
        this.setStreamable(new StreamableObject(streamSerializer, request, this.getEncoding()));
        super.beforeExecute(httpConnection);
    }

	/**
	 * be used to get response from server
	 * 
	 * @param httpConnection	<code>HttpConnection</code> http connection can post/get request parameter to server,instance such as link HttpSocketConnection/PoolableHttpSocketConnection/FixedURLConnection
	 */
	@Override
	public Object afterExecute(HttpConnection httpConnection) throws IOException {
		InputStream inputStream = (InputStream) super.afterExecute(httpConnection);
        String encoding = getEncoding(Const.DEFAULT_ENCODING);
		return streamSerializer.unmarshal(inputStream, encoding);
	}

	/**
	 * get streamSerializer
	 * 
	 * @return streamSerializer
	 */
	public StreamSerializer getStreamSerializer() {
		return streamSerializer;
	}

	/**
	 * set streamSerializer
	 * 
	 * @param streamSerializer
	 */
	public void setStreamSerializer(StreamSerializer streamSerializer) {
		this.streamSerializer = streamSerializer;
	}

	/**
	 * get request
	 * 
	 * @return	request
	 */
	public Object getRequest() {
		return request;
	}

	/**
	 * set request
	 * 
	 * @param request
	 */
	public void setRequest(Object request) {
		this.request = request;
	}

}
