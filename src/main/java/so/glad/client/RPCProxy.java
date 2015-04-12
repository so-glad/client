package so.glad.client;

import java.util.List;

/**
 * RPCProxy
 */
public interface RPCProxy {
	
    Object invoke(List<?> parameters);

    Object invoke(Object... parameters);

    void setUrl(String url);

    void setMethod(String method);

    void setEncoding(String encoding);

    void setContentType(String contentType);

    void setConnectTimeout(int connectTimeout);

    void setTimeout(int timeout);

    void setCompress(boolean compress);

    @Deprecated
    void setHead(String key, String value);

    void setHeader(String key, String value);
}
