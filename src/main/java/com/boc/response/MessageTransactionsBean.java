

package com.boc.response;

import java.sql.Blob;

/*
Created By SaiMadan on Jun 30, 2016
*/
public class MessageTransactionsBean 
{
String wfRefNo;
String wobNo;
String branchCode;
String productCode;
byte[] requestXML;
String mapName;
String retry;
String className;
String caseType;
String applicationId;

public String getWfRefNo() {
	return wfRefNo;
}
public void setWfRefNo(String wfRefNo) {
	this.wfRefNo = wfRefNo;
}
public String getWobNo() {
	return wobNo;
}
public void setWobNo(String wobNo) {
	this.wobNo = wobNo;
}
public String getBranchCode() {
	return branchCode;
}
public void setBranchCode(String branchCode) {
	this.branchCode = branchCode;
}
public String getProductCode() {
	return productCode;
}
public void setProductCode(String productCode) {
	this.productCode = productCode;
}


public byte[] getRequestXML() {
	return requestXML;
}
public void setRequestXML(byte[] requestXML) {
	this.requestXML = requestXML;
}
public String getMapName() {
	return mapName;
}
public void setMapName(String mapName) {
	this.mapName = mapName;
}
public String getRetry() {
	return retry;
}
public void setRetry(String retry) {
	this.retry = retry;
}
public String getClassName() {
	return className;
}
public void setClassName(String className) {
	this.className = className;
}
public String getCaseType() {
	return caseType;
}
public void setCaseType(String caseType) {
	this.caseType = caseType;
}
public String getApplicationId() {
	return applicationId;
}
public void setApplicationId(String applicationId) {
	this.applicationId = applicationId;
}
	


}
