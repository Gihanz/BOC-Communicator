

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

import com.boc.DepSvc.DepAcctAdd.rq.ApplicationInfoType;
import com.boc.DepSvc.DepAcctAdd.rq.CreditIntType;
import com.boc.DepSvc.DepAcctAdd.rq.CurrencyAmount;
import com.boc.DepSvc.DepAcctAdd.rq.CustRelationType;
import com.boc.DepSvc.DepAcctAdd.rq.CustTypeType;
import com.boc.DepSvc.DepAcctAdd.rq.Date;
import com.boc.DepSvc.DepAcctAdd.rq.DepAcctAddRq;
import com.boc.DepSvc.DepAcctAdd.rq.DepAcctAddRq.RqUID;
import com.boc.DepSvc.DepAcctAdd.rq.DepAcctBasicType;
import com.boc.DepSvc.DepAcctAdd.rq.DepAcctDetailType;
import com.boc.DepSvc.DepAcctAdd.rq.DepAcctIdType;
import com.boc.DepSvc.DepAcctAdd.rq.DepAcctInfoType;
import com.boc.DepSvc.DepAcctAdd.rq.ExtraInfoType;
import com.boc.DepSvc.DepAcctAdd.rq.StmtInfoType;
import com.boc.DepSvc.DepAcctAdd.rq.SvcChgType;
import com.boc.DepSvc.DepAcctAdd.rq.TransferToAcctType;
import com.boc.DepSvc.DepAcctAdd.rs.DepAcctAddRs;
import com.boc.fiserv.CreateAccountService;
import com.boc.fiserv.response.AccountAddResponse;
import com.boc.fiserv.response.CalcDateResponse;
import com.boc.fiserv.response.CustomerProfileAddRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Apr 10, 2017
*/
public class CreateAccountServiceImpl extends BaseServiceImpl implements CreateAccountService
{
	private static Logger log = LoggerFactory.getLogger(CreateAccountServiceImpl.class);
	public AccountAddResponse execute(JSONObject accountDetails,String applicationId) throws JAXBException,Exception
	{
		String inputXML = createAccountAddRqJaxbObject(accountDetails,applicationId);
		log.info("inputXML is "+inputXML);
		AccountAddResponse accResponse = null;
		if(null!= inputXML)
			accResponse	 = invokeAccountAddRq(inputXML);
		return accResponse;
	}
	
	public AccountAddResponse invokeAccountAddRq(String inputXML)
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("AcctAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("AcctAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		String outputXML = null;
		String accountNo = null;
		DepAcctAddRs depAccountAddRs =null;
		AccountAddResponse accResponse = null;
		accResponse = new AccountAddResponse();

		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			depAccountAddRs =  (DepAcctAddRs) generateOutputObject(removedIFX,DepAcctAddRs.class);
			if(null!=depAccountAddRs)
			{
				statusCode = depAccountAddRs.getStatus().getStatusCode();
				if(null!=statusCode && statusCode==0)
				{
					accountNo = depAccountAddRs.getDepAcctId().getAcctId();
					accResponse.setAccountNo(accountNo);
				}
				else
				{
					accResponse.setErrorCode(depAccountAddRs.getStatus().getError().get(0).getErrNum());
					accResponse.setErrorDescription(depAccountAddRs.getStatus().getError().get(0).getErrDesc());
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
		
		return accResponse;
	}
	
	
	public String createAccountAddRqJaxbObject(JSONObject accountDetails,String applicationId) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = null;
		String currencyCode=null,dateFormat=null,serviceRquid=null;
		DepAcctAddRq deptAcctAddRq = null;
		DepAcctIdType depAcctIdType = null;
		DepAcctInfoType depAcctInfo = null;
		DepAcctBasicType deptAcctBasic = null;
		DepAcctDetailType depAcctDetailType = null;
		ApplicationInfoType applicationInfoType = null;
		TransferToAcctType transferToAcctType = null;
		SvcChgType svcChgType = null;
		StmtInfoType stmtInfoType = null;
		CreditIntType creditIntType = null;
		depAcctInfo = new DepAcctInfoType();
		depAcctIdType = new DepAcctIdType();
		deptAcctAddRq = new DepAcctAddRq();
		deptAcctBasic = new DepAcctBasicType();
		applicationInfoType = new ApplicationInfoType();
		depAcctDetailType = new DepAcctDetailType();
		transferToAcctType = new TransferToAcctType();
		ExtraInfoType extraInfo = null;
		svcChgType = new SvcChgType();
		creditIntType = new CreditIntType();
		stmtInfoType = new StmtInfoType();
		RqUID rquId = null;
		CurrencyAmount currencyAmount = null;
		String accountType=null,operatingInstruction=null,productCode=null,source=null,branchCode=null,customerType=null,updateStatementCycle=null;
		String actType=null,fundingActType=null,fundingActId=null,altAddressCheck=null,inputXML=null,constantXML=null,inputRq=null,staff=null;
		String referenceNo=null,shortName=null,statementCycle=null,cyclePeriods=null,actPurpose=null,ownership=null,specialHandlingCode=null;
		JSONArray cifArray = null,relationshipArray=null,accountCustomerTypeArray=null;
		Date applicationDate = null,nextSvcChgDate=null,nextStmtDate=null;
		java.util.Date nextStmtDt=null;
		Calendar todayCal = null,nextStmtCal=null;
		Double amountTransfer = null;
		boolean existingFundReq;
		List<CustRelationType>customerRelationshipLst = null;
		try		
		{
			rsbundle =getResourceBundle();
			JSONObject jAccountObject = accountDetails; //new JSONObject(accountDetails);
			currencyCode = (String)rsbundle.getString("CURRENCYCODE");
			dateFormat = (String)rsbundle.getString("dateFormat");
			rquId = new RqUID();
			serviceRquid = null;	
			serviceRquid = GenerateUUID.getUUID();		
			rquId.setValue(serviceRquid);
			deptAcctAddRq.setRqUID(rquId);
			int joint=1;
			if((jAccountObject.has(rsbundle.getString("CASACCOUNTTYPE"))))
			{
				accountType = (String) jAccountObject.getString(rsbundle.getString("CASACCOUNTTYPE"));
				log.info("createAccountAddRqJaxbObject:accountType is "+accountType);
				depAcctIdType.setAcctType(accountType);
			}
			deptAcctAddRq.setDepAcctId(depAcctIdType);
			
			customerRelationshipLst = new ArrayList<CustRelationType>();
			if((jAccountObject.has(rsbundle.getString("CASACCOUNTCIF"))))
			{
				cifArray= jAccountObject.getJSONArray(rsbundle.getString("CASACCOUNTCIF"));
				log.info("createAccountAddRqJaxbObject:CASACCOUNTCIF is "+cifArray);
				if(null!= cifArray && cifArray.size()>0)
				{
					log.info("cifArray size is "+cifArray.size());
					if(cifArray.size()==1)
					{
						CustRelationType custRelationType1 = null;
						custRelationType1 = new CustRelationType();
						custRelationType1.setRelationCode("SOW");
						custRelationType1.setCustPermId((String)cifArray.get(0));
						customerRelationshipLst.add(custRelationType1);
						depAcctInfo.getCustRelation().addAll(customerRelationshipLst);
					}
					else
					{
						for(int i=0;i<cifArray.size();i++)
						{
							CustRelationType custRelationType = null;
							custRelationType = new CustRelationType();
							if((jAccountObject.has(rsbundle.getString("CASRELATIONSHIP"))))
							{
								relationshipArray = jAccountObject.getJSONArray(rsbundle.getString("CASRELATIONSHIP"));
								String relationship = relationshipArray.getString(i);
								if(jAccountObject.has(rsbundle.getString("CASOPERATINGINSTR")))
								{
									String cifNumber = (String)cifArray.get(i);
									log.info("createAccountAddRqJaxbObject:cifNumber is "+cifNumber+" && relationship "+relationship);
									operatingInstruction = (String) jAccountObject.getString(rsbundle.getString("CASOPERATINGINSTR"));
									if(null!= relationship && relationship.equalsIgnoreCase("Primary"))
									{
										if(null!= operatingInstruction && operatingInstruction.equalsIgnoreCase("Both"))
										{
											custRelationType.setRelationCode("JAF");
											custRelationType.setCustPermId(cifNumber);
											customerRelationshipLst.add(custRelationType);
										}
										else
										{
											custRelationType.setRelationCode("JOF");
											custRelationType.setCustPermId(cifNumber);
											customerRelationshipLst.add(custRelationType);
										}
									}
									else
									{
										if(null!= operatingInstruction && operatingInstruction.equalsIgnoreCase("Both"))
										{
											custRelationType.setRelationCode("JA"+joint);
											custRelationType.setCustPermId(cifNumber);
											customerRelationshipLst.add(custRelationType);
										}
										else
										{
											custRelationType.setRelationCode("JO"+joint);
											custRelationType.setCustPermId(cifNumber);
											customerRelationshipLst.add(custRelationType);
										}
										joint=joint+1;
									}
								}
							}
						}
						depAcctInfo.getCustRelation().addAll(customerRelationshipLst);
					}
				}
			}
			
			applicationInfoType.setApplicationId(applicationId);
			
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
			
			applicationDate = new Date();
			applicationDate.setDay(Long.valueOf(todayCal.get(todayCal.DAY_OF_MONTH)));
			applicationDate.setMonth(Long.valueOf(todayCal.get(todayCal.MONTH)+1));
			applicationDate.setYear(Long.valueOf(todayCal.get(todayCal.YEAR)));
			applicationInfoType.setApplicationDt(applicationDate);
			deptAcctBasic.setApplicationInfo(applicationInfoType);
			
			if((jAccountObject.has(rsbundle.getString("CASPRODUCTCODE"))))
			{
				productCode = (String) jAccountObject.getString(rsbundle.getString("CASPRODUCTCODE"));
				deptAcctBasic.setProdId(productCode);
			}
			
			if(jAccountObject.has(rsbundle.getString("CASSOURCE")))
				source = (String)jAccountObject.getString(rsbundle.getString("CASSOURCE"));
			log.info("createAccountAddRqJaxbObject:source is "+source);
			if(null!= source && source.equalsIgnoreCase("Branch Office"))
			{
				deptAcctBasic.setPrimOfficerCode("BRV");
				log.info("createAccountAddRqJaxbObject:setting officeCode BRV");
			}
			else if(null!= source && source.equalsIgnoreCase("Call Center"))
			{
				deptAcctBasic.setPrimOfficerCode("CLL");
				log.info("createAccountAddRqJaxbObject:setting officeCode CLL");
			}
			else if(null!= source && source.equalsIgnoreCase("Internet"))
			{
				deptAcctBasic.setPrimOfficerCode("INT");
				log.info("createAccountAddRqJaxbObject:setting officeCode INT");
			}
			else if(null!= source  && source.equalsIgnoreCase("Kiosk"))
			{
				deptAcctBasic.setPrimOfficerCode("KOK");
				log.info("createAccountAddRqJaxbObject:setting officeCode KOK");
			}
			else if(null!= source && source.equalsIgnoreCase("Mobile"))
			{
				deptAcctBasic.setPrimOfficerCode("MOB");
				log.info("createAccountAddRqJaxbObject:setting officeCode MOB");
			}
			else if(null!= source && source.equalsIgnoreCase("Web"))
			{
				deptAcctBasic.setPrimOfficerCode("WEB");
				log.info("createAccountAddRqJaxbObject:setting officeCode WEB");
			}
			
			if(jAccountObject.has(rsbundle.getString("CASBRANCHCODE")))
				branchCode = (String)jAccountObject.getString(rsbundle.getString("CASBRANCHCODE"));
			log.info("createAccountAddRqJaxbObject:branchCode is "+branchCode);
			if(null!=branchCode)
				deptAcctBasic.setBranchId(branchCode);
			
			if(jAccountObject.has(rsbundle.getString("CASFUNDEXISTACT")))
			{
				existingFundReq = Boolean.valueOf(jAccountObject.getString(rsbundle.getString("CASFUNDEXISTACT")));
				log.info("createAccountAddRqJaxbObject:existingFundReq is "+existingFundReq);
			
				if(jAccountObject.has(rsbundle.getString("CASTRANSFERAMOUNT")))
				{
					currencyAmount = new  CurrencyAmount();
					amountTransfer =Double.valueOf(jAccountObject.getString(rsbundle.getString("CASTRANSFERAMOUNT")));
					log.info("createAccountAddRqJaxbObject:amountTransfer is "+amountTransfer);
					if(null!=amountTransfer)
						currencyAmount.setAmt(getBigDecimalWithDecimalPoint(amountTransfer));
					currencyAmount.setCurCode(currencyCode);
					
					deptAcctBasic.setOpenAmt(currencyAmount);
				}
			
				if(jAccountObject.has(rsbundle.getString("CASACCOUNTTYPE")))
				{
					
					actType = jAccountObject.getString(rsbundle.getString("CASACCOUNTTYPE"));
					log.info("createAccountAddRqJaxbObject:actType is "+actType);
					if(actType.equalsIgnoreCase("SV"))
						transferToAcctType.setAnchorProfile("193");
					else
					{
						transferToAcctType.setAnchorProfile("192");						
					}
				}
			
				if(jAccountObject.has(rsbundle.getString("CASFUNDINGACTID")))
				{
					fundingActId = jAccountObject.getString(rsbundle.getString("CASFUNDINGACTID"));
					log.info("createAccountAddRqJaxbObject:fundingActId is "+fundingActId);
					transferToAcctType.setAcctId(fundingActId);
				}
				if(jAccountObject.has(rsbundle.getString("CASFUNCDINGACTTYPE")))
				{
					fundingActType = jAccountObject.getString(rsbundle.getString("CASFUNCDINGACTTYPE"));
					log.info("createAccountAddRqJaxbObject:fundingActType is "+fundingActType);
					if(fundingActType.equalsIgnoreCase("SV"))
						transferToAcctType.setTargetProfile("123");
					else
						transferToAcctType.setTargetProfile("122");
				}
				
				if(jAccountObject.has(rsbundle.getString("CASREFERENCENO")))
				{
					referenceNo = jAccountObject.getString(rsbundle.getString("CASREFERENCENO"));
					log.info("createAccountAddRqJaxbObject:referenceNo is "+referenceNo);
					transferToAcctType.setReferenceId(referenceNo);
				}
				if(jAccountObject.has(rsbundle.getString("CASSHORTNAME")))
				{
					shortName = jAccountObject.getString(rsbundle.getString("CASSHORTNAME"));
					log.info("createAccountAddRqJaxbObject:shortName is "+shortName);
					transferToAcctType.setDescription("Initial Dep - "+shortName);
				}
				
				deptAcctBasic.setFundingAcct(transferToAcctType);
			}
			
		
			
			depAcctInfo.setDepAcctBasic(deptAcctBasic);
			
			svcChgType.setSvcChgFreq("M00131"); //Always Hardcoded
			
			nextSvcChgDate = new Date();
			nextSvcChgDate.setDay(Long.valueOf(todayCal.getActualMaximum(Calendar.DAY_OF_MONTH)));
			nextSvcChgDate.setMonth(Long.valueOf(todayCal.get(todayCal.MONTH)+1));
			nextSvcChgDate.setYear(Long.valueOf(todayCal.get(todayCal.YEAR)));
			log.info("day is "+Long.valueOf(todayCal.getActualMaximum(Calendar.DAY_OF_MONTH)));
			log.info("month is "+Long.valueOf(todayCal.get(todayCal.MONTH)+1));
			log.info("year is "+Long.valueOf(todayCal.get(todayCal.YEAR)));
			svcChgType.setNxtSvcChgDt(nextSvcChgDate);
			depAcctInfo.setSvcChg(svcChgType);
			
			log.info("createAccountAddRqJaxbObject:nextSvcChgDate is "+nextSvcChgDate);
			
			/*deptAcctBasic Account Fund has to be added
			 * 
			 * 	
			svcChgType.setSvcChgFreq(value);
			 * */
			
			
			
			if(jAccountObject.has(rsbundle.getString("CASACCOUNTCUSTOMERTYPE")))
				accountCustomerTypeArray = jAccountObject.getJSONArray(rsbundle.getString("CASACCOUNTCUSTOMERTYPE"));	
			if(null!= accountCustomerTypeArray && accountCustomerTypeArray.size()>0)
			{
				customerType = (String)accountCustomerTypeArray.get(0);
				if(null!=customerType && customerType.equalsIgnoreCase("PERSONAL"))
					depAcctDetailType.setCustType(CustTypeType.PERSONAL);
				else
					depAcctDetailType.setCustType(CustTypeType.BUSINESS);
			}
			
			if(jAccountObject.has(rsbundle.getString("CASSTAFF")))
			{
				staff = (String)jAccountObject.getString(rsbundle.getString("CASSTAFF"));	
				log.info("createAccountAddRqJaxbObject:staff is "+staff);
				if(null!=staff && staff.equalsIgnoreCase("True"))
				{
					depAcctDetailType.setEmployeeCode("E");
				}
			}
			
			depAcctInfo.setDepAcctDetail(depAcctDetailType);
			
			stmtInfoType.setNoActivityStmtCode("N");  //Always Hardcoded
			
			if(jAccountObject.has(rsbundle.getString("CASESTATEMENTTYPE")))
			{
				specialHandlingCode = (String)jAccountObject.getString(rsbundle.getString("CASESTATEMENTTYPE"));
				log.info("createAccountAddRqJaxbObject:specialHandlingCode is "+specialHandlingCode);
				stmtInfoType.setSpecialHandlingCode(specialHandlingCode+"LK");
			}
			
			/*if(jAccountObject.has(rsbundle.getString("CASALTADDRESSCHECK")))
				altAddressCheck = (String)jAccountObject.getString(rsbundle.getString("CASALTADDRESSCHECK"));
			log.info("createAccountAddRqJaxbObject:altAddressCheck is "+altAddressCheck);
			if(altAddressCheck.equalsIgnoreCase("true"))
				stmtInfoType.setUseAlternateAddrFlag(true);
			else
				stmtInfoType.setUseAlternateAddrFlag(false);*/
			
			//Check if Update statement Cycle is enabled  
			
			if(jAccountObject.has(rsbundle.getString("CASUPDATESTATEMENTCYCLE")))
				updateStatementCycle = (String)jAccountObject.getString(rsbundle.getString("CASUPDATESTATEMENTCYCLE"));	
			log.info("createAccountAddRqJaxbObject:updateStatementCycle is "+updateStatementCycle);
			if(updateStatementCycle.equalsIgnoreCase("True"))
			{	
				if(jAccountObject.has(rsbundle.getString("CASSTATEMENTCYCLE")))
					statementCycle = (String)jAccountObject.getString(rsbundle.getString("CASSTATEMENTCYCLE"));	
				log.info("createAccountAddRqJaxbObject:statementCycle is "+statementCycle);
				String freq= null;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(current);
				calendar.add(Calendar.MONTH,-1); //Service returns 1month advance
				if(null!=statementCycle && statementCycle.equalsIgnoreCase("M"))
				{
					freq = "M00131";
					int lastDate = calendar.getActualMaximum(Calendar.DATE);
					calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
					log.info("Monthly Date "+calendar.getTime());
					calendar.setTime(calendar.getTime());
					
					nextStmtDate = new Date();
					nextStmtDate.setDay(Long.valueOf(calendar.get(calendar.DAY_OF_MONTH)));
					nextStmtDate.setMonth(Long.valueOf(calendar.get(calendar.MONTH)+1));
					nextStmtDate.setYear(Long.valueOf(calendar.get(calendar.YEAR)));
					
					stmtInfoType.setNextStmtDt(nextStmtDate);
				}
				if(null!=statementCycle && statementCycle.equalsIgnoreCase("W"))
				{
					freq = "D00700";
					calendar.setFirstDayOfWeek(Calendar.MONDAY);
					calendar.setTime(current);
				    int today = calendar.get(Calendar.DAY_OF_WEEK);
				    calendar.add(Calendar.DAY_OF_WEEK, -today+Calendar.FRIDAY);
				    log.info("Weekly Date "+calendar.getTime());
					calendar.setTime(calendar.getTime());
					
					nextStmtDate = new Date();
					nextStmtDate.setDay(Long.valueOf(calendar.get(calendar.DAY_OF_MONTH)));
					nextStmtDate.setMonth(Long.valueOf(calendar.get(calendar.MONTH)+1));
					nextStmtDate.setYear(Long.valueOf(calendar.get(calendar.YEAR)));
					
					stmtInfoType.setNextStmtDt(nextStmtDate);
				}
				if(null!=statementCycle && statementCycle.equalsIgnoreCase("D"))
				{
					freq = "D00100";
					calendar.setTime(current);
					
					nextStmtDate = new Date();
					nextStmtDate.setDay(Long.valueOf(calendar.get(calendar.DAY_OF_MONTH)));
					nextStmtDate.setMonth(Long.valueOf(calendar.get(calendar.MONTH)+1));
					nextStmtDate.setYear(Long.valueOf(calendar.get(calendar.YEAR)));
					
					stmtInfoType.setNextStmtDt(nextStmtDate);
				}
				if(null!=statementCycle && statementCycle.equalsIgnoreCase("Q"))
				{
					freq = "C00400";
				}
				if(null!=statementCycle &&  statementCycle.equalsIgnoreCase("B"))
				{
					freq = "C00200";
				}
				if(null!=statementCycle && statementCycle.equalsIgnoreCase("A"))
				{
					freq = "C00100";
				}
				
				stmtInfoType.setStmtFreq(freq);
			}
			else //Hardcoded to fix for TimeOut
			{
				String freq= null;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(current);
				calendar.add(Calendar.MONTH,-1); 
				freq = "M00131";
				int lastDate = calendar.getActualMaximum(Calendar.DATE);
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				log.info("Monthly Date "+calendar.getTime());
				calendar.setTime(calendar.getTime());
				
				nextStmtDate = new Date();
				nextStmtDate.setDay(Long.valueOf(calendar.get(calendar.DAY_OF_MONTH)));
				nextStmtDate.setMonth(Long.valueOf(calendar.get(calendar.MONTH)+1));
				nextStmtDate.setYear(Long.valueOf(calendar.get(calendar.YEAR)));
				
				stmtInfoType.setNextStmtDt(nextStmtDate);
				stmtInfoType.setStmtFreq(freq);
			}
			
			
			
			stmtInfoType.setMailCode("N"); //Always Hardcoded
			
			depAcctInfo.setStmtInfo(stmtInfoType);
			
			creditIntType.setIntPmtFreq("M00131");  //Always Hardcoded
			creditIntType.setNextIntAccrDt(nextSvcChgDate);
			if(null!= accountType && accountType.equalsIgnoreCase("SV"))
			{
				//creditIntType.setIntPlanCode("0");
				log.info("createAccountAddRqJaxbObject:IntPlanCode is 0");
				depAcctInfo.setCreditInt(creditIntType);
			}
			
			extraInfo = new ExtraInfoType();
			
			if(jAccountObject.has(rsbundle.getString("CASACCOUNTPURPOSE")))
				actPurpose = (String)jAccountObject.getString(rsbundle.getString("CASACCOUNTPURPOSE"));	
			log.info("createAccountAddRqJaxbObject:actPurpose is "+actPurpose);
			
			if(jAccountObject.has(rsbundle.getString("CASOWNERSHIP")))
				ownership = (String)jAccountObject.getString(rsbundle.getString("CASOWNERSHIP"));	
			log.info("createAccountAddRqJaxbObject:ownership is "+ownership);
			
			extraInfo.setOwnershipCode(ownership);
			extraInfo.setPurposeCode(actPurpose);
			
			deptAcctAddRq.setExtraInfo(extraInfo);
			deptAcctAddRq.setDepAcctInfo(depAcctInfo);
			
			inputXML = generateInputXML(deptAcctAddRq);
			constantXML = getConstantXML();
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<DepSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</DepSvcRq></IFX>";
			inputRq = constantXML+cifSvcRq;
			log.info("createAccountAddRqJaxbObject:inputRq is "+inputRq);
			System.out.println(inputRq);
		}
			catch(ParseException e)
			{
				e.printStackTrace();
				log.error("createAccountAddRqJaxbObject:inputRq ParseException Occured "+e.fillInStackTrace());
				throw new Exception(e);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				log.error("createAccountAddRqJaxbObject:inputRq Exception Occured "+e.fillInStackTrace());
				throw new Exception(e);
			}
			finally
			{
				deptAcctAddRq = null;
				depAcctIdType = null;
				depAcctInfo = null;
				deptAcctBasic = null;
				depAcctDetailType = null;
				applicationInfoType = null;
				svcChgType = null;
				stmtInfoType = null;
				creditIntType = null;
				cyclePeriods=null;
				rquId = null;
				actType=null;fundingActType=null;fundingActId=null;altAddressCheck=null;inputXML=null;constantXML=null;
				referenceNo=null;shortName=null;statementCycle=null;cyclePeriods=null;
				accountType=null;operatingInstruction=null;productCode=null;source=null;branchCode=null;customerType=null;
				actPurpose=null;ownership=null;
				cifArray = null;relationshipArray=null;
				applicationDate = null;
				nextSvcChgDate=null;nextStmtDate=null;
				nextStmtDt=null;
				todayCal = null;nextStmtCal=null;
				amountTransfer = null;specialHandlingCode=null;
				customerRelationshipLst = null;
			}
		return inputRq;
		
	}
	
	public static String leftPad(String originalString, int length,
	         char padCharacter) {
	      String paddedString = originalString;
	      while (paddedString.length() < length) {
	         paddedString = padCharacter + paddedString;
	      }
	      return paddedString;
	   }
	
}
