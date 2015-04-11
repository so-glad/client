package so.glad.client;


public interface SessionAwareClient extends Client {
	/**
	 * 
	 * @param client instance of ParameterClient 
	 */
	void setClient(Client client);

	/**
	 * 
	 * @return a client which implements Client can do post/get method
	 */
	Client getClient();

	/**
	 *  Session Life Cycle begin
	 */
	
	void begin();

	/**
	 * Session Life Cycle end
	 */
	void end();

	/**
	 * 
	 * @return a system time when session begin
	 */
	long getBeginTime();
}
