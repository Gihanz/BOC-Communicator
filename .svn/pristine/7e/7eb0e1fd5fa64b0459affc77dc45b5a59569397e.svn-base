

package com.boc.fiserv;

import java.text.ParseException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import com.boc.fiserv.response.LoanAcctAddResponse;

/*
Created By SaiMadan on Jun 24, 2016
*/
public interface LoanAcctAddService {

	public String execute(String referenceNumber,String applicationId,String caseTypeName) throws JAXBException,Exception;
	public HashMap getLoanAcctDetailsFromCM(String referenceNumber,String caseTypeName,String[] loanAcctAddParametersArr) throws Exception;
	public String createLoanActAddRqJAxbObject(HashMap parametersMap, String applicationId,String caseTypeName) throws ParseException, Exception;
	public LoanAcctAddResponse invokeCommunicator(String inputXML) throws Exception;
}
