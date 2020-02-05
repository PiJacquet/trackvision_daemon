package daemon.business.suspectBehavior;

import java.io.IOException;

import daemon.ConfigurationDaemon;

public class SchedulerDetective implements Runnable {
	
	private Integer frequenceScheduler;
	
	public SchedulerDetective(Integer frequenceScheduler) {
		this.frequenceScheduler=frequenceScheduler*1000;
	}

	public void run() { 
		InactivityDetective inactivityDetective = new InactivityDetective(ConfigurationDaemon.cache);
		while(true) {
			try {
				Thread.sleep(frequenceScheduler);
				System.out.println("launching a test");
				inactivityDetective.refreshListObjects();
				System.out.println("Number of problems :" + inactivityDetective.investiguateAll());
			}catch(InterruptedException e) {
				break;
			}
			catch(IOException e) {
				System.out.println("Scheduler Detective : " + e.getMessage());
				break;
			}
		}

	}
}
