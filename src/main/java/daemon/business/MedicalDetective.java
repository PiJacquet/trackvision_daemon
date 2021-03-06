package daemon.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.MsgReportHeartBeat;
import common.MsgReportSugarLevel;
import common.Object_Attribut;
import daemon.ConfigurationDaemon;

public class MedicalDetective {

	public static void heartBeatDetection(MsgReportHeartBeat msg) {
		String msgDate= msg.getDate();
		float msgValue = msg.getHearBeatValue();
		int id_apart=1;
		int id_Object= getId_Object(id_apart);
		System.out.println("Object n : "+ id_Object+ "  " +msgDate + " : " + msgValue);
		addDataToHistorical(msgDate, msgValue, id_Object);

		if (msgValue<60) {
			String message="Low heart rate "+ msgValue + "btm";
			System.out.println(message);
			addMessageToAlert(message,msgDate,id_Object);		
		}
		else if (msgValue>70) {
			String message="High heart rate "+ msgValue + "btm";
			System.out.println(message);
			addMessageToAlert(message,msgDate,id_Object);	

		}

	}

	public static void sugarLevelDetection(MsgReportSugarLevel msg) {
		String msggDate= msg.getDate();
		float msggValue = msg.getSugarLevelValue();
		int id_apartm=2;
		int id_object=getId_Object(id_apartm);
		System.out.println("Object n : "+ id_object+ "  " +msggDate + " : " + msggValue);
		addDataToHistorical(msggDate, msggValue,id_object);

		if (msggValue<0.5) {
			String message="Low Sugar Level "+ msggValue + " g/l";
			System.out.println(message);
			addMessageToAlert(message,msggDate,id_object);		
		}
		else if (msggValue>1.26) {
			String message="High Sugar Level "+ msggValue + " g/l";
			System.out.println(message);
			addMessageToAlert(message,msggDate,id_object);	

		}
	}


	public static boolean addDataToHistorical(String msgDate, float msgValue, int id) {
		Connection connection = ConfigurationDaemon.connectionPool.getConnection();
		try {
			PreparedStatement stmt = connection.prepareStatement("Insert into HistoricalMedicalData (date,MedicalData,id_Object) values (?,?,?);");
			stmt.setString(1, msgDate);
			stmt.setFloat(2,msgValue );
			stmt.setInt(3, id);
			stmt.executeUpdate();
			ConfigurationDaemon.connectionPool.closeConnection(connection);	
		} catch (Exception e) {
			ConfigurationDaemon.connectionPool.closeConnection(connection);	
			return false;
		}
		return true;
	}

	public static boolean addMessageToAlert(String msg,String msgDate, int id) {
		Connection connection = ConfigurationDaemon.connectionPool.getConnection();
		try {
			PreparedStatement stmt = connection.prepareStatement("insert into Alerts (State_Alert, Level_Alert, Date_Alert, Message_Alert, ID_Object) values (?,?,?,?,?);");
			stmt.setInt(1, 1);
			stmt.setInt(2, 1);
			stmt.setString(3,msgDate); 
			stmt.setString(4,msg); 
			stmt.setInt(5,id); 
			stmt.executeUpdate();
			ConfigurationDaemon.connectionPool.closeConnection(connection);	
		} catch (Exception e) {
			ConfigurationDaemon.connectionPool.closeConnection(connection);	
			return false;
		}
		return true;
	}

	public static int getId_Object(int idApartment){

		Connection connection = ConfigurationDaemon.connectionPool.getConnection();
		int id_Object;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT Id_Object FROM Objects WHERE ID_Apartment=? AND Type_Object=?;");
			stmt.setInt(1, idApartment);
			stmt.setString(2, "Connected Bracelet");
			ResultSet result = stmt.executeQuery();

			if(!result.next())
				throw new Exception("No Object associated to the id");
			Object_Attribut object = new Object_Attribut(result.getInt(1));
			id_Object= object.getId();
			ConfigurationDaemon.connectionPool.closeConnection(connection);
		}
		catch(Exception e) {
			ConfigurationDaemon.connectionPool.closeConnection(connection);
			System.out.println("#MedicalDetective error : " + e.getMessage());
			return -1;
		}
		return id_Object;
	}


}
