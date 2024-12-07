package com.boc.fiserv;

import javax.xml.bind.JAXBException;

import com.boc.fiserv.response.CustomerProfileRs;

public interface CustProfBasicInqService {
	public String execute(String referenceNumber,String branchCode, String productCode,String wobNumber,String subMapName,String activityName,String className) throws JAXBException,Exception;
	public CustomerProfileRs invokeCommunicator(String inputXML) throws Exception;
	public String getAccountNo(String referenceNumber) throws Exception;
	public String createCustProfBasicInqRqJaxbObject(String accountNo,String branchCode, String productCode,String referenceNumber,String wobNumber,String subMapName,String activityName,String className) throws JAXBException,Exception;
	public String createCustProfBasicInqRqJaxbObject(String accountNo) throws JAXBException, Exception;
	public CustomerProfileRs invokeCustProfBasicInq(String inputXML) throws Exception;
}
