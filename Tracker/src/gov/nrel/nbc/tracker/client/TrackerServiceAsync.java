package gov.nrel.nbc.tracker.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous interface for the GWT-RPC communication.
 * 
 * @author jalbersh
 *
 */
public interface TrackerServiceAsync {
	void addUser(String userId, String pass, AsyncCallback<Boolean> callback);
	void getSessionId(AsyncCallback<String> callback);
	void hasPermission(String id, String task, AsyncCallback<Boolean> callback);
	void logoff(String id, AsyncCallback<Void> callback);
	void logon(String name, String pass, AsyncCallback<String> callback);
	void getGroupForUser(String id, AsyncCallback<String> callback);
	void getGroupsForUser(String id, AsyncCallback<List<String>> callback);
	void getUserSessionTimeout(AsyncCallback<Integer> callback);
	void getUnits(AsyncCallback<Collection<String>> callback);
	void getStatusNames(String id, AsyncCallback<Collection<String>> callback);
	void getFeedstockNames(AsyncCallback<Collection<String>> callback);
	void getFractionNames(AsyncCallback<Collection<String>> callback);
	void getTreatmentNames(AsyncCallback<Collection<String>> callback);
	void getSampleIds(String id, AsyncCallback<Collection<String>> callback);
	void getShelves(AsyncCallback<Collection<String>> callback);
	void getHolders(AsyncCallback<Collection<String>> callback);
	void getRooms(AsyncCallback<Collection<String>> callback);
	void getBuildings(AsyncCallback<Collection<String>> callback);
	void getOrigins(String id, AsyncCallback<Collection<String>> callback);
	void getDestinations(AsyncCallback<Collection<String>> callback);
	void getForms(AsyncCallback<Collection<String>> callback);
	void getStrains(AsyncCallback<Collection<String>> callback);
	void getCustodians(String id, AsyncCallback<Collection<String>> callback);
	void getCompositions(AsyncCallback<Collection<String>> callback);
	void getBiomasses(AsyncCallback<Collection<String>> callback);
	void getBiomassLots(AsyncCallback<Collection<String>> callback);
	void getAttachmentExtensions(AsyncCallback<List<String>> callback);
	void findSampleById(long sampleId, AsyncCallback<SampleCriteria> callback);
	void findAttachmentsByTrackingId(long trackingId, AsyncCallback<Collection<String>> callback);
	void findAttachmentsInfoByTrackingId(long trackingId, AsyncCallback<Collection<FileInfo>> callback);
	void removeAttachment(long attachmentId, SampleCriteria sample, AsyncCallback<Boolean> callback);
	void searchSamples(List<SampleCriteria> criterias, String id, int start, int pageSize, AsyncCallback<Collection<SampleCriteria>> callback);
	void searchSamplesCount(List<SampleCriteria> criterias, String id, AsyncCallback<Integer> callback);
	void saveSample(String id, SampleCriteria criteria, AsyncCallback<Long> callback);
	void saveLabel(SampleCriteria criteria, AsyncCallback<Long> callback);
	void getDataFile(List<SampleCriteria> criterias, String id, AsyncCallback<String> callback);
	void getOwnerNames(AsyncCallback<Collection<String>> asyncCallback);
	void getCustodianNames(AsyncCallback<Collection<String>> asyncCallback);
	void getPackaging(AsyncCallback<Collection<String>> asyncCallback);
	void getSubLocations(AsyncCallback<Collection<String>> asyncCallback);
	void trbHasFile(String num, String page, AsyncCallback<Boolean> asyncCallback);
	void printAllLabels(List<SampleCriteria> criteria, String id, String printerName, AsyncCallback<Boolean> asyncCallback);
	void printLabel(String id, LabelDTO label, String printerName, AsyncCallback<Boolean> asyncCallback);
	void emailLabel(String id, LabelDTO label, AsyncCallback<Boolean> asyncCallback);
	void getPrinterList(AsyncCallback<List<String>> asyncCallback);
	void checkPermutationStrongName(AsyncCallback<Void> callback); 
}
