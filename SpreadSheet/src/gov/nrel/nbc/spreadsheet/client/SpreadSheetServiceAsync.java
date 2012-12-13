package gov.nrel.nbc.spreadsheet.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous GWT-RPC interface for the SubmissionService. 
 * 
 * @author James Albersheim
 *
 */
public interface SpreadSheetServiceAsync {
	void getOperators(String config_name, String header_name, AsyncCallback<List<String>> callback);
	
	void getAttachments(long id, AsyncCallback<Collection<FileInfo>> callback);
	
	void getUrls(AsyncCallback<List<NameValue>> callback);
	
	void getTitle(AsyncCallback<String> callback);
	
	void getMetaData(long id, AsyncCallback<Collection<NameValue>> callback);
	
	void getWorkbookConfigFromWorkbook(long id, AsyncCallback<String> callback);
	
	void getWorkbookConfigName(long id, AsyncCallback<String> callback);
	
	void setMetaData(Collection<NameValue> metaData, long id, AsyncCallback<Boolean> callback);
	
	void getHeaders(String wbConfig, String shConfig, AsyncCallback<List<NameValue>> callback);
	
	void getType(String selection, AsyncCallback<String> callback);
	
	void getSampleFileName(String config, AsyncCallback<String> callback);
	
	void getStringValues(String config_name, String selection, AsyncCallback<List<String>> callback);
	
	void getMetaDataHeaders(String config_name, AsyncCallback<List<NameValue>> callback);
	
	void getMetaDataHeaders(String config_name, int type, AsyncCallback<List<NameValue>> callback);
	
	void getMetaDataValues(String config_name, String header, AsyncCallback<List<String>> callback);
	
	void getAttachmentExtensions(AsyncCallback<List<String>> callback);
	
	void getWorkbookConfigs(AsyncCallback<List<String>> callback);
	
	void getSheetConfigs(String wbConfig, AsyncCallback<List<String>> callback);
	
	void performSelect(String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList, int pos, int page,
			AsyncCallback<List<List<String>>> asyncCallback);
	
	void getWorkbookCounts(String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList, int pos, int page,
			AsyncCallback<List<List<String>>> asyncCallback);
	
	void downloadSelect(String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList, AsyncCallback<List<String>> callback);
	
	void getFileName(String id, AsyncCallback<String> asyncCallback);
	
	void getFileNameAndPath(String id, AsyncCallback<List<String>> asyncCallback);

	void removeAttachment(long attachmentId, long workbook_id, AsyncCallback<Boolean> callback);
	
}
