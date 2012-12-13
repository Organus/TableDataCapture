package gov.nrel.nbc.spreadsheet.test;

import java.util.ArrayList;
import java.util.List;

public class PassByValue {

	private Integer x = 1;
	public List<String> list = new ArrayList<String>();
	
	/**
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}
	private void incrementor(Integer y)
	{
		y++;
		//x = y;
	}
	private void add(List<String> l, String str) {
		l.add(str);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PassByValue pbv = new PassByValue();
		System.out.println("b4: x="+pbv.getX());
		pbv.incrementor(pbv.getX());
		System.out.println("after: x="+pbv.getX());
		System.out.println("b4: list="+pbv.getList().size());
		pbv.add(pbv.list,"Hello");
		System.out.println("after: list="+pbv.getList().size());
		
	}
	/**
	 * @return the list
	 */
	public List<String> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<String> list) {
		this.list = list;
	}

}
