package so.glad.client.https;

import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class AlwaysTrustHostnameVerifier implements HostnameVerifier {
    public boolean verify(String s, SSLSession sslSession) {
        LoggerFactory.getLogger(this.getClass()).debug("Ignore host name verifying");
        return true;
    }
}
