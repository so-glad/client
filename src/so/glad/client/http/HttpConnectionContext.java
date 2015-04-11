package so.glad.client.http;

import java.util.Properties;

/**
 * HttpConnectionContext represents the http connection context, which includs
 * authentication information, soap action, cookie, redirect
 * @author Palmtale
 * 2015-03-25
 */
public final class HttpConnectionContext {
    public static final String AUTHENTICATION_BASIC = "basic";
    public static final String AUTHENTICATION_DIGEST = "digest";

    public static final String ZIP_TYPE_GZIP = HttpConnection.HEADER_VALUE_CONTENT_ENCODING_GZIP;

    private String username;
    private String password;
    private String authentication;
    
    private String sessionId;
    
    private String sessionName;
   
    private String zipType;

    private boolean isChunk = false;

    private String soapAction;

    private Properties headerProperties;

    private Properties properties;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getZipType() {
        return zipType;
    }

    public void setZipType(String zipType) {
        this.zipType = zipType;
    }

    public boolean isChunk() {
        return isChunk;
    }

    public void setChunk(boolean chunk) {
        isChunk = chunk;
    }

    public Properties getHeaderProperties() {
        return headerProperties;
    }

    public void setHeaderProperties(Properties headerProperties) {
        this.headerProperties = headerProperties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	
	public long getBeginTime(){
		return System.currentTimeMillis();
	}
}