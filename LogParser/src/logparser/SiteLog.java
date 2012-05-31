/**
 * 
 */
package logparser;

import java.util.ArrayList;

/**
 * @author lucian
 *
 */
public class SiteLog {
    private ArrayList<LogRecord> siteRecords = new ArrayList<LogRecord>();     
    private String site; 
    private long totalTraffic;
    private long totalResponseTime;    
    /**
     * 
     */
    public SiteLog(String siteName)
    {
	
    }
   /**
    * Создаем лог сайта из колекции записей
    * @param records клекция записей
    * @param check проверяем принадлежат ли все записи одному сайту
    */
    public SiteLog(ArrayList<LogRecord> records, boolean check, String siteName)
    {
	siteRecords.clear();
	if (check)
	{
	    for (LogRecord i:records)
	    {
		if (i.getSite().equalsIgnoreCase(siteName))
		{
		    siteRecords.add(i);
		}
	    }
	}
	else
	{
	    siteRecords.addAll(records);
	}
	site =siteName;
	makeStat();
    }
    
    
    protected void makeStat ()
    {
	totalTraffic = 0;
	totalResponseTime = 0;
	for (LogRecord i:siteRecords)
	{
	    totalTraffic+=i.getResponseSize();
	    totalResponseTime+=i.getEndTimestamp()-i.getStartTimestamp();
	}
    }
    
    public boolean addRecord (LogRecord record)
    {
	boolean ok = siteRecords.add(record);
	if (ok)
	{
	    totalResponseTime += record.getEndTimestamp()-record.getStartTimestamp();
	    totalTraffic += record.getResponseSize();
	}
	return ok;
    }
    
    public double getAvgResonseTime ()
    {
	return (double) totalResponseTime / siteRecords.size();
    }
    
    public double getAvgTraffic ()
    {
	return (double) totalTraffic / siteRecords.size();
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
     * @return the totalTraffic
     */
    public long getTotalTraffic() {
        return totalTraffic;
    }
    /**
     * @param totalTraffic the totalTraffic to set
     */
    public void setTotalTraffic(long totalTraffic) {
        this.totalTraffic = totalTraffic;
    }
    /**
     * @return the totalResponseTime
     */
    public long getTotalResponseTime() {
        return totalResponseTime;
    }
    /**
     * @param totalResponseTime the totalResponseTime to set
     */
    public void setTotalResponseTime(long totalResponseTime) {
        this.totalResponseTime = totalResponseTime;
    }
    /**
     * среднее арифметическое и среднеквадратическое отклонение размера ответа (response)  
     * @return
     */
    public double getResponseSizeDispesion ()
    {
	double dispersion = 0;
	double avgRespSize = getAvgTraffic();
	for (LogRecord i:siteRecords)
	{
	    dispersion += Math.pow(avgRespSize-i.getResponseSize(), 2);
	}
	return Math.pow(dispersion/siteRecords.size(),0.5);
    }
    
}
