package so.glad.client;

import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

/**
 * The invocation exception for client
 *
 * @author Palmtale
 * 2015-03-25
 */
public class WebServiceInvocationException extends RuntimeException {

	private static final long serialVersionUID = 9214737863899685464L;

    private static final Set<Integer> retryableStatusCodes = new HashSet<Integer>();
    static {
        retryableStatusCodes.add(404);
        retryableStatusCodes.add(503);
    }

    private boolean retryable = false;
    
    private Integer statusCode;
    
	public WebServiceInvocationException() {
		super();
	}

	public WebServiceInvocationException(String url, String message) {
		super(message);
	}

    public WebServiceInvocationException(String url, int statusCode, String responseText) {
        super("Http unexpected response with code [" + statusCode
                + "], " + "response [" + responseText + "]");
        
        this.statusCode = statusCode;

        if (retryableStatusCodes.contains(statusCode)) {
            this.retryable = true;
        }
    }

    public WebServiceInvocationException(String url, SocketException socketException) {
        super("SocketError: " + socketException.getMessage(), socketException);
        if(socketException.getMessage().contains("Connection timed out")) {
            this.retryable = true;
        }
        if(socketException.getMessage().contains("springsocial refused")) {
            this.retryable = true;
        }
    }

	public WebServiceInvocationException(String url, Throwable cause) {
		super(cause.getMessage() == null ? "UNKNOWN" : cause.getMessage(), cause);
	}

    public boolean getRetryable() {
        return retryable;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
