package com.boc.fiserv.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.LoanSvc.CollateralItemAdd.rq.CollateralItemAddRq;
import com.boc.LoanSvc.CollateralItemAdd.rq.CollateralItemAddRq.CustId;
import com.boc.LoanSvc.CollateralItemAdd.rq.CollateralItemAddRq.RqUID;
import com.boc.LoanSvc.CollateralItemAdd.rq.CollateralItemInfoType;
import com.boc.LoanSvc.CollateralItemAdd.rq.CollateralValueType;
import com.boc.LoanSvc.CollateralItemAdd.rq.CommodityType;
import com.boc.LoanSvc.CollateralItemAdd.rq.CurrencyAmount;
import com.boc.LoanSvc.CollateralItemAdd.rq.DDSVTMAcctType;
import com.boc.LoanSvc.CollateralItemAdd.rq.Date;
import com.boc.LoanSvc.CollateralItemAdd.rq.DepAcctIdType;
import com.boc.LoanSvc.CollateralItemAdd.rq.PropertyType;
import com.boc.LoanSvc.CollateralItemAdd.rq.SPNameType;
import com.boc.LoanSvc.CollateralItemAdd.rs.CollateralItemAddRs;
import com.boc.LoanSvc.CollateralItemAdd.rs.StatusType;
import com.boc.LoanSvc.LoanAcctAdd.rq.IntGuarantyCodeType;
import com.boc.connector.CMConnector;
import com.boc.fiserv.CreateCollateralService;
import com.boc.fiserv.response.CreateCollateralResponse;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Jul 20, 2016
*/
public class CreateCollateralServiceImpl extends BaseServiceImpl implements CreateCollateralService
{
	//public static Logger log = Logger.getLogger("com.boc.fiserv.impl.CreateCollateralServiceImpl");
	private static Logger log = LoggerFactory.getLogger(CreateCollateralServiceImpl.class);
	public CreateCollateralResponse execute(HashMap parametersMap) throws JAXBException,Exception
	{
		ResourceBundle rsbundle =getResourceBundle();
		String homeLoanCaseType = rsbundle.getString("HOMELOANCASETYPE");
		String caseTypeName = null,inputXML=null;
		if(parametersMap.containsKey("CaseType"))
		{
			caseTypeName = (String) parametersMap.get("CaseType");
			if(caseTypeName.equalsIgnoreCase(homeLoanCaseType))
			{
				inputXML  = createHomeLoanCollateralJAXBObject(parametersMap);
			}
			else
			{
				inputXML = createCollateralJAXBObject(parametersMap);
			}
		}
		else //To Handle existing cases in production for personal loan
			inputXML = createCollateralJAXBObject(parametersMap);
		log.info("CreateCollateralResponse execute:inputXML is "+inputXML);
		
		String referenceNo = (String) parametersMap.get(rsbundle.getString("REFERENCENO"));
		
		CreateCollateralResponse createCollateralRs = invokeCommunicator(inputXML,referenceNo);
		return createCollateralRs;
	}
	
	public String createHomeLoanCollateralJAXBObject(HashMap parametersMap) throws ParseException,Exception
	{
		
		ResourceBundle rsbundle = null;
		String currencyCode = null,dateFormat=null,serviceRquid=null,collateralCode=null,guaranterName1=null,guaranterName2=null;
		String guaranterNic1 =null,guaranterNic2 = null,proposedCollateral=null,monthFormat=null,collateralActNumber=null,collateralActType=null;
		String reviewFrqDtStr = null,propertyCode=null,inputXML=null,constantXML=null,messageRquid=null,collateralSvcRq=null,inputRq=null;
		CollateralItemAddRq collateralItemAddRq = null; 
		CustId custId = null;
		RqUID rquId = null;
		CommodityType commodity = null;
		CollateralItemInfoType collateralItemInfo = null; 
		List collateralDescList = null;
		CollateralValueType collateralVal = null;
		CurrencyAmount originalAmt  = null,unitPriceAmt = null;
		BigDecimal collateralOriginalAmt = null;
		Double proposedAmt = null;
		String orginalAmtDouble=null;
		java.util.Date todatDt = null,reviewFreqDt=null;
		Date nextCollateralReviewDt = null,matDate=null; 
		Boolean fdAgainstLoan = false,saAgainstLoan=false,propAgainstLoan=false;
		DepAcctIdType deptActId = null; 
		Calendar reviewCal=null,reviewFrqCal = null;
		SimpleDateFormat sf = null;
		PropertyType propertyType = null; 
		try
		{
		rsbundle =getResourceBundle();
		currencyCode = (String)rsbundle.getString("CURRENCYCODE");
		dateFormat = (String)rsbundle.getString("dateFormat");
		
		collateralItemAddRq =	new CollateralItemAddRq();
		 
		rquId = new RqUID();
		serviceRquid = null;	
		serviceRquid = GenerateUUID.getUUID();		
		rquId.setValue(serviceRquid);
		
		collateralItemAddRq.setRqUID(rquId);
		
		custId = new CustId();
		custId.setSPName(SPNameType.FISERV_ICBS);
		custId.setCustPermId((String)parametersMap.get(rsbundle.getString("CIFNO")));
		
		collateralItemAddRq.setCustId(custId);
		
		
		collateralItemInfo =new CollateralItemInfoType();
		collateralCode = null;
		collateralCode = (String)parametersMap.get(rsbundle.getString("COLLATERALCODE"));
		log.info("createCollateralJAXBObject: collateralCode"+collateralCode);
		collateralItemInfo.setCollateralCode(collateralCode);
		
		
		commodity = new CommodityType();
		commodity.setCommodityId((String)parametersMap.get(rsbundle.getString("CIFNO")));
		collateralItemInfo.setCommodity(commodity);
		collateralItemInfo.setLocationCode((String)parametersMap.get(rsbundle.getString("BRANCHCODE")));
		collateralItemInfo.setCollateralShortDesc((String)parametersMap.get(rsbundle.getString("COLLATERALDESC")));
		log.info("createCollateralJAXBObject: collateralDescription is "+parametersMap.get(rsbundle.getString("COLLATERALDESC")));
		
		{
			String propertyAddress = (String)parametersMap.get(rsbundle.getString("PROPERTYADDRESS"));
			log.info("createCollateralJAXBObject: propertyAddress is "+propertyAddress);
			//if(null!= guaranterName1 && null!=guaranterNic1)
			{
				if(null!=propertyAddress)
				{
					//String[] collateralArray proposedCollateral.split("(?<=\\G.{60})");
					collateralDescList = splitEqually(propertyAddress,60);
					if(null!= collateralDescList && collateralDescList.size()>0)
					{
							if(collateralDescList.size()==1)
							{
								collateralItemInfo.setCollateralDesc1((String)collateralDescList.get(0));
							}
							else if(collateralDescList.size()==2 || collateralDescList.size()==3)
							{
								collateralItemInfo.setCollateralDesc1((String)collateralDescList.get(0));
								collateralItemInfo.setCollateralDesc2((String)collateralDescList.get(1));
							}
							/*else if(collateralDescList.size()==3)
							{
								collateralItemInfo.setCollateralDesc1((String)collateralDescList.get(0));
								collateralItemInfo.setCollateralDesc2((String)collateralDescList.get(1));
							}*/
							
							String planNo = (String)parametersMap.get(rsbundle.getString("PLANNO"));
							String surveyedBy = (String)parametersMap.get(rsbundle.getString("SURVEYEDBY"));
							String surveyedOn = (String)parametersMap.get(rsbundle.getString("SURVEYEDON"));
							
							String collateralDesc3 = "Plan No "+planNo+" Surveyed by "+surveyedBy+" On "+surveyedOn;
							collateralItemInfo.setCollateralDesc3(collateralDesc3);
							
					}
					
				}
			}
		}
		
		String insuranceRequired = (String)parametersMap.get(rsbundle.getString("INSURANCEREQUIREDFLAG"));
		log.info("createCollateralJAXBObject: insuranceRequired is "+insuranceRequired);
		boolean insuranceRequiredFlag=false;
		if(null!=insuranceRequired)
		{
			if(insuranceRequired.equalsIgnoreCase("Y"))
			{
				insuranceRequiredFlag = true;
				
				String insuranceExpiryDateFormStr  = (String)parametersMap.get(rsbundle.getString("INSURANCEEXPIRYDATE"));
				log.info("createCollateralJAXBObject: insuranceExpiryDateFormStr is "+insuranceExpiryDateFormStr);
				if(null !=insuranceExpiryDateFormStr)
				{
					sf  = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date insuranceExpiryDateFormDt= sf.parse(insuranceExpiryDateFormStr);
					
					Date insuranceExpiryDate = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(insuranceExpiryDateFormDt);
					
					Long day = Long.valueOf(cal.get(cal.DAY_OF_MONTH));
					Long month = Long.valueOf(cal.get(cal.MONTH)+1);					
					Long year = Long.valueOf(cal.get(cal.YEAR));
					
					insuranceExpiryDate.setDay(day);
					insuranceExpiryDate.setMonth(month);
					insuranceExpiryDate.setYear(year);
				
				collateralItemInfo.setInsExpDt(insuranceExpiryDate);
				}
				collateralItemInfo.setInsRequiredFlag(insuranceRequiredFlag);
			}
			else
				collateralItemInfo.setInsRequiredFlag(insuranceRequiredFlag);
		
		}
		
		
		collateralVal  = new CollateralValueType();
		String fsv = (String)parametersMap.get(rsbundle.getString("FSV"));
		log.info("createCollateralJAXBObject: fsv is "+fsv);
		String boq = (String)parametersMap.get(rsbundle.getString("BOQ"));
		log.info("createCollateralJAXBObject: boq is "+boq);
		BigDecimal fsvAmt = null,boqAmt=null,collateralAmount=null;
		if(null!=fsv && null != boq)
		{
			fsv = fsv.replaceAll(",", "");
			fsvAmt = getBigDecimalWithDecimalPoint(Double.valueOf(fsv));
			boq = boq.replaceAll(",", "");
			boqAmt = getBigDecimalWithDecimalPoint(Double.valueOf(boq));
			collateralAmount = fsvAmt.add(boqAmt);
			log.info("createCollateralJAXBObject: collateralAmount is "+collateralAmount);
			
		}
		if(null != fsv)
		{
			fsv = fsv.replaceAll(",", "");
			fsvAmt = getBigDecimalWithDecimalPoint(Double.valueOf(fsv));
			collateralAmount = fsvAmt;
		}
		if(null != boq)
		{
			boq = boq.replaceAll(",", "");
			boqAmt = getBigDecimalWithDecimalPoint(Double.valueOf(boq));
			collateralAmount = boqAmt;
		}
		
		originalAmt = new CurrencyAmount();
		originalAmt.setAmt(collateralAmount);
		originalAmt.setCurCode(currencyCode);
		
		collateralVal.setUnitPriceAmt(originalAmt);
		collateralVal.setCollateralAmt(originalAmt);
		collateralVal.setCollateralOriginalAmt(originalAmt);
		
		collateralVal.setUnitsNum("1"); //Always Hardcoded
		collateralVal.setMarginPercent(new BigDecimal(100));//Always Hardcoded
		
		Date lastRepriseDate = null;
		String lastrepriceDateStr = (String)parametersMap.get(rsbundle.getString("LASTREPRICEDATE"));
		log.info("createCollateralJAXBObject: lastRepriceDate is "+lastrepriceDateStr);
		if(null!=lastrepriceDateStr)
		{
			sf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date lastRepriceDateForm = sf.parse(lastrepriceDateStr);
			
			lastRepriseDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastRepriceDateForm);
			
			Long day = Long.valueOf(cal.get(cal.DAY_OF_MONTH));
			Long month = Long.valueOf(cal.get(cal.MONTH)+1);					
			Long year = Long.valueOf(cal.get(cal.YEAR));
			
			
			lastRepriseDate.setDay(day);
			lastRepriseDate.setMonth(month);
			lastRepriseDate.setYear(year);
			
			collateralVal.setLastRepriceDt(lastRepriseDate);
		}
		
		
		collateralItemInfo.setCollateralValue(collateralVal);
		
		 
		todatDt =	new java.util.Date();
		reviewCal =	Calendar.getInstance();
		reviewCal.setTime(todatDt);
		monthFormat =	String.valueOf(reviewCal.get(reviewCal.DAY_OF_MONTH));
		if(monthFormat.length()==1)
			monthFormat="0"+monthFormat;
		collateralItemInfo.setCollateralReviewFreq("M012"+monthFormat);
		
		int reviewNextYearInt = (int) (reviewCal.get(Calendar.MONTH)+12);
		log.info("createCollateralJAXBObject: reviewNextYearInt is "+reviewNextYearInt);
		reviewCal.set(Calendar.MONTH, reviewNextYearInt);
		java.util.Date nextYearDt = reviewCal.getTime();
		
		
		nextCollateralReviewDt = new Date();
		
		nextCollateralReviewDt.setDay(Long.valueOf(reviewCal.get(reviewCal.DAY_OF_MONTH)));
		int reviewFrqInt1 = reviewCal.get(reviewCal.MONTH)+1;
		nextCollateralReviewDt.setMonth(Long.valueOf(reviewFrqInt1)); //Month starts with 0, increment by 1
		nextCollateralReviewDt.setYear(Long.valueOf(reviewCal.get(reviewCal.YEAR)));
		collateralItemInfo.setNextCollateralReviewDt(nextCollateralReviewDt);
		
		
		//if(null!=propAgainstLoan)
		{
			//if(propAgainstLoan)
			{
				propertyCode = (String)parametersMap.get(rsbundle.getString("PROPRTYTYPECODE"));
				log.info("createCollateralJAXBObject: propertyCode is "+propertyCode);
				
				String deedNo = (String)parametersMap.get(rsbundle.getString("DEEDNO"));
				String bondNo = (String)parametersMap.get(rsbundle.getString("BONDNO"));
				String extentA = (String)parametersMap.get(rsbundle.getString("EXTENTA"));
				String extentB = (String)parametersMap.get(rsbundle.getString("EXTENTR"));
				String extentC = (String)parametersMap.get(rsbundle.getString("EXTENTP"));
				
				propertyType =	new PropertyType();
				propertyType.setPropertyTypeCode(propertyCode);
				propertyType.setPropertyShortLegal(deedNo);
				if(null!=bondNo)
				{
					propertyType.setLegalRegistrationId(bondNo);
					String bondRegDateStr =(String) parametersMap.get(rsbundle.getString("BONDREGDATE"));
					log.info("createCollateralJAXBObject: bondRegDateStr is "+bondRegDateStr);
					if(null!=bondRegDateStr)
					{
						sf  = new SimpleDateFormat("dd/MM/yyyy");
						java.util.Date bondRegDate = sf.parse(bondRegDateStr);
						log.info("createCollateralJAXBObject: bondRegDate is "+bondRegDate);
					
						Date legalRegisterationDate = new Date();
						Calendar cal = Calendar.getInstance();
						cal.setTime(bondRegDate);
						
						Long day = Long.valueOf(cal.get(cal.DAY_OF_MONTH));
						Long month = Long.valueOf(cal.get(cal.MONTH)+1);					
						Long year = Long.valueOf(cal.get(cal.YEAR));
						
						
						legalRegisterationDate.setDay(day);
						legalRegisterationDate.setMonth(month);
						legalRegisterationDate.setYear(year);
						
						propertyType.setLegalRegistrationDt(legalRegisterationDate);
					}
				}
				
				CurrencyAmount appraisalAmount  = null;
				appraisalAmount = new CurrencyAmount();
				appraisalAmount.setAmt(fsvAmt);
				appraisalAmount.setCurCode(currencyCode);
				
				propertyType.setAppraisalValue(appraisalAmount);
				
				if(null!=lastRepriseDate)
				{
					propertyType.setAppraisalDt(lastRepriseDate);
				}
				String appraisalName = (String)parametersMap.get(rsbundle.getString("APPRAISALNAME"));
				propertyType.setAppraisalName(appraisalName);
				propertyType.setPropertyLotSize(extentA+"A"+extentB+"R"+extentC+"P");
				collateralItemInfo.setProperty(propertyType);
			}
		}
		collateralItemAddRq.setCollateralItemInfo(collateralItemInfo);
		
		inputXML = generateInputXML(collateralItemAddRq);
		constantXML = getConstantXML();
		messageRquid = null;
		messageRquid = GenerateUUID.getUUID();
		collateralSvcRq = "<LoanSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</LoanSvcRq></IFX>";
		inputRq = constantXML+collateralSvcRq;
		log.info("createCollateralJAXBObject:inputRq is "+inputRq);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			log.error("createCollateralJAXBObject:inputRq ParseException Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createCollateralJAXBObject:inputRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			currencyCode = null;dateFormat=null;serviceRquid=null;collateralCode=null;guaranterName1=null;guaranterName2=null;
			guaranterNic1 =null;guaranterNic2 = null;proposedCollateral=null;monthFormat=null;collateralActNumber=null;collateralActType=null;
			reviewFrqDtStr = null;propertyCode=null;inputXML=null;constantXML=null;messageRquid=null;collateralSvcRq=null;
			collateralItemAddRq = null; 
			custId = null;
			rquId = null;
			commodity = null;
			collateralItemInfo = null; 
			collateralDescList = null;
			collateralVal = null;
			originalAmt  = null;unitPriceAmt = null;
			collateralOriginalAmt = null;
			proposedAmt = null;orginalAmtDouble=null;
			todatDt = null;reviewFreqDt=null;
			nextCollateralReviewDt = null;matDate=null; 
			fdAgainstLoan = null;saAgainstLoan=null;propAgainstLoan=null;
			deptActId = null; 
			reviewCal=null;reviewFrqCal = null;
			propertyType = null; 
		}
		return inputRq;
	}
	public String createCollateralJAXBObject(HashMap parametersMap) throws ParseException,Exception
	{
		ResourceBundle rsbundle = null;
		String currencyCode = null,dateFormat=null,serviceRquid=null,collateralCode=null,guaranterName1=null,guaranterName2=null;
		String guaranterNic1 =null,guaranterNic2 = null,proposedCollateral=null,monthFormat=null,collateralActNumber=null,collateralActType=null;
		String reviewFrqDtStr = null,propertyCode=null,inputXML=null,constantXML=null,messageRquid=null,collateralSvcRq=null,inputRq=null;
		CollateralItemAddRq collateralItemAddRq = null; 
		CustId custId = null;
		RqUID rquId = null;
		CommodityType commodity = null;
		CollateralItemInfoType collateralItemInfo = null; 
		List collateralDescList = null;
		CollateralValueType collateralVal = null;
		CurrencyAmount originalAmt  = null,unitPriceAmt = null;
		BigDecimal collateralOriginalAmt = null;
		Double proposedAmt = null;
		String orginalAmtDouble=null;
		java.util.Date todatDt = null,reviewFreqDt=null;
		Date nextCollateralReviewDt = null,matDate=null; 
		Boolean fdAgainstLoan = false,saAgainstLoan=false,propAgainstLoan=false;
		DepAcctIdType deptActId = null; 
		Calendar reviewCal=null,reviewFrqCal = null;
		PropertyType propertyType = null; 
		try
		{
		rsbundle =getResourceBundle();
		currencyCode = (String)rsbundle.getString("CURRENCYCODE");
		dateFormat = (String)rsbundle.getString("dateFormat");
		
		collateralItemAddRq =	new CollateralItemAddRq();
		 
		rquId = new RqUID();
		serviceRquid = null;	
		serviceRquid = GenerateUUID.getUUID();		
		rquId.setValue(serviceRquid);
		
		collateralItemAddRq.setRqUID(rquId);
		
		custId = new CustId();
		custId.setSPName(SPNameType.FISERV_ICBS);
		custId.setCustPermId((String)parametersMap.get(rsbundle.getString("CIFNO")));
		
		collateralItemAddRq.setCustId(custId);
		
		
		collateralItemInfo =new CollateralItemInfoType();
		collateralCode = null;
		collateralCode = (String)parametersMap.get(rsbundle.getString("COLLATERALCODE"));
		log.info("createCollateralJAXBObject: collateralCode"+collateralCode);
		collateralItemInfo.setCollateralCode(collateralCode);
		
		
		commodity = new CommodityType();
		commodity.setCommodityId((String)parametersMap.get(rsbundle.getString("CIFNO")));
		collateralItemInfo.setCommodity(commodity);
		collateralItemInfo.setLocationCode((String)parametersMap.get(rsbundle.getString("BRANCHCODE")));
		collateralItemInfo.setCollateralShortDesc((String)parametersMap.get(rsbundle.getString("COLLATERALDESC")));
		log.info("createCollateralJAXBObject: collateralDescription is "+parametersMap.get(rsbundle.getString("COLLATERALDESC")));
		String guaranteeCollateralCode = rsbundle.getString("COLLATERALGUARANTEECODE");
		String[] guaranteeCollateralCodeArr = guaranteeCollateralCode.split(",");
		
		boolean guaranteeLoanFlag = false;
		
		if(null!= collateralCode && null!=guaranteeCollateralCodeArr)
		{
			for(String guaranteeCode:guaranteeCollateralCodeArr)
			{
				if(guaranteeCode.equalsIgnoreCase(collateralCode))
				{
					guaranteeLoanFlag = true;
				}
			}
		}
		/*String fdCollateralCode = rsbundle.getString("COLLATERALFDCODE");
		String[] fdCollateralCodeArr = fdCollateralCode.split(",");
		if(null!= collateralCode && null!=fdCollateralCodeArr)
		{
			for(String fdCode:fdCollateralCodeArr)
			{
				if(fdCode.equalsIgnoreCase(collateralCode))
				{
					fdAgainstLoan = true;
				}
			}
		}*/
		
		
		String intGuarantyCode = null; 
		intGuarantyCode =(String)parametersMap.get(rsbundle.getString("GUARANTEECODE"));
		log.info("createCollateralJAXBObject:intGuarantyCode is "+intGuarantyCode);
		System.out.println("createCollateralJAXBObject:intGuarantyCode is "+intGuarantyCode);
		//String intGuarantyCode = "Rate cannot change";
		if(null!=intGuarantyCode)
		{
			if(intGuarantyCode.equalsIgnoreCase((String)rsbundle.getString("RATECHANGEINDEXREVIEW")))
			{
				fdAgainstLoan=true;
			}
		}
		
		String saCollateralCode = rsbundle.getString("COLLATERALSACODE");
		String[] saCollateralCodeArr = saCollateralCode.split(",");
		if(null!= saCollateralCode && null!=saCollateralCodeArr)
		{
			for(String saCode:saCollateralCodeArr)
			{
				if(saCode.equalsIgnoreCase(collateralCode))
				{
					saAgainstLoan = true;
				}
			}
		}
		
		String propertyCollateralCode = rsbundle.getString("COLLATERALPROPERTYCODE");
		String[] propertyCollateralCodeArr = propertyCollateralCode.split(",");
		if(null!= propertyCollateralCode && null!=propertyCollateralCodeArr)
		{
			for(String propertyCollCode:propertyCollateralCodeArr)
			{
				if(propertyCollCode.equalsIgnoreCase(collateralCode))
				{
					propAgainstLoan = true;
				}
			}
		}
		
		if(guaranteeLoanFlag)
		{
			String collateralDesc1  = null,collateralDesc2=null;
			guaranterName1 = (String)parametersMap.get(rsbundle.getString("GUARANTERNAME1"));
			log.info("createCollateralJAXBObject: guaranterName1 is "+guaranterName1);
			guaranterName2 = (String)parametersMap.get(rsbundle.getString("GUARANTERNAME2"));
			log.info("createCollateralJAXBObject: guaranterName2 is "+guaranterName2);
			guaranterNic1 = (String)parametersMap.get(rsbundle.getString("GUARANTERNIC1"));
			log.info("createCollateralJAXBObject: guaranterNic1 is "+guaranterNic1);
			guaranterNic2 = (String)parametersMap.get(rsbundle.getString("GUARANTERNIC2"));
			log.info("createCollateralJAXBObject: guaranterNic2 is "+guaranterNic2);
			if(null!= guaranterName1)
			{
				collateralDesc1 = guaranterName1;
				collateralItemInfo.setCollateralDesc1(collateralDesc1);
			}
			if(null!=guaranterNic1)
			{
				if(null!=collateralDesc1 && collateralDesc1.length()>0)
					collateralDesc1 = collateralDesc1+" "+guaranterNic1;
				else
					collateralDesc1 = guaranterNic1;
				
				collateralItemInfo.setCollateralDesc1(collateralDesc1);
			}
			if(null!= guaranterName2)
			{
				collateralDesc2 = guaranterName2;
				collateralItemInfo.setCollateralDesc2(collateralDesc2);
			}
			if(null!=guaranterNic2)
			{
				if(null!=collateralDesc2 && collateralDesc2.length()>0)
					collateralDesc2 = collateralDesc2+" "+guaranterNic2;
				else
					collateralDesc2 = guaranterNic2;
				
				collateralItemInfo.setCollateralDesc2(collateralDesc2);
			}
			/*if(null!= guaranterName1 && null!=guaranterNic1)
				collateralItemInfo.setCollateralDesc1(guaranterName1+" "+guaranterNic1);
			if(null!= guaranterName2 && null!=guaranterNic2)
				collateralItemInfo.setCollateralDesc2(guaranterName2+" "+guaranterNic2);*/
		}
		else
		{
			proposedCollateral = (String)parametersMap.get(rsbundle.getString("PROPOSEDCOLLATERAL"));
			log.info("createCollateralJAXBObject: proposedCollateral is "+proposedCollateral);
			if(null!= guaranterName1 && null!=guaranterNic1)
			{
				if(null!=proposedCollateral)
				{
					//String[] collateralArray proposedCollateral.split("(?<=\\G.{60})");
					collateralDescList = splitEqually(proposedCollateral,60);
					if(null!= collateralDescList && collateralDescList.size()>0)
					{
							if(collateralDescList.size()==1)
							{
								collateralItemInfo.setCollateralDesc1((String)collateralDescList.get(0));
							}
							else if(collateralDescList.size()==2)
							{
								collateralItemInfo.setCollateralDesc1((String)collateralDescList.get(0));
								collateralItemInfo.setCollateralDesc2((String)collateralDescList.get(1));
							}
							else if(collateralDescList.size()==3)
							{
								collateralItemInfo.setCollateralDesc1((String)collateralDescList.get(0));
								collateralItemInfo.setCollateralDesc2((String)collateralDescList.get(1));
								collateralItemInfo.setCollateralDesc3((String)collateralDescList.get(2));
							}
					}
					
				}
			}
		}
		
		
		collateralVal  = new CollateralValueType();
		
		orginalAmtDouble = (String)parametersMap.get(rsbundle.getString("COLLATERALAMOUNT"));
			
		//orginalAmtDouble = 11111.00;
		log.info("createCollateralJAXBObject: orginalAmtDouble is "+orginalAmtDouble);
		if(null!= orginalAmtDouble)
		{
			orginalAmtDouble = orginalAmtDouble.replaceAll(",", "");
			
			collateralOriginalAmt = getBigDecimalWithDecimalPoint(Double.valueOf(orginalAmtDouble));
			originalAmt = new CurrencyAmount();
			originalAmt.setAmt(collateralOriginalAmt);
			originalAmt.setCurCode(currencyCode);
			
			collateralVal.setUnitPriceAmt(originalAmt);
			collateralVal.setCollateralAmt(originalAmt);
			collateralVal.setCollateralOriginalAmt(originalAmt);
		}
		
		collateralVal.setUnitsNum("1"); //Always Hardcoded
		collateralVal.setMarginPercent(new BigDecimal(100));//Always Hardcoded
		
		collateralItemInfo.setCollateralValue(collateralVal);
		
		 
		todatDt =	new java.util.Date();
		reviewCal =	Calendar.getInstance();
		reviewCal.setTime(todatDt);
		monthFormat =	String.valueOf(reviewCal.get(reviewCal.DAY_OF_MONTH));
		if(monthFormat.length()==1)
			monthFormat="0"+monthFormat;
		collateralItemInfo.setCollateralReviewFreq("M012"+monthFormat);
		
		int reviewNextYearInt = (int) (reviewCal.get(Calendar.MONTH)+12);
		log.info("createCollateralJAXBObject: reviewNextYearInt is "+reviewNextYearInt);
		reviewCal.set(Calendar.MONTH, reviewNextYearInt);
		java.util.Date nextYearDt = reviewCal.getTime();
		
		
		nextCollateralReviewDt = new Date();
		
		nextCollateralReviewDt.setDay(Long.valueOf(reviewCal.get(reviewCal.DAY_OF_MONTH)));
		int reviewFrqInt1 = reviewCal.get(reviewCal.MONTH)+1;
		nextCollateralReviewDt.setMonth(Long.valueOf(reviewFrqInt1)); //Month starts with 0, increment by 1
		nextCollateralReviewDt.setYear(Long.valueOf(reviewCal.get(reviewCal.YEAR)));
		collateralItemInfo.setNextCollateralReviewDt(nextCollateralReviewDt);
		
		if(null!= fdAgainstLoan || null!= saAgainstLoan)
		{
			if(fdAgainstLoan || saAgainstLoan)
			{
				collateralActNumber = (String)parametersMap.get(rsbundle.getString("COLLATERALACCTNUMBER"));
				log.info("createCollateralJAXBObject: collateralActNumber is "+collateralActNumber);
				collateralActType = (String)parametersMap.get(rsbundle.getString("COLLATERALACCTTYPE"));
				log.info("createCollateralJAXBObject: collateralActType is "+collateralActType);
				
				deptActId = new DepAcctIdType();
				deptActId.setAcctId(collateralActNumber);
				if(null!=collateralActType && collateralActType.equalsIgnoreCase("TM"))
					deptActId.setAcctType(DDSVTMAcctType.TM);
				else
					deptActId.setAcctType(DDSVTMAcctType.EX);
				collateralItemInfo.setDepAcctId(deptActId);
			}
			if(fdAgainstLoan)
			{
				matDate = new Date();
				//reviewFreqDt = 	(java.util.Date)parametersMap.get(rsbundle.getString("RATEREVIEWDATE"));
				reviewFrqDtStr = (String) parametersMap.get(rsbundle.getString("RATEREVIEWDATE"));
				log.info("createCollateralJAXBObject: reviewFrqDtStr is "+reviewFrqDtStr);
				SimpleDateFormat sf = null; 
				SimpleDateFormat originalFormat = null;
				originalFormat = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date reviewFreqDtOriginal = originalFormat.parse(reviewFrqDtStr);
				sf = new SimpleDateFormat(dateFormat);
				String reviewFrqDtOrignialStr = sf.format(reviewFreqDtOriginal);
				reviewFreqDt = sf.parse(reviewFrqDtOrignialStr);
				
				reviewFrqCal =	Calendar.getInstance();
				if(null!=reviewFreqDt)
				{
					reviewFrqCal.setTime(reviewFreqDt);
					int reviewMonthInt = reviewFrqCal.get(reviewFrqCal.MONTH)+1;
					matDate.setDay(Long.valueOf(reviewFrqCal.get(reviewFrqCal.DAY_OF_MONTH)));
					matDate.setMonth(Long.valueOf(reviewMonthInt));
					matDate.setYear(Long.valueOf(reviewFrqCal.get(reviewFrqCal.YEAR)));
					collateralItemInfo.setMatDt(matDate);
				}
			}
		}
		if(null!=propAgainstLoan)
		{
			if(propAgainstLoan)
			{
				propertyCode = (String)parametersMap.get(rsbundle.getString("PROPRTYTYPECODE"));
				log.info("createCollateralJAXBObject: propertyCode is "+propertyCode);
				propertyType =	new PropertyType();
				propertyType.setPropertyTypeCode(propertyCode);
				collateralItemInfo.setProperty(propertyType);
			}
		}
		collateralItemAddRq.setCollateralItemInfo(collateralItemInfo);
		
		inputXML = generateInputXML(collateralItemAddRq);
		constantXML = getConstantXML();
		messageRquid = null;
		messageRquid = GenerateUUID.getUUID();
		collateralSvcRq = "<LoanSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</LoanSvcRq></IFX>";
		inputRq = constantXML+collateralSvcRq;
		log.info("createCollateralJAXBObject:inputRq is "+inputRq);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			log.error("createCollateralJAXBObject:inputRq ParseException Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createCollateralJAXBObject:inputRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			currencyCode = null;dateFormat=null;serviceRquid=null;collateralCode=null;guaranterName1=null;guaranterName2=null;
			guaranterNic1 =null;guaranterNic2 = null;proposedCollateral=null;monthFormat=null;collateralActNumber=null;collateralActType=null;
			reviewFrqDtStr = null;propertyCode=null;inputXML=null;constantXML=null;messageRquid=null;collateralSvcRq=null;
			collateralItemAddRq = null; 
			custId = null;
			rquId = null;
			commodity = null;
			collateralItemInfo = null; 
			collateralDescList = null;
			collateralVal = null;
			originalAmt  = null;unitPriceAmt = null;
			collateralOriginalAmt = null;
			proposedAmt = null;orginalAmtDouble=null;
			todatDt = null;reviewFreqDt=null;
			nextCollateralReviewDt = null;matDate=null; 
			fdAgainstLoan = null;saAgainstLoan=null;propAgainstLoan=null;
			deptActId = null; 
			reviewCal=null;reviewFrqCal = null;
			propertyType = null; 
		}
		return inputRq;
	}
	
	public CreateCollateralResponse invokeCommunicator(String inputXML,String referenceNo) throws Exception
	{
		CreateCollateralResponse createCollateralResponse = null;
		try
		{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String collateralIdKey  = rsbundle.getString("COLLATERALID");
		String collateralErrorCodeKey  = rsbundle.getString("COLLATERALERRORCODE");
		String collateralErrorDescKey  = rsbundle.getString("COLLATERALERRORDESC");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		//String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl);
		String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
		log.info("invokeCommunicator: Invoked Communicator"+outputXML);
		System.out.println("outputXML is "+outputXML);
		String startIndexStr = rsbundle.getString("CreateCollateralServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CreateCollateralServiceImpl.truncateEndStr");
		String startStatusCode = rsbundle.getString("IFXStatusCode");
		String endStatusCode =  rsbundle.getString("IFXEndStatusCode");
		String startStatusDesc = rsbundle.getString("IFXStatusDescription");
		String endStatusDesc = rsbundle.getString("IFXEndStatusDescription");
		Long statusCode = null;
		String	statusDescription=null, collateralId = null;
		HashMap statusMap = null;
		log.info("invokeCommunicator: Checking for Error Response");
		
		String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
		CollateralItemAddRs collateralAddRs= (CollateralItemAddRs) generateOutputObject(removedIFX,CollateralItemAddRs.class);
		if(null!=collateralAddRs)
		{
			StatusType statusType =  collateralAddRs.getStatus();
			statusCode = statusType.getStatusCode();
			System.out.println("statusCode is "+statusCode);
			if(null!=statusCode && statusCode==0)
			{
				createCollateralResponse= new CreateCollateralResponse();
				collateralId = collateralAddRs.getCollateralItemRec().getCollateralId();
				log.info("invokeCommunicator:collateralId is "+collateralId);
				createCollateralResponse.setCollateralId(collateralId);
			}
			else
			{
				createCollateralResponse = new CreateCollateralResponse();
				String errorNum = collateralAddRs.getStatus().getError().get(0).getErrNum();
				log.info("invokeCommunicator: Error Code is "+errorNum);
				String errorDesc = collateralAddRs.getStatus().getError().get(0).getErrDesc();
				createCollateralResponse.setErrorCode(collateralAddRs.getStatus().getError().get(0).getErrNum());
				log.info("invokeCommunicator: Error Code is "+collateralAddRs.getStatus().getError().get(0).getErrDesc());
				createCollateralResponse.setErrorDescription(collateralAddRs.getStatus().getError().get(0).getErrDesc());
				System.out.println(errorNum+" "+errorDesc);
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
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			CollateralItemAddRs collateralAddRs= (CollateralItemAddRs) generateOutputObject(removedIFX,CollateralItemAddRs.class);
			String collateralId = null;
			if(null != collateralAddRs)
			{
				createCollateralResponse= new CreateCollateralResponse();
				collateralId = collateralAddRs.getCollateralItemRec().getCollateralId();
				log.info("invokeCommunicator:collateralId is "+collateralId);
				createCollateralResponse.setCollateralId(collateralId);
			}
		}
		else
		{
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			CollateralItemAddRs collateralAddRs= (CollateralItemAddRs) generateOutputObject(removedIFX,CollateralItemAddRs.class);
			log.info("invokeCommunicator:collateralAddRs is "+collateralAddRs);
			log.info("invokeCommunicator:Error Number is "+collateralAddRs.getStatus().getError().get(0).getErrNum());
			log.info("invokeCommunicator:Error Description is "+collateralAddRs.getStatus().getError().get(0).getErrDesc());
			if(null != collateralAddRs)
			{
			createCollateralResponse = new CreateCollateralResponse();
			String errorNum = collateralAddRs.getStatus().getError().get(0).getErrNum();
			log.info("invokeCommunicator: Error Code is "+errorNum);
			String errorDesc = collateralAddRs.getStatus().getError().get(0).getErrDesc();
			createCollateralResponse.setErrorCode(collateralAddRs.getStatus().getError().get(0).getErrNum());
			log.info("invokeCommunicator: Error Code is "+collateralAddRs.getStatus().getError().get(0).getErrDesc());
			createCollateralResponse.setErrorDescription(collateralAddRs.getStatus().getError().get(0).getErrDesc());
			}
		}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("invokeCommunicator: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return createCollateralResponse;
	}
	
	public static List<String> splitEqually(String text, int size) {
	    // Give the list the right capacity to start with. You could use an array
	    // instead if you wanted.
	    List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

	    for (int start = 0; start < text.length(); start += size) {
	        ret.add(text.substring(start, Math.min(text.length(), start + size)));
	    }
	    return ret;
	}
	
	
	public static void main(String[] a)
	{
		HashMap propertyMap = null;
		propertyMap = new HashMap();
		propertyMap.put("BOC_CIFId", "0001912835");
		//propertyMap.put("BOC_CollateralCode", "40");
		propertyMap.put("BOC_COLLATERALDESC", "dsdsa");
		propertyMap.put("BOC_BranchCode", "298");
		/*propertyMap.put("BOC_GuarantorName", value);
		propertyMap.put("BOC_Gurantor2Name", value);
		propertyMap.put("BOC_GuarantorNICNo", value);
		propertyMap.put("BOC_Gurantor2NICNo", value);*/
		propertyMap.put("BOC_FacilitiesProposedCollateral", "sdgfdsgsdfgfdsfdsfdsfsdfsdfsdfsdfsdfsdfafdsdgsdfsgdfgdfgdfdf");
		propertyMap.put("BOC_OriginalAmount", 100000.0);
		propertyMap.put("BOC_LoanAmount", 100000.0);		
		propertyMap.put("OC_CollateralSecurityAccountNo", "000076796376");
		propertyMap.put("BOC_VRReviewDate", "08-12-2016");
		//propertyMap.put("BOC_PROPERTY_CODE", value);
		CreateCollateralServiceImpl serviceImpl = new CreateCollateralServiceImpl();
		try {
			String inputXML = serviceImpl.createCollateralJAXBObject(propertyMap);
			System.out.println("inputXML is "+inputXML);
			serviceImpl.invokeCommunicator(inputXML,"");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
