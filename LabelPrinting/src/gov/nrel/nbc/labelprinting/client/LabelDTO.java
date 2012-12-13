package gov.nrel.nbc.labelprinting.client;

import java.io.Serializable;

/**
 * A data transfer object or DTO for label information.
 * 
 * @author jalbersh
 *
 */
public class LabelDTO implements Serializable {
	private static final long serialVersionUID = -5241686588353206549L;
	
	private String ownerName;
	private String entryDate;
	private String sampleId;
	private String trbNum;
	private String trbPage;
	private String trackingId;
	private String feedstock;
	private String treatment;
	private String fraction;
	private String description;
	private String fire;
	private String reactivity;
	private String specific;
	private String health;
	private boolean printed;
	private String strain;
	private String form;
	private String destination;
	private String custodian;
	private String composition;
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
	 * @return the entryDate
	 */
	public String getEntryDate() {
		return entryDate;
	}
	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
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
	/**
	 * @return the trbNum
	 */
	public String getTrbNum() {
		return trbNum;
	}
	/**
	 * @param trbNum the trbNum to set
	 */
	public void setTrbNum(String trbNum) {
		this.trbNum = trbNum;
	}
	/**
	 * @return the trbPage
	 */
	public String getTrbPage() {
		return trbPage;
	}
	/**
	 * @param trbPage the trbPage to set
	 */
	public void setTrbPage(String trbPage) {
		this.trbPage = trbPage;
	}
	/**
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}
	/**
	 * @param trackingId the trackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
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
	 * @return the feedstock
	 */
	public String getFeedstock() {
		return feedstock;
	}
	/**
	 * @param feedstock the feedstock to set
	 */
	public void setFeedstock(String feedstock) {
		this.feedstock = feedstock;
	}
	/**
	 * @return the treatment
	 */
	public String getTreatment() {
		return treatment;
	}
	/**
	 * @param treatment the treatment to set
	 */
	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}
	/**
	 * @return the fraction
	 */
	public String getFraction() {
		return fraction;
	}
	/**
	 * @param fraction the fraction to set
	 */
	public void setFraction(String fraction) {
		this.fraction = fraction;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the custodian
	 */
	public String getCustodian() {
		return custodian;
	}
	/**
	 * @param custodian the custodian to set
	 */
	public void setCustodian(String custodian) {
		this.custodian = custodian;
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
}
