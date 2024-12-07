

package com.boc.fiserv;

import java.text.ParseException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import com.boc.fiserv.response.CreateCollateralResponse;

/*
Created By SaiMadan on Jul 20, 2016
*/
public interface CreateCollateralService {
	public CreateCollateralResponse execute(HashMap parametersMap) throws JAXBException,Exception;
	public String createCollateralJAXBObject(HashMap parametersMap) throws ParseException, Exception;
	public CreateCollateralResponse invokeCommunicator(String inputXML,String referenceNo) throws Exception;
}
