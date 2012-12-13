package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

public class populateWorkTest extends TestCase implements AppConstants {
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = SpreadSheetServiceImpl.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(populateWorkTest.class);
	}
	
	public void testPopulateWork() throws Exception {
		/*
			Connection connSS = null;
			Statement stmtTest1 = null;
			Statement stmtTest2 = null;
			try {
				// Load the database driver
				Class.forName( MYSQL_DRIVER ) ;
				// Get a connection to the database
		        String urlTest = FIRST_PART_DB;
		        if (DevTestProdConstants.DEV_PROD_TEST.equals(DevTestProdConstants.LOCAL))
		        	urlTest += DevTestProdConstants.DEV;
		        else
		        	urlTest += DevTestProdConstants.DEV_PROD_TEST;
		        urlTest += LAST_PART_DB;
		        connSS = DriverManager.getConnection(urlTest,USER,PASS);
				// Get a statement from the connection
				stmtTest1 = connSS.createStatement() ;
				stmtTest2 = connSS.createStatement() ;
				
				String sampleSQL = "select `Tracking ID`,`Work Type`,`Acid Concentration (%)`, `Temperature (deg. C)`, `time (min)`, `sample ID` from "
								+ " `Mels Total Mass Closure_Total Mass Closure_cell` where `Tracking ID` > 0 and row_data_id>604";
				ResultSet rsTest = stmtTest1.executeQuery( sampleSQL ) ;
				while (rsTest.next()) {
					long trackingId = rsTest.getLong(1);
					String workKind = rsTest.getString(2);
					double acid = rsTest.getDouble(3);
					String temp = rsTest.getString(4);
					double time = rsTest.getDouble(5);
					String sampleId = rsTest.getString(6);
					try {
						// workkind - 720(SG), 712 (ZC)
						// config - 22
						// 
						if (workKind.equals("Zip Clave"))
							workKind = "Zipperclave";
						else if (workKind.equals("Steam Gun"))
							workKind = "Steamgun";
						WorkCriteria workCriteria = new WorkCriteria();
						workCriteria.addInput(new Long(1640));
						workCriteria.addOutput(new Long(trackingId));
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.HOUR, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						workCriteria.setSampleId(sampleId);
						workCriteria.setWhenDone(cal.getTime());
						workCriteria.setWorker("Mel Tucker");				
						workCriteria.setWorkKind(workKind);
						List<NameValue> details = new ArrayList<NameValue>();
						NameValue nv = new NameValue();
						nv.setName("Impregnation Catalyst");
						nv.setValue("FeSO4");
						details.add(nv);
						nv = new NameValue();
						nv.setName("Acid Loading (mg catalyst/g feedstock)");
						nv.setValue(String.valueOf(acid));
						details.add(nv);
						nv = new NameValue();
						nv.setName("Reaction Time (s)");
						String stime = String.valueOf(time*60.0);
						nv.setValue(stime);
						details.add(nv);
						nv = new NameValue();
						nv.setName("Reaction Temperature (C)");
						nv.setValue(temp);
						details.add(nv);
						nv = new NameValue();
						nv.setName("Feedstock");
						nv.setValue("Kramer - 33A14");
						details.add(nv);
						nv = new NameValue();
						nv.setName("Client");
						nv.setValue("Mel Tucker");
						details.add(nv);
						nv = new NameValue();
						nv.setName("Task");
						nv.setValue("BB072150");
						details.add(nv);
						nv = new NameValue();
						nv.setName("Task Number");
						nv.setValue("BB072150");
						details.add(nv);
						nv = new NameValue();
						nv.setName("Comments");
						nv.setValue("Ingested from AnnotatedFTIR NIR Sample Log sheet3-9-10_DC.xlsx");
						details.add(nv);
						workCriteria.setDetails(details);
						WorkServiceImpl wsi = new WorkServiceImpl();
						wsi.createWork(workCriteria);					
					} catch( Exception se ) {
					      System.out.println( "Exception:" ) ;
					      System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
					} 
				}
			} catch( SQLException se ) {
			      System.out.println( "SQL Exception:" ) ;
			      System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
			      // Loop through the SQL Exceptions
			      while( se != null )
			      {
			          System.out.println( "Message: " + se.getMessage()   ) ;
			          se = se.getNextException() ;
			      }
			} catch (Exception e) {
				System.out.println("Exception on getting type. error: " + e);
				System.out.println(SpreadSheetServiceImpl.getStackTrace(e));
			} finally {
				// Close the result set, statement and the connection
				try {
					stmtTest1.close() ;
					stmtTest2.close() ;
					connSS.close() ;
					//tx.commit();
				} catch( SQLException se ) {
				      System.out.println( "SQL Exception:" ) ;
				      System.out.println(SpreadSheetServiceImpl.getStackTrace(se));
				} catch (Exception e) {
					System.out.println("Exception on getting type. error: " + e);
					System.out.println(SpreadSheetServiceImpl.getStackTrace(e));
				}
			}
			*/
	}
}
