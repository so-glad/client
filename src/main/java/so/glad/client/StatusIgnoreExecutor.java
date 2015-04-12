package so.glad.client;

import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLWrapper;
import so.glad.client.http.impl.HttpConnectionContextWrapper;

import java.io.IOException;
import java.net.SocketException;

/**
 * StatusIgnoreExecutor
 * @author Palmtale
 * 2015-03-25
 */
public class StatusIgnoreExecutor implements Executor {
    @Override
    public Object execute(URLWrapper urlWrapper, HttpConnectionContext httpConnectionContext, HttpConnectionFactory httpConnectionFactory, HttpConnectionTemplate httpConnectionTemplate) {

        Object o = null;
        HttpConnection httpConnection = null;
        try {
            httpConnection = httpConnectionFactory.getHttpConnection(urlWrapper);
            if (httpConnectionContext != null) {
                httpConnection = new HttpConnectionContextWrapper(
                        httpConnection, httpConnectionContext);
            }
            if (null != httpConnectionTemplate) {
                httpConnectionTemplate.beforeExecute(httpConnection);
            }
            int statusCode = httpConnection.execute();

            if (null != httpConnectionTemplate) {
                o = httpConnectionTemplate.afterExecute(httpConnection);
            }

        } catch (SocketException e) {
            throw new WebServiceInvocationException(urlWrapper.toString(), e);
        } catch (IOException e) {
            throw new WebServiceInvocationException(urlWrapper.toString(), e);
        } finally {
            if (httpConnection != null) {
                try {
                    httpConnection.close();
                } catch (Exception e) {
                    Const.LOG.error("Close httpConnection ["
                            + httpConnection + "] failed", e);
                }
            }
        }

        return o;
    }
}
