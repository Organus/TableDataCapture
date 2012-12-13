package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.client.CriteriaTrioDTO;
import gov.nrel.nbc.spreadsheet.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.SheetDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;

public class MetadataHeaderDAOHibernateTest extends TestCase {

	ClassLoader thisLoader;
	private MetadataHeaderDAOHibernate mhdh;
	
	  /**
	   * Add as many tests as you like.
	   */
	  public void testSomething() {
			String ret = "";
			mhdh = new MetadataHeaderDAOHibernate();
			System.out.println(ret);
	  }

	//@Override
	protected void setUp() {
    	thisLoader = MetadataHeaderDAOHibernate.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(MetadataHeaderDAOHibernateTest.class);
	}

	public void testFindAllSynonyms() {
	    mhdh = new MetadataHeaderDAOHibernate();
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		mhdh.setSession(session);
	    List<String> syns = mhdh.findAllNames();
	    assertNotNull(syns);
	    Iterator<String> it = syns.iterator();
	    while (it.hasNext()) {
	    	String syn = it.next();
	    	assertNotNull(syn);
	    	System.out.println("got syn="+syn);
	    }
	    session.close();
	}
	
	public void testGetTypeBySynonym () {
		mhdh = new MetadataHeaderDAOHibernate();
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		mhdh.setSession(session);
		DataType type = mhdh.getTypeByName("Comments");
		assertNotNull(type);
		System.out.println("got type for comments="+type.getDescription());
	    session.close();
	}
	
	public void testFindBySynonym() {
		mhdh = new MetadataHeaderDAOHibernate();
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		mhdh.setSession(session);
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName("SolidsPreDecember2010");
		assertNotNull(config);
		MetadataHeader header = mhdh.findByName("Submitted By",config);
		assertNotNull(header);
		System.out.println("Submitted By has header="+header.getName());
	    session.close();
	}
	
	public void testFindByValue() {
		mhdh = new MetadataHeaderDAOHibernate();
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		mhdh.setSession(session);
		wcdh.setSession(session);
		WorkbookConfig config = wcdh.findByName("SolidsPreDecember2010");
		assertNotNull(config);
		SheetDAOHibernate sdh = new SheetDAOHibernate();
		sdh.setSession(session);
		//SheetConfig shConfig = wcdh.findSheetByConfigAndName(config, "Digestion");
		CriteriaTrioDTO trio = new CriteriaTrioDTO();
		ArrayList<CriteriaTrioDTO> trios = null;
		MetadataHeader mdh = null;
		//List<WorkbookRows> mcds = null;
		//Iterator<WorkbookRows> it = null;
		/**/
		mdh = mhdh.findByName("Submitted By",config);
		assertNotNull(mdh);
		String header = mdh.getName();
		assertNotNull(header);
		System.out.println("header="+header);
		trio.setHeader("Submitted By");
		trio.setOperator("=");
		trio.setValue("James Albersheim");
		trios = new ArrayList<CriteriaTrioDTO>();
		trios.add(trio);
		trio.setHeader("Feedstock Type");
		trio.setOperator("!=");
		trio.setValue("sor*");
		trios = new ArrayList<CriteriaTrioDTO>();
		trios.add(trio);
		HibernateSessionFactory.closeSession();
	}
}
