package gov.nrel.nbc.spreadsheet.test;

import gov.nrel.nbc.spreadsheet.client.CriteriaTrioDTO;
import gov.nrel.nbc.spreadsheet.dao.CellDataDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.SheetDAOHibernate;
import gov.nrel.nbc.spreadsheet.dao.WorkbookConfigDAOHibernate;
import gov.nrel.nbc.spreadsheet.hibernate.HibernateSessionFactory;
import gov.nrel.nbc.spreadsheet.server.CellDataHeaderData;
import gov.nrel.nbc.spreadsheet.server.RowCellData;
import gov.nrel.nbc.spreadsheet.server.WorkbookRows;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;

public class CellDataDAOHibernateTest extends TestCase {


	ClassLoader thisLoader;
	private CellDataDAOHibernate cddh;
	
	  /**
	   * Add as many tests as you like.
	   */
	  public void testSomething() {
			String ret = "";
			cddh = new CellDataDAOHibernate();
			System.out.println(ret);
	  }
	
	//@Override
	protected void setUp() {
    	thisLoader = CellDataDAOHibernate.class.getClassLoader();
	}
	
	public static Test suite() {
		return new TestSuite(CellDataDAOHibernateTest.class);
	}

	public void testMethod() {
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
	    cddh = new CellDataDAOHibernate();
	    cddh.setSession(session);
	    CriteriaTrioDTO trio = new CriteriaTrioDTO();
		trio.setHeader("Sample ID");
		trio.setOperator("=");
		trio.setValue("09CMP-*");
		ArrayList<CriteriaTrioDTO> cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		List<WorkbookRows> metaCalcDatas = new ArrayList<WorkbookRows>();
		WorkbookConfigDAOHibernate wcdh = new WorkbookConfigDAOHibernate();
		wcdh.setSession(session);
		//WorkbookConfig config = wcdh.findByName("SolidsPreDecember2010");
		SheetDAOHibernate sdh = new SheetDAOHibernate();
		sdh.setSession(session);
		//SheetConfig shConfig = wcdh.findSheetByConfigAndName(config, "Digestion");
		List<WorkbookRows> metaList = null;
		if ((metaList != null) && (!metaList.isEmpty()))
			metaCalcDatas.addAll(metaList);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
			while (it6.hasNext()) {
				WorkbookRows mcd = it6.next();
				List<RowCellData> rows = mcd.getRowDataList();
				mcd.setRowDataList(rows);
				Iterator<RowCellData> it7 = rows.iterator();
				while (it7.hasNext()) {
					RowCellData row = it7.next();
					cds.addAll(row.getRowCellDataHeader());
				}
			}
		}
		trio.setHeader("Sample ID");
		trio.setOperator("!=");
		trio.setValue("8*");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
			while (it6.hasNext()) {
				WorkbookRows mcd = it6.next();
				List<RowCellData> rows = mcd.getRowDataList();
				mcd.setRowDataList(rows);
				Iterator<RowCellData> it7 = rows.iterator();
				while (it7.hasNext()) {
					RowCellData row = it7.next();
					cds.addAll(row.getRowCellDataHeader());
				}
			}
		}
		trio.setHeader("S % Ash");
		trio.setOperator(">");
		trio.setValue("4.19");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
			while (it6.hasNext()) {
				WorkbookRows mcd = it6.next();
				List<RowCellData> rows = mcd.getRowDataList();
				mcd.setRowDataList(rows);
				Iterator<RowCellData> it7 = rows.iterator();
				while (it7.hasNext()) {
					RowCellData row = it7.next();
					cds.addAll(row.getRowCellDataHeader());
				}
			}
		}
		trio.setHeader("S % Ash");
		trio.setOperator("<");
		trio.setValue("5.0");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
			while (it6.hasNext()) {
				WorkbookRows mcd = it6.next();
				List<RowCellData> rows = mcd.getRowDataList();
				mcd.setRowDataList(rows);
				Iterator<RowCellData> it7 = rows.iterator();
				while (it7.hasNext()) {
					RowCellData row = it7.next();
					cds.addAll(row.getRowCellDataHeader());
				}
			}
		}
		trio.setHeader("Tracking ID");
		trio.setOperator("<");
		trio.setValue("1");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
			while (it6.hasNext()) {
				WorkbookRows mcd = it6.next();
				List<RowCellData> rows = mcd.getRowDataList();
				mcd.setRowDataList(rows);
				Iterator<RowCellData> it7 = rows.iterator();
				while (it7.hasNext()) {
					RowCellData row = it7.next();
					cds.addAll(row.getRowCellDataHeader());
				}
			}
		}
		trio.setHeader("Tracking ID");
		trio.setOperator(">");
		trio.setValue("0");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			if (metaCalcDatas != null) {
				Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
				while (it6.hasNext()) {
					WorkbookRows mcd = it6.next();
					List<RowCellData> rows = mcd.getRowDataList();
					mcd.setRowDataList(rows);
					Iterator<RowCellData> it7 = rows.iterator();
					while (it7.hasNext()) {
						RowCellData row = it7.next();
						cds.addAll(row.getRowCellDataHeader());
					}
				}
			}
		}
		trio.setHeader("Tracking ID");
		trio.setOperator("=");
		trio.setValue("1794");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			if (metaCalcDatas != null) {
				Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
				while (it6.hasNext()) {
					WorkbookRows mcd = it6.next();
					List<RowCellData> rows = mcd.getRowDataList();
					mcd.setRowDataList(rows);
					Iterator<RowCellData> it7 = rows.iterator();
					while (it7.hasNext()) {
						RowCellData row = it7.next();
						cds.addAll(row.getRowCellDataHeader());
					}
				}
			}
		}
		trio.setHeader("Tracking ID");
		trio.setOperator("!=");
		trio.setValue("0");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			if (metaCalcDatas != null) {
				Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
				while (it6.hasNext()) {
					WorkbookRows mcd = it6.next();
					List<RowCellData> rows = mcd.getRowDataList();
					mcd.setRowDataList(rows);
					Iterator<RowCellData> it7 = rows.iterator();
					while (it7.hasNext()) {
						RowCellData row = it7.next();
						cds.addAll(row.getRowCellDataHeader());
					}
				}
			}
		}
		trio.setHeader("Date Hydrolyzed");
		trio.setOperator("=");
		trio.setValue("2010-08-10");// 00:00:00");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
			while (it6.hasNext()) {
				WorkbookRows mcd = it6.next();
				List<RowCellData> rows = mcd.getRowDataList();
				mcd.setRowDataList(rows);
				Iterator<RowCellData> it7 = rows.iterator();
				while (it7.hasNext()) {
					RowCellData row = it7.next();
					cds.addAll(row.getRowCellDataHeader());
				}
			}
		}	
		trio.setHeader("Date Hydrolyzed");
		trio.setOperator("!=");
		trio.setValue("2010-08-10");// 00:00:00");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			if (metaCalcDatas != null) {
				Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
				while (it6.hasNext()) {
					WorkbookRows mcd = it6.next();
					List<RowCellData> rows = mcd.getRowDataList();
					mcd.setRowDataList(rows);
					Iterator<RowCellData> it7 = rows.iterator();
					while (it7.hasNext()) {
						RowCellData row = it7.next();
						cds.addAll(row.getRowCellDataHeader());
					}
				}
			}
		}	
		trio.setHeader("Date Hydrolyzed");
		trio.setOperator(">");
		trio.setValue("2010-08-10");// 00:00:00");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			if (metaCalcDatas != null) {
				Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
				while (it6.hasNext()) {
					WorkbookRows mcd = it6.next();
					List<RowCellData> rows = mcd.getRowDataList();
					mcd.setRowDataList(rows);
					Iterator<RowCellData> it7 = rows.iterator();
					while (it7.hasNext()) {
						RowCellData row = it7.next();
						cds.addAll(row.getRowCellDataHeader());
					}
				}
			}
		}	
		trio.setHeader("Date Hydrolyzed");
		trio.setOperator("<");
		trio.setValue("2010-08-10");// 00:00:00");
		cdTrioList = new ArrayList<CriteriaTrioDTO>();
		cdTrioList.add(trio);
		if (cdTrioList.size()>0) 
		{
			List<CellDataHeaderData> cds = new ArrayList<CellDataHeaderData>();
			if (metaCalcDatas != null) {
				Iterator<WorkbookRows> it6 = metaCalcDatas.iterator();
				while (it6.hasNext()) {
					WorkbookRows mcd = it6.next();
					List<RowCellData> rows = mcd.getRowDataList();
					mcd.setRowDataList(rows);
					Iterator<RowCellData> it7 = rows.iterator();
					while (it7.hasNext()) {
						RowCellData row = it7.next();
						cds.addAll(row.getRowCellDataHeader());
					}
				}
			}
		}	
	}
}
