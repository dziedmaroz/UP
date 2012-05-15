/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author lucian
 */
public class Logger
{

    private String logDir;
    private LogLevel logLevel;
    private BufferedWriter fout;

    public Logger(String logDir, LogLevel logLevel) throws IOException
    {
        this.logDir = logDir;
        this.logLevel = logLevel;
    }

    public void addRecord(String record, LogLevel priority) throws IOException
    {
        if (priority.compareTo(logLevel) <= 0)
        {
            Calendar cal = Calendar.getInstance();
            cal.clear(Calendar.HOUR);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            String logfilename =  cal.getTime().toString();
            logfilename=logfilename.replaceAll(":", "-");
            fout = new BufferedWriter(new FileWriter(logDir + File.separator +logfilename, true));
            System.out.println("[" + new Date().toString() + "] " + record);
            fout.write("[" + new Date().toString() + "] " + record);
            fout.newLine();
            fout.close();
        }
    }
}
