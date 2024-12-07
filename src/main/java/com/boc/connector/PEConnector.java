

package com.boc.connector;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boc.utils.CripUtils;

import filenet.vw.api.VWException;
import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWMapDefinition;
import filenet.vw.api.VWProcess;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWQueueQuery;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWWorkObject;
import filenet.vw.api.VWWorkObjectNumber;
import filenet.vw.api.VWWorkflowDefinition;

/*
Created By SaiMadan on Jun 29, 2016
*/
public class PEConnector
{
	//private Logger log = Logger.getLogger(PEConnector.class);
	private static Logger log = LoggerFactory.getLogger(PEConnector.class);
	 private VWSession pesession = null;
	 String comment = null;
	 String wobNumberConst  = null;
	 private VWSession getPESession(String ceuri,String username, String password,String connectionpointname) throws VWException
	    {		
		 log.debug("> Getting PE Session ");

	        if (pesession == null || !(pesession.isLoggedOn()))
	        {
	            pesession = new VWSession();
	            pesession.setBootstrapCEURI(ceuri);
	            pesession.logon(username,password,connectionpointname);
	        }
	        
	        log.debug("< Exiting PE Session"+pesession);
	        
	        return pesession;
	    }
	 
	
	 /**
	    * 
	    * @param searchCriteria
	    * @param workflowDataFields
	    * @param isDispatchWorkitem
	    * @throws VWException
	    */
	 
	 	/*public void dispatchWorkitem(Map<String, Object> searchCriteria, Map<String, Object> workflowDataFields, Boolean isDispatchWorkitem, String rosterName,String mapName,String activityName) throws VWException
	 			{
			 		
			 		VWRosterQuery rosterQuery;
			        VWWorkObject rosterElement;
			        VWProcess process;
			        VWWorkflowDefinition workflowDefinition;
			        VWMapDefinition[] maps;
	 				ResourceBundle rsbundle = ResourceBundle.getBundle("config");
			    	String ceUri = rsbundle.getString("CEURI");
			    	String userId = rsbundle.getString("CMUSERID");
			    	String encryptPasswd = rsbundle.getString("CMPASSWORD");
			    	//String password = CripUtils.decryptStr(encryptPasswd);
			    	String password = "boc@123";
			    	String connectionPoint = rsbundle.getString("CMCONNECTIONPOINT");
			    	String f_InstrSheetName = rsbundle.getString("F_INSTRSHEETNAME");
			    	String f_operation = rsbundle.getString("F_OPERATION");
			    	mapName = "Create Loan Account";
			    	comment = rsbundle.getString("COMMENT");
			    	wobNumberConst = rsbundle.getString("WOB_NUMBER");
			    	if (pesession == null || !(pesession.isLoggedOn()))
		            {
		                getPESession(ceUri,userId,password,connectionPoint);
		            }
			    	final VWRoster roster = pesession.getRoster(rosterName);
			    	System.out.println("roster is "+roster);
			    	if(roster == null) {
		                log.error("Roste Not Found "+roster);
			    		return;
		            }
			    	int queryFlag;// = VWRoster.QUERY_NO_OPTIONS;
			    	String filters;
			    	
			    	String queueName = "Inbox(0)";
			    	VWQueue queue = pesession.getQueue(queueName);
			    	System.out.println("Queue Depth: " + queue.fetchCount());
			    	
			    	Object[] queryMin = new Object[2]; 
			    	Object[] queryMax = new Object[2]; 
			    	String wob = new String(); 
			    	wob = "0xA097AB56BCBC5040AF5AC23B824149EF";
			    	String queryIndex = "F_WobNum"; 
			    	queryMin[0] = wob; 
			    	queryMax[0] = wob; 
			    	queryFlag = VWQueue.QUERY_MIN_VALUES_INCLUSIVE + VWQueue.QUERY_MAX_VALUES_INCLUSIVE; 
			    	int fetchType = VWFetchType.FETCH_TYPE_WORKOBJECT;
			    	
			    	rosterQuery = roster.createQuery(queryIndex,queryMin,queryMax,queryFlag,null,null,fetchType);
			    			// Process Results
			    	System.out.println("query count is "+rosterQuery.fetchCount());
			    			while(rosterQuery.hasNext()) {
			    				
			    				VWWorkObject workObject = (VWWorkObject) rosterQuery.next();
				                String f_instrshhetname=workObject.getFieldValue(f_InstrSheetName).toString();
				                log.info("F_INSTRSHEETNAME -"+ f_instrshhetname);
				                if(f_instrshhetname.equalsIgnoreCase(mapName))
					            {  
				                	workObject.doLock(true);
				    				
					                 workObject = getValidWorkObjectParameter(workObject, workflowDataFields);
					                
					                log.info("F_Operation -"+ workObject.getFieldValue(f_operation));
					                log.info("isDispatchWorkitem -"+ isDispatchWorkitem);
					                if (isDispatchWorkitem && workObject.getFieldValue(f_operation).toString().equalsIgnoreCase(activityName))
					                {
					                	log.info("Dispatch WOB");
					                	 
					                    workObject.doSave(false);
					                    workObject.doRefresh(false, true);
					                    workObject.doDispatch();
					                	
					                }
					                else
					                {
					                	log.info("Do Not Dispatch WOB. Save the Data.");
					                    workObject.doSave(true);
					                    workObject.doRefresh(false, false);
					                }
					            }
			    			}
			    	
			    	filters = "F_WobNum = :1";
			    	//filters = "F_PRODUCTCODE = :80";
			    	rosterQuery = roster.createQuery(null, null, null, queryFlag, filters, null, VWFetchType.FETCH_TYPE_WORKOBJECT);
			    	if (rosterQuery.fetchCount() < 1) {
		                System.out.println("No roster elements found");
		                log.error("No roster elements found");
		                pesession.logoff();
		                return;
		            }
	 			}*/
	 
	 
	    public void dispatchWorkitem(Map<String, Object> searchCriteria, Map<String, Object> workflowDataFields, Boolean isDispatchWorkitem, String rosterName,String mapName,String activityName) throws VWException
	    {
	    	ResourceBundle rsbundle = ResourceBundle.getBundle("config");
	    	String ceUri = rsbundle.getString("CEURI");
	    	String userId = rsbundle.getString("CMUSERID");
	    	String encryptPasswd = rsbundle.getString("CMPASSWORD");
	    	String password = CripUtils.decryptStr(encryptPasswd);
	    	String connectionPoint = rsbundle.getString("CMCONNECTIONPOINT");
	    	String f_InstrSheetName = rsbundle.getString("F_INSTRSHEETNAME");
	    	String f_operation = rsbundle.getString("F_OPERATION");
	    	String delayStepName = rsbundle.getString("F_DELAYSTEPNAME");
	    	comment = rsbundle.getString("COMMENT");
	    	wobNumberConst = rsbundle.getString("WOB_NUMBER");
	    	
	    	Object[] queryMin = new Object[2]; 
	    	Object[] queryMax = new Object[2]; 
	    	String wob = new String(); 
	    	//wob = "0x66BF502FAF34D44A8FE57C80EB1274D8";
	    	//wob = "0xD1A1F05D813AF548BBB956AA83D0ADB3";
	    	//System.out.println();
	    
	    	String wobNumber = (String) searchCriteria.get("F_WobNum");
	    	log.info("wobNumber is "+wobNumber);
	    	wob = "0x"+wobNumber;
	    	String queryIndex = "F_WobNum"; 
	    	queryMin[0] = wob; 
	    	queryMax[0] = wob; 
	    	int fetchType = VWFetchType.FETCH_TYPE_WORKOBJECT;
	    	int queryFlag = VWQueue.QUERY_MIN_VALUES_INCLUSIVE + VWQueue.QUERY_MAX_VALUES_INCLUSIVE; 
	    	
	    	
	        if (log.isTraceEnabled())
	        {
	            log.trace("> Entering dispatchWorkitem method");
	        }

	            if (pesession == null || !(pesession.isLoggedOn()))
	            {
	                getPESession(ceUri,userId,password,connectionPoint);
	            }
	            
	            final VWRoster roster = pesession.getRoster(rosterName);
	          

	            int queryFlags = VWRoster.QUERY_NO_OPTIONS;

	            final FNRosterFilter filter = getFNRosterFilter(searchCriteria);
	            
	            // Perform Query
	            //final VWRosterQuery query = roster.createQuery(null, null, null, queryFlags, filter.getQueryFilter(), filter.getSubstitutionVars(), VWFetchType.FETCH_TYPE_WORKOBJECT);
	            //log.info(">bfr query.hasNext()");
	            //log.info(query.fetchCount()+" :MapName::"+mapName);
	            //while(query.hasNext())
	            //{
	            	 //VWWorkObject workObject = (VWWorkObject) query.next();
	            	forLoop : for(int i =0;i<=3;i++)
		            {	 
	            	     
		            	try
		            	{	
		            		log.info("filter.getQueryFilter() is "+filter.getQueryFilter());
		            		VWRosterQuery query = roster.createQuery(queryIndex,queryMin,queryMax,queryFlag,null,null,fetchType);
		            		//VWRosterQuery query = roster.createQuery(null, null, null, queryFlags, filter.getQueryFilter(), filter.getSubstitutionVars(), VWFetchType.FETCH_TYPE_WORKOBJECT);
		                     log.info(">bfr query.hasNext()");
		                     log.info("Number Of WOB retrieved:"+query.fetchCount()+" & MapName retrieved From DB:"+mapName);
		                     while(query.hasNext())
			                 {
			                    VWWorkObject workObject = (VWWorkObject) query.next();
				                String f_instrshhetname=workObject.getFieldValue(f_InstrSheetName).toString();
				                log.info("F_INSTRSHEETNAME -"+ f_instrshhetname);
				                if(f_instrshhetname.equalsIgnoreCase(mapName))
					            {  
				                 workObject.doLock(true);
				
				                 //workObject = getValidWorkObjectParameter(workObject, workflowDataFields);
				                
				                //log.info("F_Operation -"+ workObject.getFieldValue(f_operation));
				                log.info("isDispatchWorkitem -"+ isDispatchWorkitem);
				
				               // if ((!(workObject.getFieldValue(ProcessEngineConstants.F_INSTRSHEETNAME).toString().equalsIgnoreCase(getProperty("waitconditionstepname")))) && isDispatchWorkitem && workObject.getFieldValue(ProcessEngineConstants.F_OPERATION).toString().equalsIgnoreCase(getProperty("delaystepname")))
				                if (isDispatchWorkitem && workObject.getFieldValue(f_operation).toString().equalsIgnoreCase(delayStepName))
				                {
				                	log.info("Dispatch WOB");
				                	 
				                    workObject.doSave(false);
				                    workObject.doRefresh(false, true);
				                    workObject.doDispatch();
				                	
				                }
				                else
				                {
				                	log.info("Do Not Dispatch WOB. Save the Data.");
				                    workObject.doSave(true);
				                    workObject.doRefresh(false, false);
				                }
					            }
				                else
				                {
				                	log.info(" error for -" + i + " time. Sub Map name mismatch.");
				                	throw new VWException("Sub Map Exception","Exception while comparing submap name ");
				                }
			               }
			                break forLoop;
		            	} 
		            	catch (Exception e)
		            	{
							if(i<3)
							{
								try {	
									log.error(" error for -" + i + " time");
									Thread.sleep(1000);
								} catch (Exception e2) {
									log.error("Exception while suspending the thread - ",e2);
									throw new VWException(e2);
								}
							}
							
							if(i>2)
							{
								log.error("After looping:Exception Dispatching the Workobject - ",e);
								throw new VWException(e);
							}
									
								
						}
	                
	            	}
	            //}
	            

	    }
	    
	    private VWWorkObject getValidWorkObjectParameter(VWWorkObject workObject, Map<String, Object> workflowDataFields) throws VWException
	    {
	        final Iterator<Entry<String, Object>> iterator = workflowDataFields.entrySet().iterator();

	        while (iterator.hasNext())
	        {
	            final Map.Entry<String, Object> entry = iterator.next();
	            final String key = entry.getKey();
	            final Object value = entry.getValue();
	            log.info("key -" + key + ", value -" + value);
	            if(workObject.hasFieldName(key))
	            {
	            	if (value instanceof Integer)
	                {
	                    workObject.setFieldValue(key, (Integer) value, false);
	                }
	                if (value instanceof String)
	                {
	                	if(key.equalsIgnoreCase(comment))
	                	{
	                		 workObject.setFieldValue(key, (String)(value + "-" + workObject.getFieldValue(key)), false);
	                	}
	                	else
	                	{
	                		workObject.setFieldValue(key, (String) value, false);
	                	}
	                }

	                if (value instanceof Double)
	                {
	                    workObject.setFieldValue(key, (Double) value, false);
	                }

	                if (value instanceof Float)
	                {

	                    workObject.setFieldValue(key, (Float) value, false);
	                }

	                if (value instanceof Date)
	                {
	                    workObject.setFieldValue(key, (Date) value, false);
	                }
	                if (entry.getValue() instanceof Boolean)
	                {

	                    workObject.setFieldValue(key, (Boolean) value, false);
	                }
	                
	                if (value instanceof Integer[])
	                {
	                    workObject.setFieldValue(key, (Integer[]) value, false);
	                }
	                
	                
	                if (value instanceof String[])
	                {
	                	
	                		workObject.setFieldValue(key, (String[]) value, false);
	                	
	                }

	                if (value instanceof Double[])
	                {
	                    workObject.setFieldValue(key, (Double[]) value, false);
	                }

	                if (value instanceof Float[])
	                {

	                    workObject.setFieldValue(key, (Float[]) value, false);
	                }

	                if (value instanceof Date[])
	                {
	                    workObject.setFieldValue(key, (Date[]) value, false);
	                }
	                if (entry.getValue() instanceof Boolean[])
	                {

	                    workObject.setFieldValue(key, (Boolean[]) value, false);
	                }
	            }
	            else
	            	log.trace("key -" + key + " Does not exists");
	            
	        }
	        
	        return workObject;
	    }
	    
	    
	    private FNRosterFilter getFNRosterFilter(Map<String, Object> searchCriteria)
	    {
	        final Object subVars[] = new Object[searchCriteria.size()];
	        
	        int k = 0;

	        final StringBuffer sb = new StringBuffer();

	        boolean first = true;

	        for (final Iterator<Entry<String, Object>> iterator1 = searchCriteria.entrySet().iterator(); iterator1.hasNext(); k++)
	        {
	            final Map.Entry<String, Object> entry = iterator1.next();
	            final String key = entry.getKey();
	            final Object value = entry.getValue();
	            if (first)
	            {            	
	                sb.append('(').append(key).append(" = :").append((k + 1));               
	                first = false;
	            	
	            }
	            
	            else
	            {
	                sb.append(" and ").append(key).append(" = :").append((k + 1));
	            }

	            if (key.equalsIgnoreCase(wobNumberConst))
	            {

	                VWWorkObjectNumber vwWorkObjectNumber = new VWWorkObjectNumber(value.toString());
	                subVars[k] = vwWorkObjectNumber;
	            }

	            if (value instanceof Integer)
	            {
	                subVars[k] = value;
	            }

	            if (value instanceof String)
	            {
	                subVars[k] = value;
	            }

	            if (value instanceof Double)
	            {
	                subVars[k] = value;
	            }

	            if (value instanceof Float)
	            {

	                subVars[k] = value;
	            }

	            if (value instanceof Date)
	            {
	                subVars[k] = value;
	            }
	            if (value instanceof Boolean)
	            {

	                subVars[k] = value;
	            }
	        }

	        sb.append(')');
	        log.debug("sb-"+ sb.toString());

	        return new FNRosterFilter(sb.toString(), subVars);
	    }

	    private static class FNRosterFilter extends Object
	    {
	        
	        private String queryFilter;
	        
	        private Object[] substitutionVars;

	        public FNRosterFilter(String queryFilter, Object[] substitutionVars)
	        {
	            this.queryFilter = queryFilter;
	            this.substitutionVars = substitutionVars;
	        }
	        
	        public String getQueryFilter()
	        {
	            return queryFilter;
	        }

	        public Object[] getSubstitutionVars()
	        {
	            return substitutionVars;
	        }        
	    }
	    
}
