/**
 * 
 */
package logparser;

import java.util.HashMap;

/**
 * @author lucian
 * 
 */
public class Log {
    private HashMap<String, SiteLog> records;

    public Log() {
	records = new HashMap<String, SiteLog>();
    }
    /**
     * Adds new record to the log
     * @param record
     * @return
     */
    public boolean addRecord(LogRecord record) {
	if (records.containsKey(record.getSite())) {
	    return records.get(record.getSite()).addRecord(record);
	} else {
	    SiteLog tmpSite = new SiteLog(record.getSite());
	    boolean flag = tmpSite.addRecord(record);
	    if (flag) {
		records.put(record.getSite(), tmpSite);
	    }
	    return flag;
	}	 
    }
}
