package gov.nrel.nbc.tracker.server;

import gov.nrel.nbc.tracker.client.AppConstants;
import gov.nrel.nbc.tracker.client.DevTestProdConstants;
import gov.nrel.nbc.tracker.client.SampleCriteria;
import gov.nrel.nbc.tracker.utils.XLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A class to implement the methods to create Excel files from result data.
 * 
 * @author jalbersh
 *
 */
public class ExportData implements AppConstants {
	
	private static final String DATE = "DATE";

	private static final String LONG = "LONG";

	private static final String STRING = "STRING";

	private static final String LONG_FORMAT = "##0";
	
	/**
	 * String constant that specifies the format of dates in the Detailed Excel worksheet.
	 */
    private final String DATE_FORMAT = "yyyy-MM-dd";	
    /**
	 * Holder for the Excel file.
	 */
	private File excelFile = null;

	/**
	 * A holder for the logging class.
	 */
    private static final XLogger log = new XLogger(ExportData.class);

    /**
     * Headers for spreadsheet.
     */
	private final String[] headers = {"Tracking ID","Sample ID","External ID","Owner Name","Custodian Name","Status","TRB Number","TRB Page","Treatment","Feedstock","Fraction","Fire","Reactivity","Specific","Health","Approximate Amount","Units","Building","Room","Sublocation","Shelf","Holder","Packaging","Storage Notes","Date Entered","Comments", "Label Descriptor"};
	private final String[] types = {LONG,           STRING,     STRING,       STRING,      STRING,           STRING,  LONG,        LONG,      STRING,     STRING,     STRING,   LONG,    LONG,       STRING,    LONG,    LONG,                STRING,   STRING,   STRING,  STRING,     STRING, STRING,   STRING,     STRING,         DATE,         STRING,    STRING};
	private final String[] algal_headers = {"Tracking ID","Sample ID","Custodian Name","Origin", "Status","Strain", "Destination", "Biomass Lot", "Form", "Fraction","Composition", "Approximate Amount","Units","Storage Notes","Date Entered","Comments"};
	private final String[] algal_types = {    LONG,           STRING,     STRING,      STRING,    STRING,  STRING,   STRING,        STRING,        STRING,  STRING,   STRING,            LONG,             STRING,   STRING,         DATE,         STRING};
	
    /**
     * Private method to initialize the Excel file.
     */
	private void initExcelFile() {
		ResourceBundle rBundle = ResourceBundle.getBundle(TRACKER_PROPERTIES_FILE_NAME);
		File tempDir = new File(rBundle.getString(TEMP_DIR));
		tempDir.mkdirs();
		excelFile = new File(tempDir.getPath() + File.separator + EXCEL_FILE_NAME + new Date().getTime() + AppConstants.EXCEL_FILE_SUFFIX_2007);
		excelFile.setExecutable(true);
		excelFile.setReadable(true);
		excelFile.setWritable(true);
	}
	
	/**
	 * Method to create and save the query results Excel file.
	 * 
	 * @param samples <SampleCriteria> objects to include in Excel file.
	 * @param sheetName <String> name of sheet
	 * @return <String> Path to Excel file, locally.
	 */
	public String createExcelFile(Collection<SampleCriteria> samples, String sheetName) {
		if (samples != null) {
			try {
				initExcelFile();
				
				
				Workbook workbook = new XSSFWorkbook();
				FileOutputStream fos = new FileOutputStream(excelFile);
				Sheet sheet = null;
								
				if (sheetName != null) {
					// Create new sheet and add it to end of list.
					sheet = workbook.createSheet(sheetName);
				}
				
				populateDetailedWorksheet(samples, sheet, sheetName, workbook);
				
				workbook.write(fos);
				fos.close();
				return excelFile.getPath();
			} catch (IOException ie) {
				log.severe("Error creating workbook: " + ie);
				return null;
			} catch (Exception ree) {
				log.severe("Exception: " + ree);
				log.warning(TrackerServiceImpl.getStackTrace(ree));
				return null;
			} 
		} else {
			return null;
		}
	}

	/**
	 * Private method to create and populate a sheet in a workbook for
	 * a Detailed report.
	 * 
	 * @param samples - Collection<SampleCriteria>
	 * @param sheet - <Sheet> MS Excel work sheet to write data to.
	 * @param sheetName - <String> name of work sheet
	 * @param workbook - <Workbook> - MS Excel work book to write data to.
	 */
	private void populateDetailedWorksheet(Collection<SampleCriteria> samples, Sheet sheet, String sheetName, Workbook workbook)
	{		
		// Organize data
		CreationHelper createHelper = workbook.getCreationHelper();
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle fcf = workbook.createCellStyle();
		fcf.setFont(font);
		
		int rowCtr = 0;
		Row row = sheet.createRow(rowCtr);
		if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			for (int colCtr=0;colCtr<algal_headers.length;colCtr++) {
				// Write out tags
				Cell cell = null;
				if (rowCtr == 0) {
					cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
					cell.setCellValue(algal_headers[colCtr]);
					cell.setCellStyle(fcf);
					sheet.autoSizeColumn(colCtr);
				}
			}
		} else {
			for (int colCtr=0;colCtr<headers.length;colCtr++) {
				// Write out tags
				Cell cell = null;
				if (rowCtr == 0) {
					cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
					cell.setCellValue(headers[colCtr]);
					cell.setCellStyle(fcf);
					sheet.autoSizeColumn(colCtr);
				}
			}
		}
		
		rowCtr++;

		// Write out values
		Iterator<SampleCriteria> it = samples.iterator();
		while (it.hasNext()) {
			SampleCriteria sample = it.next();
			long trackingId = sample.getTrackingId();
			String sampleId = sample.getSampleId() != null ? sample.getSampleId() : "";
			String extId = sample.getExternalId() != null ? sample.getExternalId() : "";
			String own = sample.getOwnerName() != null ? sample.getOwnerName() : "";
			String cust = sample.getCustodianName() != null ? sample.getCustodianName() : "";
			String status = sample.getStatus() != null ? sample.getStatus() : "";
			int num = sample.getTrbNum();
			int page = sample.getTrbPage();
			String treatment = sample.getTreatment() != null ? sample.getTreatment() : "";
			String feed = sample.getFeedstock() != null ? sample.getFeedstock() : "";
			String lot = sample.getBiomass_lots() != null && !sample.getBiomass_lots().isEmpty() ? sample.getBiomass_lots() : "";
			String form = sample.getForm() != null && !sample.getForm().isEmpty() ? sample.getForm() : "";
			String dest = sample.getDestination() != null && !sample.getDestination().isEmpty() ? sample.getDestination() : "";
			String comp = sample.getComposition() != null && !sample.getComposition().isEmpty() ? sample.getComposition() : "";
			String origin = sample.getOrigin() != null && !sample.getOrigin().isEmpty() ? sample.getOrigin() : "";
			String fraction = sample.getFraction() != null ? sample.getFraction() : "";			
			String strain = sample.getStrain() != null ? sample.getStrain() : "";			
			String fire = sample.getFire() != null ? sample.getFire() : "";			
			String reactivity = sample.getReactivity() != null ? sample.getReactivity() : "";			
			String specific = sample.getSpecific() != null ? sample.getSpecific() : "";			
			String health = sample.getHealth() != null ? sample.getHealth() : "";			
			String amount = sample.getAmount() != null ? sample.getAmount() : "";
			String units = sample.getUnits() != null ? sample.getUnits() : "";
			String building = sample.getBuilding() != null ? sample.getBuilding() : "";
			String room = sample.getRoom() != null ? sample.getRoom() : "";
			String subloc = sample.getSubLocation() != null ? sample.getSubLocation() : "";
			String shelf = sample.getShelf() != null ? sample.getShelf() : "";
			String holder = sample.getHolder() != null ? sample.getHolder() : "";
			String pkging = sample.getPackaging() != null ? sample.getPackaging() : "";
			String snotes = sample.getStorageNotes() != null ? sample.getStorageNotes() : "";
			Date date = sample.getStartCreateDate() != null ? sample.getStartCreateDate() : null;
			String sdate = date != null ? date.toString() : "";
			String comments = sample.getComments() != null ? sample.getComments() : "";
			String labelDescr = sample.getLabelDescription() != null && !sample.getLabelDescription().isEmpty() ? sample.getLabelDescription() : "";
			String[] values = {String.valueOf(trackingId),sampleId,extId,own,cust,status,String.valueOf(num),String.valueOf(page),treatment,feed,fraction,fire,reactivity,specific,health,amount,units,
					building,room,subloc,shelf,holder,pkging,snotes,sdate,comments,labelDescr};
			                              // "Tracking ID","Sample ID","Custodian Name","Origin", "Status","Strain", "Destination", "Biomass Lot", "Form", "Fraction","Composition", "Approximate Amount","Units","Storage Notes","Date Entered","Comments"};
			String[] algal_values = {String.valueOf(trackingId),sampleId, cust,           origin, status, strain ,       dest ,     lot,           form,    fraction, comp,               amount,           units,      snotes,         sdate,     comments};
			row = sheet.createRow(rowCtr);
			if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
				for (int colCtr=0; colCtr<algal_headers.length; colCtr++) {
					// Write out tags
					Cell cell = null;
					String type = algal_types[colCtr];
					if (type.equals(STRING)) {
						cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
						cell.setCellValue(algal_values[colCtr]);
						sheet.autoSizeColumn(colCtr);
					} else if (type.equals(LONG)) {
						CellStyle lcf = workbook.createCellStyle();
						lcf.setDataFormat(createHelper.createDataFormat().getFormat(LONG_FORMAT));
						cell = row.createCell(colCtr, Cell.CELL_TYPE_NUMERIC);
						Integer longValue = 0;
						try {
							longValue = Integer.parseInt(algal_values[colCtr]);
						} catch (NumberFormatException nfe) {
						}
						cell.setCellValue(longValue);
						cell.setCellStyle(lcf);
						sheet.autoSizeColumn(colCtr);
					} else if (type.equals(DATE)) {
						Date dateValue = null;
						try {
							dateValue = new SimpleDateFormat(DATE_FORMAT).parse(algal_values[colCtr]);
							cell = row.createCell(colCtr, Cell.CELL_TYPE_NUMERIC);
							CellStyle dcf = workbook.createCellStyle();
							dcf.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
							cell.setCellValue(dateValue);
							cell.setCellStyle(dcf);
						} catch (ParseException pe) {
							log.severe("Error parsing dates:" + pe);
							cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
							cell.setCellValue("");
						}
						sheet.autoSizeColumn(colCtr);
					}
				}
			} else {
				for (int colCtr=0; colCtr<headers.length; colCtr++) {
					// Write out tags
					Cell cell = null;
					String type = types[colCtr];
					if (type.equals(STRING)) {
						cell = row.createCell(colCtr, Cell.CELL_TYPE_STRING);
						cell.setCellValue(values[colCtr]);
						sheet.autoSizeColumn(colCtr);
					} else if (type.equals(LONG)) {
						CellStyle lcf = workbook.createCellStyle();
						lcf.setDataFormat(createHelper.createDataFormat().getFormat(LONG_FORMAT));
						cell = row.createCell(colCtr, Cell.CELL_TYPE_NUMERIC);
						Integer longValue = 0;
						try {
							longValue = Integer.parseInt(values[colCtr]);
						} catch (NumberFormatException nfe) {
						}
						cell.setCellValue(longValue);
						cell.setCellStyle(lcf);
						sheet.autoSizeColumn(colCtr);
					} else if (type.equals(DATE)) {
						CellStyle dcf = workbook.createCellStyle();
						dcf.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
						cell = row.createCell(colCtr, Cell.CELL_TYPE_NUMERIC);
						Date dateValue = null;
						try {
							dateValue = new SimpleDateFormat(DATE_FORMAT).parse(values[colCtr]);
						} catch (ParseException pe) {
							log.severe("Error parsing dates:" + pe);
						}
						cell.setCellValue(dateValue);
						cell.setCellStyle(dcf);
						sheet.autoSizeColumn(colCtr);
					}
				}
			}
			rowCtr++;
		}
	}
}
