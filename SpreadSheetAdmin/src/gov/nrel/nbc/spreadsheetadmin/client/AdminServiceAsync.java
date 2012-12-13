package gov.nrel.nbc.spreadsheetadmin.client;

import gov.nrel.nbc.spreadsheetadmin.client.NameValue;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminServiceAsync {

	void getCellHeaders(String wbConfig, String filename, String sheet, AsyncCallback<SheetCellHeaders> callback);
	void getCellDataHeaders(String wbConfigName, String shConfigName, AsyncCallback<List<NameValue>> callback);
	void getMetaDataHeaders(String config_name, int type, AsyncCallback<List<NameValue>> callback);
	void getInternalMetadataHeaders(String wbConfig, String filename, String sheet, AsyncCallback<List<String>> callback);
	void setSheetConfig(List<NameValue> specs, String wbConfig, String sheetname,AsyncCallback<String> callback);
	void alterDataTableForMetaData(List<NameValue> headers, String wbConfigName, String shConfigName, AsyncCallback<Boolean> callback);
	void setMetaDataHeaders(List<NameValue> metaHeaders, String wbConfigName, AsyncCallback<Boolean> callback);
	void setCellDataHeaders(List<NameValue> cellHeaders, String wbConfigName, String shConfigName, AsyncCallback<Boolean> callback);
	void setMetaDataHeader(NameValue metaHeader, String wbConfigName, AsyncCallback<Boolean> callback);
	void setCellDataHeader(NameValue cellHeader, String wbConfigName, String shConfigName, AsyncCallback<Boolean> callback);
	void getTypeId(String description, AsyncCallback<Long> callback);
	void getUrls(AsyncCallback<List<NameValue>> callback);
	void getDataFormats(String type, AsyncCallback<List<NameValue>> callback);
	void getTitle(AsyncCallback<String> callback);
	void getWorkbookConfigs(AsyncCallback<List<String>> callback);
	void getSheetConfigs(String wbConfig, AsyncCallback<List<String>> callback);
	void workbookConfigExists(String wbConfig, AsyncCallback<Boolean> callback);
	void getDataTypes(AsyncCallback<List<NameValue>> callback);
	void deleteWorkbook(long workbook_id, AsyncCallback<Boolean> callback);
	void deleteWorkbookConfig(String workbook_config, AsyncCallback<Boolean> callback);
	void deleteExternalMetadataHeaders(String workbook_config, AsyncCallback<Boolean> callback);
	void deleteInternalMetadataHeaders(String workbook_config, AsyncCallback<Boolean> callback);
	void deleteCellDataHeaders(String workbook_config, String sheet_config, AsyncCallback<Boolean> callback);
}
