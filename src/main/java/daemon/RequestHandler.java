package daemon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import common.Message;
import common.ToolSerialize;

public class RequestHandler implements Runnable{

	protected Socket socket;
    private static List<Object> list1, list2;
	 
	public RequestHandler(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {

		// Get the JSON format from the Byte request
		String requestJSON = requestDecrypted();
		System.out.println("TrackVision Daemon : Request received > " + requestJSON);

		// Treat it
		
			String answer = treatRequest(requestJSON);
		
		// Send the answer if necessary
		if(answer!=null) {
			System.out.println("TrackVision Daemon : Sending answer > " + answer);
			sendAnswer(answer);
		}

		// The end, we close the socket
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String requestDecrypted() {

		DataInputStream rawInput=null;
		String decodedInput = "";

		// Instantiation
		try {
			rawInput = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("#Error : socket problem > " + e.getMessage());
		}

		// Reading phase
		try {
			decodedInput += rawInput.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return decodedInput;

	}

	private void sendAnswer(String answer) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(answer);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String treatRequest(String request) {
		
		Message input = ToolSerialize.jsonToMessage(request);
		
		switch(input.getType()) {
		case REPORTFURNACE:
			break;
		case REPORTSMOKE:
			break;
		case REPORTTEMPERATURE:
			break;
		case REPORTHEARTBEAT:
			
      list1 = new Vector<Object>();
	  list1.add(input);
 
      CacheReport.addElements(list1);
	  CacheReport.browse(input.getType());
      
			break;
		case REPORTSUGARLEVEL:
			
			      list2 = new Vector<Object>();
				  list2.add(input);
				  
			      CacheReport.addElements(list2);
				  CacheReport.browse(input.getType());
			break;
		default:
			break;

		}
		return null;
		
	}
}

