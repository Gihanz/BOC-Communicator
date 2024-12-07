

package com.boc.fiserv.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.BankSvc.AcctListInq.rq.AcctListInqRq;
import com.boc.BankSvc.AcctListInq.rq.RecCtrlInType;
import com.boc.BankSvc.AcctListInq.rq.SPNameType;
import com.boc.BankSvc.AcctListInq.rs.AcctBalType;
import com.boc.BankSvc.AcctListInq.rs.AcctBasicType;
import com.boc.BankSvc.AcctListInq.rs.AcctListInqRs;
import com.boc.BankSvc.AcctListInq.rs.AcctSummType;
import com.boc.BankSvc.AcctListInq.rs.CurrencyAmount;
import com.boc.BankSvc.AcctListInq.rq.AcctListInqRq.CustId;
import com.boc.BankSvc.AcctListInq.rq.AcctListInqRq.RqUID;
import com.boc.fiserv.AcctLstInqService;
import com.boc.fiserv.response.AccountFundingListBean;
import com.boc.fiserv.response.AccountLstInqResponse;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Apr 12, 2017
*/
public class AcctLstInqServiceImpl extends BaseServiceImpl implements AcctLstInqService
{
	private static Logger log = LoggerFactory.getLogger(AcctLstInqServiceImpl.class);
	public static AccountLstInqResponse accountLstInqResponse = new AccountLstInqResponse();
	
	public AccountLstInqResponse execute(String cifNumber,String cursor,Long maxrecord) throws JAXBException,Exception
	{
		String inputRq=null;
		inputRq = createAccountlstInqRqJaxbObject(cifNumber,cursor,maxrecord);
		if(null!=inputRq)
		{
			accountLstInqResponse = invokeAccountlstInqRq(inputRq,cifNumber,accountLstInqResponse);
		}
		return accountLstInqResponse;
	}
	
	public String getDebitAmount(String cifNumber, String accountNo,String accountType)  throws JAXBException,Exception
	{
		String serviceRquid=null,inputRq=null;
		ResourceBundle rsbundle = null;
		AcctListInqRq acctListInqRq = null;
		CustId custId = null;
		custId = new CustId();
		RqUID rquId=null;
		try
		{
			rsbundle =getResourceBundle();
			rquId = new RqUID();
			serviceRquid = GenerateUUID.getUUID();		
			rquId.setValue(serviceRquid);	
			acctListInqRq.setRqUID(rquId);
			custId.setSPName(SPNameType.FISERV_ICBS);
			custId.setCustPermId(cifNumber);
			log.info("createAccountlstInqRqJaxbObject:cifNumber is "+cifNumber);
			acctListInqRq.setCustId(custId);
			custId.setSPName(SPNameType.FISERV_ICBS);
			custId.setCustPermId(cifNumber);
			log.info("createAccountlstInqRqJaxbObject:cifNumber is "+cifNumber);
			acctListInqRq.setCustId(custId);
			
			
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createAccountlstInqRqJaxbObject Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return inputRq;
	}
	
	public String createAccountlstInqRqJaxbObject(String cifNumber,String cursor,Long maxrecord) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = null;
		RqUID rquId=null;
		AcctListInqRq acctListInqRq = null;
		RecCtrlInType recCtrlIn = null;
		CustId custId = null;
		acctListInqRq = new AcctListInqRq();
		recCtrlIn = new RecCtrlInType();
		custId = new CustId();
		String serviceRquid=null,inputRq=null;
		Long recursiveCount;
		try		
		{
			rsbundle =getResourceBundle();
			rquId = new RqUID();
			serviceRquid = GenerateUUID.getUUID();		
			rquId.setValue(serviceRquid);	
			acctListInqRq.setRqUID(rquId);
			recursiveCount = Long.valueOf((String)rsbundle.getString("RECURSIVECOUNT"));
			recCtrlIn.setMaxRec(recursiveCount);
			if(maxrecord==99)
			{
				recCtrlIn.setCursor(cursor);
			}
			acctListInqRq.setRecCtrlIn(recCtrlIn);
			custId.setSPName(SPNameType.FISERV_ICBS);
			custId.setCustPermId(cifNumber);
			log.info("createAccountlstInqRqJaxbObject:cifNumber is "+cifNumber);
			acctListInqRq.setCustId(custId);
			
			String inputXML = generateInputXML(acctListInqRq);
			String constantXML = getConstantXML();
			
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<BankSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</BankSvcRq></IFX>";
			inputRq = constantXML+cifSvcRq;
			log.info("createAccountlstInqRqJaxbObject:inputRq is "+inputRq);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createAccountlstInqRqJaxbObject Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return inputRq;
	}
	
	public AccountLstInqResponse invokeAccountlstInqRq(String inputXML,String cifNumber,AccountLstInqResponse accountLstInqResponse) throws Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("AcctLstInqServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("AcctLstInqServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		String outputXML = null;
		List<AcctSummType> acctSummTypeLst = null;
		List<AcctBalType> acctBalList = null;
		AcctBasicType acctBasicType = null;
		//AccountLstInqResponse accountLstInqResponse = null;
		//accountLstInqResponse = new AccountLstInqResponse();
		AccountFundingListBean accountFundingListBean = null;
		List<AccountFundingListBean> accountFundingListBeanLst = null;
		accountFundingListBeanLst = new ArrayList<AccountFundingListBean>();
		CurrencyAmount accountBal = null;
		boolean availAct = false;
		Long matchedRecord=null;
		String cursor=null;
		String accountType=null,accountNo = null,productName=null,relationshipCode=null,balanceType=null,actStatus=null;
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			log.info("removedIFX "+removedIFX);
			AcctListInqRs acctListInqRs =  (AcctListInqRs) generateOutputObject(removedIFX,AcctListInqRs.class);
			log.info(" acctListInqRs  "+acctListInqRs);
			if(null!=acctListInqRs)
			{
				statusCode = acctListInqRs.getStatus().getStatusCode();
				log.info(" statusCode  "+statusCode);
				if(null!=statusCode && statusCode==0)
				{
					//acctSummTypeLst = new ArrayList<AcctSummType>();
					matchedRecord = acctListInqRs.getRecCtrlOut().getMatchedRec();
					log.info("matchedRecord is "+matchedRecord);
					cursor = acctListInqRs.getRecCtrlOut().getCursor();
					log.info("cursor is "+cursor);
					acctSummTypeLst = acctListInqRs.getAcctSumm();
					if(null!=acctSummTypeLst && acctSummTypeLst.size()>0)
					{
						for(AcctSummType acctSummType:acctSummTypeLst)
						{
							accountFundingListBean = new AccountFundingListBean();
							acctBasicType = acctSummType.getAcctBasic();
							accountType = acctBasicType.getAcctType();
							accountNo = acctBasicType.getAcctId();
							productName = acctSummType.getNickname();
							relationshipCode = acctSummType.getRelationCode();
							acctBalList = acctSummType.getAcctBal();
							if(null!=acctBalList)
							{
								for(AcctBalType acctBal:acctBalList)
								{
									balanceType = acctBal.getBalType();
									if(null!= balanceType && balanceType.equalsIgnoreCase("Avail"))
									{
										accountBal  = acctBal.getCurAmt();
										availAct = true;
									}
								}							
							}
							//actStatus = acctSummType.getAcctStatCode();
							log.info(" accountType "+accountType);
							log.info(" relationshipCode "+relationshipCode);
							log.info(" relationshipCode.startsWith "+relationshipCode.startsWith("JO"));
							log.info(" availAct "+availAct);
							if((accountType.equalsIgnoreCase("SV") || accountType.equalsIgnoreCase("DD")) && ((relationshipCode.equalsIgnoreCase("SOW") || (relationshipCode.startsWith("JO")))
							&& 	availAct))
							{
								accountFundingListBean.setAccountId(accountNo);
								accountFundingListBean.setProductName(productName);
								accountFundingListBean.setAmount(accountBal.getAmt());
								accountFundingListBean.setCurrency(accountBal.getCurCode());
								accountFundingListBean.setAccountType(accountType);
								accountFundingListBeanLst.add(accountFundingListBean);
								log.info(" accountBal.getAmt() "+accountBal.getAmt());
							}
							
						}
						
						accountLstInqResponse.setAccountFundingListBean(accountFundingListBeanLst);
						log.info("matchedRecord is "+matchedRecord);
						if(matchedRecord==99)
						{
							AcctLstInqServiceImpl acctLstInqServiceImpl = new AcctLstInqServiceImpl();
							acctLstInqServiceImpl.execute(cifNumber, cursor, matchedRecord);
						}
						else
						{
							log.info("matchedRecord is Completed"+matchedRecord);
						}
					}					
				}
				else
				{
					accountLstInqResponse = new AccountLstInqResponse();
					log.info("Error exists ");;
					log.info(acctListInqRs.getStatus().getError().get(0).getErrNum());
					accountLstInqResponse.setErrorCode(acctListInqRs.getStatus().getError().get(0).getErrNum());
					accountLstInqResponse.setErrorDescription(acctListInqRs.getStatus().getError().get(0).getErrDesc());
					//accountLstInqResponseLst.add(accountLstInqResponse);
				}
			}
			
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("invokeAccountlstInqRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			log.error("invokeAccountlstInqRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("invokeAccountlstInqRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("invokeAccountlstInqRq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return accountLstInqResponse;
	}
}
