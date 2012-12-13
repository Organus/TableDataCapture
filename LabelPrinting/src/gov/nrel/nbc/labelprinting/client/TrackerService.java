package gov.nrel.nbc.labelprinting.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Interface for the GWT-RPC methods
 * 
 * @author jalbersh
 *
 */
public interface TrackerService extends RemoteService {
	Boolean addUser(String userId, String pass);
	String getSessionId();
	Boolean hasPermission(String id,  String task);
	String getGroupForUser(String id);
	List<String> getGroupsForUser(String id);
	void logoff(String id);
	String logon(String name, String pass);
	Collection<String> getUnits();
	Collection<String> getStatusNames(String id);
	Collection<String> getFeedstockNames();
	Collection<String> getFractionNames();
	Collection<String> getTreatmentNames();
	Collection<String> getSampleIds(String id);
	Collection<String> getShelves();
	Collection<String> getHolders();
	Collection<String> getRooms();
	Collection<String> getBuildings();
	Collection<String> getOrigins(String id);
	Collection<String> getDestinations();
	Collection<String> getForms();
	Collection<String> getStrains();
	Collection<String> getCustodians(String id);
	Collection<String> getBiomasses();
	Collection<String> getBiomassLots();
	Collection<String> getCompositions();
	List<String> getAttachmentExtensions();
	SampleCriteria findSampleById(long sampleId);
	Collection<String> findAttachmentsByTrackingId(long trackingId);
	Collection<FileInfo> findAttachmentsInfoByTrackingId(long trackingId);
	Boolean removeAttachment(long attachmentId, SampleCriteria sample);
	Collection<SampleCriteria> searchSamples(List<SampleCriteria> criterias, String id, int start, int pageSize);
	int searchSamplesCount(List<SampleCriteria> criterias, String id);
	Long saveSample(String id, SampleCriteria criteria);
	Long saveLabel(SampleCriteria criteria);
	String getDataFile(List<SampleCriteria> criterias, String id);
	Collection<String> getOwnerNames();
	Collection<String> getCustodianNames();
	Collection<String> getSubLocations();
	Collection<String> getPackaging();
	Boolean trbHasFile(String num, String page);
	Boolean printLabel(String id, LabelDTO label, String printerName);
	Boolean emailLabel(String id, LabelDTO label);
	Boolean printAllLabels(List<SampleCriteria> criteria, String id, String printerName);
	List<String> getPrinterList();
	Integer getUserSessionTimeout();
	void checkPermutationStrongName(); 
}
