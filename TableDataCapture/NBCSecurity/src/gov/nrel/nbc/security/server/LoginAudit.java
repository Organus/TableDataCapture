package gov.nrel.nbc.security.server;

import gov.nrel.nbc.security.dbUtils.DBUtils;
import gov.nrel.nbc.security.model.Users;
import gov.nrel.nbc.security.utils.XLogger;

import java.sql.Timestamp;
import java.util.Calendar;

public class LoginAudit {

	/**
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(XLogger.INFO);
	public static final int MAX_LOGIN_ATTEMPTS = 10;
	public static final String TABLE = "login_audit";
	private String user;
	private int count;
    private java.util.Date modifiedDate = null;

    private static LoginAudit instance;

    private LoginAudit () { 	
    }
    public static synchronized LoginAudit getInstance() {
		if (instance == null) {
			LoginAudit returnLoginAudit = new LoginAudit();
		    return returnLoginAudit;
		} else {
		    return instance;
		}
    }
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser() {
		return user;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public void setModifiedDate(java.util.Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public java.util.Date getModifiedDate() {
		return modifiedDate;
	}
	public synchronized boolean isMajor(Users user) {
		if (getCount(user)>=LoginAudit.MAX_LOGIN_ATTEMPTS) {
			return true;
		}
		return false;
	}
	private synchronized long getCount(Users user) {
		log.info("in getCount");
        long count = 0;

        log.info("user="+user.getEmail());
        // IF WHERE CLAUSE is not Empty then perform Search
        String sql = "select count from login_audit where user = '"+user.getUserId()+"'";
        DBUtils db = new DBUtils();
        db.performQuery(sql, false);
        if (db.getNextRow()) {
        	count = db.getLongColumn(1);
        	log.info("got count="+count);
        } else {
        	log.info("in loginAudit.else");
        	sql = "insert into login_audit (user,count,modifiedDate)";
        	//Calendar cal = Calendar.getInstance();
        	//Date now = cal.getTime();
        	sql += " values ('"+user.getUserId()+"',"+count+",CURRENT_TIMESTAMP)";//+now+")";
        	int ret = db.performInsert(sql);
        	log.info("inserted "+ret+" rows");
        }
        db.close();
        return count;
	}
	private synchronized boolean past15(Timestamp time) {
		boolean past = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int mins = cal.get(Calendar.MINUTE);
        log.info("minutes="+mins);
        int hours = cal.get(Calendar.HOUR);
        log.info("hours="+hours);
        int days = cal.get(Calendar.DAY_OF_MONTH);
        log.info("days="+days);
        if (mins+15 > 60)
        {
        	mins = mins+15-60;
        	log.info("mins = "+mins);
        	if (hours+1 > 12) {
        		hours=hours+1-12;
        		log.info("hours="+hours);
        		days = days+1;
        		if (days > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
        			days = 1;
        			month = month+1;
        			if (month>11) {
        				month = 0;
        				year = year + 1;
        			}
        		}
        		log.info("days="+days);
        	} else
        		hours = hours+1;
        } else {
        	mins = mins+15;
        }
        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        Calendar ctimePlus15 = Calendar.getInstance();
        ctimePlus15.set(Calendar.MINUTE,mins);
        ctimePlus15.set(Calendar.HOUR,hours);
        ctimePlus15.set(Calendar.DAY_OF_MONTH,days);
        ctimePlus15.set(Calendar.MONTH,month);
        ctimePlus15.set(Calendar.YEAR,year);
        
        java.util.Date timePlus15 = ctimePlus15.getTime();

        // create a java calendar instance
        Calendar calendar = Calendar.getInstance();

        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();
        log.info("now="+now.toString());
        log.info("timePlus15="+timePlus15.toString());
        if (now.after(timePlus15)) {
        	log.info("past 15 minutes since unlocking");
        	past = true;
        }
        return past;
	}
	private synchronized Timestamp getLockedTime(Users user) {
		log.info("in getLockedTime");
        // create a java calendar instance
        Calendar calendar = Calendar.getInstance();

        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();

        // a java current time (now) instance
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        Timestamp time = currentTimestamp;

        log.info("user="+user.getEmail());
        // IF WHERE CLAUSE is not Empty then perform Search
        String sql = "select modifiedDate from login_audit where user = '"+user.getUserId()+"'";
        DBUtils db = new DBUtils();
        db.performQuery(sql, false);
        if (db.getNextRow()) {
            time = db.getTimestampColumn(1);
            log.info("got time="+time);
        }
        db.close();
        return time;
	}
	public synchronized boolean multipleOf3(Users user) {
    	long count = getCount(user);
    	log.info("in multipleOf3 - got count="+count);
        if (count>(LoginAudit.MAX_LOGIN_ATTEMPTS-1)) {
        	//this.resetCount(user);
        	return false;
        }
        log.info("count="+count);
        if (count % 3 == 0 && count != 0) {
        	log.info("multipleOf3");
        	return true;
        }
		return false;
	}
	public synchronized boolean inLockoutTime(Users user) {
        Timestamp lockedTime =  this.getLockedTime(user);
        if (!past15(lockedTime))
        	return true;
        return false;
	}
	public synchronized void setLockedTime(Users user) {
        // create a java calendar instance
        //Calendar calendar = Calendar.getInstance();

        // get a java.util.Date from the calendar instance.
        // this date will represent the current instant, or "now".
        //java.util.Date now = calendar.getTime();

        // a java current time (now) instance
        //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        String sql = "update login_audit set modifiedDate = CURRENT_TIMESTAMP"//+currentTimestamp
         		   + " where user = '"+user.getUserId()+"'";
        DBUtils db = new DBUtils();
        int ret = db.performUpdate(sql);
        db.close();
        log.info("update ret="+ret);
	}
	public synchronized boolean notLocked(Users user) {
    	log.info("in notLocked");
    	long count = getCount(user);
        if (count>(LoginAudit.MAX_LOGIN_ATTEMPTS-1)) {
        	//this.resetCount(user);
        	return false;
        }
        log.info("count="+count);
        if (multipleOf3(user)) {
            Timestamp lockedTime =  this.getLockedTime(user);
            if (past15(lockedTime)) {
            	log.info("past 15");
            	//setLockedTime(user);
	        	return true;
            } else 
            	return false;
        } 
        return true;
    }
    public synchronized void incrementCount(Users user) {
        long count = getCount(user);
        log.info(user.getEmail() + " has count="+count);
        setCount(user,(count+1));
        if ((count+1) % 3 == 0 && (count+1) != 0) {
        	log.info("multipleOf3");
        	setLockedTime(user);
        }
    }
    public synchronized void resetCount(Users user) {
    	log.info("resetting count");
        setCount(user,0);
    }
	private synchronized void setCount(Users user,long count) {
		String sql = "update login_audit set count="+count
                   + " where user = '"+user.getUserId()+"'";
        DBUtils db = new DBUtils();
        int ret = db.performUpdate(sql);
        db.close();
        log.info("update ret="+ret);
    }
}
