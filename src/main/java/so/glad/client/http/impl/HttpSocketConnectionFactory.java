package so.glad.client.http.impl;

import so.glad.client.http.*;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

/**
 * @author Palmtale
 * 2015-3-25
 */
public class HttpSocketConnectionFactory extends BaseConnectionFactory implements HttpConnectionFactory {
    private SSLSocketFactory sslSocketFactory;
    private InetAddressFactory inetAddressFactory = new InetAddressFactoryImpl();

    public HttpSocketConnectionFactory() {
    }

    @Override
    public HttpConnection getHttpConnection(URLWrapper urlWrapper) throws IOException {
        HttpSocketConnection httpSocketConnection = new HttpSocketConnection();
        URL url = urlWrapper.getURL();
        Socket socket = SocketUtils.createSocket(
                getSSLSocketFactory(),
                getInetAddressFactory(),
                url,
                getURLSetting(urlWrapper.getAlias()).getConnectTimeout());
        socket.setSoTimeout(HttpUtils.getTimeoutMilliseconds(getURLSetting(urlWrapper.getAlias()).getTimeout()));
        socket.setKeepAlive(false);
        httpSocketConnection.setSocket(socket);
        httpSocketConnection.setPath(url.getPath());

        httpSocketConnection.setRequestParameters(HttpUtils.getParameters(url));
        return httpSocketConnection;
    }


    public SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public InetAddressFactory getInetAddressFactory() {
        return inetAddressFactory;
    }

    @Override
    public void setInetAddressFactory(InetAddressFactory inetAddressFactory) {
        this.inetAddressFactory = inetAddressFactory;
    }
}
