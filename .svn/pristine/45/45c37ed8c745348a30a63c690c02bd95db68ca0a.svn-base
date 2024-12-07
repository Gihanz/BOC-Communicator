

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
import com.boc.fiserv.response.CustomerProfileAddRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Mar 21, 2017
*/
public class GuardianNameAlertAddServiceImpl extends BaseServiceImpl implements CIFFullNameAlertAddService
{
	private static Logger log = LoggerFactory.getLogger(GuardianNameAlertAddServiceImpl.class);
	public CustomerProfileAddRs execute(JSONObject fullNameDetails) throws JAXBException,Exception
	{
		String inputXML = createGuardianFullNameRqJaxbObject(fullNameDetails);
		CustomerProfileAddRs customerProfileAddRs = invokeGuardianFullNameAddRq(inputXML);
		return customerProfileAddRs;
	}
	
	
	public CustomerProfileAddRs invokeGuardianFullNameAddRq(String inputXML) throws Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CIFFullNameAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CIFFullNameAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		CIFFullNameAddResponse cifFullNameAddResponse = null;
		cifFullNameAddResponse = new CIFFullNameAddResponse();
		CustomerProfileAddRs customerProfileAddRs = null;
		customerProfileAddRs = new CustomerProfileAddRs();
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
					customerProfileAddRs.setCifFullNameRs(responseInt);
				}			
				else
				{
					customerProfileAddRs.setErrorCode(cifFullNameAddRsObj.getStatus().getError().get(0).getErrNum());
					customerProfileAddRs.setErrorDescription(cifFullNameAddRsObj.getStatus().getError().get(0).getErrDesc());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("AlertAddRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return customerProfileAddRs;
	}
	public String createGuardianFullNameRqJaxbObject(JSONObject fullNameDetails) throws JAXBException,Exception
	{
		
		ResourceBundle rsbundle = getResourceBundle();
		AlertAddRq alertAddRq =null;
		RqUID rquId = null;
		String serviceRquid = null,guardianCIF=null,accountType=null,accountNo=null,guardianName=null,inputXML=null,constantXML=null,inputRq=null;
		rquId = new RqUID();
		alertAddRq = new AlertAddRq();
		AlertInfoType alertInfoType = null;
		CustIdType custId = null;
		custId = new CustIdType();
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
			if(jAccountObject.has(rsbundle.getString("CASGUARDIANCIF")))
				guardianCIF = (String) jAccountObject.getString(rsbundle.getString("CASGUARDIANCIF"));
			log.info("createGuardianFullNameRqJaxbObject:guardianCIF is "+guardianCIF);
			if(jAccountObject.has(rsbundle.getString("CASACCOUNTNO")))
				accountNo = (String) jAccountObject.getString(rsbundle.getString("CASACCOUNTNO"));
			log.info("createGuardianFullNameRqJaxbObject:accountNo is "+accountNo);
			if(jAccountObject.has(rsbundle.getString("CASGUARDIANNAME")))
				guardianName =  (String) jAccountObject.getString(rsbundle.getString("CASGUARDIANNAME"));
			log.info("createGuardianFullNameRqJaxbObject:guardianName is "+guardianName);
			if(null!=guardianCIF)
			{
				custId.setCustPermId(guardianCIF);
				alertAddRq.setCustId(custId);
			}
			if(null!= accountNo)
			{
				bankAcctId.setAcctId(accountNo);
				accountType = (String) jAccountObject.getString(rsbundle.getString("CASACCOUNTTYPE"));
				log.info("createGuardianFullNameRqJaxbObject:accountType is "+accountType);
				if(accountType.equalsIgnoreCase("SV"))
						bankAcctId.setAcctType(AcctTypeType.SV);
				else
					bankAcctId.setAcctType(AcctTypeType.DD);
				alertAddRq.setBankAcctId(bankAcctId);
			}
			alertInfoType.setAlertType(AlertTypeType.MEMO);
			if(null!= accountNo)
				alertInfoType.setAlertTitle(accountNo);
			alertInfoType.setAlertCategory("GARDIAN");
			alertInfoType.setAlertCategoryDesc("Gardian's Details");
			if(null!= guardianName)
				alertInfoType.setAlertText(guardianName);
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
			log.info("createGuardianFullNameRqJaxbObject:fullName is "+inputRq);
			System.out.println(inputRq);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createGuardianFullNameRqJaxbObject: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			serviceRquid = null;guardianCIF=null;accountNo=null;guardianName=null;inputXML=null;constantXML=null;
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
