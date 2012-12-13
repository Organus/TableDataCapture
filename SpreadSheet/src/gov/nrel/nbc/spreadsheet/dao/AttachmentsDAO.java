package gov.nrel.nbc.spreadsheet.dao;

import gov.nrel.nbc.spreadsheet.dto.Attachments;
import gov.nrel.nbc.spreadsheet.dto.WorkbookData;

import java.util.List;

public interface AttachmentsDAO extends GenericDAO<Attachments, Long> {
	public List<Attachments> findByFilename(String filename);
	
	public List<Attachments> findByWorkbook(WorkbookData workbook);
	
	public Attachments findById(long id);
	
	public Attachments findLatest();
}
