package gov.nrel.nbc.labelprinting.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a data transfer object class. A <Sample> represents a
 *  sample of material from a feedstock or experiment.
 * 
 * @author jalbersh
 */
public class Sample {
	
	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * Sample name
	 */
	private String sampleId;
	
	/**
	 * Comment
	 */
	private String comment;
	
	/**
	 * Description
	 */
	private String description;
	
	/**
	 * Sample amount
	 */
	private String amount;
	
	/**
	 * Units of sample amount
	 */
	private AmountUnits units;
	
	/**
	 * External ID
	 */
	private String externalId;
	
	/**
	 * Custodian name
	 */
	private String custodianName;
	
	/**
	 * Sample treatment
	 */
	private Treatments treatment;
	
	/**
	 * Sample feedstock
	 */
	private Feedstocks feedstock;
	
	/**
	 * Sample fraction
	 */
	private Fractions fraction;
	
	/**
	 * Sample location
	 */
	private Location location;
	
	/**
	 * Sample TRB number
	 */
	private int trbNum;
	
	/**
	 * Sample TRB page
	 */
	private int trbPage;
	
	/**
	 * Sample status
	 */
	private Status status;
	
	/**
	 * Sample create date
	 */
	private Date createDate;
	
	/**
	 * Sample modified date
	 */
	private Date modifyDate;
	
	/**
	 * Owner name
	 */
	private String ownerName;
	
	/**
	 * Storage Notes
	 */
	private String storageNotes;
	
	/**
	 * fire hazard
	 */
	private String fire;
	
	/**
	 * Reactivity Hazard
	 */
	private String reactivity;
	
	/**
	 * health hazard
	 */
	private String health;
	
	/**
	 * specific hazard
	 */
	private String specificHazard;
	
	private String biomassLot;

	/**
	 * Sample form
	 */
	private Forms form;
	
	/**
	 * Sample strain
	 */
	private Strains strain;
	
	/**
	 * Sample destination
	 */
	private Destinations destination;
	
	/**
	 * Sample composition
	 */
	private Composition composition;
	
	/**
	 * printed flag (true=printed)
	 */
	private boolean printed;

	private Origins origin; 

	private Set<Attachments> attachments = new HashSet<Attachments> ();
	
	/**
	 * @return the attachments
	 */
	public Set<Attachments> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachments> attachments) {
		this.attachments = attachments;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getModifyDate() {
		return modifyDate;
	}
	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getSampleId() {
		return sampleId;
	}
	
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public AmountUnits getUnits() {
		return units;
	}
	
	public void setUnits(AmountUnits units) {
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
	
	public Feedstocks getFeedstock() {
		return feedstock;
	}
	
	public void setFeedstock(Feedstocks feedstock) {
		this.feedstock = feedstock;
	}
	
	public Fractions getFraction() {
		return fraction;
	}
	
	public void setFraction(Fractions fraction) {
		this.fraction = fraction;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * @return the trbNum
	 */
	public int getTrbNum() {
		return trbNum;
	}

	/**
	 * @param trbNum the trbNum to set
	 */
	public void setTrbNum(int trbNum) {
		this.trbNum = trbNum;
	}

	/**
	 * @return the trbPage
	 */
	public int getTrbPage() {
		return trbPage;
	}

	/**
	 * @param trbPage the trbPage to set
	 */
	public void setTrbPage(int trbPage) {
		this.trbPage = trbPage;
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
	 * @return the printed
	 */
	public boolean isPrinted() {
		return printed;
	}

	/**
	 * @param printed the printed to set
	 */
	public void setPrinted(boolean printed) {
		this.printed = printed;
	}

	/**
	 * @param storageNotes the storageNotes to set
	 */
	public void setStorageNotes(String storageNotes) {
		this.storageNotes = storageNotes;
	}

	/**
	 * @return the storageNotes
	 */
	public String getStorageNotes() {
		return storageNotes;
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
	 * @param origin the origin to set
	 */
	public void setOrigin(Origins origin) {
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public Origins getOrigin() {
		return origin;
	}

	/**
	 * @param specificHazard the specificHazard to set
	 */
	public void setSpecificHazard(String specificHazard) {
		this.specificHazard = specificHazard;
	}

	/**
	 * @return the specificHazard
	 */
	public String getSpecificHazard() {
		return specificHazard;
	}

	/**
	 * @param treatment the treatment to set
	 */
	public void setTreatment(Treatments treatment) {
		this.treatment = treatment;
	}

	/**
	 * @return the treatment
	 */
	public Treatments getTreatment() {
		return treatment;
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
	 * @param biomassLot the biomassLot to set
	 */
	public void setBiomassLot(String biomassLot) {
		this.biomassLot = biomassLot;
	}

	/**
	 * @return the biomassLot
	 */
	public String getBiomassLot() {
		return biomassLot;
	}

	/**
	 * @return the form
	 */
	public Forms getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(Forms form) {
		this.form = form;
	}

	/**
	 * @return the strain
	 */
	public Strains getStrain() {
		return strain;
	}

	/**
	 * @param strain the strain to set
	 */
	public void setStrain(Strains strain) {
		this.strain = strain;
	}

	/**
	 * @return the destination
	 */
	public Destinations getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Destinations destination) {
		this.destination = destination;
	}

	/**
	 * @param composition the composition to set
	 */
	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	/**
	 * @return the composition
	 */
	public Composition getComposition() {
		return composition;
	}

}