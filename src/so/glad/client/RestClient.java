package so.glad.client;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.URLWrapper;

/**
 * RestClient
 *
 * @author Palmtale
 * 2015-03-25
 */
public interface RestClient extends Client {
    public Object get(URLWrapper urlWrapper, String id);

    public Object put(URLWrapper urlWrapper, String id, Object object);

    public Object delete(URLWrapper urlWrapper, String id);

    public Object service(URLWrapper urlWrapper, Object object);

    public Object get(String url, String id);

    public Object put(String url, String id, Object object);

    public Object delete(String url, String id);

    public Object service(String url, Object object);

    public Object get(URLWrapper urlWrapper, String id, HttpConnectionContext httpConnectionContext);

    public Object put(URLWrapper urlWrapper, String id, Object object, HttpConnectionContext httpConnectionContext);

    public Object delete(URLWrapper urlWrapper, String id, HttpConnectionContext httpConnectionContext);

    public Object service(URLWrapper urlWrapper, Object object, HttpConnectionContext httpConnectionContext);
}
