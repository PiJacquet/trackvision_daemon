package daemon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import common.Message;
import common.MsgReportHeartBeat;
import common.MsgReportSugarLevel;
import common.ToolSerialize;
import daemon.business.MedicalDetective;

public class RequestHandler implements Runnable{

	protected Socket socket;
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
		case REPORTHEARTBEAT:
			MedicalDetective.heartBeatDetection((MsgReportHeartBeat)input);
			break;
		case REPORTSUGARLEVEL:
			MedicalDetective.sugarLevelDetection((MsgReportSugarLevel)input);
			break;
		case REPORTFURNACE:
			break;
		case REPORTSMOKE:
			break;
		case REPORTTEMPERATURE:
			break;
		default:
			break;
		case LISTOBJECTS:
			break;

		}
		return null;
		
	}
}

