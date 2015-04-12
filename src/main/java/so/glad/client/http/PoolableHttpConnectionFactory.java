package so.glad.client.http;

import java.io.IOException;

/**
 * @author Palmtale
 * 2015-03-25
 */
public interface PoolableHttpConnectionFactory extends HttpConnectionFactory {
    
	int DEFAULT_CONNECTION_SIZE = 5;
   
    int DEFAULT_IDLE_TIMEOUT = 60 * 3;

    void setIdleTimeout(int second);

    void destroy();

    @Override
    HttpConnection getHttpConnection(URLWrapper urlWrapper) throws IOException;
}
