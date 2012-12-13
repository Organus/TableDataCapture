package gov.nrel.nbc.spreadsheet.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import gov.nrel.nbc.spreadsheet.client.NameValue;
import gov.nrel.nbc.spreadsheet.parse.NetCDFFileParser;
import gov.nrel.nbc.spreadsheet.parse.PDFFileParser;
import gov.nrel.nbc.spreadsheet.parse.RowDTO;
import gov.nrel.nbc.spreadsheet.parse.TextFileParser;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

public class NetCDFFileParserTest extends TestCase {
	ClassLoader thisLoader;
	@Before
	public void setUp() throws Exception {
    	thisLoader = NetCDFFileParser.class.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(NetCDFFileParserTest.class);
	}
	/*
	public void testGetHeaders() throws Exception {
		NetCDFFileParser np = new NetCDFFileParser(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc"));
		np.getHeaders(np.getDataFile());
	}
	
	public void testGetData() throws Exception {
		NetCDFFileParser np = new NetCDFFileParser(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc"));//"/Users/jalbersh/Downloads/tika/ us_cities.xml"));
		//List<RowDTO> rows = np.getData("");
		np.getDataRows(np.getDataFile());
		np.setWsheet("main");
		List<RowDTO> rows = np.getData();
		System.out.println("has "+rows.size()+" rows of data");
	}
	
	public void testGetAllData() throws Exception {
		NetCDFFileParser np = new NetCDFFileParser(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc"));
		//List<RowDTO> rows = np.getData("");
		np.getDataRows(np.getDataFile());
		List<RowDTO> rows = np.getData();
		System.out.println("has "+rows.size()+" rows of data");
	}

	public void testGetDataRows() throws Exception {
		NetCDFFileParser np = new NetCDFFileParser(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc"));
		np.getDataRows(np.getDataFile());
		//System.out.println("content="+content);
	}
	
	*/
	public void testGetMetaData() throws Exception {
		NetCDFFileParser np = new NetCDFFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc"));
		List<NameValue> metas = np.getCDFMetadata(np.getDataFile());
		Iterator<NameValue> mit = metas.iterator();
		while (mit.hasNext()) {
			NameValue nv = mit.next();
			System.out.println(nv.getName()+" has value "+nv.getValue());
		}
	}
	
	public void testTextMetaData() throws Exception {
		TextFileParser tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/sm110531_pcs ck cell_ring cup_spectra std.nir"));
		List<NameValue> metas = tfp.getMetadata(tfp.getDataFile());
		Iterator<NameValue> mit = metas.iterator();
		while (mit.hasNext()) {
			NameValue nv = mit.next();
			System.out.println(nv.getName()+" has value "+nv.getValue());
		}
		tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/CP101116_stds 140_8res 128scan.SPG"));
		metas = tfp.getMetadata(tfp.getDataFile());
		mit = metas.iterator();
		while (mit.hasNext()) {
			NameValue nv = mit.next();
			System.out.println(nv.getName()+" has value "+nv.getValue());
		}
		tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/SM110531_pcs ck cell_ring cup_std winisi predict.anl"));
		metas = tfp.getMetadata(tfp.getDataFile());
		mit = metas.iterator();
		while (mit.hasNext()) {
			NameValue nv = mit.next();
			System.out.println(nv.getName()+" has value "+nv.getValue());
		}
		tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/CP101116_stds_0001.spa"));
		metas = tfp.getMetadata(tfp.getDataFile());
		mit = metas.iterator();
		while (mit.hasNext()) {
			NameValue nv = mit.next();
			System.out.println(nv.getName()+" has value "+nv.getValue());
		}
	}

	public void testTextContent() throws Exception {
		TextFileParser tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/sm110531_pcs ck cell_ring cup_spectra std.nir"));
		String content = tfp.getContent(tfp.getDataFile());
		System.out.println("content="+content);
		tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/CP101116_stds 140_8res 128scan.SPG"));
		content = tfp.getContent(tfp.getDataFile());
		System.out.println("content="+content);
		tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/SM110531_pcs ck cell_ring cup_std winisi predict.anl"));
		content = tfp.getContent(tfp.getDataFile());
		System.out.println("content="+content);
		tfp = new TextFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/CP101116_stds_0001.spa"));
		content = tfp.getContent(tfp.getDataFile());
		System.out.println("content="+content);
	}

	public void testPDFContent() throws Exception {
		PDFFileParser tfp = new PDFFileParser(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/NREL Subcontract Docs/Bi-Monthly Status Reports/Bi-Monthly Technical Status Report-06.04-06.17.pdf"));
		String content = tfp.getContent(tfp.getDataFile());
		System.out.println("content="+content);
	}

	public void testPDFMetaData() throws Exception {
		PDFFileParser tfp = new PDFFileParser(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/NREL Subcontract Docs/Bi-Monthly Status Reports/Bi-Monthly Technical Status Report-06.04-06.17.pdf"));
		List<NameValue> metas = tfp.getMetadata(tfp.getDataFile());
		Iterator<NameValue> mit = metas.iterator();
		while (mit.hasNext()) {
			NameValue nv = mit.next();
			System.out.println(nv.getName()+" has value "+nv.getValue());
		}
	}
	
	public void testGetContent() throws Exception {
		NetCDFFileParser np = new NetCDFFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc"));
		String content = np.getCDFContent(np.getDataFile());
		System.out.println("content="+content);
		List<NameValue> metas = np.getCDFMetadata(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc"));
		Iterator<NameValue> mit = metas.iterator();
		while (mit.hasNext()) {
			NameValue nv = mit.next();
			System.out.println(nv.getName()+" has value "+nv.getValue());
		}
		//sm110531_pcs ck cell_ring cup_spectra std.nir
		np = new NetCDFFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/sm110531_pcs ck cell_ring cup_spectra std.nir"));
		content = np.getNumbersContent(np.getDataFile());
		System.out.println("content="+content);
		//SM110531_pcs ck cell_ring cup_std winisi predict.anl
		np = new NetCDFFileParser(true,new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/SM110531_pcs ck cell_ring cup_std winisi predict.anl"));
		content = np.getNumbersContent(np.getDataFile());
		System.out.println("content="+content);
		//us_cities.numbers
		//np = new NetCDFFileParser(new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/us_cities.numbers"));
		//content = np.getNumbersContent(np.getDataFile());
		//System.out.println("content="+content);
	}
	
	public void testOS () throws Exception {
		  String nameOS = "os.name";  
		  String versionOS = "os.version";  
		  String architectureOS = "os.arch";
		  System.out.println("\n  The information about OS");
		  System.out.println("\nName of the OS: " + 
		  System.getProperty(nameOS));
		  System.out.println("Version of the OS: " + 
		  System.getProperty(versionOS));
		  System.out.println("Architecture of THe OS: " + 
		  System.getProperty(architectureOS));
	}
	
}
