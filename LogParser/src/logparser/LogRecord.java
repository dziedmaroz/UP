/**
<request> 
        <site>test.com</site> 
        <URI>/example/part2</URI> 
        <request-start-timestamp>1234567000000</request-start-timestamp> 
        <requset-end-timestamp>1234567002301</requset-end-timestamp> 
        <response-size>12334</response-size> 
 </request> 
 * 
 */
package logparser;

/**
 * @author lucian
 * 
 */
public class LogRecord {
    private String site;
    private String URI;
    private long startTimestamp;
    private long endTimestamp;
    private long responseSize;
    
    /**
     * @param site
     * @param uRI
     * @param startTimestamp
     * @param endTimestamp
     * @param responseSize
     */
    public LogRecord(String site, String URI, long startTimestamp,
	    long endTimestamp, long responseSize) {
	 
	this.site = site;
	this.URI = URI;
	this.startTimestamp = startTimestamp;
	this.endTimestamp = endTimestamp;
	this.responseSize = responseSize;
	 
	 
    }
    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }
    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }
    /**
     * @return the uRI
     */
    public String getURI() {
        return URI;
    }
    /**
     * @param uRI the uRI to set
     */
    public void setURI(String uRI) {
        URI = uRI;
    }
    /**
     * @return the startTimestamp
     */
    public long getStartTimestamp() {
        return startTimestamp;
    }
    /**
     * @param startTimestamp the startTimestamp to set
     */
    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }
    /**
     * @return the endTimestamp
     */
    public long getEndTimestamp() {
        return endTimestamp;
    }
    /**
     * @param endTimestamp the endTimestamp to set
     */
    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
    /**
     * @return the responseSize
     */
    public long getResponseSize() {
        return responseSize;
    }
    /**
     * @param responseSize the responseSize to set
     */
    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }
    
    
    
    

}
