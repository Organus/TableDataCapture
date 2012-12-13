package gov.nrel.nbc.security.dbUtils;

import gov.nrel.nbc.security.client.AppConstants;

public class GenerateGUID implements AppConstants {
	private String seed;
	public GenerateGUID(String seed) {
		this.seed = seed;
	}
	public String generateGUID() {
	      long ip; //the 0-63th bit
	      long t;  //the 64-127 bit
	      String salt = CRYPTOKEY+"."+seed;
	      //try {
	            //byte[] ipa=java.net.InetAddress.getLocalHost().getAddress();
	            ip=(long)(salt).hashCode();
	            ip=ip<<32 | ((long)(salt).hashCode());
	      //} catch (java.net.UnknownHostException e) {
	            //ip=(long)(Math.random()*(Long.MAX_VALUE));
	      //}
	      t=System.currentTimeMillis()+(long)(Math.random()*(Long.MAX_VALUE));
	      String sip=Long.toHexString(ip);
	      while (sip.length()<16)
	            sip="0"+sip;
	      String st=Long.toHexString(t);
	      while (st.length()<16)
	            st="0"+st;
	      String ret=sip+st;
	      ret=ret.substring(0,8)+"-"+ret.substring(8,12)+"-"+ret.substring(12,16)+"-"+ret.substring(16,20)+"-"+ret.substring(20);
	      return ret;
	}
}
