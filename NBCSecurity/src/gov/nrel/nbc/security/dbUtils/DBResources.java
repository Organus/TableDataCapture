package gov.nrel.nbc.security.dbUtils;

import gov.nrel.nbc.security.client.AppConstants;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DBResources extends ResourceBundle implements AppConstants {
	private HashMap<String,String> contents = new HashMap<String,String>();
    public DBResources() {
       	//	 # DB user and password
//        contents.put(dbUserKey,dbUserValue);	             
//        contents.put(dbPassKey,dbPassValue);	             
    }
    protected Object[][] getContents() {
    
        return new Object[][] {
            // LOCALIZE THE SECOND STRING OF EACH ARRAY (e.g., "OK")
            //{"OkKey", "OK"},
            //{"CancelKey", "Cancel"},
//       		 {dbUserKey,dbUserValue},
//       		 {dbPassKey,dbPassValue},
       		 // END OF MATERIAL TO LOCALIZE
       };
    }
    

	@SuppressWarnings("unchecked")
	//@Override
	public Enumeration<String> getKeys() {
		return (Enumeration<String>) contents.keySet();
	}

	//@Override
	protected Object handleGetObject(String key) {
		return contents.get(key);
	}
	
}

