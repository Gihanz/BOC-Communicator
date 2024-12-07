package com.boc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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



public class XmlUtils {

	// T inputObj should be have @XmlRootElement
	public static <T> void writeXml(String xmlFile, T inputObj) throws IOException, XMLStreamException, JAXBException {
		FileOutputStream adrFile = null;
		JAXBContext ctx;
	    try {
	    	adrFile = new FileOutputStream(xmlFile);	       
			ctx = JAXBContext.newInstance(inputObj.getClass());
	        Marshaller ma = ctx.createMarshaller();
	        ma.marshal(inputObj, adrFile);
	        ma.marshal(inputObj, System.out);
	    }
	    catch(IOException exc) {
	    	throw exc;
	    }
	    catch(JAXBException ex){
	    	throw ex;
	    }
	    finally {
	    	adrFile.close();
	    	adrFile = null;
	    	ctx = null;
	    }		
	}
	@SuppressWarnings("unchecked")
	public static <T> T readXml(String xmlFile, Class<T> clazz) throws IOException, XMLStreamException, JAXBException {		
		FileInputStream adrFile = null;
		JAXBContext ctx;
		try {
		    adrFile = new FileInputStream(xmlFile);
		    ctx = JAXBContext.newInstance(clazz);
		    Unmarshaller um = ctx.createUnmarshaller();
		    return (T) um.unmarshal(adrFile);
		}
		catch(IOException exc) {
			throw exc;
		}
		catch(JAXBException exc) {
			throw exc;
		}
		finally {
	    	adrFile.close();
	    	adrFile = null;
	    	ctx = null;
		}
	}
	

}
