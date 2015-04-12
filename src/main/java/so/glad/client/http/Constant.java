package so.glad.client.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The constants used in http implementation.
 * @author Palmtale
 * 2015-3-25
 */
public abstract class Constant {
	
    public static final Logger LOG = LoggerFactory.getLogger(Constant.class.getPackage().getName());

    public static final String LINE_SEPARATOR = "\r\n";

    public static final int DEFAULT_CONNECT_TIMEOUT = 3;

    public static final int DEFAULT_TIMEOUT = 60 * 3;

    public static final String HTTP_META_DEFAULT_CHARSET = "US-ASCII";

    public static final String HTTP_BODY_DEFAULT_CHARSET = "UTF-8";
    
    public static final String HTTP_SESSION_DEFAULT = "Set-Cookie";

}
