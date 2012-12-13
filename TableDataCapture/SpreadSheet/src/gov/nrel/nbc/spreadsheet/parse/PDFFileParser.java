
package gov.nrel.nbc.spreadsheet.parse;

import gov.nrel.nbc.spreadsheet.client.NameValue;
import gov.nrel.nbc.spreadsheet.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.DataTypeDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.SheetDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheet.dto.DataFormat;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.dto.GenericValue;
import gov.nrel.nbc.spreadsheet.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheet.dto.SheetConfig;
import gov.nrel.nbc.spreadsheet.dto.ValueData;
import gov.nrel.nbc.spreadsheet.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;
import gov.nrel.nbc.spreadsheet.utilities.XLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import javax.servlet.ServletException;

//import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.tika.metadata.Metadata;
//import ucar.nc2.NetcdfFile;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.iwork.IWorkPackageParser;
//import org.apache.tika.parser.iwork.IWorkParser;
import org.apache.tika.parser.netcdf.NetCDFParser;
//import org.apache.tika.parser.pdf.PDFParser;
//import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.xml.sax.ContentHandler;

@SuppressWarnings("deprecation")
public class PDFFileParser extends GenericFileParser {
    private static final XLogger log = new XLogger(PDFFileParser.class);
    
	/**
	 * The config
	 */
	private WorkbookConfig wbConfig;

	/**
	 * The session
	 */
	private Session session;
	/**
	 * The tags for the data.
	 */
	private List<CellDataHeader> headers;

	/**
	 * The data.
	 */
	private List<RowDTO> data;
	
	private String wsheet;

	/** 
     * A data file for parsing.
     */
    private File dataFile;

	private static final String MM_DD_YYYY = "MM/dd/yyyy";

	private static final String STRING = "STRING";

	private static final String DATE = "DATE";

	private static final String REAL = "REAL";

	private static final String LONG = "LONG";

	private static final String BOOLEAN = "BOOLEAN";
	
	private static final String START_OF_INFO = "sf:ct sfa:s=";
	
	private static final String END_OF_INFO = "sf:ct=";
	
	private static final String START_OF_DATA = "sf:v=";
	
	private static final String OTHER_DATA = "sfa:s=";
	
	private static final String START_VALUE = "<sf:n";
	
	private static final String END_VALUE = "</sf:n";

	private static final String START_OTHER_VALUE = "<sf:t";
	
	private static final String END_OTHER_VALUE = "</sf:t";

	private static final String END_OF_DATA = "sf:datasource";
	
    private String DELIMITER = "\t";
    
    //private String SPACES = "    ";
    
    public static final String TAB = "\t";
    
    public static final String COMMA = ",";

    private String QUOTE = "\"";
    
    public PDFFileParser(File file) throws Exception {
        if (file == null || ! file.exists()) {
        	log.severe("File was not found! File: " + file.getPath());
            throw new FileNotFoundException(file.getPath());
        }
        dataFile = file;
    }

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet
	 *            The Worksheet we parse. 
	 *            
	 */
	public List<NameValue> getMetadataNames(String worksheet) {
		List<NameValue> metadatas = new ArrayList<NameValue>();
		//try {
	        //String content = getContent(dataFile);
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			DataTypeDAOHibernate dtdh = new DataTypeDAOHibernate();
			dtdh.setSession(session);
			sdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstMDRow = (short)(dbSheet.getMeta_start_row()-1);
			short lastMDRow = (short)(dbSheet.getMeta_end_row()-1);
			short firstMDCol = (short)(dbSheet.getMeta_start_col()-1);
			if (firstMDRow > -1 && lastMDRow > 0) {
				// skip to first MD row
				int rowCtr=0;
				//while (rowCtr< firstMDRow) { scanner.nextLine(); rowCtr++; }
				for (;rowCtr<=lastMDRow;rowCtr++) {
		        	//String line = scanner.nextLine();
		        	//Scanner lineScan = new Scanner(line);
		        	//lineScan.useDelimiter(DELIMITER);
		        	int colCtr=0;
		        	//while (lineScan.hasNext()) {
		        		// skip to first MD col
		        		if (colCtr < firstMDCol) {
		        			//lineScan.next();
		        		} else {
			        		String value = "";
			        		//value = lineScan.next();
							NameValue nv = new NameValue();
							nv.setOrder(colCtr);
							String name = value;
							nv.setName(name);
							metadatas.add(nv);
		        		}
			        	colCtr++;
		        	//}
				}
			}
		return metadatas;
	}

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet
	 *            The Worksheet we parse. 
	 */
	public List<NameValue> getMetadata(String worksheet) {
		List<NameValue> metadatas = new ArrayList<NameValue>();
		try {
	        //String content = getContent(dataFile);
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			DataTypeDAOHibernate dtdh = new DataTypeDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			mhdh.setSession(session);
			dtdh.setSession(session);
			sdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstMDRow = (short)(dbSheet.getMeta_start_row()-1);
			short lastMDRow = (short)(dbSheet.getMeta_end_row()-1);
			short firstMDCol = (short)(dbSheet.getMeta_start_col()-1);
			short lastMDCol = (short)(dbSheet.getMeta_end_col()-1);
			// skip to first MD row
			int rowCtr=0;
			//while (rowCtr< firstMDRow) { scanner.nextLine(); rowCtr++; }
			for (;rowCtr<=lastMDRow;rowCtr++) {
				NameValue nv = new NameValue();
				nv.setOrder(rowCtr-firstMDRow);
	        	//String line = scanner.nextLine();
	        	//Scanner lineScan = new Scanner(line);
	        	//lineScan.useDelimiter(DELIMITER);
	        	// skip to firstMDol
	        	int colCtr=0;
	        	//while (lineScan.hasNext()&&colCtr<firstMDCol) { lineScan.next(); colCtr++; } 
				for (;colCtr<=lastMDCol;colCtr++) {
					if (colCtr == firstMDCol) {
						String name = "";
						//name = lineScan.next();
						nv.setName(name);
					} else if (colCtr == lastMDCol) {
						String value = "";
						//value = lineScan.next();
						nv.setValue(value);
						MetadataHeader header = mhdh.findByName(nv.getName(),wbConfig);
						if (header != null) {
							DataType dt = header.getTypeId();
							int type = (int)dt.getId();
							if (type==3) {// date
								try {
									DataFormat format = header.getData_format();
									String sformat = MM_DD_YYYY;
									if (format != null) {
											sformat = format.getFormat();
									}
									SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
									dFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
									Date date = dFormat.parse(value);
									String dvalue = dFormat.format(date);
									nv.setValue(dvalue);
								} catch (IllegalArgumentException ise) {
									log.warning("problem getting date from text");
									nv.setValue(value);
								}
							}
						}
					} 
				}
				if (nv.getValue().length()>0)
					metadatas.add(nv);
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			//try {
		    //    scanner.close();
			//} catch (Exception ioe) {
			//	log.warning("Exception caught: "+ioe);
			//}
		}
		return metadatas;
	}

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet String - The Worksheet we parse.
	 * @return List<String>
	 */
	public List<String> getHeaders(String worksheet) {
		List<String> hdrs = new ArrayList<String>();
		try {
 			String nameOS = "os.name";  
 			String os = System.getProperty(nameOS);	  
 			if (os != null && (os.toLowerCase().contains("windows")||os.toLowerCase().contains("mac")||os.toLowerCase().contains("linux")))
 				return getHeaders(worksheet,dataFile);
	        String content = getContent(dataFile);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			//SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			//short firstCell = (short)(dbSheet.getHdr_col()-1);
			//short headerRow = (short) (dbSheet.getHdr_row()-1);
			// skip to first header row
			int start = content.indexOf(START_OF_INFO);
			if (start > -1) {
				content = content.substring(start+START_OF_INFO.length()+1);
				int end = content.indexOf(END_OF_INFO);
				int begin = 0;
				int index = content.indexOf(QUOTE);
				int sum = index+begin;
				log.info("length="+(index-begin));
				String temp = content;
				while (sum+begin < end) {
					String headerName = temp.substring(begin,index);
					log.info("header="+headerName);
					headerName = headerName.trim();
					headerName = headerName.replace("\"","");
					hdrs.add(headerName);
					temp = temp.substring(index+START_OF_INFO.length()+1); 
					begin = temp.indexOf(START_OF_INFO)+START_OF_INFO.length()+1;
					//log.info("begin substr="+temp.substring(begin));
					index = temp.substring(begin).indexOf(QUOTE)+begin;
					log.info("length="+(index-begin));
					sum += index;
				}
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			//try {
		    //    scanner.close();
			//} catch (Exception ioe) {
			//	log.warning("Exception caught: "+ioe);
			//}
		}
		return hdrs;
	}

	public List<String> getHeaders(File f) {
		List<String> hdrs = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String content = "";
			String line = br.readLine();
			while (line != null) {
				content += line;
				line = br.readLine();
			}
			fr.close();
			br.close();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			//SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			//short firstCell = (short)(dbSheet.getHdr_col()-1);
			//short headerRow = (short) (dbSheet.getHdr_row()-1);
			// skip to first header row
			int start = content.indexOf(START_OF_INFO);
			if (start > -1) {
				content = content.substring(start+START_OF_INFO.length()+1);
				int end = content.indexOf(END_OF_INFO);
				int begin = 0;
				int index = content.indexOf(QUOTE);
				int sum = index+begin;
				log.info("length="+(index-begin));
				String temp = content;
				while (sum+begin < end) {
					String headerName = temp.substring(begin,index);
					log.info("header="+headerName);
					headerName = headerName.trim();
					headerName = headerName.replace("\"","");
					hdrs.add(headerName);
					temp = temp.substring(index+START_OF_INFO.length()+1); 
					begin = temp.indexOf(START_OF_INFO)+START_OF_INFO.length()+1;
					//log.info("begin substr="+temp.substring(begin));
					index = temp.substring(begin).indexOf(QUOTE)+begin;
					log.info("length="+(index-begin));
					sum += index;
				}
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			//try {
		    //    scanner.close();
			//} catch (Exception ioe) {
			//	log.warning("Exception caught: "+ioe);
			//}
		}
		return hdrs;
	}

    /**
	 * Get the data from the worksheet.
	 * 
	 * @param workbook
	 *            The Workbook we parse.
	 * @throws ServletException 
	 */
	private int getDataRows(String worksheet) throws IOException,
			ServletException, Exception {
 		try {
 			String nameOS = "os.name";  
 			String os = System.getProperty(nameOS);	  
 			if (os != null && (os.toLowerCase().contains("windows")||os.toLowerCase().contains("mac")||os.toLowerCase().contains("linux")))
 				return getDataRows(worksheet,dataFile);
 			String content = getContent(dataFile);
			data = new ArrayList<RowDTO>();
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			sdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			CellHeaderDAOHibernate cddh = new CellHeaderDAOHibernate();
			cddh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			int firstDataRow = (int)dbSheet.getData_row()-1;
			int firstDataCol = (int)dbSheet.getData_col()-1;
			//int lastCol = firstDataCol + headers.size();
			// skip headers
			String temp = "";
			int end = 0;
			int begin = 0;
			int sum = 0;
			int index = 0;
			int start = content.indexOf(START_OF_INFO);
			if (start > -1) {
				content = content.substring(start+START_OF_INFO.length()+1);
				end = content.indexOf(END_OF_INFO);
				begin = 0;
				index = content.indexOf(QUOTE);
				sum = index+begin;
				//log.info("length="+(index-begin));
				temp = content;
				while (sum+begin < end) {
					//String headerName = temp.substring(begin,index);
					//log.info("header="+headerName);
					temp = temp.substring(index+START_OF_INFO.length()+1); 
					begin = temp.indexOf(START_OF_INFO)+START_OF_INFO.length()+1;
					//log.info("begin substr="+temp.substring(begin));
					index = temp.substring(begin).indexOf(QUOTE)+begin;
					//log.info("length="+(index-begin));
					sum += index;
				}
			}
			end = temp.indexOf(END_OF_INFO);
			int row = 0;
//			while (row < firstDataRow && scanner.hasNext()) { scanner.nextLine(); row++; }
//			for (; scanner.hasNext(); row++) {
			boolean first = true;
			end = temp.indexOf(END_OF_DATA);
			int col = 0;
			RowDTO rowDto = new RowDTO();
			while (sum+begin < end) {
				String heading = "";
				if (col >= headers.size()) {
					first = false;
					rowDto = new RowDTO();
					row++;
					col = 0;
					if (temp.indexOf(START_OF_INFO) == -1 || temp.indexOf(START_OF_INFO)>temp.indexOf(END_OF_DATA))
						break;
				}
				for (; col < headers.size(); col++) {
					String sValue = "";
					if (begin==index)
						sValue = " ";
					else
						sValue = temp.substring(begin,index);
					int usedHeaderCol = firstDataCol;
					//lastCol = firstDataCol + headers.size();
					// skip to first data column
//	        		while (col < firstDataCol && lineScan.hasNext()) { lineScan.next(); col++; }
//					for (; col < lastCol && lineScan.hasNext(); col++) {
					//log.info("sValue="+sValue);
					rowDto = populateRowDTO(sValue,col,row,headers,rowDto,usedHeaderCol,firstDataRow );
					if (!first && col == 0) {
						heading = START_OF_INFO;
					}
					temp = temp.substring(index+heading.length()+1); 
					String start_heading = "";
					String end_heading = "";
					if (temp.indexOf(START_OF_DATA) < temp.indexOf(OTHER_DATA)) {
						heading = START_OF_DATA;
						start_heading = START_VALUE;
						end_heading = END_VALUE;
					}
					else {
						heading = OTHER_DATA;
						start_heading = START_OTHER_VALUE;
						end_heading = END_OTHER_VALUE;
					}
					int head = temp.indexOf(heading);
					int sv = 0;
					int ev = 0;
					if (start_heading.equals(START_VALUE))
					{
						if (head > temp.indexOf(END_VALUE)+1) {
							sv=temp.substring(temp.indexOf(END_VALUE)+1).indexOf(start_heading)+temp.indexOf(END_VALUE)+1;
							ev=temp.substring(temp.indexOf(END_VALUE)+1).indexOf(end_heading)+temp.indexOf(END_VALUE)+1;
						} else {
							sv=temp.indexOf(start_heading);
							ev=temp.indexOf(end_heading);													
						}
					} 
					else
					{
						sv=temp.indexOf(start_heading);
						ev=temp.indexOf(end_heading);						
					}
					if (col==0 || (sv < head && ev > head)) {
						begin = temp.indexOf(heading)+heading.length()+1;
						//log.info("begin substr="+temp.substring(begin));
						index = temp.substring(begin).indexOf(QUOTE)+begin;
					} else {
						begin = index = ev;
						if (col == headers.size()-1) {
							rowDto = populateRowDTO("",col+1,row,headers,rowDto,usedHeaderCol,firstDataRow );
							col = headers.size();
						}
					}
					usedHeaderCol++;
					sum += index;
				}
				if ((rowDto.getRow() != null) && (!rowDto.getRow().isEmpty())) {
					data.add(rowDto);
				}
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		}
		return 0;
	}

	private RowDTO populateRowDTO (String sValue, int col,int row, List<String> headers, RowDTO rowDto ) {
		CellDataDTO cellData = new CellDataDTO();
		boolean errorFlag = false;
		//if (sValue != null) sValue = sValue.trim();
		if ((sValue != null) && (!sValue.isEmpty())) {
			GenericValue gValue = new GenericValue();
			ValueData gd = new ValueData();
			//CellDataHeader header = headers.get(usedHeaderCol-firstDataCol);
			String header = "";
			//if (first)
			//	header = headers.get(col-1);
			//else
			header = headers.get(col);
			if (header == null) {
				log.warning("header for col="+(col-1)+" was null");
			} else {
				String headerName = header;
				//log.info("headerName="+headerName);
				String stype = STRING;
				if (stype.equals(LONG) || stype.equals(REAL)) {
					if (stype.equals(LONG)) {
						try {
							gValue.setLongValue(Integer.parseInt(sValue));
							cellData.setType(LONG);
							gd.setLvalue(gValue.getLongValue());
							//log.info("LONG: Cell type: " + type.toString()
							//	+ "; Tag: " + headerName);
						} catch (NumberFormatException nfe) {
							log.warning("Invalid data: " + sValue);
							//errorFlag = true;
							sValue = "0";
						}
					} else if (stype.equals(REAL)) {
						//return AppConstants.SPREADSHEET_ERRORS;
						sValue = "0.0";
						//throw new ServletException("There was an error in the spreadsheet.");
					} else {
						try {
							if (!sValue.equals("true") && !sValue.equals("false")) {
								double val = Double.parseDouble(sValue);
								gValue.setRealValue(val);
								cellData.setType(REAL);
								gd.setRvalue(gValue.getRealValue());
							} else {
								boolean bValue = false;
								bValue = Boolean.parseBoolean(sValue);
								gValue.setBooleanValue(bValue);
								cellData.setType(BOOLEAN);
								gd.setBvalue(gValue.getBooleanValue());
							}
						} catch (NumberFormatException nfe) {
							log.warning("Invalid data: " + sValue);
							errorFlag = true;
						}
					}
				} else if (stype.equals(DATE)) {
		        	GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		        	Date collectionTime = new Date();
		        	try {
						DataFormat format = null;//header.getData_format();
						String sformat = MM_DD_YYYY;
						if (format != null) sformat = format.getFormat();
						SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
						dFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
						Date date = null;
						try {
							date = dFormat.parse(sValue);
						} catch (ParseException e1) {}
						//String dvalue = dFormat.format(date);
		        		collectionTime = date;
		        	} catch (ClassCastException cce) {
		        		log.warning(cce.getMessage());
		        		//return AppConstants.MISMATCHED_TYPE;
		        		sValue = "";
		        	}
		        	cal.setTime(collectionTime);
					gValue.setDateValue(cal.getTime());
					cellData.setType(DATE);
					gd.setDvalue(gValue.getDateValue());
				} else if (stype.equals(BOOLEAN)) {
					boolean bValue = false;
					bValue = Boolean.parseBoolean(sValue);
					gValue.setBooleanValue(bValue);
					cellData.setType(BOOLEAN);
					gd.setBvalue(gValue.getBooleanValue());
				} else {
					gValue.setStringValue(sValue);
					cellData.setType(STRING);
					gd.setSvalue(gValue.getStringValue());
				}
				if ((errorFlag != true)) {
					cellData.setValue(gd);
					rowDto.setSheetRow(row);
					cellData.setTag(headerName);
					rowDto.addCell(cellData);
					log.info(headerName+" has value="+gd.getSvalue());
				}
			}
		}
		return rowDto;
	}
	
	private RowDTO populateRowDTO (String sValue, int col,int row, List<CellDataHeader> headers, RowDTO rowDto, int usedHeaderCol, int firstDataCol ) {
		CellDataDTO cellData = new CellDataDTO();
		boolean errorFlag = false;
		//if (sValue != null) sValue = sValue.trim();
		if ((sValue != null) && (!sValue.isEmpty())) {
			GenericValue gValue = new GenericValue();
			ValueData gd = new ValueData();
			CellDataHeader header = headers.get(usedHeaderCol-firstDataCol);
			header = headers.get(col);
			if (header == null) {
				log.warning("header for col="+(col-1)+" was null");
			} else {
				String headerName = header.getName();
				//log.info("headerName="+headerName);
				String stype = STRING;
				if (stype.equals(LONG) || stype.equals(REAL)) {
					if (stype.equals(LONG)) {
						try {
							gValue.setLongValue(Integer.parseInt(sValue));
							cellData.setType(LONG);
							gd.setLvalue(gValue.getLongValue());
							//log.info("LONG: Cell type: " + type.toString()
							//	+ "; Tag: " + headerName);
						} catch (NumberFormatException nfe) {
							log.warning("Invalid data: " + sValue);
							//errorFlag = true;
							sValue = "0";
						}
					} else if (stype.equals(REAL)) {
						//return AppConstants.SPREADSHEET_ERRORS;
						sValue = "0.0";
						//throw new ServletException("There was an error in the spreadsheet.");
					} else {
						try {
							if (!sValue.equals("true") && !sValue.equals("false")) {
								double val = Double.parseDouble(sValue);
								gValue.setRealValue(val);
								cellData.setType(REAL);
								gd.setRvalue(gValue.getRealValue());
							} else {
								boolean bValue = false;
								bValue = Boolean.parseBoolean(sValue);
								gValue.setBooleanValue(bValue);
								cellData.setType(BOOLEAN);
								gd.setBvalue(gValue.getBooleanValue());
							}
						} catch (NumberFormatException nfe) {
							log.warning("Invalid data: " + sValue);
							errorFlag = true;
						}
					}
				} else if (stype.equals(DATE)) {
		        	GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		        	Date collectionTime = new Date();
		        	try {
						DataFormat format = header.getData_format();
						String sformat = MM_DD_YYYY;
						if (format != null) sformat = format.getFormat();
						SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
						dFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
						Date date = null;
						try {
							date = dFormat.parse(sValue);
						} catch (ParseException e1) {}
						//String dvalue = dFormat.format(date);
		        		collectionTime = date;
		        	} catch (ClassCastException cce) {
		        		log.warning(cce.getMessage());
		        		//return AppConstants.MISMATCHED_TYPE;
		        		sValue = "";
		        	}
		        	cal.setTime(collectionTime);
					gValue.setDateValue(cal.getTime());
					cellData.setType(DATE);
					gd.setDvalue(gValue.getDateValue());
				} else if (stype.equals(BOOLEAN)) {
					boolean bValue = false;
					bValue = Boolean.parseBoolean(sValue);
					gValue.setBooleanValue(bValue);
					cellData.setType(BOOLEAN);
					gd.setBvalue(gValue.getBooleanValue());
				} else {
					gValue.setStringValue(sValue);
					cellData.setType(STRING);
					gd.setSvalue(gValue.getStringValue());
				}
				if ((errorFlag != true)) {
					cellData.setValue(gd);
					rowDto.setSheetRow(row);
					cellData.setTag(headerName);
					rowDto.addCell(cellData);
					log.info(headerName+" has value="+gd.getSvalue());
				}
			}
		}
		return rowDto;
	}

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet String - The Worksheet we parse.
	 * @return List<String>
	 */
	public List<String> getHeaders(String worksheet, File f) {
        Scanner scanner = null;
		List<String> hdrs = new ArrayList<String>();
		if (headers != null) headers.clear();
		else headers = new ArrayList<CellDataHeader>();
		try {
			String content = getContent(f);
	        scanner = new Scanner(content);
	        scanner.useDelimiter(DELIMITER);
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			chdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstCell = (short)(dbSheet.getHdr_col()-1);
			short headerRow = (short) (dbSheet.getHdr_row()-1);
			// skip to first header row
			int rowCtr=0;
			while (rowCtr< headerRow) { scanner.nextLine(); rowCtr++; }
        	String line = scanner.nextLine();
        	Scanner lineScan = new Scanner(line);
        	lineScan.useDelimiter(DELIMITER);
        	int colCtr=0;
			while (lineScan.hasNext()) 
			{
				// skip to first header cell
				if (colCtr<firstCell) {
					lineScan.next();
				} else {
					String headerName = lineScan.next();
					headerName = headerName.trim();
					headerName = headerName.replace("\"","");
					hdrs.add(headerName);
					CellDataHeader cellHeader = chdh.findByNameAndConfig(dbSheet, headerName);
					if (cellHeader != null) {
						log.info(cellHeader.getName());
						headers.add(cellHeader);
					}
				}
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			try {
		        scanner.close();
			} catch (Exception ioe) {
				log.warning("Exception caught: "+ioe);
			}
		}
		return hdrs;
	}

    /**
	 * Get the data from the Summary worksheet.
	 * 
	 * @param workbook
	 *            The Workbook we parse.
	 * @throws ServletException 
	 */
	private int getDataRows(String worksheet, File f) throws IOException,
			ServletException, Exception {
        Scanner scanner = null;
		try {
			String content = getContent(f);
			log.info("content="+content);
	        scanner = new Scanner(content);
	        scanner.useDelimiter(DELIMITER);
			data = new ArrayList<RowDTO>();
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			sdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			CellHeaderDAOHibernate cddh = new CellHeaderDAOHibernate();
			cddh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			int firstDataRow = (int)dbSheet.getData_row()-1;
			int firstDataCol = (int)dbSheet.getData_col()-1;
			headers = cddh.findByConfig(dbSheet);
			int lastCol = headers.size();//firstDataCol + headers.size();
			log.info("there are "+headers.size()+" headers ");
			// skip to first data row
			int row = 0;
			log.info("firstDataCol="+firstDataCol+";lastCol="+lastCol);
			while (row < firstDataRow && scanner.hasNext()) { scanner.nextLine(); row++; }
			for (; scanner.hasNext(); row++) {
				RowDTO rowDto = new RowDTO();
				int usedHeaderCol = firstDataCol;
				lastCol = headers.size();//firstDataCol + headers.size();
	        	String line = scanner.nextLine();
	        	Scanner lineScan = new Scanner(line);
	        	lineScan.useDelimiter(DELIMITER);
	        	int col=0;
	        	// skip to first data column
	        	while (col < firstDataCol && lineScan.hasNext()) { lineScan.next(); col++; }
				for (col = 0; col < lastCol && lineScan.hasNext(); col++) {
					CellDataDTO cellData = new CellDataDTO();
					boolean errorFlag = false;
					String sValue = lineScan.next();
					if (sValue != null) sValue = sValue.trim();
					if ((sValue != null) && (!sValue.isEmpty())) {
						GenericValue gValue = new GenericValue();
						ValueData gd = new ValueData();
						CellDataHeader header = headers.get(col);//usedHeaderCol-firstDataCol);
						if (header == null) {
							log.warning("header for col="+(col)+" was null");//usedHeaderCol-firstDataCol)+" was null");
						} else {
							String headerName = header.getName();
							String stype = header.getTypeId().getDescription();
							log.info("headerName="+headerName+" has type="+stype+" with data-"+sValue);
							if (stype.equals(LONG) || stype.equals(REAL)) {
								if (stype.equals(LONG)) {
									try {
										gValue.setLongValue(Integer.parseInt(sValue));
										cellData.setType(LONG);
										gd.setLvalue(gValue.getLongValue());
										//log.info("LONG: Cell type: " + type.toString()
										//	+ "; Tag: " + headerName);
									} catch (NumberFormatException nfe) {
										log.warning("Invalid long data: " + sValue);
										//if (sValue.equals("-")) {
											gValue.setLongValue(0);
											cellData.setType(LONG);
											gd.setLvalue(gValue.getLongValue());
										//}
										//errorFlag = true;
									}
								} else if (stype.equals(LONG)) {
									//return AppConstants.SPREADSHEET_ERRORS;
									sValue = "";
									//throw new ServletException("There was an error in the spreadsheet.");
								} else {
									try {
										if (!sValue.equals("true") && !sValue.equals("false")) {
											double val = Double.parseDouble(sValue);
											gValue.setRealValue(val);
											cellData.setType(REAL);
											gd.setRvalue(gValue.getRealValue());
										} else {
											boolean bValue = false;
											bValue = Boolean.parseBoolean(sValue);
											gValue.setBooleanValue(bValue);
											cellData.setType(BOOLEAN);
											gd.setBvalue(gValue.getBooleanValue());
										}
									} catch (NumberFormatException nfe) {
										//log.warning("Invalid boolean data: " + sValue);
										//errorFlag = true;
										gValue.setStringValue(sValue);
										cellData.setType(STRING);
										gd.setSvalue(gValue.getStringValue());
									}
								}
							} else if (stype.equals(DATE)) {
					        	GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
					        	Date collectionTime = new Date();
					        	try {
									DataFormat format = header.getData_format();
									String sformat = MM_DD_YYYY;
									if (format != null) sformat = format.getFormat();
									SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
									dFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
									Date date = dFormat.parse(sValue);
									//String dvalue = dFormat.format(date);
					        		collectionTime = date;
					        	} catch (ClassCastException cce) {
					        		log.warning(cce.getMessage());
					        		//return AppConstants.MISMATCHED_TYPE;
					        		sValue = "";
					        	}
					        	cal.setTime(collectionTime);
								gValue.setDateValue(cal.getTime());
								cellData.setType(DATE);
								gd.setDvalue(gValue.getDateValue());
							} else if (stype.equals(BOOLEAN)) {
								boolean bValue = false;
								bValue = Boolean.parseBoolean(sValue);
								gValue.setBooleanValue(bValue);
								cellData.setType(BOOLEAN);
								gd.setBvalue(gValue.getBooleanValue());
							} else {
								gValue.setStringValue(sValue);
								cellData.setType(STRING);
								gd.setSvalue(gValue.getStringValue());
							}
							if ((errorFlag != true)) {
								cellData.setValue(gd);
								rowDto.setSheetRow(row);
								cellData.setTag(headerName);
								rowDto.addCell(cellData);
							}
						}
					} else {
						//log.info("cell contents NULL for header="+headers.get(usedHeaderCol-firstDataCol).getName()+" at row="+row);
					}
					usedHeaderCol++;
				}
				if ((rowDto.getRow() != null) && (!rowDto.getRow().isEmpty()))
					data.add(rowDto);
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			try {
		        scanner.close();
			} catch (Exception ioe) {
				log.warning("Exception caught: "+ioe);
			}
		}
		return 0;
	}

	/**
	 * Get the data from the worksheet.
	 * 
	 * @param workbook
	 *            The Workbook we parse.
	 * @throws ServletException 
	 */
	public int getDataRows(File f) throws IOException,
			ServletException, Exception {
 		try {
 			// Mime: application/vnd.apple.numbers
 			String nameOS = "os.name";  
 			String os = System.getProperty(nameOS);	  
 			if (os != null && (os.toLowerCase().contains("windows")||os.toLowerCase().contains("mac")||os.toLowerCase().contains("linux")))
 				return getDataRows(wsheet,f);
 			String content = getContent(f);
			data = new ArrayList<RowDTO>();
			List<String> headers1 = new ArrayList<String>();
			String temp = "";
			int end = 0;
			int begin = 0;
			int sum = 0;
			int index = 0;
			int start = content.indexOf(START_OF_INFO);
			if (start > -1) {
				content = content.substring(start+START_OF_INFO.length()+1);
				end = content.indexOf(END_OF_INFO);
				begin = 0;
				index = content.indexOf(QUOTE);
				sum = index+begin;
				//log.info("length="+(index-begin));
				temp = content;
				while (sum+begin < end) {
					String headerName = temp.substring(begin,index);
					//log.info("header="+headerName);
					headers1.add(headerName);
					temp = temp.substring(index+START_OF_INFO.length()+1); 
					begin = temp.indexOf(START_OF_INFO)+START_OF_INFO.length()+1;
					//log.info("begin substr="+temp.substring(begin));
					index = temp.substring(begin).indexOf(QUOTE)+begin;
					//log.info("length="+(index-begin));
					sum += index;
				}
			}
			boolean first = true;
			end = temp.indexOf(END_OF_DATA);
			int row = 0;
			int col = 0;
			RowDTO rowDto = new RowDTO();
			while (sum+begin < end) {
				String heading = "";
				if (col >= headers.size()) {
					first = false;
					rowDto = new RowDTO();
					row++;
					col = 0;
					if (temp.indexOf(START_OF_INFO) == -1 || temp.indexOf(START_OF_INFO)>temp.indexOf(END_OF_DATA))
						break;
				}
				for (; col < headers.size(); col++) {
					String sValue = "";
					if (begin==index)
						sValue = " ";
					else
						sValue = temp.substring(begin,index);
					//log.info("sValue="+sValue);
					rowDto = populateRowDTO(sValue,col,row,headers1,rowDto);
					if (!first && col == 0) {
						heading = START_OF_INFO;
					}
					temp = temp.substring(index+heading.length()+1); 
					String start_heading = "";
					String end_heading = "";
					if (temp.indexOf(START_OF_DATA) < temp.indexOf(OTHER_DATA)) {
						heading = START_OF_DATA;
						start_heading = START_VALUE;
						end_heading = END_VALUE;
					}
					else {
						heading = OTHER_DATA;
						start_heading = START_OTHER_VALUE;
						end_heading = END_OTHER_VALUE;
					}
					int head = temp.indexOf(heading);
					int sv = 0;
					int ev = 0;
					if (start_heading.equals(START_VALUE))
					{
						if (head > temp.indexOf(END_VALUE)+1) {
							sv=temp.substring(temp.indexOf(END_VALUE)+1).indexOf(start_heading)+temp.indexOf(END_VALUE)+1;
							ev=temp.substring(temp.indexOf(END_VALUE)+1).indexOf(end_heading)+temp.indexOf(END_VALUE)+1;
						} else {
							sv=temp.indexOf(start_heading);
							ev=temp.indexOf(end_heading);													
						}
					} 
					else
					{
						sv=temp.indexOf(start_heading);
						ev=temp.indexOf(end_heading);						
					}
					if (col==0 || (sv < head && ev > head)) {
						begin = temp.indexOf(heading)+heading.length()+1;
						//log.info("begin substr="+temp.substring(begin));
						index = temp.substring(begin).indexOf(QUOTE)+begin;
					} else {
						begin = index = ev;
						if (col == headers.size()-1) {
							rowDto = populateRowDTO("",col+1,row,headers1,rowDto);
							col = headers.size();
						}
					}
					sum += index;
				}
				if ((rowDto.getRow() != null) && (!rowDto.getRow().isEmpty())) {
					data.add(rowDto);
				}
			}
		} catch (Exception e) {
		//} finally {
		//	if (br != null) br.close();
		//	if (fr != null) fr.close();
		}
		return 0;
	}

	public String getContent(File f) {
	    InputStream is = null;
	    String content = null;
	    try {
	      //ContentHandler contenthandler = new BodyContentHandler();
	      //Metadata metadata = new Metadata();
	      //metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
	      //is = new FileInputStream(f);
	      //PDFParser txtParser = new PDFParser(is);
	      //ParseContext parseContext = new ParseContext();
	      //parseContext.set(Parser.class, new AutoDetectParser());
	      //contenthandler = new BodyContentHandler(10000000);
	      //metadata = new Metadata();
	      //txtParser.parse();//is, contenthandler, metadata, parseContext);
	      PDFParser parser = new PDFParser(new FileInputStream(f));
	      parser.parse();
	      COSDocument cosDoc = parser.getDocument();
	      PDDocument pdDoc = new PDDocument(cosDoc);
	      PDFTextStripper textStripper=new PDFTextStripper();//"UTF-16LE");
	      //textStripper.setLineSeparator("\r\n");
          //File outFile = null;

          //outFile = new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/NREL Subcontract Docs/Bi-Monthly Status Reports/", "test-pdf.txt");

	      content = textStripper.getText(pdDoc);
	      pdDoc.close();
	      // application/vnd.apple.numbers
	      // application/x-netcdf
	      //metadata.set(Metadata.CONTENT_TYPE, "application/pdf");
	      //metadata.set("org.apache.tika.parser.pdf.sortbyposition", "false");
	      //System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
	      //content = contenthandler.toString();
		}
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	    	if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
	    }
	    return content;
	}
	
	public InputStream getInputStream(String path) throws Exception {
        StringBuffer holder = new StringBuffer();
        try{
            FileInputStream reader = new FileInputStream(path);
	        BufferedReader br = new BufferedReader(new InputStreamReader(reader));
	        String strLine;
	        //Read File Line By Line
	        boolean start = true;
	        while ((strLine = br.readLine()) != null)   {
	                if( !start )    
	                        holder.append("\n");
	
	                holder.append(strLine);
	                start = false;
	        }
	        //Close the input stream
	        reader.close();
	    } catch (Throwable e){//this is where the heap error is caught up to 2Gb
	    	System.err.println("Error: " + e.getMessage());
	    }

	    System.out.println("size is "+holder.length());
        return new StringBufferInputStream(holder.toString());
	}
	public String getCDFContent(File f) {
	    InputStream is = null;
	    String content = null;
	    try {
	    	String path = f.getPath();
		    ContentHandler contenthandler = new BodyContentHandler();
		    Metadata metadata = new Metadata();
		    metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
		    NetCDFParser netCDFParser = new NetCDFParser();
		    ParseContext parseContext = new ParseContext();
		    parseContext.set(Parser.class, new AutoDetectParser());
		    InputStream is1 = getInputStream(path);
		    System.out.println("avail1="+is1.available());
		    is = new FileInputStream(f);
		    System.out.println("avail="+is.available());
		    contenthandler = new BodyContentHandler(10000000);
		    metadata = new Metadata();
		    metadata.set(Metadata.CONTENT_TYPE, "application/x-netcdf");
		    System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
		    netCDFParser.parse(is, contenthandler, metadata, parseContext);
		    // application/vnd.apple.numbers
		    // application/x-netcdf
		    content = contenthandler.toString();
		}
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    finally {
	    	if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
	    }
	    return content;
	}
	
	public List<NameValue> getMetadata(File f) {
	    FileInputStream fis = null;
	    List<NameValue> metas = new ArrayList<NameValue>();
	    try {
	      ContentHandler contenthandler = new BodyContentHandler();
	      Metadata metadata = new Metadata();
	      metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
	      fis = new FileInputStream(f);
	      PDFParser txtParser = new PDFParser(fis);
	      ParseContext parseContext = new ParseContext();
	      parseContext.set(Parser.class, new AutoDetectParser());
	      contenthandler = new BodyContentHandler(10000000);
	      metadata = new Metadata();
	      InputStream is = new FileInputStream(f);
	      txtParser.parse();//is, contenthandler, metadata, parseContext);
	      // application/vnd.apple.numbers
	      // application/x-netcdf
	      metadata.set("org.apache.tika.parser.pdf.sortbyposition", "false");
	      //metadata.set(Metadata.CONTENT_TYPE, "application/pdf");
	      //System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
	      String [] names = metadata.names();
	      for (int i=0;i<names.length;i++) {
	    	  String name = names[i];
	    	  String val = metadata.get(name);
		      NameValue nv = new NameValue();
		      nv.setName(name);
		      nv.setValue(val);
		      metas.add(nv);
	      }
		}
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	    	if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
	    }
	    return metas;
	}
	public String getNumbersContent(File f) {
	    FileInputStream is = null;
	    String content = null;
	    try {
	      ContentHandler contenthandler = new BodyContentHandler();
	      Metadata metadata = new Metadata();
	      metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
	      IWorkPackageParser iWorkParser = new IWorkPackageParser();
	      ParseContext parseContext = new ParseContext();
	      parseContext.set(Parser.class, new AutoDetectParser());
	      is = new FileInputStream(f);
	      contenthandler = new BodyContentHandler(10000000);
	      metadata = new Metadata();
	      iWorkParser.parse(is, contenthandler, metadata, parseContext);
	      // application/vnd.apple.numbers
	      // application/x-netcdf
	      metadata.set(Metadata.CONTENT_TYPE, "application/x-netcdf");
	      System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
	      content = contenthandler.toString();
		}
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	    	if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
	    }
	    return content;
	}
	
	public static void main(String args[]) throws Exception {
	    //FileInputStream is = null;
	    try {
	      File f = null;
	      f = new File("C:/Documents and Settings/jalbersh.NREL_NT/My Documents/Downloads/HRDL_iop12_19991027024421.nc");
	      PDFFileParser np = new PDFFileParser(f);
	      String content = np.getContent(f);
          //System.out.println("metasize="+metadata.size());
          System.out.println("Home="+content.contains("Home"));
          System.out.println("-226="+content.contains("-226"));
          System.out.println("-137.5="+content.contains("-137.5"));
          System.out.println("Checking Account: 300545668="+content.contains("Checking Account: 300545668"));
          System.out.println("Credit Card="+content.contains("Credit Card"));
          //System.out.println("Groceries="+content.toLowerCase().contains("groceries"));
          System.out.println("Food="+content.contains("Food"));
          System.out.println("Try adding your own account transactions to this table.="+content.contains("Try adding your own account transactions to this table."));
	      //System.out.println("content: " + content);
		  //is.close();
		}
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	    	//if (is != null) is.close();
	    }
	  }
	
	/**
	 * Controller method for getting the tags and data.
	 * 
	 * @throws Exception
	 */
	private int parseData(String worksheet) throws Exception {
		int ret = -1;
		if (headers != null || data == null) {
			ret = getDataRows(worksheet);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	//@Override
	public List<RowDTO>  getData(String worksheet) throws Exception {
		if ((headers == null) || (headers.isEmpty()))
			getHeaders(worksheet);
		if ((data == null) || (data.isEmpty())) {
			retVal = parseData(worksheet);
			if (retVal < 0) return null;
		}
		return data;
	}

	/**
	 * Get the data tag names.
	 * 
	 * @return List<String>
	 */
	public List<String> getSheets() {
		List<String> hdrs = new ArrayList<String>();
		try {
			hdrs.add("main");
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} 
		return hdrs;
	}
	
	/**
	 * @return the wbConfig
	 */
	public WorkbookConfig getWbConfig() {
		return wbConfig;
	}

	/**
	 * @param wbConfig the wbConfig to set
	 */
	public void setWbConfig(WorkbookConfig wbConfig) {
		this.wbConfig = wbConfig;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @return the headers
	 */
	public List<CellDataHeader> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<CellDataHeader> headers) {
		this.headers = headers;
	}

	/**
	 * @return the data
	 */
	public List<RowDTO> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<RowDTO> data) {
		this.data = data;
	}

	/**
	 * @return the dataFile
	 */
	public File getDataFile() {
		return dataFile;
	}

	/**
	 * @param dataFile the dataFile to set
	 */
	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	/**
	 * @return the dELIMITER
	 */
	public String getDELIMITER() {
		return DELIMITER;
	}

	/**
	 * @param delimiter the dELIMITER to set
	 */
	public void setDELIMITER(String delimiter) {
		DELIMITER = delimiter;
	}
	
	public void setWsheet(String wsheet) {
		this.wsheet = wsheet;
	}
	
	public String getWsheet() {
		return wsheet;
	}
}
