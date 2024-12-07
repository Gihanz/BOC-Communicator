package com.boc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;



public class XmlUtils2 {

	// T inputObj should be have @XmlRootElement
	public static <T> String writeXml(T inputObj) throws IOException, XMLStreamException, JAXBException {
		JAXBContext ctx;
		StringWriter strWriter = null;
	    try {
			ctx = JAXBContext.newInstance(inputObj.getClass());
	        Marshaller ma = ctx.createMarshaller();
	        strWriter = new StringWriter();
	        ma.marshal(inputObj,strWriter);
	        //ma.marshal(inputObj, System.out);
	    }	    
	    catch(JAXBException ex){
	    	throw ex;
	    }
	    finally {
	    	ctx = null;
	    }	
	    return strWriter.toString();
	}
	@SuppressWarnings("unchecked")
	public static <T> T readXml(String serviceXML, Class<T> clazz) throws IOException, XMLStreamException, JAXBException {		
		JAXBContext ctx;
		try {
		    ctx = JAXBContext.newInstance(clazz);		    
		    Unmarshaller um = ctx.createUnmarshaller();
		    return (T) um.unmarshal(new StreamSource(new StringReader(serviceXML)));
		}		
		catch(JAXBException exc) {
			throw exc;
		}
		finally {
	    	ctx = null;
		}
	}
	

}
