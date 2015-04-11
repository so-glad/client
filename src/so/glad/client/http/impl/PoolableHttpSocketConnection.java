package so.glad.client.http.impl;

import so.glad.client.http.Constant;
import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpUtils;
import so.glad.client.http.impl.io.HttpSocketRequestStream;
import so.glad.client.http.impl.io.HttpSocketResponseStream;
import so.glad.client.http.impl.io.NoncloseInputStream;
import so.glad.client.http.impl.io.NoncloseOutputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Palmtale
 * 2015-03-25
 */
public class PoolableHttpSocketConnection extends HttpSocketConnection {
    private SocketWrapper socketWrapper;
    private PoolableHttpSocketConnectionFactory poolableHttpSocketConnectionFactory;

    public PoolableHttpSocketConnectionFactory getPoolableHttpSocketConnectionFactory() {
        return poolableHttpSocketConnectionFactory;
    }

    public void setPoolableHttpSocketConnectionFactory(PoolableHttpSocketConnectionFactory poolableHttpSocketConnectionFactory) {
        this.poolableHttpSocketConnectionFactory = poolableHttpSocketConnectionFactory;
    }

    private static InputStream getNoncloseInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        } else {
            return new NoncloseInputStream(inputStream);
        }
    }

    /**
     * Release the connection, not closing the connection
     */
    @Override
    public void close() {
        try {
            if (this.getResponseStream() != null) {
                if (this.getResponseStream() instanceof NoncloseInputStream) {
                    NoncloseInputStream noncloseInputStream = (NoncloseInputStream) this.getResponseStream();
                    if (!(noncloseInputStream.getInputStream() instanceof HttpSocketResponseStream)) {
                        throw new IllegalStateException("Invalid response stream for socket connection");
                    } else {
                        ((HttpSocketResponseStream) noncloseInputStream.getInputStream()).clear();
                    }
                } else {
                    throw new IllegalStateException("Invalid response stream for socket connection [" + this.getResponseStream() + "]");
                }
            }
        } catch (IOException e) {
            Constant.LOG.debug("Clear the response stream failed", e);
        } finally {
            this.setRequestStream(null);
            this.setResponseHeaders(null);
            this.setResponseStream(null);
            getPoolableHttpSocketConnectionFactory().release(socketWrapper);
            socketWrapper = null;
        }
    }

    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
        this.setSocket(socketWrapper.getSocket());
    }

    @Override
    protected void initHeaders() {
        super.initHeaders();
        this.setRequestHeader(HttpConnection.HEADER_CONNECTION,
                HttpConnection.HEADER_VALUE_CONNECTION_KEEP_ALIVE);

    }

    @Override
    protected int response(HttpSocketResponseStream httpSocketResponseStream) throws IOException {
        setResponseHeaders(httpSocketResponseStream.getHeaders());
        setResponseStream(getNoncloseInputStream(httpSocketResponseStream));
        if (HttpConnection.HEADER_VALUE_CONNECTION_CLOSE.equalsIgnoreCase(
                this.getResponseHeader(HttpConnection.HEADER_CONNECTION))) {
            socketWrapper.setMaxAliveTimeout(0);
        } else if(HttpConnection.HEADER_VALUE_CONNECTION_KEEP_ALIVE.equalsIgnoreCase
                (this.getResponseHeader(HttpConnection.HEADER_CONNECTION))
                && this.getResponseHeader(HttpConnection.HEADER_KEEP_ALIVE) != null){
            String keepAliveValue = this.getResponseHeader(HttpConnection.HEADER_KEEP_ALIVE);
            if(HttpConnection.HEADER_VALUE_KEEP_ALIVE_CLOSED.equalsIgnoreCase(keepAliveValue)) {
                socketWrapper.setMaxAliveTimeout(0);
            } else {
                socketWrapper.setIdleTimeout(HttpUtils.getTimeoutMilliseconds(HttpUtils.getKeepAliveTimeout(keepAliveValue)));
            }
        }
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

    @Override
    public int execute() throws IOException {
        try {
            return super.execute();
        } catch (IOException e) {
            socketWrapper.setMaxAliveTimeout(0);
            throw e;
        }
    }
}
