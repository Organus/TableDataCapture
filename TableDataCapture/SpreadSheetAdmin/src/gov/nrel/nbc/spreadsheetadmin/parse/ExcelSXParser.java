package gov.nrel.nbc.spreadsheetadmin.parse;

import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.client.NameValue;
import gov.nrel.nbc.spreadsheetadmin.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.DataTypeDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.SheetDataDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dto.CellDataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.DataFormat;
import gov.nrel.nbc.spreadsheetadmin.dto.DataType;
import gov.nrel.nbc.spreadsheetadmin.dto.GenericValue;
import gov.nrel.nbc.spreadsheetadmin.dto.MetadataHeader;
import gov.nrel.nbc.spreadsheetadmin.dto.SheetConfig;
import gov.nrel.nbc.spreadsheetadmin.dto.ValueData;
import gov.nrel.nbc.spreadsheetadmin.dto.WorkbookConfig;
import gov.nrel.nbc.spreadsheetadmin.server.SampleSpreadSheetUploadServiceImpl;
import gov.nrel.nbc.spreadsheetadmin.utilities.XLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * A class to parse the Calculation Sheet data files into <CellData>
 *  and <CellDataHeader> objects.
 * 
 * @author James Albersheim
 */
public class ExcelSXParser extends GenericFileParser implements AppConstants {

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
	private static final XLogger log = new XLogger(XLogger.INFO);

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
	public ExcelSXParser(File xls, WorkbookConfig config, Session session) throws Exception {
		if (xls != null && !xls.exists()) {
			log.severe("File was not found! File: " + xls.getPath());
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
	public ExcelSXParser() {
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
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
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
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			int numSheets = workbook.getNumberOfSheets();
			for (int i=0;i<numSheets;i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				boolean hidden = workbook.isSheetHidden(i);
				if (!hidden) {
					String name = sheet.getSheetName();
					sheets.add(name);
				}
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warning("IOException caught: "+ioe);
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
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(worksheet);
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
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
					XSSFRow row = sheet.getRow(rowCtr);
					NameValue nv = new NameValue();
					Cell cell = row.getCell(firstMDCol);
					nv.setOrder(rowCtr-firstMDRow);
					String name = cell.getStringCellValue();
					nv.setName(name);
					metadatas.add(nv);
				}
			}
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warning("IOException caught: "+ioe);
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
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(worksheet);
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
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
				XSSFRow row = sheet.getRow(rowCtr);
				NameValue nv = new NameValue();
				nv.setOrder(rowCtr-firstMDRow);
				for (int colCtr=firstMDCol;colCtr<=lastMDCol;colCtr++) {
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
										log.warning("problem getting long from text call");
										String s = cell.getStringCellValue();
										nv.setValue(String.valueOf(s));
									}
									break;
								case 2:  // Real
									try {
										double d = cell.getNumericCellValue();
										nv.setValue(String.valueOf(d));
									} catch (IllegalStateException ise) {
										log.warning("problem getting real from text call");
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
										log.warning("problem getting date from text call");
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
										log.warning("problem getting boolean from text call");
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
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warning("IOException caught: "+ioe);
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
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(worksheet);
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			sdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			short firstRow = (short)(dbSheet.getHdr_row()-1);
			short firstCol = (short)(dbSheet.getHdr_col()-1);
			XSSFRow row = sheet.getRow(firstRow);
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
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} finally {
			try {
				fis.close();
			} catch (IOException ioe) {
				log.warning("IOException caught: "+ioe);
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
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
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
	private void getHeaders(XSSFWorkbook workbook, String worksheet) {
		headers = new ArrayList<CellDataHeader>();
		try {
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			chdh.setSession(session);
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			sdh.setSession(session);
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			headers.addAll(chdh.findByConfig(dbSheet));
		} catch (HibernateException he) {
			log.severe("Hibernate exception on getting type. error: " + he);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(he);
			log.warning(stack);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
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
	
	/*
	private boolean isAlpha(char c) {
		boolean ret = false;
		for (int i=0;i<alpha.length;i++) {
			if (alpha[i].charAt(0)==c) {
				return true;
			}
		}
		return ret;
	}

	private boolean isNum(char c) {
		boolean ret = false;
		try {
			Integer.parseInt(String.valueOf(c));
			ret = true;
		} catch (NumberFormatException nfe) {}
		return ret;
	}

	private String getFormula(XSSFWorkbook workbook, Cell cell, int ctr)
	{
		String formula = cell.getCellFormula();
		try {
			log.info("formula="+formula);
			int sheetNum = workbook.getNumberOfSheets();
			String sheetName = workbook.getSheetName(ctr);
			
			while (ctr<sheetNum && !formula.contains(sheetName)) {
				sheetName = workbook.getSheetName(ctr++);
			}
			if (ctr < sheetNum) {
				XSSFSheet sheet1 = workbook.getSheet(sheetName);
				int rowCtr1 = 1;
				int index = formula.indexOf(sheetName)+sheetName.length()+2;
				String col1 = "";
				int i = 0;
				while (!isAlpha(formula.charAt(index+i))) i++;
				col1 = formula.substring(index+i);
				i = 0;
				while (isAlpha(col1.charAt(i))) i++;
				int part = i;
				while (isNum(col1.charAt(i))) i++;
				col1 = col1.substring(0,i);
				String col2 = "";
				String row2 = "";
				col2 = col1.substring(0,part);
				row2 = col1.substring(part);
				try {
					rowCtr1 = Integer.parseInt(row2);
				} catch (NumberFormatException nfe) {}
				int col2i = convertExcelColumnToIndex(col2);
				Row row1 = sheet1.getRow(rowCtr1);//rows.next();
				rowCtr1++;
				if (col2i > -1) {
					Cell cell1 = row1.getCell(col2i);
					int ctype = 0;
					ctype = cell1.getCellType();
					if (ctype == Cell.CELL_TYPE_FORMULA) {
						String formula1 = cell1.getCellFormula();
						log.info("new formula="+formula1);
						formula = getFormula(workbook,cell1,ctr);
					} else {
						int start = formula.indexOf(sheetName);
						formula = formula.substring(0,start) + "TEXT" + formula.substring(start+sheetName.length()+1);
					}
				}
			}		
		} catch (Exception e) {
			log.warning(SpreadSheetServiceImpl.getStackTrace(e));
		}
		return formula;
	}
	*/
	/**
	 * Get the data from the Summary worksheet.
	 * 
	 * @param workbook
	 *            The Workbook we parse.
	 * @throws ServletException 
	 */
	private int getDataRows(XSSFWorkbook workbook, String worksheet) throws IOException,
			ServletException {
		XSSFSheet sheet = workbook.getSheet(worksheet);
		if (sheet == null) {
			log.severe("Unable to get worksheet="+worksheet);
			return AppConstants.MISSING_WORKSHEET;
		}
		data = new ArrayList<RowDTO>();

		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
		if (dbSheet.getData_row()<1) throw new IOException("data row in DB set to invalid number ("+dbSheet.getData_row() + ")");
		int rowCtr = (int)dbSheet.getData_row()-1;
		int lastRow = sheet.getLastRowNum();
		
		boolean rowBreakOut = false;
		while (rowCtr <= lastRow && !rowBreakOut) { 
			Row row = sheet.getRow(rowCtr);//rows.next();
			rowCtr++;
			RowDTO rowDto = new RowDTO();
			boolean breakOut = false;
			short firstCell = (short)(dbSheet.getData_col()-1);//0;//row.getFirstCellNum();
			short lastCell = (short)(firstCell + headers.size());//LAST_COLUMN_NUM;//row.getLastCellNum();
			int usedHeaderCol = firstCell;
			for (int col=firstCell; col<lastCell && !breakOut; col++) {
				Cell cell = null;
				boolean hidden = sheet.isColumnHidden(col);
				if (!hidden) {
					log.info("getting new cell for col="+col + "; header="+headers.get(usedHeaderCol-firstCell).getName());
					cell = row.getCell(col);
					int ctype = 0;
					try {
						if (cell != null) {
							ctype = cell.getCellType();
							switch (ctype) {
								case Cell.CELL_TYPE_NUMERIC: log.info(headers.get(usedHeaderCol-firstCell).getName()+" is numeric");break;
								case Cell.CELL_TYPE_STRING: log.info(headers.get(usedHeaderCol-firstCell).getName()+" is string");break;
								case Cell.CELL_TYPE_FORMULA: 
									log.info(headers.get(usedHeaderCol-firstCell).getName()+" is formula");
									if (rowCtr == (int)dbSheet.getData_row()) {
										String formula = cell.getCellFormula();
										log.info("formula="+formula);
									}
									break;
								case Cell.CELL_TYPE_BLANK: log.info(headers.get(usedHeaderCol-firstCell).getName()+" is blank");break;
								case Cell.CELL_TYPE_ERROR: log.info(headers.get(usedHeaderCol-firstCell).getName()+" is error");break;
								case Cell.CELL_TYPE_BOOLEAN: log.info(headers.get(usedHeaderCol-firstCell).getName()+" is boolean");break;
								default: log.info("some other type");
							}
						}
						//String value = cell.getStringCellValue();
						//log.info("value=."+value+".");
					} catch (Exception e2) {
						log.warning(SampleSpreadSheetUploadServiceImpl.getStackTrace(e2));
						//log.info("caught exception while trying to get string value");
					}
					CellDataDTO cellData = new CellDataDTO();
					boolean errorFlag = false;
					String sValue = "";
					GenericValue gValue = new GenericValue();
					ValueData gd = new ValueData();
					CellDataHeader header = headers.get(usedHeaderCol-firstCell);
					if (cell != null) {
						if (header == null) {
							log.warning("header for col="+(usedHeaderCol-firstCell)+" was null");
						} else {
							 String type = header.getTypeId().getDescription();
							 //log.info("type="+type+" for col="+col+" and header="+header.getName());
							 if (type.equals(DATE)) {
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
										log.warning("not date value: "+sValue);
										gValue.setStringValue(sValue);
										cellData.setType(STRING);
										gd.setSvalue(gValue.getStringValue());
									}
								} catch (Exception e1) {
									log.warning("exception caught for date:"+e1+" for col="+(usedHeaderCol-firstCell)+" and row="+rowCtr);
									sValue = cell.getStringCellValue();
									sValue = sValue.trim();
									errorFlag = true;
								}
							} else if (type.equals(LONG) || type.equals(REAL)) {
								if (ctype == Cell.CELL_TYPE_BLANK) errorFlag = true;
								else {
									double numeric = -1.0;
									try {
										//log.info("in NUMERIC");
										// try to get numeric value - will fail for string
										numeric = cell.getNumericCellValue();
										log.info("header: "+header.getName()+"; col: "+col+" got value from cell = "+numeric);
										String typeStr = "";
										if (header.getTypeId() != null)
											typeStr = headers.get(usedHeaderCol-firstCell).getTypeId().getDescription();
										if (typeStr.equals(REAL)) {
											try {
												gValue.setRealValue(numeric);
												cellData.setType(REAL);
												gd.setRvalue(gValue.getRealValue());
												log.info("REAL: Col: "+col+" Cell type: " + type
															+ "; Tag: " + headers.get(usedHeaderCol-firstCell).getName()+"; value="+numeric);
											} catch (NumberFormatException nfe) {
												log.warning("Invalid data: " + numeric);
												errorFlag = true;
											}
										} else if (typeStr.equals(LONG)) {
											try {
												gValue.setLongValue((int)numeric);
												cellData.setType(LONG);
												gd.setLvalue(gValue.getLongValue());
												//log.info("LONG: Cell type: " + type
												//		+ "; Tag: " + headers.get(col).getName()+"; value="+numeric);
											} catch (NumberFormatException nfe) {
												log.warning("Invalid data: " + sValue);
												errorFlag = true;
											}
										} 
									} catch (NumberFormatException nfe) {
										log.warning("numberFormatException encountered for header="+header.getName());
										//log.warning(SpreadSheetServiceImpl.getStackTrace(nfe));
										log.info("type="+type+" for col="+col+" and header="+header.getName());
										try {
											sValue = cell.getStringCellValue();
										} catch (IllegalStateException ise) {
											log.warning(ise.getMessage());
											sValue = "";
											//return AppConstants.MISMATCHED_TYPE;
										}
										if (sValue != null) sValue = sValue.trim();
										log.warning("sValue="+sValue);
										if (sValue != null && !sValue.isEmpty()) {
											log.warning("not number value: "+sValue);
											log.warning("setting default value="+sValue);
											gValue.setStringValue(sValue);
											cellData.setType(STRING);
											gd.setSvalue(gValue.getStringValue());
										} else {
											log.warning("errorFlag set to true");
											errorFlag = true;
										}
									} catch (IllegalStateException ise) {
										log.warning("illegal state: "+ise);
										log.warning("treating "+header.getName()+" as a number, and type="+type+" for col="+(usedHeaderCol-firstCell)+" and row="+rowCtr);
										if (ise.getMessage().endsWith("Cannot get a numeric value from a text cell")) {
											sValue = cell.getStringCellValue();
											sValue = sValue.trim();
											try {
												numeric = Double.parseDouble(sValue);
											} catch (NumberFormatException nfe) {
												log.warning("failed to convert to double = "+sValue);
											}
											if (type.equals(REAL)) {
												try {
													gValue.setRealValue(numeric);
													cellData.setType(REAL);
													gd.setRvalue(gValue.getRealValue());
												} catch (NumberFormatException nfe) {
													log.warning("Invalid data: " + numeric);
													errorFlag = true;
												}
											} else if (type.equals(LONG)) {
												try {
													gValue.setLongValue((int)numeric);
													cellData.setType(LONG);
													gd.setLvalue(gValue.getLongValue());
												} catch (NumberFormatException nfe) {
													log.warning("Invalid data: " + sValue);
													errorFlag = true;
												}
											} 									
										} else
											errorFlag = true;								
									}
								}
							} else if (type.equals(BOOLEAN)) {
								boolean bValue = false;
								try {
									sValue = String.valueOf(cell.getBooleanCellValue());
								} catch (Exception eb) {}
								bValue = Boolean.parseBoolean(sValue);
								gValue.setBooleanValue(bValue);
								cellData.setType(BOOLEAN);
								gd.setBvalue(gValue.getBooleanValue());
							} else { // STRING or BLANK - treat as String
								try {
									sValue = cell.getStringCellValue();
									sValue = sValue.trim();
									if (col == 0 && (sValue == null || sValue.isEmpty())) {
										breakOut = true;
									} else {
										gValue.setStringValue(sValue);
										cellData.setType(STRING);
										gd.setSvalue(gValue.getStringValue());
									}
								} catch (IllegalStateException ise) {
									log.warning("treating "+header.getName()+" as text, and type="+type+" for col="+(usedHeaderCol-firstCell)+" and row="+rowCtr);
									log.warning("illegal state: "+ise);
									if (ise.getMessage().endsWith("Cannot get a text value from a numeric cell")) {
										double numeric = cell.getNumericCellValue();
										sValue = String.valueOf(numeric);
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
							}
							cellData.setValue(gd);
							cellData.setEmpty(false);
						}
					} else { //if (col != 0) {
						// treat as blank cell
						cellData.setEmpty(true);
						String typeStr = "";
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
					} //else {
						// treat as blank row
						//breakOut = true;
						//errorFlag = true;
					//}
					if ((errorFlag != true)) {
						if (gd != null) {
							cellData.setValue(gd);
							rowDto.setSheetRow(rowCtr);
							cellData.setTag(headers.get(usedHeaderCol-firstCell).getName());
							rowDto.addCell(cellData);
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
				Iterator<CellDataDTO> cit = cells.iterator();
				int ctr = -1;
				while (cit.hasNext() && !found) {
					ctr++;
					CellDataDTO cell = cit.next();
					found = !cell.isEmpty();
				}
				if (found)
					data.add(rowDto);
				else
					rowBreakOut = true;
			}
		}
		return 0;
	}
}
