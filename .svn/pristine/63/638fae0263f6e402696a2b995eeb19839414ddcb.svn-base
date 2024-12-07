

package com.boc.fiserv.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
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

import com.boc.CIFSvc.CustProfBasicInq.rs.CustProfBasicInqRs;
import com.boc.LoanSvc.LoanAcctAdd.rq.ApplicationInfoType;
import com.boc.LoanSvc.LoanAcctAdd.rq.BankInfoType;
import com.boc.LoanSvc.LoanAcctAdd.rq.BillCodeType;
import com.boc.LoanSvc.LoanAcctAdd.rq.CollateralInfoType;
import com.boc.LoanSvc.LoanAcctAdd.rq.CollateralItemInfoType;
import com.boc.LoanSvc.LoanAcctAdd.rq.CollateralPledgeType;
import com.boc.LoanSvc.LoanAcctAdd.rq.CollateralValueType;
import com.boc.LoanSvc.LoanAcctAdd.rq.CollectFromAcctType;
import com.boc.LoanSvc.LoanAcctAdd.rq.CommodityType;
import com.boc.LoanSvc.LoanAcctAdd.rq.CurrencyAmount;
import com.boc.LoanSvc.LoanAcctAdd.rq.CustRelationType;
import com.boc.LoanSvc.LoanAcctAdd.rq.Date;
import com.boc.LoanSvc.LoanAcctAdd.rq.DepAcctIdFromType;
import com.boc.LoanSvc.LoanAcctAdd.rq.DepAcctIdType;
import com.boc.LoanSvc.LoanAcctAdd.rq.IndirectLiabilityType;
import com.boc.LoanSvc.LoanAcctAdd.rq.IntGuarantyCodeType;
import com.boc.LoanSvc.LoanAcctAdd.rq.LTVTypeCodeType;
import com.boc.LoanSvc.LoanAcctAdd.rq.LiabilityTypeType;
import com.boc.LoanSvc.LoanAcctAdd.rq.LoanAcctAddRq;
import com.boc.LoanSvc.LoanAcctAdd.rq.LoanAcctBasicType;
import com.boc.LoanSvc.LoanAcctAdd.rq.LoanAcctDetailType;
import com.boc.LoanSvc.LoanAcctAdd.rq.LoanAcctAddRq.RqUID;
import com.boc.connector.CMConnector;
import com.boc.LoanSvc.LoanAcctAdd.rq.LoanAcctInfoType;
import com.boc.LoanSvc.LoanAcctAdd.rq.NoteTypeCodeType;
import com.boc.LoanSvc.LoanAcctAdd.rq.PmtSchedType;
import com.boc.LoanSvc.LoanAcctAdd.rq.PmtSchedTypeCodeType;
import com.boc.LoanSvc.LoanAcctAdd.rq.TermType;
import com.boc.LoanSvc.LoanAcctAdd.rq.TermUnitsType;
import com.boc.LoanSvc.LoanAcctAdd.rq.VariableRateInfoType;
import com.boc.LoanSvc.LoanAcctAdd.rs.LoanAcctAddRs;
import com.boc.LoanSvc.LoanAcctAdd.rs.StatusType;
import com.boc.fiserv.LoanAcctAddService;
import com.boc.fiserv.response.CalcDateResponse;
import com.boc.fiserv.response.CustomerProfileRs;
import com.boc.fiserv.response.LoanAcctAddResponse;
import com.boc.response.StatusBean;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Jun 24, 2016
*/
public class LoanAcctAddServiceImpl  extends BaseServiceImpl implements LoanAcctAddService
{
	//public static Logger log = Logger.getLogger("com.boc.fiserv.impl.LoanAcctAddServiceImpl");
	private static Logger log = LoggerFactory.getLogger(LoanAcctAddServiceImpl.class);
	public static void main(String[] a)
	{
		HashMap parametersMap = new HashMap();
		/*parametersMap.put("BOC_CIFNo", "123455");
		parametersMap.put("BOC_AccountNo", "123455");
		parametersMap.put("BOC_ReferenceNo", "123455");
		parametersMap.put("BOC_ProductCode", "2");
		parametersMap.put("BOC_BranchCode", "00002");
		parametersMap.put("BOC_FacilitiesProposedAmount", new BigDecimal(123455));
		parametersMap.put("BOC_RepaymentPeriod", new Date());
		parametersMap.put("BOC_FacilitiesProposedCollateral", "123");
		parametersMap.put("BOC_FacilitiesProposedInterestRates", new BigDecimal(12));*/
		LoanAcctAddServiceImpl serviceImpl = new LoanAcctAddServiceImpl();
		//serviceImpl.createLoanActAddRqJAxbObject(parametersMap);
		String referenceNumber = "0001000802016000187";
		try {
			//serviceImpl.execute(referenceNumber,"","Personal Loan");
			String inputXML = "<IFX><SignonRq><SignonPswd><SignonRole>CSR</SignonRole><CustId><SPName>FiservICBS</SPName><CustLoginId>SGDMSID</CustLoginId></CustId><CustPswd><CryptType>NONE</CryptType><Pswd>sg78@789</Pswd></CustPswd><GenSessKey>0</GenSessKey></SignonPswd><ClientDt><Year>2016</Year><Month>8</Month><Day>30</Day></ClientDt><ClientApp><Org>Fiserv</Org><Name>DM</Name><Version>0.1</Version><ClientAppKey>BOCSRVRHECGNSYSQCUWRJDHKRFDBNTGN</ClientAppKey></ClientApp><ComputerId>SGPVMAPRDEV03</ComputerId><InstitutionCode>BOCSR</InstitutionCode></SignonRq><EnvironmentInfo><EnvironmentName>default</EnvironmentName></EnvironmentInfo><LoanSvcRq><RqUID>ac5e457a-becf-4ff2-97e6-442e7fa6b780</RqUID><SPName>FiservICBS</SPName><LoanAcctAddRq><RqUID>fb0e077b-f08c-449f-bd97-a6dfcc23bddb</RqUID><LoanAcctInfo><CustRelation><RelationCode>SOW</RelationCode><CustPermId>0005048693</CustPermId></CustRelation><LoanAcctBasic><ApplicationInfo><ApplicationId>100010000070</ApplicationId><ApplicationSeq>00070</ApplicationSeq></ApplicationInfo><ProdId>1</ProdId><BranchId>1</BranchId><PrimOfficerCode>WEB</PrimOfficerCode><NoteTypeCode>Term</NoteTypeCode><OpenAmt><Amt>1234567.00</Amt><CurCode>000</CurCode></OpenAmt><IntGuarantyCode>RateChgWithIndexChg</IntGuarantyCode><IntRate>10</IntRate><FirstPmtDt><Year>2016</Year><Month>7</Month><Day>26</Day></FirstPmtDt><Term><Count>120</Count><TermUnits>Months</TermUnits></Term><BillCode>TransferOrder</BillCode><FDICCode>1</FDICCode><FRBCode>102</FRBCode><LTVTypeCode>Original</LTVTypeCode></LoanAcctBasic><LoanAcctDetail><CensusTractCode>101</CensusTractCode><MSACode>1</MSACode><LoanApprovalCode>Y</LoanApprovalCode><RiskCode>A</RiskCode><AvailToDisbCalc>0</AvailToDisbCalc></LoanAcctDetail><VariableRateInfo><IndexId>1</IndexId></VariableRateInfo><PmtSched><PmtSchedUseNum>119</PmtSchedUseNum><PmtFreq>M00126</PmtFreq><PmtSchedNextPmtDt><Year>2016</Year><Month>7</Month><Day>26</Day></PmtSchedNextPmtDt><PmtSchedTypeCode>PrincipalAndInt</PmtSchedTypeCode><PmtSchedAmt><Amt>16314.889999999999417923390865325927734375</Amt><CurCode>000</CurCode></PmtSchedAmt><IntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></IntBearingEscrowAmt><NonIntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></NonIntBearingEscrowAmt></PmtSched><PmtSched><PmtSchedUseNum>1</PmtSchedUseNum><PmtFreq>M00126</PmtFreq><PmtSchedNextPmtDt><Year>2026</Year><Month>6</Month><Day>26</Day></PmtSchedNextPmtDt><PmtSchedTypeCode>PrincipalAndInt</PmtSchedTypeCode><PmtSchedAmt><Amt>999999999999999.99</Amt><CurCode>000</CurCode></PmtSchedAmt><IntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></IntBearingEscrowAmt><NonIntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></NonIntBearingEscrowAmt></PmtSched><CollectFromAcct><AnchorProfile>187</AnchorProfile><TargetProfile>115</TargetProfile><DepAcctIdFrom><AcctId>4313867</AcctId><AcctType>SV</AcctType><BankInfo><BankId>1</BankId></BankInfo></DepAcctIdFrom><ReferenceId>0001000012016000265</ReferenceId><Description>Ln Recovery</Description><CurAmt><Amt>16314.889999999999417923390865325927734375</Amt><CurCode>000</CurCode></CurAmt><Freq>M00126</Freq><StartDt><Year>2016</Year><Month>7</Month><Day>26</Day></StartDt></CollectFromAcct><CollateralInfo><CollateralId>000002117491</CollateralId><CollateralPledge><PledgeRule>FaceAmount</PledgeRule><MaxPledgeAmt><Amt>15555.00</Amt><CurCode>000</CurCode></MaxPledgeAmt></CollateralPledge></CollateralInfo></LoanAcctInfo></LoanAcctAddRq></LoanSvcRq></IFX>";
			serviceImpl.invokeCommunicator(inputXML);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String execute(String referenceNumber,String applicationId,String caseTypeName) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		String homeLoanCaseType = rsbundle.getString("HOMELOANCASETYPE");
		String loanActAddParameters=null;
		if(caseTypeName.equalsIgnoreCase(homeLoanCaseType))
		{
			loanActAddParameters = rsbundle.getString("HOMELOANACCTADDPARAMETERS");
		}
		else
		{
			loanActAddParameters = rsbundle.getString("LOANACCTADDPARAMETERS");
		}
		String[] loanAcctAddParametersArr = loanActAddParameters.split(",");
		HashMap parametersMap = getLoanAcctDetailsFromCM(referenceNumber,caseTypeName,loanAcctAddParametersArr);
		String inputXML = createLoanActAddRqJAxbObject(parametersMap,applicationId,caseTypeName);
		//invokeCommunicator(inputXML);
		return inputXML;
	}
	
	public HashMap getLoanAcctDetailsFromCM(String referenceNumber,String caseTypeName,String[] loanAcctAddParametersArr) throws Exception
	{
		CMConnector cmConnector = new CMConnector();
		log.info("getLoanAcctDetailsFromCM:Retrieving attributes from CaseManager");
		HashMap parametersMap =  cmConnector.getLoanAcctDetails(referenceNumber,caseTypeName,loanAcctAddParametersArr);
		return parametersMap;
	}
	
	public String createLoanActAddRqJAxbObject(HashMap parametersMap, String applicationId,String caseTypeName) throws ParseException, Exception
	{
		String inputRq = null,inputXML=null,constantXML=null;
		log.info("createLoanActAddRqJAxbObject:Creating LoanAcctAddRq JAXB object");
		String referenceNo=null,floorCeilingStr=null,reviewPer=null,reviewFreq=null,reviewFrqDay=null,indexId = null;
			String monthFormat=null,monthMatFormat=null,paymentFrequencyFormatted=null,paymentFrequencyMatFormatted=null,repaymentmethod=null,collectFromDay=null,accountType=null;
			Double proposedAmt =null,proposedReducingAmt=null,propsedInterestRate =null,proposedInstallment=null,floorIntPct=null,ceilingIntPct=null,indexVariance=null;
			//intGuarantyCode,firtPaymentCalMonthInt,paymentFrqInt,currentMaturity
			BigDecimal floorIntPctBigDecimal=null,ceilingPctBigDecimal=null,indexVarianceBigDecimal=null;
			Date firstPaymentLoanDt = null,reviewDt=null,pmtScheduleNextDate=null,collectFromDate=null;
			java.util.Date reviewFreqDt = null;
			Calendar firtPaymentCal =null,paymentMaturityCal=null,reviewFrqCal=null,paymentFrqMatCal=null,collectStartCal=null;

			ApplicationInfoType applicantInfo = null;
			Long repaymentPeriodLong =null;
			TermType termType = null;
			LoanAcctBasicType loanAcctBasic = null;
			LoanAcctDetailType loanAcctDetailType = null;
			VariableRateInfoType variableateInfoType=null;
			PmtSchedType pmdShedType =null,pmdShedType1 = null,pmdShedType2=null,pmdShedType3=null;
			CurrencyAmount currencyAmount= null,currAmountPmdShedType = null, currAmountIntBearing = null, currAmountPmdShedType1 = null;
			CurrencyAmount 	currAmountNonIntBearing = null,currAmountIntBearing1 = null,currAmountNonIntBearing1 = null,currencyAmountCollectFrom =null;
			DepAcctIdFromType depAcctIdType=null;
			CollectFromAcctType collectFromAccType=null;
			BankInfoType bankInfo = null;
			LoanAcctInfoType loanAcctInfo = null;
			LoanAcctAddRq loanAcctAddRq = null;
		try
		{
		ResourceBundle rsbundle =getResourceBundle();
		String currencyCode = (String)rsbundle.getString("CURRENCYCODE");
		String currentProcessingKey = (String)rsbundle.getString("CURRENTPROCESSINGKEY");
		//String otherProcessingDate = (String)rsbundle.getString("OTHERPROCESSINGDATE");
		String dateFormat = (String)rsbundle.getString("dateFormat");
		String homeLoanCaseType = rsbundle.getString("HOMELOANCASETYPE");
		log.info("homeLoanCaseType from property file is "+homeLoanCaseType+" and caseTypeName is "+caseTypeName );
		DateFormat formatter = new SimpleDateFormat(dateFormat); 
		//java.util.Date otherProcessingDt = (java.util.Date)formatter.parse(otherProcessingDate); 
		RqUID rquId = null; 
		rquId = new RqUID();
		String serviceRquid = null;	
		serviceRquid = GenerateUUID.getUUID();		
		rquId.setValue(serviceRquid);
		loanAcctAddRq = new LoanAcctAddRq();
		loanAcctAddRq.setRqUID(rquId);

		
	loanAcctInfo = new LoanAcctInfoType();
		
			CustRelationType custRelation1 = null;
			custRelation1 = new CustRelationType();
			String app2CifNo = (String) parametersMap.get(rsbundle.getString("APP2CIFNO"));
			boolean applicant2Found = false;
			if(null!=app2CifNo && !("".equalsIgnoreCase(app2CifNo)))
					applicant2Found = true;
			String app3CifNo = (String) parametersMap.get(rsbundle.getString("APP3CIFNO"));
			boolean applicant3Found = false;
			if(null!=app3CifNo && !("".equalsIgnoreCase(app3CifNo)))
					applicant3Found = true;
			
			if(!applicant2Found && !applicant3Found)
			{
				
					custRelation1.setRelationCode("SOW");
					custRelation1.setCustPermId((String)parametersMap.get(rsbundle.getString("CIFNO")));
					loanAcctInfo.getCustRelation().add(custRelation1);
				
			}
			else if(applicant2Found && !applicant3Found)
			{	
				//#updated by vikshith : for multiple applicant Same CIF was going twice and JA1 was going with out CustPermId.
				
				custRelation1.setRelationCode("JAF");
				custRelation1.setCustPermId((String)parametersMap.get(rsbundle.getString("CIFNO")));
				loanAcctInfo.getCustRelation().add(custRelation1);
				
				CustRelationType custRelation2 = new CustRelationType();
				custRelation2.setRelationCode("JA1");
				custRelation2.setCustPermId(app2CifNo);
				loanAcctInfo.getCustRelation().add(custRelation2);

			}
			else if(!applicant2Found && applicant3Found)
			{	
				//#updated by vikshith : for multiple applicant Same CIF was going twice and JA1 was going with out CustPermId.
				
				custRelation1.setRelationCode("JAF");
				custRelation1.setCustPermId((String)parametersMap.get(rsbundle.getString("CIFNO")));
				loanAcctInfo.getCustRelation().add(custRelation1);
				
				CustRelationType custRelation2 = new CustRelationType();
				custRelation2.setRelationCode("JA1");
				custRelation2.setCustPermId(app3CifNo);
				loanAcctInfo.getCustRelation().add(custRelation2);

			}
			else if(applicant2Found && applicant3Found)
			{	
				//#updated by vikshith : for multiple applicant Same CIF was going twice and JA1 was going with out CustPermId.
				
				custRelation1.setRelationCode("JAF");
				custRelation1.setCustPermId((String)parametersMap.get(rsbundle.getString("CIFNO")));
				loanAcctInfo.getCustRelation().add(custRelation1);
				
				CustRelationType custRelation2 = new CustRelationType();
				custRelation2.setRelationCode("JA1");
				custRelation2.setCustPermId(app2CifNo);
				loanAcctInfo.getCustRelation().add(custRelation2);
				
				CustRelationType custRelation3 = new CustRelationType();
				custRelation3.setRelationCode("JA2");
				custRelation3.setCustPermId(app3CifNo);
				loanAcctInfo.getCustRelation().add(custRelation3);

			}
			

	applicantInfo = new ApplicationInfoType();
			loanAcctBasic = new LoanAcctBasicType();
	
			referenceNo = (String)parametersMap.get(rsbundle.getString("REFERENCENO"));
			log.info("createLoanActAddRqJAxbObject:referenceNo is "+referenceNo);
			log.info("createLoanActAddRqJAxbObject:applicationId is "+applicationId);
			applicantInfo.setApplicationId(applicationId); //Get the logic of 12 characters unique number
			applicantInfo.setApplicationSeq(applicationId.substring(7));
			loanAcctBasic.setApplicationInfo(applicantInfo);
			
			loanAcctBasic.setProdId((String)parametersMap.get(rsbundle.getString("PRODUCTCODE")));
			loanAcctBasic.setBranchId((String)parametersMap.get(rsbundle.getString("BRANCHCODE")));
			log.info("createLoanActAddRqJAxbObject:retrieving source ");
			String source = (String)parametersMap.get(rsbundle.getString("SOURCE"));
			log.info("createLoanActAddRqJAxbObject:retrieved source "+source);
			/*Switch-Case would have been helpful but it need JRE1.7 and FileNet is exactly using JRE1.7 hence code is made 1.6*/
			if(source.equalsIgnoreCase("Branch Office"))
			{
				loanAcctBasic.setPrimOfficerCode("BRV");
				log.info("createLoanActAddRqJAxbObject:setting officeCode BRV");
			}
			else if(source.equalsIgnoreCase("Call Center"))
			{
				loanAcctBasic.setPrimOfficerCode("CLL");
				log.info("createLoanActAddRqJAxbObject:setting officeCode CLL");
			}
			else if(source.equalsIgnoreCase("Internet"))
			{
				loanAcctBasic.setPrimOfficerCode("INT");
				log.info("createLoanActAddRqJAxbObject:setting officeCode INT");
			}
			else if(source.equalsIgnoreCase("Kiosk"))
			{
				loanAcctBasic.setPrimOfficerCode("KOK");
				log.info("createLoanActAddRqJAxbObject:setting officeCode KOK");
			}
			else if(source.equalsIgnoreCase("Mobile"))
			{
				loanAcctBasic.setPrimOfficerCode("MOB");
				log.info("createLoanActAddRqJAxbObject:setting officeCode MOB");
			}
			else if(source.equalsIgnoreCase("Web"))
			{
				loanAcctBasic.setPrimOfficerCode("WEB");
				log.info("createLoanActAddRqJAxbObject:setting officeCode WEB");
			}
			
			
			loanAcctBasic.setNoteTypeCode(NoteTypeCodeType.TERM);//Always Term(3)
			
			currencyAmount = new  CurrencyAmount();
			proposedAmt =	(Double) parametersMap.get(rsbundle.getString("PROPOSEDAMOUNT"));
			
			
			log.info("createLoanActAddRqJAxbObject:proposedAmt is "+proposedAmt);
			if(null!=proposedAmt)
				currencyAmount.setAmt(getBigDecimalWithDecimalPoint(proposedAmt));
			currencyAmount.setCurCode(currencyCode);
			
			loanAcctBasic.setOpenAmt(currencyAmount);
			
			
			String intGuarantyCode = null; 
			Boolean fdAgainstLoan = false;
			intGuarantyCode =(String)parametersMap.get(rsbundle.getString("GUARANTEECODE"));
			log.info("createLoanActAddRqJAxbObject:intGuarantyCode is "+intGuarantyCode);
			System.out.println("createLoanActAddRqJAxbObject:intGuarantyCode is "+intGuarantyCode);
			if(null!=intGuarantyCode)
			{
				if(intGuarantyCode.equalsIgnoreCase((String)rsbundle.getString("RATECANNOTCHANGE")))
					loanAcctBasic.setIntGuarantyCode(IntGuarantyCodeType.RATE_NO_CHG);
				else if(intGuarantyCode.equalsIgnoreCase((String)rsbundle.getString("RATECHANGEATMATURITY")))
					loanAcctBasic.setIntGuarantyCode(IntGuarantyCodeType.RATE_MAY_CHG_AT_MAT);
				else if(intGuarantyCode.equalsIgnoreCase((String)rsbundle.getString("RATECHANGEANYTIME")))
					loanAcctBasic.setIntGuarantyCode(IntGuarantyCodeType.RATE_MAY_CHG_ANYTIME);
				else if(intGuarantyCode.equalsIgnoreCase((String)rsbundle.getString("RATECHANGEINDEX")))
					loanAcctBasic.setIntGuarantyCode(IntGuarantyCodeType.RATE_CHG_WITH_INDEX_CHG);
				else if(intGuarantyCode.equalsIgnoreCase((String)rsbundle.getString("RATECHANGEINDEXREVIEW")))
				{
					loanAcctBasic.setIntGuarantyCode(IntGuarantyCodeType.RATE_CHG_WITH_INDEX_REV);
					fdAgainstLoan=true;
				}
			}
			propsedInterestRate = 	(Double) parametersMap.get(rsbundle.getString("PROPOSEDINTERESTRATE"));
			log.info("createLoanActAddRqJAxbObject:propsedInterestRate is "+propsedInterestRate);
			if(null!=propsedInterestRate)
			{
				BigDecimal bigDecimal = new BigDecimal(propsedInterestRate);
				
				if(null!= bigDecimal && bigDecimal.scale() > 4)
				{
					bigDecimal = bigDecimal.setScale(4,BigDecimal.ROUND_HALF_UP);
				}
				else
					bigDecimal.setScale(4,BigDecimal.ROUND_UNNECESSARY);
				loanAcctBasic.setIntRate(bigDecimal);
			}
			firstPaymentLoanDt = new Date();
			java.util.Date firstPaymentDt  = null;
			firstPaymentDt = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
			log.info("firstPaymentDt:"+firstPaymentDt.getTime());
		
			firtPaymentCal = Calendar.getInstance();
			firtPaymentCal.setTime(firstPaymentDt);
			int firtPaymentCalMonthInt = firtPaymentCal.get((firtPaymentCal.MONTH))+1;
			firstPaymentLoanDt.setDay(Long.valueOf(firtPaymentCal.get(firtPaymentCal.DAY_OF_MONTH)));
			firstPaymentLoanDt.setMonth(Long.valueOf(firtPaymentCalMonthInt));
			firstPaymentLoanDt.setYear(Long.valueOf(firtPaymentCal.get(firtPaymentCal.YEAR)));
		
			log.info("firstPaymentLoanDt:"+firstPaymentLoanDt);
			
			loanAcctBasic.setFirstPmtDt(firstPaymentLoanDt);
			
			repaymentPeriodLong = Long.valueOf((Integer) parametersMap.get(rsbundle.getString("REPAYMENTPERIOD")));
			log.info("repaymentPeriodLong:"+repaymentPeriodLong);			
			termType = 	new TermType();
			termType.setCount(repaymentPeriodLong);
			
			java.util.Date currentMaturity = null; 
			currentMaturity = 	new java.util.Date();
			
			paymentMaturityCal = Calendar.getInstance();
			paymentMaturityCal.setTime(currentMaturity);
			paymentMaturityCal.set(Calendar.MONTH, (int) (paymentMaturityCal.get(Calendar.MONTH)+repaymentPeriodLong));
			currentMaturity = paymentMaturityCal.getTime();
			log.info("createLoanActAddRqJAxbObject: currentMaturityDate"+currentMaturity);
			
			termType.setTermUnits(TermUnitsType.MONTHS);	//get from EForm
			loanAcctBasic.setTerm(termType);
			
			loanAcctBasic.setBillCode(BillCodeType.TRANSFER_ORDER);  //???get Code from mapping table
			
			loanAcctBasic.setFDICCode((String) parametersMap.get(rsbundle.getString("FDICCODE")));
			
			loanAcctBasic.setFRBCode((String) parametersMap.get(rsbundle.getString("FRBCODE")));
			
			loanAcctBasic.setLTVTypeCode(LTVTypeCodeType.ORIGINAL);  //Always Hardcoded
	loanAcctInfo.setLoanAcctBasic(loanAcctBasic);
		
			loanAcctDetailType = new  LoanAcctDetailType();
			loanAcctDetailType.setCensusTractCode((String) parametersMap.get(rsbundle.getString("CENSUSCODE")));
			
			loanAcctDetailType.setMSACode((String) parametersMap.get(rsbundle.getString("MSACODE")));
			
			loanAcctDetailType.setLoanApprovalCode("Y");    //Always Hardcoded

			loanAcctDetailType.setRiskCode(String.valueOf(parametersMap.get(rsbundle.getString("RISKCODE"))));
			
			loanAcctDetailType.setAvailToDisbCalc("0");  //Always Hardcoded
	loanAcctInfo.setLoanAcctDetail(loanAcctDetailType);
			
			variableateInfoType = new  VariableRateInfoType();
			
			indexId =	(String) parametersMap.get(rsbundle.getString("INDEXID"));
			variableateInfoType.setIndexId(indexId);			 
			/*if(null!= indexId && indexId.equalsIgnoreCase("99"))
				variableateInfoType.setIndexVarPct(new BigDecimal(propsedInterestRate));   //If 99*/
			if (null!= indexId)
			{
				indexVariance =	(Double) parametersMap.get(rsbundle.getString("INDEXVARIANCE"));
				if(null!=indexVariance)
				{
					indexVarianceBigDecimal = new BigDecimal(indexVariance);
					//variableateInfoType.setIndexVarPct(indexVarianceBigDecimal);
					variableateInfoType.setIndexVarPct(getBigDecimalWithDecimalPoint(indexVariance));
				}
			}
			
			boolean floorCeilingFlag=false;
			floorCeilingStr = (String) parametersMap.get(rsbundle.getString("FLOORCEILINGFLAG"));
			if(null != floorCeilingStr && floorCeilingStr.equalsIgnoreCase("Y"))
			{
				floorCeilingFlag = true;
				variableateInfoType.setFloorCeilingFlag(floorCeilingFlag);
			}
			if(null != floorCeilingStr && floorCeilingFlag)
			{
				floorIntPct =	(Double) parametersMap.get(rsbundle.getString("FLOORINTPCT"));
				if(null!= floorIntPct)
				{
					floorIntPctBigDecimal =	new BigDecimal(floorIntPct);
					//variableateInfoType.setFloorIntRatePct(floorIntPctBigDecimal);
					variableateInfoType.setFloorIntRatePct(getBigDecimalWithDecimalPoint(floorIntPct));
				}
				ceilingIntPct = (Double) parametersMap.get(rsbundle.getString("CEILINGINTPCT"));
				if(null!=ceilingIntPct )
				{
					ceilingPctBigDecimal =	new BigDecimal(ceilingIntPct);
					//variableateInfoType.setCeilingIntRatePct(ceilingPctBigDecimal);
					variableateInfoType.setCeilingIntRatePct(getBigDecimalWithDecimalPoint(ceilingIntPct));
				}
			}
	
			log.info("fdAgainstLoan is "+fdAgainstLoan);
			if(null!=fdAgainstLoan && fdAgainstLoan)
			{
				reviewDt  =	new Date();
				reviewPer =	(String)parametersMap.get(rsbundle.getString("REVIEWPER"));
				reviewFreq =	String.valueOf(parametersMap.get(rsbundle.getString("RATEREVIEWFREQ")));
				reviewFreqDt = 	(java.util.Date)parametersMap.get(rsbundle.getString("RATEREVIEWDATE"));
				reviewFrqCal =	Calendar.getInstance();
				if(null!=reviewFreqDt)
				{
					reviewFrqCal.setTime(reviewFreqDt);
					reviewFrqDay =	String.valueOf(reviewFrqCal.get(Calendar.DATE));
					String reviewFreqFormat = null;
					log.info("reviewFreq is "+reviewFreq);
					reviewFreqFormat = reviewPer+"0";
					if(null!=reviewFreq && reviewFreq.length()==1)
						reviewFreq="0"+reviewFreq;
					variableateInfoType.setRateReviewFreq(reviewFreqFormat+reviewFreq+reviewFrqDay);
					int reviewMonthInt = reviewFrqCal.get(reviewFrqCal.MONTH)+1;
					reviewDt.setDay(Long.valueOf(reviewFrqCal.get(reviewFrqCal.DAY_OF_MONTH)));
					reviewDt.setMonth(Long.valueOf(reviewMonthInt));
					reviewDt.setYear(Long.valueOf(reviewFrqCal.get(reviewFrqCal.YEAR)));
					variableateInfoType.setRateReviewNextDt(reviewDt);
				}
			
				variableateInfoType.setRateReviewLeadDaysNum(BigInteger.valueOf(0)); //Always Hardcoded
				variableateInfoType.setRateReviewGraceDaysNum(BigInteger.valueOf(0)); //Always Hardcoded
			}
			
	loanAcctInfo.setVariableRateInfo(variableateInfoType);
	
			log.info("Checking for HomeLoan ");
			if(homeLoanCaseType.equalsIgnoreCase(caseTypeName))
			{
				log.info("Case Triggered is HomeLoan");
				Integer gracePeriodInt =(Integer) parametersMap.get(rsbundle.getString("GRACEPERIOD"));			
				if(null!=gracePeriodInt)
				{
					log.info("Grace Period Exists "+gracePeriodInt);
					pmdShedType1 =	new PmtSchedType();
					
					pmdShedType1.setPmtSchedUseNum(String.valueOf(gracePeriodInt));
					pmtScheduleNextDate =  new Date();
					java.util.Date nextPaymnetDt = null;
					nextPaymnetDt = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
					
					Calendar paymentFrqCal = null; 
					paymentFrqCal =		Calendar.getInstance();
					paymentFrqCal.setTime(nextPaymnetDt);
					monthFormat =	String.valueOf(paymentFrqCal.get(paymentFrqCal.DAY_OF_MONTH));
					log.info("monthFormat is "+monthFormat);
					if(monthFormat.length()==1)
						monthFormat="0"+monthFormat;
					paymentFrequencyFormatted =	"M001"+monthFormat;
					log.info("paymentFrequencyFormatted is "+paymentFrequencyFormatted);
					pmdShedType1.setPmtFreq(paymentFrequencyFormatted);
					int paymentFrqInt = paymentFrqCal.get(paymentFrqCal.MONTH)+1;
					pmtScheduleNextDate.setDay(Long.valueOf(paymentFrqCal.get(paymentFrqCal.DAY_OF_MONTH)));
					pmtScheduleNextDate.setMonth(Long.valueOf(paymentFrqInt));
					pmtScheduleNextDate.setYear(Long.valueOf(paymentFrqCal.get(paymentFrqCal.YEAR)));
					
					pmdShedType1.setPmtSchedNextPmtDt(pmtScheduleNextDate);
					
					pmdShedType1.setPmtSchedTypeCode(PmtSchedTypeCodeType.INTEREST);
					
					
					currAmountPmdShedType = 	new  CurrencyAmount();
					currAmountPmdShedType.setAmt(getBigDecimalWithDecimalPoint(0.00));
					currAmountPmdShedType.setCurCode(currencyCode);
					pmdShedType1.setPmtSchedAmt(currAmountPmdShedType);
					
					currAmountIntBearing =	new CurrencyAmount();
					currAmountIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00));		//Always Hardcoded
					currAmountIntBearing.setCurCode(currencyCode);				//Always Hardcoded
				pmdShedType1.setIntBearingEscrowAmt(currAmountIntBearing);
				
					currAmountNonIntBearing =	new CurrencyAmount();
					currAmountNonIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00)); //Always Hardcoded
					currAmountNonIntBearing.setCurCode(currencyCode); //Always Hardcoded
				pmdShedType1.setNonIntBearingEscrowAmt(currAmountNonIntBearing);
					
				loanAcctInfo.getPmtSched().add(pmdShedType1);
				}
				else
				{
					java.util.Date dt = null;//new java.util.Date();
					dt = (java.util.Date) parametersMap.get(rsbundle.getString("COMMUNICATORDATE"));
					log.info("Communicator Advanced Date is "+dt);
					Calendar calAddMonth = Calendar.getInstance();
					calAddMonth.setTime(dt);
					//calAddMonth.add(Calendar.MONTH,+1);
					
					java.util.Date nextPaymnetDt = null;
					nextPaymnetDt = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
					Calendar paymentFrqCal = null; 
					paymentFrqCal =		Calendar.getInstance();
					paymentFrqCal.setTime(nextPaymnetDt);
					log.info("Firstpayment is greater than 30 days and no Grace Period "+paymentFrqCal.after(calAddMonth));
					if(paymentFrqCal.after(calAddMonth))
					{
						pmdShedType =	new PmtSchedType();
						pmdShedType.setPmtSchedUseNum("1");
						
						pmtScheduleNextDate =  new Date();
						 nextPaymnetDt = null;
						nextPaymnetDt = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
						paymentFrqCal =		Calendar.getInstance();
						paymentFrqCal.setTime(nextPaymnetDt);
						monthFormat =	String.valueOf(paymentFrqCal.get(paymentFrqCal.DAY_OF_MONTH));
						log.info("monthFormat is "+monthFormat);
						if(monthFormat.length()==1)
							monthFormat="0"+monthFormat;
						paymentFrequencyFormatted =	"M001"+monthFormat;
						log.info("paymentFrequencyFormatted is "+paymentFrequencyFormatted);
						pmdShedType.setPmtFreq(paymentFrequencyFormatted);
						
						
						//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						//java.util.Date dt = sdf.parse(calDateStr);
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(nextPaymnetDt);
						cal.add(Calendar.MONTH,-1);
						
						Long day = Long.valueOf(cal.get(cal.DAY_OF_MONTH));
						Long month = Long.valueOf(cal.get(cal.MONTH)+1);					
						Long year = Long.valueOf(cal.get(cal.YEAR));
						pmtScheduleNextDate.setDay(day);
						pmtScheduleNextDate.setMonth(month);
						pmtScheduleNextDate.setYear(year);
						pmdShedType.setPmtSchedNextPmtDt(pmtScheduleNextDate);
						
						pmdShedType.setPmtSchedTypeCode(PmtSchedTypeCodeType.INTEREST);
						
						
						currAmountPmdShedType = 	new  CurrencyAmount();
						currAmountPmdShedType.setAmt(getBigDecimalWithDecimalPoint(0.00));
						currAmountPmdShedType.setCurCode(currencyCode);
						pmdShedType.setPmtSchedAmt(currAmountPmdShedType);
						
						currAmountIntBearing =	new CurrencyAmount();
						currAmountIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00));		//Always Hardcoded
						currAmountIntBearing.setCurCode(currencyCode);				//Always Hardcoded
					pmdShedType.setIntBearingEscrowAmt(currAmountIntBearing);
					
						currAmountNonIntBearing =	new CurrencyAmount();
						currAmountNonIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00)); //Always Hardcoded
						currAmountNonIntBearing.setCurCode(currencyCode); //Always Hardcoded
					pmdShedType.setNonIntBearingEscrowAmt(currAmountNonIntBearing);
					
					loanAcctInfo.getPmtSched().add(pmdShedType);
					
					}
					
				}
			
				pmdShedType2 =	new PmtSchedType();
				pmtScheduleNextDate =  new Date();
				if(null!=gracePeriodInt)
				{
					pmdShedType2.setPmtSchedUseNum(String.valueOf((repaymentPeriodLong-1)-Long.valueOf(gracePeriodInt)));
				}
				else
				{
					pmdShedType2.setPmtSchedUseNum(String.valueOf(repaymentPeriodLong-1));
				}
				
				Calendar cal2 = Calendar.getInstance();
				java.util.Date nextPaymnetDt = null;
				nextPaymnetDt = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
				cal2.setTime(nextPaymnetDt);
				if(null!=gracePeriodInt)
				{
					log.info("Grace Period Exists "+gracePeriodInt);
					cal2.add(Calendar.MONTH,+gracePeriodInt);
					log.info("First payment date "+nextPaymnetDt+" is updated with grace period, year is "+cal2.get(cal2.YEAR)+" month is "+(cal2.get(cal2.MONTH)+1));
				}
				
				monthFormat =	String.valueOf(cal2.get(cal2.DAY_OF_MONTH));
				log.info("monthFormat is "+monthFormat);
				if(monthFormat.length()==1)
					monthFormat="0"+monthFormat;
				paymentFrequencyFormatted =	"M001"+monthFormat;
				log.info("paymentFrequencyFormatted is "+paymentFrequencyFormatted);
				pmdShedType2.setPmtFreq(paymentFrequencyFormatted);
				
				Long day = Long.valueOf(cal2.get(cal2.DAY_OF_MONTH));
				Long month = Long.valueOf(cal2.get(cal2.MONTH)+1);					
				Long year = Long.valueOf(cal2.get(cal2.YEAR));
				pmtScheduleNextDate.setDay(day);
				pmtScheduleNextDate.setMonth(month);
				pmtScheduleNextDate.setYear(year);
				pmdShedType2.setPmtSchedNextPmtDt(pmtScheduleNextDate);
				
				repaymentmethod =	(String) parametersMap.get(rsbundle.getString("REPAYMENTMETHOD"));
				currAmountPmdShedType = 	new  CurrencyAmount();
				log.info("createLoanActAddRqJAxbObject:proposedAmt is "+proposedAmt);
				if(null!= repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODEMI")))
				{
						pmdShedType2.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_AND_INT);
						proposedInstallment = (Double) parametersMap.get(rsbundle.getString("PROPOSEDINSTALLMENT"));
						if(null!=proposedInstallment)
							currAmountPmdShedType.setAmt(getBigDecimalWithDecimalPoint(proposedInstallment));
						currAmountPmdShedType.setCurCode(currencyCode);
						pmdShedType2.setPmtSchedAmt(currAmountPmdShedType);
						
				}
				if(null != repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODREDUCING")))
				{
					pmdShedType2.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_PLUS_INT);
					if(null!=proposedAmt)
					{
						if(null!=gracePeriodInt)
							proposedReducingAmt = proposedAmt/((repaymentPeriodLong)-Long.valueOf(gracePeriodInt));
						else
							proposedReducingAmt = proposedAmt/(repaymentPeriodLong);
						log.info("createLoanActAddRqJAxbObject:proposedReducingAmt is "+proposedReducingAmt);
						currAmountPmdShedType.setAmt(getBigDecimalWithDecimalPoint(proposedReducingAmt));
					}
					currAmountPmdShedType.setCurCode(currencyCode);
					pmdShedType2.setPmtSchedAmt(currAmountPmdShedType);
				}
				
				currAmountIntBearing =	new CurrencyAmount();
				currAmountIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00));		//Always Hardcoded
				currAmountIntBearing.setCurCode(currencyCode);				//Always Hardcoded
			pmdShedType2.setIntBearingEscrowAmt(currAmountIntBearing);
			
				currAmountNonIntBearing =	new CurrencyAmount();
				currAmountNonIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00)); //Always Hardcoded
				currAmountNonIntBearing.setCurCode(currencyCode); //Always Hardcoded
			pmdShedType2.setNonIntBearingEscrowAmt(currAmountNonIntBearing);
				
			loanAcctInfo.getPmtSched().add(pmdShedType2);
			
			pmdShedType3 =	new PmtSchedType();
			pmdShedType3.setPmtSchedUseNum("1");
		
			java.util.Date current = null;
			//current = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
			try
			{
				log.info("Retrieving Communicator advanced date ");
				current = (java.util.Date) parametersMap.get(rsbundle.getString("COMMUNICATORDATE"));
			}
			catch(Exception e)
			{
				log.info("Property Not Found Exception occured");
				log.error("Current Communicator Date Property", e.fillInStackTrace());
			}
			
			
			//java.util.Date current = new java.util.Date();
			log.info("Communicator advanced date is "+current);
			if(null==current)
			{
				DateCalcInqServiceImpl dateService = new DateCalcInqServiceImpl();
				CalcDateResponse dtResponse = dateService.getDateCalcInq();
				String communicatorDate = dtResponse.getDate();
				log.info("Communicator advanced date if Null is "+communicatorDate);
				SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
				current = sf.parse(communicatorDate);
				log.info("current advanced date if Null is "+current);
			}
			
			paymentFrqMatCal =	Calendar.getInstance();
			paymentFrqMatCal.setTime(current);
			//paymentFrqMatCal.add(Calendar.MONTH,-1);
			current = paymentFrqMatCal.getTime();
			log.info("Communicator current date without change is"+current);
			monthMatFormat =	String.valueOf(paymentFrqMatCal.get(paymentFrqMatCal.DAY_OF_MONTH));
			log.info("monthMatFormat is "+monthMatFormat);
			if(monthMatFormat.length()==1)
				monthMatFormat="0"+monthMatFormat;
			paymentFrequencyMatFormatted =	"M001"+monthMatFormat;
			log.info("paymentFrequencyMatFormatted is "+paymentFrequencyMatFormatted);
			pmdShedType3.setPmtFreq(paymentFrequencyMatFormatted);
			int paymentFrqMatCal1Int = (int) (paymentFrqMatCal.get(Calendar.MONTH)+(repaymentPeriodLong - 1));
			paymentFrqMatCal.set(Calendar.MONTH, paymentFrqMatCal1Int);
			current = paymentFrqMatCal.getTime();
			log.info("After adding number of terms current date is"+current);
			
			Date pmtScheduleNextDate1 = null; 
			pmtScheduleNextDate1 =	new Date();
			
			pmtScheduleNextDate1.setDay(Long.valueOf(paymentFrqMatCal.get(paymentFrqMatCal.DAY_OF_MONTH)));
			int paymentFrqInt1 = paymentFrqMatCal.get(paymentFrqMatCal.MONTH)+1;
			pmtScheduleNextDate1.setMonth(Long.valueOf(paymentFrqInt1)); //Month starts with 0, increment by 1
			pmtScheduleNextDate1.setYear(Long.valueOf(paymentFrqMatCal.get(paymentFrqMatCal.YEAR)));
			pmdShedType3.setPmtSchedNextPmtDt(pmtScheduleNextDate1);
			
			
			if(null!= repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODEMI")))
				pmdShedType3.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_AND_INT);
			if(null != repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODREDUCING")))
			pmdShedType3.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_PLUS_INT);
		
				currAmountPmdShedType1=	new  CurrencyAmount();
				BigDecimal pmtShedType1 = null; 
				pmtShedType1 =	new BigDecimal("999999999999999.99");
				currAmountPmdShedType1.setAmt(pmtShedType1);//Always Hardcoded
				currAmountPmdShedType1.setCurCode(currencyCode);
				pmdShedType3.setPmtSchedAmt(currAmountPmdShedType1);	
				currAmountIntBearing1 = new CurrencyAmount();
				currAmountIntBearing1.setAmt(getBigDecimalWithDecimalPoint(0.00));
				currAmountIntBearing1.setCurCode(currencyCode);
				pmdShedType3.setIntBearingEscrowAmt(currAmountIntBearing1);
				currAmountNonIntBearing1 = 	new CurrencyAmount();
				currAmountNonIntBearing1.setAmt(getBigDecimalWithDecimalPoint(0.00));
				currAmountNonIntBearing1.setCurCode(currencyCode);
				pmdShedType3.setNonIntBearingEscrowAmt(currAmountNonIntBearing1);
				
			loanAcctInfo.getPmtSched().add(pmdShedType3);
				log.info("All Payment schedule been added for HomeLoan");
			}
			else
			{
			pmdShedType =	new PmtSchedType();
			pmdShedType.setPmtSchedUseNum(String.valueOf(repaymentPeriodLong-1));
			
			pmtScheduleNextDate =  new Date();
			java.util.Date nextPaymnetDt = null;
			nextPaymnetDt = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
			
			Calendar paymentFrqCal = null; 
			paymentFrqCal =		Calendar.getInstance();
			paymentFrqCal.setTime(nextPaymnetDt);
			monthFormat =	String.valueOf(paymentFrqCal.get(paymentFrqCal.DAY_OF_MONTH));
			log.info("monthFormat is "+monthFormat);
			if(monthFormat.length()==1)
				monthFormat="0"+monthFormat;
			paymentFrequencyFormatted =	"M001"+monthFormat;
			log.info("paymentFrequencyFormatted is "+paymentFrequencyFormatted);
			pmdShedType.setPmtFreq(paymentFrequencyFormatted);
			int paymentFrqInt = paymentFrqCal.get(paymentFrqCal.MONTH)+1;
			pmtScheduleNextDate.setDay(Long.valueOf(paymentFrqCal.get(paymentFrqCal.DAY_OF_MONTH)));
			pmtScheduleNextDate.setMonth(Long.valueOf(paymentFrqInt));
			pmtScheduleNextDate.setYear(Long.valueOf(paymentFrqCal.get(paymentFrqCal.YEAR)));
			
			
			
	
			
			pmdShedType.setPmtSchedNextPmtDt(pmtScheduleNextDate);
			
			repaymentmethod =	(String) parametersMap.get(rsbundle.getString("REPAYMENTMETHOD"));
			currAmountPmdShedType = 	new  CurrencyAmount();
			log.info("createLoanActAddRqJAxbObject:proposedAmt is "+proposedAmt);
			if(null!= repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODEMI")))
			{
					pmdShedType.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_AND_INT);
					proposedInstallment = (Double) parametersMap.get(rsbundle.getString("PROPOSEDINSTALLMENT"));
					if(null!=proposedInstallment)
						currAmountPmdShedType.setAmt(getBigDecimalWithDecimalPoint(proposedInstallment));
					currAmountPmdShedType.setCurCode(currencyCode);
					pmdShedType.setPmtSchedAmt(currAmountPmdShedType);
					
			}
			if(null != repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODREDUCING")))
			{
				pmdShedType.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_PLUS_INT);
				if(null!=proposedAmt)
				{
					proposedReducingAmt = proposedAmt/(repaymentPeriodLong);
					log.info("createLoanActAddRqJAxbObject:proposedReducingAmt is "+proposedReducingAmt);
					currAmountPmdShedType.setAmt(getBigDecimalWithDecimalPoint(proposedReducingAmt));
				}
				currAmountPmdShedType.setCurCode(currencyCode);
				pmdShedType.setPmtSchedAmt(currAmountPmdShedType);
			}
			
				
				
				currAmountIntBearing =	new CurrencyAmount();
				currAmountIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00));		//Always Hardcoded
				currAmountIntBearing.setCurCode(currencyCode);				//Always Hardcoded
			pmdShedType.setIntBearingEscrowAmt(currAmountIntBearing);
			
				currAmountNonIntBearing =	new CurrencyAmount();
				currAmountNonIntBearing.setAmt(getBigDecimalWithDecimalPoint(0.00)); //Always Hardcoded
				currAmountNonIntBearing.setCurCode(currencyCode); //Always Hardcoded
			pmdShedType.setNonIntBearingEscrowAmt(currAmountNonIntBearing);
	
			loanAcctInfo.getPmtSched().add(pmdShedType);
			
			pmdShedType1 =	new PmtSchedType();
			pmdShedType1.setPmtSchedUseNum("1");

			java.util.Date current = null;
			//current = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
			try
			{
				log.info("Retrieving Communicator advanced date ");
				current = (java.util.Date) parametersMap.get(rsbundle.getString("COMMUNICATORDATE"));
			}
			catch(Exception e)
			{
				log.info("Property Not Found Exception occured");
				log.error("Current Communicator Date Property", e.fillInStackTrace());
			}
			/*if(currentProcessingKey.equalsIgnoreCase("CURRENTSYSTEMDATE"))	
			{
				current = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
				//current =  new java.util.Date();
			}
			else
			{
				current = otherProcessingDt;
			}*/
			
			//java.util.Date current = new java.util.Date();
			log.info("Communicator advanced date is "+current);
			if(null==current)
			{
				DateCalcInqServiceImpl dateService = new DateCalcInqServiceImpl();
				CalcDateResponse dtResponse = dateService.getDateCalcInq();
				String communicatorDate = dtResponse.getDate();
				log.info("Communicator advanced date if Null is "+communicatorDate);
				SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
				current = sf.parse(communicatorDate);
				log.info("current advanced date if Null is "+current);
			}
			
			paymentFrqMatCal =	Calendar.getInstance();
			paymentFrqMatCal.setTime(current);
			paymentFrqMatCal.add(Calendar.MONTH,-1);
			current = paymentFrqMatCal.getTime();
			log.info("Communicator current date  is"+current);
			monthMatFormat =	String.valueOf(paymentFrqMatCal.get(paymentFrqMatCal.DAY_OF_MONTH));
			log.info("monthMatFormat is "+monthMatFormat);
			if(monthMatFormat.length()==1)
				monthMatFormat="0"+monthMatFormat;
			paymentFrequencyMatFormatted =	"M001"+monthMatFormat;
			log.info("paymentFrequencyMatFormatted is "+paymentFrequencyMatFormatted);
			pmdShedType1.setPmtFreq(paymentFrequencyMatFormatted);
			int paymentFrqMatCal1Int = (int) (paymentFrqMatCal.get(Calendar.MONTH)+(repaymentPeriodLong));
			paymentFrqMatCal.add(Calendar.MONTH, repaymentPeriodLong.intValue());
			java.util.Date paymentFrqDate = paymentFrqMatCal.getTime();
			log.info("After adding number of terms current date is"+paymentFrqDate);
			
			//Updating code to handle Loan Maturity Date
			Calendar currentCalDate = Calendar.getInstance();
			currentCalDate.setTime(current);
			int communicatorMaturityday = currentCalDate.get(Calendar.DAY_OF_MONTH);
			log.info("communicatorMaturityday obtained is "+communicatorMaturityday);
			int advancedDay = paymentFrqMatCal.getActualMaximum(paymentFrqMatCal.DAY_OF_MONTH);
			log.info("advancedDay obtained is "+advancedDay);
			
			Date pmtScheduleNextDate1 = null; 
			pmtScheduleNextDate1 =	new Date();
			
			if(communicatorMaturityday>=advancedDay)			
				pmtScheduleNextDate1.setDay(Long.valueOf(advancedDay));
			else
				pmtScheduleNextDate1.setDay(Long.valueOf(communicatorMaturityday));
			int paymentFrqInt1 = paymentFrqMatCal.get(paymentFrqMatCal.MONTH)+1;
			pmtScheduleNextDate1.setMonth(Long.valueOf(paymentFrqInt1)); //Month starts with 0, increment by 1
			pmtScheduleNextDate1.setYear(Long.valueOf(paymentFrqMatCal.get(paymentFrqMatCal.YEAR)));
			pmdShedType1.setPmtSchedNextPmtDt(pmtScheduleNextDate1);
			
			
			if(null!= repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODEMI")))
				pmdShedType1.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_AND_INT);
			if(null != repaymentmethod && repaymentmethod.equalsIgnoreCase(rsbundle.getString("PAYMENTMETHODREDUCING")))
				pmdShedType1.setPmtSchedTypeCode(PmtSchedTypeCodeType.PRINCIPAL_PLUS_INT);
		
				currAmountPmdShedType1=	new  CurrencyAmount();
				BigDecimal pmtShedType1 = null; 
				pmtShedType1 =	new BigDecimal("999999999999999.99");
				currAmountPmdShedType1.setAmt(pmtShedType1);//Always Hardcoded
				currAmountPmdShedType1.setCurCode(currencyCode);
				pmdShedType1.setPmtSchedAmt(currAmountPmdShedType1);	
				currAmountIntBearing1 = new CurrencyAmount();
				currAmountIntBearing1.setAmt(getBigDecimalWithDecimalPoint(0.00));
				currAmountIntBearing1.setCurCode(currencyCode);
				pmdShedType1.setIntBearingEscrowAmt(currAmountIntBearing1);
				currAmountNonIntBearing1 = 	new CurrencyAmount();
				currAmountNonIntBearing1.setAmt(getBigDecimalWithDecimalPoint(0.00));
				currAmountNonIntBearing1.setCurCode(currencyCode);
				pmdShedType1.setNonIntBearingEscrowAmt(currAmountNonIntBearing1);

	loanAcctInfo.getPmtSched().add(pmdShedType1);
			}
			
			log.info("All payment Schedules been added");
			collectFromAccType = new CollectFromAcctType();
			collectFromAccType.setAnchorProfile("187");	 //Always Hardcoded
			
			depAcctIdType = new DepAcctIdFromType();
			accountType =	(String)parametersMap.get(rsbundle.getString("ACCOUNTTYPE"));
			depAcctIdType.setAcctType(accountType);
			
			if(null!= accountType && accountType.equalsIgnoreCase(rsbundle.getString("ACCOUNTSAVINGTYPE")))
				collectFromAccType.setTargetProfile("115");
			else 
				collectFromAccType.setTargetProfile("112");
				
				depAcctIdType.setAcctId((String)parametersMap.get(rsbundle.getString("ACCOUNTNO")));
				
					bankInfo =	new BankInfoType();
					bankInfo.setBankId("1");  //Always Hardcoded
					depAcctIdType.setBankInfo(bankInfo);	
					depAcctIdType.setBankInfo(bankInfo);
			collectFromAccType.setDepAcctIdFrom(depAcctIdType);
			collectFromAccType.setReferenceId((String)parametersMap.get(rsbundle.getString("REFERENCENO")));
			collectFromAccType.setDescription("Ln Recovery"); 	 //Always Hardcoded
				currencyAmountCollectFrom = new CurrencyAmount();
				//if(null!=proposedAmt)
					//currencyAmountCollectFrom.setAmt(getBigDecimalWithDecimalPoint(proposedAmt));	
				proposedInstallment = (Double) parametersMap.get(rsbundle.getString("PROPOSEDINSTALLMENT"));
				if(null!=proposedInstallment)
					currencyAmountCollectFrom.setAmt(getBigDecimalWithDecimalPoint(proposedInstallment));	
				currencyAmountCollectFrom.setCurCode(currencyCode);
			collectFromAccType.setCurAmt(currencyAmountCollectFrom);
			
			java.util.Date nextPaymnetDt = null;
			nextPaymnetDt = (java.util.Date) parametersMap.get(rsbundle.getString("FIRSTPMTDATE"));
			collectFromDate =	new Date();
			collectStartCal = 	Calendar.getInstance();
			collectStartCal.setTime(nextPaymnetDt);
			
			collectFromDate.setDay(Long.valueOf(collectStartCal.get(collectStartCal.DAY_OF_MONTH)));
			int collectFromDateMonthInt = collectStartCal.get(collectStartCal.MONTH)+1;
			collectFromDate.setMonth(Long.valueOf(collectFromDateMonthInt));
			collectFromDate.setYear(Long.valueOf(collectStartCal.get(collectStartCal.YEAR)));
			
			collectFromDay =	String.valueOf(collectStartCal.get(collectStartCal.DAY_OF_MONTH));
			log.info("collectFromDay is "+collectFromDay);
			if(collectFromDay.length()==1)
				collectFromDay="0"+collectFromDay;
			paymentFrequencyFormatted =	"M001"+collectFromDay;
			log.info("collectFromFreqFormatted is "+paymentFrequencyFormatted);
			collectFromAccType.setFreq(paymentFrequencyFormatted);
		
			collectFromAccType.setStartDt(collectFromDate);
	loanAcctInfo.setCollectFromAcct(collectFromAccType);

			String collateralId = (String)parametersMap.get(rsbundle.getString("COLLATERALID"));
			Double  collateralAmount = (Double) parametersMap.get(rsbundle.getString("COLLATERALAMOUNT")); 
			Double minAmount; 
			if(null!=collateralId)
			{
				CollateralInfoType collateralInfo = new CollateralInfoType();
				CurrencyAmount pledgeAmt = new CurrencyAmount();
				collateralInfo.setCollateralId(collateralId);
				
				CollateralPledgeType collateralPledge = new CollateralPledgeType();
				collateralPledge.setPledgeRule("FaceAmount"); //Always HardCoded
				if(null!=collateralAmount)
				{
					minAmount = Math.min(collateralAmount, proposedAmt);
					pledgeAmt.setAmt(getBigDecimalWithDecimalPoint(minAmount));
					pledgeAmt.setCurCode(currencyCode);
					collateralPledge.setMaxPledgeAmt(pledgeAmt);
					collateralInfo.setCollateralPledge(collateralPledge);
				}
	loanAcctInfo.getCollateralInfo().add(collateralInfo);	
			}
	
			String collateralCode = (String)parametersMap.get(rsbundle.getString("COLLATERALCODE"));
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
			if(guaranteeLoanFlag)
			{
				 //Always HardCoded
				String guarantorCIF1= (String)parametersMap.get(rsbundle.getString("GUARANTERCIF1"));
				String guarantorCIF2= (String)parametersMap.get(rsbundle.getString("GUARANTERCIF2"));
				if(null!=guarantorCIF1 && !("".equalsIgnoreCase(guarantorCIF1)))
				{
					IndirectLiabilityType indirectLiability1 = new IndirectLiabilityType();
					indirectLiability1.setLiabilityType(LiabilityTypeType.GUARANTOR);
					indirectLiability1.setCustPermId(guarantorCIF1);
					indirectLiability1.setTaxLiabilityPctAmt(new BigDecimal(0)); //Always HardCoded
					indirectLiability1.setMaxLiability(currencyAmount);
					loanAcctInfo.getIndirectLiability().add(indirectLiability1);
				}
				if(null!=guarantorCIF2 && !("".equalsIgnoreCase(guarantorCIF2)))
				{
					IndirectLiabilityType indirectLiability2 = new IndirectLiabilityType();
					indirectLiability2.setLiabilityType(LiabilityTypeType.GUARANTOR);
					indirectLiability2.setCustPermId(guarantorCIF2);
					indirectLiability2.setTaxLiabilityPctAmt(new BigDecimal(0)); //Always HardCoded
					indirectLiability2.setMaxLiability(currencyAmount);
					loanAcctInfo.getIndirectLiability().add(indirectLiability2);
				}
				
			}
	
loanAcctAddRq.setLoanAcctInfo(loanAcctInfo);


inputXML = generateInputXML(loanAcctAddRq);
constantXML = getConstantXML();

String messageRquid = null;
messageRquid = GenerateUUID.getUUID();
String cifSvcRq = "<LoanSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</LoanSvcRq></IFX>";
inputRq = constantXML+cifSvcRq;
log.info("createLoanActAddRqJAxbObject:inputRq is "+inputRq);
System.out.println(inputRq);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			log.error("createLoanActAddRqJAxbObject: ParseException Occured "+e.fillInStackTrace());
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createLoanActAddRqJAxbObject: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			referenceNo=null;applicationId=null;floorCeilingStr=null;reviewPer=null;reviewFreq=null;reviewFrqDay=null;indexId = null;
			monthFormat=null;paymentFrequencyFormatted=null;repaymentmethod=null;collectFromDay=null;accountType=null;
			proposedAmt =null;propsedInterestRate =null;floorIntPct=null;ceilingIntPct=null;indexVarianceBigDecimal=null;
			//intGuarantyCode,firtPaymentCalMonthInt,paymentFrqInt,currentMaturity
			floorIntPctBigDecimal=null;ceilingPctBigDecimal=null;indexVariance=null;
			firstPaymentLoanDt = null;reviewDt=null;pmtScheduleNextDate=null;collectFromDate=null;
			reviewFreqDt = null;
			firtPaymentCal =null;paymentMaturityCal=null;reviewFrqCal=null;paymentFrqMatCal=null;collectStartCal=null;
			applicantInfo = null;
			repaymentPeriodLong =null;
			termType = null;
			loanAcctBasic = null;
			loanAcctDetailType = null;
			variableateInfoType=null;
			pmdShedType =null;pmdShedType1 = null;
			currencyAmount= null;currAmountPmdShedType = null; currAmountIntBearing = null; currAmountPmdShedType1 = null;
			currAmountNonIntBearing = null;currAmountIntBearing1 = null;currAmountNonIntBearing1 = null;currencyAmountCollectFrom =null;
			depAcctIdType=null;
			collectFromAccType=null;
			bankInfo = null;
			loanAcctInfo = null;
			loanAcctAddRq = null;
			inputXML = null;constantXML=null;
		}
		return inputRq;
	}

	
	public LoanAcctAddResponse invokeCommunicator(String inputXML) throws Exception  //For Scheduler Application invokes this method
	{
		String loanActId = null;
		LoanAcctAddResponse loanAcctAddResponse = null;
		try
		{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
		//String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl);
		System.out.println("outputXML is "+outputXML);
		log.info("invokeCommunicator: Invoked Communicator");
		String startIndexStr = rsbundle.getString("LoanAcctAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("LoanAcctAddServiceImpl.truncateEndStr");
		String startStatusCode = rsbundle.getString("IFXStatusCode");
		String endStatusCode =  rsbundle.getString("IFXEndStatusCode");
		String startStatusDesc = rsbundle.getString("IFXStatusDescription");
		String endStatusDesc = rsbundle.getString("IFXEndStatusDescription");
		String startErrorCode = rsbundle.getString("IFXErrorNum");
		String endErrorCode = rsbundle.getString("IFXEndErrorNum");
		String startErrorDesc= rsbundle.getString("IFXErrorDescription");
		String endErrorDesc = rsbundle.getString("IFXEndErrorDescription");
		Long statusCode = null;
		//String statusCode = null;
		String	statusDescription=null,errorCode=null,errorDesc=null;
		HashMap statusMap = null;
		StatusBean statusBean = null;
		log.info("invokeCommunicator: Checking for Error Response");
		//LoanAcctAddRs loanAcctAddRs1= (LoanAcctAddRs) generateOutputObject(outputXML,LoanAcctAddRs.class);
		//System.out.println("loanAcctAddRs1 is "+loanAcctAddRs1.getStatus().getStatusCode());
		statusBean = checkOutputErrorResponse(outputXML,startStatusCode,endStatusCode,startStatusDesc,endStatusDesc,startErrorCode,endErrorCode,startErrorDesc,endErrorDesc);
		//if(null!=statusMap)
		if(null!=statusBean)
		{
			/*Set sttusKeySet = statusMap.keySet();
			Iterator iter = sttusKeySet.iterator();
			while(iter.hasNext())
			{
				statusCode = Long.valueOf((String)iter.next());
				log.info("statusCode is "+statusCode);
				statusDescription =  (String) statusMap.get(String.valueOf(statusCode));
				log.info("statusDescription is "+statusDescription);
			}*/
			statusCode = Long.valueOf(statusBean.getStatusCode());
			statusDescription = statusBean.getStatusDesc();
			errorCode = statusBean.getErrorCode();
			errorDesc = statusBean.getErrorDesc();
			if(null!=statusCode && statusCode!=0)
			{
				int xmlIndex = outputXML.indexOf(startIndexStr, 0);
				if(xmlIndex!=-1)
				{
					String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
					LoanAcctAddRs loanAcctAddRs= (LoanAcctAddRs) generateOutputObject(removedIFX,LoanAcctAddRs.class);
					loanAcctAddResponse= new LoanAcctAddResponse();
					System.out.println("loanAcctAddRs is "+loanAcctAddRs);
					if(null!=loanAcctAddRs)
					{
						String errorNum = loanAcctAddRs.getStatus().getError().get(0).getErrNum();
						log.info("invokeCommunicator: Error Code is "+errorNum);
						System.out.println("invokeCommunicator: Error Code is "+errorNum);
						loanAcctAddResponse.setStatusCode(String.valueOf(statusCode));
						loanAcctAddResponse.setStatusDesc(loanAcctAddRs.getStatus().getStatusDesc());
						loanAcctAddResponse.setErrorCode(loanAcctAddRs.getStatus().getError().get(0).getErrNum());
						log.info("invokeCommunicator: Error Description is "+loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
						System.out.println("invokeCommunicator: Error Description is "+loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
						loanAcctAddResponse.setErrorDescription(loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
					}
				}
				else
				{
					
					loanAcctAddResponse = new LoanAcctAddResponse();
					log.info("invokeCommunicator: Status Code is "+statusCode);
					loanAcctAddResponse.setStatusCode(String.valueOf(statusCode));
					log.info("invokeCommunicator: Status Description is "+statusDescription);
					loanAcctAddResponse.setStatusDesc(statusDescription);
					log.info("invokeCommunicator: Error Code is "+errorCode);
					loanAcctAddResponse.setErrorCode(errorCode);
					log.info("invokeCommunicator: Error Description is "+errorDesc);
					loanAcctAddResponse.setErrorDescription(errorDesc);
				}
			}
			else //if(null!=statusCode && statusCode==0)
			{
				String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
				LoanAcctAddRs loanAcctAddRs= (LoanAcctAddRs) generateOutputObject(removedIFX,LoanAcctAddRs.class);
				if(null!=loanAcctAddRs)
				{
					StatusType statusType =  loanAcctAddRs.getStatus();
					statusCode = statusType.getStatusCode();
					//if(null!=statusCode && statusCode==0)
					{
						loanAcctAddResponse= new LoanAcctAddResponse();
						loanActId = loanAcctAddRs.getLoanAcctId().getAcctId();
						log.info("loanActId is "+loanActId);
						loanAcctAddResponse.setLoanActId(loanActId);
						loanAcctAddResponse.setStatusCode(String.valueOf(statusCode));
					}
					/*else
					{
						loanAcctAddResponse= new LoanAcctAddResponse();
						
						String errorNum = loanAcctAddRs.getStatus().getError().get(0).getErrNum();
						log.info("invokeCommunicator: Error Code is "+errorNum);
						loanAcctAddResponse.setStatusCode(String.valueOf(statusCode));
						loanAcctAddResponse.setStatusDesc(loanAcctAddRs.getStatus().getStatusDesc());
						loanAcctAddResponse.setErrorCode(loanAcctAddRs.getStatus().getError().get(0).getErrNum());
						log.info("invokeCommunicator: Error Description is "+loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
						loanAcctAddResponse.setErrorDescription(loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
					}*/
				}				
			}
			/*else
			{
				//To handle  Duplicate RQUID
				if(null!=statusCode && statusCode==Long.valueOf(duplicateRqIdCode))
				{
					loanAcctAddResponse = new LoanAcctAddResponse();
					log.info("invokeCommunicator: Error Code is "+statusCode);
					loanAcctAddResponse.setStatusCode(String.valueOf(statusCode));
					log.info("invokeCommunicator: Error Code is "+statusDescription);
					loanAcctAddResponse.setStatusDesc(statusDescription);
				}
				else
				{
					String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
					LoanAcctAddRs loanAcctAddRs= (LoanAcctAddRs) generateOutputObject(removedIFX,LoanAcctAddRs.class);
					loanAcctAddResponse= new LoanAcctAddResponse();
					System.out.println("loanAcctAddRs is "+loanAcctAddRs);
					if(null!=loanAcctAddRs)
					{
						String errorNum = loanAcctAddRs.getStatus().getError().get(0).getErrNum();
						log.info("invokeCommunicator: Error Code is "+errorNum);
						System.out.println("invokeCommunicator: Error Code is "+errorNum);
						loanAcctAddResponse.setStatusCode(String.valueOf(statusCode));
						loanAcctAddResponse.setStatusDesc(loanAcctAddRs.getStatus().getStatusDesc());
						loanAcctAddResponse.setErrorCode(loanAcctAddRs.getStatus().getError().get(0).getErrNum());
						log.info("invokeCommunicator: Error Description is "+loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
						System.out.println("invokeCommunicator: Error Description is "+loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
						loanAcctAddResponse.setErrorDescription(loanAcctAddRs.getStatus().getError().get(0).getErrDesc());
					}
				}
			}*/
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
			LoanAcctAddRs loanAcctAddRs= (LoanAcctAddRs) generateOutputObject(removedIFX,LoanAcctAddRs.class);
			if(null != loanAcctAddRs)
			{
				StatusType statusType = loanAcctAddRs.getStatus();
				loanAcctAddResponse = new LoanAcctAddResponse();
				loanActId = loanAcctAddRs.getLoanAcctId().getAcctId();
				loanAcctAddResponse.setLoanActId(loanActId);
			}
		}
		else
		{
			loanAcctAddResponse = new LoanAcctAddResponse();
			log.info("invokeCommunicator: Error Code is "+statusCode);
			loanAcctAddResponse.setErrorCode(String.valueOf(statusCode));
			log.info("invokeCommunicator: Error Code is "+statusDescription);
			loanAcctAddResponse.setErrorDescription(statusDescription);
		}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("invokeCommunicator: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return loanAcctAddResponse;
	}
	
	
	public String getApplicationId(String referenceNo)
	{
		System.out.println("referenceNo is "+referenceNo);
		String branchCode = referenceNo.substring(0,4);
		String yearSeq = referenceNo.substring(11);
		String applicationId = branchCode+yearSeq;
		System.out.println("applicationId is "+applicationId);
		//applicationId="0000200001201600636";
		
		return applicationId;
	}
}
