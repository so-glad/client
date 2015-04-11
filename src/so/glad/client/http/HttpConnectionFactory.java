package so.glad.client.http;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Palmtale
 * 2015-03-25
 */
public interface HttpConnectionFactory {

    HttpConnection getHttpConnection(URLWrapper urlWrapper) throws IOException;

    void setSSLSocketFactory(SSLSocketFactory sslSocketFactory);

    HashMap<String, URLSetting> getSettings();

    void setSettings(HashMap<String, URLSetting> settings);

    URLSetting getURLSetting(String alias);

    void setInetAddressFactory(InetAddressFactory inetAddressFactory);
}
