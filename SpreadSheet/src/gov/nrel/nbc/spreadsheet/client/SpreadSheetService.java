package gov.nrel.nbc.spreadsheet.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The SubmissionService interface is a GWT-RPC interface for
 * submitting and ingesting calculation spreadsheets.
 * 
 * @author James Albersheim
 *
 */
public interface SpreadSheetService extends RemoteService {
	List<String> getOperators(String config_name, String header_name);
	
	List<NameValue> getHeaders(String wbConfig, String shConfig);
	 
	Collection<FileInfo> getAttachments(long id);
	
	List<NameValue> getUrls();
	
	String getTitle();
	
	Collection<NameValue> getMetaData(long id);
	
	String getWorkbookConfigName(long id);
	
	String getWorkbookConfigFromWorkbook(long id);
	
	String getType(String selection);
	
	String getSampleFileName(String config);
	
	List<String> getStringValues(String config_name, String selection);
	
	List<String> getAttachmentExtensions();

	List<String> getWorkbookConfigs();
	
	List<String> getSheetConfigs(String wbConfig);
	
	List<NameValue> getMetaDataHeaders(String config_name);
	
	List<NameValue> getMetaDataHeaders(String config_name, int type);
	
	List<String> getMetaDataValues(String config_name, String header);
	
	Boolean setMetaData(Collection<NameValue> metaData, long id);
	
	List<List<String>> performSelect(String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList, int pos, int page);
	
	List<List<String>> getWorkbookCounts(String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList, int pos, int page);
	
	List<String> downloadSelect(String wbConfig, String shConfig, List<CriteriaTrioDTO> trioList);
	
	String getFileName(String id);
	
	List<String> getFileNameAndPath(String id);

	Boolean removeAttachment(long attachmentId, long workbook_id);

}
