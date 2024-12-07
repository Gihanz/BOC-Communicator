

package com.boc.fiserv.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.CIFSvc.CustEmpAdd.rq.CurrencyAmount;
import com.boc.CIFSvc.CustEmpAdd.rq.CustEmpAddRq;
import com.boc.CIFSvc.CustEmpAdd.rq.CustEmpAddRq.RqUID;
import com.boc.CIFSvc.CustEmpAdd.rq.CustIdType;
import com.boc.CIFSvc.CustEmpAdd.rq.Date;
import com.boc.CIFSvc.CustEmpAdd.rq.EmpAddrCode;
import com.boc.CIFSvc.CustEmpAdd.rq.EmpAddrType;
import com.boc.CIFSvc.CustEmpAdd.rq.EmpTypeType;
import com.boc.CIFSvc.CustEmpAdd.rq.EmploymentType;
import com.boc.CIFSvc.CustEmpAdd.rq.PhoneNumType;
import com.boc.CIFSvc.CustEmpAdd.rs.CustEmpAddRs;
import com.boc.fiserv.CIFEmployeeAddService;
import com.boc.fiserv.response.CIFEmployeeAddResponse;
import com.boc.fiserv.response.CIFFullNameAddResponse;
import com.boc.fiserv.response.CustomerProfileAddRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Mar 22, 2017
*/
public class CIFEmployeeAddServiceImpl extends BaseServiceImpl implements CIFEmployeeAddService 
{
	private static Logger log = LoggerFactory.getLogger(CustProfBasicAddServiceImpl.class);

	public CustomerProfileAddRs execute(JSONObject employeeDetails) throws JAXBException,Exception
	{
		CIFEmployeeAddResponse cifEmployeeAddResponse = null;
		CustomerProfileAddRs customerProfileAddRs = null;
		String inputXML = createCIFEmployeeRqJaxbObject(employeeDetails);
		if(null!=inputXML)
			customerProfileAddRs = invokeCIFEmployeeAddRq(inputXML);
		return customerProfileAddRs;
	}
	
	public CustomerProfileAddRs invokeCIFEmployeeAddRq(String inputXML)
	{
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CIFEmployeeAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CIFEmployeeAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		String outputXML = null;
		CIFEmployeeAddResponse cifEmployeeAddResponse = null;
		cifEmployeeAddResponse = new CIFEmployeeAddResponse();
		CustomerProfileAddRs customerProfileAddRs = null;
		customerProfileAddRs = new CustomerProfileAddRs();
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			CustEmpAddRs custEmpAddRsObj= (CustEmpAddRs)generateOutputObject(removedIFX,CustEmpAddRs.class);
			if(null!=custEmpAddRsObj)
			{
				statusCode = custEmpAddRsObj.getStatus().getStatusCode();
				if(null!=statusCode && statusCode==0)
				{
					customerProfileAddRs.setStatusCode(custEmpAddRsObj.getStatus().getStatusCode());
					customerProfileAddRs.setStatusDescription(custEmpAddRsObj.getStatus().getStatusDesc());
				}
				else
				{
					customerProfileAddRs.setErrorCode(custEmpAddRsObj.getStatus().getError().get(0).getErrNum());
					customerProfileAddRs.setErrorCode(custEmpAddRsObj.getStatus().getError().get(0).getErrDesc());
				}
			}
			
		}
		 catch (MalformedURLException e) {
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
	
	public String createCIFEmployeeRqJaxbObject(JSONObject employeeDetails) throws JAXBException,Exception
	{
		
		ResourceBundle rsbundle = getResourceBundle();
		CustEmpAddRq custEmpAddRq = null;
		CustIdType custIdType = null;
		EmploymentType employmentType = null;
		EmpAddrType empAddrType = null;
		RqUID rquId = null;
		rquId = new RqUID();
		custEmpAddRq = new CustEmpAddRq();
		custIdType = new CustIdType();
		employmentType = new EmploymentType();
		empAddrType = new EmpAddrType();
		String serviceRquid = null,cifId=null,employerName=null,companyAddress1=null,companyAddress2=null,companyAddress3=null,companyAddress4=null;
		String companyAddress5=null,companyPostCode=null,startDateStr=null,businessEmail=null,phoneType=null,phoneDay=null;
		String inputXML=null,constantXML=null,inputRq=null,jobTitle=null;
		Calendar startCal = null;
		Date startCalDate=null;
		Double income = null;
		CurrencyAmount empIncomeAmount= null;
		List<PhoneNumType> phoneNumLst= null;
		try
		{
			//JSONObject jCIFObject = new JSONObject(employeeDetails);
			JSONObject jCIFObject = employeeDetails;
			serviceRquid = GenerateUUID.getUUID();
			rquId.setValue(serviceRquid);
			custEmpAddRq.setRqUID(rquId);
			if(jCIFObject.has(rsbundle.getString("CASCIFID")))
				cifId = (String) jCIFObject.getString(rsbundle.getString("CASCIFID"));
			log.info("createCIFEmployeeRqJaxbObject:cifId is "+cifId);
			if(null!=cifId)
			{
				custIdType.setCustPermId(cifId);
				custEmpAddRq.setCustId(custIdType);
			}
			if(jCIFObject.has(rsbundle.getString("CASEMPLOYERNAME")))
				employerName = (String) jCIFObject.getString(rsbundle.getString("CASEMPLOYERNAME"));
			log.info("createCIFEmployeeRqJaxbObject:employerName is "+employerName);
			if(jCIFObject.has(rsbundle.getString("CASCOMPANYADDRESS1")))
				companyAddress1 = (String) jCIFObject.getString(rsbundle.getString("CASCOMPANYADDRESS1"));
			log.info("createCIFEmployeeRqJaxbObject:companyAddress1 is "+companyAddress1);
			if(jCIFObject.has(rsbundle.getString("CASCOMPANYADDRESS2")))
				companyAddress2 = (String) jCIFObject.getString(rsbundle.getString("CASCOMPANYADDRESS2"));
			log.info("createCIFEmployeeRqJaxbObject:companyAddress2 is "+companyAddress2);
			if(jCIFObject.has(rsbundle.getString("CASCOMPANYADDRESS3")))
				companyAddress3 = (String) jCIFObject.getString(rsbundle.getString("CASCOMPANYADDRESS3"));
			log.info("createCIFEmployeeRqJaxbObject:companyAddress3 is "+companyAddress3);
			if(jCIFObject.has(rsbundle.getString("CASCOMPANYADDRESS4")))
				companyAddress4 = (String) jCIFObject.getString(rsbundle.getString("CASCOMPANYADDRESS4"));
			log.info("createCIFEmployeeRqJaxbObject:companyAddress4 is "+companyAddress4);
			if(jCIFObject.has(rsbundle.getString("CASCOMPANYADDRESS5")))
				companyAddress5 = (String) jCIFObject.getString(rsbundle.getString("CASCOMPANYADDRESS5"));
			log.info("createCIFEmployeeRqJaxbObject:companyAddress5 is "+companyAddress5);
			if(jCIFObject.has(rsbundle.getString("CASCOMPANYPOSTCODE")))
				companyPostCode = (String) jCIFObject.getString(rsbundle.getString("CASCOMPANYPOSTCODE"));
			log.info("createCIFEmployeeRqJaxbObject:companyPostCode is "+companyPostCode);
			empAddrType.setAddrCode(EmpAddrCode.EMPLOYMENT);
			if(null!=employerName)
				empAddrType.setFullName(employerName);
			if(null!=companyAddress1)
				empAddrType.setAddr1(companyAddress1);
			if(null!=companyAddress2)
				empAddrType.setAddr2(companyAddress2);
			if(null!=companyAddress3)
				empAddrType.setAddr3(companyAddress3);
			if(null!=companyAddress4)
				empAddrType.setAddr4(companyAddress4);
			if(null!=companyAddress5)
				empAddrType.setAddr5(companyAddress5);
			if(null!=companyPostCode)
				empAddrType.setPostalCode(companyPostCode);
			employmentType.setEmpAddr(empAddrType);
			if(jCIFObject.has(rsbundle.getString("CASSTARTDATE")))
				startDateStr = (String) jCIFObject.getString(rsbundle.getString("CASSTARTDATE"));
			log.info("createCIFEmployeeRqJaxbObject:startDateStr is "+startDateStr);
			if(null!=startDateStr)
			{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",new Locale("es,ES"));
			java.util.Date startDate = df.parse(startDateStr);
			
			startCal =	Calendar.getInstance();
			startCal.setTime(startDate);
			startCalDate = new Date();
			startCalDate.setDay(Long.valueOf(startCal.get(startCal.DAY_OF_MONTH)));
			startCalDate.setMonth(Long.valueOf(startCal.get(startCal.MONTH)+1));
			startCalDate.setYear(Long.valueOf(startCal.get(startCal.YEAR)));
			employmentType.setEmpStartDt(startCalDate);
			}
			
			empIncomeAmount = new  CurrencyAmount();
			if(jCIFObject.has(rsbundle.getString("CASINCOME")))
				income =	(Double) Double.valueOf(jCIFObject.getString(rsbundle.getString("CASINCOME")));
			log.info("createCIFEmployeeRqJaxbObject:empIncomeAmount is "+empIncomeAmount);
			if(null!=income)
				empIncomeAmount.setAmt(getBigDecimalWithDecimalPoint(income));
			employmentType.setEmpIncomeAmt(empIncomeAmount);
			if(jCIFObject.has(rsbundle.getString("CASBUSINESSEMAIL")))
				businessEmail = (String) jCIFObject.getString(rsbundle.getString("CASBUSINESSEMAIL"));
			log.info("createCIFEmployeeRqJaxbObject:businessEmail is "+businessEmail);
			if(null!=businessEmail)
				employmentType.setBusEmailAddr(businessEmail);
			if(jCIFObject.has(rsbundle.getString("CASPHONEDAY")))
				phoneDay = (String)jCIFObject.getString(rsbundle.getString("CASPHONEDAY"));
			log.info("createCIFEmployeeRqJaxbObject:phoneDay is "+phoneDay);
			phoneNumLst = new ArrayList<PhoneNumType>();
			PhoneNumType phoneTypeObj= null;
			phoneTypeObj = new PhoneNumType();
			if(null!=phoneDay)
			{
				phoneType = (String)rsbundle.getString("BUSINESSPHONE");
				phoneTypeObj.setPhoneType(phoneType);
				phoneTypeObj.setPhone(phoneDay);
				phoneNumLst.add(phoneTypeObj);
			}
			employmentType.getPhoneNum().addAll(phoneNumLst);

			employmentType.setEmpType(EmpTypeType.PRIMARY);
			//employmentType.setEmpPhoneAvailCode(value);
			if(jCIFObject.has(rsbundle.getString("CASJOBTITLE")))
				jobTitle = (String) jCIFObject.getString(rsbundle.getString("CASJOBTITLE"));
			log.info("createCIFEmployeeRqJaxbObject:jobTitle is "+jobTitle);
			
			employmentType.setJobTitleType(jobTitle);
			
			
			
			custEmpAddRq.setEmployment(employmentType);
			inputXML = generateInputXML(custEmpAddRq);
			constantXML = getConstantXML();
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<CIFSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</CIFSvcRq></IFX>";
			inputRq = constantXML+cifSvcRq;
			log.info("createCIFEmployeeRqJaxbObject:inputRq is "+inputRq);
			System.out.println(inputRq);
			
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			log.error("createCIFEmployeeRqJaxbObject:inputRq: ParseException Occured "+e.fillInStackTrace());
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createCIFEmployeeRqJaxbObject: Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			custEmpAddRq = null;
			custIdType = null;
			employmentType = null;
			empAddrType = null;
			rquId = null;
			serviceRquid = null;cifId=null;employerName=null;companyAddress1=null;companyAddress2=null;companyAddress3=null;companyAddress4=null;
			companyAddress5=null;companyPostCode=null;startDateStr=null;businessEmail=null;phoneType=null;phoneDay=null;
			inputXML=null;constantXML=null;
			startCal = null;
			startCalDate=null;
			income = null;
			empIncomeAmount= null;
			phoneNumLst= null;
		}
		
		return inputRq;
	}
	
}
