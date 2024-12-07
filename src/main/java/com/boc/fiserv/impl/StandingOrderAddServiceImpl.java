package com.boc.fiserv.impl;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.BankSvc.XferAdd.rq.CurrencyAmount;
import com.boc.BankSvc.XferAdd.rq.DDSVTMAcctType;
import com.boc.BankSvc.XferAdd.rq.Date;
import com.boc.BankSvc.XferAdd.rq.DepAcctIdFromType;
import com.boc.BankSvc.XferAdd.rq.DepAcctIdToType;
import com.boc.BankSvc.XferAdd.rq.RecModelInfoType;
import com.boc.BankSvc.XferAdd.rq.SPNameType;
import com.boc.BankSvc.XferAdd.rq.XferAddRq;
import com.boc.BankSvc.XferAdd.rq.XferAddRq.CustId;
import com.boc.BankSvc.XferAdd.rq.XferAddRq.RqUID;
import com.boc.BankSvc.XferAdd.rq.XferAddRq.XferInfo;
import com.boc.BankSvc.XferAdd.rs.XferAddRs;
import com.boc.fiserv.StandingOrderAddService;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

public class StandingOrderAddServiceImpl extends BaseServiceImpl implements StandingOrderAddService{
	
	private static Logger log = LoggerFactory.getLogger(StandingOrderEnquiryImpl.class);
	
	public Long execute(JSONObject standingOrder) throws JAXBException,Exception
	{		
		String inputRq=null;
		Long statusCode = null;
		inputRq = createStandingOrderAddRqJaxbObject(standingOrder);
		if(null!=inputRq)
			statusCode = invokeStandingOrderAddRq(inputRq);
		return statusCode;
	}
	
	public String createStandingOrderAddRqJaxbObject(JSONObject standingOrder) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = null;
		RqUID rquId=null;
		XferAddRq addRq = null;
		XferInfo xferInfo=null;
		RecModelInfoType recType = null;
		CustId custId = null;
		DepAcctIdFromType fromType= null;
		DepAcctIdToType toType = null;
		CurrencyAmount curAmnt = null;
			
		
		rquId = new RqUID();
		addRq = new XferAddRq();
		xferInfo=new XferInfo();
		custId = new CustId();
		fromType = new DepAcctIdFromType();
		toType = new DepAcctIdToType();
		curAmnt = new CurrencyAmount();
		recType = new RecModelInfoType();		
		
		
		String serviceRquid=null,inputRq=null;
		try		
		{
			rsbundle =getResourceBundle();	
			
			serviceRquid = GenerateUUID.getUUID();		
			rquId.setValue(serviceRquid);				
			addRq.setRqUID(rquId);
						
			custId.setSPName(SPNameType.FISERV_ICBS);
			log.info("standing pder");
			addRq.setCustId(custId);
			
			fromType.setAcctId("345");
			fromType.setAcctType(DDSVTMAcctType.SV);
			
			toType.setAcctId("13444");
			toType.setAcctType(DDSVTMAcctType.SV);
			
			curAmnt.setAmt(new BigDecimal(343535));
			curAmnt.setCurCode("LKR");			

			recType.setFreq("M00301");			
			recType.setFinalDueDt(new Date());
			recType.setInitialDueDt(new Date());			
			
			xferInfo.setDepAcctIdFrom(fromType);
			xferInfo.setDepAcctIdTo(toType);
			xferInfo.setCurAmt(curAmnt);
			xferInfo.setCategory("Test category");
			xferInfo.setPayeeDesc("test");			
			addRq.setXferInfo(xferInfo);
			
			addRq.setRecModelInfo(recType);
			
			String inputXML = generateInputXML(addRq);
			String constantXML = getConstantXML();
			
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<BankSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</BankSvcRq></IFX>";
			inputRq = constantXML+cifSvcRq;
			log.info("standing order xml "+inputRq);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createAccountlstInqRqJaxbObject Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return inputRq;
	}
	
	public Long invokeStandingOrderAddRq(String inputXML) throws Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("StandingOrderAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("StandingOrderAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String outputXML = null;
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			log.info("removedIFX "+removedIFX);
			XferAddRs xferAddRs = (XferAddRs)generateOutputObject(removedIFX,XferAddRs.class);
			statusCode = xferAddRs.getStatus().getStatusCode();
			log.info(" statusCode  "+statusCode);
			
			//xferAddRs.getXferRec().getXferId()
			
			return statusCode;
		 }
		 catch (JAXBException e) {
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
		}
	
	public static void main(String args[]){
		
		StandingOrderAddServiceImpl x=new StandingOrderAddServiceImpl();
		try {
			String y=x.createStandingOrderAddRqJaxbObject(null);
			log.info(y);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
