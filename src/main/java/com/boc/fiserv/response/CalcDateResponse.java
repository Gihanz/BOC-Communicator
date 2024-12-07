

package com.boc.fiserv.response;

import java.util.Date;

/*
Created By SaiMadan on Sep 9, 2016
*/
public class CalcDateResponse {
	
	String date;
	String ErrorCode;
	String ErrorDescription;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
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

	
	
}
