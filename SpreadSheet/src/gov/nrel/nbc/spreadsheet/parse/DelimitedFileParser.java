
package gov.nrel.nbc.spreadsheet.parse;

import gov.nrel.nbc.security.crypto.DataEncryption;
import gov.nrel.nbc.security.dbUtils.DBUtils;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.servlet.ServletException;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * A class to parse the Opto output files.
 *
 * @author bberry
 */
public class DelimitedFileParser extends GenericFileParser {

    /** 
     * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(DelimitedFileParser.class);
    
	private static final String MM_DD_YYYY = "MM/dd/yyyy";

	private static final String STRING = "STRING";

	private static final String DATE = "DATE";

	private static final String REAL = "REAL";

	private static final String LONG = "LONG";

	private static final String BOOLEAN = "BOOLEAN";

    private String DELIMITER = "\t";

    //private static final String NEWLINE = "\n";
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

	/** 
     * A data file for parsing.
     */
    private File dataFile;
    
    public DelimitedFileParser(boolean handle, File file) throws Exception {
		this.handle_as_blanks = handle;
        if (file == null || ! file.exists()) {
        	log.severe("File was not found! File: " + file.getPath());
            throw new FileNotFoundException(file.getPath());
        }
        dataFile = file;
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

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet
	 *            The Worksheet we parse. 
	 *            
	 */
	public List<NameValue> getMetadataNames(String worksheet) {
        Scanner scanner = null;
		List<NameValue> metadatas = new ArrayList<NameValue>();
		try {
	        scanner = new Scanner(dataFile);
	        scanner.useDelimiter(DELIMITER);
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
				while (rowCtr< firstMDRow) { scanner.nextLine(); rowCtr++; }
				for (;rowCtr<=lastMDRow;rowCtr++) {
		        	String line = scanner.nextLine();
		        	Scanner lineScan = new Scanner(line);
		        	lineScan.useDelimiter(DELIMITER);
		        	int colCtr=0;
		        	while (lineScan.hasNext()) {
		        		// skip to first MD col
		        		if (colCtr < firstMDCol) {
		        			lineScan.next();
		        		} else {
			        		String value = lineScan.next();
							NameValue nv = new NameValue();
							nv.setOrder(colCtr);
							String name = value;
							nv.setName(name);
							metadatas.add(nv);
		        		}
			        	colCtr++;
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
		return metadatas;
	}

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet
	 *            The Worksheet we parse. 
	 */
	public List<NameValue> getMetadata(String worksheet) {
        Scanner scanner = null;
		List<NameValue> metadatas = new ArrayList<NameValue>();
		try {
	        scanner = new Scanner(dataFile);
	        scanner.useDelimiter(DELIMITER);
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
			while (rowCtr< firstMDRow) { scanner.nextLine(); rowCtr++; }
			for (;rowCtr<=lastMDRow;rowCtr++) {
				NameValue nv = new NameValue();
				nv.setOrder(rowCtr-firstMDRow);
	        	String line = scanner.nextLine();
	        	Scanner lineScan = new Scanner(line);
	        	lineScan.useDelimiter(DELIMITER);
	        	// skip to firstMDol
	        	int colCtr=0;
	        	while (lineScan.hasNext()&&colCtr<firstMDCol) { lineScan.next(); colCtr++; } 
				for (;colCtr<=lastMDCol;colCtr++) {
					if (colCtr == firstMDCol) {
						String name = lineScan.next();
						nv.setName(name);
					} else if (colCtr == lastMDCol) {
						String value = lineScan.next();
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
			try {
		        scanner.close();
			} catch (Exception ioe) {
				log.warning("Exception caught: "+ioe);
			}
		}
		return metadatas;
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
	        scanner = new Scanner(dataFile);
	        scanner.useDelimiter(DELIMITER);
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			chdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstCell = 0;
			short headerRow = 0;
			if (dbSheet != null) {
				firstCell = (short)(dbSheet.getHdr_col()-1);
				headerRow = (short) (dbSheet.getHdr_row()-1);
			}
			// skip to first header row
			int rowCtr=0;
			while (rowCtr< headerRow) { scanner.nextLine(); rowCtr++; }
        	String line = scanner.nextLine();
        	//log.warning("line="+line);
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
					CellDataHeader cellHeader = null;
					if (dbSheet != null)
						cellHeader = chdh.findByNameAndConfig(dbSheet, headerName);
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
	 * Get the data tag names.
	 * 
	 * @param worksheet String - The Worksheet we parse.
	 * @return List<String>
	 */
	public List<String> getHeaders(String worksheet) {
        Scanner scanner = null;
		List<String> hdrs = new ArrayList<String>();
		if (headers != null) headers.clear();
		else headers = new ArrayList<CellDataHeader>();
		try {
	        scanner = new Scanner(dataFile);
	        scanner.useDelimiter(DELIMITER);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			chdh.setSession(session);
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
	public int getDataRows(String worksheet) throws IOException,
			ServletException, Exception {
        //Scanner scanner = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(
				    new InputStreamReader(
				        new FileInputStream(dataFile), "UTF-8"));
			StringTokenizer st = null;
			int lineNumber = 0, tokenNumber = 0;
			//scanner = new Scanner(dataFile,"UTF-8");
	        //log.warning("scanner="+scanner.toString());
	        //scanner.useDelimiter(DELIMITER);
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
			//int lastCol = firstDataCol + headers.size();
			// skip to first data row
			int row = 0;
			String line="";
			if ( (br.readLine()) != null);
			//while (row < firstDataRow && scanner.hasNext()) { scanner.nextLine(); row++; }
			while ((line = br.readLine()) != null) {
			//for (; scanner.hasNext(); row++) {
				row++;
				RowDTO rowDto = new RowDTO();
				int usedHeaderCol = firstDataCol;
				lastCol = firstDataCol + headers.size();
	        	//byte[] bs = scanner.nextLine().getBytes("UTF-8");
				//byte[] bs = line.getBytes("UTF-8");
	        	//String line = new String(bs,"UTF-8");
	        	log.warning("line="+line);
				//ByteArrayInputStream bais = new ByteArrayInputStream(bs);
				//Scanner lineScan = new Scanner(bais);//line);
	        	//lineScan.useDelimiter(DELIMITER);
	        	int col=0;
	        	// skip to first data column
	        	st = new StringTokenizer(line, DELIMITER);
	        	//while (col < firstDataCol && lineScan.hasNext()) { lineScan.next(); col++; }
				//for (col = 0; col < lastCol && lineScan.hasNext(); col++) {
	        	while (col < firstDataCol && st.hasMoreTokens()) { st.nextToken(); col++; }
				for (col = 0; col < lastCol && st.hasMoreTokens(); col++) {
					CellDataDTO cellData = new CellDataDTO();
					boolean errorFlag = false;
					tokenNumber++;
					//byte[] bv = lineScan.next().getBytes("UTF-8");
					//CharBuffer cb = new CharsetDecoder()
					//String sValue = new String(bv,"UTF-8");
					String sValue = st.nextToken();
					log.warning("Line # " + lineNumber +
							", Token # " + tokenNumber
							+ ", Token : "+ sValue);
					log.warning("sValue="+sValue);
					if (sValue != null) sValue = sValue.trim();
					if ((sValue != null) && (!sValue.isEmpty())) {
						GenericValue gValue = new GenericValue();
						ValueData gd = new ValueData();
						CellDataHeader header = headers.get(usedHeaderCol-firstDataCol);
						if (header == null) {
							log.warning("header for col="+(usedHeaderCol-firstDataCol)+" was null");
						} else {
							String headerName = header.getName();
							//log.info("headerName="+headerName);
							String stype = header.getTypeId().getDescription();
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
										errorFlag = true;
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
										log.warning("Invalid numeric data: " + sValue);
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
									Date date = dFormat.parse(sValue);
									//String dvalue = dFormat.format(date);
					        		collectionTime = date;
					        	} catch (ClassCastException cce) {
					        		log.warning(cce.getMessage());
					        		//return AppConstants.MISMATCHED_TYPE;
					        		sValue = "";
					        	} catch (ParseException pe) {
					        		log.warning(pe.getMessage());
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
		        //scanner.close();
				br.close();
			} catch (Exception ioe) {
				log.warning("Exception caught: "+ioe);
			}
		}
		return 0;
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
		
}

