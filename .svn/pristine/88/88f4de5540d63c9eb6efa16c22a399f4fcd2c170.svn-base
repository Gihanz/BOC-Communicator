

package com.boc.fiserv.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.CIFSvc.CustProfBasicAdd.rq.AddrCodeType;
import com.boc.CIFSvc.CustProfBasicAdd.rq.CurrencyAmount;
import com.boc.CIFSvc.CustProfBasicAdd.rq.CustAddrType;
import com.boc.CIFSvc.CustProfBasicAdd.rq.CustProfBasicAddRq;
import com.boc.CIFSvc.CustProfBasicAdd.rq.CustProfBasicAddRq.RqUID;
import com.boc.connector.CMConnector;
import com.boc.CIFSvc.CustProfBasicAdd.rq.CustProfBasicType;
import com.boc.CIFSvc.CustProfBasicAdd.rq.CustTypeType;
import com.boc.CIFSvc.CustProfBasicAdd.rq.Date;
import com.boc.CIFSvc.CustProfBasicAdd.rq.PersonNameType;
import com.boc.CIFSvc.CustProfBasicAdd.rq.PhoneNumType;
import com.boc.fiserv.CustProfBasicAddService;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Mar 9, 2017
*/
public class CustProfBasicAddServiceImpl2 extends BaseServiceImpl implements CustProfBasicAddService
{
	private static Logger log = LoggerFactory.getLogger(CustProfBasicAddServiceImpl2.class);
	public String execute(String cifDetails,String caseTypeName) throws JAXBException,Exception
	{
		String casaAddParameters = null;
		ResourceBundle rsbundle = getResourceBundle();
		casaAddParameters = rsbundle.getString("CASAADDPARAMETERS");
		String[] casaAddParametersArr = casaAddParameters.split(",");
		//HashMap parametersMap = getCASADetailsFromCM(referenceNumber,caseTypeName,casaAddParametersArr);
		HashMap parametersMap = getCIFDetailsFromJson(cifDetails,caseTypeName);
		String inputXML = createCustProfBasicAddRqJaxbObject(parametersMap);
		String output = invokeCommunicator(inputXML);
		return output;
		
	}

	public HashMap getCIFDetailsFromJson(String cifDetails,String caseTypeName)
	{
		 
		try {
			ResourceBundle rsbundle = getResourceBundle();
			JSONObject jCIFObject = new JSONObject(cifDetails);
			/*branchCode = jCIFObject.getString(rsbundle.getString("CASBRANCHCODE"));
			
			rlcCodeValue = jCIFObject.getString("rlcCode");
			
			
			
			
			
			jObject.put(rlcCode, rlcCodeValue);*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;	
	}
	
	public String invokeCommunicator(String inputXML) throws Exception
	{
		return invokeCustProfBasicAddRq(inputXML);
	}
	
	public String invokeCustProfBasicAddRq(String inputXML)
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		
		String outputXML = null;
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
		
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
		
		return outputXML;
	}
	public HashMap getCASADetailsFromCM(String referenceNumber,String caseTypeName,String[] loanAcctAddParametersArr) throws Exception
	{
		CMConnector cmConnector = new CMConnector();
		log.info("CustProfBasicAddServiceImpl:Retrieving attributes from CaseManager");
		HashMap parametersMap =  cmConnector.getLoanAcctDetails(referenceNumber,caseTypeName,loanAcctAddParametersArr);
		return parametersMap;
	}
	
	public String createCustProfBasicAddRqJaxbObject(HashMap parametersMap) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		
		CustProfBasicAddRq custProfBasicAdd = null;
		RqUID rquId = null;
		CustProfBasicType custProfBasicType = null;
		custProfBasicAdd = new CustProfBasicAddRq();
		rquId = new RqUID();
		String serviceRquid = null;	
		String inputXML=null,constantXML=null,inputRq=null,branchCode =null,shortname=null,customerType=null,accountType=null,lifeStage=null,address=null,
		primaryOfficerCode=null,source=null,prefCustomer=null,title=null,initials=null,name=null,surname=null,
		postCode=null,countryCode=null,sourceOfData=null,accomodation=null,mobile=null,phoneDay=null,phoneEve=null,
		phoneType=null,email=null,nic=null,passport=null,custClassification=null,taxId=null,gender=null,language=null,
		citizen=null,maritalStatus=null,profession=null,sourceOfIncome=null,responsiveness=null;
		int dependents;
		Double income=null;
		PersonNameType personNameType=null;
		CurrencyAmount empIncomeAmount= null;
		CustAddrType custAddrType = null;
		List<PhoneNumType> phoneNumLst= null;
		java.util.Date  openDt=null,dob=null,todayDt=null;
		Calendar openDtCal = null,dobCal=null,todayCal=null;
		Date openDate = null,dateOfBirth=null,todayDate=null;
		try
		{
			serviceRquid = GenerateUUID.getUUID();
			rquId.setValue(serviceRquid);
			custProfBasicAdd.setRqUID(rquId);
			custProfBasicType = new CustProfBasicType();
			String currencyCode = (String)rsbundle.getString("CURRENCYCODE");
			branchCode = (String) parametersMap.get(rsbundle.getString("CASBRANCHCODE"));
			custProfBasicType.setBranchId(branchCode);
			shortname = (String) parametersMap.get(rsbundle.getString("CASSHORTNAME"));
			custProfBasicType.setShortName(shortname);
			openDt = 	(java.util.Date)parametersMap.get(rsbundle.getString("CASOPENEDDATE"));
			openDtCal =	Calendar.getInstance();
			openDtCal.setTime(openDt);
			openDate = new Date();
			openDate.setDay(Long.valueOf(openDtCal.get(openDtCal.DAY_OF_MONTH)));
			openDate.setMonth(Long.valueOf(openDtCal.get(openDtCal.MONTH)+1));
			openDate.setYear(Long.valueOf(openDtCal.get(openDtCal.YEAR)));
			custProfBasicType.setOpenDt(openDate);
			customerType = (String)parametersMap.get(rsbundle.getString("CASCUSTOMERTYPE"));
			lifeStage = (String)parametersMap.get(rsbundle.getString("CASLIFESTAGE"));
			primaryOfficerCode = (String)parametersMap.get(rsbundle.getString("CASPRIMARYOFFICERCODE"));
			source = (String)parametersMap.get(rsbundle.getString("CASSOURCE"));
			if(source.equalsIgnoreCase("Branch Office"))
			{
				custProfBasicType.setPrimOfficerCode("BRV");
				log.info("CustProfBasicAddServiceImpl:setting officeCode BRV");
			}
			else if(source.equalsIgnoreCase("Call Center"))
			{
				custProfBasicType.setPrimOfficerCode("CLL");
				log.info("CustProfBasicAddServiceImpl:setting officeCode CLL");
			}
			else if(source.equalsIgnoreCase("Internet"))
			{
				custProfBasicType.setPrimOfficerCode("INT");
				log.info("CustProfBasicAddServiceImpl:setting officeCode INT");
			}
			else if(source.equalsIgnoreCase("Kiosk"))
			{
				custProfBasicType.setPrimOfficerCode("KOK");
				log.info("CustProfBasicAddServiceImpl:setting officeCode KOK");
			}
			else if(source.equalsIgnoreCase("Mobile"))
			{
				custProfBasicType.setPrimOfficerCode("MOB");
				log.info("CustProfBasicAddServiceImpl:setting officeCode MOB");
			}
			else if(source.equalsIgnoreCase("Web"))
			{
				custProfBasicType.setPrimOfficerCode("WEB");
				log.info("CustProfBasicAddServiceImpl:setting officeCode WEB");
			}
			prefCustomer = (String)parametersMap.get(rsbundle.getString("CASPREFCUSTOMER"));
			custProfBasicType.setPreferredCustCode(prefCustomer);
			title = (String)parametersMap.get(rsbundle.getString("CASTITLE"));
			custProfBasicType.setSalutation(title);
			
			personNameType = new PersonNameType();
			personNameType.setTitlePrefix(title);
			
			initials = (String)parametersMap.get(rsbundle.getString("CASINITIALS"));
			personNameType.setNameSuffix(initials);
			surname = (String)parametersMap.get(rsbundle.getString("CASSURNAME"));
			personNameType.setLastName(surname);
			custProfBasicType.setPersonName(personNameType);
			
			address = (String)parametersMap.get(rsbundle.getString("CASADDRESS"));
			custAddrType = new CustAddrType();
			
			name = (String)parametersMap.get(rsbundle.getString("CASNAME"));
			postCode = (String)parametersMap.get(rsbundle.getString("CASPOSTCODE"));
			countryCode = (String)parametersMap.get(rsbundle.getString("CASCOUNTRY"));
			
			custAddrType.setFullName(name);
			custAddrType.setAddr1(address);
			custAddrType.setPostalCode(postCode);
			custAddrType.setCountryCode(countryCode);
			custAddrType.setAddrCode(AddrCodeType.PRIMARY);			
			custProfBasicType.setCustAddr(custAddrType);
			
			sourceOfData = (String)parametersMap.get(rsbundle.getString("CASSOURCEOFDATA"));
			custProfBasicType.setSourceCode(sourceOfData);
			accomodation = (String)parametersMap.get(rsbundle.getString("CASACCOMODATION"));
			custProfBasicType.setAccomCode(accomodation);
			dob = (java.util.Date)parametersMap.get(rsbundle.getString("CASDOB"));
			
			dobCal =	Calendar.getInstance();
			dobCal.setTime(dob);
			dateOfBirth = new Date();
			dateOfBirth.setDay(Long.valueOf(dobCal.get(dobCal.DAY_OF_MONTH)));
			dateOfBirth.setMonth(Long.valueOf(dobCal.get(dobCal.MONTH)+1));
			dateOfBirth.setYear(Long.valueOf(dobCal.get(dobCal.YEAR)));
			custProfBasicType.setBirthDt(dateOfBirth);
			mobile = (String)parametersMap.get(rsbundle.getString("CASMOBILE"));
			phoneEve = (String)parametersMap.get(rsbundle.getString("CASPHONEEVE"));
			phoneDay = (String)parametersMap.get(rsbundle.getString("CASPHONEDAY"));
			phoneNumLst = new ArrayList<PhoneNumType>();
			PhoneNumType phoneTypeObj= null;
			phoneTypeObj = new PhoneNumType();
			if(null!=phoneEve)
			{
				phoneType = (String)rsbundle.getString("RESIDENCEPHONE");
				phoneTypeObj.setPhoneType(phoneType);
				phoneTypeObj.setPhone(phoneEve);
				phoneNumLst.add(phoneTypeObj);
			}
			if(null!=phoneDay)
			{
				phoneType = (String)rsbundle.getString("BUSINESSPHONE");
				phoneTypeObj.setPhoneType(phoneType);
				phoneTypeObj.setPhone(phoneDay);
				phoneNumLst.add(phoneTypeObj);
			}
			if(null!=mobile)
			{
				phoneType = (String)rsbundle.getString("MOBILEPHONE");
				phoneTypeObj.setPhoneType(phoneType);
				phoneTypeObj.setPhone(mobile);
				phoneNumLst.add(phoneTypeObj);
			}
			custProfBasicType.getPhoneNum().addAll(phoneNumLst);
			email = (String)parametersMap.get(rsbundle.getString("CASEMAIL"));
			custProfBasicType.setBusEmailAddr(email);
			
			custProfBasicType.setSolicitableCode("1"); //ALWAYS HARCODED
			custClassification = (String)parametersMap.get(rsbundle.getString("CASCUSCLASSIFICATION"));
			if(null!=custClassification &&  custClassification.equalsIgnoreCase("Tax Payer"))
			{
				taxId = (String)parametersMap.get(rsbundle.getString("CASTAXID"));				
				custProfBasicType.setTaxId(taxId);
			}
			language = (String)parametersMap.get(rsbundle.getString("CASLANGUAGE"));
			custProfBasicType.setLanguage(language);
			citizen = (String)parametersMap.get(rsbundle.getString("CASCITIZEN"));
			custProfBasicType.setCitizenCode(citizen);
			
			custProfBasicType.setMoveInConfCode("1"); //Always Hardcoded
			
			todayDt  = new java.util.Date();
			
			todayCal =	Calendar.getInstance();
			todayCal.setTime(todayDt);
			todayDate = new Date();
			todayDate.setDay(Long.valueOf(todayCal.get(todayCal.DAY_OF_MONTH)));
			todayDate.setMonth(Long.valueOf(todayCal.get(todayCal.MONTH)+1));
			todayDate.setYear(Long.valueOf(todayCal.get(todayCal.YEAR)));
			custProfBasicType.setMoveInDt(todayDate);
			
			profession = (String)parametersMap.get(rsbundle.getString("CASPROFESSION"));
			custProfBasicType.setProfessionCode(profession);
			
			empIncomeAmount = new  CurrencyAmount();
			income =	(Double) parametersMap.get(rsbundle.getString("CASINCOME"));
			
			log.info("createLoanActAddRqJAxbObject:proposedAmt is "+income);
			if(null!=income)
				empIncomeAmount.setAmt(getBigDecimalWithDecimalPoint(income));
			//empIncomeAmount.setCurCode(currencyCode);
			
			custProfBasicType.setEmpIncomeAmt(empIncomeAmount);
			sourceOfIncome = (String)parametersMap.get(rsbundle.getString("CASSOURCEOFINCOME"));
			custProfBasicType.setEmpIncomeSource(sourceOfIncome);
			
			responsiveness = (String)parametersMap.get(rsbundle.getString("CASRESPONSIVENESS"));
			custProfBasicType.setResponsivenessCode(responsiveness);
			
			if(customerType.equalsIgnoreCase("P"))
			{
				custProfBasicType.setCustType(CustTypeType.PERSONAL);
				custProfBasicType.setCustMailAcctCode("R");
				custProfBasicType.setLifeStageCode(lifeStage);
				nic = (String)parametersMap.get(rsbundle.getString("CASNIC"));
				custProfBasicType.setNationalId(nic);
				passport = (String)parametersMap.get(rsbundle.getString("CASPASSPORT"));
				custProfBasicType.setPassportId(passport);
				gender = (String)parametersMap.get(rsbundle.getString("CASGENDER"));
				custProfBasicType.setGenderCode(gender);
				dependents = (Integer)parametersMap.get(rsbundle.getString("CASDEPENDENTS"));
				custProfBasicType.setDependentsNum(new BigInteger(String.valueOf(dependents)));
				maritalStatus = (String)parametersMap.get(rsbundle.getString("CASMARTIALSTATUS"));
				custProfBasicType.setMaritalStatusCode(maritalStatus);
			}
			else
			{
				custProfBasicType.setCustType(CustTypeType.BUSINESS);
				accountType = (String)parametersMap.get(rsbundle.getString("CASACCOUNTTYPE"));
				custProfBasicType.setCustMailAcctCode(accountType);
				custProfBasicType.setLifeStageCode("N/A");
			}
			custProfBasicType.setSecondaryOfficerCode("NO");
			
			inputXML = generateInputXML(custProfBasicType);
			constantXML = getConstantXML();
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<CIFSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</CIFSvcRq></IFX>";
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
			log.error("createCustProfBasicInqRqJaxbObject Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			custProfBasicAdd = null;
			rquId = null;
			custProfBasicType = null;
			serviceRquid = null;	
			inputXML=null;constantXML=null;inputRq=null;branchCode =null;shortname=null;customerType=null;accountType=null;lifeStage=null;address=null;
			primaryOfficerCode=null;source=null;prefCustomer=null;title=null;initials=null;name=null;surname=null;
			postCode=null;countryCode=null;sourceOfData=null;accomodation=null;mobile=null;phoneDay=null;phoneEve=null;
			phoneType=null;email=null;nic=null;passport=null;custClassification=null;taxId=null;gender=null;language=null;
			citizen=null;maritalStatus=null;profession=null;sourceOfIncome=null;responsiveness=null;
			income=null;
			personNameType=null;
			empIncomeAmount= null;
			custAddrType = null;
			phoneNumLst= null;
			openDt=null;dob=null;todayDt=null;
			openDtCal = null;dobCal=null;todayCal=null;
			openDate = null;dateOfBirth=null;todayDate=null;
		}
		
	

		return inputRq;
		
	}
	
	
	public static void main(String a[])
	{
		
	}
}
