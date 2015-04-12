package so.glad.client;

import java.io.*;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import so.glad.client.http.HttpConnection;
import so.glad.client.http.Streamable;
import so.glad.client.http.StreamableString;


/**
 * base to initialize some parameters of httpConection such as (url encoding , request header , content type , encoding) and get response from server 
 *
 * @author Palmtale
 * 2015-3-25
 */
public class BaseHttpConnectionTemplate implements HttpConnectionTemplate {
	private String urlEncoding;
	private String contentType;
	private String encoding;
    private Logger log;
    
    private String requestId;

    private int responseDataType;

    private Streamable streamable;

    private String httpMethod;

    private boolean logEnabled = true;

    public BaseHttpConnectionTemplate() {
        requestId = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * be used to set request header , content type , url encoding , parameters encoding
     * 
     * @param httpConnection	http connection
     */
    @Override
	public void beforeExecute(HttpConnection httpConnection) {
		httpConnection.setUrlEncoding(urlEncoding);
        httpConnection.setRequestContentType(contentType, encoding);
        httpConnection.setRequestHeader(
				HttpConnection.HEADER_ACCEPT_ENCODING,
				HttpConnection.HEADER_VALUE_CONTENT_ENCODING_GZIP);

        httpConnection.setHttpMethod(httpMethod);
        if(logEnabled && getLog().isInfoEnabled()) {
            if(streamable != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                String buffer;
                try {
                    streamable.write(byteArrayOutputStream);
                    buffer = byteArrayOutputStream.toString(this.getEncoding());
                } catch (Exception e) {
                    throw new RuntimeException("log stream failed", e);
                }
                getLog().info(this.getRequestId() + " request >| " + buffer);
                streamable = new StreamableString(buffer, this.getEncoding());
            }
        }
        httpConnection.setRequestStream(streamable);
	}

	/**
	 * be used to get response from server
	 * 
	 * @param httpConnection	<code>HttpConnection</code> http connection can post/get request parameter to server,instance such as link HttpSocketConnection/PoolableHttpSocketConnection/FixedURLConnection
	 */
    @Override
    public Object afterExecute(HttpConnection httpConnection) throws IOException {
        InputStream inputStream = httpConnection.getResponseStream();
        String encoding = getEncoding(Const.DEFAULT_ENCODING);
        if(logEnabled && getLog().isInfoEnabled()) {
            String buffer = IOUtils.toString(inputStream, encoding);
            getLog().info(getRequestId() + " response <| " + buffer);
            switch (getResponseDataType()) {
                case RESPONSE_DATA_TYPE_STREAM: {
                    return  new ByteArrayInputStream(buffer.getBytes(encoding));
                }
                case RESPONSE_DATA_TYPE_READER: {
                    return new StringReader(buffer);
                }
                default: {
                    return buffer;
                }
            }
        }

        switch (getResponseDataType()) {
            case RESPONSE_DATA_TYPE_STREAM: {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, byteArrayOutputStream);
                byteArrayOutputStream.flush();
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            }
            case RESPONSE_DATA_TYPE_READER: {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, byteArrayOutputStream);
                byteArrayOutputStream.flush();
                return new InputStreamReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), encoding);
            }
            default: {
                return IOUtils.toString(inputStream, encoding);
            }
        }
    }

    /**
     * get url encoding
     * 
     * @return urlEncoding
     */
    public String getUrlEncoding() {
		return urlEncoding;
	}
    
    /**
     * set url encoding
     * 
     * @param urlEncoding  
     */
	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

	/**
	 * get content type
	 * 
	 * @return contentType 
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * set content type
	 * 
	 * @param contentType type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * get encoding
	 * 
	 * @return encoding
	 */
	public String getEncoding() {
		return encoding == null ? Const.DEFAULT_ENCODING : encoding;
	}
	
	/**
	 * set encoding
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * get encoding
	 * 
	 * @param defaultEncoding
	 * @return encoding
	 */
	public String getEncoding(String defaultEncoding) {
		return encoding == null ? defaultEncoding : encoding;
	}

	/**
	 * get log 
	 * 
	 * @return log
	 */
    public Logger getLog() {
        return log != null ? log : Const.getStreamLog("unknown");
    }

    /**
     * set log
     * 
     * @param log
     */
    public void setLog(Logger log) {
        this.log = log;
    }

    public String getRequestId() {
        return requestId;
    }

    public int getResponseDataType() {
        return responseDataType;
    }

    public void setResponseDataType(int responseDataType) {
        this.responseDataType = responseDataType;
    }

    public Streamable getStreamable() {
        return streamable;
    }

    public void setStreamable(Streamable streamable) {
        this.streamable = streamable;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }
}
