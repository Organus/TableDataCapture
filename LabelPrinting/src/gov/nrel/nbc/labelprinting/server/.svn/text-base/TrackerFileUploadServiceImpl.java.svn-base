package gov.nrel.nbc.labelprinting.server;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.dao.AmountUnitsDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.FeedstocksDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.FractionsDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.LocationDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.OriginsDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.StatusDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.TreatmentsDAOHibernate;
import gov.nrel.nbc.labelprinting.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.labelprinting.model.AmountUnits;
import gov.nrel.nbc.labelprinting.model.Feedstocks;
import gov.nrel.nbc.labelprinting.model.Fractions;
import gov.nrel.nbc.labelprinting.model.Location;
import gov.nrel.nbc.labelprinting.model.Origins;
import gov.nrel.nbc.labelprinting.model.Sample;
import gov.nrel.nbc.labelprinting.model.Status;
import gov.nrel.nbc.labelprinting.model.Treatments;
import gov.nrel.nbc.labelprinting.utils.XLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * A <HttpServlet> that implements the methods for the upload of a TRB file.
 * 
 * @author jalbersh
 *
 */
public class TrackerFileUploadServiceImpl extends HttpServlet implements AppConstants {

private static final String FIXED_QUOTE = "`";

private static final String SINGLE_QUOTE = "'";

	/**
	 * A serialization identifier.
	 */
	private static final long serialVersionUID = -4515434818206581128L;

    /**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(TrackerFileUploadServiceImpl.class);

	/**
	 * A reference to the sample
	 */
	private Sample sampleData = null;

	/**
	 * A reference to the sample location
	 */
	private Location locationData = null;
    
	private String tempDirKey = "";
	private String tempDirValue = "";
	private String fileDirKey = "";
	private String fileDirValue = "";
	private String logDirKey = "";
	private String logDirValue = "";
	private File tempDir = null;
	public String db_first = "";
	public String db_last = ""; 
	public String db_tracker = ""; 
	public String db_test = "";
	public String db_prod = "";
	public String db_dev = "";
	public String db_local = "";
	public String pop_host = "";
	public String pop_user = "";
	public String pop_pass = "";
	public String db_user = "";
	public String db_pass = "";
	public String print_method = "";
	
	public TrackerFileUploadServiceImpl() {
		getProperties();
	}
	
	public void getProperties() {
		ResourceBundle rBundle = null;
		
		try {
			rBundle = ResourceBundle.getBundle(TRACKER_PROPERTIES_FILE_NAME);
			tempDir = new File(rBundle.getString(FILE_DIR));
			db_first = rBundle.getString(DB_FIRST);
			db_last = rBundle.getString(DB_LAST);
			db_tracker = rBundle.getString(DB_TRACKER); 
			db_test = rBundle.getString(DB_TEST);
			db_prod = rBundle.getString(DB_PROD);
			db_dev = rBundle.getString(DB_DEV);
			db_local = rBundle.getString(DB_LOCAL);
			pop_user = rBundle.getString(PROP_POP_USER);
			pop_pass = rBundle.getString(PROP_POP_PASS);
			pop_host = rBundle.getString(PROP_POP_HOST);
			db_user = rBundle.getString(DB_USER);
			db_pass = rBundle.getString(DB_PASS);
			print_method = rBundle.getString(PRINT_METHOD);
		} catch (MissingResourceException mre) {
			log.severe("Problem reading resource file: "+mre.getMessage());
			log.warning(TrackerServiceImpl.getStackTrace(mre));
			rBundle = new MyResources();
		}	
	}

	 public class MyResources extends ResourceBundle {
		 private HashMap<String,String> contents = new HashMap<String,String>();
	     public MyResources() {
	        	//	 # Temp directory for temporary files
    		 contents.put(tempDirKey,tempDirValue);
	        	//	 # Directory to store attachments
	         contents.put(fileDirKey,fileDirValue);
	        	//	 # Directory to create log file for files processed.
	         contents.put(logDirKey,logDirValue);	             
	     }
	     protected Object[][] getContents() {
	     
	         return new Object[][] {
	             // LOCALIZE THE SECOND STRING OF EACH ARRAY (e.g., "OK")
	             //{"OkKey", "OK"},
	             //{"CancelKey", "Cancel"},
	        	//	 # Temp directory for temporary files
	        		 {tempDirKey,tempDirValue},
	        	//	 # Directory to store attachments
	        		 {fileDirKey,fileDirValue},
	        	//	 # Directory to create log file for files processed.
	        		 {logDirKey,logDirValue}	             
	        		 // END OF MATERIAL TO LOCALIZE
	        };
	     }
	     

		@SuppressWarnings("unchecked")
		@Override
		public Enumeration<String> getKeys() {
			return (Enumeration<String>) contents.keySet();
		}

		@Override
		protected Object handleGetObject(String key) {
			return contents.get(key);
		}
		
	 }

	/**
	 * A public method that handles the doPost message
	 * 
	 * @param req <HttpServletRequest>
	 * @param rsp <HttpServletResponse>
	 */
    @SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse rsp)
        throws ServletException, IOException {
    	log.fine("doPost called!");
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (isMultipart == true) {
			long sampleId = 0L;
			getProperties();
	        File tdir = createTempDir(tempDir.getPath());
			FileItemFactory itemFactory = new DiskFileItemFactory(1000*1024*1024,tdir);
			ServletFileUpload upload = new ServletFileUpload(itemFactory);
			upload.setFileSizeMax(1000*1024*1024);
			try {
				List<DiskFileItem> items = (List<DiskFileItem>)upload.parseRequest(req);
				Iterator<DiskFileItem> it = items.iterator();
				while (it.hasNext()) {
					DiskFileItem item = it.next();
					if (item.isFormField()==true){
						String name = item.getFieldName();
						String value = item.getString();
						if (value != null)
							value = value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE);
						log.fine(name + " = " + value);
						saveData(name, value);
					}
				}
				if (!items.isEmpty())
					sampleId = persistData();
				else
					log.warning("There were no parameters passed to doPost!");
					
			} catch (FileUploadException fue) {
				log.warning("Error uploading TRB file! Error: " + fue);
			}
			if (sampleData != null && sampleId != 0) {
				PrintWriter writer = rsp.getWriter();
				writer.write(Long.toString(sampleData.getId()));
				writer.close();
			} else {
				log.warning("The sampleData was not saved!");
			}
		}
    }
	
    /**
     * Returns the contents of the file as an array of bytes.  If the
     * contents of the file were not yet cached in memory, they will be
     * loaded from the disk storage and cached.
     *
     * @return The contents of the file as an array of bytes.
     */
    private static File createTempDir(String baseTempPath) {
        //final String baseTempPath = System.getProperty("java.io.tmpdir");

        Random rand = new Random();
        int randomInt = 1 + rand.nextInt();

        File tempDir = new File(baseTempPath + File.separator + "tempDir" + randomInt);
        if (tempDir.exists() == false) {
            tempDir.mkdirs();
        }

        //tempDir.deleteOnExit();

        return tempDir;
    }
    
    /**
     * Private method to save the trb, location and sample data
     * to the database.
     */
    private long persistData() {
    	log.fine("persistData called!");
    	if (sampleData == null) {
    		log.warning("sample data was null. Not good.");
    		return 0L;
    	}
    	Session session = null;
    	try {
			session = HibernateSessionFactory.getSession();
	
			Transaction tx = session.beginTransaction();
			
			Location foundLoc = null;
			if (locationData != null) {
				try {
					Location loc = new Location();
					if (locationData.getBuilding() != null) {loc.setBuilding(locationData.getBuilding().indexOf(SINGLE_QUOTE) == -1 ? locationData.getBuilding() : locationData.getBuilding().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));}
					else loc.setBuilding("");
					if (locationData.getRoom() != null) {loc.setRoom(locationData.getRoom().indexOf(SINGLE_QUOTE) == -1 ? locationData.getRoom() : locationData.getRoom().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));}
					else loc.setRoom("");
					if (locationData.getHolder() != null) {loc.setHolder(locationData.getHolder().indexOf(SINGLE_QUOTE) == -1 ? locationData.getHolder() : locationData.getHolder().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));}
					else loc.setHolder("");
					if (locationData.getShelf() != null) {loc.setShelf(locationData.getShelf().indexOf(SINGLE_QUOTE) == -1 ? locationData.getShelf() : locationData.getShelf().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));}
					else loc.setShelf("");
					if (locationData.getPackaging() != null) {loc.setPackaging(locationData.getPackaging().indexOf(SINGLE_QUOTE) == -1 ? locationData.getPackaging() : locationData.getPackaging().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));}
					else loc.setPackaging("");
					if (locationData.getSubLocation() != null) {loc.setSubLocation(locationData.getSubLocation().indexOf(SINGLE_QUOTE) == -1 ? locationData.getSubLocation() : locationData.getSubLocation().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));}
					else loc.setSubLocation("");
					LocationDAOHibernate ldh = new LocationDAOHibernate();
					ldh.setSession(session);
					List<Location> locs = ldh.findByExample(loc, "id");
					log.info("Matched " + locs.size() + " locations.");
/*					log.info("Building: " + loc.getBuilding());
					log.info("Room: " + loc.getRoom());
					log.info("SubLocation: " + loc.getSubLocation());
					log.info("Shelf: " + loc.getShelf());
					log.info("Holder: " + loc.getHolder());
					log.info("Packaging: " + loc.getPackaging());
*/
					if (locs != null && !locs.isEmpty()) {
						foundLoc = locs.get(0);
						sampleData.setLocation(foundLoc);
					} else {
						locationData = loc;
						sampleData.setLocation(locationData);
						session.save(locationData);
					}
				} catch (Exception e) {
					log.severe("locationData exception: " + e);
					e.printStackTrace();
				}
			}
			
			Feedstocks feedstock = sampleData.getFeedstock();
			if (feedstock != null)
				session.saveOrUpdate(feedstock);
			
			session.save(sampleData);
			
			tx.commit();
			
			return sampleData.getId();
    	} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
    		return 0L;
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
    		return 0L;
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
	}

	/**
	 * private method to return first character or the empty string
	 *  
	 *  @param input <String> input string
	 *  
	 *  @return <String> output
	 */
	private String transformSafety(String input) {
		if (input == null || input.isEmpty()) return "";
		String first = input.substring(0,1);
		return first;
	}
	
	/**
	 * private method to return first characters before the dash or the empty string
	 *  
	 *  @param input <String> input string
	 *  
	 *  @return <String> output
	 */
	private String transformSpecific(String input) {
		int dash = 0;
		String value = "";
		if (input == null || input.isEmpty()) return "";
		dash = input.indexOf(" - ");
		if (dash != -1)
			value = input.substring(0,dash);
		return value;
	}

	/**
     * Private method that saves the sample data to a global.
     * 
     * @param name <String> parameter name
     * @param value <String> parameter value
     * @throws ServletException 
     */
	private void saveData(String name, String value) throws ServletException {
    	log.fine("saveData called!");
		if (sampleData == null) {
			sampleData = new Sample();
			sampleData.setCreateDate(new Date());
			sampleData.setModifyDate(new Date());
		}
		
		if (name.equalsIgnoreCase(SAMPLE_NAME)) {
			sampleData.setSampleId(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_COMMENTS)) {
			sampleData.setComment(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_AMOUNT)) {
			sampleData.setAmount(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_STORAGENOTES)) {
			sampleData.setStorageNotes(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_UNIT)) {
			setUnit(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_EXTERNAL_ID)) {
			sampleData.setExternalId(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_CUSTODIAN_NAME)) {
			sampleData.setCustodianName(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_ORIGIN)) {
			setOrigin(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_OWN_NAME)) {
			sampleData.setOwnerName(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_TREATMENT)) {
			setTreatment(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_FEEDSTOCK)) {
			setFeedstock(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_FRACTION)) {
			setFraction(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_STATUS)) {
			setStatus(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_FIRE)) {
			String fire = transformSafety(value);
			sampleData.setFire(fire);
		} else if (name.equalsIgnoreCase(SAMPLE_REACTIVITY)) {
			String reactivity = transformSafety(value);
			sampleData.setReactivity(reactivity);
		} else if (name.equalsIgnoreCase(SAMPLE_SPECIFIC)) {
			String specific = transformSpecific(value);
			sampleData.setSpecificHazard(specific);
		} else if (name.equalsIgnoreCase(SAMPLE_HEALTH)) {
			String health = transformSafety(value);
			sampleData.setHealth(health);
		} else if (name.equalsIgnoreCase(SAMPLE_BUILDING)) {
			setBuildingLocation(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_ROOM)) {
			setRoomLocation(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_HOLDER)) {
			setHolderLocation(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_SHELF)) {
			setShelfLocation(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_SUBLOCATION)) {
			setSubLocationLocation(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_PACKAGING)) {
			setPackagingLocation(value.indexOf(SINGLE_QUOTE) == -1 ? value : value.replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		} else if (name.equalsIgnoreCase(SAMPLE_TRB_NUM)) {
			int num = -1;
			try {
				if (value != null && !value.isEmpty())
					num = Integer.parseInt(value);
			} catch (NumberFormatException nfe) {
				log.warning("failed to parse num="+value);
			}
			if (num != -1)
				sampleData.setTrbNum(num);
		} else if (name.equalsIgnoreCase(SAMPLE_TRB_PAGE)) {
			int num = -1;
			try {
				if (value != null && !value.isEmpty())
					num = Integer.parseInt(value);
			} catch (NumberFormatException nfe) {
				log.warning("failed to parse page="+value);
			}
			if (num != -1)
				sampleData.setTrbPage(num);
		}
	}

	/**
	 * Private method to set the sublocation for the current sample.
	 * 
	 * @param value <String> sublocation value
	 */
	private void setSubLocationLocation(String value) {
		if (locationData == null) {
			locationData = new Location();
			sampleData.setLocation(locationData);
		}
		if (value == null) {
			locationData.setSubLocation("");
		}
		else {
			locationData.setSubLocation(value);
			
		}
	}

	/**
	 * Private method to set the packaging for the current sample.
	 * 
	 * @param value <String> packaging value
	 */
	private void setPackagingLocation(String value) {
		if (locationData == null) {
			locationData = new Location();
			sampleData.setLocation(locationData);
		}
		if (value == null) {
			locationData.setPackaging("");
			}
		else{
			locationData.setPackaging(value);
		}
	}

	/**
	 * Private method that creates a location, if necessary,
	 *  and sets the shelf value.
	 * 
	 * @param value <String>
	 */
	private void setShelfLocation(String value) {
		if (locationData == null) {
			locationData = new Location();
			sampleData.setLocation(locationData);
		}
		if (value == null) {
			locationData.setShelf("");
		}
		else{
			locationData.setShelf(value);
		}
	}

	/**
	 * Private method that creates a location, if necessary,
	 *  and sets the container value.
	 * 
	 * @param value <String>
	 */
	private void setHolderLocation(String value) {
		if (locationData == null) {
			locationData = new Location();
			sampleData.setLocation(locationData);
		}
		if (value == null) {
			locationData.setHolder("");
		}
		else{
			locationData.setHolder(value);
		}
	}

	/**
	 * Private method that creates a location, if necessary,
	 *  and sets the room value.
	 * 
	 * @param value <String>
	 */
	private void setRoomLocation(String value) {
		if (locationData == null) {
			locationData = new Location();
			sampleData.setLocation(locationData);
		}
		if (value != null) {
			locationData.setRoom("");
		}
		else{
			locationData.setRoom(value);
		}
	}

	/**
	 * Private method that creates a location, if necessary,
	 *  and sets the building value.
	 * 
	 * @param value <String>
	 */
	private void setBuildingLocation(String value) {
		if (locationData == null) {
			locationData = new Location();
			sampleData.setLocation(locationData);
		}
		if (value == null) {
			locationData.setBuilding("");
		}
		else{
			locationData.setBuilding(value);
		}
	}

	/**
	 * Private method to create a fraction, if necessary, and
	 *  set it in the sample data.
	 *  
	 * @param value
	 */
	private void setFraction(String value) {
		if ((value != null) && (!value.isEmpty())) {
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();
				FractionsDAOHibernate fdh = new FractionsDAOHibernate();
				fdh.setSession(session);
				Transaction tx = session.beginTransaction();
				Fractions fraction = fdh.findByName(value);
				if (fraction == null) {
					fraction = new Fractions(value);
				}
				sampleData.setFraction(fraction);
				tx.commit();
			} catch (HibernateException he) {
	    		log.severe("Hibernate exception: " + he);
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.severe("Exception caught: " + ex);
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }
		}
	}

	/**
	 * Private method to create a feedstock, if necessary, and
	 *  set it in the sample data.
	 *  
	 * @param value
	 */
	private void setFeedstock(String value) {
		if ((value != null) && (!value.isEmpty())) {
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();
				Transaction tx = session.beginTransaction();
				FeedstocksDAOHibernate fdh = new FeedstocksDAOHibernate();
				fdh.setSession(session);
				Feedstocks feedstock = fdh.findByName(value);
				if (feedstock == null) {
					feedstock = new Feedstocks(value);
				}
				sampleData.setFeedstock(feedstock);
				tx.commit();
			} catch (HibernateException he) {
	    		log.severe("Hibernate exception: " + he);
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.severe("Exception caught: " + ex);
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }
		}
	}

	/**
	 * Private method to create a treatment, if necessary, and
	 *  set it in the sample data.
	 *  
	 * @param value
	 */
	private void setTreatment(String value) {
		if ((value != null) && (!value.isEmpty())) {
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();
				Transaction tx = session.beginTransaction();
				TreatmentsDAOHibernate sdh = new TreatmentsDAOHibernate();
				sdh.setSession(session);
				Treatments treatment = sdh.findByName(value);
				if (treatment == null) {
					treatment = new Treatments(value);
				}
				sampleData.setTreatment(treatment);
				tx.commit();
			} catch (HibernateException he) {
	    		log.severe("Hibernate exception: " + he);
	    		log.severe(TrackerServiceImpl.getStackTrace(he));
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.severe("Exception caught: " + ex);
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }
		}
	}

	/**
	 * Private method to create an origin, if necessary, and
	 *  set it in the sample data.
	 *  
	 * @param value
	 */
	private void setOrigin(String value) {
		if ((value != null) && (!value.isEmpty())) {
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();
				Transaction tx = session.beginTransaction();
				OriginsDAOHibernate audh = new OriginsDAOHibernate();
				audh.setSession(session);
				Origins origin = audh.findByName(value);
				if (origin == null) {
					origin = new Origins();
					origin.setName(value);
				}
				sampleData.setOrigin(origin);
				tx.commit();
			} catch (HibernateException he) {
	    		log.severe("Hibernate exception: " + he);
	    		log.severe(TrackerServiceImpl.getStackTrace(he));
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.severe("Exception caught: " + ex);
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }
		}
	}

	/**
	 * Private method to create a status, if necessary, and
	 *  set it in the sample data.
	 *  
	 * @param value
	 */
	private void setStatus(String value) {
		if ((value != null) && (!value.isEmpty())) {
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();
				Transaction tx = session.beginTransaction();
				StatusDAOHibernate audh = new StatusDAOHibernate();
				audh.setSession(session);
				Status status = audh.findByName(value);
				if (status == null) {
					status = new Status(value);
				}
				sampleData.setStatus(status);
				tx.commit();
			} catch (HibernateException he) {
	    		log.severe("Hibernate exception: " + he);
	    		log.severe(TrackerServiceImpl.getStackTrace(he));
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.severe("Exception caught: " + ex);
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }
		}
	}

	/**
	 * Private method to create a unit, if necessary, and
	 *  set it in the sample data.
	 *  
	 * @param value
	 */
	private void setUnit(String value) {
		if ((value != null) && (!value.isEmpty())) {
			Session session = null;
			try {
				session = HibernateSessionFactory.getSession();
				Transaction tx = session.beginTransaction();
				AmountUnitsDAOHibernate audh = new AmountUnitsDAOHibernate();
				audh.setSession(session);
				AmountUnits unit = audh.findByName(value);
				if (unit == null) {
					unit = new AmountUnits(value);
				}
				sampleData.setUnits(unit);
				tx.commit();
			} catch (HibernateException he) {
	    		log.severe("Hibernate exception: " + he);
	    		log.severe(TrackerServiceImpl.getStackTrace(he));
	        	try {
	        		if (session != null && session.isConnected())
	        			session.getTransaction().rollback();
	        	} catch (HibernateException rbEx) {
	            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
	        	}
	        } catch (Exception ex) {
	    		log.severe("Exception caught: " + ex);
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }
		}
	}
}
