

package com.boc.fiserv.response;

import java.math.BigInteger;

/*
Created By SaiMadan on Mar 20, 2017
*/
public class CustomerProfileAddRs {
	
	Long statusCode;
	String statusDescription;
	String ErrorCode;
	String ErrorDescription;
	String cifNo;
	String fullNameMemoCreated;
	String employeeMemoCreated;
	BigInteger cifFullNameRs;
	String custToCustResponse;
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
	public String getCifNo() {
		return cifNo;
	}
	public void setCifNo(String cifNo) {
		this.cifNo = cifNo;
	}
	public BigInteger getCifFullNameRs() {
		return cifFullNameRs;
	}
	public void setCifFullNameRs(BigInteger cifFullNameRs) {
		this.cifFullNameRs = cifFullNameRs;
	}
	public Long getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getCustToCustResponse() {
		return custToCustResponse;
	}
	public void setCustToCustResponse(String custToCustResponse) {
		this.custToCustResponse = custToCustResponse;
	}
	public String getFullNameMemoCreated() {
		return fullNameMemoCreated;
	}
	public void setFullNameMemoCreated(String fullNameMemoCreated) {
		this.fullNameMemoCreated = fullNameMemoCreated;
	}
	public String getEmployeeMemoCreated() {
		return employeeMemoCreated;
	}
	public void setEmployeeMemoCreated(String employeeMemoCreated) {
		this.employeeMemoCreated = employeeMemoCreated;
	}
	

}
