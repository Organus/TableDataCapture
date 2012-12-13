package gov.nrel.nbc.tracker.model;

/**
 * This is a data transfer object class. This class
 * represents Technical Record Book (TRB).
 * 
 * @author jalbersh
 */
public class Trb {

	/**
	 * Generated unique identifier with no business meaning
	 */
	private long id;
	
	/**
	 * The TRB number
	 */
	private int num = -1;
	
	/**
	 * The TRB page number
	 */
	private int page = -1;
	
	/**
	 * A reference to a scanned copy of the TRB page.
	 */
	private CalcSSFile trbFile;
	
	/**
	 * Simple constructor
	 */
	public Trb() {
		
	}
	
	/**
	 * Complex constructor
	 */
	public Trb(int trbNum, int page) {
		this.num = trbNum;
		this.page = page;
	}
	
	/**
	 * @return the trbFile
	 */
	public CalcSSFile getTrbFile() {
		return trbFile;
	}
	
	/**
	 * @param trbFile the trbFile to set
	 */
	public void setTrbFile(CalcSSFile trbFile) {
		this.trbFile = trbFile;
	}
	
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
	 * @return the num
	 */
	public int getNum() {
		return num;
	}
	
	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}
	
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}
	
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
}
