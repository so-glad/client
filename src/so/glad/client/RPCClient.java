package so.glad.client;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.URLWrapper;

/**
 * RPCClient
 *
 * @author Palmtale
 * 2015-03-25
 */
public interface RPCClient {
    Object service(String url, String method, Object... parameters);

    Object service(URLWrapper urlWrapper, String method, Object... parameters);

    Object service(URLWrapper urlWrapper, String method, HttpConnectionContext httpConnectionContext, Object... parameters);

    <T> T service(String url, String method, Class<T> clazz, Object... parameters);

    <T> T service(URLWrapper urlWrapper, String method, Class<T> clazz, Object... parameters);

    <T> T service(URLWrapper urlWrapper, String method, Class<T> clazz, HttpConnectionContext httpConnectionContext, Object... parameters);
}
