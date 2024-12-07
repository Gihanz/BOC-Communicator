package com.boc.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InvokeCommunicatorUtil 
{
	private static Logger log = LoggerFactory.getLogger(InvokeCommunicatorUtil.class);
	
public static String invokeCommunicator(String inputXml,String fiServurl,String certificatePath,String certificatePassword) throws MalformedURLException,ProtocolException,IOException,Exception
{
	
	//String fiServurl = UIConstants.fiServurl;
	//String inputXml="<IFX><SignonRq><SignonPswd><SignonRole>CSR</SignonRole><CustId><SPName>FiservICBS</SPName><CustLoginId>SGINTNET</CustLoginId></CustId><CustPswd><CryptType>NONE</CryptType><Pswd>SGINTNET</Pswd></CustPswd><GenSessKey>0</GenSessKey></SignonPswd><ClientDt><Year>2011</Year><Month>1</Month><Day>27</Day></ClientDt><ClientApp><Org>Fiserv</Org><Name>IB</Name><Version>0.1</Version><ClientAppKey>BOCSRVRHECGNSYSQCUWRJDHKRFDBNTGN</ClientAppKey></ClientApp><ComputerId>SGPVMAPRDEV03</ComputerId><InstitutionCode>BOCSR</InstitutionCode></SignonRq><EnvironmentInfo><EnvironmentName>default</EnvironmentName></EnvironmentInfo><BankSvcRq><RqUID>6c3b8d83c88b739cacc0b3c25</RqUID><SPName>FiservICBS</SPName><AcctInqRq><RqUID>8fb82ad09d7b4257bee3004b296c5</RqUID><DepAcctId><AcctId>002101010565</AcctId><AcctType>TM</AcctType></DepAcctId><IncBal>1</IncBal></AcctInqRq></BankSvcRq></IFX>";
	byte[] xmlByteArr = inputXml.getBytes();
	int    postDataLength = xmlByteArr.length;
	String output = null;
	StringBuffer outputStr = new StringBuffer(); 
	//certificatePath = "D:/saimadan/certificate/HOCOM.pfx";
	try 
	{ 
		URL url = new URL(fiServurl);
		Properties systemProperties = System.getProperties();
		//systemProperties.setProperty("https.proxyHost","aurorah01.bankofceylon.local");
		//systemProperties.setProperty("https.proxyPort","8002");
		systemProperties.put( "javax.net.ssl.trustStore", certificatePath);
		//systemProperties.put( "javax.net.ssl.trustStore", "D:/saimadan/certificate/HOCOM.pfx");
		//System.setProperty("javax.net.ssl.trustStorePassword","123");
		System.setProperty("javax.net.ssl.trustStorePassword",certificatePassword);
		System.setProperty("javax.net.ssl.trustStoreType","PKCS12");
		System.setProperties(systemProperties);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		
		conn.setRequestProperty( "Connection", "close" );
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );
		
		File pKeyFile = new File(certificatePath);
		//File pKeyFile = new File("D:/saimadan/certificate/HOCOM.pfx");
		//String pKeyPassword = "123";
		String pKeyPassword = certificatePassword;
	      //KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509"); //For Standalone
	      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("IbmX509"); //For WAS
	      KeyStore keyStore = KeyStore.getInstance("PKCS12");
	      InputStream keyInput = new FileInputStream(pKeyFile);
	      keyStore.load(keyInput, pKeyPassword.toCharArray());
	      keyInput.close();
	      keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());
	      SSLContext context = SSLContext.getInstance("TLS");
	      context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
	      SSLSocketFactory sockFact = context.getSocketFactory();
	      conn.setSSLSocketFactory( sockFact );
		
		DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
		wr.write(xmlByteArr);

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		log.info("invokeCommunicator:Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			outputStr.append(output);
			log.info("invokeCommunicator output XML is "+output);
		}

		conn.disconnect();

	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.error("invokeCommunicator: MalformedURLException Occured"+e.fillInStackTrace());
		throw e;
	} catch (ProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.error("invokeCommunicator: ProtocolException Occured"+e.fillInStackTrace());
		throw e;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.error("invokeCommunicator: IOException Occured"+e.fillInStackTrace());
		throw e;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		log.error("invokeCommunicator: Exception Occured"+e.fillInStackTrace());
		throw e;
	}
	
	return outputStr.toString();
}


/*public static String invokeCommunicator(String inputXml,String fiServurl) throws MalformedURLException,ProtocolException,IOException,Exception
{
	
	//String fiServurl = UIConstants.fiServurl;
	//String inputXml="<IFX><SignonRq><SignonPswd><SignonRole>CSR</SignonRole><CustId><SPName>FiservICBS</SPName><CustLoginId>SGINTNET</CustLoginId></CustId><CustPswd><CryptType>NONE</CryptType><Pswd>SGINTNET</Pswd></CustPswd><GenSessKey>0</GenSessKey></SignonPswd><ClientDt><Year>2011</Year><Month>1</Month><Day>27</Day></ClientDt><ClientApp><Org>Fiserv</Org><Name>IB</Name><Version>0.1</Version><ClientAppKey>BOCSRVRHECGNSYSQCUWRJDHKRFDBNTGN</ClientAppKey></ClientApp><ComputerId>SGPVMAPRDEV03</ComputerId><InstitutionCode>BOCSR</InstitutionCode></SignonRq><EnvironmentInfo><EnvironmentName>default</EnvironmentName></EnvironmentInfo><BankSvcRq><RqUID>6c3b8d83c88b739cacc0b3c25</RqUID><SPName>FiservICBS</SPName><AcctInqRq><RqUID>8fb82ad09d7b4257bee3004b296c5</RqUID><DepAcctId><AcctId>002101010565</AcctId><AcctType>TM</AcctType></DepAcctId><IncBal>1</IncBal></AcctInqRq></BankSvcRq></IFX>";
	byte[] xmlByteArr = inputXml.getBytes();
	int    postDataLength = xmlByteArr.length;
	String output = null;
	StringBuffer outputStr = new StringBuffer(); 
	try 
	{
		URL url = new URL(fiServurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );
		DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
		wr.write(xmlByteArr);

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		
		log.info("invokeCommunicator:Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			outputStr.append(output);
			log.info("invokeCommunicator output XML is "+output);
		}

		conn.disconnect();

	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.error("invokeCommunicator: MalformedURLException Occured"+e.fillInStackTrace());
		throw e;
	} catch (ProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.error("invokeCommunicator: ProtocolException Occured"+e.fillInStackTrace());
		throw e;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.error("invokeCommunicator: IOException Occured"+e.fillInStackTrace());
		throw e;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		log.error("invokeCommunicator: Exception Occured"+e.fillInStackTrace());
		throw e;
	}
	
	return outputStr.toString();
}*/



public static void main(String a[])
{
	String inputXML="<IFX><SignonRq><SignonPswd><SignonRole>CSR</SignonRole><CustId><SPName>FiservICBS</SPName><CustLoginId>SGDMSID</CustLoginId></CustId><CustPswd><CryptType>NONE</CryptType><Pswd>sg78@789</Pswd></CustPswd><GenSessKey>0</GenSessKey></SignonPswd><ClientDt><Year>2016</Year><Month>8</Month><Day>22</Day></ClientDt><ClientApp><Org>Fiserv</Org><Name>DM</Name><Version>0.1</Version><ClientAppKey>BOCSRVRHECGNSYSQCUWRJDHKRFDBNTGN</ClientAppKey></ClientApp><ComputerId>SGPVMAPRDEV03</ComputerId><InstitutionCode>BOCSR</InstitutionCode></SignonRq><EnvironmentInfo><EnvironmentName>default</EnvironmentName></EnvironmentInfo><LoanSvcRq><RqUID>0c37d1b9-879a-4323-813a-c66553b5d9f5</RqUID><SPName>FiservICBS</SPName><CollateralItemAddRq><RqUID>66e2f679-a6bf-44c9-b849-3e5b48aa5605</RqUID><CustId><SPName>FiservICBS</SPName><CustPermId>0005048693</CustPermId></CustId><CollateralItemInfo><CollateralCode>2</CollateralCode><Commodity><CommodityId>0005048693</CommodityId></Commodity><LocationCode>1</LocationCode><CollateralShortDesc>Hire Purchase/Leasing</CollateralShortDesc><CollateralValue><CollateralOriginalAmt><Amt>111111.00</Amt><CurCode>000</CurCode></CollateralOriginalAmt><CollateralAmt><Amt>111111.00</Amt><CurCode>000</CurCode></CollateralAmt><UnitPriceAmt><Amt>111111.00</Amt><CurCode>000</CurCode></UnitPriceAmt><UnitsNum>1</UnitsNum><MarginPercent>100</MarginPercent></CollateralValue><CollateralReviewFreq>M01222</CollateralReviewFreq><NextCollateralReviewDt><Year>2017</Year><Month>8</Month><Day>22</Day></NextCollateralReviewDt></CollateralItemInfo></CollateralItemAddRq></LoanSvcRq></IFX>";
	String fiServURL="https://hofiservcom01.bankofceylon.local/CRG_PRODUAT/crg.aspx";
	try {
		//invokeCommunicator(inputXML,fiServURL);
		invokeCommunicator(inputXML,fiServURL,"/fs1/IBM/BOC/certificate/HOCOM.pfx","123");
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
