package so.glad.client.http.impl;

import so.glad.client.http.Constant;
import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpUtils;
import so.glad.client.http.Streamable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * BaseHttpConnection is the base implementation of HttpConnection
 *
 * @author Palmtale
 * 2015-03-25
 */
public abstract class BaseHttpConnection implements HttpConnection {

    private String httpMethod;
    private Properties requestHeaders;
    private Map<String, Object> requestParameters;

    private Streamable requestStream;

    private InputStream responseStream;
    private Properties responseHeaders;
    private String path;
    private String urlEncoding;
    
    public String getUrlEncoding() {
		return urlEncoding;
	}

	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}
    
    public InputStream getResponseStream() {
        return responseStream;
    }

    public Reader getResponseReader() {
        try {
            return new InputStreamReader(getResponseStream(), getResponseCharset());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Invalid response charset [" + getResponseCharset() + "]");
        }
    }

    @Override
    public Properties getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public String getResponseHeader(String name) {
        return getResponseHeaders().getProperty(name);
    }

    public BaseHttpConnection() {
        setRequestHeaders(new Properties());
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Properties getRequestHeaders() {
        return requestHeaders;
    }

    public Map<String, Object> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(Map<String, Object> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public void setRequestHeader(String name, String value) {
        getRequestHeaders().setProperty(name, value);
    }

    public void setRequestParameter(String name, Object value) {
        if(getRequestParameters() == null) {
            setRequestParameters(new HashMap<String, Object>());
        }
        getRequestParameters().put(name, value);
    }

    public Streamable getRequestStream() {
        return requestStream;
    }

    public void setRequestStream(Streamable requestStream) {
        this.requestStream = requestStream;
    }

    public String getRequestCharset() {
        String contentType = this.getRequestHeaders().getProperty(HEADER_CONTENT_TYPE);
        if(contentType != null && contentType.indexOf(CHARSET_TOKEN) > 0) {
            return HttpUtils.fromQuotedString(contentType.substring(contentType.indexOf(CHARSET_TOKEN) + CHARSET_TOKEN.length()));
        }
        return Constant.HTTP_BODY_DEFAULT_CHARSET;
    }

    public void setRequestContentType(String contentType, String charset) {
        if(charset != null) {
            contentType = contentType + CHARSET_TOKEN + charset;
        }
        this.getRequestHeaders().setProperty(HEADER_CONTENT_TYPE, contentType);
    }

    public String getResponseCharset() {
        String contentType = this.getResponseHeader(HEADER_CONTENT_TYPE);
        if(contentType != null && contentType.indexOf(CHARSET_TOKEN) > 0) {
            return HttpUtils.fromQuotedString(contentType.substring(contentType.indexOf(CHARSET_TOKEN) + CHARSET_TOKEN.length()));
        }
        return Constant.HTTP_BODY_DEFAULT_CHARSET;
    }

    public String getResponseContentType() {
        String contentType = this.getResponseHeader(HEADER_CONTENT_TYPE);
        if(contentType != null && contentType.indexOf(CHARSET_TOKEN) > 0) {
            return contentType.substring(0, contentType.indexOf(CHARSET_TOKEN));
        }
        return contentType;
    }

    public void setResponseStream(InputStream responseStream) {
        this.responseStream = responseStream;
    }

    public void setResponseHeaders(Properties responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    @Override
    public void close() {
        if(getResponseStream() != null) {
            try {
                getResponseStream().close();
            } catch (IOException e) {
                Constant.LOG.debug("Ignore response stream close exception", e);
            }
        }
    }

    public int getResponseLength() {
        String conentLength = this.getResponseHeader(HEADER_CONTENT_LENGTH);
        if(conentLength == null) {
            throw new IllegalStateException("Content-Length is missing in the response header");
        }
        return Integer.parseInt(conentLength);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRequestHeaders(Properties requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

	
}
