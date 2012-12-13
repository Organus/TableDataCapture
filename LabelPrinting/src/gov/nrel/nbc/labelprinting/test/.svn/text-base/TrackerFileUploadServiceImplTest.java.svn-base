package gov.nrel.nbc.labelprinting.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import gov.nrel.nbc.labelprinting.dao.LocationDAOHibernate;
import gov.nrel.nbc.labelprinting.dao.SampleDAOHibernate;
import gov.nrel.nbc.labelprinting.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.labelprinting.model.Location;
import gov.nrel.nbc.labelprinting.model.Sample;
import gov.nrel.nbc.labelprinting.server.TrackerFileUploadServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TrackerFileUploadServiceImplTest extends TestCase {
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = TrackerFileUploadServiceImpl.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(TrackerFileUploadServiceImplTest.class);
	}
	
	public void testCreateNewSampleWithOldLocation() throws Exception {
	    String url = "http://localhost/tracker/clientUploadService";
	    String webXML = "C:\\projects\\james\\tracker\\war\\WEB-INF\\web.xml";
	    File fXML = new File(webXML);
	    ServletRunner sr = new ServletRunner(fXML);
	    sr.registerServlet("/tracker/clientUploadService",TrackerFileUploadServiceImpl.class.getName());
	    ServletUnitClient sc = sr.newClient();
	    WebRequest request = new PostMethodWebRequest(url, true);
	    // get old number of locations
	    LocationDAOHibernate ldh = new LocationDAOHibernate();
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		ldh.setSession(session);
	    List<Location> locations = ldh.findAll();
	    int numOldLocs = 0;
	    if (locations != null && !locations.isEmpty())
	    	numOldLocs = locations.size();
		request.setParameter("sampleTrbPage", "67");
	    request.setParameter("sampleTrbNum", "4290");
	    request.setParameter("sampleStorageNotes", "");
	    request.setParameter("samplePackaging", "baggy");
	    request.setParameter("sampleCase", "corn stover");
	    request.setParameter("sampleMatrix", "123");
	    request.setParameter("sampleShelf", "lower");
	    request.setParameter("sampleSublocation", "06");
	    request.setParameter("sampleRoom", "117");
	    request.setParameter("sampleContainer", "bottle");
	    request.setParameter("sampleBuilding", "FTLB");
	    request.setParameter("sampleStatus", "In Process");
	    request.setParameter("sampleFraction", "liquid");
	    request.setParameter("sampleFeedstock", "corn stover");
	    request.setParameter("sampleState", "feedstock");
	    request.setParameter("sampleEmpName", "James Albersheim");
	    request.setParameter("sampleOwnName", "Von Berry");
	    request.setParameter("sampleExternalId", "");
	    request.setParameter("sampleUnits", "grams");
	    request.setParameter("sampleAmount", "50");
	    request.setParameter("sampleComments", "");
	    request.setParameter("sampleName", "JWA10302009-1");
	    SampleDAOHibernate sdh = new SampleDAOHibernate();
	    sdh.setSession(session);
	    List<Sample> samples = sdh.findAll();
	    int numOldSamples = 0;
	    if (samples != null && !samples.isEmpty())
	    	numOldSamples = samples.size();
	    try
	    {
	    	// Use the InvocationContext to create an instance
	    	// of the servlet
	    	InvocationContext ic = sc.newInvocation(request);
	    	TrackerFileUploadServiceImpl cfuServlet = null;
	    	try {
	    		cfuServlet = (TrackerFileUploadServiceImpl)ic.getServlet();
	    		assertNull("A session already exists",
	    				ic.getRequest().getSession(false));
	    	}
	    	catch (Exception e2) {System.out.println("Error initializing cdServlet. Exception is " + e2); }
	    	HttpServletRequest cdServletRequest = ic.getRequest();
	    	HttpServletResponse cdServletResponse = ic.getResponse();
	    	if (cfuServlet != null) {
	    		try {
	    			cfuServlet.doPost(cdServletRequest,cdServletResponse);
	    		} catch (Exception e5) {System.out.println("Error calling servlet.doPost. Exception is " + e5);e5.printStackTrace();}
	    	}
	    }
	    catch (Exception e) {System.out.println("Error testing CalcFileUploadServiceImpl. Exception is " + e); e.printStackTrace(); }
	    session = HibernateSessionFactory.getSession();
		ldh.setSession(session);
	    locations = ldh.findAll();
	    int numNewLocs = 0;
	    if (locations != null && !locations.isEmpty())
	    	numNewLocs = locations.size();
	    assertEquals(numOldLocs,numNewLocs);
	    sdh.setSession(session);
	    samples = sdh.findAll();
	    int numNewSamples = 0;
	    if (samples != null && !samples.isEmpty())
	    	numNewSamples = samples.size();
	    assertEquals(numNewSamples,numOldSamples+1);
	    session.close();
	}
	public void testCreateNewSampleWithNewLocation() throws Exception {
	    String url = "http://localhost/tracker/clientUploadService";
	    String webXML = "C:\\projects\\james\\tracker\\war\\WEB-INF\\web.xml";
	    File fXML = new File(webXML);
	    ServletRunner sr = new ServletRunner(fXML);
	    sr.registerServlet("/tracker/clientUploadService",TrackerFileUploadServiceImpl.class.getName());
	    ServletUnitClient sc = sr.newClient();
	    WebRequest request = new PostMethodWebRequest(url, true);
	    LocationDAOHibernate ldh = new LocationDAOHibernate();
		Session session = HibernateSessionFactory.getSession();	
		session.beginTransaction();
		ldh.setSession(session);	
	    List<Location> locations = ldh.findAll();
	    int numOldLocs = 0;
	    if (locations != null && !locations.isEmpty())
	    	numOldLocs = locations.size();
	    List<String> rooms = ldh.findAllRooms();
	    int roomnum = 117;
	    String newRoom = "";
	    Iterator<String> it = rooms.iterator();
	    while (it.hasNext()) {
	    	String room = it.next();
	    	if (room != null && room.equals(String.valueOf(roomnum))) {
	    		roomnum++;
	    		newRoom = String.valueOf(roomnum);
	    	}
	    	else {
	    		try {
	    			newRoom = String.valueOf(Integer.parseInt(room)+1);
	    		} catch (NumberFormatException nfe) {}
	    	}
	    }
	    System.out.println("got newRoom="+newRoom);
		request.setParameter("sampleTrbPage", "67");
	    request.setParameter("sampleTrbNum", "4290");
	    request.setParameter("sampleStorageNotes", "");
	    request.setParameter("samplePackaging", "baggy");
	    request.setParameter("sampleCase", "corn stover");
	    request.setParameter("sampleMatrix", "123");
	    request.setParameter("sampleShelf", "lower");
	    request.setParameter("sampleSublocation", "06");
	    request.setParameter("sampleRoom", newRoom);
	    request.setParameter("sampleContainer", "bottle");
	    request.setParameter("sampleBuilding", "FTLB");
	    request.setParameter("sampleStatus", "In Process");
	    request.setParameter("sampleFraction", "liquid");
	    request.setParameter("sampleFeedstock", "corn stover");
	    request.setParameter("sampleState", "feedstock");
	    request.setParameter("sampleEmpName", "James Albersheim");
	    request.setParameter("sampleOwnName", "Von Berry");
	    request.setParameter("sampleExternalId", "");
	    request.setParameter("sampleUnits", "grams");
	    request.setParameter("sampleAmount", "50");
	    request.setParameter("sampleComments", "");
	    request.setParameter("sampleName", "JWA10302009-1");
	    SampleDAOHibernate sdh = new SampleDAOHibernate();
	    sdh.setSession(session);
	    List<Sample> samples = sdh.findAll();
	    int numOldSamples = 0;
	    if (samples != null && !samples.isEmpty())
	    	numOldSamples = samples.size();
	    try
	    {
	    	// Use the InvocationContext to create an instance
	    	// of the servlet
	    	InvocationContext ic = sc.newInvocation(request);
	    	TrackerFileUploadServiceImpl cfuServlet = null;
	    	try {
	    		cfuServlet = (TrackerFileUploadServiceImpl)ic.getServlet();
	    		assertNull("A session already exists",
	    				ic.getRequest().getSession(false));
	    	}
	    	catch (Exception e2) {System.out.println("Error initializing cdServlet. Exception is " + e2); }
	    	HttpServletRequest cdServletRequest = ic.getRequest();
	    	HttpServletResponse cdServletResponse = ic.getResponse();
	    	if (cfuServlet != null) {
	    		try {
	    			cfuServlet.doPost(cdServletRequest,cdServletResponse);
	    		} catch (Exception e5) {System.out.println("Error calling servlet.doPost. Exception is " + e5);e5.printStackTrace();}
	    	}
	    }
	    catch (Exception e) {System.out.println("Error testing CalcFileUploadServiceImpl. Exception is " + e); e.printStackTrace(); }
	    session = HibernateSessionFactory.getSession();
	    ldh.setSession(session);
	    locations = ldh.findAll();
	    int numNewLocs = 0;
	    if (locations != null && !locations.isEmpty())
	    	numNewLocs = locations.size();
	    assertEquals(numNewLocs,numOldLocs+1);
	    sdh.setSession(session);
	    samples = sdh.findAll();
	    int numNewSamples = 0;
	    if (samples != null && !samples.isEmpty())
	    	numNewSamples = samples.size();
	    assertEquals(numNewSamples,numOldSamples+1);
	    session.close();
	}
}
