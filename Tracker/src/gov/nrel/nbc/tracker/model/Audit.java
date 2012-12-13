package gov.nrel.nbc.tracker.model;

import java.util.Date;

/**
 * This is a data transfer object class. An <Audit> represents
 *  audit information for printing a label.
 * 
 * @author jalbersh
 */
public class Audit {
	private long audit_id;
	private Sample sample;
	//private long tracking_id;
	private Date printDate;
	/**
	 * @return the audit_id
	 */
	public long getAudit_id() {
		return audit_id;
	}
	/**
	 * @param audit_id the audit_id to set
	 */
	public void setAudit_id(long audit_id) {
		this.audit_id = audit_id;
	}
	/**
	 * @return the sample
	 */
	public Sample getSample() {
		return sample;
	}
	/**
	 * @param sample the sample to set
	 */
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	/**
	 * @return the tracking_id
	 */
	//public long getTracking_id() {
	//	return tracking_id;
	//}
	/**
	 * @param tracking_id the tracking_id to set
	 */
	//public void setTracking_id(long tracking_id) {
	//	this.tracking_id = tracking_id;
	//}
	/**
	 * @return the printDate
	 */
	public Date getPrintDate() {
		return printDate;
	}
	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
}
