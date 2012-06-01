/**
 * 
 */
package logparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import javax.xml.bind.ParseConversionEvent;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

 

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
    
    public Log parse () throws XMLStreamException
    {
	Log log = new Log ();	 
	return log;
    }
    
    private static String readStartElement(XMLEventReader eventReader)
	    throws XMLStreamException {
	XMLEvent event = eventReader.nextEvent();
	StartElement startElement = event.asStartElement();

	QName elementName = startElement.getName();
	return elementName.getLocalPart();
	 

	
    }

    private static StringBuilder readCharacters(XMLEventReader eventReader)
	    throws XMLStreamException {
	StringBuilder buffer = new StringBuilder();

	XMLEvent next;
	do {
	    next = eventReader.nextEvent();
	    String data = next.asCharacters().getData();
	    buffer.append(data.trim());

	    next = eventReader.peek();
	} while (next != null && next.isCharacters());

	return buffer;
    }
    
    private LogRecord parseRecord (XMLEventReader eventReader) throws XMLStreamException
    {
	long startTimestamp = 0;
	long endTimestamp = 0;
	long responseSize = 0;
	String URI = "";
	String site ="";
	XMLEvent next = eventReader.peek();
	while (!(next.isEndElement() && next.asEndElement().getName().getLocalPart().equalsIgnoreCase("request")))
	{
	    
	}
	return new LogRecord(site, URI, startTimestamp, endTimestamp, responseSize);
	
    }
    
}				
