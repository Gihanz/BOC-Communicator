

package com.boc.fiserv.impl;

import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.BankSvc.AcctListInq.rs.AcctListInqRs;
import com.boc.BankSvc.XferMod.rq.XferModRq;
import com.boc.BankSvc.XferMod.rs.XferModRs;
import com.boc.fiserv.StandingOrderEnquiry;
import com.boc.fiserv.response.AccountLstInqResponse;
import com.boc.fiserv.response.CustomerProfileAddRs;
import com.boc.fiserv.response.StandingOrderResponse;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on May 2, 2017
*/
public class StandingOrderEnquiryImpl extends  BaseServiceImpl implements StandingOrderEnquiry  
{
	private static Logger log = LoggerFactory.getLogger(StandingOrderEnquiryImpl.class);

	public StandingOrderResponse execute(JSONObject standingOrdereDetails) throws JAXBException,Exception
	{
		
		
		return null;
	}

	
	public String createStandingOrderInqRqJaxbObject(JSONObject standingOrdereDetails) throws JAXBException,Exception
	{
		
		XferModRq xferModRq = null;
		xferModRq = new XferModRq();
		
		//xferModRq.set
		return null;
	}
	
	public AccountLstInqResponse invokeAccountlstInqRq(String inputXML) throws Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("StandingOrderEnquiryImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("StandingOrderEnquiryImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		String outputXML = null;
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			log.info("removedIFX "+removedIFX);
			XferModRs xferModRs = (XferModRs)generateOutputObject(removedIFX,XferModRs.class);
			statusCode = xferModRs.getStatus().getStatusCode();
			log.info(" statusCode  "+statusCode);
			if(null!=statusCode && statusCode==0)
			{
				xferModRs.getCustId().getCustPermId();
				//xferModRs.get
			}
			return null;
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
	
	}
	

