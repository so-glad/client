package so.glad.client.http.impl;

import so.glad.client.http.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 * @author Palmtale
 * 2015-03-25
 */
public final class FixedURLConnectionFactory extends BaseConnectionFactory implements HttpConnectionFactory {
    public FixedURLConnectionFactory() {
    }

    @Override
    public HttpConnection getHttpConnection(URLWrapper urlWrapper) throws IOException {
        URL url = urlWrapper.getURL();
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(HttpUtils.getTimeoutMilliseconds(getURLSetting(urlWrapper.getAlias()).getConnectTimeout()));
        httpURLConnection.setReadTimeout(HttpUtils.getTimeoutMilliseconds(getURLSetting(urlWrapper.getAlias()).getTimeout()));

        FixedURLConnection fixedURLConnection = new FixedURLConnection();
        fixedURLConnection.setHttpURLConnection(httpURLConnection);
        fixedURLConnection.setUrl(url);

        fixedURLConnection.setRequestHeader(HttpConnection.HEADER_CONNECTION, HttpConnection.HEADER_VALUE_CONNECTION_CLOSE);
        return fixedURLConnection;
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }

    @Override
    public void setInetAddressFactory(InetAddressFactory inetAddressFactory) {
    }
}
