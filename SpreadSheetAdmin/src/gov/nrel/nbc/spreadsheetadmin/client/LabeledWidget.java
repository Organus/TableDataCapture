package gov.nrel.nbc.spreadsheetadmin.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A custom GWT widget to marry a label to another widget.
 * 
 * @author James Albersheim
 */
public class LabeledWidget extends Composite implements Focusable {
	
	/**
	 * Label object to marry to the widget.
	 */
    Label label;
    
    /**
     * GWT Widget to marry to the label.
     */
    Widget widget;

    /**
     * GWT HorizontalPanel to contain label and widget.
     */
	private final HorizontalPanel pan = new HorizontalPanel();

    /**
     * Constructor that takes a Widget.
     * 
     * @param widget Widget to marry.
     */
    public LabeledWidget(Widget widget){
        label = new Label();
        label.setWordWrap(false);
        pan.setSpacing(10);
        pan.add(label);
        pan.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_LEFT);
        pan.add(widget);
        pan.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_RIGHT);
        this.widget = widget;
        initWidget(pan);
    }
    
    public String getLabelText() {
        return label.getText();
    }
    
    public Label getLabel(){
        return label;
    }

    /**
     * Method to set the text of the label object.
     * 
     * @param label String Label string.
     */
    public void setLabelText(String label) {
        this.label.setText(label);
    }

    public Widget getWidget() {
        return (Widget) widget;
    }

    /**
     * Method to return the tab position of the widget.
     */
    public int getTabIndex() {
        return ((Focusable)widget).getTabIndex();
        
    }

    /**
     * Method to set the key that automatically sets
     * the focus to the widget.
     * 
     * @param key char Keyboard key that sets focus.
     */
    public void setAccessKey(char key) {
        ((Focusable)widget).setAccessKey(key);
    }


    /**
     * Method to set the focus of the widget.
     * 
     * @param focused boolean Flag to tell widget to have focus or not.
     */
    public void setFocus(boolean focused) {
        ((Focusable)widget).setFocus(focused);
    }


    /**
     * Method to set the tab position of the widget.
     * 
     * @param index int Position that widget should take.
     */
    public void setTabIndex(int index) {
        ((Focusable)widget).setTabIndex(index);
    }
    
    /**
     * Public method to set the width of the panel.
     * 
     * @param width String
     */
    public void setPanelWidth(String width) {
    	if (!width.isEmpty())
    		pan.setWidth(width);
    }
}

