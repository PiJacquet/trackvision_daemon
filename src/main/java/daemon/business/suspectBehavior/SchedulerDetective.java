package daemon.business.suspectBehavior;

public class SchedulerDetective implements Runnable {
	
	final protected static Integer frequenceScheduler = 15*60;

	public void run() {
		while(true) {
			try {
				Thread.sleep(frequenceScheduler);
			}catch(InterruptedException e) {
				break;
			}
		}

	}
}
