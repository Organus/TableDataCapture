package gov.nrel.nbc.spreadsheet.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
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
     * Constructor that takes a Widget.
     * 
     * @param widget Widget to marry.
     */
    public LabeledWidget(Widget widget){
        label = new Label();
        label.setWordWrap(false);
        Panel pan= new HorizontalPanel();
        pan.add(label);
        pan.add(widget);
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
     * @param label Label string.
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
     * @param key Keyboard key that sets focus.
     */
    public void setAccessKey(char key) {
        ((Focusable)widget).setAccessKey(key);
    }


    /**
     * Method to set the focus of the widget.
     * 
     * @param focused Flag to tell widget to have focus or not.
     */
    public void setFocus(boolean focused) {
        ((Focusable)widget).setFocus(focused);
    }


    /**
     * Method to set the tab position of the widget.
     * 
     * @param index Position that widget should take.
     */
    public void setTabIndex(int index) {
        ((Focusable)widget).setTabIndex(index);
    }
}

