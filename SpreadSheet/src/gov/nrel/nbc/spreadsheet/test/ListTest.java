package gov.nrel.nbc.spreadsheet.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ListTest extends TestCase {

	protected List<Integer> fEmpty;
	protected List<Integer> fFull;

	//@Override
	protected void setUp() {
		fEmpty= new ArrayList<Integer>();
		fFull= new ArrayList<Integer>();
		fFull.add(1);
		fFull.add(2);
		fFull.add(3);
	}
	public static Test suite() {
		return new TestSuite(ListTest.class);
	}
	public void testCapacity() {
		int size= fFull.size(); 
		for (int i= 0; i < 100; i++)
			fFull.add(i);
		assertTrue(fFull.size() == 100+size);
	}
	public void testContains() {
		assertTrue(fFull.contains(1));  
		assertTrue(!fEmpty.contains(1));
	}
	public void testElementAt() {
		int i= fFull.get(0);
		assertTrue(i == 1);

		try { 
			fFull.get(fFull.size());
		} catch (IndexOutOfBoundsException e) {
			return;
		}
		fail("Should raise an ArrayIndexOutOfBoundsException");
	}
	public void testRemoveAll() {
		fFull.removeAll(fFull);
		fEmpty.removeAll(fEmpty);
		assertTrue(fFull.isEmpty());
		assertTrue(fEmpty.isEmpty()); 
	}
	
	public void testStrings() {
		String two = "2";
		String oneeleven = "111";
		String fifteen="15";
		
		if (two.compareTo(oneeleven)>0) System.out.println(two+">"+oneeleven);
		else System.out.println(oneeleven+">="+two);
		if (two.compareTo(fifteen)>0) System.out.println(two+">"+fifteen);
		else System.out.println(fifteen);
		if (fifteen.compareTo(oneeleven)>0) System.out.println(fifteen+">"+oneeleven);
		else System.out.println(oneeleven);
	}
}
