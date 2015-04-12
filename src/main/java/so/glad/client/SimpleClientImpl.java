package so.glad.client;

import so.glad.client.http.*;

import java.io.InputStream;
import java.io.Reader;

/**
 * Simple client realization is used to get some response by get or post
 * @author Palmtale
 * 2015-03-25
 */
public class SimpleClientImpl extends BaseClient implements SimpleClient {

    /**
     * get the response from a url with a http connection context
     *
     * @param urlWrapper            url with alias
     * @param data                  the data to post
     * @param httpConnectionContext the http connection context
     * @return the response string
     */
    public String post(URLWrapper urlWrapper, String data,
                       HttpConnectionContext httpConnectionContext) {
        return (String) executePostOrGet(urlWrapper,
                new StreamableString(data, this.getEncoding()),
                httpConnectionContext,
                HttpConnection.METHOD_POST,
                HttpConnectionTemplate.RESPONSE_DATA_TYPE_STRING);
    }

    /**
     * post some datastream to a url to get the response
     *
     * @param urlWrapper            url with alias
     * @param dataStream            the data stream to post
     * @param httpConnectionContext the http connection context
     * @return the response string
     */
    public InputStream post(URLWrapper urlWrapper, InputStream dataStream,
                            HttpConnectionContext httpConnectionContext) {

        return (InputStream) executePostOrGet(urlWrapper,
                new StreamableInputStream(dataStream),
                httpConnectionContext,
                HttpConnection.METHOD_POST,
                HttpConnectionTemplate.RESPONSE_DATA_TYPE_STREAM);
    }

    /**
     * post some data in data reader to a url to get the response
     *
     * @param urlWrapper            url with alias
     * @param dataReader            the data in data reader to post
     * @param httpConnectionContext the http connection context
     * @return the response string
     */
    public Reader post(URLWrapper urlWrapper, Reader dataReader,
                       HttpConnectionContext httpConnectionContext) {

        return (Reader) executePostOrGet(urlWrapper,
                new StreamableReader(dataReader, this.getEncoding()),
                httpConnectionContext,
                HttpConnection.METHOD_POST,
                HttpConnectionTemplate.RESPONSE_DATA_TYPE_READER);
    }

    /**
     * post some data to a url to get the response
     *
     * @param urlWrapper url with alias
     * @param data       the data to post
     * @return the response string
     */
    public String post(URLWrapper urlWrapper, String data) {
        return post(urlWrapper, data, new HttpConnectionContext());
    }

    /**
     * post some datastream to a url to get the response
     *
     * @param urlWrapper url with alias
     * @param dataStream the data stream to post
     * @return the response string
     */
    public InputStream post(URLWrapper urlWrapper, InputStream dataStream) {
        return post(urlWrapper, dataStream, new HttpConnectionContext());
    }

    /**
     * post some data in data reader to a url to get the response
     *
     * @param urlWrapper url with alias
     * @param dataReader the data in data reader to post
     * @return the response string
     */
    public Reader post(URLWrapper urlWrapper, Reader dataReader)  {
        return post(urlWrapper, dataReader, new HttpConnectionContext());
    }

    @Override
    public String post(String url, String data) {
        return this.post(getDefaultWrapper(url), data);
    }

    @Override
    public String post(String url, String data, HttpConnectionContext httpConnectionContext) {
        return this.post(getDefaultWrapper(url), data, httpConnectionContext);
    }

    @Override
    public void put(String url, InputStream inputStream, HttpConnectionContext httpConnectionContext) {
        executePostOrGet(getDefaultWrapper(url),
                new StreamableInputStream(inputStream),
                httpConnectionContext,
                HttpConnection.METHOD_PUT,
                HttpConnectionTemplate.RESPONSE_DATA_TYPE_STREAM);
    }

    private URLWrapper getDefaultWrapper(String url) {
        return new URLWrapper(url, "default");
    }

    /**
     * get the response from a url with a http connection context use post/get method
     * 
     * @param urlWrapper			url with alias
     * @param streamable					the data to post
     * @param httpConnectionContext	the http connection context
     * @param method				http connection method
     * @return	string 				response from server
     */
    private Object executePostOrGet(URLWrapper urlWrapper,
                                    Streamable streamable,
                                    HttpConnectionContext httpConnectionContext,
                                    String method,
                                    int responseDataType) {
        BaseHttpConnectionTemplate simpleClientTemplate = new BaseHttpConnectionTemplate();
        simpleClientTemplate.setHttpMethod(method);
        simpleClientTemplate.setStreamable(streamable);
        simpleClientTemplate.setLog(Const.getStreamLog(urlWrapper.getAlias()));
        simpleClientTemplate.setLogEnabled(isLogEnabled());
        simpleClientTemplate.setResponseDataType(responseDataType);
        return execute(urlWrapper, httpConnectionContext, simpleClientTemplate);
    }

    @Override
    public InputStream getAsStream(URLWrapper urlWrapper, HttpConnectionContext httpConnectionContext) {
        return (InputStream) executePostOrGet(urlWrapper, null,
                httpConnectionContext,
                HttpConnection.METHOD_GET,
                HttpConnectionTemplate.RESPONSE_DATA_TYPE_STREAM);
    }

    @Override
    public Reader getAsReader(URLWrapper urlWrapper, HttpConnectionContext httpConnectionContext) {
        return (Reader) executePostOrGet(urlWrapper, null,
                httpConnectionContext,
                HttpConnection.METHOD_GET,
                HttpConnectionTemplate.RESPONSE_DATA_TYPE_READER);
    }

    /**
     * get the response from a url with a http connection context
     *
     * @param urlWrapper            url with alias
     * @param httpConnectionContext the http connection context
     * @return the response string
     */
    public String get(URLWrapper urlWrapper, HttpConnectionContext httpConnectionContext) {
        return executePostOrGet(urlWrapper, null,
                httpConnectionContext,
                HttpConnection.METHOD_GET,
                HttpConnectionTemplate.RESPONSE_DATA_TYPE_STRING).toString();
    }

    @Override
    public InputStream getAsStream(URLWrapper urlWrapper) {
        return getAsStream(urlWrapper, new HttpConnectionContext());
    }

    @Override
    public InputStream getAsStream(String url) {
        return getAsStream(getDefaultWrapper(url), new HttpConnectionContext());
    }

    @Override
    public InputStream getAsStream(String url, HttpConnectionContext httpConnectionContext) {
        return getAsStream(getDefaultWrapper(url), httpConnectionContext);
    }

    @Override
    public Reader getAsReader(URLWrapper urlWrapper) {
        return getAsReader(urlWrapper, new HttpConnectionContext());
    }

    @Override
    public Reader getAsReader(String url) {
        return getAsReader(getDefaultWrapper(url), new HttpConnectionContext());
    }

    @Override
    public Reader getAsReader(String url, HttpConnectionContext httpConnectionContext) {
        return getAsReader(getDefaultWrapper(url), httpConnectionContext);
    }

    @Override
    public String get(URLWrapper urlWrapper) {
        return get(urlWrapper, new HttpConnectionContext());
    }

    @Override
    public String get(String url) {
        return this.get(getDefaultWrapper(url));
    }

    @Override
    public String get(String url, HttpConnectionContext httpConnectionContext) {
        return this.get(getDefaultWrapper(url), httpConnectionContext);
    }
}
