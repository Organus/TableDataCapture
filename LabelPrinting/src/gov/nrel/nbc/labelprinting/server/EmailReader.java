package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.client.LabelDTO;
import gov.nrel.nbc.labelprinting.utils.XLogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class EmailReader implements AppConstants {

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
      private static final XLogger log = new XLogger(EmailReader.class);
      /**
       * this method will retrieve and read the emails from the INBOX
       * @param popServer
       * @param popUser
       * @param popPassword
       */
      public void receive(String popServer, String popUser, String popPassword)
      {
          Store store=null;
          Folder folder=null;
          try
          {
                Properties props = new Properties();
                props.put("mail.store.protocol", "imaps"); 
                props.put("mail.imap.host", popServer);
                props.put("mail.imap.port", "993");
    		    //props.put("mail.debug", "false");
    		    props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    		    props.put("mail.imap.socketFactory.fallback", "false");
                Session session = Session.getDefaultInstance(props);
                session.setDebug(false);
                store = session.getStore("imaps");
                //log.info("trying to connect to "+popServer+" using "+popUser+" and "+popPassword);
                store.connect(popServer, popUser, popPassword);
                folder = store.getDefaultFolder().getFolder("INBOX");
                if (folder == null) throw new Exception("No POP3 INBOX");
                folder.open(Folder.READ_ONLY);
                //log.info("No of Messages : " + folder.getMessageCount());
                //log.info("No of Unread Messages : " + folder.getUnreadMessageCount());
                Message[] msgs = folder.getMessages();
                //log.info("java EmailReader"
                //            +" msgs " + msgs.length);
                boolean found = true;
                for (int msgNum = msgs.length-1; msgNum >= 0 && found; msgNum--)
                {
                	try {
                      Message msg = msgs[msgNum];
                      Calendar cal = Calendar.getInstance();
                      String today = String.valueOf(cal.get(Calendar.MONTH)+1)+"/"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(cal.get(Calendar.YEAR));
                      Date sentDate = msg.getSentDate();
                      Calendar cal1 = Calendar.getInstance();
                      cal1.setTime(sentDate);
                      String sent = String.valueOf(cal1.get(Calendar.MONTH)+1)+"/"+String.valueOf(cal1.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(cal1.get(Calendar.YEAR));                      
                      if (sent.equals(today)) {
                    	  processMessage(msg);
                      }
                      else {
                    	  found = false;
                    	  break;
                      }
                	} catch (Exception e1) {}
                }
                folder.expunge();
          }
          catch (Exception ex)
          {
                ex.printStackTrace();
          }
          finally
          {
                // -- Close down nicely --
                try
                {
                      if (folder!=null) folder.close(false);
                      if (store!=null) store.close();
                }
                catch (Exception ex2) {ex2.printStackTrace();}
          }
      }
      
      /**
       * this method will process an email message and write
       * relevant contents into the xml file
       * @param message
       */
      public void processMessage(Message message)
      {
            try
            {
                  String subject=message.getSubject();
                  if (subject != null && subject.equals(SABC_LABEL_PRINTING)) {
                	  message.setFlag(Flags.Flag.DELETED, true); // set the DELETED flag

	                  // -- Get the message part (i.e. the message itself) --
	                  Part messagePart=message;
	                  Object content=messagePart.getContent();
	            
	                  // -- or its first body part if it is a multipart message --
	                  if (content instanceof Multipart)
	                  {
	                        messagePart=((Multipart)content).getBodyPart(0);
	                  }
	                  
	                  // -- Get the content type --
	                  String contentType=messagePart.getContentType();
	                  
	                  if (contentType.startsWith("text/plain") || contentType.startsWith("text/html"))
	                  {
	                        InputStream is = messagePart.getInputStream();
	
	                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	                                                
	                        // now read the message into a List - each message line is a list item
	                        // a List will be easier to manipulate and its indexed
	                        // remove blank lines at the same time
	                        List<String> msgList = new ArrayList<String>();
	                        
	                        String thisLine=reader.readLine();
	                        while (thisLine!=null)
	                        {
	                              if(thisLine.trim().length()>0)
	                                    msgList.add(thisLine);
	                              thisLine=reader.readLine();
	                        }
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
	                        Iterator<String> it = msgList.iterator();
	                        while (it.hasNext()) {
	                        	String line = it.next();
	                        	String[] parts = line.split("\t");
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
	                  }
                  }
            }
            catch(Exception x)
            {
                  //x.printStackTrace();
            }
            
      }
      
}