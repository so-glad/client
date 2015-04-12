package so.glad.client;

import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLWrapper;

import java.util.concurrent.TimeUnit;

/**
 * retry execute class to control retry times and sleep interval
 *
 * @author Palmtale
 * 2015-3-25
 */
public class RetryExecutor implements Executor{

	//retry times
	private int retry;
	
	//sleep time
	private int interval;
	
	private Executor executor;

	/**
	 * Construction of RetryExecutor assign BaseExecutor is the default executor
	 */
    public RetryExecutor() {
        this.executor = new BaseExecutor();
    }

    /**
     * Construction of RetryExecutor with specific retry times and sleep time
     * 
     * @param retry			retry times
     * @param interval		sleep time
     */
    public RetryExecutor(int retry, int interval) {
        this();
        this.retry = retry;
        this.interval = interval;
    }

    /**
     * Construction of RetryExecutor with specific retry times and sleep time
     * 
     * @param retry			retry times
     * @param interval		sleep time
     * @param executor		specific executor class
     */
    public RetryExecutor(int retry, int interval, Executor executor) {
        this.retry = retry;
        this.interval = interval;
        this.executor = executor;
    }
	
    /**
	 * specific retry execute method realization ,contain control retry times logic
	 * 
	 * @param wrapper						<code>URLWrapper</code> url with alias
	 * @param httpConnectionContext			<code>HttpConnectionContext</code>http connection context 
	 * @param httpConnectionFactory			<code>HttpConnectionFactory</code> be used to generate http connection ,instance such as HttpSocketConnectionFactory/PoolableHttpSocketConnectionFactory/FixedURLConnectionFactory
	 * @param httpConnectionTemplate		<code>HttpConnectionTemplate</code>http connection (request header , http method , content type , url encoding,parameters) ready 
	 * @return response object
	 */
	@Override
	public Object execute(URLWrapper wrapper,
			HttpConnectionContext httpConnectionContext,
			HttpConnectionFactory httpConnectionFactory,
			HttpConnectionTemplate httpConnectionTemplate)  {
		Object object;
		try {
			object = executor.execute(wrapper, httpConnectionContext, httpConnectionFactory, httpConnectionTemplate);
		}catch (WebServiceInvocationException e) {
			WebServiceInvocationException lastException = e;
			int currentRetry = 0;
			while (currentRetry < getRetry()) {
				Const.LOG.info("Retry [" + currentRetry + "]", e);
				try {
					sleep();
					object =executor.execute(wrapper, httpConnectionContext,
							httpConnectionFactory, httpConnectionTemplate);
					
					return object;
				} catch (WebServiceInvocationException e1) {
					currentRetry++;
					lastException = e1;
				} 
			}
			throw lastException;
		} 
		return object;
	}
	
	/**
	 * each retry interval
	 */
	private void sleep() {
		try {
			TimeUnit.SECONDS.sleep(getInterval());
		} catch (InterruptedException interruptedException) {
			Const.LOG.error("Sleep error", interruptedException);
		}
	}

	/**
	 * get retry time
	 * 
	 * @return retry
	 */
	public int getRetry() {
		return retry;
	}

	/**
	 * set retry time
	 * 
	 * @param retry
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}

	/**
	 * get executor
	 * 
	 * @return	executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * set executor
	 * 
	 * @param executor
	 */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	/**
	 * get interval
	 * 
	 * @return interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * set interval
	 * 
	 * @param interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + interval;
		result = prime * result + retry;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RetryExecutor other = (RetryExecutor) obj;
		if (interval != other.interval)
			return false;
		if (retry != other.retry)
			return false;
		return true;
	}


}
