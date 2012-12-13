package gov.nrel.nbc.spreadsheetadmin.parse;

import gov.nrel.nbc.spreadsheetadmin.client.AppConstants;
import gov.nrel.nbc.spreadsheetadmin.client.NameValue;
import gov.nrel.nbc.spreadsheetadmin.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.DataTypeDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.SheetDataDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheetadmin.dao.WorkbookDataDAOHibernate;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.FormulaCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.formula.FormulaException;
import jxl.read.biff.BiffException;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * A class to parse the Calculation Sheet data files into <CellData>
 *  and <CellDataHeader> objects.
 * 
 * @author James Albersheim
 */
public class ExcelParser extends GenericFileParser implements AppConstants {

	private static final String MM_DD_YYYY = "MM/dd/yyyy";

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
	 * The Hibernate session.
	 */
	private Session session;
	/**
	 * The tags for the data as a List<CellDataHeader>.
	 */
	private List<CellDataHeader> headers;

	/**
	 * The data are made up of RowDTOs with variable size.
	 * The size is governed by the number of columns in a configuration
	 * minus the number of null fields in a data table row.
	 */
	private List<RowDTO> data;

	/**
	 * Get a CalcExcelParser for a given File.
	 * 
	 * @param xls - The Excel file.
	 * 
	 * @param config - WorkbookConfig that represents contains
	 * 		(row,col) starting points for the header and data.
	 * 
	 * @param session - A Hibernate Session object.
	 *  
	 * @throws Exception
	 */
	public ExcelParser(File xls, WorkbookConfig config, Session session) throws Exception {
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
	public ExcelParser() {
	}

	/**
	 * Get the parsed Excel file as a Set of <CellData> objectst contained
	 *  in <RowDTO> objects.
	 * 
	 * @return <List<RowDTO>> - A List<RowDTO> holding the parsed data rows.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RowDTO> getData(String worksheet) throws Exception {
		if ((headers == null) || (headers.isEmpty()))
			parseTags(worksheet);
		if ((data == null) || (data.isEmpty())) {
			retVal = parseData(worksheet);
			if (retVal < 0) return null;
		}
		return data;
	}

	/**
	 * 		Get the parsed Excel file as a List of <CellDataHeader> objects.
	 * This method expects that the private global WorkbookConfig wbConfig
	 * is set.
	 * 
	 * @return A <List<CellDataHeader>> holding the parsed data rows.
	 * @throws Exception
	 */
	public List<CellDataHeader> getTags(String worksheet) throws Exception {
		if ((headers == null) || (headers.isEmpty()))
			parseTags(worksheet);
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
			Workbook workbook = Workbook.getWorkbook(xlsFile);
			ret = getDataRows(workbook, worksheet);
			workbook.close();
		}
		return ret;
	}

	/**
	 * Controller method for getting the tags.
	 * 
	 * @throws Exception
	 */
	private void parseTags(String worksheet) throws Exception {
		if (headers == null) {
			Workbook workbook = Workbook.getWorkbook(xlsFile);
			getHeaders(workbook, worksheet);
			workbook.close();
		}
	}

	/**
	 * Get the data tag names.
	 * 
	 * @return List<String>
	 */
	@SuppressWarnings("deprecation")
	public List<String> getSheets() {
		List<String> hdrs = new ArrayList<String>();
		try {
			Workbook workbook = Workbook.getWorkbook(xlsFile);
			Sheet[] sheets = workbook.getSheets();
			for (int i=0;i<sheets.length;i++)
				if (!sheets[i].isHidden())
					hdrs.add(sheets[i].getName());
		} catch (Exception e) {
			log.severe("Exception on getting type. error: " + e);
			String stack = SampleSpreadSheetUploadServiceImpl.getStackTrace(e);
			log.warning(stack);
		} 
		return hdrs;
	}
	
	/**
	 * Get the data tag names.
	 * 
	 * @param worksheet
	 *            The Worksheet we parse. 
	 *            
	 * @return List<NameValue>
	 */
	public List<NameValue> getMetadataNames(String worksheet) {
		FileInputStream fis = null;
		List<NameValue> metadatas = new ArrayList<NameValue>();
		try {
			fis = new FileInputStream(xlsFile);
			Workbook workbook = Workbook.getWorkbook(xlsFile);
			Sheet sheet = workbook.getSheet(worksheet);
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
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
				for (int rowCtr=firstMDRow;rowCtr<=lastMDRow;rowCtr++) {
					Cell[] row = sheet.getRow(rowCtr);
					NameValue nv = new NameValue();
					Cell cell = row[firstMDCol];
					nv.setOrder(rowCtr-firstMDRow);
					String name = cell.getContents();
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
	 * @return List<NameValue> A List of data transfer objects.
	 */
	public List<NameValue> getMetadata(String worksheet) {
		FileInputStream fis = null;
		List<NameValue> metadatas = new ArrayList<NameValue>();
		try {
			fis = new FileInputStream(xlsFile);
			Workbook workbook = Workbook.getWorkbook(xlsFile);
			Sheet sheet = workbook.getSheet(worksheet);
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
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
			for (int rowCtr=firstMDRow;rowCtr<=lastMDRow;rowCtr++) {
				Cell[] row = sheet.getRow(rowCtr);
				NameValue nv = new NameValue();
				nv.setOrder(rowCtr-firstMDRow);
				for (int colCtr=firstMDCol;colCtr<=lastMDCol;colCtr++) {
					Cell cell = row[colCtr];
					if (colCtr == firstMDCol) {
						String name = cell.getContents();
						nv.setName(name);
					} else if (colCtr == lastMDCol) {
						String value = cell.getContents();
						nv.setValue(value);
						MetadataHeader header = mhdh.findByName(nv.getName(),wbConfig);
						if (header != null) {
							DataType dt = header.getTypeId();
							int type = (int)dt.getId();
							if (type==3) {// date
								CellType ctype = cell.getType();
								try {
									DataFormat format = header.getData_format();
									String sformat = MM_DD_YYYY;
									if (format != null) {
											sformat = format.getFormat();
									}
									SimpleDateFormat dFormat = new SimpleDateFormat(sformat);
									dFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
									Date date = null;
									if (ctype == CellType.DATE || ctype == CellType.DATE_FORMULA) {
										DateCell dcell = (DateCell)cell;
										date = dcell.getDate();
										Calendar cal = Calendar.getInstance();
										cal.setTime(date);
										//cal.add(Calendar.HOUR_OF_DAY, 8);
										date = cal.getTime();
									} else {
										date = dFormat.parse(value);
									}
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
	 * 			String - The Worksheet we parse.
	 * @return List<String>
	 */
	public List<String> getHeaders(String worksheet) {
		List<String> hdrs = new ArrayList<String>();
		try {
			Workbook workbook = Workbook.getWorkbook(xlsFile);
			Sheet sheet = workbook.getSheet(worksheet);
			if (sheet==null){
				log.warning("Worksheet "+worksheet+" does not exist in this workbook.");
				return hdrs;
			}
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			wcdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			if (dbSheet==null){
				log.warning(wbConfig.getConfig_name()+" + "+worksheet+" combination does not exist in the database.");
				return hdrs;
			}
			short firstCell = (short)(dbSheet.getHdr_col()-1);
			short headerRow = (short) (dbSheet.getHdr_row()-1);
			Cell[] row = sheet.getRow(headerRow);
			if (row==null || row.length==0){
				log.warning("Did not find expected header row at row "+headerRow+" in worksheet "+worksheet+".");
				return hdrs;
			}			
			for (int i = firstCell; i < sheet.getColumns(); i++) {
				boolean hidden = sheet.getColumnView(i).isHidden();
				String headerName = row[i].getContents();
				headerName = headerName.trim();
				if (!hidden) {
					headerName = headerName.replace("\"","");
					hdrs.add(headerName);
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
		} 
		return hdrs;
	}

	/**
	 * Finds and adds the data header names to the global
	 * private ArrayList<CellDataHeader> headers.
	 * 
	 * @param workbook
	 *            The Workbook we parse. This is not closed.
	 * 
	 */
	private void getHeaders(Workbook workbook, String worksheet) {
		try {
			CellHeaderDAOHibernate chdh = new CellHeaderDAOHibernate();
			chdh.setSession(session);
			SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
			WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
			WorkbookDataDAOHibernate wdh = new WorkbookDataDAOHibernate();
			wcdh.setSession(session);
			wdh.setSession(session);
			sdh.setSession(session);
			SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
			headers = new ArrayList<CellDataHeader>();
			headers.addAll(chdh.findByConfig(dbSheet));
			Sheet sheet = workbook.getSheet(worksheet);
			int firstRow = (short)(dbSheet.getHdr_row()-1);
			int firstCol = (short)(dbSheet.getHdr_col()-1);
			Cell[] row = sheet.getRow(firstRow);
			for (int colCtr=0;colCtr<row.length;colCtr++) {
				Cell cell = row[colCtr];
				if (colCtr >= firstCol) {
					boolean hidden = sheet.getColumnView(colCtr).isHidden();
					String headerName = cell.getContents();
					headerName = headerName.trim();
					if (!hidden) {
						headerName = headerName.replace("\"","");
						headerName = headerName.replace(",", "_");
						CellDataHeader cellHeader = chdh.findByNameAndConfig(dbSheet, headerName);
						log.info(cellHeader.getName());
						//headers.add(cellHeader);
					}
				}
			}
		} catch (NumberFormatException nfe) {
		} catch (HibernateException he) {
			log.severe("Hibernate exception: " + he);
			try {
				if (session != null && session.isConnected())
					session.getTransaction().rollback();
			} catch (HibernateException rbEx) {
				log.severe("Couldn't roll back transaction! Error: " + rbEx);
			}
		} catch (Exception ex) {
			log.severe("Exception caught: " + ex);
			log.severe(SampleSpreadSheetUploadServiceImpl.getStackTrace(ex));
		} 			
	}

	/**
	 * Get the data from the Summary worksheet and places
	 * it in the private global ArrayList<RowDTO> data.
	 * RowDTOs will have variable size because they are compacted
	 * by ignoring null database fields.
	 * 
	 * @param workbook
	 *            The Workbook we parse.
	 * @throws ServletException 
	 */
	private int getDataRows(Workbook workbook, String worksheet) throws IOException,
			BiffException, ServletException, Exception {
		Sheet sheet = workbook.getSheet(worksheet);
		if (sheet == null) {
			log.severe("Unable to get worksheet="+worksheet);
			return AppConstants.MISSING_WORKSHEET;
		}
		data = new ArrayList<RowDTO>();

		SheetDataDAOHibernate sdh = new SheetDataDAOHibernate();
		sdh.setSession(session);
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		SheetConfig dbSheet = wcdh.findSheetByConfigAndSynonym(wbConfig, worksheet);
		int firstDataRow = (int)dbSheet.getData_row()-1;
		int firstDataCol = (int)dbSheet.getData_col()-1;
		int lastCol = firstDataCol + headers.size();
		if (lastCol > sheet.getColumns()) {
			return AppConstants.MISMATCHED_COLUMN_NUMBERS;
			//throw new Exception ("Number of worksheet columns exceeds number of columns in configuration.");
		}
		for (int row = firstDataRow; row < sheet.getRows(); row++) {
			RowDTO rowDto = new RowDTO();
			int usedHeaderCol = firstDataCol;
			lastCol = firstDataCol + headers.size();
			for (int col = firstDataCol; col < lastCol; col++) {
				CellDataDTO cellData = new CellDataDTO();
				boolean errorFlag = false;
				boolean hidden = sheet.getColumnView(col).isHidden();
				if (!hidden) {
					Cell cell = sheet.getCell(col, row);
					String sValue = cell.getContents();
					if (sValue != null) sValue = sValue.trim();
					if ((sValue != null) && (!sValue.isEmpty())) {
						CellType type = sheet.getCell(col, row).getType();
						if (row == firstDataRow && cell != null) {
							if (type == CellType.NUMBER_FORMULA || type == CellType.DATE_FORMULA || type == CellType.STRING_FORMULA || type == CellType.BOOLEAN_FORMULA) {
								FormulaCell c=null;
								String f="";
								try {
								    c  = (FormulaCell) cell;
								} catch (Exception e) {e.printStackTrace();}
								try {
								    f=c.getFormula();
									log.info("formula="+f);
								} catch (FormulaException e) {e.printStackTrace();}	
							}
						}
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
										log.warning("Invalid data: " + sValue);
										errorFlag = true;
									}
								} else if (stype.equals(LONG) || (type == CellType.ERROR)) {
									//return AppConstants.SPREADSHEET_ERRORS;
									sValue = "";
									//throw new ServletException("There was an error in the spreadsheet.");
								} else {
									try {
										if (!sValue.equals("true") && !sValue.equals("false")) {
											NumberCell nCell = (NumberCell)cell;
											double val = nCell.getValue();
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
					        		collectionTime = ((DateCell) cell).getDate();
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
				} else {
					// hidden is true here
					lastCol++;					
				}
			}
			if ((rowDto.getRow() != null) && (!rowDto.getRow().isEmpty()))
				data.add(rowDto);
		}
		return 0;
	}
}
