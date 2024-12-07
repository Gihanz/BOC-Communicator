

package com.boc.fiserv.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.CIFSvc.CustProfBasicInq.rs.CustProfBasicInqRs;
import com.boc.CIFSvc.CustProfBasicInq.rs.CustProfBasicType;
import com.boc.CIFSvc.CustProfBasicInq.rs.PhoneNumType;
import com.boc.MaintSvc.AcctCustInq.rq.AcctCustInqRq;
import com.boc.MaintSvc.AcctCustInq.rq.AcctCustInqRq.RqUID;
import com.boc.MaintSvc.AcctCustInq.rq.BankAcctIdType;
import com.boc.MaintSvc.AcctCustInq.rs.AcctCustInqRs;
import com.boc.MaintSvc.AcctCustInq.rs.AcctRelationType;
import com.boc.connector.PEConnector;
import com.boc.fiserv.AcctCustInqService;
import com.boc.fiserv.response.CustomerProfileRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Jun 24, 2016
*/
public class AcctCustInqServiceImpl extends BaseServiceImpl  implements AcctCustInqService
{
	//public static Logger log = Logger.getLogger("com.boc.fiserv.impl.AcctCustInqServiceImpl");
	private static Logger log = LoggerFactory.getLogger(AcctCustInqServiceImpl.class);
	public static void main(String a[])
	{
		try
		{
		AcctCustInqServiceImpl serviceImpl = new AcctCustInqServiceImpl();
		serviceImpl.getAcctCustInqRq("4313867","SV");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public CustomerProfileRs getAcctCustInqRq(String accountNo,String accType) throws Exception
	{
		CustomerProfileRs custProfileRs = null;
		try
		{
		ResourceBundle rsbundle = getResourceBundle();
		String dateFormat=rsbundle.getString("dateFormat");// "dd-MM-yyyy";
		String fiServUrl=rsbundle.getString("FISERV_URL");//"http://172.21.12.146/CRG_TRNG01/crg.aspx";https://172.20.12.146/CRG_PRODUAT/crg.aspx
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		
		String inputRq = null;
		AcctCustInqRq accCustInqRq = null;
		
		RqUID rquId = null; 
		rquId = new RqUID();
		String serviceRquid = null;	
		serviceRquid = GenerateUUID.getUUID();
		rquId.setValue(serviceRquid);
		
		BankAcctIdType bankAcctId = null;
		bankAcctId = new BankAcctIdType();
		bankAcctId.setAcctType(accType);
		bankAcctId.setAcctId(accountNo);
		
		accCustInqRq = new AcctCustInqRq();
		
		accCustInqRq.setRqUID(rquId);
		accCustInqRq.setBankAcctId(bankAcctId);
		
		String inputXML = generateInputXML(accCustInqRq);
		String truncatedXML = truncateXml(inputXML);
		//String truncatedXMLAgain =  truncateXmlAgain(truncatedXML);
		String constantXML = getConstantXML();
		
		String messageRquid = null;
		messageRquid = GenerateUUID.getUUID();
		String custBasicInqSvcRq = "<MaintSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+truncatedXML+"</MaintSvcRq></IFX>";
		
		inputRq = constantXML+custBasicInqSvcRq;
		log.info("getAcctCustInqRq:inputRq is "+inputRq);
		
		//String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputRq,fiServUrl);
		String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputRq,fiServUrl,certificatePath,certificatePwd);
		String startStatusCode = rsbundle.getString("IFXStatusCode");
		String endStatusCode =  rsbundle.getString("IFXEndStatusCode");
		String startStatusDesc = rsbundle.getString("IFXStatusDescription");
		String endStatusDesc = rsbundle.getString("IFXEndStatusDescription");
		Long statusCode = null;
		String	statusDescription=null;
		HashMap statusMap = null;
		String startIndexStr = rsbundle.getString("AcctCustInqServiceImpl.truncateStartStr"); //"<AcctCustInqRs";
		String endIndexStr =  rsbundle.getString("AcctCustInqServiceImpl.truncateEndStr"); //"</MaintSvcRs>";
		String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
		AcctCustInqRs outputObj =  (AcctCustInqRs) generateOutputObject(removedIFX,AcctCustInqRs.class);
		if(null!=outputObj)
		{
			statusCode = outputObj.getStatus().getStatusCode();
			if(null!=statusCode && statusCode==0)
			{
				custProfileRs = new CustomerProfileRs();
				List<AcctRelationType> acctRelationTypeLst =   outputObj.getAcctRelation();
				List<String> cifLst = null;
				String  cifStr = null;
				if(null!=acctRelationTypeLst)
				{
					log.info("CIF Found is "+acctRelationTypeLst.size());
					if(acctRelationTypeLst.size()>0)
					{
						int i=0;
						cifLst = new ArrayList<String>();
						for(AcctRelationType relationType:acctRelationTypeLst)
						{
							/*if(i==0)
							{
								cifStr = relationType.getCustPermId()+"_"+relationType.getFullName();
							}
							else
							{
								cifStr=cifStr+","+relationType.getCustPermId()+"_"+relationType.getFullName();
							}*/
							cifLst.add(relationType.getCustPermId()+"_"+relationType.getFullName());
							custProfileRs.setCifList(cifLst);
						}
					}
					/*else
					{
						String cifId = acctRelationTypeLst.get(0).getCustPermId();
						custProfileRs = invokeCustProfBasicInq(cifId);
						cifLst = new ArrayList<String>();
						cifLst.add(acctRelationTypeLst.get(0).getCustPermId()+"_"+acctRelationTypeLst.get(0).getFullName());
						custProfileRs.setCifList(cifLst);
						
					}*/
				}
			}
			else
			{
				custProfileRs = new CustomerProfileRs();
				custProfileRs.setErrorCode(outputObj.getStatus().getError().get(0).getErrNum());
				custProfileRs.setErrorDescription(outputObj.getStatus().getError().get(0).getErrDesc());
			}
		}
		
		/*statusMap = checkOutputErrorResponse(outputXML,startStatusCode,endStatusCode,startStatusDesc,endStatusDesc);
		
		if(null!=statusMap)
		{
			Set sttusKeySet = statusMap.keySet();
			Iterator iter = sttusKeySet.iterator();
			while(iter.hasNext())
			{
				statusCode = (String) iter.next();
				statusDescription =  (String) statusMap.get(statusCode);
			}
		}
		if(null!=statusCode && statusCode.equalsIgnoreCase("0"))
		{
			String startIndexStr = rsbundle.getString("AcctCustInqServiceImpl.truncateStartStr"); //"<AcctCustInqRs";
			String endIndexStr =  rsbundle.getString("AcctCustInqServiceImpl.truncateEndStr"); //"</MaintSvcRs>";
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			AcctCustInqRs outputObj =  (AcctCustInqRs) generateOutputObject(removedIFX,AcctCustInqRs.class);
			custProfileRs = new CustomerProfileRs();
			List<AcctRelationType> acctRelationTypeLst =   outputObj.getAcctRelation();
			List<String> cifLst = null;
			String  cifStr = null;
			if(null!=acctRelationTypeLst)
			{
				if(acctRelationTypeLst.size()>1)
				{
					int i=0;
					cifLst = new ArrayList<String>();
					for(AcctRelationType relationType:acctRelationTypeLst)
					{
						if(i==0)
						{
							cifStr = relationType.getCustPermId()+"_"+relationType.getFullName();
						}
						else
						{
							cifStr=cifStr+","+relationType.getCustPermId()+"_"+relationType.getFullName();
						}
						cifLst.add(relationType.getCustPermId()+"_"+relationType.getFullName());
						custProfileRs.setCifList(cifLst);
					}
				}
				else
				{
					String cifId = acctRelationTypeLst.get(0).getCustPermId();
					custProfileRs = invokeCustProfBasicInq(cifId);
					cifLst = new ArrayList<String>();
					cifLst.add(acctRelationTypeLst.get(0).getCustPermId()+"_"+acctRelationTypeLst.get(0).getFullName());
					custProfileRs.setCifList(cifLst);
					
				}
			}
		}
		else
		{
			custProfileRs = new CustomerProfileRs();
			custProfileRs.setErrorCode(statusCode);
			custProfileRs.setErrorDescription(statusDescription);
		}*/
		}
		catch(Exception e)
		{
			log.error("getAcctCustInqRq:Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		//System.out.println("cifStr is "+cifStr);
		return custProfileRs;
	}
	
	
	public CustomerProfileRs invokeCustProfBasicInq(String cifId) throws Exception
	{
		CustomerProfileRs custResponse = null;
		ResourceBundle rsbundle = getResourceBundle();
		//String dateFormat = rsbundle.getString("dateFormat");
		String dateFormat=rsbundle.getString("dateFormat");//"dd-MM-yyyy";
		String fiServUrl=rsbundle.getString("FISERV_URL");//"http://172.21.12.146/CRG_TRNG01/crg.aspx";
		String phoneType = rsbundle.getString("RESIDENCEPHONE");
		String mobilePhoneType = rsbundle.getString("MOBILEPHONE");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		//String fiServUrl  = rsbundle.getString("FISERV_URL");
		CustProfBasicInqServiceImpl impl = new CustProfBasicInqServiceImpl();
		try {
			String custProfInputXML = impl.createCustProfBasicInqRqJaxbObject(cifId);
			//String outputXML = InvokeCommunicatorUtil.invokeCommunicator(custProfInputXML, fiServUrl);
			String outputXML = InvokeCommunicatorUtil.invokeCommunicator(custProfInputXML, fiServUrl,certificatePath,certificatePwd);
			String startStatusCode = rsbundle.getString("IFXStatusCode");
			String endStatusCode =  rsbundle.getString("IFXEndStatusCode");
			String startStatusDesc = rsbundle.getString("IFXStatusDescription");
			String endStatusDesc = rsbundle.getString("IFXEndStatusDescription");
			String startIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateStartStr"); //
			String endIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateEndStr");
			Long statusCode = null;
			String	statusDescription=null;
			HashMap statusMap = null;
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			CustProfBasicInqRs custProfBasicInqRsObj =  (CustProfBasicInqRs) generateOutputObject(removedIFX,CustProfBasicInqRs.class);
			if(null!=custProfBasicInqRsObj)
			{
				statusCode = custProfBasicInqRsObj.getStatus().getStatusCode();
				if(null!=statusCode && statusCode==0)
				{
					custResponse = impl.getCustProfBasicInq(custProfBasicInqRsObj,dateFormat,phoneType,mobilePhoneType);
				}
				else
				{
					custResponse = new CustomerProfileRs();
					custResponse.setErrorCode(custProfBasicInqRsObj.getStatus().getError().get(0).getErrNum());
					custResponse.setErrorDescription(custProfBasicInqRsObj.getStatus().getError().get(0).getErrDesc());
				}
			}
			/*statusMap = checkOutputErrorResponse(outputXML,startStatusCode,endStatusCode,startStatusDesc,endStatusDesc);
			 * if(null!=statusMap)
			{
				Set sttusKeySet = statusMap.keySet();
				Iterator iter = sttusKeySet.iterator();
				while(iter.hasNext())
				{
					statusCode = (String) iter.next();
					statusDescription =  (String) statusMap.get(statusCode);
				}
			}
			if(null!=statusCode && statusCode.equalsIgnoreCase("0"))
			{			
				
				String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
				CustProfBasicInqRs custProfBasicInqRsObj =  (CustProfBasicInqRs) generateOutputObject(removedIFX,CustProfBasicInqRs.class);
				if(null!=custProfBasicInqRsObj)
				{
					custResponse = impl.getCustProfBasicInq(custProfBasicInqRsObj,dateFormat,phoneType,mobilePhoneType);
				}
			}
			else{
				custResponse = new CustomerProfileRs();
				custResponse.setErrorCode(statusCode);
				custResponse.setErrorDescription(statusDescription);
			}*/
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("invokeCustProfBasicInq:JAXBException Occured"+e.fillInStackTrace());
			throw new Exception(e);
		}
		catch(Exception e)
		{
			log.error("invokeCustProfBasicInq:Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return custResponse;
	}
	
	
	
}
