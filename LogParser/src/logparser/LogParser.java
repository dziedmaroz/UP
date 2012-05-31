/**
 * 
 */
package logparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

 

/**
 * @author lucian
 *
 */
public class LogParser {
    private XMLEventReader eventReader;    
    /**
     * @param filePath
     */
    public LogParser(String filePath) throws FileNotFoundException,XMLStreamException 
    {
	eventReader = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(filePath));
    }     
}				
