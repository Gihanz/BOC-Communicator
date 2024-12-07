

package com.boc.fiserv.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.CIFSvc.AlertAdd.rq.AcctTypeType;
import com.boc.CIFSvc.AlertAdd.rq.AlertAddRq;
import com.boc.CIFSvc.AlertAdd.rq.AlertAddRq.RqUID;
import com.boc.CIFSvc.AlertAdd.rq.AlertInfoType;
import com.boc.CIFSvc.AlertAdd.rq.AlertTypeType;
import com.boc.CIFSvc.AlertAdd.rq.BankAcctIdType;
import com.boc.CIFSvc.AlertAdd.rq.CustIdType;
import com.boc.CIFSvc.AlertAdd.rq.Date;
import com.boc.CIFSvc.AlertAdd.rs.AlertAddRs;
import com.boc.fiserv.CIFFullNameAlertAddService;
import com.boc.fiserv.response.CIFFullNameAddResponse;
import com.boc.fiserv.response.CalcDateResponse;
import com.boc.fiserv.response.ChequeFullNameAddServiceResponse;
import com.boc.fiserv.response.CustomerProfileAddRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Mar 21, 2017
*/
public class ChequeNameAlertAddServiceImpl extends BaseServiceImpl implements CIFFullNameAlertAddService
{
	private static Logger log = LoggerFactory.getLogger(ChequeNameAlertAddServiceImpl.class);
	public ChequeFullNameAddServiceResponse execute(JSONObject fullNameDetails) throws JAXBException,Exception
	{
		String inputXML = createChequeFullNameRqJaxbObject(fullNameDetails);
		ChequeFullNameAddServiceResponse chequeFullNameAddServiceResponse = invokeChequeCIFFullNameAddRq(inputXML);
		return chequeFullNameAddServiceResponse;
	}
	
	
	public ChequeFullNameAddServiceResponse invokeChequeCIFFullNameAddRq(String inputXML) throws Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CIFFullNameAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CIFFullNameAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		ChequeFullNameAddServiceResponse chequeFullNameAddServiceResponse = null;
		chequeFullNameAddServiceResponse = new ChequeFullNameAddServiceResponse();
		
		try
		{
			String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			AlertAddRs cifFullNameAddRsObj = (AlertAddRs)generateOutputObject(removedIFX,AlertAddRs.class);
			if(null!=cifFullNameAddRsObj)
			{
				statusCode = cifFullNameAddRsObj.getStatus().getStatusCode();
				if(null!=statusCode && statusCode==0)
				{
					BigInteger responseInt = cifFullNameAddRsObj.getAlertId();
					log.info("createChequeFullNameRqJaxbObject:responseInt is "+responseInt);
					chequeFullNameAddServiceResponse.setChqFullNameStatus(responseInt);
				}			
				else
				{
					chequeFullNameAddServiceResponse.setErrorCode(cifFullNameAddRsObj.getStatus().getError().get(0).getErrNum());
					chequeFullNameAddServiceResponse.setErrorDescription(cifFullNameAddRsObj.getStatus().getError().get(0).getErrDesc());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("AlertAddRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return chequeFullNameAddServiceResponse;
	}
	public String createChequeFullNameRqJaxbObject(JSONObject fullNameDetails) throws JAXBException,Exception
	{
		
		ResourceBundle rsbundle = getResourceBundle();
		AlertAddRq alertAddRq =null;
		RqUID rquId = null;
		String serviceRquid = null,accountType=null,accountNo=null,fullName=null,inputXML=null,constantXML=null,inputRq=null;
		rquId = new RqUID();
		alertAddRq = new AlertAddRq();
		AlertInfoType alertInfoType = null;
		BankAcctIdType bankAcctId = null;
		bankAcctId = new BankAcctIdType();
		AcctTypeType acctType = null;
		alertInfoType = new AlertInfoType();
		java.util.Date todayDt=null;
		Calendar todayCal=null;
		Date todayDate=null;
		try
		{
			//JSONObject jCIFObject = new JSONObject(fullNameDetails);
			String dateFormat = (String)rsbundle.getString("dateFormat");
			JSONObject jAccountObject = fullNameDetails;
			serviceRquid = GenerateUUID.getUUID();
			rquId.setValue(serviceRquid);
			alertAddRq.setRqUID(rquId);
			if(jAccountObject.has(rsbundle.getString("CASACCOUNTNO")))
				accountNo = (String) jAccountObject.getString(rsbundle.getString("CASACCOUNTNO"));
			log.info("createChequeFullNameRqJaxbObject:accountNo is "+accountNo);
			if(jAccountObject.has(rsbundle.getString("CASCBNAME")))
				fullName =  (String) jAccountObject.getString(rsbundle.getString("CASCBNAME"));
			log.info("createChequeFullNameRqJaxbObject:fullName is "+fullName);
			if(null!= accountNo)
			{
				bankAcctId.setAcctId(accountNo);
				accountType = (String) jAccountObject.getString(rsbundle.getString("CASACCOUNTTYPE"));
				log.info("createChequeFullNameRqJaxbObject:accountType is "+accountType);
				if(accountType.equalsIgnoreCase("SV"))
					bankAcctId.setAcctType(AcctTypeType.SV);
				else
					bankAcctId.setAcctType(AcctTypeType.DD);
				alertAddRq.setBankAcctId(bankAcctId);
			}
			alertInfoType.setAlertType(AlertTypeType.MEMO);
			if(null!= accountNo)
				alertInfoType.setAlertTitle(accountNo);
			alertInfoType.setAlertCategory("CHEQUEBOOK");
			alertInfoType.setAlertCategoryDesc("NAME TO BE PRINTED IN THE CHEQUE BOOK");
			if(null!= fullName)
				alertInfoType.setAlertText(fullName);
			//alertInfoType.setAlertExtendedText(value);
			//alertInfoType.setPrimOfficerCode(value);
			
			java.util.Date current = null;
			DateCalcInqServiceImpl dateService = new DateCalcInqServiceImpl();
			CalcDateResponse dtResponse = dateService.getDateCalcInq();
			String communicatorDate = dtResponse.getDate();
			log.info("Communicator advanced date if Null is "+communicatorDate);
			SimpleDateFormat sf1 = new SimpleDateFormat(dateFormat);
			current = sf1.parse(communicatorDate);
			log.info("current advanced date if Null is "+current);
			
			todayCal =	Calendar.getInstance();
			todayCal.setTime(current);
			todayCal.add(Calendar.MONTH,-1);	//Service returns 1month advance
			todayDate = new Date();
			todayDate.setDay(Long.valueOf(todayCal.get(todayCal.DAY_OF_MONTH)));
			todayDate.setMonth(Long.valueOf(todayCal.get(todayCal.MONTH)+1));
			todayDate.setYear(Long.valueOf(todayCal.get(todayCal.YEAR)));
			alertInfoType.setAlertCreateDt(todayDate);
			alertAddRq.setAlertInfo(alertInfoType);
			
			inputXML = generateInputXML(alertAddRq);
			constantXML = getConstantXML();
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<CIFSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</CIFSvcRq></IFX>";
			inputRq = constantXML+cifSvcRq;
			log.info("createChequeFullNameRqJaxbObject:fullName is "+inputRq);
			System.out.println(inputRq);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createChequeFullNameRqJaxbObject: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			serviceRquid = null;accountNo=null;fullName=null;inputXML=null;constantXML=null;
			alertAddRq =null;acctType = null;bankAcctId=null;
			rquId = null;
			alertInfoType = null;
			todayDt=null;
			todayCal=null;
			todayDate=null;
		}
		return inputRq;
		
	}


}
