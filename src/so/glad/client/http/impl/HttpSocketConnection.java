package so.glad.client.http.impl;

import org.slf4j.Logger;
import so.glad.client.Const;
import so.glad.client.http.Constant;
import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpUtils;
import so.glad.client.http.StreamableString;
import so.glad.client.http.impl.io.HttpSocketRequestStream;
import so.glad.client.http.impl.io.HttpSocketResponseStream;
import so.glad.client.http.impl.io.NoncloseInputStream;
import so.glad.client.http.impl.io.NoncloseOutputStream;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author yangyang
 * @since 2009-3-18
 */
public class HttpSocketConnection extends BaseHttpConnection implements HttpConnection {
    
    public static final Logger ACCESS_LOG = Const.getAccessLog();
    
    protected Socket socket;

    @Override
    public int execute() throws IOException {
        initHeaders();

        if(this.getPath() == null || "".equals(this.getPath())) {
            this.setPath("/");
        }
        if (getRequestParameters() != null&&!getRequestParameters().isEmpty()) {
            if (getRequestStream() == null && METHOD_POST.equals(this.getHttpMethod())) {
                String queryString = HttpUtils.getQueryString(getRequestParameters(),
                        this.getRequestCharset());
                Constant.LOG.debug("Query stream >> " + queryString);
                this.setRequestStream(new StreamableString(queryString, this.getRequestCharset()));
            } else {
            	String queryString;
            	if(null!=this.getUrlEncoding()){
            		queryString = HttpUtils.getQueryString(getRequestParameters(),
            				 this.getUrlEncoding());
            	}else{
            		queryString = HttpUtils.getQueryString(getRequestParameters(),
           				 Constant.HTTP_META_DEFAULT_CHARSET);
            	}
                Constant.LOG.debug("Query string >> " + queryString);
                this.setPath(this.getPath() + "?" + queryString);
            }
        }
        HttpSocketRequestStream httpSocketRequestStream = getHttpSocketRequestStream();
        if (getRequestStream() != null) {
            getRequestStream().write(httpSocketRequestStream);
        }
        
        long beforeFlush = System.currentTimeMillis();
        httpSocketRequestStream.flush();

        int resultCode = response(new HttpSocketResponseStream(new NoncloseInputStream(socket.getInputStream())));
        long afterFlush = System.currentTimeMillis();
        ACCESS_LOG.debug(socket.getInetAddress().getHostName() + "|" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " \"" + httpSocketRequestStream.getPathLine() + "\" " + resultCode + " "
                + httpSocketRequestStream.getHeaders().get(HttpConnection.HEADER_CONTENT_LENGTH) + " rt=" + (afterFlush - beforeFlush) + " " + this.getResponseHeader(HttpConnection.HEADER_CONTENT_LENGTH));
        return resultCode;
    }

    protected int response(HttpSocketResponseStream httpSocketResponseStream) throws IOException {
        setResponseHeaders(httpSocketResponseStream.getHeaders());
        setResponseStream(httpSocketResponseStream);
        return httpSocketResponseStream.getStatusCode();
    }

    protected HttpSocketRequestStream getHttpSocketRequestStream() throws IOException {
        HttpSocketRequestStream httpSocketRequestStream;
        httpSocketRequestStream = new HttpSocketRequestStream(new NoncloseOutputStream(socket.getOutputStream()));
        httpSocketRequestStream.setHttpMethod(this.getHttpMethod());
        httpSocketRequestStream.setVersion("1.1");
        httpSocketRequestStream.setHeaders(this.getRequestHeaders());
        httpSocketRequestStream.setPath(this.getPath());
        return httpSocketRequestStream;
    }

    protected void initHeaders() {
        if (this.getRequestHeaders().getProperty(HEADER_HOST) == null) {
            this.setRequestHeader(HEADER_HOST, socket.getInetAddress().getHostName() + ":" + socket.getPort());
        }
        if (this.getRequestHeaders().getProperty(HEADER_USER_AGENT) == null) {
            this.setRequestHeader(HEADER_USER_AGENT, HEADER_VALUE_USER_AGENT_D_CLIENT);
        }
        if (this.getRequestHeaders().getProperty(HEADER_ACCEPT) == null) {
            this.setRequestHeader(HEADER_ACCEPT, HEADER_VALUE_ACCEPT_ALL);
        }
        if (this.getRequestHeaders().getProperty(HEADER_CONNECTION) == null) {
            this.setRequestHeader(HttpConnection.HEADER_CONNECTION, HttpConnection.HEADER_VALUE_CONNECTION_CLOSE);
        }
    }

    @Override
    public void close() {
        super.close();
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Constant.LOG.debug("Ignore Socket close exception", e);
            }
        }
        socket = null;
        this.setRequestHeaders(null);
        this.setRequestStream(null);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
