package so.glad.client;


/**
 * base to initialize client and some parameters of SessionAwareClient
 *
 * @author Palmtale
 * 2015-3-25
 */
public abstract class BaseSessionAwareClient extends BaseClient implements SessionAwareClient {
    
	private Client client;

    private long beginTime;

    /**
     * will be used to change client content type
     * 
     * @param contentType be used to change client content type
     */
    public void setContentType(String contentType){
    	this.getClient().setContentType(contentType);
    }
    
    /**
	 * get the content type
	 * 
	 * @return content type
	 */
    public String getContentType(){
    	return this.getClient().getContentType();
    }
    
    /**
     * will be used to change client url encoding
     * 
     * @param urlEncoding 
     */
    public void setUrlEncoding(String urlEncoding){
    	this.getClient().setUrlEncoding(urlEncoding);
    }
    
    /**
	 * get the encoding for http url
	 * 
	 * @return urlEncoding
	 */
    public String getUrlEncoding(){
    	return this.getClient().getUrlEncoding();
    }
    
    /**
     * will be used to change client parameters encoding
     * 
     * @param encoding 
     */
    public void setEncoding(String encoding){
    	this.getClient().setEncoding(encoding);
    }
    /**
	 *	get the encoding for http parameters
	 * 
	 * @return encoding
	 */
    public String getEncoding(){
    	return this.getClient().getEncoding();
    }
    /**
     * get client example: ParameterClient
     * 
     * @return client 			
     */
    public Client getClient() {
        return client;
    }

    /**
     * set client example: ParameterClient
     * 
     * @param client			
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * 
     * session life cycle begin and initialize session begin time
     */
    @Override
    public void begin() {
    }

    /**
     * session life cycle end
     */
    @Override
    public void end() {
    }

    /**
     * set session begin time
     * 
     * @param beginTime
     */
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

    /**
     * get session begin time
     * 
     * @return beginTime
     */
    @Override
    public long getBeginTime() {
        return beginTime;
    }


}
