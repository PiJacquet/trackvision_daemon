package daemon;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import common.Message;

public class CacheReport {


	private static Map<Integer, List<Message>> cacheReports;
	private static Map<String, List<Object>> cacheTypes;
	
	public CacheReport() {
		//TODO init the cache with the value contained in the DB?
		cacheReports = new ConcurrentHashMap<Integer, List<Message>>();
		cacheTypes = new ConcurrentHashMap<String, List<Object>>();
	}
	
	public void addReport(Integer id, Message report) {
		if(cacheReports.containsKey(id)) {
			cacheReports.get(id).add(report);
			if(cacheReports.get(id).size()>5) {	//we maintain the historic to 5 reports to avoid performance issues
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
		return cacheReports.get(id).get(cacheReports.get(id).size()-1);
	}
	
	
}



