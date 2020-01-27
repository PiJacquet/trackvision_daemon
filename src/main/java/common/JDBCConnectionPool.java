package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import daemon.ConfigurationDaemon;

public class JDBCConnectionPool {

	private ArrayList<Connection> listConnections;

	public JDBCConnectionPool() {
		try {
		Class.forName("com.mysql.jdbc.Driver"); 
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		listConnections = new ArrayList<Connection>();
		long debut = System.currentTimeMillis();;
		for (int i = 0; i< 10; i++) {
			if(!init())
				break;
		}
		System.out.println("TrackVision Daemon : Pool loading in : " + (System.currentTimeMillis()-debut) + "ms");
	}

	public boolean init() {
		Connection connection = null;
		try{  
			// Database name (be carefull of the upper/lower case), username, password	
			//connection = DriverManager.getConnection("jdbc:mysql://10.1.3.2:3306/trackdevbd","client_dev","toto");
			connection = DriverManager.getConnection("jdbc:mysql://" + ConfigurationDaemon.db_url + "/" + ConfigurationDaemon.db_name ,
						 ConfigurationDaemon.db_user,ConfigurationDaemon.db_pwd);
			listConnections.add(connection);
		}
		catch(Exception e){ 
			e.getStackTrace();
			System.out.println("#Error : A problem occurs with a connection " + e.getMessage());
			if ( connection != null )
				try {
					connection.close(); // if a problem occurs, the connection is removed
					listConnections.remove(connection);
				} catch (SQLException ignore ) {}
			return false;

		}
		return true;
	}

	public Connection getConnection() {
		//TODO launch a thread to add more connection ? See the synchronization problem
		if (statusConnection()==0) {
			System.out.println("#Error : JDBCConnectionPool > getConnection() : Not enough connections are available");
			return null;
		}
		Connection availableConnection = listConnections.get(0);
		listConnections.remove(availableConnection);
		return availableConnection;
	}

	public void closeConnection(Connection availableConnection) {
		if(!listConnections.contains(availableConnection))
			listConnections.add(availableConnection);
	}

	public int statusConnection() {
		return listConnections.size();
	}
	
	public boolean isEmpty() {
		return statusConnection()==0;
	}
}

