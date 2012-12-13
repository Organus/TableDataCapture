package gov.nrel.nbc.spreadsheet.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * Public <Composite> class that contains the File selection widgets.
 * 
 * @author James Albersheim
 *
 */
public class FileSelectionPanel extends Composite {
	
	private static final String SELECT_LABEL = "Select a file to attach:";

	private static final String REMOVE_NOTICE = "Please select a file to remove.";

	private static final String REMOVE = "Remove";

	// Constants
	
	private static final String DOWNLOAD = "Download";
	
	private static final String URL_TRACKER_SERVICE = "submissionService";
	
	private static final String URL_ATTACHMENT_UPLOAD_SERVICE = "attachmentUploadService";
	
	private static final String URL_ATTACHMENT_DOWNLOAD_SERVICE = "fileDownloadService";

	private static final String LIST_GRID_WIDTH = "450px";

	private static final String MAX_PANEL_WIDTH = "500px";

	private static final String LIST_PANEL_HEIGHT = "100px";

	private static final String SUBSUBTITLE_STYLE = "SubSubTitle";

	private static final String SELECT_ROW = "Select";

	private static final String ADD_BUTTON = "Add";

	private static final String GRID_HEIGHT = "100px";
	
	private static final int ATTACHMENT_SELECT_COL = 0;
	private static final int ATTACHMENT_ID_COL = 1;
	// Instance variables

	private FileUpload searchBox = new FileUpload();
	
	private SpreadSheetServiceAsync ssa = null;

	private Grid listGrid = new Grid(0,0);

	private ClickHandler itemClickHandler = new ItemSelectClickHandler();

	private Button removeButton = new Button(REMOVE);

	private Button downloadButton = new Button(DOWNLOAD);

	private ClickHandler addClickHandler = new AddClickHandler();

	private ClickHandler removeClickHandler = new RemoveFileClickHandler();
	
	private List<String> listColumns = new ArrayList<String>();

	private final Button addButton = new Button(ADD_BUTTON);

	private final FormPanel attachmentFormPanel = new FormPanel();

	private List<Long> removeList = new ArrayList<Long>();
	
	private List<FileInfo> attachmentList = new ArrayList<FileInfo>();

	private List<FileInfo> attachmentPendingList = new ArrayList<FileInfo>();

	private String workbookId;
	
	private String attachmentId;
	
	private String fileName;
	
	private final HorizontalPanel searchPanel = new HorizontalPanel();
	
	private final FlexTable mainTable = new FlexTable();
	
	private final FormPanel fileDownloadFormPanel = new FormPanel();
	/**
	 * Default constructor
	 */
	public FileSelectionPanel () {
				
		initServices();
		
		initAttachmentForm();
		
		int row = 0;
		int col = 0;
		
		DecoratorPanel dPanel = new DecoratorPanel();
		
		initWidget(dPanel);
		
		dPanel.setWidth(MAX_PANEL_WIDTH);
		dPanel.setHeight(LIST_PANEL_HEIGHT);
		
		mainTable.setWidth(MAX_PANEL_WIDTH);
		mainTable.setHeight(LIST_PANEL_HEIGHT);
		dPanel.add(mainTable);
		
		Label searchLabel = new Label(SELECT_LABEL);
		searchLabel.addStyleDependentName(SUBSUBTITLE_STYLE);
		searchPanel.add(searchLabel);
		
		searchPanel.add(searchBox);
		attachmentFormPanel.add(searchPanel);

		mainTable.setWidget(row, col, attachmentFormPanel);

		row++;
		
		listGrid.setWidth(LIST_GRID_WIDTH);
		listGrid.addClickHandler(itemClickHandler);
		listGrid.setCellSpacing(3);
		listGrid.setBorderWidth(1);
		
		listColumns.add("File Name");
		
		listGrid.setVisible(false);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidth(MAX_PANEL_WIDTH);
		
		VerticalPanel scrollVert = new VerticalPanel();
		scrollVert.add(listGrid);
		scrollVert.setHeight(GRID_HEIGHT);
		scrollPanel.add(scrollVert);
					
		mainTable.setWidget(row, col, listGrid);
		
		addHeadingsToGrid();
		
		row++;
		
		mainTable.setWidget(row, col, fileDownloadFormPanel);

		row++;
		
		String fileDownloadUrlTag = GWT.getModuleBaseURL() + URL_ATTACHMENT_DOWNLOAD_SERVICE;
		fileDownloadFormPanel.setAction(fileDownloadUrlTag);
		fileDownloadFormPanel.setEncoding(FormPanel.ENCODING_URLENCODED);
		fileDownloadFormPanel.setMethod(FormPanel.METHOD_GET);

		HorizontalPanel listButtonPanel = new HorizontalPanel();
		listButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		listButtonPanel.setSpacing(20);
		
		addButton.addClickHandler(addClickHandler);
		listButtonPanel.add(addButton);

		removeButton.setEnabled(false);
		removeButton.addClickHandler(removeClickHandler);
		listButtonPanel.add(removeButton);

		downloadButton.setEnabled(false);
		downloadButton.addClickHandler(new DownloadAttachmentClickHandler());
		listButtonPanel.add(downloadButton);
		
		mainTable.setWidget(row, col, listButtonPanel);
	}

	/**
	 * Private method to setup the Attachments web form.
	 */
	private void initAttachmentForm() {
		String attachUrlTag = GWT.getModuleBaseURL() + URL_ATTACHMENT_UPLOAD_SERVICE;
		attachmentFormPanel.setAction(attachUrlTag);
		attachmentFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		attachmentFormPanel.setMethod(FormPanel.METHOD_POST);
		
		attachmentFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				attachmentId = event.getResults();
				//Code work-around for FireFox anomaly; 
				attachmentId = cleanUp(attachmentId);
				if (workbookId != null && !workbookId.isEmpty() && 
						attachmentId != null && !attachmentId.isEmpty()) {
					Window.alert("File successfully attached with id="+attachmentId);
				} else {
					//Window.alert("Problems encountered attaching file.");
				}
				ListIterator<FileInfo> it = attachmentList.listIterator();
				while (it.hasNext()) {
					FileInfo info = it.next();
					if (info.getFilename().equals(fileName)) {
						info.setAttachment_id(attachmentId);
						it.set(info);
						// Add file to list
                	    int gridRow = listGrid.getRowCount();
                	    listGrid.resizeRows(gridRow + 1);
                	    int col = 0;
                	    listGrid.setWidget(gridRow, col, new CheckBox());
                	    listGrid.getCellFormatter().setHorizontalAlignment(gridRow, col, HasHorizontalAlignment.ALIGN_CENTER);
                	    col++;
                	    listGrid.setText(gridRow, col, info.getName());
                	    listGrid.getCellFormatter().setWordWrap(gridRow, col, false);
                	    listGrid.setVisible(true);
						break;
					}
				}
			}	
			/**
			 * Private method to remove the HTML tags from around sampleId.
			 */
			private String cleanUp(String dirtyString) {
				if (dirtyString != null && !dirtyString.isEmpty()) {
					if (dirtyString.toLowerCase().startsWith("<")) {
						int index = dirtyString.indexOf(">");
						dirtyString = dirtyString.substring(index + 1, dirtyString.length() - 1);
						index = dirtyString.indexOf("<");
						dirtyString = dirtyString.substring(0, index);
					}
				}
				return dirtyString;
			}			
		});
	}

	/**
	 * Public getter for listGrid
	 * 
	 * @return <Grid> the listGrid
	 */
	public Grid getListGrid() {
		return listGrid;
	}

	/**
	 * Public setter for listGrid
	 * 
	 * @param listGrid <Grid> the listGrid to set
	 */
	public void setListGrid(Grid listGrid) {
		this.listGrid = listGrid;
	}

	/**
	 * Protected method to create and set the <Grid> of
	 *  items in the list.
	 */
	protected void addHeadingsToGrid() {
		
		int gridRow = 0;
		int gridCol = 0;
		
		listGrid.resizeRows(gridRow + 1);
		listGrid.resizeColumns(gridCol + 1);
		
		listGrid.setWidget(gridRow, gridCol, new Label(SELECT_ROW));
		listGrid.getCellFormatter().setHorizontalAlignment(gridRow, gridCol, HasHorizontalAlignment.ALIGN_CENTER);
		
		if (listColumns != null && !listColumns.isEmpty()) {
			Iterator<String> it = listColumns.iterator();
			while (it.hasNext()) {
				String label = it.next();
				gridCol++;
				listGrid.resizeColumns(gridCol + 1);
				listGrid.setWidget(gridRow, gridCol, new Label(label));
				listGrid.getCellFormatter().setWordWrap(gridRow, gridCol, false);
				listGrid.getCellFormatter().setHorizontalAlignment(gridRow, gridCol, HasHorizontalAlignment.ALIGN_CENTER);
			}
		}
	}

	/**
	 * Private method to initialize the services
	 */
	private void initServices() {
		ssa = (SpreadSheetServiceAsync) GWT.create(SpreadSheetService.class);		

		ServiceDefTarget endpoint = (ServiceDefTarget) ssa;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + URL_TRACKER_SERVICE);
	}

	/**
	 * Private <ClickHandler> to handle downloading attachments from the work
	 * 
	 * @author James Albersheim
	 *
	 */
	private class DownloadAttachmentClickHandler implements ClickHandler {

		private static final String ATTACHMENT_ID_NAME = "attachmentId";

		public void onClick(ClickEvent event) {
			int index = -1;
			for (int row = 1; row < listGrid.getRowCount() && index == -1; row++) {
				CheckBox cb = (CheckBox)listGrid.getWidget(row, ATTACHMENT_SELECT_COL);
				if (cb.getValue().booleanValue()) 
					index = row;
			}
			
			if (index == -1) 
				Window.alert(REMOVE_NOTICE);
			else {
				// Delete the file on the server
				long attachmentId = 0;
				Iterator<FileInfo> it = attachmentList.iterator();
				while (it.hasNext()) {
					FileInfo fi = it.next();
					String attachmentString = fi.getFilename();
					String gridId = listGrid.getText(index, ATTACHMENT_ID_COL);
					if (attachmentString.equalsIgnoreCase(gridId)) {
						try {
							attachmentId = Long.parseLong(fi.getAttachment_id());
						} catch (NumberFormatException e) {
							attachmentId = -1;
						}
						if (attachmentId != -1) {
							fileDownloadFormPanel.clear();
							Hidden hiddenId = new Hidden(ATTACHMENT_ID_NAME, String.valueOf(attachmentId));							
							VerticalPanel vp = new VerticalPanel();
							vp.add(hiddenId);
							fileDownloadFormPanel.add(vp);
							fileDownloadFormPanel.submit();
						}
					}
				}
				it = attachmentPendingList.iterator();
				while (it.hasNext()) {
					FileInfo fi = it.next();
					String attachmentString = fi.getFilename();
					String gridId = listGrid.getText(index, ATTACHMENT_ID_COL);
					if (attachmentString.equalsIgnoreCase(gridId)) {
						try {
							attachmentId = Long.parseLong(fi.getAttachment_id());
						} catch (NumberFormatException e) {
							attachmentId = -1;
						}
						if (attachmentId != -1) {
							fileDownloadFormPanel.clear();
							Hidden hiddenId = new Hidden(ATTACHMENT_ID_NAME, String.valueOf(attachmentId));							
							VerticalPanel vp = new VerticalPanel();
							vp.add(hiddenId);
							fileDownloadFormPanel.add(vp);
							fileDownloadFormPanel.submit();
						}
					}
				}
			}		
		}
	}

	/**
	 * Private <ClickHandler> to handle the enabling of the Remove button
	 *  and the visibility of the attachment list.
	 * 
	 * @author James Albersheim
	 *
	 */
	private class ItemSelectClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			int index = -1;
			for (int row = 1;row < listGrid.getRowCount() && index == -1;row++) {
				CheckBox cb = (CheckBox)listGrid.getWidget(row, 0);
				if (cb.getValue().booleanValue()) 
					index = row;
			}
			removeButton.setEnabled(index != -1);
			downloadButton.setEnabled(index != -1);
			listGrid.setVisible(listGrid.getRowCount() > 1);
		}		
	}
	
	/**
	 * Private <ClickHandler> to handle adding attachments to the request
	 * 
	 * @author James Albersheim
	 */
	private class AddClickHandler implements ClickHandler {

		/**
		 * Private method to remove the HTML tags from around sampleId.
		 */
		private String cleanUp(String dirtyString) {
			if (dirtyString != null && !dirtyString.isEmpty()) {
				if (dirtyString.toLowerCase().startsWith("<")) {
					int index = dirtyString.indexOf(">");
					dirtyString = dirtyString.substring(index + 1, dirtyString.length() - 1);
					index = dirtyString.indexOf("<");
					dirtyString = dirtyString.substring(0, index);
				}
			}
			return dirtyString;
		}			
		public void onClick(ClickEvent event) {
			fileName = searchBox.getFilename();
			fileName = cleanUp(fileName);
			if (fileName != null && !fileName.isEmpty()) {
				String separator = "\\";//File.separator;
				String path = "";
				String name = "";
				if (fileName.indexOf(separator) != (-1)) {
					name = fileName.substring(fileName.lastIndexOf(separator) + 1);
					path = fileName.substring(0,fileName.lastIndexOf(separator));
				} else {
					name = fileName;
					path = fileName;
				}
				searchBox.setName(name);
				FileInfo info = new FileInfo();
				info.setName(name);
				info.setPath(path);
				info.setFilename(fileName);
				Hidden w1 = new Hidden();
				w1.setName("name");
				w1.setValue(name);
				Hidden w2 = new Hidden();
				w2.setName("path");
				w2.setValue(path);
				Hidden w3 = new Hidden();
				w3.setName("file");
				w3.setValue(fileName);
				searchPanel.add(w1);
				searchPanel.add(w2);
				searchPanel.add(w3);
				if (workbookId != null && !workbookId.isEmpty()) {
					Hidden w4 = new Hidden();
					w4.setName("id");
					w4.setValue(workbookId);
					searchPanel.add(w4);
				}
				//attachmentFormPanel.add(searchPanel);
				attachmentFormPanel.submit();
				searchBox.setName("");
				attachmentList.add(info);
				attachmentPendingList.add(info);				
			} else {
				Window.alert("Please select a file before adding.");
			}
		}		
	}

	/**
	 * Protected <ClickHandler> to handle removing attachments from the request.
	 * 
	 * @author James Albersheim
	 */
	protected class RemoveFileClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			int index = -1;
			for (int row=1;row<listGrid.getRowCount() && index == -1;row++) {
				CheckBox cb = (CheckBox)listGrid.getWidget(row, 0);
				if (cb.getValue().booleanValue()) 
					index = row;
			}
			if (index == -1) 
				Window.alert("Please select a file to remove.");
			else {
				String fileName = listGrid.getText(index, 1);
				ListIterator<FileInfo> it = attachmentList.listIterator();
				while (it.hasNext()) {
					FileInfo info = it.next();
					if (info.getFilename().equalsIgnoreCase(fileName)) {
						String idString = info.getAttachment_id();
						long id = 0;
						try {
							id = Long.parseLong(idString);
						} catch (NumberFormatException e) {
							//Do nothing
						}
						if (id != 0) {
							removeList.add(id);
						}
						break;
					}
				}
				it.remove();
				
				listGrid.removeRow(index);
				removeButton.setEnabled(false);
			}
			listGrid.setVisible(listGrid.getRowCount() > 1);
		}		
	}
	
	/**
	 * Public method to retrieve a list of attachment ids from
	 *  the local list of attachments.
	 * 
	 * @return <List<Long>> List of attachment ids.
	 */
	public List<Long> getAttachmentIds() {
		List<Long> ids = new ArrayList<Long>();
		Iterator<FileInfo> it = attachmentList.iterator();
		while (it.hasNext()) {
			FileInfo fileInfo = it.next();
			try {
				ids.add(Long.parseLong(fileInfo.getAttachment_id()));
			} catch (NumberFormatException e) {
				// do nothing
			}
		}
		return ids;
	}

	/**
	 * Public method to retrieve a list of attachment ids from
	 *  the local list of attachments.
	 * 
	 * @return <List<Long>> List of attachment ids.
	 */
	public List<Long> getPendingAttachmentIds() {
		List<Long> ids = new ArrayList<Long>();
		Iterator<FileInfo> it = attachmentPendingList.iterator();
		while (it.hasNext()) {
			FileInfo fileInfo = it.next();
			try {
				ids.add(Long.parseLong(fileInfo.getAttachment_id()));
			} catch (NumberFormatException e) {
				// do nothing
			}
		}
		return ids;
	}

	/**
	 * Public method to retrieve a list of attachment ids from
	 *  the local list of attachments.
	 * 
	 * @return <List<Long>> List of attachment ids.
	 */
	public List<Long> getAllAttachmentIds() {
		List<Long> ids = new ArrayList<Long>();
		Iterator<FileInfo> it = attachmentPendingList.iterator();
		while (it.hasNext()) {
			FileInfo fileInfo = it.next();
			try {
				ids.add(Long.parseLong(fileInfo.getAttachment_id()));
			} catch (NumberFormatException e) {
				// do nothing
			}
		}
		it = attachmentList.iterator();
		while (it.hasNext()) {
			FileInfo fileInfo = it.next();
			try {
				ids.add(Long.parseLong(fileInfo.getAttachment_id()));
			} catch (NumberFormatException e) {
				// do nothing
			}
		}
		return ids;
	}

	/**
	 * Public setter for removeList
	 * 
	 * @param removeList List<Long> - the removeList to set
	 */
	public void setRemoveList(List<Long> removeList) {
		this.removeList = removeList;
	}

	/**
	 * Public getter for removeList
	 * 
	 * @return <List<Long>> the removeList
	 */
	public List<Long> getRemoveList() {
		return removeList;
	}
	
	/**
	 * Public getter for attachmentList
	 * 
	 * @return <List<FileInfo>> the attachmentList
	 */
	public List<FileInfo> getAttachmentList() {
		return attachmentList;
	}

	/**
	 * Public setter for attachmentList
	 * 
	 * @param attachmentList <List<FileInfo>> - the attachmentList to set
	 */
	public void setAttachmentList(List<FileInfo> attachmentList) {
		this.attachmentList = attachmentList;
	}

	/**
	 * @param workbookId the workbookId to set
	 */
	public void setWorkbookId(String workbookId) {
		this.workbookId = workbookId;
	}

	/**
	 * @return the workbookId
	 */
	public String getWorkbookId() {
		return workbookId;
	}
}
