package so.glad.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.impl.HttpSocketConnectionFactory;
import so.glad.client.https.AlwaysTrustSSLSocketFactory;

/**
 * @author Palmtale
 * 2015-3-25
 */
public class Const {
    public static final Logger LOG = LoggerFactory.getLogger(Const.class);

    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_TEXT_XML = "text/xml";
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static HttpConnectionFactory defaultHttpConnectionFactory;

    public static Logger getStreamLog(String urlAlias) {
        return LoggerFactory.getLogger("http.StreamLog." + urlAlias);
    }

    public static Logger getAccessLog() {
        return LoggerFactory.getLogger("http.AccessLog");
    }

    public synchronized static HttpConnectionFactory getDefaultHttpConnectionFactory() {
        if (defaultHttpConnectionFactory == null) {
            defaultHttpConnectionFactory = new HttpSocketConnectionFactory();
            defaultHttpConnectionFactory.setSSLSocketFactory(new AlwaysTrustSSLSocketFactory());
        }
        return defaultHttpConnectionFactory;
    }

    public synchronized static void setDefaultHttpConnectionFactory(HttpConnectionFactory httpConnectionFactory) {
        defaultHttpConnectionFactory = httpConnectionFactory;
    }
}
