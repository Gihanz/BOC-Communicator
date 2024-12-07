

package com.boc.connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SimpleTimeZone;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.utils.CripUtils;
import com.filenet.api.collection.BooleanList;
import com.filenet.api.collection.DateTimeList;
import com.filenet.api.collection.EngineCollection;
import com.filenet.api.collection.Float64List;
import com.filenet.api.collection.IdList;
import com.filenet.api.collection.Integer32List;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import com.ibm.casemgmt.api.Case;
import com.ibm.casemgmt.api.CaseType;
import com.ibm.casemgmt.api.Comment;
import com.ibm.casemgmt.api.DeployedSolution;
import com.ibm.casemgmt.api.context.CaseMgmtContext;
import com.ibm.casemgmt.api.context.P8ConnectionCache;
import com.ibm.casemgmt.api.context.SimpleP8ConnectionCache;
import com.ibm.casemgmt.api.context.SimpleVWSessionCache;
import com.ibm.casemgmt.api.objectref.ObjectStoreReference;
import com.ibm.casemgmt.api.properties.CaseMgmtProperties;
import com.ibm.casemgmt.api.properties.CaseMgmtProperty;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWGuid;
import filenet.vw.api.VWParticipant;
import filenet.vw.api.VWXMLData;

/*
Created By SaiMadan on Jun 27, 2016
*/
public class CMConnector 
{
	private static Logger log = LoggerFactory.getLogger(CMConnector.class);
	private UserContext uc;
	private boolean userSubjectPushed;
	private Connection conn;
	private CaseMgmtContext origCmctx;
	
	public static void main(String a[])
	{
		CMConnector connect = new CMConnector();
		connect.getCMConnection();
		String accountNo;
		try {
			//accountNo = connect.getAccountNo("12345");
			
			//System.out.println("accountNo is "+accountNo);
			//connect.getLoanAcctDetails("12345");
			ResourceBundle rs = connect.getResourceBundle();
			
			//connect.updateLoanActId("0001000112016000058", "78078201","Personal Loan");
			
			/*String loanActAddParameters= rs.getString("LOANACCTADDPARAMETERS");
			String[] loanActAddParametersArr = loanActAddParameters.split(",");
			HashMap parametersMap = connect.getLoanAcctDetails("0001000012016000080", loanActAddParametersArr);*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connect.releaseCMConnection();
	}
	
	public ResourceBundle getResourceBundle()
	{
		ResourceBundle rsbundle = ResourceBundle.getBundle("config");
		return rsbundle;
	}
	
	public Connection getCMConnection()
	{
		ResourceBundle rs = getResourceBundle();
		String ceURI = rs.getString("CEURI"); //"http://DRDMTEST01:9080/wsi/FNCEWS40MTOM/";
		String userId =  rs.getString("CMUSERID"); //"p8admin_test";
		String encryptedPassword =rs.getString("CMPASSWORD");  //"boc@123";
		String stanza = rs.getString("CMSTANZA") ;//"FileNetP8WSI";
		
		String password = CripUtils.decryptStr(encryptedPassword);
		
		P8ConnectionCache connCache = new SimpleP8ConnectionCache();
        conn = connCache.getP8Connection(ceURI);
        Subject subject = UserContext.createSubject(conn, userId,password, stanza);
        uc = UserContext.get();
        uc.pushSubject(subject);
        userSubjectPushed=true;
        Locale origLocale = uc.getLocale();
        uc.setLocale(Locale.ENGLISH);
        origCmctx = CaseMgmtContext.set(new CaseMgmtContext(new SimpleVWSessionCache(), connCache));
    return conn;
	}
	
	public void releaseCMConnection()
	{
		if (userSubjectPushed)
        {
			CaseMgmtContext.set(origCmctx);
			Locale origLocale = uc.getLocale();
            uc.setLocale(origLocale);
            uc.popSubject();
            userSubjectPushed = false;
	        conn=null;
	        origCmctx=null;
	        System.out.println("Connection Released");
        }
	}
	
	
	
	public String getAccountNo(String referenceNumber) throws Exception
	{
		Connection conn = getCMConnection();		
		ResourceBundle rs = getResourceBundle();
		String solutionName = rs.getString("CMSOLUTIONNAME") ;//"Loan Application";
		String caseTypename = rs.getString("CMPERSONALCASETYPE") ;//"BOC_PersonalLoan";
		String targetOsname = rs.getString("CMTARGETOS") ;//"fnObjStr";
		String domainName = rs.getString("CMDOMAINNAME") ;//"fnp8domain";
		String propReferenceNumber = rs.getString("REFERENCENO");
		String accountNo = null;
		try
		{
			Domain domain = Factory.Domain.fetchInstance(conn, domainName, null);
	    	ObjectStore os = Factory.ObjectStore.fetchInstance(domain, targetOsname,null);
	    	ObjectStoreReference osRef = new ObjectStoreReference(os);
	    	
	    	
	    	
	    	/*DeployedSolution deployedSolution = DeployedSolution.fetchInstance(osRef, solutionName); 
	        System.out.println("Solution Name "+deployedSolution.getSolutionName());
	        List<CaseType> caseTypes = deployedSolution.getCaseTypes();
	        for(CaseType ct : caseTypes) {
	            System.out.println(ct.getName());
	            if(ct.getName().equalsIgnoreCase(caseTypename))
	            {
	            	Id caseId = ct.getId();
	            	CaseType caseTypeInstance =  ct.fetchInstance(osRef, caseTypename);
	            	
	            	System.out.println("caseId is "+caseId);
	            	
	            	searchCase(os,solutionName,caseTypename);
	            }
	        }*/
			
			//String collectionName="0072";
			//referenceNumber="12345";
			//String searchString = "SELECT t.[This],t.[FolderName], t.[LastModifier], t.[DateLastModified], t.[CmAcmCaseTypeFolder], t.[CmAcmCaseState],t.[CmAcmCaseIdentifier], t.[DateCreated], t.[Creator], t.[Id], t.[ContainerType],t.[LockToken], t.[LockTimeout], t.[ClassDescription], t.[DateLastModified], t.[FolderName] FROM [CmAcmCaseFolder] t where t.[CmAcmCaseIdentifier] LIKE '%%' AND t.[CmAcmParentSolution]=Object('/IBM Case Manager/Solution Deployments/"+solutionName+"') AND t.[CollectionName] LIKE '"+collectionName+"' ORDER BY t.[DateCreated] DESC";
			//String searchString = "SELECT t.[This],t.[FolderName], t.[LastModifier], t.[DateLastModified], t.[CmAcmCaseTypeFolder], t.[CmAcmCaseState],t.[CmAcmCaseIdentifier], t.[DateCreated], t.[Creator], t.[Id], t.[ContainerType],t.[LockToken], t.[LockTimeout], t.[ClassDescription], t.[DateLastModified], t.[FolderName] FROM [CmAcmCaseFolder] t where t.[CmAcmCaseIdentifier] LIKE '%%' AND t.[CmAcmParentSolution]=Object('/IBM Case Manager/Solution Deployments/"+solutionName+"')  ORDER BY t.[DateCreated] DESC";
			String searchString = "SELECT * FROM "+caseTypename+" WHERE ("+propReferenceNumber+" = '" + referenceNumber + "' AND CmAcmCaseState = 2) ";
			log.info("getAccountNo:searchString is "+searchString);
			SearchSQL sqlObject = new SearchSQL (searchString);
					// Executes the content search
					SearchScope searchScope = new SearchScope(os);
					RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, null, null, new Boolean(true));
					
					Iterator iter = rowSet.iterator();
					if (iter.hasNext()) {
					RepositoryRow row = (RepositoryRow) iter.next();
					String folderName = row.getProperties().get("FolderName").getStringValue();
					String caseInstanceFolderName = folderName;
					log.info("getAccountNo:caseInstanceFolderName is "+caseInstanceFolderName);
					Id id = row.getProperties().get("Id").getIdValue();
					System.out.println("Id is "+id.toString());
					Case caseObj =  Case.fetchInstance(osRef, id, null, null);
					CaseMgmtProperties properties =  caseObj.getProperties();
					List<CaseMgmtProperty> propertiesLst = properties.asList();
					if(propertiesLst !=null)
					{
						for(CaseMgmtProperty property:propertiesLst)
						{
							log.debug("getAccountNo:property Name is "+property.getDisplayName()+" "+property.getValue());
							if(property.getDisplayName().equalsIgnoreCase("AccountNo"))
									{
										accountNo = (String) property.getValue();
									}
						}
							
					}
				}
		}
		catch(Exception e)
		{
			log.error("Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			releaseCMConnection();
		}
		return accountNo;
	}
	
	public HashMap getLoanAcctDetails(String referenceNumber,String caseTypeName,String[] loanAcctAddParametersArr) throws Exception
	{
		Connection conn = getCMConnection();		
		ResourceBundle rs = getResourceBundle();
		HashMap  paramterValues = null;
		String solutionName = rs.getString("CMSOLUTIONNAME") ;//"Loan Application";
		String caseTypename = caseTypeName;//rs.getString("CMPERSONALCASETYPE") ;//"BOC_PersonalLoan";
		String targetOsname = rs.getString("CMTARGETOS") ;//"fnObjStr";
		String domainName = rs.getString("CMDOMAINNAME") ;//"fnp8domain";
		String propReferenceNumber = rs.getString("REFERENCENO");
		try
		{
			Domain domain = Factory.Domain.fetchInstance(conn, domainName, null);
	    	ObjectStore os = Factory.ObjectStore.fetchInstance(domain, targetOsname,null);
	    	ObjectStoreReference osRef = new ObjectStoreReference(os);
	    	String searchString = "SELECT * FROM "+caseTypename+" WHERE ("+propReferenceNumber+" = '" + referenceNumber + "' AND CmAcmCaseState = 2) ";
	    	log.info("getLoanAcctDetails:searchString is "+searchString);
			SearchSQL sqlObject = new SearchSQL (searchString);
					// Executes the content search
			SearchScope searchScope = new SearchScope(os);
			RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, null, null, new Boolean(true));
			
			Iterator iter = rowSet.iterator();
			if (iter.hasNext()) {
			RepositoryRow row = (RepositoryRow) iter.next();
			String folderName = row.getProperties().get("FolderName").getStringValue();
			String caseInstanceFolderName = folderName;
			log.info("getLoanAcctDetails:caseInstanceFolderName is "+caseInstanceFolderName);
			Id id = row.getProperties().get("Id").getIdValue();
			paramterValues=getTaskPropertyValues(osRef,id,loanAcctAddParametersArr);
			}
		}
		catch(Exception e)
		{
			log.error("Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			releaseCMConnection();
		}
		return paramterValues;
	}
	
	public void updateLoanActId(String referenceNumber,String caseTypeName,String proprtyName,String propertyValue) throws Exception
	{
		Connection conn = getCMConnection();		
		ResourceBundle rs = getResourceBundle();
		HashMap  paramterValues = null;
		String solutionName = rs.getString("CMSOLUTIONNAME") ;//"Loan Application";
		String caseTypename = caseTypeName;//rs.getString("CMPERSONALCASETYPE") ;//"BOC_PersonalLoan";
		String targetOsname = rs.getString("CMTARGETOS") ;//"fnObjStr";
		String domainName = rs.getString("CMDOMAINNAME") ;//"fnp8domain";
		String propReferenceNumber = rs.getString("REFERENCENO");
		//String propLaonActId = rs.getString("LOANACTID");
		try
		{
			Domain domain = Factory.Domain.fetchInstance(conn, domainName, null);
	    	ObjectStore os = Factory.ObjectStore.fetchInstance(domain, targetOsname,null);
	    	ObjectStoreReference osRef = new ObjectStoreReference(os);
	    	String searchString = "SELECT * FROM "+caseTypename+" WHERE ("+propReferenceNumber+" = '" + referenceNumber + "' AND CmAcmCaseState = 2) ";
	    	log.info("updateLoanActId:searchString is "+searchString);
			SearchSQL sqlObject = new SearchSQL (searchString);
					// Executes the content search
			SearchScope searchScope = new SearchScope(os);
			RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, null, null, new Boolean(true));
			
			Iterator iter = rowSet.iterator();
			if (iter.hasNext()) {
			RepositoryRow row = (RepositoryRow) iter.next();
			String folderName = row.getProperties().get("FolderName").getStringValue();
			String caseInstanceFolderName = folderName;
			log.info("updateLoanActId:caseInstanceFolderName is "+caseInstanceFolderName);
			Id id = row.getProperties().get("Id").getIdValue();
			Case caseObj =  Case.fetchInstance(osRef, id, null, null);
			CaseMgmtProperties taskProps = caseObj.getProperties();
			taskProps.putObjectValue(proprtyName, propertyValue);
			//#Added by Vikshith : After setting the loanActId property Case object was not saving so caseObj.save(RefreshMode.REFRESH, null, null); added to save case object.
			log.info("Case object saved.");
			caseObj.save(RefreshMode.REFRESH, null, null);
			log.info("updateLoanActId:"+ proprtyName +"Property update done succesfully");
			}
		}
		catch(Exception e)
		{
			log.error("Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			releaseCMConnection();
		}
	}
	
	public void updateCMProperty(String referenceNumber,String caseTypeName,HashMap propertyMap) throws Exception
	{
		Connection conn = getCMConnection();		
		ResourceBundle rs = getResourceBundle();
		HashMap  paramterValues = null;
		String solutionName = rs.getString("CMSOLUTIONNAME") ;//"Loan Application";
		String caseTypename = caseTypeName;// rs.getString("CMPERSONALCASETYPE") ;//"BOC_PersonalLoan";
		String targetOsname = rs.getString("CMTARGETOS") ;//"fnObjStr";
		String domainName = rs.getString("CMDOMAINNAME") ;//"fnp8domain";
		String propReferenceNumber = rs.getString("REFERENCENO");
		try
		{
			Domain domain = Factory.Domain.fetchInstance(conn, domainName, null);
	    	ObjectStore os = Factory.ObjectStore.fetchInstance(domain, targetOsname,null);
	    	ObjectStoreReference osRef = new ObjectStoreReference(os);
	    	String searchString = "SELECT * FROM "+caseTypename+" WHERE ("+propReferenceNumber+" = '" + referenceNumber + "' AND CmAcmCaseState = 2) ";
	    	log.info("updateLoanActId:searchString is "+searchString);
			SearchSQL sqlObject = new SearchSQL (searchString);
					// Executes the content search
			SearchScope searchScope = new SearchScope(os);
			RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, null, null, new Boolean(true));
			
			Iterator iter = rowSet.iterator();
			if (iter.hasNext()) {
			RepositoryRow row = (RepositoryRow) iter.next();
			String folderName = row.getProperties().get("FolderName").getStringValue();
			String caseInstanceFolderName = folderName;
			log.info("updateLoanActId:caseInstanceFolderName is "+caseInstanceFolderName);
			Id id = row.getProperties().get("Id").getIdValue();
			Case caseObj =  Case.fetchInstance(osRef, id, null, null);
			CaseMgmtProperties taskProps = caseObj.getProperties();
			if(null!=propertyMap)
			{
				Set keyset = propertyMap.keySet();
				Iterator propertyIter = keyset.iterator();
				while(propertyIter.hasNext())
				{
					String key = (String)propertyIter.next();
					String value = (String)propertyMap.get(key);
					taskProps.putObjectValue(key, value);
					log.info("updateLoanActId:"+key+" Property update done succesfully");
				}
			}
			}
		}
		catch(Exception e)
		{
			log.error("Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			releaseCMConnection();
		}
	}
	
	public void updateProperty(String referenceNumber,String propReferenceNumber,String caseTypeName,String solutionName,HashMap propertyMap) throws Exception
	{
		Connection conn = getCMConnection();		
		ResourceBundle rs = getResourceBundle();
		HashMap  paramterValues = null;
		//String solutionName = rs.getString("CMSOLUTIONNAME") ;//"Loan Application";
		String caseTypename = caseTypeName;// rs.getString("CMPERSONALCASETYPE") ;//"BOC_PersonalLoan";
		String targetOsname = rs.getString("CMTARGETOS") ;//"fnObjStr";
		String domainName = rs.getString("CMDOMAINNAME") ;//"fnp8domain";
		//String propReferenceNumber = rs.getString("REFERENCENO");
		try
		{
			Domain domain = Factory.Domain.fetchInstance(conn, domainName, null);
	    	ObjectStore os = Factory.ObjectStore.fetchInstance(domain, targetOsname,null);
	    	ObjectStoreReference osRef = new ObjectStoreReference(os);
	    	String searchString = "SELECT * FROM "+caseTypename+" WHERE ("+propReferenceNumber+" = '" + referenceNumber + "' AND CmAcmCaseState = 2) ";
	    	log.info("updateProperty: searchString is "+searchString);
			SearchSQL sqlObject = new SearchSQL (searchString);
					// Executes the content search
			SearchScope searchScope = new SearchScope(os);
			RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, null, null, new Boolean(true));
			
			Iterator iter = rowSet.iterator();
			if (iter.hasNext()) {
			RepositoryRow row = (RepositoryRow) iter.next();
			String folderName = row.getProperties().get("FolderName").getStringValue();
			String caseInstanceFolderName = folderName;
			log.info("updateProperty: caseInstanceFolderName is "+caseInstanceFolderName);
			Id id = row.getProperties().get("Id").getIdValue();
			Case caseObj =  Case.fetchInstance(osRef, id, null, null);
			CaseMgmtProperties taskProps = caseObj.getProperties();
			if(null!=propertyMap)
			{
				Set keyset = propertyMap.keySet();
				Iterator propertyIter = keyset.iterator();
				while(propertyIter.hasNext())
				{
					String key = (String)propertyIter.next();
					String value = (String)propertyMap.get(key);
					taskProps.putObjectValue(key, value);
					log.info("updateProperty:"+key+" Property update done succesfully");
				}
			}
			}
		}
		catch(Exception e)
		{
			log.error("Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		finally
		{
			releaseCMConnection();
		}
	}
	
	public String[] getTaskPropertyNames(ObjectStoreReference osRef,Id id)  throws Exception
	{
			   String as[];
			   Case caseObj =  Case.fetchInstance(osRef, id, null, null);
			   CaseMgmtProperties taskProps = caseObj.getProperties();
	            List lst = taskProps.asList();
	            Iterator it = lst.iterator();
	            ArrayList taskPropNames = new ArrayList();
	            do
	            {
	                if(!it.hasNext())
	                    break;
	                CaseMgmtProperty f = (CaseMgmtProperty)it.next();
	                if(!f.isSystemOwned() && !f.isInherited() && (f.getPropertyType() == TypeID.BOOLEAN || f.getPropertyType() == TypeID.DATE || f.getPropertyType() == TypeID.DOUBLE || f.getPropertyType() == TypeID.STRING || f.getPropertyType() == TypeID.LONG))
	                {
	                	taskPropNames.add(f.getSymbolicName());
	                	System.out.println(f.getSymbolicName());
	                }
	            } while(true);
	            String retPropNames[] = new String[taskPropNames.size()];
	            taskPropNames.toArray(retPropNames);
	            as = retPropNames;
			return as;
	}
	
	public HashMap getTaskPropertyValues(ObjectStoreReference osRef,Id id, String taskPropertyNames[])
	        throws Exception
	    {
			HashMap propertyMap = null;
			propertyMap = new HashMap();
			
			Case caseObj =  Case.fetchInstance(osRef, id, null, null);
			CaseMgmtProperties taskProps = caseObj.getProperties();
			for(String propertyName:taskPropertyNames)
			{
				log.debug("getTaskPropertyValues:property name is "+propertyName);
				 CaseMgmtProperty property = taskProps.find(propertyName);
				 {
					 	boolean isArray = property.getCardinality() != Cardinality.SINGLE;
		                Object propvalue = property.getValue();
		                if(isArray)
		                {
		                	propvalue =  convertParamValue(propvalue, isArray);
		                	propertyMap.put(propertyName, propvalue);
		                }
		                else
		                	propertyMap.put(propertyName, propvalue);
		                //String valueStr = convertParamValueToOutputString(propvalue, isArray);
		                log.debug("getTaskPropertyValues:property Value is "+propvalue);
				 }
			}
			return propertyMap;
	    }
	
	public Object convertParamValue(Object value, boolean isArray)
	{
		Object propValue = null;
		if(isArray)
        {
			List<Float> valueFloatLst= Collections.unmodifiableList((List) value);
            StringBuilder sb = new StringBuilder();
            Iterator iter = null;
            if(value instanceof BooleanList)
                iter = ((BooleanList)value).iterator();
            else
            if(value instanceof DateTimeList)
                iter = ((DateTimeList)value).iterator();
            else
            if(value instanceof Float64List)
            {
                iter = ((Float64List)value).iterator();
                System.out.println("***"+iter.hasNext());
            }
            else
            if(value instanceof IdList)
                iter = ((IdList)value).iterator();
            else
            if(value instanceof Integer32List)
                iter = ((Integer32List)value).iterator();
            else
            if(value instanceof StringList)
                iter = ((StringList)value).iterator();
            else
            if(value instanceof Integer[])
                iter = Arrays.asList((Integer[])(Integer[])value).iterator();
            else
            if(value instanceof String[])
                iter = Arrays.asList((String[])(String[])value).iterator();
            else
            if(value instanceof Boolean[])
                iter = Arrays.asList((Boolean[])(Boolean[])value).iterator();
            else
            if(value instanceof Double[])
                iter = Arrays.asList((Double[])(Double[])value).iterator();
            else
            if(value instanceof Date[])
                iter = Arrays.asList((Date[])(Date[])value).iterator();
            else
            if(value instanceof VWAttachment[])
                iter = Arrays.asList((VWAttachment[])(VWAttachment[])value).iterator();
            else
            if(value instanceof VWParticipant[])
                iter = Arrays.asList((VWParticipant[])(VWParticipant[])value).iterator();
            else
            if(value instanceof VWXMLData[])
                iter = Arrays.asList((VWXMLData[])(VWXMLData[])value).iterator();
            else
            if(value instanceof VWGuid[])
                iter = Arrays.asList((VWGuid[])(VWGuid[])value).iterator();
            else
            if(value instanceof Object[])
                iter = Arrays.asList((Object[])(Object[])value).iterator();
            else
            if(value instanceof Iterable)
                iter = ((Iterable)value).iterator();
			else
				try {
					throw new Exception((new StringBuilder()).append("Unsupported array value of type ").append(value.getClass().getCanonicalName()).toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            while(iter.hasNext())
            {
            	propValue =  iter.next();
            	log.info("convertParamValue:propValue is "+propValue);
            }
        }
		return propValue;
	}
	
	private static String convertParamValueToOutputString(Object value, boolean isArray)
	        throws Exception
	    {
	        String strValue;
	        if(value == null)
	        {
	            if(isArray)
	                strValue = "{\"\"}";
	            else
	                strValue = "";
	        } else
	        if(isArray)
	        {
	            StringBuilder sb = new StringBuilder();
	            Iterator iter;
	            if(value instanceof BooleanList)
	                iter = ((BooleanList)value).iterator();
	            else
	            if(value instanceof DateTimeList)
	                iter = ((DateTimeList)value).iterator();
	            else
	            if(value instanceof Float64List)
	                iter = ((Float64List)value).iterator();
	            else
	            if(value instanceof IdList)
	                iter = ((IdList)value).iterator();
	            else
	            if(value instanceof Integer32List)
	                iter = ((Integer32List)value).iterator();
	            else
	            if(value instanceof StringList)
	                iter = ((StringList)value).iterator();
	            else
	            if(value instanceof Integer[])
	                iter = Arrays.asList((Integer[])(Integer[])value).iterator();
	            else
	            if(value instanceof String[])
	                iter = Arrays.asList((String[])(String[])value).iterator();
	            else
	            if(value instanceof Boolean[])
	                iter = Arrays.asList((Boolean[])(Boolean[])value).iterator();
	            else
	            if(value instanceof Double[])
	                iter = Arrays.asList((Double[])(Double[])value).iterator();
	            else
	            if(value instanceof Date[])
	                iter = Arrays.asList((Date[])(Date[])value).iterator();
	            else
	            if(value instanceof VWAttachment[])
	                iter = Arrays.asList((VWAttachment[])(VWAttachment[])value).iterator();
	            else
	            if(value instanceof VWParticipant[])
	                iter = Arrays.asList((VWParticipant[])(VWParticipant[])value).iterator();
	            else
	            if(value instanceof VWXMLData[])
	                iter = Arrays.asList((VWXMLData[])(VWXMLData[])value).iterator();
	            else
	            if(value instanceof VWGuid[])
	                iter = Arrays.asList((VWGuid[])(VWGuid[])value).iterator();
	            else
	            if(value instanceof Object[])
	                iter = Arrays.asList((Object[])(Object[])value).iterator();
	            else
	            if(value instanceof Iterable)
	                iter = ((Iterable)value).iterator();
	            else
	                throw new Exception((new StringBuilder()).append("Unsupported array value of type ").append(value.getClass().getCanonicalName()).toString());
	            for(; iter.hasNext(); sb.append('\'').append(singleValueToString(iter.next())).append('\''))
	                if(sb.length() > 0)
	                    sb.append(",");

	            sb.insert(0, '{').append('}');
	            strValue = sb.toString();
	        } else
	        {
	            strValue = singleValueToString(value);
	        }
	        return strValue;
	    }
	
	private static String singleValueToString(Object value)
	        throws Exception
	    {
	        String strValue = null;
	        if(value == null)
	            strValue = "";
	        else
	        if(value instanceof String)
	            strValue = (String)value;
	        else
	        if((value instanceof Boolean) || (value instanceof Double) || (value instanceof Float) || (value instanceof Long) || (value instanceof Integer) || (value instanceof Id) || (value instanceof VWAttachment) || (value instanceof VWGuid) || (value instanceof VWXMLData))
	        {
	        	//strValue = value.toString();
	        }
	        else
	        if(value instanceof Date)
	        {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	            sdf.setTimeZone(new SimpleTimeZone(0, "UTC"));
	            strValue = sdf.format((Date)value);
	        } else
	        if(value instanceof VWParticipant)
	            strValue = ((VWParticipant)value).getParticipantName();
	        else
	            throw new Exception((new StringBuilder()).append("Unsupported value of type ").append(value.getClass().getCanonicalName()).toString());
	        return strValue;
	    }
}
