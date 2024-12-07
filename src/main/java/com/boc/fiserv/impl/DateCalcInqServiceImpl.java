

package com.boc.fiserv.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.MaintSvc.DateCalcInq.rq.DateCalcInqRq;
import com.boc.MaintSvc.DateCalcInq.rq.DateCalcInqRq.BranchId;
import com.boc.MaintSvc.DateCalcInq.rq.DateCalcInqRq.DateCalcOption;
import com.boc.MaintSvc.DateCalcInq.rq.DateCalcInqRq.RqUID;
import com.boc.MaintSvc.DateCalcInq.rq.DateCalcInqRq.SendReceiveFundsCode;
import com.boc.MaintSvc.DateCalcInq.rs.Date;
import com.boc.MaintSvc.DateCalcInq.rs.DateCalcInfoType;
import com.boc.MaintSvc.DateCalcInq.rs.DateCalcInqRs;
import com.boc.fiserv.response.CalcDateResponse;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Sep 9, 2016
*/
public class DateCalcInqServiceImpl extends BaseServiceImpl {
	private static Logger log = LoggerFactory.getLogger(DateCalcInqServiceImpl.class);
	public CalcDateResponse getDateCalcInq() throws Exception
	{
		String calDateStr = null;
		CalcDateResponse calcDateResponse = null;
		try
		{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl=rsbundle.getString("FISERV_URL");//"http://172.21.12.146/CRG_TRNG01/crg.aspx";https://172.20.12.146/CRG_PRODUAT/crg.aspx
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		DateCalcInqRq dateRq = new DateCalcInqRq();
		String messageRquid = null,inputRq = null;
		
		RqUID rquId=null;
		rquId = new RqUID();
		String serviceRquid = null;	
		serviceRquid = GenerateUUID.getUUID();		
		rquId.setValue(serviceRquid);
		dateRq.setRqUID(rquId);
		
		DateCalcOption dateCalOption = new DateCalcOption();
		dateCalOption.setValue("01");
		
		SendReceiveFundsCode sendrcvFundCode = new SendReceiveFundsCode();
		sendrcvFundCode.setValue("1");
		
		BranchId branchId = new BranchId();
		branchId.setValue("00001");
		
		dateRq.setDateCalcOption(dateCalOption);
		dateRq.setSendReceiveFundsCode(sendrcvFundCode);		
		dateRq.setBranchId(branchId);
		
		
		String inputXML = generateInputXML(dateRq);
		String truncatedXML = truncateXml(inputXML);
		//String truncatedXMLAgain =  truncateXmlAgain(truncatedXML);
		String constantXML = getConstantXML();
		
		
		messageRquid = GenerateUUID.getUUID();
		String custBasicInqSvcRq = "<MaintSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+truncatedXML+"</MaintSvcRq></IFX>";
		
		inputRq = constantXML+custBasicInqSvcRq;
		log.info("getDateCalcInq:inputRq is "+inputRq);
		
		String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputRq,fiServUrl,certificatePath,certificatePwd);
		String startStatusCode = rsbundle.getString("IFXStatusCode");
		String endStatusCode =  rsbundle.getString("IFXEndStatusCode");
		String startStatusDesc = rsbundle.getString("IFXStatusDescription");
		String endStatusDesc = rsbundle.getString("IFXEndStatusDescription");
		
		
		String startIndexStr = rsbundle.getString("DateCalInqServiceImpl.truncateStartStr"); //"<DateCalcInqRs";
		String endIndexStr =  rsbundle.getString("DateCalInqServiceImpl.truncateEndStr"); //"</MaintSvcRs>";
		String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
		DateCalcInqRs outputObj =  (DateCalcInqRs) generateOutputObject(removedIFX,DateCalcInqRs.class);
		Long statusCode = null;
		if(null!=outputObj)
		{
			calcDateResponse = new CalcDateResponse();
			statusCode = outputObj.getStatus().getStatusCode();
			if(null!=statusCode && statusCode==0)
			{
				DateCalcInfoType dateCalcInfoType =  outputObj.getDateCalcInfo();
				if(null!=dateCalcInfoType)
				{
					Date prcDate = dateCalcInfoType.getPrcDt();
					calDateStr = prcDate.getDay()+"-"+prcDate.getMonth()+"-"+prcDate.getYear();
					log.info("Communicator date obtained is "+calDateStr);
					System.out.println("Communicator date obtained is "+calDateStr);
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					java.util.Date dt = sdf.parse(calDateStr);
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(dt);
					cal.add(Calendar.MONTH,+1);
					
					
					Long day = Long.valueOf(cal.get(cal.DAY_OF_MONTH));
					Long month = Long.valueOf(cal.get(cal.MONTH)+1);					
					Long year = Long.valueOf(cal.get(cal.YEAR));
							
					calDateStr = String.valueOf(day)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
					log.info("After adding 30 days communicator date is "+calDateStr);
					calcDateResponse.setDate(calDateStr);
				}
			}
		}
		else
		{
			calcDateResponse.setErrorCode(outputObj.getStatus().getError().get(0).getErrNum());
			calcDateResponse.setErrorDescription(outputObj.getStatus().getError().get(0).getErrDesc());
		}
		}
		catch(Exception e)
		{
			log.error("getDateCalcInq:Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return calcDateResponse;
	}

	
	public static void main(String a[])
	{
		DateCalcInqServiceImpl dt = new DateCalcInqServiceImpl();
		try {
			CalcDateResponse calcDateResponse = dt.getDateCalcInq();
			System.out.println("calcDateResponse is "+calcDateResponse.getDate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
