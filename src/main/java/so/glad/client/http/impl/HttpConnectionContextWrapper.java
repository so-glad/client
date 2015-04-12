package so.glad.client.http.impl;


import org.slf4j.Logger;
import so.glad.client.http.Constant;
import so.glad.client.http.HttpConnection;
import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpUtils;

import java.io.IOException;

/**
 * @author Palmtale
 * 2015-3-25
 */
public class HttpConnectionContextWrapper extends HttpConnectionBaseWrapper {
	public static final Logger log = Constant.LOG;

	private HttpConnectionContext httpConnectionContext;

    public HttpConnectionContextWrapper(HttpConnection httpConnection, HttpConnectionContext httpConnectionContext) {
        super(httpConnection);
        this.httpConnectionContext = httpConnectionContext;
    }

    @Override
	public int execute() throws IOException {
		if (httpConnectionContext != null) {
			if (httpConnectionContext.getHeaderProperties() != null) {
				for (Object key : httpConnectionContext.getHeaderProperties()
						.keySet()) {
					if (key == null) {
						continue;
					}
					httpConnection.setRequestHeader(key.toString(),
							httpConnectionContext.getHeaderProperties()
									.getProperty(key.toString()));
				}
			}
			if (httpConnectionContext.getSoapAction() != null) {
				httpConnection.setRequestHeader(
						HttpConnection.HEADER_SOAP_ACTION,
						httpConnectionContext.getSoapAction());
			}

			if (httpConnectionContext.getSessionId() != null
					&& httpConnectionContext.getSessionName() != null) {
				httpConnection.setRequestHeader(HttpConnection.HEADER_COOKIE,
						httpConnectionContext.getSessionName() + "="
								+ httpConnectionContext.getSessionId());
			}

			if (HttpConnectionContext.AUTHENTICATION_BASIC
					.equals(httpConnectionContext.getAuthentication())) {
				httpConnection.setRequestHeader(
						HttpConnection.HEADER_AUTHORIZATION, HttpUtils
								.getBasicAuthorization(httpConnectionContext
										.getUsername(), httpConnectionContext
										.getPassword()));
			} else if (httpConnectionContext.getAuthentication() != null) {
				String authentication = httpConnectionContext
						.getAuthentication();
				throw new IllegalArgumentException(
						"Not support authentication [" + authentication + "]");
			}

			if (httpConnectionContext.getZipType() != null) {
				httpConnection.setRequestHeader(
						HttpConnection.HEADER_CONTENT_ENCODING,
						httpConnectionContext.getZipType());
			}

			if (httpConnectionContext.isChunk()) {
				httpConnection.setRequestHeader(
						HttpConnection.HEADER_TRANSFER_ENCODING,
						HttpConnection.HEADER_VALUE_TRANSFER_ENCODING_CHUNK);
			}
		}

		int code = httpConnection.execute();

		String cookie = httpConnection.getResponseHeader(Constant.HTTP_SESSION_DEFAULT);
		if (null != cookie) {
			int beginIndex = cookie.indexOf("=");
			int endIndex = cookie.indexOf(";");
			httpConnectionContext.setSessionName(cookie
					.substring(0, beginIndex));
			if(endIndex==-1){
				httpConnectionContext.setSessionId(cookie.substring(beginIndex + 1));
			}else{
				httpConnectionContext.setSessionId(cookie.substring(beginIndex + 1,
						endIndex));
			}
		}
		return code;
	}

    public HttpConnectionContext getHttpConnectionContext() {
        return httpConnectionContext;
    }
}
