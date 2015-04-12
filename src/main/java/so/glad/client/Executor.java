package so.glad.client;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLWrapper;

/**
 * @author Palmtale
 * 2015-3-25
 */
public interface Executor {
	/**
	 * Classes implementing this interface have a define execute request method
	 * 
	 * @param wrapper					url with simple description
	 * @param httpConnectionContext	http content context
	 * @param httpConnectionFactory 	Use to generate http connection ,instance
	 * @param httpConnectionTemplate	http connection (request header , http method , content type , url encoding,parameters) ready
	 * @return response object
	 */
	Object execute(URLWrapper wrapper,
				   HttpConnectionContext httpConnectionContext,
				   HttpConnectionFactory httpConnectionFactory,
				   HttpConnectionTemplate httpConnectionTemplate);

}
