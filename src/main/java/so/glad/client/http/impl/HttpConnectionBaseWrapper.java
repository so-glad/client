package so.glad.client.http.impl;

import so.glad.client.http.HttpConnection;
import so.glad.client.http.Streamable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

/**
 * HttpConnectionBaseWrapper
 * @author Palmtale
 * 2015-3-25
 */
public abstract class HttpConnectionBaseWrapper implements HttpConnection {
    protected HttpConnection httpConnection;

    public HttpConnectionBaseWrapper(HttpConnection httpConnection) {
        this.httpConnection = httpConnection;
    }

    @Override
    public void setHttpMethod(String httpMethod) {
        httpConnection.setHttpMethod(httpMethod);
    }

    @Override
    public void setRequestHeader(String name, String value) {
        httpConnection.setRequestHeader(name, value);
    }

    @Override
    public void setRequestParameter(String name, Object value) {
        httpConnection.setRequestParameter(name, value);
    }

    @Override
    public void setRequestStream(Streamable requestStream) {
        httpConnection.setRequestStream(requestStream);
    }

    @Override
    public InputStream getResponseStream() {
        return httpConnection.getResponseStream();
    }

    @Override
    public Reader getResponseReader() {
        return httpConnection.getResponseReader();
    }

    @Override
    public Properties getResponseHeaders() {
        return httpConnection.getResponseHeaders();
    }

    @Override
    public String getResponseHeader(String name) {
        return httpConnection.getResponseHeader(name);
    }

    @Override
    public int execute() throws IOException {
        return httpConnection.execute();
    }

    @Override
    public void close() {
        httpConnection.close();
    }

    @Override
    public String getResponseCharset() {
        return httpConnection.getResponseCharset();
    }

    @Override
    public int getResponseLength() {
        return httpConnection.getResponseLength();
    }

    @Override
    public void setRequestContentType(String contentType, String charset) {
        httpConnection.setRequestContentType(contentType, charset);
    }

    @Override
    public String getResponseContentType() {
        return httpConnection.getResponseContentType();
    }

    @Override
    public String getPath() {
        return httpConnection.getPath();
    }

    @Override
    public void setRequestParameters(Map<String, Object> requestParameters) {
        httpConnection.setRequestParameters(requestParameters);
    }

    @Override
    public String getUrlEncoding() {
        return httpConnection.getUrlEncoding();
    }

    @Override
    public void setUrlEncoding(String urlEncoding) {
        httpConnection.setUrlEncoding(urlEncoding);
    }
}
