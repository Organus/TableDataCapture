package gov.nrel.nbc.labelprinting.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data transfer object for sample data.
 * 
 * @author jalbersh
 *
 */
public class SampleCriteria implements Serializable {

	private static final long serialVersionUID = 8801221172797514447L;

	private long trackingId = 0;
	private String sampleId = null;
	private String comments = null;
	private String description = null;
	private String amount = null;
	private String units = null;
	private String externalId = null;
	private String custodianName = null;
	private String origin = null;
	private String treatment = null;
	private String fire;
	private String reactivity;
	private String specific;
	private String health;
	private String destination = null;
	private String form = null;
	private String strain = null;
	private String sources = null;
	private String biomass_lots = null;
	private String composition = null;
	private String operator=null;
	
	private List<Long> trackingIds = new ArrayList<Long>();
	
	private List<String> attachment_ext = new ArrayList<String>();

	private List<Long> attachmendIds = new ArrayList<Long>();

	private List<String> groups = new ArrayList<String>();

	private String feedstock = null;
	private String fraction = null;
	private String status = null;
	private String building = null;
	private String room = null;
	private String holder = null;
	private String shelf = null;
	private String subLocation = null;
	private String packaging = null;
	private String ownerName = null;
	private String storageNotes = null; 
	private int trbNum = 0;
	private int trbPage = 0;
	private Date startCreateDate = null;
	private Date endCreateDate = null;
	private Date modDate = null;

	public long getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(long trackingId) {
		this.trackingId = trackingId;
	}

	/**
	 * @return the sampleId
	 */
	public String getSampleId() {
		return sampleId;
	}

	/**
	 * @param sampleId the sampleId to set
	 */
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCustodianName() {
		return custodianName;
	}

	public void setCustodianName(String custodianName) {
		this.custodianName = custodianName;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getFeedstock() {
		return feedstock;
	}

	public void setFeedstock(String feedstock) {
		this.feedstock = feedstock;
	}

	public String getFraction() {
		return fraction;
	}

	public void setFraction(String fraction) {
		this.fraction = fraction;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public int getTrbNum() {
		return trbNum;
	}

	public void setTrbNum(int trbNum) {
		this.trbNum = trbNum;
	}

	public int getTrbPage() {
		return trbPage;
	}

	public void setTrbPage(int trbPage) {
		this.trbPage = trbPage;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public String getStorageNotes() {
		return storageNotes;
	}
	/**
	 * @param storageNotes the storageNotes to set
	 */
	public void setStorageNotes(String storageNotes) {
		this.storageNotes = storageNotes;
	}

	/**
	 * @return the holder
	 */
	public String getHolder() {
		return holder;
	}

	/**
	 * @param holder the holder to set
	 */
	public void setHolder(String holder) {
		this.holder = holder;
	}

	/**
	 * @return the subLocation
	 */
	public String getSubLocation() {
		return subLocation;
	}

	/**
	 * @param subLocation the subLocation to set
	 */
	public void setSubLocation(String subLocation) {
		this.subLocation = subLocation;
	}

	/**
	 * @return the packaging
	 */
	public String getPackaging() {
		return packaging;
	}

	/**
	 * @param packaging the packaging to set
	 */
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param attachmendIds the attachmendIds to set
	 */
	public void setAttachmendIds(List<Long> attachmendIds) {
		this.attachmendIds = attachmendIds;
	}

	/**
	 * @return the attachmendIds
	 */
	public List<Long> getAttachmendIds() {
		return attachmendIds;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param fire the fire to set
	 */
	public void setFire(String fire) {
		this.fire = fire;
	}

	/**
	 * @return the fire
	 */
	public String getFire() {
		return fire;
	}

	/**
	 * @param reactivity the reactivity to set
	 */
	public void setReactivity(String reactivity) {
		this.reactivity = reactivity;
	}

	/**
	 * @return the reactivity
	 */
	public String getReactivity() {
		return reactivity;
	}

	/**
	 * @param specific the specific to set
	 */
	public void setSpecific(String specific) {
		this.specific = specific;
	}

	/**
	 * @return the specific
	 */
	public String getSpecific() {
		return specific;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(String health) {
		this.health = health;
	}

	/**
	 * @return the health
	 */
	public String getHealth() {
		return health;
	}

	/**
	 * @param startCreateDate the startCreateDate to set
	 */
	public void setStartCreateDate(Date startCreateDate) {
		this.startCreateDate = startCreateDate;
	}

	/**
	 * @return the startCreateDate
	 */
	public Date getStartCreateDate() {
		return startCreateDate;
	}

	/**
	 * @param endCreateDate the endCreateDate to set
	 */
	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	/**
	 * @return the endCreateDate
	 */
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	/**
	 * @param attachment_ext the attachment_ext to set
	 */
	public void setAttachment_ext(List<String> attachment_ext) {
		this.attachment_ext = attachment_ext;
	}

	/**
	 * @return the attachment_ext
	 */
	public List<String> getAttachment_ext() {
		return attachment_ext;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	/**
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * @param trackingIds the trackingIds to set
	 */
	public void setTrackingIds(List<Long> trackingIds) {
		this.trackingIds = trackingIds;
	}

	/**
	 * @return the trackingIds
	 */
	public List<Long> getTrackingIds() {
		return trackingIds;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the form
	 */
	public String getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(String form) {
		this.form = form;
	}

	/**
	 * @return the strain
	 */
	public String getStrain() {
		return strain;
	}

	/**
	 * @param strain the strain to set
	 */
	public void setStrain(String strain) {
		this.strain = strain;
	}

	/**
	 * @return the sources
	 */
	public String getSources() {
		return sources;
	}

	/**
	 * @param sources the sources to set
	 */
	public void setSources(String sources) {
		this.sources = sources;
	}

	/**
	 * @return the biomass_lots
	 */
	public String getBiomass_lots() {
		return biomass_lots;
	}

	/**
	 * @param biomass_lots the biomass_lots to set
	 */
	public void setBiomass_lots(String biomass_lots) {
		this.biomass_lots = biomass_lots;
	}

	/**
	 * @param composition the composition to set
	 */
	public void setComposition(String composition) {
		this.composition = composition;
	}

	/**
	 * @return the composition
	 */
	public String getComposition() {
		return composition;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

}
