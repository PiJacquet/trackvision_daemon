package daemon;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import common.Message;

public class CacheReport {

	private Timestamp launchDate;
	private Map<Integer, List<Message>> cacheReports;
	private Map<String, List<Object>> cacheTypes; //TODO
	private Integer cacheSize;
	
	public CacheReport(Integer cacheSize) {
		// We keep the launch hour for inactivity detective (it impacts the latest received messages)
		launchDate = new Timestamp(System.currentTimeMillis());
		cacheReports = new ConcurrentHashMap<Integer, List<Message>>();
		cacheTypes = new ConcurrentHashMap<String, List<Object>>();
		this.cacheSize=cacheSize;
	}
	
	public void addReport(Integer id, Message report) {
		
		if(report.getTime()==null)
			report.setTime(new Timestamp(System.currentTimeMillis()));
		
		if(cacheReports.containsKey(id)) {
			cacheReports.get(id).add(report);
			if(cacheReports.get(id).size()>cacheSize) {	//we maintain the historic to X reports to avoid performance issues
				cacheReports.get(id).remove(0);
			}
		}
		else {
			cacheReports.put(id, new ArrayList<Message>());
			cacheReports.get(id).add(report);
			// We also update the referential
		}
	}
	
	public List<Message> getReports(Integer id) {
		return cacheReports.get(id);
	}
	
	public Message getLastReport(Integer id) {
		if(cacheReports.get(id)!=null)
			return cacheReports.get(id).get(cacheReports.get(id).size()-1);
		else
			return null;
	}

	public Timestamp getLaunchDate() {
		return launchDate;
	}

	//For JUnit Test ONLY
	public void setLaunchDate(Timestamp launchDate) {
		this.launchDate = launchDate;
	}
	
}



