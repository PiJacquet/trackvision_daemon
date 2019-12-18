package daemon;

import java.io.IOException;
import java.net.ServerSocket;

import daemon.business.suspectBehavior.SchedulerDetective;

public class DeamonLaunch {

	public static void main(String args[]) throws IOException {
		
		System.out.println("TrackVision Daemon : starting");
		
		// Configuration
		new ConfigurationDaemon();
		final ServerSocket serverSocket = new ServerSocket(ConfigurationDaemon.server_port);
		
		new Thread(new SchedulerDetective()).start();
		
		// Define the closing process of the server application (by closing the port and avoiding any blocked port issue at the next launch)
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("TrackVision Daemon : closing " + ConfigurationDaemon.server_port);
					serverSocket.close();
				} catch (IOException e) {
					System.out.println("#Error while closing the daemon " + e.getMessage());
				}
			}
			}));
		
		RequestReceiver request = new RequestReceiver(serverSocket);
		request.acceptConnection();
	}
	
}
