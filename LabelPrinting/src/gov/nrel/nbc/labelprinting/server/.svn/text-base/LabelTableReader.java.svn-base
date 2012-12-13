package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.client.LabelDTO;
import gov.nrel.nbc.security.dbUtils.DBUtils;
import gov.nrel.nbc.labelprinting.utils.XLogger;

public class LabelTableReader implements AppConstants {

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(LabelTableReader.class);
      /**
       * this method will retrieve and read the emails from the INBOX
       * @param dbPath
       * @param dbUser
       * @param dbPass
       */
      public void receive(String dbPath, String dbUser, String dbPass)
      {
          DBUtils db = null;
          boolean ret = false;
          try
          {
        	  db = new DBUtils(dbPath,dbUser,dbPass);
        	  String sql = "select id, session_id, label from "+LABEL_TABLE;
        	  db.performQuery(sql, false);
        	  while (db.getNextRow()) {
            	  long id = db.getLongColumn(1);
            	  String session = db.getStringColumn(2);
            	  String label = db.getStringColumn(3);
                  ret = processMessage(id, session, label);
                  if (ret) {
	                  String sql2 = "delete from "+LABEL_TABLE+" where id="+id;
	                  int rows = db.performDelete(sql2);
	                  log.info("rows deleted="+rows);
                  }
              }
          }
          catch (Exception ex)
          {
                ex.printStackTrace();
          }
          finally
          {
                db.close();
          }
      }
      
      /**
       * this method will process an email message and write
       * relevant contents into the xml file
       * @param message
       */
      public boolean processMessage(long id, String session, String msg)
      {
    	  boolean ret = false;
            try
            {
               LabelDTO label = new LabelDTO();
               /*
	           description1,2,3
	           form
	           fraction
	           composition
	           sampleid
        	   strain
        	   destination
        	   custodian
               entry date	                        
	           */
               String printer = "15747s_18200s";
               String[] lines = msg.split("\n");
               for (int i=0;i<lines.length;i++) {
	               String[] parts = lines[i].split("\t");
	               if (parts.length>1) {
	            	   if (parts[0].equals(DESCRIPTION))
	            		   label.setDescription(parts[1]);	                   
	            	   else if (parts[0].equals(FORM))	               
	            		   label.setForm(parts[1]);	                   
	            	   else if (parts[0].equals(FRACTION))	               
	            		   label.setFraction(parts[1]);	                   
	            	   else if (parts[0].equals(COMPOSITION))	               
	            		   label.setComposition(parts[1]);	                   
	            	   else if (parts[0].equals(SAMPLEID))	               
	            		   label.setSampleId(parts[1]);	                   
	            	   else if (parts[0].equals(STRAIN))	               
	            		   label.setStrain(parts[1]);	                   
	            	   else if (parts[0].equals(DESTINATION))	               
	            		   label.setDestination(parts[1]);	                   
	            	   else if (parts[0].equals(CUSTODIAN))	               
	            		   label.setCustodian(parts[1]);	                   
	            	   else if (parts[0].equals(ENTRY_DATE))	               
	            		   label.setEntryDate(parts[1]);	                   
	            	   else if (parts[0].equals(TRACKINGID))	               
	            		   label.setTrackingId(parts[1]);
               		   else if (parts[0].equals(PRINTER))  {
            			   printer = parts[1];	                        			
            			   log.info("found printer="+printer);
            		   }
	               }	           
               }
               PrintLabelService pls = new PrintLabelService();
               pls.printLabel(label, printer, "SABC");
               ret = true;
            }
            catch(Exception x)
            {
                  //x.printStackTrace();
            }
            return ret;
      }
      
}