package gov.nrel.nbc.spreadsheet.parse;

import gov.nrel.nbc.security.dbUtils.DBUtils;
import gov.nrel.nbc.spreadsheet.client.AppConstants;
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
import gov.nrel.nbc.spreadsheet.server.SpreadSheetServiceImpl;
import gov.nrel.nbc.spreadsheet.server.SpreadSheetUploadServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * A class to parse the Calculation Sheet data files into <CellData>
 *  and <CellDataHeader> objects.
 * 
 * @author James Albersheim
 */
public class ExcelSParser extends GenericFileParser implements AppConstants {

	private static final String MM_DD_YYYY = "MM/dd/yyyy";

	private static final String LAST_COLUMN = "S % Total";

	private static final String STRING = "STRING";

	private static final String DATE = "DATE";

	private static final String REAL = "REAL";

	private static final String LONG = "LONG";

	private static final String BOOLEAN = "BOOLEAN";

	/**
	 * XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
	 */
	private static final Logger log = Logger.getLogger(ExcelSParser.class);

	private String errorStr;
	/**
	 * An Excel file for parsing.
	 */
	private File xlsFile;

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
	 * Get a CalcExcelParser for a given File.
	 * 
	 * @param xls - The Excel file.
	 * @throws Exception
	 */
	public ExcelSParser(boolean handle, File xls, WorkbookConfig config, Session session) throws Exception {
		this.handle_as_blanks = handle;
		if (xls != null && !xls.exists()) {
			log.error("File was not found! File: " + xls.getPath());
			throw new FileNotFoundException(xls.getPath());
		} else if (xls == null) {
			throw new Exception("Null xls filename given");
		}
		xlsFile = xls;
	    wbConfig = config;
		this.session = session;
	}

	/**
	 * Default constructor
	 */
	public ExcelSParser() {
	}

	/**
	 * Get the parsed Excel file as a Set of <CellData> objectst contained
	 *  in <RowDTO> objects.
	 * 
	 * @return A List<RowDTO> holding the parsed data rows.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RowDTO> getData(String worksheet) throws Exception {
		if ((headers == null) || (headers.isEmpty()))
			parseHeaders(worksheet);
		if ((data == null) || (data.isEmpty())) {
			retVal = parseData(worksheet);
			if (retVal < 0) return null;
		}
		return data;
	}

	/**
	 * Get the parsed Excel file as a List of <CellDataHeader> objects.
	 * 
	 * @return A <List<CellDataHeader>> holding the parsed data rows.
	 * @throws Exception
	 */
	public List<CellDataHeader> getTags(String worksheet) throws Exception {
		if ((headers == null) || (headers.isEmpty()))
			parseHeaders(worksheet);
		return headers;
	}

	/**
	 * Controller method for getting the tags and data.
	 * 
	 * @throws Exception
	 */
	private int parseData(String worksheet) throws Exception {
		int ret = -1;
		if (headers != null || data == null) {
			FileInputStream fis = new FileInputStream(xlsFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			ret = getDataRows(workbook,worksheet);
			fis.close();
		}
		return ret;
	}

	/**
	 * Get the data tag names.
	 * 
	 */
	public List<String> getSheets() {
		FileInputStream fis = null;
		List<String> sheets = new ArrayList<String>();
		try {
			fis = new FileInputStream(xlsFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			int numSheets = workbook.getNumberOfSheets();
			for (int i=0;i<numSheets;i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				boolean hidden = workbook.isSheetHidden(i);
				if (!hidden) {
					String name = sheet.getSheetName();
					sheets.add(name);
				}
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warn("IOException caught: "+ioe);
			}
		}
		return sheets;
	}

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet
	 *            The Worksheet we parse. 
	 */
	public List<NameValue> getMetadataNames(String worksheet) {
		FileInputStream fis = null;
		List<NameValue> metadatas = new ArrayList<NameValue>();
		try {
			fis = new FileInputStream(xlsFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheet(worksheet);
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			DataTypeDAOHibernate dtdh = new DataTypeDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			dtdh.setSession(session);
			sdh.setSession(session);
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstMDRow = (short)(dbSheet.getMeta_start_row()-1);
			short lastMDRow = (short)(dbSheet.getMeta_end_row()-1);
			short firstMDCol = (short)(dbSheet.getMeta_start_col()-1);
			if (firstMDRow > -1 && lastMDRow > 0) {
				for (int rowCtr=firstMDRow;rowCtr<=lastMDRow;rowCtr++) {
					HSSFRow row = sheet.getRow(rowCtr);
					NameValue nv = new NameValue();
					Cell cell = row.getCell(firstMDCol);
					nv.setOrder(rowCtr-firstMDRow);
					String name = cell.getStringCellValue();
					nv.setName(name);
					metadatas.add(nv);
				}
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warn("IOException caught: "+ioe);
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
		FileInputStream fis = null;
		List<NameValue> metadatas = new ArrayList<NameValue>();
		try {
			fis = new FileInputStream(xlsFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheet(worksheet);
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			DataTypeDAOHibernate dtdh = new DataTypeDAOHibernate();
			MetadataHeaderDAOHibernate mhdh = new MetadataHeaderDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			mhdh.setSession(session);
			dtdh.setSession(session);
			sdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstMDRow = (short)(dbSheet.getMeta_start_row()-1);
			short lastMDRow = (short)(dbSheet.getMeta_end_row()-1);
			short firstMDCol = (short)(dbSheet.getMeta_start_col()-1);
			short lastMDCol = (short)(dbSheet.getMeta_end_col()-1);
			for (int rowCtr=firstMDRow;rowCtr<=lastMDRow;rowCtr++) {
				HSSFRow row = sheet.getRow(rowCtr);
				NameValue nv = new NameValue();
				nv.setOrder(rowCtr-firstMDRow);
				for (int colCtr=firstMDCol;colCtr<=lastMDCol;colCtr++) {
					if (colCtr >= row.getLastCellNum()) return null;
					Cell cell = row.getCell(colCtr);
					if (colCtr == firstMDCol) {
						String name = cell.getStringCellValue();
						nv.setName(name);
					} else if (colCtr == lastMDCol) {
						MetadataHeader header = mhdh.findByName(nv.getName(),wbConfig);
						if (header != null) {
							DataType dt = header.getTypeId();
							int type = (int)dt.getId();
							switch(type) {
								case 1:  // Long
									try {
										long l = (long)cell.getNumericCellValue();
										nv.setValue(String.valueOf(l));
									} catch (IllegalStateException ise) {
										log.warn("problem getting long from text call");
										String s = cell.getStringCellValue();
										nv.setValue(String.valueOf(s));
									}
									break;
								case 2:  // Real
									try {
										double d = cell.getNumericCellValue();
										nv.setValue(String.valueOf(d));
									} catch (IllegalStateException ise) {
										log.warn("problem getting real from text call");
										String s = cell.getStringCellValue();
										nv.setValue(String.valueOf(s));
									}
									break;
								case 3: // Date
									try {
										Date d1 = cell.getDateCellValue();
										DataFormat format = header.getData_format();
										String sformat = MM_DD_YYYY;
										if (format != null) {
												sformat = format.getFormat();
										}
										SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
										String value = dFormat.format(d1);
										nv.setValue(value);
									} catch (IllegalStateException ise) {
										log.warn("problem getting date from text call");
										String s = cell.getStringCellValue();
										nv.setValue(String.valueOf(s));
									}
									break;
								case 4: // String
									String s = cell.getStringCellValue();
									nv.setValue(String.valueOf(s));
									break;
								case 5: // Boolean
									try {
										boolean b = cell.getBooleanCellValue();
										nv.setValue(String.valueOf(b));
									} catch (IllegalStateException ise) {
										log.warn("problem getting boolean from text call");
										String s1 = cell.getStringCellValue();
										nv.setValue(String.valueOf(s1));
									}
									break;
							}
						}
					} 
				}
				metadatas.add(nv);
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warn("IOException caught: "+ioe);
			}
		}
		return metadatas;
	}
	
	private String change2FullMonthName(String date) {
		String retDate = date;
		String mon = date.substring(0,3);
		if (mon.equals("Jan")) retDate = "January" + date.substring(3);
		if (mon.equals("Feb")) retDate = "Feburary" + date.substring(3);
		if (mon.equals("Mar")) retDate = "March" + date.substring(3);
		if (mon.equals("Apr")) retDate = "Arpil" + date.substring(3);
		if (mon.equals("May")) retDate = date;
		if (mon.equals("Jun")) retDate = "June" + date.substring(3);
		if (mon.equals("Jul")) retDate = "July" + date.substring(3);
		if (mon.equals("Aug")) retDate = "August" + date.substring(3);
		if (mon.equals("Sep")) retDate = "September" + date.substring(3);
		if (mon.equals("Oct")) retDate = "October" + date.substring(3);
		if (mon.equals("Nov")) retDate = "November" + date.substring(3);
		if (mon.equals("Dec")) retDate = "December" + date.substring(3);
		return retDate;
	}

	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet
	 *            The Worksheet we parse. 
	 */
	public List<String> getHeaders(String worksheet) {
		FileInputStream fis = null;
		List<String> hdrs = new ArrayList<String>();
		try {
			fis = new FileInputStream(xlsFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheet(worksheet);
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			sdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstRow = (short)(dbSheet.getHdr_row()-1);
			short firstCol = (short)(dbSheet.getHdr_col()-1);
			HSSFRow row = sheet.getRow(firstRow);
			Iterator<Cell> cit = row.cellIterator();
			int colCtr = 0;
			while (cit.hasNext()) {
				Cell cell = cit.next();
				if (colCtr >= firstCol) {
					boolean hidden = sheet.isColumnHidden(colCtr);
					String headerName = "";
					if (cell != null) {
						int ctype = cell.getCellType();
						switch (ctype) {
							case Cell.CELL_TYPE_NUMERIC: 
								try {
									Date collectionTime = cell.getDateCellValue();
									GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
									cal.setTime(collectionTime);
									Date dt = cal.getTime(); 
									headerName = DateFormat.getDateInstance().format(dt); 
									headerName = change2FullMonthName(headerName);
								} catch (IllegalStateException e1) {
									headerName = String.valueOf(cell.getNumericCellValue());
								}
								break; // assume date
							case Cell.CELL_TYPE_STRING: headerName = cell.getStringCellValue(); break;
							case Cell.CELL_TYPE_FORMULA: headerName = cell.getCellFormula(); break;
							case Cell.CELL_TYPE_BLANK: break;
							case Cell.CELL_TYPE_ERROR: break;
							case Cell.CELL_TYPE_BOOLEAN: break;
							default: log.info("some other type");
						}
					}
					headerName = headerName.trim();
					if (!hidden) {
						headerName = headerName.replace("\"","");
						//headerName = headerName.replace(",", "_");
						hdrs.add(headerName);
					}
				}
				colCtr++;
			}
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warn("IOException caught: "+ioe);
			}
		}
		return hdrs;
	}

	/**
	 * Controller method for getting the tags.
	 * 
	 * @throws Exception
	 */
	private void parseHeaders(String worksheet) throws Exception {
		if (headers == null) {
			FileInputStream fis = new FileInputStream(xlsFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			getHeaders(workbook,worksheet);
			fis.close();
		}
	}

	private static final String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	/**
	 * Get the data tag names.
	 * 
	 * @param workbook
	 *            The Workbook we parse. This is not closed.
	 */
	private void getHeaders(HSSFWorkbook workbook, String worksheet) {
		headers = new ArrayList<CellDataHeader>();
		try {
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			chdh.setSession(session);
			SheetDAOHibernate sdh = new SheetDAOHibernate();
			sdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			headers.addAll(chdh.findByConfig(dbSheet));
		} catch (HibernateException he) {
			log.error("Hibernate exception on getting type. error: " + he);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warn(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.error("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.error("Exception on getting type. error: " + e);
			String stack = SpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warn(stack);
		} 
	}

	/**
	 * Public method to convert an Excel column to an integer.
	 * 
	 * @param excelIndex String Excel column letter
	 * 
	 * @return int converted column index
	 */
	public int convertExcelColumnToIndex(String excelIndex) {
		int ALPHABET_LENGTH = 26;
		HashMap<String, Integer> alphaMap = new HashMap<String, Integer>();
		for (int i = 1; i <= ALPHABET_LENGTH; i++) {
			alphaMap.put(alpha[i-1], new Integer(i));
		}
		int excelIntIndex = -1;
		excelIndex = excelIndex.toUpperCase();
		if (excelIndex != null && !excelIndex.isEmpty()) {
			if (excelIndex.length() == 1) {
				String digit = excelIndex.substring(0, 1);
				try {
					excelIntIndex = Integer.parseInt(digit)-1;
				} catch (NumberFormatException nfe) {
					excelIntIndex = alphaMap.get(digit);
				}
			} else if (excelIndex.length() == 2) {
				String digit = excelIndex.substring(1, 2);
				excelIntIndex = alphaMap.get(digit);
				digit = excelIndex.substring(0, 1);
				try {
					excelIntIndex = Integer.parseInt(digit)-1;
				} catch (NumberFormatException nfe) {
					excelIntIndex += ALPHABET_LENGTH * alphaMap.get(digit);
				}
			} else if (excelIndex.length() == 3) {
				String digit = excelIndex.substring(2, 3);
				excelIntIndex = alphaMap.get(digit);
				digit = excelIndex.substring(1, 2);
				excelIntIndex += ALPHABET_LENGTH * alphaMap.get(digit);
				digit = excelIndex.substring(0, 1);
				try {
					excelIntIndex = Integer.parseInt(digit)-1;
				} catch (NumberFormatException nfe) {
					excelIntIndex += Math.pow(ALPHABET_LENGTH, 2) * alphaMap.get(digit);
				}
			}
		}
		return excelIntIndex;
	}
	
	/**
	 * Get the data from the Summary worksheet.
	 * 
	 * @param workbook
	 *            The Workbook we parse.
	 * @throws ServletException 
	 */
	private int getDataRows(HSSFWorkbook workbook, String worksheet) throws IOException,
			ServletException {
		HSSFSheet sheet = workbook.getSheet(worksheet);
		HSSFEvaluationWorkbook ewb = HSSFEvaluationWorkbook.create(workbook);
		int sheetIndex = ewb.getSheetIndex(worksheet);
		EvaluationSheet esheet = ewb.getSheet(sheetIndex);
		if (sheet == null) {
			log.error("Unable to get worksheet="+worksheet);
			return AppConstants.MISSING_WORKSHEET;
		}
		data = new ArrayList<RowDTO>();

		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
		if (dbSheet.getData_row()<1) throw new IOException("data row in DB set to invalid number ("+dbSheet.getData_row() + ")");
		int rowCtr = (int)dbSheet.getData_row()-1;
		int lastRow = sheet.getLastRowNum();
		log.info("lastRow="+lastRow);
		
		boolean partialIngest = false;
		boolean rowBreakOut = false;
		while (rowCtr <= lastRow && !rowBreakOut) { 
			Row row = sheet.getRow(rowCtr);//rows.next();
			rowCtr++;
			RowDTO rowDto = new RowDTO();
			boolean breakOut = false;
			short firstCell = (short)(dbSheet.getData_col()-1);//0;//row.getFirstCellNum();
			short lastCell = (short)(firstCell + headers.size());//LAST_COLUMN_NUM;//row.getLastCellNum();
			int usedHeaderCol = firstCell;
			boolean notNull = false;
			short cn = row.getLastCellNum();
			/*
			if (cn<lastCell) {
				breakOut = true;
				rowBreakOut = true;
			}
			*/
			for (int col=firstCell; col<lastCell && !breakOut; col++) {
				Cell cell;
				boolean hidden = sheet.isColumnHidden(col);
				if (!hidden) {
					System.out.println("getting new cell for col="+col + "; header="+headers.get(usedHeaderCol-firstCell).getName());
					cell = row.getCell(col,Row.RETURN_BLANK_AS_NULL);
					EvaluationCell ecell = esheet.getCell(rowCtr, col);
					int ctype = 0;
					try {
						if (cell != null) {
							notNull = true;
							ctype = cell.getCellType();
						} else {
							log.info("cell is null");
						}
						//String value = cell.getStringCellValue();
						//log.info("value=."+value+".");
					} catch (Exception e2) {
						log.warn(SpreadSheetServiceImpl.getStackTrace(e2));
						//log.info("caught exception while trying to get string value");
					}
					CellDataDTO cellData = new CellDataDTO();
					boolean errorFlag = false;
					String sValue = "";
					GenericValue gValue = new GenericValue();
					ValueData gd = new ValueData();
					CellDataHeader header = headers.get(usedHeaderCol-firstCell);
					if (cell != null) {
						notNull = true;
						if (header == null) {
							log.warn("header for col="+(usedHeaderCol-firstCell)+" was null");
						} else {
							 String type = header.getTypeId().getDescription();
							 System.out.println("type="+ctype+" for col="+col+" and header="+header.getName());
							 int itype = 0;
							 if (type.equals(LONG) || type.equals(REAL)) itype = Cell.CELL_TYPE_NUMERIC;
							 else if (type.equals(STRING)) itype = Cell.CELL_TYPE_STRING;
							 else if (type.equals(BOOLEAN)) itype = Cell.CELL_TYPE_BOOLEAN;
							 else if (type.equals(DATE)) itype = Cell.CELL_TYPE_NUMERIC;
							 if (ctype == Cell.CELL_TYPE_BLANK) errorFlag = true;
							 else if ((ctype == Cell.CELL_TYPE_NUMERIC || ctype == Cell.CELL_TYPE_FORMULA) && type.equals(DATE)) {
								try {
									boolean dateFormatted = DateUtil.isCellDateFormatted(cell);
									if (dateFormatted) {
										Date collectionTime = cell.getDateCellValue();
										GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
										cal.setTime(collectionTime);
										gValue.setDateValue(cal.getTime());
										cellData.setType(DATE);
										gd.setDvalue(gValue.getDateValue());
									} else {
										sValue = cell.getStringCellValue();
										sValue = sValue.trim();
										log.warn("not date value: "+sValue);
										gValue.setStringValue(sValue);
										cellData.setType(STRING);
										gd.setSvalue(gValue.getStringValue());
									}
								} catch (Exception e1) {
									if (!this.handle_as_blanks) {
										errorStr = "-Parsing error for date at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
										log.warn(errorStr);
										sValue = cell.getStringCellValue();
										sValue = sValue.trim();
										errorFlag = true;
									}
								}
							} else if ((ctype == Cell.CELL_TYPE_NUMERIC || ctype == Cell.CELL_TYPE_FORMULA) && (type.equals(LONG) || type.equals(REAL))) {
								double numeric = -1.0;
								try {
									//log.info("in NUMERIC");
									numeric = cell.getNumericCellValue();
									log.warn("header: "+header.getName()+"; col: "+col+" got value from cell = "+numeric);
									String typeStr = "";
									if (header.getTypeId() != null)
										typeStr = headers.get(usedHeaderCol-firstCell).getTypeId().getDescription();
									if (type.equals(REAL)||typeStr.equals(REAL)) {
										try {
											gValue.setRealValue(numeric);
											cellData.setType(REAL);
											gd.setRvalue(gValue.getRealValue());
											//log.info("REAL: Col: "+col+" Cell type: " + type + "; Tag: " + headers.get(usedHeaderCol-firstCell).getName()+"; value="+numeric);
										} catch (NumberFormatException nfe) {
											errorStr = "-Parsing error for real at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
											log.warn("Invalid data: " + numeric+errorStr);
											errorFlag = true;
										}
									} else if (type.equals(LONG)||typeStr.equals(LONG)) {
										try {
											gValue.setLongValue((long)numeric);
											cellData.setType(LONG);
											gd.setLvalue(gValue.getLongValue());
											//log.info("LONG: Cell type: " + type + "; Tag: " + headers.get(col).getName()+"; value="+numeric);
										} catch (NumberFormatException nfe) {
											errorStr = "-Parsing error for long at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
											log.warn("Invalid data: " + sValue+errorStr);
											errorFlag = true;
										}
									} 
								} catch (NumberFormatException nfe) {
									System.out.println("numberFormatException encountered for header="+header.getName());
									errorStr = "-Parsing error for numeric value at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
									//log.warn(SpreadSheetServiceImpl.getStackTrace(nfe));
									//log.info("type="+type+" for col="+col+" and header="+header.getName());
									if (sValue != null) sValue = sValue.trim();
									log.warn("sValue="+sValue);
									if (sValue != null && !sValue.isEmpty()) {
										log.warn("not number value: "+sValue);
										log.warn("setting default value="+sValue);
										gValue.setStringValue(sValue);
										cellData.setType(STRING);
										gd.setSvalue(gValue.getStringValue());
									} else {
										//log.warn("errorFlag set to true");
										errorFlag = true;
									}
								} catch (IllegalStateException ise) {
									//log.warn("illegal state: "+ise);
									log.warn("treating "+header.getName()+" as a number, and type="+ctype+" for col="+(usedHeaderCol-firstCell)+" and row="+rowCtr);
									if (ise.getMessage().endsWith("Cannot get a numeric value from a text cell")) {
										sValue="";
										try {
											if (ctype != Cell.CELL_TYPE_ERROR) {
												sValue = cell.getStringCellValue();
											}
										} catch (Exception e15) {
											if (!this.handle_as_blanks) {
												errorStr = "-Parsing error for real at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
												log.warn("Exception caught: "+e15.getMessage()+errorStr);
												partialIngest = true;
												errorFlag = true;
												breakOut = true;
											}
										}
										sValue = sValue.trim();
										try {
											numeric = Double.parseDouble(sValue);
										} catch (NumberFormatException nfe) {
											if (!this.handle_as_blanks && sValue != null && !sValue.isEmpty()) {
												errorStr = "-Parsing error for numeric at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
												log.warn("failed to convert to double = "+sValue);
												partialIngest = true;
												errorFlag = true;
												breakOut = true;
											} else {
											}
										}
									} else {
										errorFlag = true;
									}
								}
							} else if ((ctype == Cell.CELL_TYPE_BOOLEAN || ctype == Cell.CELL_TYPE_FORMULA) && type.equals(BOOLEAN)) {
								boolean bValue = false;
								try {
									sValue = String.valueOf(cell.getBooleanCellValue());
									//if (sValue != null) notNull = true;
								} catch (Exception eb) {
									errorStr = "-Parsing error for boolean at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
									//errorFlag = true;
								}
								bValue = Boolean.parseBoolean(sValue);
								gValue.setBooleanValue(bValue);
								cellData.setType(BOOLEAN);
								gd.setBvalue(gValue.getBooleanValue());
							} else if ((ctype == Cell.CELL_TYPE_BLANK || ctype == Cell.CELL_TYPE_STRING || ctype == Cell.CELL_TYPE_FORMULA) && type.equals(STRING)) { // STRING or BLANK - treat as String
								try {
									sValue = cell.getStringCellValue();
									log.warn("sValue="+sValue);
									if (col == 0 && (sValue == null || sValue.isEmpty())) {
										breakOut = true;
									} else {
										gValue.setStringValue(sValue);
										cellData.setType(STRING);
										gd.setSvalue(gValue.getStringValue());
									}
								} catch (IllegalStateException ise) {
									errorStr = "-Parsing error for text at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr;
									log.warn("treating "+header.getName()+" as text, and type="+ctype+" for col="+(usedHeaderCol-firstCell)+" and row="+rowCtr);
									log.warn("illegal state: "+ise);
									if (ise.getMessage().endsWith("Cannot get a text value from a numeric cell")) {
										double numeric = cell.getNumericCellValue();
										sValue = String.valueOf(numeric);
										//if (sValue != null) notNull = true;
										if (col == 0 && (sValue == null || sValue.isEmpty())) {
											breakOut = true;
										} else {
											gValue.setStringValue(sValue);
											cellData.setType(STRING);
											gd.setSvalue(gValue.getStringValue());
										}
									} else
										errorFlag = true;								
								}
							} else if (ctype != itype) {
								if (!this.handle_as_blanks) {
									String ctypestr="";
									switch (ctype) {
									case Cell.CELL_TYPE_NUMERIC: ctypestr="Numeric or Date";break; // assume date
									case Cell.CELL_TYPE_STRING: ctypestr="String";break;
									case Cell.CELL_TYPE_FORMULA: ctypestr="Formula";break;
									case Cell.CELL_TYPE_BLANK: break;
									case Cell.CELL_TYPE_ERROR: break;
									case Cell.CELL_TYPE_BOOLEAN: ctypestr="Boolean";break;
									default: ctypestr="Numeric or Date";break;
									}
									String itypestr="";
									if (itype == Cell.CELL_TYPE_NUMERIC) itypestr="Numeric or Date";
									else if (itype == Cell.CELL_TYPE_STRING) itypestr = "String";
									else if (itype == Cell.CELL_TYPE_BOOLEAN) itypestr = "Boolean";
									else itypestr="Numeric or Date";
									errorStr = "Type mismatch at col="+(usedHeaderCol-firstCell)+" and row="+rowCtr+".\nExpecting "+itypestr+" but found "+ctypestr;
									partialIngest = true;
									errorFlag = true;
									breakOut = true;
								}
							}
							cellData.setValue(gd);
							cellData.setEmpty(false);
						}
					} else { //if (col != 0) {
						log.info("cell is null/blank");
						// treat as blank cell
						String typeStr = "";
						cellData.setEmpty(true);
						if (header.getTypeId() != null)
							typeStr = headers.get(usedHeaderCol-firstCell).getTypeId().getDescription();
						if (typeStr.equals(REAL)) {
							gd.setRvalue(DEFAULT_REAL);
							cellData.setType(REAL);
						} else if (typeStr.equals(LONG)) {
							gd.setLvalue(DEFAULT_LONG);
							cellData.setType(LONG);
						} else if (typeStr.equals(BOOLEAN)) {
							gd.setBvalue(false);
							cellData.setType(BOOLEAN);
						} else if (typeStr.equals(DATE)) {
							gd.setDvalue(null);
							cellData.setType(DATE);
						} else {
							sValue = "";
							gd.setSvalue("");
							cellData.setType(STRING);
						}
						cellData.setValue(gd);
					} 
					if ((errorFlag != true)) {
						if (gd != null) {
							if (col > lastCell-2 && !notNull) {
								breakOut = true;
							} else {
								cellData.setValue(gd);
								rowDto.setSheetRow(rowCtr);
								cellData.setTag(headers.get(usedHeaderCol-firstCell).getName());
								rowDto.addCell(cellData);
							}
						}
					}
					//if (col == 0 && (sValue == null || sValue.isEmpty())) {
					//	breakOut = true;
					//} 
					if ((headers.get(usedHeaderCol-firstCell).getName().equals(LAST_COLUMN))) {
						breakOut = true;
					}
					usedHeaderCol++;
				} else {
					// hidden is true here
					lastCell++;
				}
			}
			log.info("got "+rowCtr+" rows");
			if ((rowDto.getRow() != null) && (!rowDto.getRow().isEmpty())) {
				boolean found = false;
				List<CellDataDTO> cells = rowDto.getRow();
				int ctr = -1;
				int emptyCounter = 0;
				Iterator<CellDataDTO> cit = cells.iterator();
				while (cit.hasNext() && !found) {
					ctr++;
					CellDataDTO cell = cit.next();
					if(cell.isEmpty())
						emptyCounter++;
					else {
						//found = !cell.isEmpty();
						ValueData gd = cell.getValue();
						String typeStr = cell.getType();
						if (typeStr == null)
							typeStr = headers.get(ctr).getTypeId().getDescription();
						if (typeStr.equals(REAL)) {
							if (gd.getRvalue() != 0.0)
								found = true;
						} else if (typeStr.equals(LONG)) {
							if (gd.getLvalue() != 0)
								found = true;
						} else if (typeStr.equals(BOOLEAN)) {
							if (gd.getBvalue() != false)
								found = true;
						} else if (typeStr.equals(DATE)) {
							if (gd.getDvalue() != null)
								found = true;
						} else {
							if (gd.getSvalue() != null && !gd.getSvalue().equals(""))
								found = true;
						}
					}
				}
				if (found && emptyCounter < cn)// && ctr > headers.size()-1)
					data.add(rowDto);
				else
					rowBreakOut = true;
			}
		}
		if (partialIngest)
			return -9;
		return 0;
	}

	public String getErrorStr() {
		return errorStr;
	}

	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}
}
