package com.boc.fiserv.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.CIFSvc.CustProfBasicInq.rq.CustIdType;
import com.boc.CIFSvc.CustProfBasicInq.rq.CustProfBasicInqRq;
import com.boc.CIFSvc.CustProfBasicInq.rq.SPNameType;
import com.boc.CIFSvc.CustProfBasicInq.rq.CustProfBasicInqRq.RqUID;
import com.boc.CIFSvc.CustProfBasicInq.rs.CustProfBasicInqRs;
import com.boc.CIFSvc.CustProfBasicInq.rs.CustProfBasicType;
import com.boc.CIFSvc.CustProfBasicInq.rs.PhoneNumType;
import com.boc.connector.CMConnector;
import com.boc.fiserv.CustProfBasicInqService;
import com.boc.fiserv.response.CustomerProfileRs;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;
import com.boc.utils.XmlUtils2;

public class CustProfBasicInqServiceImpl  extends BaseServiceImpl implements CustProfBasicInqService
{
	//public static Logger log = Logger.getLogger("com.boc.fiserv.impl.CustProfBasicInqServiceImpl");
	private static Logger log = LoggerFactory.getLogger(CustProfBasicInqServiceImpl.class);
	public static void main(String a[])
	{
		CustProfBasicInqServiceImpl impl = new CustProfBasicInqServiceImpl();
		try 
		{
			impl.execute("12345","00002","00001","23476534","Create Loan Account","Create Loan Account Response","com.boc.fiserv.impl.CustProfBasicInqServiceImpl");
			//impl.createCustProfBasicInqRqJaxbObject("0004503808");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String execute(String referenceNumber,String branchCode, String productCode,String wobNumber,String subMapName,String activityName,String className) throws JAXBException,Exception
	{
		String accountNo = getAccountNo(referenceNumber);
		String inputXML = createCustProfBasicInqRqJaxbObject("0004503808",branchCode,productCode,referenceNumber,wobNumber,subMapName,activityName,className);
		return inputXML;
	}
	
	public CustomerProfileRs invokeCommunicator(String inputXML) throws Exception
	{
		return invokeCustProfBasicInq(inputXML);
	}
	
	
	public String getAccountNo(String referenceNumber) throws Exception
	{
		CMConnector cmConnector = new CMConnector();
		String accountNo = cmConnector.getAccountNo(referenceNumber);
		return accountNo;
	}
	
	public String createCustProfBasicInqRqJaxbObject(String accountNo,String branchCode, String productCode,String referenceNumber,String wobNumber,String subMapName,String activityName,String className) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		//String spName = rs.getString("SPName");
		String inputRq = null;
		CustProfBasicInqRq custBasicInq = null; 
		RqUID rquId = null; 
		CustIdType custId = null;
		custBasicInq = new CustProfBasicInqRq();
		rquId = new RqUID();
		custId = new CustIdType();
		String serviceRquid = null;	
		try
		{
		serviceRquid = GenerateUUID.getUUID();
		rquId.setValue(serviceRquid);
		custBasicInq.setRqUID(rquId);
		custId.setCustPermId(accountNo);
		custId.setSPName(SPNameType.FISERV_ICBS);
		custBasicInq.setCustId(custId);
		
		String inputXML = generateInputXML(custBasicInq);
		String constantXML = getConstantXML();
		
		String messageRquid = null;
		messageRquid = GenerateUUID.getUUID();
		String cifSvcRq = "<CIFSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</CIFSvcRq></IFX>";
		inputRq = constantXML+cifSvcRq;
		log.info("createCustProfBasicInqRqJaxbObject:inputRq is "+inputRq);
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createCustProfBasicInqRqJaxbObject Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
	/*	ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		MessageService messageService = (MessageService) ctx.getBean("messageServiceImpl");
		messageService.saveRequestMesgTransactions(branchCode,productCode, referenceNumber,wobNumber,subMapName,activityName,className,inputRq);
	*/	
		return inputRq;
	}
	
	public String createCustProfBasicInqRqJaxbObject(String accountNo) throws JAXBException,Exception
	{
		ResourceBundle rsbundle = getResourceBundle();
		//String spName = rs.getString("SPName");
		String inputRq = null;
		CustProfBasicInqRq custBasicInq = null; 
		RqUID rquId = null; 
		CustIdType custId = null;
		custBasicInq = new CustProfBasicInqRq();
		rquId = new RqUID();
		custId = new CustIdType();
		String serviceRquid = null;	
		try
		{
		serviceRquid = GenerateUUID.getUUID();
		rquId.setValue(serviceRquid);
		custBasicInq.setRqUID(rquId);
		custId.setCustPermId(accountNo);
		custId.setSPName(SPNameType.FISERV_ICBS);
		custBasicInq.setCustId(custId);
		
		String inputXML = generateInputXML(custBasicInq);
		String constantXML = getConstantXML();
		
		String messageRquid = null;
		messageRquid = GenerateUUID.getUUID();
		String cifSvcRq = "<CIFSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</CIFSvcRq></IFX>";
		inputRq = constantXML+cifSvcRq;
		log.info("createCustProfBasicInqRqJaxbObject:inputRq is "+inputRq);
		
		/*ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		MessageService messageService = (MessageService) ctx.getBean("messageServiceImpl");
		messageService.saveInputXML("00002", "00001", "23476534","","","",inputRq);*/
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("createCustProfBasicInqRqJaxbObject Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return inputRq;
	}
	
	public CustomerProfileRs invokeCustProfBasicInq(String inputXML) throws Exception
	{
		CustomerProfileRs custResponse = null;
		try
		{
		ResourceBundle rsbundle = getResourceBundle();
		String dateFormat = rsbundle.getString("dateFormat");
		String phoneType = rsbundle.getString("RESIDENCEPHONE");
		String mobilePhoneType = rsbundle.getString("MOBILEPHONE");
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("CustProfBasicInqServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
		//String outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl);
		String startStatusCode = rsbundle.getString("IFXStatusCode");
		String endStatusCode =  rsbundle.getString("IFXEndStatusCode");
		String startStatusDesc = rsbundle.getString("IFXStatusDescription");
		String endStatusDesc = rsbundle.getString("IFXEndStatusDescription");
		Long statusCode = null;
		String statusDescription=null;
		HashMap statusMap = null;
		
		String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
		CustProfBasicInqRs custProfBasicInqRsObj =  (CustProfBasicInqRs) generateOutputObject(removedIFX,CustProfBasicInqRs.class);
		if(null!=custProfBasicInqRsObj)
		{
			statusCode = custProfBasicInqRsObj.getStatus().getStatusCode();
			if(null!=statusCode && statusCode==0)
			{
			custResponse = getCustProfBasicInq(custProfBasicInqRsObj,dateFormat,phoneType,mobilePhoneType);
			}
			else
			{
				custResponse = new CustomerProfileRs();
				custResponse.setErrorCode(custProfBasicInqRsObj.getStatus().getError().get(0).getErrNum());
				custResponse.setErrorDescription(custProfBasicInqRsObj.getStatus().getError().get(0).getErrDesc());
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
			CustProfBasicInqRs custProfBasicInqRsObj =  (CustProfBasicInqRs) generateOutputObject(removedIFX,CustProfBasicInqRs.class);
			if(null!=custProfBasicInqRsObj)
			{
				custResponse = getCustProfBasicInq(custProfBasicInqRsObj,dateFormat,phoneType,mobilePhoneType);
			}
		}
		else
		{
			custResponse = new CustomerProfileRs();
			custResponse.setErrorCode(statusCode);
			custResponse.setErrorDescription(statusDescription);
		}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("invokeCustProfBasicInq Exception Occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return custResponse;
	}
	
	public CustomerProfileRs getCustProfBasicInq(CustProfBasicInqRs custProfBasicInqRs,String dateFormat,String phoneType,String mobilePhoneType ) throws Exception
	{
		CustomerProfileRs custResponse = null;
		custResponse = new CustomerProfileRs();
		try
		{
			CustProfBasicType customerProfile = custProfBasicInqRs.getCustProfBasic();
			if(null!=customerProfile.getCustType())
					custResponse.setCustomerType(customerProfile.getCustType().value());
			if(null!=customerProfile.getShortName())
				custResponse.setShortName(customerProfile.getShortName());
			if(null!=customerProfile.getCustAddr() && null!=customerProfile.getCustAddr().getFullName())
				custResponse.setName(customerProfile.getCustAddr().getFullName());
			if(null!=customerProfile.getContactPrefText())
				custResponse.setFullName(customerProfile.getContactPrefText());
			if(null != customerProfile.getNationalId() && customerProfile.getNationalId()!="")
				custResponse.setNationalId(customerProfile.getNationalId());
			else
				custResponse.setNationalId(customerProfile.getPassportId());
			
			if(null!=customerProfile.getBirthDt())
			{
				long custYear = customerProfile.getBirthDt().getYear();
				long custMonth = customerProfile.getBirthDt().getMonth();
				//#added by Vikshith : for month, 0 is considered as JAN and 11 is considered as DEC so decreasing the custMonth by 1.
				//custMonth = custMonth-1; //#Commented by Saimadan, same logic is implemented below
				long custDay = customerProfile.getBirthDt().getDay();
				
				if(0 != custYear && 0 != custMonth && 0 != custDay)
				{
					Calendar cal1 = Calendar.getInstance();
					cal1.set(cal1.DAY_OF_MONTH, (int) custDay);
					cal1.set(cal1.MONTH, (int) (custMonth-1));
					cal1.set(cal1.YEAR, (int) custYear);
					//cal1.set((int)custYear, (int)custMonth, (int)custDay);
					Date custDOB = cal1.getTime();
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);			   
				    log.info("createCustProfBasicInqRqJaxbObject:custDOB is "+custDOB);
					String dob = simpleDateFormat.format(custDOB);
					log.info("createCustProfBasicInqRqJaxbObject:dobDate is "+dob);
					Date dobDate = simpleDateFormat.parse(dob);
					log.info("createCustProfBasicInqRqJaxbObject:dobDate is "+dobDate);
					int age  = getAge(custDOB);
					custResponse.setDob(dob);
					custResponse.setAge(age);
				}
			}
			if(null!=customerProfile.getMaritalStatusCode())
				custResponse.setMartialStatus(customerProfile.getMaritalStatusCode());
			if(null!=customerProfile.getDependentsNum())
				custResponse.setDependents(customerProfile.getDependentsNum());
			System.out.println("***Addr5 is "+customerProfile.getCustAddr().getAddr5());
			System.out.println("***Addr4 is "+customerProfile.getCustAddr().getAddr4());
			String address="";
			if(null!=customerProfile.getCustAddr() && null!=customerProfile.getCustAddr().getAddr1() && customerProfile.getCustAddr().getAddr1().length()>1)
				address=customerProfile.getCustAddr().getAddr1();
			if(null!=customerProfile.getCustAddr() && null!=customerProfile.getCustAddr().getAddr2() && customerProfile.getCustAddr().getAddr2().length()>1)
				address=address+","+customerProfile.getCustAddr().getAddr2();
			if(null!=customerProfile.getCustAddr() && null!=customerProfile.getCustAddr().getAddr3() && customerProfile.getCustAddr().getAddr3().length()>1)
			{
				System.out.println("Addr3 is "+customerProfile.getCustAddr().getAddr3());
				address=address+","+customerProfile.getCustAddr().getAddr3();
			}
			if(null!=customerProfile.getCustAddr() && null!=customerProfile.getCustAddr().getAddr4() && customerProfile.getCustAddr().getAddr4().length()>1)
			{
				System.out.println("Addr4 is "+customerProfile.getCustAddr().getAddr4());
				address=address+","+customerProfile.getCustAddr().getAddr4();
			}
			if(null!=customerProfile.getCustAddr() && null!=customerProfile.getCustAddr().getAddr5() && customerProfile.getCustAddr().getAddr5().length()>1)
			{
				System.out.println("Addr5 is "+customerProfile.getCustAddr().getAddr5());
				address=address+","+customerProfile.getCustAddr().getAddr5();
			}
			
			//address=customerProfile.getCustAddr().getAddr1()+customerProfile.getCustAddr().getAddr2()+customerProfile.getCustAddr().getAddr3()+
					//customerProfile.getCustAddr().getAddr4()+customerProfile.getCustAddr().getAddr5();
			custResponse.setAddress(address);
			if(null!=customerProfile.getPhoneNum())
			{
				List<PhoneNumType> phoneTypeLst =  customerProfile.getPhoneNum();
				if(null!=phoneTypeLst)
				{
					for(PhoneNumType phoneNumType:phoneTypeLst)
					{
						String custphoneType = phoneNumType.getPhoneType();
						if(phoneType.equalsIgnoreCase(custphoneType))
							custResponse.setResidencePhone(phoneNumType.getPhone());
						else if(mobilePhoneType.equalsIgnoreCase(custphoneType))
							custResponse.setMobilePhone(phoneNumType.getPhone());
					}
				}
			}
			if(null!=customerProfile.getEmailAddr())
				custResponse.setEmailAddress(customerProfile.getEmailAddr());
			
			//#Added by vikshith : ""!= customerProfile.getTaxInfoCode() this code was always evaluating to true so changed to customerProfile.getTaxInfoCode().isEmpty() 
			if(null!=customerProfile.getTaxInfoCode() && !customerProfile.getTaxInfoCode().isEmpty())
			{
				custResponse.setTaxFileNo(customerProfile.getTaxInfoCode());
				custResponse.setTaxFileCheck("Y");
			}
			else
			{
				custResponse.setTaxFileCheck("N");
			}
			if(null!=customerProfile.getGenderCode())
				custResponse.setGenderCode(customerProfile.getGenderCode());
			if(null!=customerProfile.getCitizenCode())
				custResponse.setCitizencode(customerProfile.getCitizenCode());
			if(null!=customerProfile.getSalutation())
				custResponse.setSaluation(customerProfile.getSalutation());
			if(null!=customerProfile.getPassportId())
				custResponse.setPassportId(customerProfile.getPassportId());
			if(null!=customerProfile.getCustClassCode())
				custResponse.setCustClassCode(customerProfile.getCustClassCode());
			if(null!=customerProfile.getLanguage())
				custResponse.setLanguage(customerProfile.getLanguage());
			if(null!=customerProfile.getSourceCode())
				custResponse.setSourceCode(customerProfile.getSourceCode());
			if(null!=customerProfile.getLifeStageCode())
				custResponse.setLifeStageCode(customerProfile.getLifeStageCode());
			if(null!=customerProfile.getAccomCode())
				custResponse.setAccomCode(customerProfile.getAccomCode());
			if(null!=customerProfile.getResponsivenessCode())
				custResponse.setResponsivenessCode(customerProfile.getResponsivenessCode());
			if(null!=customerProfile.getEmpIncomeAmt().getAmt())
				custResponse.setEmpIncomeAmt(customerProfile.getEmpIncomeAmt().getAmt().toString());
			if(null!=customerProfile.getEmpIncomeSource())
				custResponse.setEmpIncomeSource(customerProfile.getEmpIncomeSource());
			if(null!=customerProfile.getYearsAtJob()) 
				custResponse.setYearsAtJob(customerProfile.getYearsAtJob());
			if(null!=customerProfile.getCustAddr().getAddr1()) 
			custResponse.setAddress1(customerProfile.getCustAddr().getAddr1());
			
			if(null!=customerProfile.getCustAddr().getAddr2()) 
				custResponse.setAddress2(customerProfile.getCustAddr().getAddr2());
			
			if(null!=customerProfile.getCustAddr().getAddr3()) 
				custResponse.setAddress3(customerProfile.getCustAddr().getAddr3());
			
			if(null!=customerProfile.getCustAddr().getAddr4())
				custResponse.setAddress4(customerProfile.getCustAddr().getAddr4());
			
			if(null!=customerProfile.getCustAddr().getAddr5())
				custResponse.setAddress5(customerProfile.getCustAddr().getAddr5());
			
			if(null!=customerProfile.getBusEmailAddr())
				custResponse.setBusEmailAddr(customerProfile.getBusEmailAddr());
			if(null!=customerProfile.getEmpStatus())
				custResponse.setEmpStatus(customerProfile.getEmpStatus());
			if(null!=customerProfile.getProfessionCode())
				custResponse.setProfessionCode(customerProfile.getProfessionCode());
			if(null!=customerProfile.getCustMailAcctCode())
				custResponse.setCustMailAcctCode(customerProfile.getCustMailAcctCode());
			if(null!=customerProfile.getPreferredCustCode())
				custResponse.setPreferredCustCode(customerProfile.getPreferredCustCode());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("getCustProfBasicInq: Exception Occured  "+e.fillInStackTrace());
			throw new Exception(e);
		}
		return custResponse;
	}
	
	public int getAge(Date dateOfBirth) {
			
			int age = 0;
		    Calendar born = Calendar.getInstance();
		    Calendar now = Calendar.getInstance();
		    if(dateOfBirth!= null) {
		        now.setTime(new Date());
		        born.setTime(dateOfBirth);  
		        if(born.after(now)) {
		            throw new IllegalArgumentException("Can't be born in the future");
		        }
		        age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);             
		        if(now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR))  {
		            age-=1;
		        }
		    }  
		    log.info("createCustProfBasicInqRqJaxbObject:Age is "+age);
		    return age;
		    
		}
}
