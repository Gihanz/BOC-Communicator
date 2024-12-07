

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

import org.apache.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.CIFSvc.AddrAdd.rq.AcctTypeType;
import com.boc.CIFSvc.AddrAdd.rq.AddrAddRq;
import com.boc.CIFSvc.AddrAdd.rq.AddrAddRq.BankAcctId;
import com.boc.CIFSvc.AddrAdd.rq.AddrAddRq.RqUID;
import com.boc.CIFSvc.AddrAdd.rq.AddrType;
import com.boc.CIFSvc.AddrAdd.rq.Date;
import com.boc.CIFSvc.AddrAdd.rs.AddrAddRs;
import com.boc.fiserv.CreateAlternateAddressService;
import com.boc.fiserv.response.AltAdressAddResponse;
import com.boc.fiserv.response.CalcDateResponse;
import com.boc.utils.GenerateUUID;
import com.boc.utils.InvokeCommunicatorUtil;

/*
Created By SaiMadan on Apr 23, 2017
*/
public class CreateAlternateAddressServiceImpl extends BaseServiceImpl implements  CreateAlternateAddressService
{
	private static Logger log = LoggerFactory.getLogger(CreateAlternateAddressServiceImpl.class);
	public AltAdressAddResponse execute(JSONObject altAddressObject) throws JAXBException,Exception
	{
		AltAdressAddResponse altAdressAddResponse = null;
		String inputXML = createAltAddressJaxbObject(altAddressObject);
		if(null!=inputXML)
			altAdressAddResponse = invokeAltAddressAddRq(inputXML);
		return altAdressAddResponse;
	}
	
	
	public AltAdressAddResponse invokeAltAddressAddRq(String inputXML) throws Exception
	{
		
		ResourceBundle rsbundle = getResourceBundle();
		String fiServUrl  = rsbundle.getString("FISERV_URL");
		String startIndexStr = rsbundle.getString("AltAddressAddServiceImpl.truncateStartStr");
		String endIndexStr = rsbundle.getString("AltAddressAddServiceImpl.truncateEndStr");
		String certificatePath=rsbundle.getString("CERTIFICATEPATH");
		String certificatePwd=rsbundle.getString("CERTIFICATEPWD");
		Long statusCode = null;
		String statusDescription=null;
		String outputXML = null;
		String accountNo = null;
		AddrAddRs addrAddRs = null;
		AltAdressAddResponse altAdressAddResponse = null;
		try {
			outputXML = InvokeCommunicatorUtil.invokeCommunicator(inputXML, fiServUrl,certificatePath,certificatePwd);
			log.info("outputXML "+outputXML);
			String removedIFX = truncateOutputXML(outputXML,startIndexStr,endIndexStr);
			addrAddRs =(AddrAddRs) generateOutputObject(removedIFX,AddrAddRs.class);
			if(null!=addrAddRs)
			{
				statusCode = addrAddRs.getStatus().getStatusCode();
				if(null!=statusCode && statusCode==0)
				{
					altAdressAddResponse.setAltAdressAdded("True");
				}
				else
				{
					altAdressAddResponse.setErrorCode(addrAddRs.getStatus().getError().get(0).getErrNum());
					altAdressAddResponse.setErrorDescription(addrAddRs.getStatus().getError().get(0).getErrDesc());
				}
			}
		
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("invokeAltAddressAddRq Exception Occured "+e.fillInStackTrace());
				throw new Exception(e);
				
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("invokeAltAddressAddRq Exception Occured "+e.fillInStackTrace());
				throw new Exception(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("invokeAltAddressAddRq Exception Occured "+e.fillInStackTrace());
				throw new Exception(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("invokeAltAddressAddRq Exception Occured "+e.fillInStackTrace());
				throw new Exception(e);
			}
		return altAdressAddResponse;
	}
	
	public String createAltAddressJaxbObject(JSONObject altAddressObject) throws Exception
	{
		ResourceBundle rsbundle = null;
		RqUID rquId=null;
		AddrAddRq addrAddRq = null;
		addrAddRq = new AddrAddRq();
		String currencyCode=null,dateFormat=null,serviceRquid=null,accountType=null,accountNo=null,operatingInstr=null;
		String address1=null,address2=null,address3=null,address4=null,address5=null,name=null,postCode=null,countryCode=null;
		String inputXML=null,constantXML=null,inputRq=null;
		BankAcctId bankActId = null;
		AddrType addrType = null;
		List<AddrType> addrLst = null;
		Calendar todayCal = null;
		try		
		{
			rsbundle =getResourceBundle();
			JSONObject jAccountObject = altAddressObject; //new JSONObject(accountDetails);
			currencyCode = (String)rsbundle.getString("CURRENCYCODE");
			dateFormat = (String)rsbundle.getString("dateFormat");
			rquId = new RqUID();
			serviceRquid = null;	
			bankActId = new BankAcctId();
			addrType = new AddrType();
			addrLst = new ArrayList<AddrType>();
			Date moveInDate=null;
			serviceRquid = GenerateUUID.getUUID();		
			rquId.setValue(serviceRquid);
			addrAddRq.setRqUID(rquId);
			
			
			if((jAccountObject.has(rsbundle.getString("CASACCOUNTTYPE"))))
				accountType = (String) jAccountObject.getString(rsbundle.getString("CASACCOUNTTYPE"));
			log.info("createAltAddressJaxbObject:accountType is "+accountType);
			if(null!=accountType && accountType.equalsIgnoreCase("SV"))
				bankActId.setAcctType(AcctTypeType.SV);
			else if(null!=accountType && accountType.equalsIgnoreCase("DD"))
				bankActId.setAcctType(AcctTypeType.DD);
			
			if(jAccountObject.has(rsbundle.getString("CASACCOUNTNO")))
				accountNo = (String) jAccountObject.getString(rsbundle.getString("CASACCOUNTNO"));
			log.info("createAltAddressJaxbObject:accountNo is "+accountNo);		
			bankActId.setAcctId(accountNo);
			addrAddRq.setBankAcctId(bankActId);
			
			if(jAccountObject.has(rsbundle.getString("CASALTADDRESS1")))
				address1 = (String)jAccountObject.getString(rsbundle.getString("CASALTADDRESS1"));
			log.info("createAltAddressJaxbObject:address1 is "+address1);
			if(jAccountObject.has(rsbundle.getString("CASALTADDRESS2")))
				address2 = (String)jAccountObject.getString(rsbundle.getString("CASALTADDRESS2"));
			log.info("createAltAddressJaxbObject:address2 is "+address2);
			if(jAccountObject.has(rsbundle.getString("CASALTADDRESS3")))
				address3 = (String)jAccountObject.getString(rsbundle.getString("CASALTADDRESS3"));
			log.info("createAltAddressJaxbObject:address3 is "+address3);
			if(jAccountObject.has(rsbundle.getString("CASALTADDRESS4")))
				address4 = (String)jAccountObject.getString(rsbundle.getString("CASALTADDRESS4"));
			log.info("createAltAddressJaxbObject:address4 is "+address4);
			if(jAccountObject.has(rsbundle.getString("CASALTADDRESS5")))
				address5 = (String)jAccountObject.getString(rsbundle.getString("CASALTADDRESS5"));
			log.info("createAltAddressJaxbObject:address5 is "+address5);
			
			/*if(jAccountObject.has(rsbundle.getString("CASACTNAME")))
				name = (String)jAccountObject.getString(rsbundle.getString("CASACTNAME"));
			log.info("createAltAddressJaxbObject:name is "+name);*/
			if(jAccountObject.has(rsbundle.getString("CASPOSTCODE")))
				postCode = (String)jAccountObject.getString(rsbundle.getString("CASPOSTCODE"));
			log.info("createAltAddressJaxbObject:postCode is "+postCode);
			if(jAccountObject.has(rsbundle.getString("CASCOUNTRY")))
				countryCode = (String)jAccountObject.getString(rsbundle.getString("CASCOUNTRY"));
			log.info("createAltAddressJaxbObject:countryCode is "+countryCode);
			
			if(jAccountObject.has(rsbundle.getString("CASOPERATINGINSTR")))
				operatingInstr = (String)jAccountObject.getString(rsbundle.getString("CASOPERATINGINSTR"));
			log.info("createAltAddressJaxbObject:operatingInstr is "+operatingInstr);
			
			
			if(null!=name)
				addrType.setFullName(name);
			if(null!=address1)
				addrType.setAddr1(address1);
			if(null!=address2)
				addrType.setAddr2(address2);
			if(null!=address3)
				addrType.setAddr3(address3);
			if(null!=address4)
				addrType.setAddr4(address4);
			if(null!=address5)
				addrType.setAddr5(address5);
			if(null!=postCode)
				addrType.setPostalCode(postCode);
			if(null!=countryCode)
			{
				if(countryCode.equalsIgnoreCase("94"))
					addrType.setCountry("LOCAL");
			}
			
			
			addrType.setAltAddrDelCode(false);
			addrType.setAltRevertFlag(false);
			addrType.setDrivenStatusCode("A");
			
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
			
			moveInDate = new Date();
			moveInDate.setDay(Long.valueOf(todayCal.get(todayCal.DAY_OF_MONTH)));
			moveInDate.setMonth(Long.valueOf(todayCal.get(todayCal.MONTH)+1));
			moveInDate.setYear(Long.valueOf(todayCal.get(todayCal.YEAR)));
			addrType.setMoveInDt(moveInDate);
			
			addrLst.add(addrType);
			
			addrAddRq.getAddr().addAll(addrLst);
			
			inputXML = generateInputXML(addrAddRq);
			constantXML = getConstantXML();
			String messageRquid = null;
			messageRquid = GenerateUUID.getUUID();
			String cifSvcRq = "<MaintSvcRq><RqUID>"+messageRquid+"</RqUID><SPName>"+rsbundle.getString("FISERV_SPNAME")+"</SPName>"+inputXML+"</MaintSvcRq></IFX>";
			inputRq = constantXML+cifSvcRq;
			log.info("createAltAddressJaxbObject:inputRq is "+inputRq);
			System.out.println(inputRq);
		}
			catch(ParseException e)
			{
				e.printStackTrace();
				log.error("createAltAddressJaxbObject:inputRq ParseException Occured "+e.fillInStackTrace());
				throw new Exception(e);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				log.error("createAltAddressJaxbObject:inputRq Exception Occured "+e.fillInStackTrace());
				throw new Exception(e);
			}
			finally
			{
				rsbundle = null;
				rquId=null;
				addrAddRq = null;
				currencyCode=null;dateFormat=null;serviceRquid=null;accountType=null;accountNo=null;operatingInstr=null;
				address1=null;address2=null;address3=null;address4=null;address5=null;name=null;postCode=null;countryCode=null;
				inputXML=null;constantXML=null;
				serviceRquid = null;	
				bankActId = null;
				addrType = null;
				addrLst = null;
				todayCal = null;
			}
			
		return inputRq;
	}
}
