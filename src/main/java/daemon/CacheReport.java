package daemon;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import common.Message;

public class CacheReport {


	private static Map<Integer, List<Object>> cacheReports;
	private static Map<String, List<Object>> cacheTypes;
	
	public CacheReport() {
		//TODO init the cache with the value contained in the DB
		cacheReports = new ConcurrentHashMap<Integer, List<Object>>();
		cacheTypes = new ConcurrentHashMap<String, List<Object>>();
	}
	
	public void addReport(Integer id, Object report) {
		if(cacheReports.containsKey(id)) {
			cacheReports.get(id).add(report);
		}
		else {
			cacheReports.put(id, new ArrayList<Object>());
			cacheReports.get(id).add(report);
			// We also update the referential
		}
	}
	
	public List<Object> getReports(Integer id) {
		return cacheReports.get(id);
	}
	
	
}



