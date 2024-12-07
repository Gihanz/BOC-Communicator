package com.boc.fiserv.impl;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.security.auth.Subject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.response.StatusBean;
import com.boc.utils.XmlUtils2;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.ibm.casemgmt.api.CaseType;
import com.ibm.casemgmt.api.DeployedSolution;
import com.ibm.casemgmt.api.context.CaseMgmtContext;
import com.ibm.casemgmt.api.context.P8ConnectionCache;
import com.ibm.casemgmt.api.context.SimpleP8ConnectionCache;
import com.ibm.casemgmt.api.context.SimpleVWSessionCache;
import com.ibm.casemgmt.api.objectref.ObjectStoreReference;

public class BaseServiceImpl 
{
	//public static Logger log = Logger.getLogger("com.boc.fiserv.impl.BaseServiceImpl");
	private static Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);
	public static void main(String a[])
	{
	//ResourceBundle rsbundle = ResourceBundle.getBundle("com.boc.fiserv.impl.config");
		String outpitXML="<IFX><GeneralStatus><StatusCode>100</StatusCode><StatusLevel>IFX</StatusLevel><StatusDesc>General Error</StatusDesc><ErrNum>01000.010.000117</ErrNum><ErrDesc>The message RqUID or the FiservKey already exist in the Communicator database.</ErrDesc></GeneralStatus><Status><StatusCode>100</StatusCode><Severity>Error</Severity><StatusDesc>General Error</StatusDesc><StatusType>NonFatal</StatusType><SupportUID>6C9D1656-D31E-40C9-9195-E0F74AB629BE</SupportUID><Error><Source>Communicator</Source><ErrNum>01000.010.000117</ErrNum><ErrDesc>The message RqUID or the FiservKey already exist in the Communicator database.</ErrDesc><SupportUID>6C9D1656-D31E-40C9-9195-E0F74AB629BE</SupportUID></Error></Status></IFX>";
		BaseServiceImpl impl = new BaseServiceImpl();
		//impl.checkOutputErrorResponse(outpitXML,"<IFX><GeneralStatus><StatusCode>","</StatusCode>","<StatusDesc>","</StatusDesc>","<ErrNum>","</ErrNum>","<ErrDesc>","</ErrDesc>");
		BigDecimal bDec = impl.getBigDecimalWithDecimalPoint(1982.259999999999990905052982270717620849609375);
		System.out.println("bDec is "+bDec);
	}
	
	public ResourceBundle getResourceBundle()
	{
		ResourceBundle rsbundle = ResourceBundle.getBundle("config",Locale.getDefault());
		return rsbundle;
	}
	
	public String generateInputXML(Object obj) throws Exception
	{
		String truncatedXML = null;
		try {
			String inputXML = XmlUtils2.writeXml(obj);
			//System.out.println("inputXML is "+inputXML);
			truncatedXML = truncateXml(inputXML);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		return truncatedXML;
	}
	
	public Object generateOutputObject(String outputXML,Class clazz) throws Exception
	{
		Object obj = null;
		try {
			obj = XmlUtils2.readXml(outputXML, clazz);
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new Exception(e);
	} catch (XMLStreamException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new Exception(e);
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new Exception(e);
	}
		return obj;
	}
	
	public String truncateOutputXML(String xmlContent,String startIndex, String endIndex)
	{
		String onlyContent = null;
		int xmlIndex = xmlContent.indexOf(startIndex, 0);
		onlyContent = xmlContent.substring(xmlIndex);
		int outIndex = onlyContent.indexOf(endIndex, 0);
		onlyContent = onlyContent.substring(0,outIndex);
		log.info("onlyContent is "+onlyContent);
		return onlyContent;
	}
	
	public String truncateXml(String inputXML)
	{
		String onlyContent = null;
		int xmlIndex = inputXML.indexOf("?>", 0);
		if(xmlIndex!=-1)
		{
			xmlIndex = xmlIndex+2;
			onlyContent = inputXML.substring(xmlIndex);		
		}
		else
		{
			onlyContent = inputXML;
		}
		return onlyContent;
	}
	
	//<AcctCustInqRq xmlns="http://www.fiserv.com/cbs">
	public String truncateXmlAgain(String inputXML)
	{
		String onlyContent = null;
		int xmlIndex = inputXML.indexOf("xmlns", 0);
		if(xmlIndex!=-1)
		{
			//xmlIndex = xmlIndex+2;
			onlyContent = inputXML.substring(0,xmlIndex);
			onlyContent=onlyContent+">";
		}
		else
		{
			onlyContent = inputXML;
		}
		return onlyContent;
	}
	
	public HashMap checkOutputErrorResponse(String outputXml,String startStatusCode,String endStatusCode,
			String statusDescription,String endStatusDescription)
	{
		//<IFX><GeneralStatus><StatusCode>
		HashMap statusMap = null;
		statusMap = new HashMap();
		String statusCode = null,statusDesc = null;
		//int startstatusCodeIndex = outputXml.indexOf(startStatusCode);
		//System.out.println("===statusCodeIndex is "+startstatusCodeIndex);
		//if(startstatusCodeIndex!=-1)
		//{
			//String statusCodeContent = outputXml.substring(startstatusCodeIndex);
			int statusEndIndex = outputXml.indexOf(endStatusCode);
			if(statusEndIndex!=-1)
			{
				//String statusDescriStr = statusCodeContent;
				String statusCodeStr = outputXml.substring(0,statusEndIndex);
				int lastSatusCodeIndex = statusCodeStr.lastIndexOf(">");
					if(lastSatusCodeIndex!=-1)
					{
						statusCode = statusCodeStr.substring(++lastSatusCodeIndex);
						log.info("statusCode is "+statusCode);
						System.out.println("statusCode is "+statusCode);
					}
				String statusDescriptionContent = outputXml.substring(statusEndIndex);
				int statusDescriptionContentindex = statusDescriptionContent.indexOf(statusDescription);
				if(statusDescriptionContentindex !=-1)
				{
					String statusDescrp = statusDescriptionContent.substring(statusDescriptionContentindex);
					int endSatusDescriptionContentindex = statusDescrp.indexOf(endStatusDescription);
					log.info("-------"+endSatusDescriptionContentindex);
					//<StatusDesc>Duplicate RqUID</StatusDesc>
					if(endSatusDescriptionContentindex!=-1)
					{
						String endStatusDescr = statusDescrp.substring(0,endSatusDescriptionContentindex);
						int endStatusDescIndex =  endStatusDescr.lastIndexOf(">");
						if(endStatusDescIndex!=-1)
						{
							statusDesc = endStatusDescr.substring(++endStatusDescIndex);
							log.info("statusDesc is "+statusDesc);
							System.out.println("statusDesc is "+statusDesc);
						}
					}
				}
			}
			statusMap.put(statusCode, statusDesc);
			return statusMap;
		//}
	}
	
	public StatusBean checkOutputErrorResponse(String outputXml,String startStatusCode,String endStatusCode,
			String statusDescription,String endStatusDescription,String startErrorCode,String endErrorCode,String startErrorDesc,String endErrorDesc)
	{
		//<IFX><GeneralStatus><StatusCode>
		StatusBean statusBean =  new StatusBean(); 
		HashMap statusMap = null;
		statusMap = new HashMap();
		String statusCode = null,statusDesc = null, errorCodeNum = null,errorCodeDesc = null;
		//int startstatusCodeIndex = outputXml.indexOf(startStatusCode);
		//System.out.println("===statusCodeIndex is "+startstatusCodeIndex);
		//if(startstatusCodeIndex!=-1)
		//{
			//String statusCodeContent = outputXml.substring(startstatusCodeIndex);
			int statusEndIndex = outputXml.indexOf(endStatusCode);
			if(statusEndIndex!=-1)
			{
				//String statusDescriStr = statusCodeContent;
				String statusCodeStr = outputXml.substring(0,statusEndIndex);
				int lastSatusCodeIndex = statusCodeStr.lastIndexOf(">");
					if(lastSatusCodeIndex!=-1)
					{
						statusCode = statusCodeStr.substring(++lastSatusCodeIndex);
						log.info("statusCode is "+statusCode);
						System.out.println("statusCode is "+statusCode);
						statusBean.setStatusCode(statusCode);
					}
				String statusDescriptionContent = outputXml.substring(statusEndIndex);
				int statusDescriptionContentindex = statusDescriptionContent.indexOf(statusDescription);
				if(statusDescriptionContentindex !=-1)
				{
					String statusDescrp = statusDescriptionContent.substring(statusDescriptionContentindex);
					int endSatusDescriptionContentindex = statusDescrp.indexOf(endStatusDescription);
					log.info("-------"+endSatusDescriptionContentindex);
					//<StatusDesc>Duplicate RqUID</StatusDesc>
					if(endSatusDescriptionContentindex!=-1)
					{
						String endStatusDescr = statusDescrp.substring(0,endSatusDescriptionContentindex);
						int endStatusDescIndex =  endStatusDescr.lastIndexOf(">");
						if(endStatusDescIndex!=-1)
						{
							statusDesc = endStatusDescr.substring(++endStatusDescIndex);
							statusBean.setStatusDesc(statusDesc);
							log.info("statusDesc is "+statusDesc);
							System.out.println("statusDesc is "+statusDesc);
						}
					}
				}
				int startErrorCodeIndex = outputXml.indexOf(startErrorCode);
				System.out.println("startErrorCodeIndex is "+startErrorCodeIndex);
				if(startErrorCodeIndex!=-1)
				{
					String errorCode = outputXml.substring(startErrorCodeIndex);
					int endErrorCodeIndex = errorCode.indexOf(endErrorCode);
					if(endErrorCodeIndex!=-1)
					{
						String errorCodeStr = errorCode.substring(0, endErrorCodeIndex);
						System.out.println("errorCodeStr is "+errorCodeStr);
						int errorCodeTagIndex =  errorCodeStr.lastIndexOf(">");
						if(errorCodeTagIndex!=-1)
						{
							errorCodeNum = errorCodeStr.substring(++errorCodeTagIndex);
							statusBean.setErrorCode(errorCodeNum);
							log.info("ErrorCodeNum is "+errorCodeNum);
							System.out.println("ErrorCodeNum is "+errorCodeNum);
						}
					}
				}
				int startErrorDescIndex= outputXml.indexOf(startErrorDesc);
				if(startErrorDescIndex!=-1)
				{
					String errorDesc = outputXml.substring(startErrorDescIndex);
					int endErrorDescIndex = errorDesc.indexOf(endErrorDesc);
					if(endErrorDescIndex!=-1)
					{
						String errorDescStr = errorDesc.substring(0, endErrorDescIndex);
						System.out.println("errorDescStr is "+errorDescStr);
						int errorDescTagIndex = errorDescStr.lastIndexOf(">");
						if(errorDescTagIndex !=-1)
						{
							errorCodeDesc = errorDescStr.substring(++errorDescTagIndex);
							statusBean.setErrorDesc(errorCodeDesc);
							log.info("errorCodeDesc is "+errorCodeDesc);
							System.out.println("errorCodeDesc is "+errorCodeDesc);
						}
					}
				}
			}
			if(null!=statusCode && null!= statusDesc)
				statusMap.put(statusCode, statusDesc);
			if(null!=errorCodeNum && null!= errorCodeDesc)
				statusMap.put(errorCodeNum, errorCodeDesc);
			return statusBean;
		//}
	}
	
	public String getConstantXML()
	{
		Calendar cal = Calendar.getInstance();
		ResourceBundle rsbundle = getResourceBundle();
		String startIFX = "<IFX><SignonRq><SignonPswd><SignonRole>"+rsbundle.getString("FISERV_SIGNONROLE")+"</SignonRole><CustId><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName><CustLoginId>"+rsbundle.getString("FISERV_CUSTLOGINID")+"</CustLoginId></CustId><CustPswd><CryptType>NONE</CryptType><Pswd>"+rsbundle.getString("FISERV_CUSTPSWD")+"</Pswd></CustPswd><GenSessKey>0</GenSessKey></SignonPswd>";
		int monthInt = cal.get((cal.MONTH))+1;
		String clientDt = "<ClientDt><Year>"+cal.get(cal.YEAR)+"</Year><Month>"+monthInt+"</Month><Day>"+cal.get(cal.DAY_OF_MONTH)+"</Day></ClientDt>";
		String clientApp = "<ClientApp><Org>"+rsbundle.getString("FISERV_CLIENTAPPORG")+"</Org><Name>"+rsbundle.getString("FISERV_CLIENTAPPNAME")+"</Name><Version>"+rsbundle.getString("FISERV_CLIENTAPPVERSION")+"</Version><ClientAppKey>"+rsbundle.getString("FISERV_CLIENTAPPKEY")+"</ClientAppKey></ClientApp><ComputerId>"+rsbundle.getString("FISERV_COMPUTERID")+"</ComputerId><InstitutionCode>"+rsbundle.getString("FISERV_INSTITUTIONCODE")+"</InstitutionCode></SignonRq><EnvironmentInfo><EnvironmentName>"+rsbundle.getString("FISERV_ENVIRONMENTNAME")+"</EnvironmentName></EnvironmentInfo>";
		String inputRq = startIFX+clientDt+clientApp;
		//System.out.println("inputRq is "+inputRq);
		return inputRq;
	}
	public BigDecimal getBigDecimalWithDecimalPoint(Double amountValue){
		BigDecimal bigDecimal = null;
		
		if(null != amountValue){
			bigDecimal = new BigDecimal(amountValue);
			if(null != bigDecimal && bigDecimal.scale() == 0)		
				bigDecimal = bigDecimal.setScale(2);
			else if(null!= bigDecimal && bigDecimal.scale() > 2)
			{
				bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
			}
		}else{
			bigDecimal = new BigDecimal(0.00);
			bigDecimal = bigDecimal.setScale(2);
		}
		
		return bigDecimal;
		
	}
	
	
	
}
