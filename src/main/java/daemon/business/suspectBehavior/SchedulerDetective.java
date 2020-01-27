package daemon.business.suspectBehavior;

import java.io.IOException;

import daemon.ConfigurationDaemon;

public class SchedulerDetective implements Runnable {
	
	final protected static Integer frequenceScheduler = 15*60;

	public void run() { 
		InactivityDetective inactivityDetective = new InactivityDetective(ConfigurationDaemon.cache);
		while(true) {
			try {
				Thread.sleep(frequenceScheduler);
				inactivityDetective.refreshListObjects();
				inactivityDetective.investiguate();
			}catch(InterruptedException e) {
				break;
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
				break;
			}
		}

	}
}
