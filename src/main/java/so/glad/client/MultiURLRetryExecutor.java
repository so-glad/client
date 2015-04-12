package so.glad.client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLWrapper;



/**
 * MultiURLRetryExecutor, you can put multiple urls with "," separator for executor, the executor could retry it
 *
 * @author Palmtale
 * 2015-3-25
 */
public class MultiURLRetryExecutor extends RetryExecutor {
    public static final Logger LOGGER = LoggerFactory.getLogger(MultiURLRetryExecutor.class);

    @Override
    public Object execute(URLWrapper wrapper, HttpConnectionContext httpConnectionContext, HttpConnectionFactory httpConnectionFactory, HttpConnectionTemplate httpConnectionTemplate) {
        String url = wrapper.getUrlString();

        List<String> multiURLs = new ArrayList<String>();
        if (url.contains(",")) {
            for (String singleURL : url.split(",")) {
                multiURLs.add(singleURL.trim());
            }
        } else {
            multiURLs.add(url);
        }

        WebServiceInvocationException exception = null;
        for (String singleURL : multiURLs) {
            try {
                return super.execute(new URLWrapper(singleURL, wrapper.getAlias()), httpConnectionContext, httpConnectionFactory, httpConnectionTemplate);
            } catch (WebServiceInvocationException e) {
                LOGGER.warn("Access [" + singleURL + "] failed", e);
                exception = e;
            }
        }
        if (exception != null) {
            throw exception;
        } else {
            throw new IllegalStateException("no exception terminated in MultiURLRetryExecutor");
        }
    }
}
