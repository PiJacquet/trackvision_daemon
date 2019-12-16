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
				socket = serverSocket.accept();
				System.out.println("TrackVision Daemon : a new connection was initialized! ");
				// We open a new Thread to serve the request
				Thread thread = new Thread(new RequestHandler(socket));
				thread.start();
			}
			catch (IOException e) {
				System.out.println("#Error : RequestReceiver > I/O error: " + e.getMessage());
			}
		}


	}

}
