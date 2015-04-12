package so.glad.client;


import so.glad.client.http.HttpConnectionContext;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLWrapper;

import java.util.concurrent.TimeUnit;

/**
 * control the execute times in a specified period of time 
 *
 * @author Palmtale
 * 2015-3-25
 */
public class ControlExecutor extends BaseExecutor implements Executor {
	
	//time unit (smallest unit is second)
	private TimeUnit controlUnit;

	//check cycle
	private int controlPeriod;

	//execute times
	private int controlValue;

	//counter
	private int transactions;

	//executor begin time
	private long startTimeMillis;

	//synchronized object
	private final Object lock;
	
	/**
	 * Construction of ControlExecutor to initialize execute task beginning time , counter and lock instanct
	 */
    public ControlExecutor(){
		startTimeMillis = System.currentTimeMillis();
		transactions = 0;
		lock = new Object();
	}
    
    /**
	 * specific control execute method realization ,execute times can be control in specified period of time  
	 * 
	 * @param wrapper						<code>URLWrapper</code> url with alias
	 * @param httpConnectionContext			<code>HttpConnectionContext</code>http connection context 
	 * @param httpConnectionFactory			<code>HttpConnectionFactory</code> be used to generate http connection ,instance such as HttpSocketConnectionFactory/PoolableHttpSocketConnectionFactory/FixedURLConnectionFactory
	 * @param httpConnectionTemplate		<code>HttpConnectionTemplate</code>http connection (request header , http method , content type , url encoding,parameters) ready 
	 * @return response object
	 */
	public Object execute(URLWrapper wrapper,
			HttpConnectionContext httpConnectionContext,
			HttpConnectionFactory httpConnectionFactory,
			HttpConnectionTemplate httpConnectionTemplate){
        long time = controlUnit.toMillis(getControlPeriod());
		synchronized (lock) {
			if (transactions + 1 > getControlValue()) {
				long costTimeMillis = System.currentTimeMillis()
						- startTimeMillis;

				if (costTimeMillis < time) {
					try {
						Thread.sleep(time - costTimeMillis);
					} catch (InterruptedException e) {
						throw new WebServiceInvocationException(wrapper.toString(), e);
					}
				}
				reset();
			}
			transactions++;
		}

        return super.execute(wrapper, httpConnectionContext,
                httpConnectionFactory, httpConnectionTemplate);
	}

	/**
	 * when execute times exceed control times ,transactions and start time will be reset 
	 */
	private void reset() {
		transactions = 0;
		startTimeMillis = System.currentTimeMillis();
	}
	
	/**
	 * set control time unit
	 * 
	 * @param controlUnit
	 */
	public void setControlUnit(TimeUnit controlUnit) {
		this.controlUnit = controlUnit;
	}

	/**
	 * set control check cycle
	 * 
	 * @param controlPeriod
	 */
	public void setControlPeriod(int controlPeriod) {
		this.controlPeriod = controlPeriod;
	}

	/**
	 * set control times
	 * 
	 * @param controlValue
	 */
	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	/**
	 * get control time unit
	 * 
	 * @return controlUnit
	 */
	public TimeUnit getControlUnit() {
		return controlUnit;
	}

	/**
	 * get control check cycle
	 * 
	 * @return controlPeriod
	 */
	public int getControlPeriod() {
		return controlPeriod;
	}

	/**
	 * get control times
	 * 
	 * @return controlValue
	 */
	public int getControlValue() {
		return controlValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + controlPeriod;
		result = prime * result + ((controlUnit == null) ? 0 : controlUnit.hashCode());
		result = prime * result + controlValue;
		result = prime * result + ((lock == null) ? 0 : lock.hashCode());
		result = prime * result + (int) (startTimeMillis ^ (startTimeMillis >>> 32));
		result = prime * result + transactions;
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
		final ControlExecutor other = (ControlExecutor) obj;
		if (controlPeriod != other.controlPeriod)
			return false;
		if (controlUnit == null) {
			if (other.controlUnit != null)
				return false;
		} else if (!controlUnit.equals(other.controlUnit))
			return false;
		if (controlValue != other.controlValue)
			return false;
		if (lock == null) {
			if (other.lock != null)
				return false;
		} else if (!lock.equals(other.lock))
			return false;
		if (startTimeMillis != other.startTimeMillis)
			return false;
		if (transactions != other.transactions)
			return false;
		return true;
	}
}
