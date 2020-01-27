package daemon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.Message;
import common.MsgListResult;
import common.MsgReportFurnace;
import common.MsgReportHeartBeat;
import common.MsgReportSmoke;
import common.MsgReportSugarLevel;
import common.MsgReportTemperature;
import common.ToolSerialize;
import daemon.business.MedicalDetective;
import daemon.business.suspectBehavior.FurnaceDetective;

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
			ConfigurationDaemon.cache.addReport(((MsgReportFurnace)input).getId(), input);
			new FurnaceDetective(((MsgReportFurnace)input).getId());
			break;
		case REPORTSMOKE:
			ConfigurationDaemon.cache.addReport(((MsgReportSmoke)input).getId(), input);
			break;
		case REPORTTEMPERATURE:
			ConfigurationDaemon.cache.addReport(((MsgReportTemperature)input).getId(), input);
			break;
		case LISTREFERENTIELS:
			return ToolSerialize.messageToJSON((Message)new MsgListResult(getListReferentiels()));
		case LISTOBJECTS: 
			return ToolSerialize.messageToJSON((Message)new MsgListResult(getListObjects()));
		case REPORTPOSITION: 
			return ToolSerialize.messageToJSON((Message)new MsgListResult(getListObjects()));
		default:
			System.out.println("#Error : RequestHandler : Unknow request " + input.getType());
		}
		
		return null;

	}

	private List<List<String>> getListReferentiels(){
		String request ="Select * from Referentiel_Objects"; 
		return getList(request); 
	}
	
	private List<List<String>> getListObjects(){
		String request ="Select * from Objects"; 
		return getList(request); 
	}

	private List<List<String>> getList(String sql){
		List<List<String>> list = new ArrayList<List<String>>();
		Connection connection = ConfigurationDaemon.connectionPool.getConnection();
		try {
			Statement statement = connection.createStatement(); 
			ResultSet result = statement.executeQuery(sql); 
			ResultSetMetaData resultMetada = result.getMetaData(); 
			while(result.next()) {
				List<String> record = new ArrayList<String>(); 
				for (int i = 1; i<= resultMetada.getColumnCount(); i++) {
					record.add(result.getString(i)); 
				}
				list.add(record); 
			}
			ConfigurationDaemon.connectionPool.closeConnection(connection);
			return list; 
		}catch (SQLException e) {
			ConfigurationDaemon.connectionPool.closeConnection(connection);
			e.printStackTrace();
			return null; 
		}
	}
}

