package daemon;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheReport implements Runnable {


	private static Map<Integer, List<List<Object>>> cache = new ConcurrentHashMap<Integer, List<List<Object>>>();

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
