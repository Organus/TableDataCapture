package gov.nrel.nbc.tracker.model;

public class Printers {
	private long id;
	private String name;
	private String remotePrinter;
	private String machine;
	private boolean status;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the remotePrinter
	 */
	public String getRemotePrinter() {
		return remotePrinter;
	}
	/**
	 * @param remotePrinter the remotePrinter to set
	 */
	public void setRemotePrinter(String remotePrinter) {
		this.remotePrinter = remotePrinter;
	}
	/**
	 * @return the machine
	 */
	public String getMachine() {
		return machine;
	}
	/**
	 * @param machine the machine to set
	 */
	public void setMachine(String machine) {
		this.machine = machine;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
}
