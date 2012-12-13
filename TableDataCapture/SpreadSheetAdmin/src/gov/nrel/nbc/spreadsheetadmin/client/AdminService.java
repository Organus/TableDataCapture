package gov.nrel.nbc.spreadsheetadmin.client;

import gov.nrel.nbc.spreadsheetadmin.client.NameValue;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface AdminService extends RemoteService, AppConstants {
	SheetCellHeaders getCellHeaders(String wbConfig, String filename, String sheet);
	List<NameValue> getCellDataHeaders(String wbConfigName, String shConfigName);
	List<NameValue> getMetaDataHeaders(String config_name, int type);
	List<String> getInternalMetadataHeaders(String wbConfig, String filename, String sheet);
	String setSheetConfig(List<NameValue> specs, String wbConfig, String sheetname);
	Boolean alterDataTableForMetaData(List<NameValue> headers, String wbConfigName, String shConfigName);
	Boolean setMetaDataHeaders(List<NameValue> metaHeaders, String wbConfigName);
	Boolean setCellDataHeaders(List<NameValue> cellHeaders, String wbConfigName, String shConfigName);
	Boolean setMetaDataHeader(NameValue metaHeader, String wbConfigName);
	Boolean setCellDataHeader(NameValue cellHeader, String wbConfigName, String shConfigName);
	Boolean deleteWorkbook(long workbook_id);
	Boolean deleteWorkbookConfig(String workbook_config);
	Boolean deleteExternalMetadataHeaders(String workbook_config);
	Boolean deleteInternalMetadataHeaders(String workbook_config);
	Boolean deleteCellDataHeaders(String workbook_config, String sheet_config);
	Long getTypeId(String description);
	List<NameValue> getUrls();
	List<NameValue> getDataFormats(String type);
	String getTitle();
	List<String> getWorkbookConfigs();
	List<String> getSheetConfigs(String wbConfig);
	Boolean workbookConfigExists(String wbConfig);
	List<NameValue> getDataTypes();
}
