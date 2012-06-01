/**
 * 
 */
package example;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Sergey Derugo Date: 2/17/12 Time: 12:50 PM
 */
public class StaXParserTest {

    public static void main(String[] args) throws XMLStreamException {
	XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	try {
	    InputStream stream = new FileInputStream(
		    "data/xml/log.xml");

	    if ((new File("data/xml/log.xml")).exists()) {
		System.out.println("EXISTS");
	    }
	    XMLEventReader eventReader = inputFactory
		    .createXMLEventReader(stream);
	    while (eventReader.hasNext())
	    {
		XMLEvent tag = eventReader.nextTag();
		if (tag.isStartElement() && tag.asStartElement().getName().getLocalPart().equalsIgnoreCase("request"))		    
		{
		    System.out.println("request");
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

    }

    private static void readStartElement(XMLEventReader eventReader)
	    throws XMLStreamException {
	XMLEvent event = eventReader.nextEvent();
	StartElement startElement = event.asStartElement();

	QName elementName = startElement.getName();
	System.out.println("Tag: " + elementName.getLocalPart());	
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
}