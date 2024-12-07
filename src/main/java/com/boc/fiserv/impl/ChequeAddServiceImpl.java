

package com.boc.fiserv.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.BankSvc.DDChkOrdAdd.rq.AcctTypeType;
import com.boc.BankSvc.DDChkOrdAdd.rq.BankAcctIdType;
import com.boc.BankSvc.DDChkOrdAdd.rq.CustIdType;
import com.boc.BankSvc.DDChkOrdAdd.rq.DDChkOrdAddRq;
import com.boc.BankSvc.DDChkOrdAdd.rq.SPNameType;
import com.boc.BankSvc.DDChkOrdAdd.rq.DDChkOrdAddRq.BankAcctId;
import com.boc.BankSvc.DDChkOrdAdd.rq.DDChkOrdAddRq.ChkOrderOptions;
import com.boc.BankSvc.DDChkOrdAdd.rq.DDChkOrdAddRq.CustId;
import com.boc.BankSvc.DDChkOrdAdd.rq.DDChkOrdAddRq.DeliveryMethod;
import com.boc.BankSvc.DDChkOrdAdd.rq.DDChkOrdAddRq.RqUID;
import com.boc.BankSvc.DDChkOrdAdd.rs.DDChkOrdAddRs;
import com.boc.fiserv.ChequeAddService;
import com.boc.fiserv.response.ChequeAddServiceResponse;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Apr 19, 2017
*/
public class ChequeAddServiceImpl   extends BaseServiceImpl implements ChequeAddService
{
	private static Logger log = LoggerFactory.getLogger(ChequeAddServiceImpl.class);
	
	public ChequeAddServiceResponse execute(JSONObject chequeDetails) throws JAXBException,Exception
	{
		ChequeAddServiceResponse chequeAddServiceResponse = null;
		String inputXML = createChequeAddRqJaxbObject(chequeDetails);
		if(null!=inputXML)
			chequeAddServiceResponse = invokeChequeAddRq(inputXML);
		return chequeAddServiceResponse;
	}
	
	public ChequeAddServiceResponse invokeChequeAddRq(String inputXML)
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("ChequeAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("ChequeAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		String outputXML = null;
		ChequeAddServiceResponse chequeAddServiceResponse = null;
		chequeAddServiceResponse = new ChequeAddServiceResponse();
		DDChkOrdAddRs chkOrdAddRs = null;
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			chkOrdAddRs= (DDChkOrdAddRs)generateOutputObject(removedIFX,DDChkOrdAddRs.class);
			if(null!=chkOrdAddRs)
			{
				statusCode = chkOrdAddRs.getStatus().getStatusCode();
				if(null!=statusCode && statusCode==0)
				{
					chequeAddServiceResponse.setChkOrderId(chkOrdAddRs.getChkOrdRec().getChkOrdId());
					chequeAddServiceResponse.setChqStatus(chkOrdAddRs.getChkOrdRec().getChkOrdStatus().getChkOrdStatusCode());
				}
				else
				{
					chequeAddServiceResponse.setErrorCode(chkOrdAddRs.getStatus().getError().get(0).getErrNum());
					chequeAddServiceResponse.setErrorDescription(chkOrdAddRs.getStatus().getError().get(0).getErrDesc());
				}
			}
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return chequeAddServiceResponse;
		
	}	
	
	
	public String createChequeAddRqJaxbObject(JSONObject accountDetails) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		String inputRq = null,inputXML=null,constantXML=null;
		RqUID rquId = null;
		rquId = new RqUID();
		String serviceRquid=null;
		DDChkOrdAddRq chkOrdAddRq = null;
		CustId custId = null;
		BankAcctId bankAcctId = null;
		bankAcctId = new BankAcctId();
		ChkOrderOptions chkOrderOptions = null;
		chkOrderOptions = new ChkOrderOptions();
		DeliveryMethod deliverymethod = null;
		deliverymethod = new DeliveryMethod();
		
		custId = new CustId();
		chkOrdAddRq = new DDChkOrdAddRq();
		String accountNo=null,accounttype=null,cbStyle=null,deliveryOptions = null;
		Long cbNoBooks=null;
		try
		{
		serviceRquid = GenerateUUID.getUUID();
		rquId.setValue(serviceRquid);
		chkOrdAddRq.setRqUID(rquId);
		
		custId.setSPName(SPNameType.FISERV_ICBS); 
		chkOrdAddRq.setCustId(custId);
		JSONObject jCIFObject = new JSONObject(accountDetails);
		if(jCIFObject.has(rsbundle.getString("CASACCOUNTNO")))
			accountNo = (String) jCIFObject.getString(rsbundle.getString("CASACCOUNTNO"));
		log.info("createChequeAddRqJaxbObject:accountNo is "+accountNo);		
		bankAcctId.setAcctId(accountNo);
		
		if(jCIFObject.has(rsbundle.getString("CASACCOUNTTYPE")))
			accounttype = (String) jCIFObject.getString(rsbundle.getString("CASACCOUNTTYPE"));
		log.info("createChequeAddRqJaxbObject:accounttype is "+accounttype);
		/*if(null!=accounttype && accounttype.equalsIgnoreCase("SV"))
			bankAcctId.setAcctType(AcctTypeType.SV);
		else if(null!=accounttype && accounttype.equalsIgnoreCase("SV"))*/
			bankAcctId.setAcctType(AcctTypeType.DD); //Always Hardcoded
		chkOrdAddRq.setBankAcctId(bankAcctId);
		
		if(jCIFObject.has(rsbundle.getString("CASCBSTYLE")))
			cbStyle = (String) jCIFObject.getString(rsbundle.getString("CASCBSTYLE"));
		log.info("createChequeAddRqJaxbObject:cbStyle is "+cbStyle);
		
		chkOrderOptions.setChkDesign(cbStyle); 
		
		if(jCIFObject.has(rsbundle.getString("CASCBNOOFBOOKS")))
			cbNoBooks = Long.valueOf((String) jCIFObject.getString(rsbundle.getString("CASCBNOOFBOOKS")));
		log.info("createChequeAddRqJaxbObject:cbNoBooks is "+cbNoBooks);
		
		chkOrderOptions.setOrderQty(cbNoBooks);
		
		//chkOrderOptions.setStartingNumber(Long.valueOf(1)); //Not required according to shylika //Always Hardcoded
		
		chkOrdAddRq.setChkOrderOptions(chkOrderOptions);
		
		if(jCIFObject.has(rsbundle.getString("CASDELIVERYOPTIONS")))
			deliveryOptions = (String) jCIFObject.getString(rsbundle.getString("CASDELIVERYOPTIONS"));
		log.info("createChequeAddRqJaxbObject:deliveryOptions is "+deliveryOptions);
		
		deliverymethod.setValue(deliveryOptions);
		
		chkOrdAddRq.setDeliveryMethod(deliverymethod);
		
		inputXML = generateInputXML(chkOrdAddRq);
		constantXML = getConstantXML();
		String messageRquid = null;
		messageRquid = GenerateUUID.getUUID();
		String cifSvcRq = "<BankSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</BankSvcRq></IFX>";
		inputRq = constantXML+cifSvcRq;
		log.info("createChequeAddRqJaxbObject:inputRq is "+inputRq);
		System.out.println(inputRq);
		
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			log.error("createChequeAddRqJaxbObject:inputRq: ParseException Occured "+e.fillInStackTrace());
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createChequeAddRqJaxbObject: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			inputXML=null;constantXML=null;
			rquId = null;
			serviceRquid=null;
			chkOrdAddRq = null;
			custId = null;
			bankAcctId = null;
			chkOrderOptions = null;
			deliverymethod = null;
			accountNo=null;accounttype=null;cbStyle=null;deliveryOptions = null;
			cbNoBooks=null;
		}
		return inputRq;
	}
	
}
