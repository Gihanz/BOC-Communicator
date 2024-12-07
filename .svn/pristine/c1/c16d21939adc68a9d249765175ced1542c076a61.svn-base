package com.boc.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InvokeCommunicatorUtil_bkup 
{
	private static Logger log = LoggerFactory.getLogger(InvokeCommunicatorUtil.class);
	
public static String invokeCommunicator(String inputXml,String fiServurl) throws MalformedURLException,ProtocolException,IOException,Exception
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
}
}
