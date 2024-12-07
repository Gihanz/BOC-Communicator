

package com.boc.fiserv.impl;


import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import com.boc.CIFSvc.ModifyCIF.rq.AddrCodeType;
import com.boc.CIFSvc.ModifyCIF.rq.CurrencyAmount;
import com.boc.CIFSvc.ModifyCIF.rq.CustAddrType;
import com.boc.CIFSvc.ModifyCIF.rq.CustProfBasicModRq;
import com.boc.CIFSvc.ModifyCIF.rq.CustProfBasicModRq.RqUID;
import com.boc.CIFSvc.ModifyCIF.rq.CustProfBasicType;
import com.boc.CIFSvc.ModifyCIF.rq.CustTypeType;
import com.boc.CIFSvc.ModifyCIF.rq.Date;
import com.boc.CIFSvc.ModifyCIF.rq.OldDataType;
import com.boc.CIFSvc.ModifyCIF.rq.PersonNameType;
import com.boc.CIFSvc.ModifyCIF.rq.PhoneNumType;
import com.boc.CIFSvc.ModifyCIF.rs.CustProfBasicModRs;
import com.boc.fiserv.CIfModifyService;
import com.boc.fiserv.response.CalcDateResponse;
import com.boc.fiserv.response.CustomerProfileAddRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By Mahesh  on July 4, 2017
 */
public class CifModifyServiceImpl extends  BaseServiceImpl implements CIfModifyService  
{
	private static Logger log = LoggerFactory.getLogger(CifModifyServiceImpl.class);
	public CustomerProfileAddRs execute(Object cifDetails) throws JAXBException,Exception
	{
		//String casaAddParameters = null;
		ResourceBundle rsbundle = getResourceBundle();
		CustomerProfileAddRs output = null;
		//casaAddParameters = rsbundle.getString("CASAADDPARAMETERS");
		//String inputXML = createCustProfBasicAddRqJaxbObject(parametersMap);
		String inputXML = modifyCustProfBasicAddRqJaxbObject(cifDetails);
		log.info("inputXML is "+inputXML);
		if(null!=inputXML)
			output = invokeCustModifyComm(inputXML);
		log.info("output is "+output);
		return output;

	}
	public String modifyCustProfBasicAddRqJaxbObject(Object cifDetails) throws JAXBException,Exception
	{

		ResourceBundle rsbundle = getResourceBundle();

		CustProfBasicModRq cifBasicMod = null;
		RqUID rquId = null;
		CustProfBasicType custProfBasicType = null;
		cifBasicMod = new CustProfBasicModRq();
		rquId = new RqUID();
		String serviceRquid = null;	
		String inputXML=null,constantXML=null,inputRq=null,branchCode =null,shortname=null,customerType=null,accountType=null,lifeStage=null,
				primaryOfficerCode=null,source=null,prefCustomer=null,title=null,initials=null,name=null,surname=null,cifId=null,
				postCode=null,countryCode=null,sourceOfData=null,accomodation=null,mobile=null,phoneDay=null,phoneEve=null,dependentsStr=null,
				phoneType=null,email=null,nic=null,passport=null,custClassification=null,taxId=null,gender=null,language=null,dobStr=null,
				citizen=null,maritalStatus=null,profession=null,sourceOfIncome=null,responsiveness=null,address1=null,address2=null,address3=null,address4=null,address5=null;
		int dependents;
		Double income=null;
		PersonNameType personNameType=null;
		CurrencyAmount empIncomeAmount= null;
		CustAddrType custAddrType = null;
		List<PhoneNumType> phoneNumLst= null;
		java.util.Date  openDt=null,dob=null,todayDt=null;
		Calendar openDtCal = null,dobCal=null,todayCal=null;
		Date openDate = null,dateOfBirth=null,todayDate=null;
		SimpleDateFormat sf=null;
		sf  = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			JSONObject jCIFObject = new JSONObject(cifDetails);
			serviceRquid = GenerateUUID.getUUID();
			rquId.setValue(serviceRquid);
			cifBasicMod.setRqUID(rquId);
			custProfBasicType = new CustProfBasicType();
			String currencyCode = (String)rsbundle.getString("CURRENCYCODE");
			String dateFormat = (String)rsbundle.getString("dateFormat");
			if(jCIFObject.has(rsbundle.getString("CASCIFID")))
				cifId = (String) jCIFObject.getString(rsbundle.getString("CASCIFID"));
			//if("".equalsIgnoreCase(cifId))
			{

				if(jCIFObject.has(rsbundle.getString("CASBRANCHCODE")))
					branchCode = (String) jCIFObject.getString(rsbundle.getString("CASBRANCHCODE"));
				log.info("createCustProfBasicAddRqJaxbObject:branchCodet is "+branchCode);
				if(null!= branchCode)
					custProfBasicType.setBranchId(branchCode);
				if(jCIFObject.has(rsbundle.getString("CASSHORTNAME")))
					shortname = (String) jCIFObject.getString(rsbundle.getString("CASSHORTNAME"));
				log.info("createCustProfBasicAddRqJaxbObject:shortname is "+shortname);
				if(null!= shortname)
					custProfBasicType.setShortName(shortname);
				/*String openDateStr = jCIFObject.getString(rsbundle.getString("CASOPENEDDATE"));
				log.info("createCustProfBasicAddRqJaxbObjectopenDateStrt is "+openDateStr);
				if(null!= openDateStr)
				{
				openDt= sf.parse(openDateStr);
				openDtCal =	Calendar.getInstance();
				openDtCal.setTime(openDt);

				openDate = new Date();
				openDate.setDay(Long.valueOf(openDtCal.get(openDtCal.DAY_OF_MONTH)));
				openDate.setMonth(Long.valueOf(openDtCal.get(openDtCal.MONTH)+1));
				openDate.setYear(Long.valueOf(openDtCal.get(openDtCal.YEAR)));
				custProfBasicType.setOpenDt(openDate);
				}*/

				log.info("createCustProfBasicAddRqJaxbObject:primaryOfficerCode is "+jCIFObject.has(rsbundle.getString("CASPRIMARYOFFICERCODE")));
				if((jCIFObject.has(rsbundle.getString("CASPRIMARYOFFICERCODE"))))
					primaryOfficerCode = (String)jCIFObject.getString(rsbundle.getString("CASPRIMARYOFFICERCODE"));
				log.info("createCustProfBasicAddRqJaxbObject:primaryOfficerCode is "+primaryOfficerCode);
				if(jCIFObject.has(rsbundle.getString("CASSOURCE")))
					source = (String)jCIFObject.getString(rsbundle.getString("CASSOURCE"));
				log.info("createCustProfBasicAddRqJaxbObject:source is "+source);
				if(null!= source && source.equalsIgnoreCase("Branch Office"))
				{
					custProfBasicType.setPrimOfficerCode("BRV");
					log.info("CustProfBasicAddServiceImpl:setting officeCode BRV");
				}
				else if(null!= source && source.equalsIgnoreCase("Call Center"))
				{
					custProfBasicType.setPrimOfficerCode("CLL");
					log.info("CustProfBasicAddServiceImpl:setting officeCode CLL");
				}
				else if(null!= source && source.equalsIgnoreCase("Internet"))
				{
					custProfBasicType.setPrimOfficerCode("INT");
					log.info("CustProfBasicAddServiceImpl:setting officeCode INT");
				}
				else if(null!= source  && source.equalsIgnoreCase("Kiosk"))
				{
					custProfBasicType.setPrimOfficerCode("KOK");
					log.info("CustProfBasicAddServiceImpl:setting officeCode KOK");
				}
				else if(null!= source && source.equalsIgnoreCase("Mobile"))
				{
					custProfBasicType.setPrimOfficerCode("MOB");
					log.info("CustProfBasicAddServiceImpl:setting officeCode MOB");
				}
				else if(null!= source && source.equalsIgnoreCase("Web"))
				{
					custProfBasicType.setPrimOfficerCode("WEB");
					log.info("CustProfBasicAddServiceImpl:setting officeCode WEB");
				}
				if(jCIFObject.has(rsbundle.getString("CASPREFCUSTOMER")))
					prefCustomer = (String)jCIFObject.getString(rsbundle.getString("CASPREFCUSTOMER"));
				log.info("createCustProfBasicAddRqJaxbObject:prefCustomer is "+prefCustomer);
				if(null!= prefCustomer)
					custProfBasicType.setPreferredCustCode(prefCustomer);
				if(jCIFObject.has(rsbundle.getString("CASTITLE")))
					title = (String)jCIFObject.getString(rsbundle.getString("CASTITLE"));
				log.info("createCustProfBasicAddRqJaxbObject:title is "+title);
				if(null!= title)
					custProfBasicType.setSalutation(title);

				personNameType = new PersonNameType();
				if(null!= title)
					personNameType.setTitlePrefix(title);
				if(jCIFObject.has(rsbundle.getString("CASINITIALS")))
					initials = (String)jCIFObject.getString(rsbundle.getString("CASINITIALS"));
				log.info("createCustProfBasicAddRqJaxbObject:initials is "+initials);
				if(null!=initials)
					personNameType.setNameSuffix(initials);
				if(jCIFObject.has(rsbundle.getString("CASSURNAME")))
					surname = (String)jCIFObject.getString(rsbundle.getString("CASSURNAME"));
				log.info("createCustProfBasicAddRqJaxbObject:surname is "+surname);
				if(null!=surname)
					personNameType.setLastName(surname);
				custProfBasicType.setPersonName(personNameType);
				if(jCIFObject.has(rsbundle.getString("CASADDRESS1")))
					address1 = (String)jCIFObject.getString(rsbundle.getString("CASADDRESS1"));
				log.info("createCustProfBasicAddRqJaxbObject:address1 is "+address1);
				if(jCIFObject.has(rsbundle.getString("CASADDRESS2")))
					address2 = (String)jCIFObject.getString(rsbundle.getString("CASADDRESS2"));
				log.info("createCustProfBasicAddRqJaxbObject:address2 is "+address2);
				if(jCIFObject.has(rsbundle.getString("CASADDRESS3")))
					address3 = (String)jCIFObject.getString(rsbundle.getString("CASADDRESS3"));
				log.info("createCustProfBasicAddRqJaxbObject:address3 is "+address3);
				if(jCIFObject.has(rsbundle.getString("CASADDRESS4")))
					address4 = (String)jCIFObject.getString(rsbundle.getString("CASADDRESS4"));
				log.info("createCustProfBasicAddRqJaxbObject:address4 is "+address4);
				if(jCIFObject.has(rsbundle.getString("CASADDRESS5")))
					address5 = (String)jCIFObject.getString(rsbundle.getString("CASADDRESS5"));
				log.info("createCustProfBasicAddRqJaxbObject:address5 is "+address5);
				custAddrType = new CustAddrType();
				if(jCIFObject.has(rsbundle.getString("CASNAME")))
					name = (String)jCIFObject.getString(rsbundle.getString("CASNAME"));
				log.info("createCustProfBasicAddRqJaxbObject:name is "+name);
				if(jCIFObject.has(rsbundle.getString("CASPOSTCODE")))
					postCode = (String)jCIFObject.getString(rsbundle.getString("CASPOSTCODE"));
				log.info("createCustProfBasicAddRqJaxbObject:postCode is "+postCode);
				if(jCIFObject.has(rsbundle.getString("CASCOUNTRY")))
					countryCode = (String)jCIFObject.getString(rsbundle.getString("CASCOUNTRY"));
				log.info("createCustProfBasicAddRqJaxbObject:countryCode is "+countryCode);

				if(null!=name)
					custAddrType.setFullName(name);
				if(null!=address1)
					custAddrType.setAddr1(address1);
				if(null!=address2)
					custAddrType.setAddr2(address2);
				if(null!=address3)
					custAddrType.setAddr3(address3);
				if(null!=address4)
					custAddrType.setAddr4(address4);
				if(null!=address5)
					custAddrType.setAddr5(address5);
				if(null!=postCode)
					custAddrType.setPostalCode(postCode);
				if(null!=countryCode)
					custAddrType.setCountryCode(countryCode);
				custAddrType.setAddrCode(AddrCodeType.PRIMARY);			
				custProfBasicType.setCustAddr(custAddrType);

				if(jCIFObject.has(rsbundle.getString("CASSOURCEOFDATA")))
					sourceOfData = (String)jCIFObject.getString(rsbundle.getString("CASSOURCEOFDATA"));
				log.info("createCustProfBasicAddRqJaxbObject:sourceOfData is "+sourceOfData);
				if(null!=sourceOfData)
					custProfBasicType.setSourceCode(sourceOfData);
				if(jCIFObject.has(rsbundle.getString("CASACCOMODATION")))
					accomodation = (String)jCIFObject.getString(rsbundle.getString("CASACCOMODATION"));
				log.info("createCustProfBasicAddRqJaxbObject:accomodation is "+accomodation);
				if(null!=accomodation)
					custProfBasicType.setAccomCode(accomodation);
				if(jCIFObject.has(rsbundle.getString("CASDOB")))
					dobStr = jCIFObject.getString(rsbundle.getString("CASDOB"));
				log.info("createCustProfBasicAddRqJaxbObject:dobStr is "+dobStr);
				if(null!=dobStr)
				{
					dob= sf.parse(dobStr);
					dobCal =	Calendar.getInstance();
					dobCal.setTime(dob);
					dateOfBirth = new Date();
					dateOfBirth.setDay(Long.valueOf(dobCal.get(dobCal.DAY_OF_MONTH)));
					dateOfBirth.setMonth(Long.valueOf(dobCal.get(dobCal.MONTH)+1));
					dateOfBirth.setYear(Long.valueOf(dobCal.get(dobCal.YEAR)));
					custProfBasicType.setBirthDt(dateOfBirth);
				}
				if(jCIFObject.has(rsbundle.getString("CASMOBILE")))
					mobile = (String)jCIFObject.getString(rsbundle.getString("CASMOBILE"));
				log.info("createCustProfBasicAddRqJaxbObject:mobile is "+mobile);
				if(jCIFObject.has(rsbundle.getString("CASPHONEEVE")))
					phoneEve = (String)jCIFObject.getString(rsbundle.getString("CASPHONEEVE"));
				log.info("createCustProfBasicAddRqJaxbObject:phoneEve is "+phoneEve);
				if(jCIFObject.has(rsbundle.getString("CASPHONEDAY")))
					phoneDay = (String)jCIFObject.getString(rsbundle.getString("CASPHONEDAY"));
				log.info("createCustProfBasicAddRqJaxbObject:phoneDay is "+phoneDay);
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

				if(jCIFObject.has(rsbundle.getString("CASEMAIL")))
					email = (String)jCIFObject.getString(rsbundle.getString("CASEMAIL"));
				log.info("createCustProfBasicAddRqJaxbObject:email is "+email);
				if(null!=email)
					custProfBasicType.setBusEmailAddr(email);

				custProfBasicType.setSolicitableCode("1"); //ALWAYS HARCODED
				if(jCIFObject.has(rsbundle.getString("CASCUSCLASSIFICATION")))
					custClassification = (String)jCIFObject.getString(rsbundle.getString("CASCUSCLASSIFICATION"));
				log.info("createCustProfBasicAddRqJaxbObject:custClassification is "+custClassification);
				if(null!=custClassification)
					custProfBasicType.setCustClassCode(custClassification);
				if(null!=custClassification &&  custClassification.equalsIgnoreCase("0"))
				{
					if(jCIFObject.has(rsbundle.getString("CASTAXID")))
						taxId = (String)jCIFObject.getString(rsbundle.getString("CASTAXID"));			
					log.info("createCustProfBasicAddRqJaxbObject:taxId is "+taxId);
					custProfBasicType.setTaxId(taxId);
				}

				if(jCIFObject.has(rsbundle.getString("CASLANGUAGE")))
					language = (String)jCIFObject.getString(rsbundle.getString("CASLANGUAGE"));
				log.info("createCustProfBasicAddRqJaxbObject:language is "+language);
				if(null!=language)
					custProfBasicType.setLanguage(language);
				if(jCIFObject.has(rsbundle.getString("CASCITIZEN")))
					citizen = (String)jCIFObject.getString(rsbundle.getString("CASCITIZEN"));
				log.info("createCustProfBasicAddRqJaxbObject:citizen is "+citizen);
				if(null!= citizen)
					custProfBasicType.setCitizenCode(citizen);

				custProfBasicType.setMoveInConfCode("1"); //Always Hardcoded

				java.util.Date current = null;
				DateCalcInqServiceImpl dateService = new DateCalcInqServiceImpl();
				CalcDateResponse dtResponse = dateService.getDateCalcInq();
				String communicatorDate = dtResponse.getDate();
				log.info("Communicator advanced date if Null is "+communicatorDate);
				SimpleDateFormat sf1 = new SimpleDateFormat(dateFormat);
				current = sf1.parse(communicatorDate);
				log.info("current advanced date if Null is "+current);



				/*todayDt  = new java.util.Date();*/

				todayCal =	Calendar.getInstance();
				todayCal.setTime(current);
				todayCal.add(Calendar.MONTH,-1);	//Service returns 1month advance
				todayDate = new Date();
				todayDate.setDay(Long.valueOf(todayCal.get(todayCal.DAY_OF_MONTH)));
				todayDate.setMonth(Long.valueOf(todayCal.get(todayCal.MONTH)+1));
				todayDate.setYear(Long.valueOf(todayCal.get(todayCal.YEAR)));
				custProfBasicType.setMoveInDt(todayDate);				
				custProfBasicType.setOpenDt(todayDate);


				if(jCIFObject.has(rsbundle.getString("CASPROFESSION")))
					profession = (String)jCIFObject.getString(rsbundle.getString("CASPROFESSION"));
				log.info("createCustProfBasicAddRqJaxbObject:profession is "+profession);
				if(null!=profession)
					custProfBasicType.setProfessionCode(profession);

				empIncomeAmount = new  CurrencyAmount();
				if(jCIFObject.has(rsbundle.getString("CASINCOME")))
					income =	(Double) Double.valueOf(jCIFObject.getString(rsbundle.getString("CASINCOME")));
				log.info("createCustProfBasicAddRqJaxbObject:empIncomeAmount is "+income);
				if(null!=income)
					empIncomeAmount.setAmt(getBigDecimalWithDecimalPoint(income));
				//empIncomeAmount.setCurCode(currencyCode);

				custProfBasicType.setEmpIncomeAmt(empIncomeAmount);
				if(jCIFObject.has(rsbundle.getString("CASSOURCEOFINCOME")))
					sourceOfIncome = (String)jCIFObject.getString(rsbundle.getString("CASSOURCEOFINCOME"));
				log.info("createCustProfBasicAddRqJaxbObject:sourceOfIncome is "+sourceOfIncome);
				if(null!=sourceOfIncome)
					custProfBasicType.setEmpIncomeSource(sourceOfIncome);
				if(jCIFObject.has(rsbundle.getString("CASRESPONSIVENESS")))
					responsiveness = (String)jCIFObject.getString(rsbundle.getString("CASRESPONSIVENESS"));
				log.info("createCustProfBasicAddRqJaxbObject:responsiveness is "+responsiveness);
				if(null!=responsiveness)
					custProfBasicType.setResponsivenessCode(responsiveness);
				if(jCIFObject.has(rsbundle.getString("CASCUSTOMERTYPE")))
					customerType = (String)jCIFObject.getString(rsbundle.getString("CASCUSTOMERTYPE"));		
				log.info("createCustProfBasicAddRqJaxbObject:customerType is "+customerType);
				if(null!=customerType && customerType.equalsIgnoreCase("P"))
				{
					custProfBasicType.setCustType(CustTypeType.PERSONAL);
					custProfBasicType.setCustMailAcctCode("R");
					if(jCIFObject.has(rsbundle.getString("CASLIFESTAGE")))
						lifeStage = (String)jCIFObject.getString(rsbundle.getString("CASLIFESTAGE"));
					log.info("createCustProfBasicAddRqJaxbObject:lifeStage is "+lifeStage);
					if(null!= lifeStage)
						custProfBasicType.setLifeStageCode(lifeStage);
					if(jCIFObject.has(rsbundle.getString("CASNIC")))
						nic = (String)jCIFObject.getString(rsbundle.getString("CASNIC"));
					log.info("createCustProfBasicAddRqJaxbObject:nic is "+nic);
					if(null!=nic)
						custProfBasicType.setNationalId(nic);
					if(jCIFObject.has(rsbundle.getString("CASPASSPORT")))
						passport = (String)jCIFObject.getString(rsbundle.getString("CASPASSPORT"));
					log.info("createCustProfBasicAddRqJaxbObject:passport is "+passport);
					if(null!=passport)
						custProfBasicType.setPassportId(passport);
					if(jCIFObject.has(rsbundle.getString("CASGENDER")))
						gender = (String)jCIFObject.getString(rsbundle.getString("CASGENDER"));
					log.info("createCustProfBasicAddRqJaxbObject:gender is "+gender);
					if(null!=gender)
						custProfBasicType.setGenderCode(gender);
					if(jCIFObject.has(rsbundle.getString("CASDEPENDENTS")))
						dependentsStr = jCIFObject.getString(rsbundle.getString("CASDEPENDENTS"));
					log.info("createCustProfBasicAddRqJaxbObject:dependentsStr is "+dependentsStr);
					if(null!=dependentsStr)
					{
						dependents = (Integer)Integer.valueOf(dependentsStr);
						custProfBasicType.setDependentsNum(new BigInteger(String.valueOf(dependents)));
					}
					if(jCIFObject.has(rsbundle.getString("CASMARTIALSTATUS")))
						maritalStatus = (String)jCIFObject.getString(rsbundle.getString("CASMARTIALSTATUS"));
					log.info("createCustProfBasicAddRqJaxbObject:maritalStatus is "+maritalStatus);
					if(null!=maritalStatus)
						custProfBasicType.setMaritalStatusCode(maritalStatus);
				}
				else if(null!=customerType && customerType.equalsIgnoreCase("N"))
				{
					custProfBasicType.setCustType(CustTypeType.BUSINESS);
					if(jCIFObject.has(rsbundle.getString("CASACCOUNTTYPE")))
						accountType = (String)jCIFObject.getString(rsbundle.getString("CASACCOUNTTYPE"));
					log.info("createCustProfBasicAddRqJaxbObject:accountType is "+accountType);
					if(null!=accountType)
						custProfBasicType.setCustMailAcctCode(accountType);
					custProfBasicType.setLifeStageCode("N/A");
					custProfBasicType.setMaritalStatusCode("N");
				}
				if(jCIFObject.has(rsbundle.getString("CASCOMPANYCIF")))
					cifId = jCIFObject.getString(rsbundle.getString("CASCOMPANYCIF"));
				log.info("modifyCustProfBasicAddRqJaxbObject:CIF is "+cifId);
				if(null!=dependentsStr)
				{
					dependents = (Integer)Integer.valueOf(dependentsStr);
					custProfBasicType.setDependentsNum(new BigInteger(String.valueOf(dependents)));
				}
				OldDataType oldxml = new OldDataType();
				
				custProfBasicType.setSecondaryOfficerCode("NO");
				cifBasicMod.setCustProfBasic(custProfBasicType);
				oldxml.setCustProfBasic(custProfBasicType);
				cifBasicMod.setOldData(oldxml);
				inputXML = generateInputXML(cifBasicMod);
				constantXML = getConstantXML();
				String messageRquid = null;
				messageRquid = GenerateUUID.getUUID();
				String cifSvcRq = "<CIFSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</CIFSvcRq></IFX>";
				inputRq = constantXML+cifSvcRq;
				log.info("createCustProfBasicAddRqJaxbObject:inputRq is "+inputRq);
				System.out.println(inputRq);
			}

		}
		catch(ParseException e)
		{
			e.printStackTrace();
			log.error("createCustProfBasicAddRqJaxbObject: ParseException Occured "+e.fillInStackTrace());
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createCustProfBasicAddRqJaxbObject: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			cifBasicMod = null;
			rquId = null;
			custProfBasicType = null;
			serviceRquid = null;	
			inputXML=null;constantXML=null;branchCode =null;shortname=null;customerType=null;accountType=null;lifeStage=null;
			primaryOfficerCode=null;source=null;prefCustomer=null;title=null;initials=null;name=null;surname=null;
			postCode=null;countryCode=null;sourceOfData=null;accomodation=null;mobile=null;phoneDay=null;phoneEve=null;
			phoneType=null;email=null;nic=null;passport=null;custClassification=null;taxId=null;gender=null;language=null;
			citizen=null;maritalStatus=null;profession=null;sourceOfIncome=null;responsiveness=null;
			address1=null;address2=null;address3=null;address4=null;address5=null;
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
	public CustomerProfileAddRs invokeCustModifyComm(String inputXML)
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CustProfBasicAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CustProfBasicAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String outputXML = null;
		CustomerProfileAddRs custResponse = null;
		custResponse = new CustomerProfileAddRs();
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			CustProfBasicModRs cifModRes =  (CustProfBasicModRs) generateOutputObject(removedIFX,CustProfBasicModRs.class);
			if(null!=cifModRes)
			{
				statusCode = cifModRes.getStatus().getStatusCode();
				if(null!=statusCode && statusCode==0)
				{
					cifModRes.getStatus().setStatusDesc(cifModRes.getStatus().getStatusDesc());
				}
				else
				{
					custResponse.setErrorCode(cifModRes.getStatus().getError().get(0).getErrNum());
					custResponse.setErrorDescription(cifModRes.getStatus().getError().get(0).getErrDesc());
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

		return custResponse;
	}

	/*public CustProfBasicType  getOldData(String cif) throws JAXBException,Exception
	{
		CustProfBasicType oldData = null;
		CustProfBasicInqServiceImpl cpil = new CustProfBasicInqServiceImpl();
		String ipXML = cpil.createCustProfBasicInqRqJaxbObject(cif);

		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		String outputXML = InvokeCommunicatorUtil.invokeCommunicator(ipXML, fiServUrl,certificatePath,certificatePwd);
		
		String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
		CustProfBasicInqRs custProfBasicInqRsObj =  (CustProfBasicInqRs) generateOutputObject(removedIFX,CustProfBasicInqRs.class);
		
		//oldData= custProfBasicInqRsObj.getCustProfBasic();
		BeanUtils.copyProperties(oldData, custProfBasicInqRsObj.getCustProfBasic());

		return oldData;

	}*/
	public static void main(String[] args) {
		//JSONObject json=  '{"CAS_CustomerMainType":"P","CAS_BID":"1","CAS_CusTitle":"MR","CAS_StartDate":"06/02/1998","CAS_Mobile":"1477769","CAS_EmployerName":"XYZHBC HPYZA","CAS_FullNameEntityName":"TUSHAR SAFAYA","CAS_Income":"600000.0","CAS_PostCode":"18005","CAS_Initials":"T","CAS_MaritalStatus":"S","CAS_PassportNo":"PXYZABIZBV","CAS_Address1":"#717 24TH CROSS KSLAYOUT","CAS_Gender":"M","CAS_PhoneNumber":"0185432407","CAS_CompanyAddress1":"SAHE AS ABOVE","CAS_DateOfBirth":"16/02/1999","CAS_PEP":"true","CAS_Email":"tusHAR.SAFAYA@ANPL.CO","CAS_SurnameShortEntityName":"SAFAYA","CAS_Citizenship":"INOJAN","CAS_TaxId":"1254321014","CAS_NICNo":"111111109V","CAS_Accommodation":"Rented or Leased"}';
		System.out.println("1");
		CifModifyServiceImpl cif =  new CifModifyServiceImpl();
		String inputXML = "<IFX><SignonRq><SignonPswd><SignonRole>CSR</SignonRole><CustId><SPName>FiservICBS</SPName><CustLoginId>SGDMSID</CustLoginId></CustId><CustPswd><CryptType>NONE</CryptType><Pswd>sg78@789</Pswd></CustPswd><GenSessKey>0</GenSessKey></SignonPswd><ClientDt><Year>2016</Year><Month>8</Month><Day>30</Day></ClientDt><ClientApp><Org>Fiserv</Org><Name>DM</Name><Version>0.1</Version><ClientAppKey>BOCSRVRHECGNSYSQCUWRJDHKRFDBNTGN</ClientAppKey></ClientApp><ComputerId>SGPVMAPRDEV03</ComputerId><InstitutionCode>BOCSR</InstitutionCode></SignonRq><EnvironmentInfo><EnvironmentName>default</EnvironmentName></EnvironmentInfo><LoanSvcRq><RqUID>ac5e457a-becf-4ff2-97e6-442e7fa6b780</RqUID><SPName>FiservICBS</SPName><LoanAcctAddRq><RqUID>fb0e077b-f08c-449f-bd97-a6dfcc23bddb</RqUID><LoanAcctInfo><CustRelation><RelationCode>SOW</RelationCode><CustPermId>0005048693</CustPermId></CustRelation><LoanAcctBasic><ApplicationInfo><ApplicationId>100010000070</ApplicationId><ApplicationSeq>00070</ApplicationSeq></ApplicationInfo><ProdId>1</ProdId><BranchId>1</BranchId><PrimOfficerCode>WEB</PrimOfficerCode><NoteTypeCode>Term</NoteTypeCode><OpenAmt><Amt>1234567.00</Amt><CurCode>000</CurCode></OpenAmt><IntGuarantyCode>RateChgWithIndexChg</IntGuarantyCode><IntRate>10</IntRate><FirstPmtDt><Year>2016</Year><Month>7</Month><Day>26</Day></FirstPmtDt><Term><Count>120</Count><TermUnits>Months</TermUnits></Term><BillCode>TransferOrder</BillCode><FDICCode>1</FDICCode><FRBCode>102</FRBCode><LTVTypeCode>Original</LTVTypeCode></LoanAcctBasic><LoanAcctDetail><CensusTractCode>101</CensusTractCode><MSACode>1</MSACode><LoanApprovalCode>Y</LoanApprovalCode><RiskCode>A</RiskCode><AvailToDisbCalc>0</AvailToDisbCalc></LoanAcctDetail><VariableRateInfo><IndexId>1</IndexId></VariableRateInfo><PmtSched><PmtSchedUseNum>119</PmtSchedUseNum><PmtFreq>M00126</PmtFreq><PmtSchedNextPmtDt><Year>2016</Year><Month>7</Month><Day>26</Day></PmtSchedNextPmtDt><PmtSchedTypeCode>PrincipalAndInt</PmtSchedTypeCode><PmtSchedAmt><Amt>16314.889999999999417923390865325927734375</Amt><CurCode>000</CurCode></PmtSchedAmt><IntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></IntBearingEscrowAmt><NonIntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></NonIntBearingEscrowAmt></PmtSched><PmtSched><PmtSchedUseNum>1</PmtSchedUseNum><PmtFreq>M00126</PmtFreq><PmtSchedNextPmtDt><Year>2026</Year><Month>6</Month><Day>26</Day></PmtSchedNextPmtDt><PmtSchedTypeCode>PrincipalAndInt</PmtSchedTypeCode><PmtSchedAmt><Amt>999999999999999.99</Amt><CurCode>000</CurCode></PmtSchedAmt><IntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></IntBearingEscrowAmt><NonIntBearingEscrowAmt><Amt>0.00</Amt><CurCode>000</CurCode></NonIntBearingEscrowAmt></PmtSched><CollectFromAcct><AnchorProfile>187</AnchorProfile><TargetProfile>115</TargetProfile><DepAcctIdFrom><AcctId>4313867</AcctId><AcctType>SV</AcctType><BankInfo><BankId>1</BankId></BankInfo></DepAcctIdFrom><ReferenceId>0001000012016000265</ReferenceId><Description>Ln Recovery</Description><CurAmt><Amt>16314.889999999999417923390865325927734375</Amt><CurCode>000</CurCode></CurAmt><Freq>M00126</Freq><StartDt><Year>2016</Year><Month>7</Month><Day>26</Day></StartDt></CollectFromAcct><CollateralInfo><CollateralId>000002117491</CollateralId><CollateralPledge><PledgeRule>FaceAmount</PledgeRule><MaxPledgeAmt><Amt>15555.00</Amt><CurCode>000</CurCode></MaxPledgeAmt></CollateralPledge></CollateralInfo></LoanAcctInfo></LoanAcctAddRq></LoanSvcRq></IFX>";
		cif.invokeCustModifyComm(inputXML);
		
	}

}


