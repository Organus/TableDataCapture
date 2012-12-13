package gov.nrel.nbc.labelprinting.test;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.client.FileInfo;
import gov.nrel.nbc.labelprinting.client.LabelDTO;
import gov.nrel.nbc.labelprinting.client.SampleCriteria;
import gov.nrel.nbc.labelprinting.model.Composition;
import gov.nrel.nbc.labelprinting.model.Destinations;
import gov.nrel.nbc.labelprinting.model.Forms;
import gov.nrel.nbc.labelprinting.model.Sample;
import gov.nrel.nbc.labelprinting.model.Strains;
import gov.nrel.nbc.labelprinting.server.TrackerServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TrackerServiceImplTest extends TestCase implements AppConstants {	
	ClassLoader thisLoader;

	@Override
	protected void setUp() {
		thisLoader = TrackerServiceImpl.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(TrackerServiceImplTest.class);
	}
	
	public void testGetDeployDir() {
		String deployPath = "C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/jboss-as-7.0.2.Final/standalone";
		String dir = TrackerServiceImpl.getDeployDir(deployPath+"/tmp/vfs");
		System.out.println("deploy dir="+dir);
		dir = TrackerServiceImpl.getContentDir(dir);
		System.out.println("content dir="+dir);
	}
	public void testPrintingByEmail() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		LabelDTO label = new LabelDTO();
		label.setTrackingId("3180");
		tsi.printLabel("0", label, "");
	}
	public void testParse() throws Exception {
		String value = "a:8:{s:12:\"company_name\";s:18:\"company name field\";s:15";
		int co = value.indexOf("company_name");
		if (co > 0)	{
			String sub = value.substring(co+"company_name".length()+4); // ";s#
			int start = sub.indexOf(":")+2; // "c
			sub = sub.substring(start);
			int end = sub.indexOf(";")-1;
			sub = sub.substring(0,end);
			System.out.println("sub="+sub);
		}
		
	}
	///*
	public void testSearchByBuilding() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setBuilding("AFUF");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String building = sample.getBuilding();
			assertEquals("AFUF",building);
		}
	}

	public void testSearchByAmount() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setAmount("50");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String amount = sample.getAmount();
			assertEquals("50",amount);
		}
	}

	public void testSearchByComments() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setComments("none");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String comments = sample.getComments();
			assertEquals("none",comments);
		}
	}

	public void testSearchByCreateDate() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		Calendar cal = Calendar.getInstance();
		cal.set(2009, 9, 5, 0, 0, 0);
		Date date = cal.getTime();
		criteria.setStartCreateDate(date);
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			Date date1 = sample.getStartCreateDate();
			System.out.println("got date="+date1.toString());
			assertEquals("2009-10-05",date1.toString());
		}
	}

	public void testSearchByCustodian() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		boolean other = false;
		criteria.setCustodianName("Jeff Wolfe");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		if (results == null) {
			criteria.setCustodianName("Weff Jolfe");
			other = true;
			results = tsi.searchSamples(criteria,"");
		}
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String cust = sample.getCustodianName();
			if (other)
				assertEquals("Weff Jolfe",cust);
			else
				assertEquals("Jeff Wolfe",cust);
		}
	}

	public void testBarCode() {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Sample sample = new Sample();
		sample.setBiomassLot("my biomass lot");
		sample.setCreateDate(Calendar.getInstance().getTime());
		sample.setDescription("My description is going to be a very long one. Now is the time for all good men to come to the aide of their party.");
		Destinations dest = new Destinations();
		dest.setName("My Desk");
		sample.setDestination(dest);
		Strains strain = new Strains();
		strain.setName("back strain");
		sample.setStrain(strain);
		Forms form = new Forms();
		form.setName("great form");
		sample.setForm(form);
		Composition comp = new Composition();
		comp.setName("metamorphic");
		sample.setCustodianName("NREL:James Albersheim");
		sample.setSampleId("My Test Sample");
		sample.setId(45678);
		tsi.createLabelImage(sample);
	}
	
	public void testSearchByExternalId() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setExternalId("121409-4");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String exid = sample.getExternalId();
			assertEquals("121409-4",exid);
		}
	}

	public void testSearchByFeedstock() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setFeedstock("corn stover");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String stock = sample.getFeedstock();
			assertEquals("corn stover",stock);
		}
	}

	public void testSearchByFraction() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setFraction("solid");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String fraction = sample.getFraction();
			assertEquals("solid",fraction);
		}
	}

	public void testSearchByHolder() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setHolder("Cart");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String holder = sample.getHolder();
			assertEquals("Cart",holder);
		}
	}

	public void testSearchByOwner() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setOwnerName("Nick Nagle");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String owner = sample.getOwnerName();
			assertEquals("Nick Nagle",owner);
		}
	}

	public void testSearchByPackaging() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setPackaging("Jar");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String pkg = sample.getPackaging();
			assertEquals("Jar",pkg);
		}
	}

	public void testSearchByRoom() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setRoom("215 Cold Room");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String room = sample.getRoom();
			assertEquals("215 Cold Room",room);
		}
	}

	public void testSearchBySampleId() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setSampleId("090902a preOH");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			assertEquals("090902a preOH",sampleId);
		}
	}

	public void testSearchByShelf() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setShelf("lower");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		boolean found = false;
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String shelf = sample.getShelf();
			if (shelf.equals("lower")) found = true;
		}
		assertTrue(found);
	}

	public void testSearchByState() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setTreatment("pretreated");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String state = sample.getTreatment();
			assertEquals("pretreated",state);
		}
	}

	public void testSearchByStatus() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setStatus("In Process");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String status = sample.getStatus();
			assertEquals("In Process",status);
		}
	}

	public void testSearchByStorageNotes() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setStorageNotes("none");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String notes = sample.getStorageNotes();
			assertEquals("none",notes);
		}
	}

	public void testSearchBySubLocation() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setSubLocation("06");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String subloc = sample.getSubLocation();
			assertEquals("06",subloc);
		}
	}

	public void testSearchByTrackingId() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setTrackingId(3);
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			long id = sample.getTrackingId();
			assertEquals(3,id);
		}
	}

	public void testSearchByTrbNum() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setTrbNum(1301);
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			int num = sample.getTrbNum();
			assertEquals(1301,num);
		}
	}

	public void testSearchByTrbPage() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setTrbPage(28);
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			int page = sample.getTrbPage();
			assertEquals(28,page);
		}
	}

	public void testSearchByUnits() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setUnits("grams");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String sampleId = sample.getSampleId();
			System.out.println("got sample="+sampleId);
			String units = sample.getUnits();
			assertEquals("grams",units);
		}
	}
	
	public void testSaveSample() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setCustodianName("Jeff Wolfe");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		boolean other = false;
		if (results == null) {
			other = true;
			criteria.setCustodianName("Weff Jolfe");
			results = tsi.searchSamples(criteria,"");
		}
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			assertNotNull(sample);
			String cust = sample.getCustodianName();
			long id = sample.getTrackingId();
			if (!other) {
				assertEquals("Jeff Wolfe",cust);
				sample.setCustodianName("Weff Jolfe");
			}
			else {
				assertEquals("Weff Jolfe",cust);
				sample.setCustodianName("Jeff Wolfe");
			}
			// TODO
			tsi.saveSample("1", sample);
			Collection<SampleCriteria> results1 = tsi.searchSamples(sample,"");
			assertNotNull(results1);
			Iterator<SampleCriteria> it1 = results1.iterator();
			assertTrue(results1.size()>0);
			System.out.println("got "+results1.size()+" results");
			while (it1.hasNext()) {
				SampleCriteria sample1 = it1.next();
				long id1 = sample1.getTrackingId();
				assertEquals(id,id1);
				String cust1 = sample1.getCustodianName();
				if (!other) {
					assertEquals("Weff Jolfe",cust1);
					sample1.setCustodianName("Jeff Wolfe");
				} else {
					assertEquals("Jeff Wolfe",cust1);
					sample1.setCustodianName("Weff Jolfe");
				}
				// TODO 
				tsi.saveSample("1", sample);				
			}
		}
	}
		
	public void testPrintLabel() throws Exception {
		//assertTrue(false);
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		LabelDTO label = new LabelDTO();
		label.setTrbNum("1301");
		label.setTrbPage("28");
		label.setSampleId("121109-6");
		label.setOwnerName("James Walter Albersheim");
		label.setTrackingId("33");
		//label.setEntryDate("06/23/2009");
//		boolean ret = label.printLabel("\\\\bberry-14764s\\14764s_15747s");
//		boolean ret = label.printLabel("\\\\dcrocker-14415s\\14415s_15747s");
		boolean ret = true;
    	String os = System.getenv("os");
    	System.out.println("os="+os);
    	// TODO
    	if (os != null && os.toLowerCase().indexOf("windows") != -1) {
    		ret = tsi.printLabel("1",label,"\\\\bberry-14764s\\14764s_15747s");
    		//ret = tsi.printLabel(label,"\\\\dcrocker-14415s\\Zebra  TLP3842");
    		//ret = tsi.printLabel(label,"\\\\ewolfrum-10389s\\Zebra  TLP3842");
    	} else {
    		ret = tsi.printLabel("1",label,"14764s_15747s");
    		//ret = tsi.printLabel(label,"14415s_15747s");
    		//ret = tsi.printLabel(label,"10389s_15747s");
    	}
		assertTrue(ret);
	}	
	
	public void testGetAttachmentIds() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.findAttachmentsByTrackingId(34);
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetAttachmentInfo() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<FileInfo> sids = tsi.findAttachmentsInfoByTrackingId(34);
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<FileInfo> it = sids.iterator();
		while (it.hasNext()) {
			FileInfo sid = it.next();
			System.out.println("got sid="+sid.getFilename());
		}
	}

	public void testDeleteSample() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Boolean ret = tsi.deleteSample(35);
		assertTrue(ret);
	}

	public void testFindSample() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria sc = tsi.findSampleById(34);
		assertNotNull(sc);
		System.out.println("got sc="+sc.getSampleId());
	}

	public void testGetBuildings() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getBuildings();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetCustodians() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getCustodianNames();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetFeedstockNames() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getFeedstockNames();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetFractionNames() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getFractionNames();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetOwnerNames() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getOwnerNames();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetPackaging() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getPackaging();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetRooms() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getRooms();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetSampleIds() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getSampleIds(JUNIT_SESSION_ID);
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetStateNames() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getTreatmentNames();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetSubLocations() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getSubLocations();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetUnits() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getUnits();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testTrbHasFile() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Boolean sids = tsi.trbHasFile("1301","11");
		assertNotNull(sids);
		System.out.println("got sid="+sids.booleanValue());
	}

	public void testGetHolders() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getHolders();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetShelves() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		Collection<String> sids = tsi.getShelves();
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}

	public void testGetDataFile() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		SampleCriteria criteria = new SampleCriteria();
		criteria.setAmount("50");
		Collection<SampleCriteria> results = tsi.searchSamples(criteria,"");
		assertNotNull(results);
		Iterator<SampleCriteria> it = results.iterator();
		assertTrue(results.size()>0);
		System.out.println("got "+results.size()+" results");
		if (results.size()>0) {
			List<SampleCriteria> samples = new ArrayList<SampleCriteria>();
			samples.addAll(results);
			String df = tsi.getDataFile(samples, "");
			assertNotNull(df);
			System.out.println("df="+df);
		}
	}
	
	public void testGetStatusNames() throws Exception {
		TrackerServiceImpl tsi = new TrackerServiceImpl();
		String id = tsi.getSessionId();
		Collection<String> sids = tsi.getStatusNames(id);
		assertNotNull(sids);
		assertTrue(sids.size()>0);
		Iterator<String> it = sids.iterator();
		while (it.hasNext()) {
			String sid = it.next();
			System.out.println("got sid="+sid);
		}
	}
	//*/

}
