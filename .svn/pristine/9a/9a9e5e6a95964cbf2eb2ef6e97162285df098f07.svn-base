

package com.boc.fiserv.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.CIFSvc.CustProfBasicAdd.rs.CustProfBasicAddRs;
import com.boc.CIFSvc.CustToCustAdd.rq.CustIdType;
import com.boc.CIFSvc.CustToCustAdd.rq.CustToCustAddRq;
import com.boc.CIFSvc.CustToCustAdd.rq.CustToCustRelationType;
import com.boc.CIFSvc.CustToCustAdd.rq.Date;
import com.boc.CIFSvc.CustToCustAdd.rq.CustToCustAddRq.RqUID;
import com.boc.CIFSvc.CustToCustAdd.rq.SPNameType;
import com.boc.CIFSvc.CustToCustAdd.rs.CustToCustAddRs;
import com.boc.fiserv.CreateCustToCustAddService;
import com.boc.fiserv.response.CalcDateResponse;
import com.boc.fiserv.response.CreateCustToCustAddResponse;
import com.boc.fiserv.response.CustomerProfileAddRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Apr 12, 2017
*/
public class CreateCustToCustAddServiceImpl extends BaseServiceImpl implements CreateCustToCustAddService
{
	private static Logger log = LoggerFactory.getLogger(CreateCustToCustAddServiceImpl.class);
	
	public CustomerProfileAddRs execute(Object cifDetails) throws JAXBException,Exception
	{
		String inputXML = null;
		CreateCustToCustAddResponse createCustToCustAddResponse = null;
		CustomerProfileAddRs customerProfileAddRs = null;
		inputXML = createCustToCustAddRqJaxbObject(cifDetails);
		if(null!=inputXML)
			customerProfileAddRs =  invokeCustToCustAddRq( inputXML);
		return customerProfileAddRs;
	}
	
	
	public CustomerProfileAddRs invokeCustToCustAddRq(String inputXML)
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CustToCustAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CustToCustAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		String outputXML = null,custToCustLinkId=null;
		CreateCustToCustAddResponse createCustToCustAddResponse =null;
		CustomerProfileAddRs customerProfileAddRs = null;
		createCustToCustAddResponse = new CreateCustToCustAddResponse();
		customerProfileAddRs = new  CustomerProfileAddRs();
		try {
		outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
		log.info("outputXML "+outputXML);
		String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
		CustToCustAddRs custToCustAddRs = (CustToCustAddRs)generateOutputObject(removedIFX,CustToCustAddRs.class);
		
		if(null!=custToCustAddRs)
		{
			statusCode = custToCustAddRs.getStatus().getStatusCode();
			if(null!=statusCode && statusCode==0)
			{
				custToCustLinkId = custToCustAddRs.getCustToCustLinkId();
				customerProfileAddRs.setCustToCustResponse(custToCustLinkId);
			}
			else
			{
				customerProfileAddRs.setErrorCode(custToCustAddRs.getStatus().getError().get(0).getErrNum());
				customerProfileAddRs.setErrorDescription(custToCustAddRs.getStatus().getError().get(0).getErrDesc());
			}
		}
		
		} catch (MalformedURLException e) {
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
		
		return customerProfileAddRs;
	}
	
	public String createCustToCustAddRqJaxbObject(Object cifDetails) throws JAXBException,Exception
	{
		
		ResourceBundle rsbundle = null;
		RqUID rquId=null;
		CustToCustAddRq custToCustAddrq = null;
		CustIdType  custIdType = null;
		custToCustAddrq = new CustToCustAddRq();
		custIdType = new CustIdType();
		List<CustToCustRelationType> custToCustRelation = null;
		custToCustRelation = new ArrayList<CustToCustRelationType>();
				
		String dateFormat=null,inputRq = null,serviceRquid=null,cifId=null,inputXML=null,constantXML=null;
		JSONArray cifRelationsArray = null,cifRelationsCIF1toCIF2Array= null,cifRelationsCIF2toCIF1Array=null;
		Calendar todayCal = null;
		Date relationStartDate = null;
		
		try
		{
			rsbundle =getResourceBundle();
			dateFormat = (String)rsbundle.getString("dateFormat");
			JSONObject jCIFObject = new JSONObject(cifDetails);
			rquId = new RqUID();
			serviceRquid = GenerateUUID.getUUID();		
			rquId.setValue(serviceRquid);	
			custToCustAddrq.setRqUID(rquId);
			if(jCIFObject.has(rsbundle.getString("CASCIFID")))
				cifId = (String) jCIFObject.getString(rsbundle.getString("CASCIFID"));
			log.info("createCustToCustAddRqJaxbObject:cifId is "+cifId);		
			if(null!=cifId)
			{
				custIdType.setCustPermId(cifId);
				custIdType.setSPName(SPNameType.FISERV_ICBS);
				custToCustAddrq.setCustId(custIdType);
			}
			
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
			relationStartDate = new Date();
			relationStartDate.setDay(Long.valueOf(todayCal.get(todayCal.DAY_OF_MONTH)));
			relationStartDate.setMonth(Long.valueOf(todayCal.get(todayCal.MONTH)+1));
			relationStartDate.setYear(Long.valueOf(todayCal.get(todayCal.YEAR)));
			
			if(jCIFObject.has(rsbundle.getString("CASCIFRELATIONS")))
				cifRelationsArray = jCIFObject.getJSONArray(rsbundle.getString("CASCIFRELATIONS"));
			
			if(jCIFObject.has(rsbundle.getString("CASRELCIF1TOCIF2")))
				cifRelationsCIF1toCIF2Array = jCIFObject.getJSONArray(rsbundle.getString("CASRELCIF1TOCIF2"));
			
			if(jCIFObject.has(rsbundle.getString("CASRELCIF2TOCIF1")))
				cifRelationsCIF2toCIF1Array = jCIFObject.getJSONArray(rsbundle.getString("CASRELCIF2TOCIF1"));
			
			if(null!=cifRelationsArray && cifRelationsArray.size()>0 && null!=cifRelationsCIF1toCIF2Array && cifRelationsCIF1toCIF2Array.size()>0
					&& null!=cifRelationsCIF2toCIF1Array && cifRelationsCIF2toCIF1Array.size()>0)
			{
				for(int i=0;i<cifRelationsArray.size();i++)
				{
					
					CustToCustRelationType custToCustRelationType = new CustToCustRelationType();
					String cifIdRelation = (String)cifRelationsArray.get(i);
					log.info("createCustToCustAddRqJaxbObject:cifIdRelation is "+cifIdRelation);
					custToCustRelationType.setCustPermId(cifIdRelation);
					custToCustRelationType.setRelationCode(cifRelationsCIF1toCIF2Array.getString(i));
					custToCustRelationType.setReverseRelationCode(cifRelationsCIF2toCIF1Array.getString(i));
					custToCustRelationType.setRelationStartDt(relationStartDate);
					log.info("createCustToCustAddRqJaxbObject:relationStartDate is "+relationStartDate);
					custToCustRelation.add(custToCustRelationType);
				}
			}
			
			custToCustAddrq.getCustToCustRelation().addAll(custToCustRelation);
			inputXML = generateInputXML(custToCustAddrq);
			constantXML = getConstantXML();
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<CIFSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</CIFSvcRq></IFX>";
			inputRq = constantXML+cifSvcRq;
			log.info("createCustToCustAddRqJaxbObject:inputRq is "+inputRq);
			System.out.println(inputRq);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			log.error("createCustToCustAddRqJaxbObject: ParseException Occured "+e.fillInStackTrace());
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createCustToCustAddRqJaxbObject Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			rquId=null;
			custToCustAddrq = null;
			custIdType = null;
			custToCustRelation = null;
			dateFormat=null;serviceRquid=null;cifId=null;inputXML=null;constantXML=null;
			cifRelationsArray = null;cifRelationsCIF1toCIF2Array= null;cifRelationsCIF2toCIF1Array=null;
			todayCal = null;
			relationStartDate = null;
		}
		return inputRq;
	}
	
}
