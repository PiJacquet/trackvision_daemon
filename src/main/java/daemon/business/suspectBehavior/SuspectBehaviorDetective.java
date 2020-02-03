package daemon.business.suspectBehavior;

import java.util.List;

import common.Message;
import daemon.ConfigurationDaemon;

public abstract class SuspectBehaviorDetective {
	
	protected Integer inspectionFrequence; //get the Inspection Frequence by multiple of X?
	protected List<Message> reports;
	
	public SuspectBehaviorDetective(Integer id) {
		reports = ConfigurationDaemon.cache.getReports(id);
	}

	public abstract void investiguate();
	
	public Integer getInspectionFrequence() {
		return inspectionFrequence;
	}


}
