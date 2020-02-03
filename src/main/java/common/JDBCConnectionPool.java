package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import daemon.ConfigurationDaemon;

public class JDBCConnectionPool implements Runnable{

	private ArrayList<Connection> listConnections;

	public JDBCConnectionPool() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
		}catch(ClassNotFoundException  e) {
			System.out.println(e.getMessage());
		}
		listConnections = new ArrayList<Connection>();
		long debut = System.currentTimeMillis();;
		for (int i = 0; i< 10; i++) {
			if(!init())
				break;
		}
		System.out.println("Pool loading in : " + (System.currentTimeMillis()-debut) + "ms");
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
			System.out.println("A problem occurs with a connection " + e.getMessage());
			if ( connection != null )
				try {
					connection.close(); // if a problem occurs, the connection is removed
					listConnections.remove(connection);
				} catch ( SQLException ignore ) {}
			return false;

		}
		return true;
	}

	public Connection getConnection() {
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

	// Checking if connection are alive
	public void run() {
		Statement stmt = null;
		Integer numberOfMissingConnection;
		try {
			while(true) {
				Thread.sleep(62*60*1000);
				numberOfMissingConnection=0;
				for(Connection co : listConnections) {
					try {
						stmt = co.createStatement();
						stmt.execute("do 1");
					} catch (SQLException e) {
						listConnections.remove(co);
						numberOfMissingConnection++;
					} finally {
						if (stmt != null) {
							try {
								stmt.close();
							}catch (Exception e){}
						}
					}
				}
				// We add the missing connection
				for(Integer i=1;i<=numberOfMissingConnection;i++) {
					if(!init())
						break;
				}
			}
		}catch(InterruptedException e) {return;}
		
	}
}

