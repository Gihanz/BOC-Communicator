

package com.boc.fiserv.response;

/*
Created By SaiMadan on Apr 23, 2017
*/
public class AltAdressAddResponse 
{

	String ErrorCode;
	String ErrorDescription;
	String altAdressAdded;
	public String getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}
	public String getErrorDescription() {
		return ErrorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		ErrorDescription = errorDescription;
	}
	public String getAltAdressAdded() {
		return altAdressAdded;
	}
	public void setAltAdressAdded(String altAdressAdded) {
		this.altAdressAdded = altAdressAdded;
	}
	
	
	
}
