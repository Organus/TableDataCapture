package gov.nrel.nbc.tracker.server;

import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.dbUtils.DBUtils;
import gov.nrel.nbc.security.model.Users;
import gov.nrel.nbc.security.server.SecurityServiceImpl;
import gov.nrel.nbc.tracker.client.AppConstants;
import gov.nrel.nbc.tracker.client.DevTestProdConstants;
import gov.nrel.nbc.tracker.client.FileInfo;
import gov.nrel.nbc.tracker.client.LabelDTO;
import gov.nrel.nbc.tracker.client.SampleCriteria;
import gov.nrel.nbc.tracker.client.TrackerService;
import gov.nrel.nbc.tracker.dao.AmountUnitsDAOHibernate;
import gov.nrel.nbc.tracker.dao.AttachmentTypeDAOHibernate;
import gov.nrel.nbc.tracker.dao.AttachmentsDAOHibernate;
import gov.nrel.nbc.tracker.dao.AuditDAOHibernate;
import gov.nrel.nbc.tracker.dao.CompositionsDAOHibernate;
import gov.nrel.nbc.tracker.dao.CustodiansDAOHibernate;
import gov.nrel.nbc.tracker.dao.DestinationsDAOHibernate;
import gov.nrel.nbc.tracker.dao.FeedstocksDAOHibernate;
import gov.nrel.nbc.tracker.dao.FormsDAOHibernate;
import gov.nrel.nbc.tracker.dao.FractionsDAOHibernate;
import gov.nrel.nbc.tracker.dao.LocationDAOHibernate;
import gov.nrel.nbc.tracker.dao.OriginsDAOHibernate;
import gov.nrel.nbc.tracker.dao.SampleDAOHibernate;
import gov.nrel.nbc.tracker.dao.StatusDAOHibernate;
import gov.nrel.nbc.tracker.dao.StrainsDAOHibernate;
import gov.nrel.nbc.tracker.dao.TrbDAOHibernate;
import gov.nrel.nbc.tracker.dao.TreatmentsDAOHibernate;
import gov.nrel.nbc.tracker.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.tracker.hibernate.TrbSessionFactory;
import gov.nrel.nbc.tracker.model.AmountUnits;
import gov.nrel.nbc.tracker.model.Attachments;
import gov.nrel.nbc.tracker.model.Audit;
import gov.nrel.nbc.tracker.model.Composition;
import gov.nrel.nbc.tracker.model.Custodians;
import gov.nrel.nbc.tracker.model.Destinations;
import gov.nrel.nbc.tracker.model.Feedstocks;
import gov.nrel.nbc.tracker.model.Forms;
import gov.nrel.nbc.tracker.model.Fractions;
import gov.nrel.nbc.tracker.model.Location;
import gov.nrel.nbc.tracker.model.Origins;
import gov.nrel.nbc.tracker.model.Sample;
import gov.nrel.nbc.tracker.model.Status;
import gov.nrel.nbc.tracker.model.Strains;
import gov.nrel.nbc.tracker.model.Trb;
import gov.nrel.nbc.tracker.model.Treatments;
import gov.nrel.nbc.tracker.utils.XLogger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * A <RemoteServiceServlet> that implements the GWT-RPC methods
 *  for the <TrackerService>.
 * 
 * @author jalbersh
 *
 */
public class TrackerServiceImpl extends RemoteServiceServlet implements
		TrackerService, AppConstants {

	private static final int START_POSITION = 40;

	private static final int MAX_PER_LINE = 51;

	private static final String NEWLINE = "\n";

	private static final String FIXED_QUOTE = "`";

	private static final String SINGLE_QUOTE = "'";

	private static final String DEFAULT_WORKSHEET_NAME = "Samples";

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(TrackerServiceImpl.class);

	/**
	 * Defaulted serial number
	 */
	private static final long serialVersionUID = -7313826634543301340L;

    @Override
    public void checkPermutationStrongName() throws SecurityException {
       return;
    }
    
	/**
	 * Public static method to return a stack trace
	 * 
	 * @param t
	 * @return <String> stack track
	 */
	public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

	/**
	 * Public method that returns a <Collection<String>> of unit names.
	 * 
	 * @return <Collection<String>> List of unit names.
	 */
	public Collection<String> getUnits() {
		Session session = null;
		Collection<String> unitNames = null;
		try {
			session = HibernateSessionFactory.getSession();
			
			AmountUnitsDAOHibernate audh = new AmountUnitsDAOHibernate();
	
			Transaction tx = session.beginTransaction();
			
			unitNames = audh.findAllNames();
			
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((unitNames != null) && (unitNames.isEmpty()))
			unitNames = null;
		
		return unitNames;
	}

	/**
	 * Public method that returns a <Collection<String>> of status names.
	 * 
	 * @return <Collection<String>> List of status names.
	 */
	public Collection<String> getStatusNames(String id) {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			StatusDAOHibernate sdh = new StatusDAOHibernate();

			Transaction tx = session.beginTransaction();
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			//List<Long> sids = ssi.getObjectIdsForUser(id, "status");		
			//stringList = sdh.findAllNames(sids);
			stringList = sdh.findAllNames();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	@Override
	public Collection<String> getStrains() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			StrainsDAOHibernate fdh = new StrainsDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = fdh.findAllNames();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of feedstock names.
	 * 
	 * @return <Collection<String>> List of feedstock names.
	 */
	public Collection<String> getFeedstockNames() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			FeedstocksDAOHibernate fdh = new FeedstocksDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = fdh.findAllNames();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of fraction names.
	 * 
	 * @return <Collection<String>> List of fraction names.
	 */
	public Collection<String> getFractionNames() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			FractionsDAOHibernate fdh = new FractionsDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = fdh.findAllNames();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of treatment names.
	 * 
	 * @return <Collection<String>> List of treatment names.
	 */
	public Collection<String> getTreatmentNames() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			TreatmentsDAOHibernate sdh = new TreatmentsDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = sdh.findAllNames();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	
    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of attachment names.
	 * 
	 * @return <Collection<String>> List of treatment names.
	 */
	public Collection<FileInfo> findAttachmentsInfoByTrackingId(long trackingId) {
		Session session = null;
		Collection<FileInfo> infoList = new ArrayList<FileInfo>();
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);
			
			Sample sample = sdh.findById(trackingId, false);
			
			Set<Attachments> attachList = sample.getAttachments();
		
			Iterator<Attachments> it = attachList.iterator();
			while (it.hasNext()) {
				Attachments attachment = it.next();
				FileInfo info = new FileInfo();
				info.setFilename(attachment.getFilename());
				info.setPath(attachment.getPath());
				info.setAttachment_id(String.valueOf(attachment.getAttachment_id()));
				info.setName(attachment.getFilename());
				infoList.add(info);
			}
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	
    	if ((infoList != null) && (infoList.isEmpty()))
			infoList = null;
		
		return infoList;
	}

	/**
	 * Public method that returns a <Collection<String>> of attachment names.
	 * 
	 * @return <Collection<String>> List of treatment names.
	 */
	public Collection<String> findAttachmentsByTrackingId(long trackingId) {
		Session session = null;
		Collection<String> stringList = new ArrayList<String>();
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);
			
			Sample sample = sdh.findById(trackingId, false);
			
			Set<Attachments> attachList = sample.getAttachments();
		
			Iterator<Attachments> it = attachList.iterator();
			while (it.hasNext()) {
				Attachments attachment = it.next();
				stringList.add(attachment.getFilename());
			}
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	
    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method to query the database for samples based on 
	 *  the <SampleCriteria>.
	 * 
	 * @param criteria <SampleCriteria> Search criteria
	 * 
	 * @return <Collection<SampleCriteria>> Collection of samples
	 */
	public int searchSamplesCount(List<SampleCriteria> criterias, String id) {
		Session session = null;
		Collection<SampleCriteria> sampleCrits = null; 
		List<Sample> samples = null;
		SampleDAOHibernate sdh = new SampleDAOHibernate();
		try {
			session = HibernateSessionFactory.getSession();
			
			sdh.setSession(session);
			
			Transaction tx = session.beginTransaction();
			
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			// TODO
			//String id = getThreadLocalRequest().getSession().getId();
			//String id = Cookies.getCookie("SessionID");
			//ssi.setModifiedTime(id);
			//ssi.audit(id, "search");
			//String sessionId = System.getProperty("sessionId");
			log.info("searchSamples:sessionId="+id);
			if (id.equals(""))
				id = JUNIT_SESSION_ID;
			/*
			SampleCriteria criteria = new SampleCriteria();
			if (id.equals(JUNIT_SESSION_ID)) {
				//criteria.getGroups().add("SABC");
				//criteria.getGroups().add("ALGAE");
				criteria.getGroups().add(DevTestProdConstants.DISPLAY_MODE.toUpperCase());
			}
			criterias.add(criteria);

				if (criteria.getGroups() != null && criteria.getGroups().size()>0) {
					List<Long> sids = ssi.getObjectIdsForUser(criteria.getGroups(), "sample");
					log.info("got "+sids.size()+" objects");
					if (sids != null && sids.size()>0)
						criteria.setTrackingIds(sids);
					else
						return null;
				} else {
					List<Long> sids = ssi.getObjectIdsForUser(id, "sample");
					if (sids != null && sids.size()>0)
						criteria.setTrackingIds(sids);
				}
			}
			*/
			//samples = sdh.findByCriteria(criterias);
			int num = sdh.findByCriteria(criterias);
			
			tx.commit();
	    	
	    	/*
	    	if ((samples == null) || (samples.isEmpty()))
	    		return 0;
	    	else if (samples == null) {
	    		return 0;
	    	} else {
	    		return samples.size();
	    	}	
	    	*/
	    	return (int)num;
	    	
		} catch (HibernateException he) {
			log.warning("A Hibernate exception was thrown. Exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
    	} finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	return 0;
	}

	/**
	 * Public method to query the database for samples based on 
	 *  the <SampleCriteria>.
	 * 
	 * @param criteria <SampleCriteria> Search criteria
	 * 
	 * @return <Collection<SampleCriteria>> Collection of samples
	 */
	public Collection<SampleCriteria> searchSamples(List<SampleCriteria> criterias, String id, int start, int pageSize) {
		Session session = null;
		Collection<SampleCriteria> sampleCrits = null; 
		List<Sample> samples = null;
		SampleDAOHibernate sdh = new SampleDAOHibernate();
		try {
			session = HibernateSessionFactory.getSession();
			
			sdh.setSession(session);
			
			Transaction tx = session.beginTransaction();
			
			//SecurityServiceImpl ssi = new SecurityServiceImpl();
			// TODO
			//String id = getThreadLocalRequest().getSession().getId();
			//String id = Cookies.getCookie("SessionID");
			//ssi.setModifiedTime(id);
			//ssi.audit(id, "search");
			//String sessionId = System.getProperty("sessionId");
			log.info("searchSamples:sessionId="+id);
			if (id.equals(""))
				id = JUNIT_SESSION_ID;
			
			/*
			SampleCriteria criteria = new SampleCriteria();
			if (id.equals(JUNIT_SESSION_ID)) {
				//criteria.getGroups().add("SABC");
				//criteria.getGroups().add("ALGAE");
				criteria.getGroups().add(DevTestProdConstants.DISPLAY_MODE.toUpperCase());
			}
			criterias.add(criteria);

				if (criteria.getGroups() != null && criteria.getGroups().size()>0) {
					List<Long> sids = ssi.getObjectIdsForUser(criteria.getGroups(), "sample");
					log.info("got "+sids.size()+" objects");
					if (sids != null && sids.size()>0)
						criteria.setTrackingIds(sids);
					else
						return null;
				} else {
					List<Long> sids = ssi.getObjectIdsForUser(id, "sample");
					if (sids != null && sids.size()>0)
						criteria.setTrackingIds(sids);
				}
			}
			*/
			samples = sdh.findByCriteria(criterias,start,pageSize);
			if (samples.size()>0) {
				log.warning("PROJECT="+samples.get(0).getProject());
			}
			
			tx.commit();
	    	
	    	if ((samples == null) || (samples.isEmpty()))
	    		return null;
	    	else if (samples == null) {
	    		return null;
	    	} else {
	    		sampleCrits = sdh.convertSamples2Criteria(samples);
	    		return sampleCrits;
	    	}	
	    	
		} catch (HibernateException he) {
			log.warning("A Hibernate exception was thrown. Exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
    	} finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	return null;
	}

	/**
	 * Public method to query the database for samples based on 
	 *  the <SampleCriteria>.
	 * 
	 * @param criteria <SampleCriteria> Search criteria
	 * 
	 * @return <Collection<SampleCriteria>> Collection of samples
	 */
	public Collection<SampleCriteria> searchSamples(SampleCriteria criteria, String id) {
		Session session = null;
		Collection<SampleCriteria> sampleCrits = null; 
		List<Sample> samples = null;
		SampleDAOHibernate sdh = new SampleDAOHibernate();
		try {
			session = HibernateSessionFactory.getSession();
			
			sdh.setSession(session);
			
			Transaction tx = session.beginTransaction();
			
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			// TODO
			//String id = getThreadLocalRequest().getSession().getId();
			//String id = Cookies.getCookie("SessionID");
			ssi.setModifiedTime(id);
			ssi.audit(id, "search");
			//String sessionId = System.getProperty("sessionId");
			log.info("searchSamples:sessionId="+id);
			if (id.equals(""))
				id = JUNIT_SESSION_ID;
			
			if (id.equals(JUNIT_SESSION_ID)) {
				//criteria.getGroups().add("SABC");
				//criteria.getGroups().add("ALGAE");
				criteria.getGroups().add(DevTestProdConstants.DISPLAY_MODE.toUpperCase());
			}

			/*
				if (criteria.getGroups() != null && criteria.getGroups().size()>0) {
					List<Long> sids = ssi.getObjectIdsForUser(criteria.getGroups(), "sample");
					log.info("got "+sids.size()+" objects");
					if (sids != null && sids.size()>0)
						criteria.setTrackingIds(sids);
					else
						return null;
				} else {
					List<Long> sids = ssi.getObjectIdsForUser(id, "sample");
					if (sids != null && sids.size()>0)
						criteria.setTrackingIds(sids);
				}
			}
			*/
			samples = sdh.findByCriteria(criteria);
			
			tx.commit();
	    	
	    	if ((samples == null) || (samples.isEmpty()))
	    		return null;
	    	else if (samples == null) {
	    		return null;
	    	} else {
	    		sampleCrits = sdh.convertSamples2Criteria(samples);
	    		return sampleCrits;
	    	}	
	    	
		} catch (HibernateException he) {
			log.warning("A Hibernate exception was thrown. Exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
    	} finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	return null;
	}

	/**
	 * Public method that returns a <Collection<String>> of origin names.
	 * 
	 * @return <Collection<String>> List of origin names.
	 */
	public List<String> getOrigins(String id) {
		List<String> origins = new ArrayList<String>();
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			
			Transaction tx = session.beginTransaction();
			
			OriginsDAOHibernate odh = new OriginsDAOHibernate();			
			odh.setSession(session);
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			//List<Long> sids = ssi.getObjectIdsForUser(id, "origins");
			//origins = odh.findAllNames(sids);
			origins = odh.findAllNames();
			
			tx.commit();
		} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.warning(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } catch (Exception e) {
			log.severe("Problem parsing configs. Error: " + e);
			log.warning(TrackerServiceImpl.getStackTrace(e));
			return null;
		} finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
		return origins;
	}

	/**
	 * Public method that returns a <Status> using the
	 *  status name.
	 * @param session 
	 * 
	 * @param value <String> Status name.
	 * @return <Status>
	 */
	private Origins getOrigin(Session session, String value) throws HibernateException {
		OriginsDAOHibernate odh = new OriginsDAOHibernate();
		odh.setSession(session);
		Origins origin = odh.findByName(value);
		if (origin == null && value != null && !value.isEmpty()) {
			origin = new Origins();
			origin.setName(value);
		}
		return origin;
	}

	/**
	 * Public method that returns a <Collection<String>> of sample names.
	 * 
	 * @return <Collection<String>> List of sample names.
	 */
	public Collection<String> getSampleIds(String id) {
		Session session = null;
		Collection<String> sampleNames = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			//List<Long> sids = ssi.getObjectIdsForUser(id, "sample");		
			sampleNames = sdh.findAllSampleIds();//findAllSampleIds(sids);
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

		if ((sampleNames != null) && (sampleNames.isEmpty()))
			sampleNames = null;
		
		return sampleNames;
	}

	/**
	 * Public method that returns a <Collection<String>> of location building names.
	 * 
	 * @return <Collection<String>> List of location building names.
	 */
	public Collection<String> getBuildings() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			LocationDAOHibernate ldh = new LocationDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = ldh.findAllBuildings();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
		
		if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of location container names.
	 * 
	 * @return <Collection<String>> List of location container names.
	 */
	public Collection<String> getHolders() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			LocationDAOHibernate ldh = new LocationDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = ldh.findAllHolders();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	
		if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of location room names.
	 * 
	 * @return <Collection<String>> List of location room names.
	 */
	public Collection<String> getRooms() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			LocationDAOHibernate ldh = new LocationDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = ldh.findAllRooms();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

		if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of location shelf names.
	 * 
	 * @return <Collection<String>> List of location shelf names.
	 */
	public Collection<String> getShelves() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			LocationDAOHibernate ldh = new LocationDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = ldh.findAllShelves();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

		if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of location packaging names.
	 * 
	 * @return <Collection<String>> List of location packaging names.
	 */
	public Collection<String> getPackaging() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			LocationDAOHibernate ldh = new LocationDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = ldh.findAllPackaging();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	
		if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a <Collection<String>> of location sublocation names.
	 * 
	 * @return <Collection<String>> List of location sublocation names.
	 */
	public Collection<String> getSubLocations() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			LocationDAOHibernate ldh = new LocationDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			stringList = ldh.findAllSubLocations();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
    	
		if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method to find a sample using a sample ID
	 * 
	 * @param sampleId long Sample ID
	 * 
	 * @return <SampleCriteria> Sample data
	 */
	public SampleCriteria findSampleById(long sampleId) {
		Session session = null;
		SampleCriteria criteria = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();

			Transaction tx = session.beginTransaction();
		
			Sample theSample = sdh.findById(new Long(sampleId), false);
			
			criteria = sdh.convertSample2Criterium(theSample);
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
		
		return criteria;
	}

	/**
	 * Public method to remove an attachment, given an attachment ID
	 *  and its parent sample if it exists.
	 * 
	 * @param attachmentId long <Attachments> ID
	 * @param crit <SampleCriteria> parent <Sample> if it exists; may be null
	 * 
	 * @return <Boolean> success or failure to remove
	 */
	public Boolean removeAttachment(long attachmentId, SampleCriteria crit) {
		Session session = null;
		Boolean worked = true;
		try {
			session = HibernateSessionFactory.getSession();
		
			AttachmentsDAOHibernate adh = new AttachmentsDAOHibernate();
			adh.setSession(session);
			
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			// Update Sample
			if (crit != null) {
				Sample sample = sdh.findById(crit.getTrackingId(), false);
				if (sample != null) {
					Set<Attachments> attachments = sample.getAttachments();
					if (attachments != null && !attachments.isEmpty()) {
						List<Attachments> attachmentsList = new ArrayList<Attachments>(attachments);
						ListIterator<Attachments> it = attachmentsList.listIterator();
						while (it.hasNext()) {
							Attachments attachment = it.next();
							if (attachment.getAttachment_id() == attachmentId) {
								it.remove();
							}
						}
						attachments.clear();
						attachments.addAll(attachmentsList);
					}
					session.saveOrUpdate(sample);
				}
			}
		
			// Remove file from server and delete row in DB.
			Attachments theAttachment = adh.findById(attachmentId, false);
			
			String path = theAttachment.getPath();
			
			File attachmentFile = new File(path);
			
			FileUtils.deleteQuietly(attachmentFile);
			
			session.delete(theAttachment);
		
			tx.commit();
		} catch (HibernateException he) {
			worked = false;
    		log.severe("Hibernate exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        } catch (Exception ex) {
			worked = false;
    		log.severe("Exception caught: " + ex);
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
		
		return worked;
	}

	/**
	 * Public method to save sample data to the database.
	 * 
	 * @param criteria <SampleCriteria> Sample data
	 * 
	 * @return <Long> Sample ID
	 */
	public Long saveSample(String id, SampleCriteria criteria) {
		log.info("in saveSample");
		Session session = null;
		long ret = -1L;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			Sample theSample = sdh.findById(new Long(criteria.getTrackingId()), false);
			
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			if (theSample == null) {
				theSample = new Sample();
				theSample.setCreateDate(new Date());
				//ssi.audit(id, "create");
				
				//if (id.equals(JUNIT_SESSION_ID))
				//	criteria.getGroups().add("SABC");

				ret = updateSampleFromCriteria(session, theSample, criteria);
				//if (criteria.getGroups() != null && criteria.getGroups().size()>0) {
				//	for (int i=0;i<criteria.getGroups().size();i++) 
				//		ssi.addObjectToGroup(ret, "sample", criteria.getGroups().get(i));
				//} else {
					//if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) 
					//	ssi.addObjectToGroup(ret, "sample", "SABC");
				//}
			} else {
				//ssi.audit(id, "modify");				
				ret = updateSampleFromCriteria(session, theSample, criteria);
			}

			//ssi.setModifiedTime(id);
			
			tx.commit();
		} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.severe(getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        	return Long.valueOf(-1L);
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
    		return Long.valueOf(-1L);
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
		
		return Long.valueOf(ret);
	}

	/**
	 * Public method to save sample data to the database.
	 * 
	 * @param criteria <SampleCriteria> Sample data
	 * 
	 * @return <Long> Sample ID
	 */
	public Long saveLabel(SampleCriteria criteria) {
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			Sample theSample = sdh.findById(new Long(criteria.getTrackingId()), false);
			
			if (theSample == null) {
				theSample = new Sample();
				theSample.setCreateDate(new Date());
			}

			long ret = updateLabelFromCriteria(session, theSample, criteria);
			
			tx.commit();
			
			return Long.valueOf(ret);
		} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (session != null && session.isConnected())
        			session.getTransaction().rollback();
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        	return Long.valueOf(-1L);
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
    		return Long.valueOf(-1L);
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
	}

	/**
	 * Public method to search for samples based on the <SampleCriteria>
	 *  and return a file path to the resulting Excel spreadsheet.
	 *  
	 *  @param criteria <SampleCriteria> Sample data
	 *  
	 *  @return <String> File path
	 */
	public String getDataFile(List<SampleCriteria> criterias, String id) {
		Collection<SampleCriteria> crits = searchSamples(criterias, id, 0, 0);
		
		ExportData ed = new ExportData();
		String filePath = ed.createExcelFile(crits, DEFAULT_WORKSHEET_NAME);
		return filePath;
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
	 * Private method to update a sample's data in the database
	 *  from the <SampleCriteria>.
	 *  
	 * @param session <Session>
	 * @param theSample <Sample> The sample in the database.
	 * @param criteria <SampleCriteria> The updated sample data.
	 * 
	 * @return long <Sample> ID
	 */
	private long updateSampleFromCriteria(Session session, Sample theSample,
			SampleCriteria criteria) throws HibernateException {
		
		Location foundLoc = null;
		List<Location> locs = null;
		
		theSample = setCriteria(theSample, criteria);
		
		theSample = setAttachments(session, theSample, criteria);
		
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		
		AmountUnits units = getUnits(session,criteria.getUnits());
		Treatments treatment = getTreatment(session,criteria.getTreatment());
		if (treatment != null)
			log.warning("treat="+treatment.getId());
		else
			log.warning("treat is null");
		Feedstocks feedstock = getFeedstock(session,criteria.getFeedstock());
		Fractions fraction = getFraction(session,criteria.getFraction());
		Status status = getStatus(session,criteria.getStatus());
		Origins origin = getOrigin(session, criteria.getOrigin());
		Location loc = getLocation(session,criteria);
		
		if (criteria.getBiomass_lots() != null && !criteria.getBiomass_lots().isEmpty()) {
			theSample.setBiomassLot(criteria.getBiomass_lots());
		} else theSample.setBiomassLot(null);
		
		if (criteria.getForm() != null && !criteria.getForm().isEmpty()) {
			Forms form = getForm(session, criteria.getForm());
			theSample.setForm(form);
		} else theSample.setForm(null);
		
		if (criteria.getStrain() != null && !criteria.getStrain().isEmpty()) {
			Strains strain = getStrain(session, criteria.getStrain());
			theSample.setStrain(strain);
		} else theSample.setStrain(null);
		
		if (criteria.getDestination() != null && !criteria.getDestination().isEmpty()) {
			Destinations destination = getDestination(session,criteria.getDestination());
			theSample.setDestination(destination);
		} else theSample.setDestination(null);
		
		if (criteria.getComposition() != null && !criteria.getComposition().isEmpty()) {
			Composition comp = getComposition(session,criteria.getComposition());
			theSample.setComposition(comp);
		} else theSample.setComposition(null);
		
		if (criteria.getCustodianName() != null && !criteria.getCustodianName().isEmpty()) {
			Custodians cust = getCustodian(session,criteria.getCustodianName());
			if (cust != null && cust.getId() == 0) {
				long cid = (Long) session.save(cust);
				//if (criteria.getGroups() != null && criteria.getGroups().size()>0) {
				//	for (int i=0;i<criteria.getGroups().size();i++) 
				//		ssi.addObjectToGroup(cid, "custodians", criteria.getGroups().get(i));
				//} else {
				//	if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) 
				//		ssi.addObjectToGroup(cid, "custodians", "SABC");
				//}
			} 
			theSample.setCustodianName(criteria.getCustodianName());
		} else theSample.setCustodianName(null);
			
		theSample.setOrigin(origin);
		theSample.setUnits(units);
		theSample.setTreatment(treatment);
		theSample.setFeedstock(feedstock);
		theSample.setFraction(fraction);
		theSample.setStatus(status);
		
		LocationDAOHibernate ldh = new LocationDAOHibernate();
		ldh.setSession(session);
		locs = ldh.findByExample(loc, "id");
		log.info("Matched " + locs.size() + " locations.");
		if (locs != null && !locs.isEmpty()) {
			foundLoc = locs.get(0);
			loc = foundLoc;
		} else {
			session.save(loc);
		}
		if (criteria.getHealth() != null && !criteria.getHealth().isEmpty()) {
			log.warning("setting health="+criteria.getHealth().substring(0,1));
			theSample.setHealth(criteria.getHealth().substring(0,1));
		} else {
			log.warning("health is null");
		}
		if (criteria.getFire() != null && !criteria.getFire().isEmpty()) {
			theSample.setFire(criteria.getFire().substring(0,1));
		}
		if (criteria.getSpecific() != null && !criteria.getSpecific().isEmpty()) {
			theSample.setSpecificHazard(criteria.getSpecific().substring(0,1));
		}
		if (criteria.getReactivity() != null && !criteria.getReactivity().isEmpty()) {
			theSample.setReactivity(criteria.getReactivity().substring(0,1));
		}

		theSample.setLocation(loc);
		
		theSample.setCreateDate(criteria.getStartCreateDate());

		session.saveOrUpdate(theSample);
		
		//setTRB(criteria);
		
		return theSample.getId();
	}

	private Sample setAttachments(Session session, Sample theSample, SampleCriteria criteria) {
		AttachmentsDAOHibernate adh = new AttachmentsDAOHibernate();
		adh.setSession(session);
		List<Long> attachIds = criteria.getAttachmendIds();
		Iterator<Long> ait = attachIds.iterator();
		while (ait.hasNext()) {
			long aid = ait.next().longValue();
			Attachments attachment = adh.findById(aid);
			if (attachment != null)
				theSample.getAttachments().add(attachment);
		}
		return theSample;
	}

	private Sample setCriteria(Sample theSample, SampleCriteria criteria) {
		if (criteria.getSampleId() != null) theSample.setSampleId(criteria.getSampleId().indexOf(SINGLE_QUOTE) == -1 ? criteria.getSampleId() : criteria.getSampleId().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setSampleId(null);
		if (criteria.getLabelDescription() != null) theSample.setLabel_description(criteria.getLabelDescription().indexOf(SINGLE_QUOTE) == -1 ? criteria.getLabelDescription() : criteria.getLabelDescription().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setLabel_description(null);
		if (criteria.getComments() != null) theSample.setComment(criteria.getComments().indexOf(SINGLE_QUOTE) == -1 ? criteria.getComments() : criteria.getComments().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setComment(null);
		if (criteria.getOwnerName() != null) theSample.setOwnerName(criteria.getOwnerName().indexOf(SINGLE_QUOTE) == -1 ? criteria.getOwnerName() : criteria.getOwnerName().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setOwnerName(null);
		if (criteria.getCustodianName() != null) theSample.setCustodianName(criteria.getCustodianName().indexOf(SINGLE_QUOTE) == -1 ? criteria.getCustodianName() : criteria.getCustodianName().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setCustodianName(null);
		if (criteria.getExternalId() != null) theSample.setExternalId(criteria.getExternalId().indexOf(SINGLE_QUOTE) == -1 ? criteria.getExternalId() : criteria.getExternalId().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setExternalId(null);
		if (criteria.getAmount() != null) theSample.setAmount(criteria.getAmount().indexOf(SINGLE_QUOTE) == -1 ? criteria.getAmount() : criteria.getAmount().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setAmount(null);
		if (criteria.getStorageNotes() != null) theSample.setStorageNotes(criteria.getStorageNotes().indexOf(SINGLE_QUOTE) == -1 ? criteria.getStorageNotes() : criteria.getStorageNotes().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else theSample.setStorageNotes(null);
		
		String fire="";
		fire = transformSafety(criteria.getFire());
		theSample.setFire(fire);
		
		String reactivity="";
		reactivity = transformSafety(criteria.getReactivity());
		theSample.setReactivity(reactivity);
		
		String specific="";
		specific = transformSpecific(criteria.getSpecific());
		theSample.setSpecificHazard(specific);
		
		String health="";
		health = transformSafety(criteria.getHealth());
		theSample.setHealth(health);
		
		if (criteria.getStartCreateDate() != null)
			theSample.setCreateDate(criteria.getStartCreateDate());
		
		//criteria.getStartCreateDate());//
		theSample.setModifyDate(new Date());
		theSample.setTrbNum(criteria.getTrbNum());
		theSample.setTrbPage(criteria.getTrbPage());
		
		return theSample;
	}

	private void setTRB(SampleCriteria criteria) {
		Session calcSheetSession = null;
		try {
			if (criteria.getTrbNum() != 0 && criteria.getTrbPage() != 0) {
				calcSheetSession = TrbSessionFactory.getSession();
				String is_not = calcSheetSession.isConnected() ? "IS" : "IS NOT";
				log.info("b4 session "+is_not+" connected" );
				Transaction tx2 = calcSheetSession.beginTransaction();
				is_not = calcSheetSession.isConnected() ? "IS" : "IS NOT";
				log.info("after session "+is_not+" connected" );
				TrbDAOHibernate tdh = new TrbDAOHibernate();
				tdh.setSession(calcSheetSession);
				Trb trb = tdh.findByNumAndPage(criteria.getTrbNum(), criteria.getTrbPage());
				if (trb == null)
					trb = new Trb();
				trb.setNum(criteria.getTrbNum());
				trb.setPage(criteria.getTrbPage());
				calcSheetSession.saveOrUpdate(trb);
				tx2.commit();
			}
		} catch (HibernateException he) {
    		log.severe("Hibernate exception: " + he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
        	try {
        		if (calcSheetSession != null && calcSheetSession.isOpen()) {
        			if (calcSheetSession.getTransaction().isActive())
        				calcSheetSession.getTransaction().rollback();
        			else
        				log.warning("transaction is not active");
        		} else {
        			log.warning("Could not rollback transaction");
        		}
        	} catch (HibernateException rbEx) {
            	log.severe("Couldn't roll back transaction! Error: " + rbEx);
        	}
        	throw new HibernateException("Failed to save TRB");
        } catch (Exception ex) {
    		log.severe("Exception caught: " + ex);
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
    		throw new HibernateException("Failed to save TRB");
        } finally {
        	if (calcSheetSession != null)
        		if (calcSheetSession.isOpen())
        			calcSheetSession.close();
        }
	}

	private Location getLocation(Session session, SampleCriteria criteria) {
		Location loc = new Location();
		if (criteria.getBuilding() != null) loc.setBuilding(criteria.getBuilding().indexOf(SINGLE_QUOTE) == -1 ? criteria.getBuilding() : criteria.getBuilding().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		if (criteria.getRoom() != null) loc.setRoom(criteria.getRoom().indexOf(SINGLE_QUOTE) == -1 ? criteria.getRoom() : criteria.getRoom().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		if (criteria.getHolder() != null) loc.setHolder(criteria.getHolder().indexOf(SINGLE_QUOTE) == -1 ? criteria.getHolder() : criteria.getHolder().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		if (criteria.getShelf() != null) loc.setShelf(criteria.getShelf().indexOf(SINGLE_QUOTE) == -1 ? criteria.getShelf() : criteria.getShelf().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		if (criteria.getPackaging() != null) loc.setPackaging(criteria.getPackaging().indexOf(SINGLE_QUOTE) == -1 ? criteria.getPackaging() : criteria.getPackaging().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		if (criteria.getSubLocation() != null) loc.setSubLocation(criteria.getSubLocation().indexOf(SINGLE_QUOTE) == -1 ? criteria.getSubLocation() : criteria.getSubLocation().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		
		// Check and see if this location data already exists.
		LocationDAOHibernate ldh = new LocationDAOHibernate();
		ldh.setSession(session);
		List<Location> locList = ldh.findByExample(loc, ID);
		if (locList != null) {
			if (locList.size() > 0) {
				loc = locList.get(0);
			}
		}
		if (locList == null || locList.size() == 0)
			session.saveOrUpdate(loc);
		return loc;
	}

	/**
	 * Private method to update a sample's data in the database
	 *  from the <SampleCriteria>.
	 *  
	 * @param session <Session>
	 * @param theSample <Sample> The sample in the database.
	 * @param criteria <SampleCriteria> The updated sample data.
	 * 
	 * @return long <Sample> ID
	 */
	private long updateLabelFromCriteria(Session session, Sample theSample,
			SampleCriteria criteria) throws HibernateException {
		
		theSample = setLabelCriteria(theSample, criteria);
		
		Treatments treatment = getTreatment(session, criteria.getTreatment());
		Feedstocks feedstock = getFeedstock(session, criteria.getFeedstock());
		Fractions fraction = getFraction(session, criteria.getFraction());

		theSample.setTreatment(treatment);
		theSample.setFeedstock(feedstock);
		theSample.setFraction(fraction);
		session.saveOrUpdate(theSample);
				
		return theSample.getId();

	}

	private Sample setLabelCriteria(Sample theSample, SampleCriteria criteria) {
		if (criteria.getSampleId() != null) 
			theSample.setSampleId(criteria.getSampleId().indexOf(SINGLE_QUOTE) == -1 ? criteria.getSampleId() : criteria.getSampleId().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else 
			theSample.setSampleId("");
		if (criteria.getLabelDescription() != null) 
			theSample.setLabel_description(criteria.getLabelDescription().indexOf(SINGLE_QUOTE) == -1 ? criteria.getLabelDescription() : criteria.getLabelDescription().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else
			theSample.setLabel_description("");
		if (criteria.getOwnerName() != null) 
			theSample.setOwnerName(criteria.getOwnerName().indexOf(SINGLE_QUOTE) == -1 ? criteria.getOwnerName() : criteria.getOwnerName().replaceAll(SINGLE_QUOTE, FIXED_QUOTE));
		else 
			theSample.setOwnerName("");
		
		String fire="";
		fire = transformSafety(criteria.getFire());
		theSample.setFire(fire);
		
		String reactivity="";
		reactivity = transformSafety(criteria.getReactivity());
		theSample.setReactivity(reactivity);
		
		String specific="";
		specific = transformSpecific(criteria.getSpecific());
		theSample.setSpecificHazard(specific);
		
		String health="";
		health = transformSafety(criteria.getHealth());
		theSample.setHealth(health);
			
		return theSample;
	}

	/**
	 * Public method that returns a <Collection<String>> of unit names.
	 * 
	 * @return <Collection<String>> List of unit names.
	 */
	public AmountUnits getUnits(Session session, String value) {
		AmountUnits units = null;
		AmountUnitsDAOHibernate audh = new AmountUnitsDAOHibernate();

		units = audh.findByName(value);
		if (units == null && value != null && !value.isEmpty()) {
			units = new AmountUnits(value);
		}
		return units;
	}

	/**
	 * Public method that returns a <Feedstocks> using the
	 *  feedstock name.
	 * 
	 * @param value <String> Feedstock name.
	 * @return <Feedstocks>
	 */
	private Feedstocks getFeedstock(Session session, String value) {
		Feedstocks feedstock = null;
		FeedstocksDAOHibernate fdh = new FeedstocksDAOHibernate();
		fdh.setSession(session);
		feedstock = fdh.findByName(value);
		if (feedstock == null && value != null && !value.isEmpty()) {
			feedstock = new Feedstocks(value);
		}
		return feedstock;
	}

	/**
	 * Public method that returns a <Treatments> using the
	 *  treatment name.
	 * 
	 * @param value <String> Treatment name.
	 * @return <Treatments>
	 */
	private Treatments getTreatment(Session session, String value) {
		Treatments treatment = null;
		TreatmentsDAOHibernate sdh = new TreatmentsDAOHibernate();
		sdh.setSession(session);
		//Transaction tx = session.beginTransaction();
		if (value != null) {
			treatment = sdh.findByName(value);
			if (treatment == null && value != null && !value.isEmpty()) {
				treatment = new Treatments(value);
			}
		} else {
			List<Treatments> treatments = sdh.findNull();
			if (treatments != null && !treatments.isEmpty()) {
				treatment = treatments.get(0);
			}
		}
		//tx.commit();
		//session.close();
		return treatment;
	}

	/**
	 * Public method that returns a <Fractions> using the
	 *  fraction name.
	 * 
	 * @param value <String> Fraction name.
	 * @return <Fractions>
	 */
	private Fractions getFraction(Session session, String value) {
		Fractions fraction = null;
		FractionsDAOHibernate fdh = new FractionsDAOHibernate();
		fdh.setSession(session);
		fraction = fdh.findByName(value);
		if (fraction == null && value != null && !value.isEmpty()) {
			fraction = new Fractions(value);
		}
		return fraction;
	}

	/**
	 * Public method that returns a <Forms> using the
	 *  form name.
	 * 
	 * @param value <String> form name.
	 * @return <Forms>
	 */
	private Forms getForm(Session session, String value) {
		Forms form = null;
		FormsDAOHibernate fdh = new FormsDAOHibernate();
		fdh.setSession(session);
		form = fdh.findByName(value);
		if (form == null && value != null && !value.isEmpty()) {
			form = new Forms(value);
		}
		return form;
	}

	/**
	 * Public method that returns a <Destination> using the
	 *  destination name.
	 * 
	 * @param value <String> destination name.
	 * @return <Destinations>
	 */
	private Destinations getDestination(Session session, String value) {
		Destinations destination = null;
		DestinationsDAOHibernate fdh = new DestinationsDAOHibernate();
		fdh.setSession(session);
		destination = fdh.findByName(value);
		if (destination == null && value != null && !value.isEmpty()) {
			destination = new Destinations(value);
		}
		return destination;
	}

	/**
	 * Public method that returns a <Custodians> using the
	 *  Custodian name.
	 * 
	 * @param value <String> Custodian name.
	 * @return <Custodian>
	 */
	private Custodians getCustodian(Session session, String value) {
		Custodians comp = null;
		CustodiansDAOHibernate fdh = new CustodiansDAOHibernate();
		fdh.setSession(session);
		comp = fdh.findByName(value);
		if (comp == null && value != null && !value.isEmpty()) {
			comp = new Custodians();
			comp.setName(value);
		}
		return comp;
	}

	/**
	 * Public method that returns a <Composition> using the
	 *  Composition name.
	 * 
	 * @param value <String> Composition name.
	 * @return <Composition>
	 */
	private Composition getComposition(Session session, String value) {
		Composition comp = null;
		CompositionsDAOHibernate fdh = new CompositionsDAOHibernate();
		fdh.setSession(session);
		comp = fdh.findByName(value);
		if (comp == null && value != null && !value.isEmpty()) {
			comp = new Composition();
			comp.setName(value);
		}
		return comp;
	}

	/**
	 * Public method that returns a <Strains> using the
	 *  fraction name.
	 * 
	 * @param value <String> Strains name.
	 * @return <Strains>
	 */
	private Strains getStrain(Session session, String value) {
		Strains strain = null;
		StrainsDAOHibernate fdh = new StrainsDAOHibernate();
		fdh.setSession(session);
		strain = fdh.findByName(value);
		if (strain == null && value != null && !value.isEmpty()) {
			strain = new Strains(value);
		}
		return strain;
	}

	/**
	 * Public method that returns a <Status> using the
	 *  status name.
	 * 
	 * @param value <String> Status name.
	 * @return <Status>
	 */
	private Status getStatus(Session session, String value) {
		Status status = null;
		StatusDAOHibernate audh = new StatusDAOHibernate();
		audh.setSession(session);
		status = audh.findByName(value);
		if (status == null && value != null && !value.isEmpty()) {
			status = new Status(value);
		}
		if (value != null) {
			status = audh.findByName(value);
			if (status == null && value != null && !value.isEmpty()) {
				status = new Status(value);
			}
		} else {
			List<Status> statuses = audh.findNull();
			if (statuses != null && !statuses.isEmpty()) {
				status = statuses.get(0);
			}
		}
		return status;
	}

	/**
	 * Public method that returns a list of custodian names.
	 * 
	 * @return <Collection<String>> list of custodian names
	 */
	public Collection<String> getCustodianNames() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			stringList = sdh.findAllCustodians();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	@Override
	public Collection<String> getDestinations() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
		
			if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
				DestinationsDAOHibernate sdh = new DestinationsDAOHibernate();
				sdh.setSession(session);
				stringList = sdh.findAllNames();
			} else {
				SampleDAOHibernate sdh = new SampleDAOHibernate();
				sdh.setSession(session);
				stringList = sdh.findAllProjects();
			}
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	@Override
	public Collection<String> getForms() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			FormsDAOHibernate sdh = new FormsDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			stringList = sdh.findAllNames();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	@Override
	public Collection<String> getCompositions() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			CompositionsDAOHibernate sdh = new CompositionsDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			stringList = sdh.findAllNames();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	@Override
	public Collection<String> getBiomasses() {
		log.info("in getBiomasses");
		Session session = null;
		Collection<String> stringList = new ArrayList<String>();
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			List<Sample> samples = sdh.findAll();
			Iterator<Sample> sit = samples.iterator();
			while (sit.hasNext()) {
				Sample s = sit.next();
				if (s.getBiomassLot() != null && !s.getBiomassLot().isEmpty()) {
					String bml = s.getBiomassLot();
					if (!stringList.contains(bml)) 
						stringList.add(bml);
				}
			}
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		if (stringList != null && !stringList.isEmpty())
			log.info("returning "+stringList.size()+" biomasses");
		else
			log.info("returning no biomasses");
		return stringList;
	}

	@Override
	public Collection<String> getBiomassLots() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			stringList = sdh.findAllBiomassLots();
				
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	@Override
	public Collection<String> getCustodians(String id) {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			//List<Long> sids = ssi.getObjectIdsForUser(id, "custodians");		
			//stringList = sdh.findAllNames(sids);
			stringList = sdh.findAllCustodians();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Public method that returns a list of owner names.
	 * 
	 * @return <Collection<String>> list of owner names
	 */
	public Collection<String> getOwnerNames() {
		Session session = null;
		Collection<String> stringList = null;
		try {
			session = HibernateSessionFactory.getSession();
		
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
		
			stringList = sdh.findAllOwners();
		
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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }

    	if ((stringList != null) && (stringList.isEmpty()))
			stringList = null;
		
		return stringList;
	}

	/**
	 * Private method to get the file name of a TRB page.
	 * 
	 * @param num - int TRB number
	 * @param page - int TRB page number
	 * @return <String> file name
	 */
    private String getFilename(int num, int page) {
		Session session = null;
		String filename = null;
		try {
			session = TrbSessionFactory.getSession();

			TrbDAOHibernate tdh = new TrbDAOHibernate();
			tdh.setSession(session);

			Transaction tx = session.beginTransaction();

			Trb trb = tdh.findByNumAndPage(num, page);
			
			if (trb != null)
				if (trb.getTrbFile() != null)
					filename = trb.getTrbFile().getPath();

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
    		log.severe(TrackerServiceImpl.getStackTrace(ex));
        } finally {
        	if (session != null && session.isConnected())
        		if (session.isOpen())
        			session.close();
        }
		return filename;
    }

    /**
     * Public convenience method to determine if a file has been 
     *  uploaded for a given TRB page.
     *  
     *  @param snum - <String> TRB number
     *  @param spage - <String> TRB page number
     *  @return <Boolean> whether TRB page file has been uploaded.
     */
	public Boolean trbHasFile(String snum, String spage) {
		int num=0;
		int page=0;
		try {
			num = Integer.parseInt(snum);
			page = Integer.parseInt(spage);
		} catch (NumberFormatException nfe) {
			log.warning("Problems parsing "+snum+" or "+spage);
		}
		String path = getFilename(num,page); 
		if (path==null) return new Boolean(false);
		File file = new File(path);
		return new Boolean(file.exists());
	}

    private int getSplitIndex(String text) {
    	int div=0;
    	String punct = ",.!?; \" ') ";
    	if (text.length()<MAX_PER_LINE) return -1;
    	int start=START_POSITION;
    	int which = 0;
    	while (div < 1 && which+1 < punct.length()) {
    		String ch = punct.substring(which,++which);
    		div = text.indexOf(ch, start);
    		if (div > 0) {
    			//log.info("found punct=<"+ch+"> at "+div);
    			if (div > MAX_PER_LINE) 
    				div = -1;
    		}
    	}
    	if (div < 1)
    		div = MAX_PER_LINE;
    	return div;
    }

    public String createLabelImage(Sample sample) {
		String filename="/tmp/"+sample.getId()+".pdf";//"c:/tmp/MVS-001.pdf";//sample.getSampleId();
		try {
	        // step 1
	        Document document = new Document();
	        document.setPageSize(new Rectangle(300,200));
	        // step 2
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
	        // step 3
	        document.open();
	        // Adding a barcode
	        Barcode39 code39 = new Barcode39();
	        code39.setCodeType(Barcode.CODE128);//CODE128_RAW);
	        code39.setCode(String.valueOf(sample.getId()));//"1001");//sample.getId();
	        Image img = code39.createImageWithBarcode(writer.getDirectContent(), null, null);
			//img.setIndentationRight(100);
			img.setAlignment(Image.RIGHT | Image.TEXTWRAP);
	        document.add(img);	 
	        // step 4
	        // Adding label text
            BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
	        Font f = new Font(bf_times);
	        f.setSize(6);
            BaseFont bf_times1 = BaseFont.createFont(BaseFont.TIMES_BOLD, "Cp1252", false);
	        Font f1 = new Font(bf_times1);
	        f1.setSize(6);
		    String descriptor1 = "";
		    String descriptor2 = "";
		    String descriptor3 = "";
		    if (sample != null) {
			    String actual = sample.getLabel_description();
			    if (actual != null && !actual.isEmpty()) {
			    	int index = getSplitIndex(actual);
			    	if (index < 1 || actual.length() < index+1) {
			    		descriptor1 = actual;
			    	} else {
			    		descriptor1 = actual.substring(0,index+1);
				    	descriptor2 = actual.substring(index+1);
				    	if (descriptor2.length()>MAX_PER_LINE) {
				    		int index1 = getSplitIndex(descriptor2);
					    	if (index1 > 1) {
						    	descriptor3 = descriptor2.substring(index1+1);
					    		descriptor2 = descriptor2.substring(0,index1+1);
					    	}
				    	}
				    	if (descriptor2 != null && !descriptor2.isEmpty() && descriptor2.substring(0,1).equals(" "))
				    		descriptor2 = descriptor2.substring(1);
				    	if (descriptor3 != null && !descriptor3.isEmpty() && descriptor3.substring(0,1).equals(" "))
				    		descriptor3 = descriptor3.substring(1);
			    	}
			    }
		    }
	        document.add(new Phrase(descriptor1+NEWLINE, f));//descriptor1
	        document.add(new Phrase(descriptor2+NEWLINE, f));//descriptor2
	        document.add(new Phrase(descriptor3+NEWLINE, f));//descriptor3
	        document.add(new Phrase("Material:",f1));document.add(new Phrase("Biomass, "+sample.getForm().getName()+","+sample.getFraction().getName()+","+sample.getComposition().getName()+NEWLINE,f));//sample.getMaterial(), sample.getForm()));
	        document.add(new Phrase("Strain:",f1));document.add(new Phrase(sample.getStrain().getName()+NEWLINE,f));//sample.getStrain()));
	        document.add(new Phrase("Sample ID:",f1));document.add(new Phrase(sample.getSampleId()+NEWLINE,f));//sample.getSampleId()));
	        document.add(new Phrase("Destination:",f1));document.add(new Phrase(sample.getDestination().getName()+NEWLINE,f));//sample.getDestination()));
	        document.add(new Phrase("Custodian:",f1));document.add(new Phrase(sample.getCustodianName()+NEWLINE,f));//sample.getCustodian()));
			Date entryDate = sample.getCreateDate();
			SimpleDateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
			String sdate = dFormat.format(entryDate);
			document.add(new Phrase("Entry Date:",f1));document.add(new Phrase(sdate+NEWLINE,f));	        //document.add(new Phrase("Entry Date:"+sample.getEntryDate()+NEWLINE,f));//sample.getEntryDate()));
	        // step 5
			document.close();			
		} catch (Exception e) {
			
		}
		return filename;
	}

	/**
	 * Public method to email a label.
	 * 
	 * @param label - <LabelDTO> label information
	 * @param printerName - <String> printer name
	 * @return <Boolean> - whether label printed or not.
	 */
	@Override
	public Boolean emailLabel(String sessionId, LabelDTO label) {
		Session session = null;
		
		SimpleDateFormat dFormat = null;
		Sample sample = null;
		try {
			session = HibernateSessionFactory.getSession();

			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			long id = Long.parseLong(label.getTrackingId());

			sample = sdh.findById(new Long(id), false);
			
			if (sample == null) {
				log.severe("error retrieving sample for id="+id);
				log.severe("error retrieving sample for tracking Id="+label.getTrackingId());
				return new Boolean(false);
			}
			
			dFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (sample.getCreateDate() != null) label.setEntryDate(dFormat.format(sample.getCreateDate()));
			else label.setEntryDate("");
			//label.setEntryDate(dFormat.format(sample.getCreateDate()));
			if (sample.getOwnerName() != null) label.setOwnerName(sample.getOwnerName());
			else label.setOwnerName("");
			//label.setOwnerName(sample.getOwnerName());
			if (sample.getSampleId() != null) label.setSampleId(sample.getSampleId());
			else label.setSampleId("");
			//label.setSampleId(sample.getSampleId());
			label.setTrbNum(String.valueOf(sample.getTrbNum()));
			label.setTrbPage(String.valueOf(sample.getTrbPage()));
			if (sample.getLabel_description() != null) label.setDescription(sample.getLabel_description());
			else label.setDescription("");
			//label.setDescription(sample.getDescription());
			if (sample.getFeedstock() != null) label.setFeedstock(sample.getFeedstock().getName());
			else label.setFeedstock("");
			if (sample.getTreatment() != null) label.setTreatment(sample.getTreatment().getName());
			else label.setTreatment("");
			if (sample.getFraction() != null) label.setFraction(sample.getFraction().getName());
			else label.setFraction("");
			if (sample != null) label.setFire(String.valueOf(sample.getFire()));
			else label.setFire("0");
			if (sample != null) label.setReactivity(String.valueOf(sample.getReactivity()));
			else label.setReactivity("0");
			if (sample != null) label.setSpecific(String.valueOf(sample.getSpecificHazard()));
			else label.setSpecific("");
			if (sample != null) label.setHealth(String.valueOf(sample.getHealth()));
			else label.setHealth("0");
			if (sample != null) label.setCustodian(sample.getCustodianName());
			if (sample != null) if (sample.getDestination() != null) label.setDestination(sample.getDestination().getName());
			if (sample != null) if (sample.getForm() != null) label.setForm(sample.getForm().getName());
			if (sample != null) if (sample.getComposition() != null) label.setComposition(sample.getComposition().getName());
			if (sample != null) if (sample.getStrain() != null) label.setStrain(sample.getStrain().getName());
			
			tx.commit();
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting label info. error: "
					+ he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		String filename = createLabelImage(sample);
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		String email = ssi.getUserEmail(getSessionId());
		boolean ret = ssi.sendmail("The label to be printed is attached", filename, email);
		log.info("got "+ret+" from send for tracking id="+label.getTrackingId());
		if (ret) {
			ssi.audit(sessionId, "print");				
		}
		return ret;
	}

	/**
	 * Public method to print a label.
	 * 
	 * @param label - <LabelDTO> label information
	 * @param printerName - <String> printer name
	 * @return <Boolean> - whether label printed or not.
	 */
	@Override
	public Boolean printLabel(String sessionId, LabelDTO label, String printerName) {
		System.out.println("in printLabel with printer="+printerName);
		Session session = null;
		Date entryDate = null;
		
		SimpleDateFormat dFormat = null;
		try {
			session = HibernateSessionFactory.getSession();

			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);

			Transaction tx = session.beginTransaction();
			
			long id = Long.parseLong(label.getTrackingId());

			Sample sample = sdh.findById(new Long(id), false);
			
			if (sample == null) {
				log.severe("error retrieving sample for id="+id);
				log.severe("error retrieving sample for tracking Id="+label.getTrackingId());
				return new Boolean(false);
			}
			
			dFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (sample.getCreateDate() != null) label.setEntryDate(dFormat.format(sample.getCreateDate()));
			else label.setEntryDate("");
			//label.setEntryDate(dFormat.format(sample.getCreateDate()));
			if (sample.getOwnerName() != null) label.setOwnerName(sample.getOwnerName());
			else label.setOwnerName("");
			//label.setOwnerName(sample.getOwnerName());
			if (sample.getSampleId() != null) label.setSampleId(sample.getSampleId());
			else label.setSampleId("");
			//label.setSampleId(sample.getSampleId());
			label.setTrbNum(String.valueOf(sample.getTrbNum()));
			label.setTrbPage(String.valueOf(sample.getTrbPage()));
			if (sample.getLabel_description() != null) label.setDescription(sample.getLabel_description());
			else label.setDescription("");
			//label.setDescription(sample.getDescription());
			if (sample.getFeedstock() != null) label.setFeedstock(sample.getFeedstock().getName());
			else label.setFeedstock("");
			if (sample.getTreatment() != null) label.setTreatment(sample.getTreatment().getName());
			else label.setTreatment("");
			if (sample.getFraction() != null) label.setFraction(sample.getFraction().getName());
			else label.setFraction("");
			if (sample != null) label.setFire(String.valueOf(sample.getFire()));
			else label.setFire("0");
			if (sample != null) label.setReactivity(String.valueOf(sample.getReactivity()));
			else label.setReactivity("0");
			if (sample != null) label.setSpecific(String.valueOf(sample.getSpecificHazard()));
			else label.setSpecific("");
			if (sample != null) label.setHealth(String.valueOf(sample.getHealth()));
			else label.setHealth("0");
			if (sample != null) label.setCustodian(sample.getCustodianName());
			if (sample != null) if (sample.getDestination() != null) label.setDestination(sample.getDestination().getName());
			if (sample != null) if (sample.getForm() != null) label.setForm(sample.getForm().getName());
			if (sample != null) if (sample.getComposition() != null) label.setComposition(sample.getComposition().getName());
			if (sample != null) if (sample.getStrain() != null) label.setStrain(sample.getStrain().getName());
			
			String sql = "select r.work_done_id "+ 
							 "from relation r, sample s, sample_relation sr "+ 
							 "where r.relation_id = sr.relation_id and "+
							 "sr.id = s.id and s.id = "+id;
	    	TrackerFileUploadServiceImpl ssi = new TrackerFileUploadServiceImpl();
	        String dbPath = "";
			if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.BIOMASS)) 
	        	dbPath = ssi.db_biomass;
			else
				dbPath = ssi.db_algae;
	        log.warning("db="+dbPath);
	        String dbUser = ssi.db_user;
	        log.warning("user="+dbUser);
	        DataEncryption de = new DataEncryption(CRYPTOKEY);
	        String dbPass=de.decrypt(ssi.db_pass);
	        DBUtils db = new DBUtils(dbPath,dbUser,dbPass);
	        db.performQuery(sql, true);
	        if (db.getNextRow()) {
	        	long wid = db.getLongColumn(1);
	        	log.warning("wid="+wid);
	        	label.setWorkId(String.valueOf(wid));
	        } else {
	        	log.warning("no work id found");
	        	label.setWorkId("");
	        }
	        db.close();
			if (sample != null && sample.isPrinted()) {
				label.setPrinted(sample.isPrinted());
				//ssi.audit(sessionId, "print");				
			}
			else {
				label.setPrinted(false);
			}
			
			PrintLabelService pls = new PrintLabelService();
			boolean ret = pls.printLabel(label, printerName, DevTestProdConstants.DISPLAY_MODE); // TODO Add CRADA printing option
			log.warning("got "+ret+" from printLabel for tracking id="+label.getTrackingId()+" to printer="+printerName);
			if (ret) {
				session = HibernateSessionFactory.getSession();
				sdh.setSession(session);
				tx = session.beginTransaction();
				Sample theSample = sdh.findById(new Long(label.getTrackingId()), false);
					
				if (theSample != null) {
					theSample.setPrinted(true);
					session.saveOrUpdate(theSample);
				}
				session = HibernateSessionFactory.getSession();
				AuditDAOHibernate adh = new AuditDAOHibernate();
				adh.setSession(session);
				//long id = Long.parseLong(label.getTrackingId());
				Audit audit = new Audit();
				//audit.setTracking_id(id);
				audit.setSample(theSample);
				Date date = Calendar.getInstance().getTime();
				audit.setPrintDate(date);
				session.save(audit);
			}
			
			tx.commit();
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting label info. error: "
					+ he);
    		log.severe(TrackerServiceImpl.getStackTrace(he));
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		if ((label.getEntryDate() == null)) {
			try {
				session = HibernateSessionFactory.getSession();
	
				SampleDAOHibernate sdh = new SampleDAOHibernate();
				sdh.setSession(session);
	
				Transaction tx = session.beginTransaction();
				
				long id = Long.parseLong(label.getTrackingId());
	
				Sample sample = sdh.findById(new Long(id), false);
				
				label.setPrinted(sample.isPrinted());
				log.info("sample "+id+" .isPrinted="+sample.isPrinted());
				
				if (sample != null) {
					entryDate = sample.getCreateDate();
					
					dFormat = new SimpleDateFormat("MM/dd/yyyy");
					
					label.setEntryDate(dFormat.format(entryDate));
				} else {
					dFormat = new SimpleDateFormat("yyyy-MM-dd");
					try {
						entryDate = dFormat.parse(label.getEntryDate());
						
					} catch (ParseException pe) {
						log.severe("Entry date is invalid. Entry date will be null.");
					}
				}
	
				tx.commit();
			} catch (HibernateException he) {
				log.severe("Hibernate exception on getting technician names. error: "
								+ he);
	    		log.severe(TrackerServiceImpl.getStackTrace(he));
				try {
					if (session != null && session.isConnected())
						session.getTransaction().rollback();
				} catch (HibernateException rbEx) {
					log.severe("Couldn't roll back transaction! Error: " + rbEx);
				}
			} catch (NumberFormatException nfe) {
				log.severe("Tracking ID is invalid. Entry date will be null.");
			} finally {
				if (session != null && session.isConnected())
					if (session.isOpen())
						session.close();
			}
		} else {			
			if (label.getEntryDate() != null && !label.getEntryDate().isEmpty()) {
				dFormat = new SimpleDateFormat("MM/dd/yyyy");
				try {
					session = HibernateSessionFactory.getSession();
					
					SampleDAOHibernate sdh = new SampleDAOHibernate();
					sdh.setSession(session);
		
					Transaction tx = session.beginTransaction();
					
					long id = Long.parseLong(label.getTrackingId());
		
					Sample sample = sdh.findById(new Long(id), false);
					
					label.setPrinted(sample.isPrinted());
					log.info("sample "+id+" .isPrinted="+sample.isPrinted());
					entryDate = dFormat.parse(label.getEntryDate());
					
					tx.commit();
				} catch (HibernateException he) {
					log.severe("Hibernate exception on getting technician names. error: "
									+ he);
		    		log.severe(TrackerServiceImpl.getStackTrace(he));
					try {
						if (session != null && session.isConnected())
							session.getTransaction().rollback();
					} catch (HibernateException rbEx) {
						log.severe("Couldn't roll back transaction! Error: " + rbEx);
					}
				} catch (ParseException pe) {
					//log.warning("Entry date is invalid. Trying other format.");
					dFormat = new SimpleDateFormat("yyyy-MM-dd");
					try {
						entryDate = dFormat.parse(label.getEntryDate());
						
					} catch (ParseException pe1) {
						log.severe("Entry date is invalid. Entry date will be null.");
					}
				} catch (NumberFormatException nfe) {
					log.severe("Tracking ID is invalid. Entry date will be null.");
				} finally {
					if (session != null && session.isConnected())
						if (session.isOpen())
							session.close();
				}
			} else entryDate = null;
		}
		
		if (entryDate != null)
			label.setEntryDate(dFormat.format(entryDate));

		boolean ret = true;
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE) && 
				!DevTestProdConstants.PRINT_MODE.equals(DevTestProdConstants.ALGAE)) {
			ret = true;
		} else { //SABC Database Printing starts here
			//PrintLabelService pls = new PrintLabelService();
			//String group = getGroupForUser(getSessionId());
			//ret = pls.printLabel(label, printerName, group);
			//log.info("got "+ret+" from printLabel for tracking id="+label.getTrackingId()+" to printer="+printerName);
			ret = true;
		}
		if (ret) {
			try {
				session = HibernateSessionFactory.getSession();
			
				SampleDAOHibernate sdh = new SampleDAOHibernate();
				sdh.setSession(session);
				Transaction tx = session.beginTransaction();

				Sample theSample = sdh.findById(new Long(label.getTrackingId()), false);
				
				if (theSample != null) {
					theSample.setPrinted(true);
					session.saveOrUpdate(theSample);
				}
				session = HibernateSessionFactory.getSession();

				AuditDAOHibernate adh = new AuditDAOHibernate();
				adh.setSession(session);
				tx = session.beginTransaction();

				//long id = Long.parseLong(label.getTrackingId());

				Audit audit = new Audit();

				//audit.setTracking_id(id);
				audit.setSample(theSample);
				Date date = Calendar.getInstance().getTime();
				audit.setPrintDate(date);
				session.save(audit);
				SecurityServiceImpl ssi = new SecurityServiceImpl();
				//ssi.audit(sessionId, "print");

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
	    		log.severe(TrackerServiceImpl.getStackTrace(ex));
	        } finally {
	        	if (session != null && session.isConnected())
	        		if (session.isOpen())
	        			session.close();
	        }			
		}
		return new Boolean(ret);
	}

	private List<LabelDTO> getLabels(Collection<SampleCriteria> samples) {
		List<LabelDTO> labels = new ArrayList<LabelDTO>();
		Iterator<SampleCriteria> sit = samples.iterator();
		while (sit.hasNext()) {
			SampleCriteria sample = sit.next();
			LabelDTO label = new LabelDTO();
			label.setSampleId(sample.getSampleId());
			label.setOwnerName(sample.getOwnerName());
			label.setTrbNum(String.valueOf(sample.getTrbNum()));
			label.setEntryDate(sample.getStartCreateDate().toString());/* TODO - format */
			label.setTrackingId(String.valueOf(sample.getTrackingId()));
			label.setTrbPage(String.valueOf(sample.getTrbPage()));
			labels.add(label);
		}
		return labels;
	}
	/**
	 * Public method to print a label.
	 * 
	 * @param label - <LabelDTO> label information
	 * @param printerName - <String> printer name
	 * @return <Boolean> - whether label printed or not.
	 */
	@Override
	public Boolean printAllLabels(List<SampleCriteria> criterias, String sessionId, String printerName) {
		log.info("in printAllLabel with printer="+printerName);
		Collection<SampleCriteria> crits = searchSamples(criterias, sessionId, 0, 0);
		List<LabelDTO> labels = getLabels(crits);
		Iterator<LabelDTO> lit = labels.iterator();
		while (lit.hasNext()) {
			LabelDTO label = lit.next();
			this.printLabel(sessionId, label, printerName);
		}
		return true;
	}

	public static String getDeployDir(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {                  
                public boolean accept(File file) {
                	if (file.getName().startsWith("temp"))
                        return file.isDirectory();
                	return false;
                }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
                if (file.lastModified() > lastMod) {
                        choice = file;
                        lastMod = file.lastModified();
                }
        }
        return choice.getPath();
	}
	 
	public static String getContentDir(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {                  
                public boolean accept(File file) {
                	if (file.getName().startsWith("content"))
                        return file.isDirectory();
                	return false;
                }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
                if (file.lastModified() > lastMod) {
                        choice = file;
                        lastMod = file.lastModified();
                }
        }
        return choice.getPath();
	}
	 
	/**
	 * Method to get attachment types
	 * 
	 * @return <List<String>> A list of attachment extensions
	 */
	public List<String> getAttachmentExtensions() {
		Session session = null;
		List<String> extensions = null;
		try {
			session = HibernateSessionFactory.getSession();

			AttachmentTypeDAOHibernate ath = new AttachmentTypeDAOHibernate();
			ath.setSession(session);
			
			Transaction tx = session.beginTransaction();

			extensions = ath.findExtensions();

			tx.commit();

		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting headers. error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		return extensions;
	}

	/**
	 * Public method to get a delete sample.
	 * 
	 * @param tracking_id - id of the sample to delete
	 */
	public Boolean deleteSample(long tracking_id) {
		Boolean ret = true;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			
			SampleDAOHibernate sdh = new SampleDAOHibernate();
			sdh.setSession(session);
			
			Sample sample = sdh.findById(tracking_id, true);
			if (sample != null) {
				session.delete(sample);
			} else
				ret = false;
			tx.commit();

		} catch (HibernateException he) {
			ret = false;
			log.severe("Hibernate exception on deleting sample. Error: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} finally {
			if (session != null && session.isConnected())
				if (session.isOpen())
					session.close();
		}
		
		return ret;
	}

	/**
	 * Public method to get list of available label printers
	 * 
	 * @return <List<String>> list of printer names.
	 */
	@Override
	public List<String> getPrinterList() {		
		PrintLabelService pls = new PrintLabelService();
		return pls.listPrinters();
	}

	@Override
	public String getSessionId() {
		String id = "";
		//log.info("in getSessionId - 0 args");
		//SecurityServiceImpl ssi = new SecurityServiceImpl();
		//id = ssi.getSessionId();
		id = JUNIT_SESSION_ID;
		//log.info("ssi returned id="+id);
		return id;
	}
	
	@Override
	public String getGroupForUser(String id) {
		if (DevTestProdConstants.SECURITY_MODE) {
			//log.info("in TrackerServiceImpl.getGroupForUser with id="+id);
			//SecurityServiceImpl ssi = new SecurityServiceImpl();
			//log.info("in TrackerServiceImpl.getGroupForUser with id="+id);
			if (id == null)
				id = JUNIT_SESSION_ID;
			//return ssi.getGroupForUser(id);
		}
		return DevTestProdConstants.DISPLAY_MODE;
	}
	
	@Override
	public Boolean hasPermission(String id, String task) {
		//log.info("in TrackerServiceImpl.hasPermission with id="+id);
		if (DevTestProdConstants.SECURITY_MODE) {
			SecurityServiceImpl ssi = new SecurityServiceImpl();
			//log.info("in TrackerServiceImpl.hasPermission with id="+id);
			if (id == null)
				id = JUNIT_SESSION_ID;
			//return ssi.hasPermission(id, task);
		} 
		return true;
	}
	
	@Override
    public Integer getUserSessionTimeout() {
		getThreadLocalRequest().getSession().setMaxInactiveInterval(INACTIVE_INTERVAL);
        int timeout = getThreadLocalRequest().getSession().getMaxInactiveInterval() * 1000;
        //log.info("timeout="+timeout/1000);
        return timeout;
    }
	
	@Override
	public void logoff(String id) {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		ssi.logoff(id);
	}
	
	@Override
	public String logon(String name, String pass) {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		return ssi.logon(name,pass);
	}
	
	@Override
	public Boolean addUser(String userId, String pass) {
		boolean ret = false;
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		Users user = new Users();
		user.setUserId(userId);
		user.setPassword(pass);
		ret = ssi.addUser(user, true);
		return ret;
	}

	public String createLabelMessage(LabelDTO label, String printer) {
		String body = "";
        /*
        description1,2,3
        biomass,form,fraction,composition
        sampleid
		strain
		destination
		custodian
	    entry date	                        
        */
		String form = "";
    	if (label.getForm() != null && !label.getForm().isEmpty() && !label.getForm().equals("null")) 
    		form = label.getForm();
    	else
    		form = " ";
		String fraction = "";
    	if (label.getFraction() != null && !label.getFraction().isEmpty() && !label.getFraction().equals("null")) 
    	{
    		if (form.equals(" ")) fraction = label.getFraction();
    		else fraction = "," + label.getFraction();
    	}
    	else
    		fraction = " ";
    	String composition = "";
    	if (label.getComposition() != null && !label.getComposition().isEmpty() && !label.getComposition().equals("null")) 
    	{
    		if (form.equals(" ") && fraction.equals(" ")) composition = label.getComposition();
    		else composition = ","+label.getComposition();
    	}
    	else
    		composition = " ";
    	String strain = "";
    	if (label.getStrain() != null && !label.getStrain().isEmpty() && !label.getStrain().equals("null")) 
    		strain = label.getStrain();
    	else
    		strain = " ";
    	String custodian = "";
    	if (label.getCustodian() != null && !label.getCustodian().isEmpty() && !label.getCustodian().equals("null")) 
    		custodian = label.getCustodian();
    	else
    		custodian = " ";
    	String destination = "";
    	if (label.getDestination() != null && !label.getDestination().isEmpty() && !label.getDestination().equals("null")) 
    		destination = label.getDestination();
    	else
    		destination = " ";		
        body += DESCRIPTION + TAB + label.getDescription() + NEWLINE;
        body += FORM + TAB + form + fraction + composition + NEWLINE;
        body += SAMPLEID + TAB + label.getSampleId() + NEWLINE;
        body += STRAIN + TAB + strain + NEWLINE;
        body += DESTINATION + TAB + destination + NEWLINE;
        body += CUSTODIAN + TAB + custodian + NEWLINE;
        body += ENTRY_DATE + TAB + label.getEntryDate() + NEWLINE;
        body += TRACKINGID + TAB + label.getTrackingId() + NEWLINE;
        body += PRINTER + TAB + printer + NEWLINE;
		return body;
	}
	
	public boolean sendEmailToPrint(Sample sample, String printer) {
		log.info("in sendEmailToPrint");
		boolean ret = true;
	    try {
			LabelDTO label = new LabelDTO();
			label.setCustodian(sample.getCustodianName());
			label.setDescription(sample.getLabel_description());
			Destinations destination = sample.getDestination();
			String dest = "";
			if (destination != null) dest = destination.getName();
			label.setDestination(dest);
			label.setEntryDate(sample.getCreateDate().toString());
			Forms form = sample.getForm();
			String frm = "";
			if (form != null) frm = form.getName();
			label.setForm(frm);
			Fractions fraction = sample.getFraction();
			String frction = "";
			if (fraction != null) frction = fraction.getName();
			label.setFraction(frction);
			Composition comp = sample.getComposition();
			String cmp = "";
			if (comp != null) cmp = comp.getName();
			label.setComposition(cmp);
			Strains strain = sample.getStrain();
			String stn = "";
			if (strain != null) stn = strain.getName();
			label.setStrain(stn);
			label.setSampleId(sample.getSampleId());
			label.setTrackingId(String.valueOf(sample.getId()));
	        String body = createLabelMessage(label, printer);
	        MimeBodyPart mbp1 = new MimeBodyPart();
		    
	    	SecurityServiceImpl susi = new SecurityServiceImpl();
	        String host = susi.emailHostValue;//"mailgate1.nrel.gov";
	        log.info("host="+host);
			
		    // Create properties, get Session
		    Properties props = new Properties();
		
		    props.put("mail.smtp.host", host);
		    props.put("mail.debug", "true");
			
		    javax.mail.Session session = javax.mail.Session.getInstance(props);
	
	        Message msg = new MimeMessage(session);
		    String to1 = susi.adminUser1Value;//EMAIL_USER;
		    log.info("destination="+to1);
	        InternetAddress[] address = {new InternetAddress(to1)};
	        msg.setRecipients(Message.RecipientType.TO, address);
	        msg.setSentDate(new Date());
	        msg.setSubject(SABC_LABEL_PRINTING);
	        msg.setFrom(new InternetAddress(susi.adminUser1Value));//EMAIL_USER));
	        mbp1.setText(body);
	        Multipart mp = new MimeMultipart();
	        mp.addBodyPart(mbp1);
	        msg.setContent(mp);
	        //Send the message
	        Transport t = session.getTransport("smtp");
	        try {
	        	DataEncryption de = new DataEncryption(CRYPTOKEY);
	        	String unc = de.decrypt(susi.emailPassValue);
	        	log.info("user="+susi.emailUserValue);
	            t.connect(susi.emailUserValue, unc);
	            t.sendMessage(msg, msg.getAllRecipients());
	            ret = true;
	        } catch (Exception e1) {
	        	ret = false;
	        	log.info("caught: "+e1.getMessage());
	        	//System.out.println("caught: "+e1.getMessage());
	        } finally {
	            t.close();
	        }
	    } catch (MessagingException mex) {
	    	ret = false;
			log.warning(mex.getMessage());
		} catch (Exception e) {
			log.warning(e.getMessage());
			ret = false;
		}		
		return ret;
	}

	public boolean sendmail(String msg, String filename, String to1) {
		boolean ret = false;
		log.info("in sendmail");
		//System.out.println("in sendmail");
	    try {
	    	SecurityServiceImpl susi = new SecurityServiceImpl();
		    String to2 = susi.adminUser1Value;//"James.Albersheim@nrel.gov";
		    //String to2 = "david.crocker@nrel.gov";
		    String from = susi.emailUserValue;//"James.Albersheim@nrel.gov";
		    String host = susi.emailHostValue;//"mailgate1.nrel.gov";
		
		    // Create properties, get Session
		    Properties props = new Properties();
		
		    props.put("mail.smtp.host", host);
		    props.put("mail.debug", "true");
		    javax.mail.Session session = javax.mail.Session.getInstance(props);
	
	        Message msg1 = new MimeMessage(session);
	        msg1.setFrom(new InternetAddress(from));
	        InternetAddress[] address = {new InternetAddress(to1), new InternetAddress(to2)};
	        msg1.setRecipients(Message.RecipientType.TO, address);
	        msg1.setSubject("Algae - "+msg);
	        msg1.setSentDate(new Date());
	        
	        // create and fill the first message part
	        MimeBodyPart mbp1 = new MimeBodyPart();
	        mbp1.setText("This is a notification sent from Algae.\n" +
	    	                    msg);

	        // create the second message part
	        MimeBodyPart mbp2 = null;

	        // attach the file to the message
	        if (filename != null && !filename.isEmpty()) {
	        	mbp2 = new MimeBodyPart();
		        FileDataSource fds = new FileDataSource(filename);
		        mbp2.setDataHandler(new DataHandler(fds));
		        mbp2.setFileName(fds.getName());
	        }

	        // create the Multipart and add its parts to it
	        Multipart mp = new MimeMultipart();
	        mp.addBodyPart(mbp1);
	        if (mbp2 != null)
	        	mp.addBodyPart(mbp2);

	        // add the Multipart to the message
	        msg1.setContent(mp);

	        //Send the message
	        Transport t = session.getTransport("smtp");
	        try {
	        	DataEncryption de = new DataEncryption(CRYPTOKEY);
	        	String unc = de.decrypt(susi.emailPassValue);
	            t.connect(susi.emailUserValue, unc);
	            t.sendMessage(msg1, msg1.getAllRecipients());
	            ret = true;
	        } catch (Exception e1) {
	        	ret = false;
	        	log.info("caught: "+e1.getMessage());
	        	//System.out.println("caught: "+e1.getMessage());
	        } finally {
	            t.close();
	        }
	    } catch (MessagingException mex) {
	    	ret = false;
			log.warning(mex.getMessage());
		} catch (Exception e) {
			log.warning(e.getMessage());
			ret = false;
		}		
		return ret;
	}

	@Override
	public List<String> getGroupsForUser(String id) {
		SecurityServiceImpl ssi = new SecurityServiceImpl();
		List<String> groups = ssi.getGroupsForUser(id);
		return groups;
	}

	private boolean insertLabelToPrint(gov.nrel.nbc.tracker.model.Sample sample, String sessionId, String printer) {
		log.info("in insertLabelToPrint");
		boolean ret = true;
		DBUtils db = null;
	    try {
			LabelDTO label = new LabelDTO();
			label.setCustodian(sample.getCustodianName());
			label.setDescription(sample.getLabel_description());
			Destinations destination = sample.getDestination();
			String dest = "";
			if (destination != null) dest = destination.getName();
			label.setDestination(dest);
			label.setEntryDate(sample.getCreateDate().toString());
			Forms form = sample.getForm();
			String frm = "";
			if (form != null) frm = form.getName();
			label.setForm(frm);
			Fractions fraction = sample.getFraction();
			String frction = "";
			if (fraction != null) frction = fraction.getName();
			label.setFraction(frction);
			Composition comp = sample.getComposition();
			String cmp = "";
			if (comp != null) cmp = comp.getName();
			label.setComposition(cmp);
			Strains strain = sample.getStrain();
			String stn = "";
			if (strain != null) stn = strain.getName();
			label.setStrain(stn);
			label.setSampleId(sample.getSampleId());
			label.setTrackingId(String.valueOf(sample.getId()));
	        String msg = createLabelMessage(label, printer);
		    
	    	TrackerFileUploadServiceImpl ssi = new TrackerFileUploadServiceImpl();
	        String dbPath = ssi.db_dev;
	        log.info("db="+dbPath);
	        String dbUser = ssi.db_user;
	        
	        DataEncryption de = new DataEncryption(CRYPTOKEY);
	        String dbPass=de.decrypt(ssi.db_pass);
			//Insert the message
	        db = new DBUtils(dbPath,dbUser,dbPass);
	        String sql = "insert into "+LABEL_TABLE+" (session_id,label) values ('"+sessionId+"','"+msg+"')";
	        int rows = db.performInsert(sql);
	        log.info("rows inserted="+rows);
		} catch (Exception e) {
			log.warning(e.getMessage());
			ret = false;
		} finally {
			db.close();
		}
		return ret;
	}

}
