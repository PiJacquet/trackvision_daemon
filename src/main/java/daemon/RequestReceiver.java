package daemon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestReceiver {

	private ServerSocket serverSocket;

	public RequestReceiver(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	// Main part of the program : waiting for input
	public void acceptConnection() {
		Socket socket = null;
		while (true) {
			try {
				// We wait for a connection
				socket = serverSocket.accept();
				System.out.println("TrackVision Daemon : a new connection was initialized! ");

				if(!ConfigurationDaemon.connectionPool.isEmpty()) {
					// We open a new Thread to serve the request
					RequestHandler service = new RequestHandler(socket);
					Thread thread = new Thread(service);
					thread.start();
				}
				else {
					// No connection is available, we can't serve the request
					System.out.println("#Error : No JDBC connection is available");
					socket.close();
				}
			} catch (IOException e) {
				System.out.println("#Error : RequestReceiver > I/O error: " + e.getMessage());
			}
		}
	}


}
