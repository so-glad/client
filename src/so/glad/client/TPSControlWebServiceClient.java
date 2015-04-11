package so.glad.client;

import java.util.concurrent.TimeUnit;

/**
 * this client is used to control how many requests will be execute in per second 
 * @author Palmtale
 * 2015-03-25
 */
public class TPSControlWebServiceClient extends WebServiceClient  {

    private ControlExecutor controlExecutor;

    /**
     * Construction of TPSControlWebServiceClient to initialize executor 
     */
    public TPSControlWebServiceClient() {
        controlExecutor = newControlExecutor();
        controlExecutor.setControlUnit(TimeUnit.SECONDS);
        Executor executor = new RetryExecutor();
        ((RetryExecutor)executor).setExecutor(controlExecutor);
        this.setExecutor(executor);
    }

    protected ControlExecutor newControlExecutor() {
		return new ControlExecutor();
	}

	/**
	 * set control check cycle
	 * 
	 * @param controlPeriod
	 */
	public void setControlPeriod(int controlPeriod) {
		this.controlExecutor.setControlPeriod(controlPeriod);
	}

	/**
	 * set control times
	 * 
	 * @param controlValue
	 */
	public void setControlValue(int controlValue) {
		this.controlExecutor.setControlValue(controlValue);
	}


}
