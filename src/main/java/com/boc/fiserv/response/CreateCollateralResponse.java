

package com.boc.fiserv.response;

/*
Created By SaiMadan on Jul 20, 2016
*/
public class CreateCollateralResponse {
	String collateralId;
	String ErrorCode;
	String ErrorDescription;
	public String getCollateralId() {
		return collateralId;
	}
	public void setCollateralId(String collateralId) {
		this.collateralId = collateralId;
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
