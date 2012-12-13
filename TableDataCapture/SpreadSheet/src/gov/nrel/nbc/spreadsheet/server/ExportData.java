package gov.nrel.nbc.spreadsheet.server;

import gov.nrel.nbc.spreadsheet.client.AppConstants;
import gov.nrel.nbc.spreadsheet.client.CriteriaTrioDTO;
import gov.nrel.nbc.spreadsheet.dao.CellHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.MetadataHeaderDAOHibernate;
import gov.nrel.nbc.spreadsheet.dto.DataFormat;
import gov.nrel.nbc.spreadsheet.dto.DataType;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.utilities.XLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * A class to implement the methods to create Excel files from <CellData> objects.
 * 
 * @author James Albersheim
 *
 */
public class ExportData implements AppConstants {
	
	private static final String REAL_FORMAT = "##0.";
	
	private static final String LONG_FORMAT = "##0";
	
	private static final String STRING = "STRING";

	private static final String DATE = "DATE";

	private static final String REAL = "REAL";

	private static final String LONG = "LONG";

	private static final String BOOLEAN = "BOOLEAN";

	/**
	 * String constant that specifies the format of dates in the Detailed Excel worksheet.
	 */
    private final String DATE_FORMAT = "MM/dd/yyyy";
	
	/**
	 * String constant that specifies the format of dates in the Detailed Excel worksheet.
	 */
    private final String DATE_FORMAT_SHORT = "MM/dd/yy";
	
    /**
     * number of meta headers for workBook
     */
    private int numHeaders = 0;
	/**
	 * Holder for the Excel file.
	 */
	private File excelFile = null;

	/**
	 * A holder for the logging class.
	 */
    private static final XLogger log = new XLogger(ExportData.class);
    
    private List<String> headers = new ArrayList<String>();
	
    /**
     * Private method to initialize the Excel file.
     */
	private void initExcelFile() {
		ResourceBundle rBundle = ResourceBundle.getBundle(SPREADSHEET_PROPERTIES_FILE_NAME);
		File tempDir = new File(rBundle.getString(FILE_DIR));
		if (!tempDir.exists()) {
			boolean ret = tempDir.mkdirs();
			if (!ret) {
				log.warning("failed to create directories for "+tempDir.getPath());
			}
		}
		excelFile = new File(tempDir.getPath() + File.separator + EXCEL_FILE_NAME + new Date().getTime() + AppConstants.EXCEL_FILE_SUFFIX_2007);
		excelFile.setExecutable(true);
		excelFile.setReadable(true);
		excelFile.setWritable(true);
	}
	
	/**
	 * Method to create and save the Detailed Excel file.
	 * 
	 * @param data <List<List<String>>> A list of rows of data (list of strings) to write
	 * @param sheetName <String> The name of the excel spreadsheet
	 * @return <String> Path to Excel file, locally.
	 */
	public String createExcelFile(List<List<String>> data, String sheetName, String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList) {
		if (data != null) {
			try {
				initExcelFile();
				
				
				Workbook workbook = new XSSFWorkbook();
				FileOutputStream fos = new FileOutputStream(excelFile);
				Sheet sheet = null;
								
				if (sheetName != null) {
					// Create new sheet and add it to end of list.
					sheet = workbook.createSheet(sheetName);
				}
				
				populateDetailedWorksheet(data, sheet, sheetName, workbook, wbConfig, shConfig, trioList);
				
				workbook.write(fos);
				fos.close();
				return excelFile.getPath();
			} catch (IOException ie) {
				log.severe("Error creating workbook: " + ie);
				return null;
			} catch (Exception ree) {
				log.severe("Exception: " + ree);
				log.severe(SpreadSheetServiceImpl.getStackTrace(ree));
				return null;
			} 
		} else {
			return null;
		}
	}

	/**
	 * Method to get type based on the selected header
	 * 
	 * @param selection <String> The selected header
	 * @return <String> A type string based on the parameterized types, such as STRING
	 */
	private String getType(String selection) {
		Session session = null;
		String type = null;
		
		if (selection.toLowerCase().equals("attachments") || selection.toLowerCase().equals("workbook id"))
			type = "LONG";
		else {
			try {
				session = HibernateSessionFactory.getSession();
	
				CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
				MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
	
				ctdh.setSession(session);
				mdh.setSession(session);
				Transaction tx = session.beginTransaction();
	
				DataType dataType = ctdh.getTypeBySynonym(selection);
				if (dataType == null) {
					dataType = mdh.getTypeByName(selection);
				}
	
				if (dataType != null)
					type = dataType.getDescription();
	
				tx.commit();
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
			} finally {
				if (session != null && session.isConnected())
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
			}
		}
		return type;
	}

	/**
	 * Method to get type based on the selected header
	 * 
	 * @param selection <String> The selected header
	 * @return <String> A format string based on the parameterized formats, such as MM/dd/yy
	 */
	private String getFormat(String selection, int col) {
		Session session = null;
		String format = null;
		
			try {
				session = HibernateSessionFactory.getSession();
	
				CellHeaderDAOHibernate ctdh = new CellHeaderDAOHibernate();
				MetadataHeaderDAOHibernate mdh = new MetadataHeaderDAOHibernate();
	
				ctdh.setSession(session);
				mdh.setSession(session);
				Transaction tx = session.beginTransaction();
	
				DataFormat	dataFormat = null;
				if (col < numHeaders)
					dataFormat = mdh.getFormatByName(selection);
				if (dataFormat == null) {
					dataFormat = ctdh.getFormatByName(selection);
				}
	
				if (dataFormat != null)
					format = dataFormat.getFormat();
	
				tx.commit();
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
			} finally {
				if (session != null && session.isConnected())
					if (session.isOpen()) {
						session.flush();
						session.close();
					}
			}
			return format;
	}

	public ExportData(int numHeaders) {
		this.numHeaders = numHeaders+1;
		headers.add("Workbook ID");
	}

	private String getRealFormat(String input) {
		int digitsAfterDecimal = 0;
		try {
			digitsAfterDecimal = Integer.parseInt(input);
		} catch (NumberFormatException nfe) {}
		String format = REAL_FORMAT;
		for (int i=0;i<digitsAfterDecimal;i++) {
			format += "0";
		}
		return format;
	}
	/**
	 * Private method to create and populate a sheet in a workbook for
	 * a Detailed report.
	 * 
	 * @param data - <List<List<String>>> Spreadsheet represented as a set of <String>'s
	 * @param sheet - <WritableSheet> MS Excel work sheet to write data to.
	 * @param sheetName - <String> name of work sheet
	 * @param workbook - <WritableWorkbook> - MS Excel work book to write data to.
	 * @throws <RowsExceededException>
	 * @throws <WriteException>
	 */
	private void populateDetailedWorksheet(List<List<String>> data, Sheet sheet, String sheetName, Workbook workbook, String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList)
	{
		
		// Organize data
		CreationHelper createHelper = workbook.getCreationHelper();
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle fcf = workbook.createCellStyle();
		fcf.setFont(font);
		
		int rowCtr = 0;
		Cell cell = null;
		Row row = null;
		
		row = sheet.createRow(rowCtr);
		cell = row.createCell(0, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Search Criteria");
		rowCtr = 1;
		row = sheet.createRow(rowCtr);
		cell = row.createCell(0, Cell.CELL_TYPE_STRING);
		cell.setCellValue("workbook");
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue(wbConfig);
		rowCtr = 2;
		row = sheet.createRow(rowCtr);
		cell = row.createCell(0, Cell.CELL_TYPE_STRING);
		cell.setCellValue("worksheet");
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue(shConfig);
		rowCtr = 3;
		row = sheet.createRow(rowCtr);
		cell = row.createCell(0, Cell.CELL_TYPE_STRING);
		cell.setCellValue("header");
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue("operator");
		cell = row.createCell(2, Cell.CELL_TYPE_STRING);
		cell.setCellValue("value");
		rowCtr = 4;
		Iterator<CriteriaTrioDTO> trit = trioList.iterator();
		while (trit.hasNext()) {
			CriteriaTrioDTO trio = trit.next();
			row = sheet.createRow(rowCtr);
			cell = row.createCell(0, Cell.CELL_TYPE_STRING);
			cell.setCellValue(trio.getHeader());
			cell = row.createCell(1, Cell.CELL_TYPE_STRING);
			cell.setCellValue(trio.getOperator());
			cell = row.createCell(2, Cell.CELL_TYPE_STRING);
			cell.setCellValue(trio.getValue());
			rowCtr++;
		}
		row = sheet.createRow(rowCtr++);
		row = sheet.createRow(rowCtr++);
		
		int firstDataRow = rowCtr;
		
		//List<String> formats = getFormats();

		// Write out values
		Iterator<List<String>> it = data.iterator();
		while (it.hasNext()) {
			List<String> rowData = it.next();
			Iterator<String> it2 = rowData.iterator();
			row = sheet.createRow(rowCtr);
			int colCtr = 0;
			while (it2.hasNext()) {
				String stringValue = it2.next();
				// Write out tags
				cell = null;
				if (rowCtr == firstDataRow) {
					if (colCtr == 0) {
						cell = row.createCell(0, Cell.CELL_TYPE_STRING);
						cell.setCellValue("Workbook ID");
						cell.setCellStyle(fcf);
						sheet.autoSizeColumn(colCtr++);						
					}
					cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
					cell.setCellValue(stringValue);
					headers.add(stringValue);
					cell.setCellStyle(fcf);
					sheet.autoSizeColumn(colCtr++);
				} else {
					String header = headers.get(colCtr);
					String type = "";
					if (colCtr == 0)
						type = "LONG";
					else
						type = getType(header);
					String format1 = getFormat(header,colCtr);
					//String format1 = formats.get(colCtr);
					//log.info(header + " has format1="+format1);
					if (type != null) {
						if (type.equals(STRING)) {
							cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
							cell.setCellValue(stringValue);
							sheet.autoSizeColumn(colCtr++);
						} else if (type.equals(LONG)) {
							Long longValue = 0L;
							if (stringValue != null && !stringValue.isEmpty()) {
								try {
									longValue = Long.parseLong(stringValue);
									CellStyle lcf = workbook.createCellStyle();
									lcf.setDataFormat(createHelper.createDataFormat().getFormat(LONG_FORMAT));
									cell = row.createCell(colCtr, Cell.CELL_TYPE_NUMERIC);
									cell.setCellValue(longValue);
									cell.setCellStyle(lcf);
								} catch (NumberFormatException nfe) {
									//log.info("problem parsing for LONG: ["+stringValue+"]");
									cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
									cell.setCellValue(stringValue);
								}
							} else {
								cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
								cell.setCellValue("");
							}
							sheet.autoSizeColumn(colCtr++);
						} else if (type.equals(REAL)) {
							Double realValue = 0.0;
							if (stringValue != null && !stringValue.isEmpty()) {
								try {
									realValue = Double.parseDouble(stringValue);
									CellStyle ncf = workbook.createCellStyle();
									String format = getRealFormat(format1);
									ncf.setDataFormat(createHelper.createDataFormat().getFormat(format));
									cell = row.createCell(colCtr, Cell.CELL_TYPE_NUMERIC);
									cell.setCellValue(realValue);
									cell.setCellStyle(ncf);						
								} catch (NumberFormatException nfe) {
									//log.info("problem parsing for REAL: ["+stringValue+"]");
									cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
									cell.setCellValue(stringValue);
								}
							} else {
								cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
								cell.setCellValue("");
							}
							sheet.autoSizeColumn(colCtr++);
						} else if (type.equals(DATE)) {
							if (stringValue != null && !stringValue.isEmpty() && !stringValue.equals("---")) {
								CellStyle dcf = workbook.createCellStyle();
								if (format1 != null)
									dcf.setDataFormat(createHelper.createDataFormat().getFormat(format1));
								else if (colCtr < numHeaders)
									dcf.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT_SHORT));
								else
									dcf.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
									
								cell = row.createCell(colCtr, Cell.CELL_TYPE_NUMERIC);
								Date dateValue = null;
								try {
									if (format1 != null)
										dateValue = new SimpleDateFormat(format1).parse(stringValue);
									else if (colCtr < 3)
										dateValue = new SimpleDateFormat(DATE_FORMAT_SHORT).parse(stringValue);
									else
										dateValue = new SimpleDateFormat(DATE_FORMAT).parse(stringValue);
								} catch (ParseException pe) {
									log.severe("Error parsing dates: " + pe);
								}
								cell.setCellValue(dateValue);
								cell.setCellStyle(dcf);
								sheet.autoSizeColumn(colCtr++);
							} else {
								cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
								cell.setCellValue(stringValue);
								sheet.autoSizeColumn(colCtr++);
							}
						} else if (type.equals(BOOLEAN)) {
							cell = row.createCell(colCtr, Cell.CELL_TYPE_BOOLEAN);
							Boolean boolValue = false;
							try {
								boolValue = Boolean.parseBoolean(stringValue);
							} catch (NumberFormatException nfe) {
							}
							cell.setCellValue(boolValue);
							sheet.autoSizeColumn(colCtr++);
						}
					} else {
						colCtr++;
					}
				}
			}
			rowCtr++;
		}
	}
}
