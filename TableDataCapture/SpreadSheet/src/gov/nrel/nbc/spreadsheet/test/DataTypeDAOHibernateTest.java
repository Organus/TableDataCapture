package gov.nrel.nbc.spreadsheet.test;

import org.hibernate.Session;

import gov.nrel.nbc.spreadsheet.dao.DataTypeDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DataTypeDAOHibernateTest extends TestCase {

	ClassLoader thisLoader;
	private DataTypeDAOHibernate dtdh;
		
	//@Override
	protected void setUp() {
    	thisLoader = DataTypeDAOHibernate.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(DataTypeDAOHibernateTest.class);
	}

	public void testFindById() {
		System.out.println("in testFindById");
	    dtdh = new DataTypeDAOHibernate();
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		dtdh.setSession(session);
	    DataType type = dtdh.findById(1);
	    assertNotNull(type);
	    System.out.println("got type="+type.getDescription());
	    session.close();
	}
	public void testFindByDescription() {
	    dtdh = new DataTypeDAOHibernate();
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		dtdh.setSession(session);
	    DataType type = dtdh.findByDescription("STRING");
	    assertNotNull(type);
	    System.out.println("got type id="+type.getId());
	    session.close();
	}
}
