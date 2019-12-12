package daemon;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import common.MessageType;
import common.MsgReportHeartBeat;
import common.MsgReportSugarLevel;

public class CacheReport implements Runnable {


	//private static Map<Integer, List<List<Object>>> cache = new ConcurrentHashMap<Integer, List<List<Object>>>();
	private static Map<Integer,List<Object>> cache2 = new ConcurrentHashMap<Integer,List<Object>> ();
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
	
	public static void  addElements(List<Object> list) {
		int i=0;
		cache2.put(i,list);
		System.out.println(cache2);
		i++;
		
	}
	public static void browse(MessageType mess)  {
		
		switch(mess) {
		case REPORTHEARTBEAT:
		      Set<Entry<Integer, List<Object>>>setHm = cache2.entrySet();
		      Iterator<Entry<Integer, List<Object>>> it = setHm.iterator();
		      while(it.hasNext()){
		         Entry<Integer, List<Object>>e = it.next();
		         MsgReportHeartBeat msg=(MsgReportHeartBeat)  e.getValue().get(0);
		         String msgDate= msg.getDate();
		         float msgValue = msg.getHearBeatValue();
		         int id_res=1;
		         int id=11;
		         System.out.println(msgDate + " : " + msgValue);
		         cache2.remove(e.getValue().get(0));
		         addDataToHistorical(msgDate, msgValue, id,id_res);
		       
	if (msgValue<60) {
		String message="Low heart rate "+ msgValue + "btm";
		System.out.println(message);
		addMessageToAlert(message,msgDate,id);		
	}
	else if (msgValue>70) {
		String message="High heart rate "+ msgValue + "btm";
		System.out.println(message);
		addMessageToAlert(message,msgDate,id);	
		
	}
        	}
			
					break;
				case REPORTSUGARLEVEL:
				      Set<Entry<Integer, List<Object>>>set = cache2.entrySet();
				      Iterator<Entry<Integer, List<Object>>> i = set.iterator();
				      while(i.hasNext()){
				         Entry<Integer, List<Object>>e = i.next();
				         MsgReportSugarLevel msg= (MsgReportSugarLevel)  e.getValue().get(0);
				         String msgDate= msg.getDate();
				         float msgValue = msg.getSugarLevelValue();
				         int id_res=2;
				         int id=12;
				         System.out.println(msgDate + " : " + msgValue);
				         cache2.remove(e.getValue().get(0));
				         addDataToHistorical(msgDate, msgValue,id,id_res);
				       
			if (msgValue<0.5) {
				String message="Low Sugar Level "+ msgValue + " g/l";
				System.out.println(message);
				addMessageToAlert(message,msgDate,id);		
			}
			else if (msgValue>1.26) {
				String message="High Sugar Level "+ msgValue + " g/l";
				System.out.println(message);
				addMessageToAlert(message,msgDate,id);	
				
			}
		        	}
					  
				
					break;
				default:
					break;
		}
		   
	}
		        
		      
public static boolean addDataToHistorical(String msgDate, float msgValue, int id, int id_res) {
	Connection connection = ConfigurationDaemon.connectionPool.getConnection();
	try {
		PreparedStatement stmt = connection.prepareStatement("Insert into HistoricalMedicalData (id_Resident,date,MedicalData,id_Object) values (?,?,?,?);");
		stmt.setInt(1, id_res);
		stmt.setString(2, msgDate);
		stmt.setFloat(3,msgValue );
		stmt.setInt(4, id);
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

	}

