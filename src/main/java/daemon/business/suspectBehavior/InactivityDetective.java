package daemon.business.suspectBehavior;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import common.beans.ConnectedObject;
import daemon.CacheReport;
import daemon.ConfigurationDaemon;

public class InactivityDetective{
	
	private CacheReport cache;
	private List<ConnectedObject> listObjects;
	
	public InactivityDetective(CacheReport cache) {
		this.cache=cache;
	}

	public void investiguate() throws IOException {
		Timestamp lastTime;
		Instant intervalTimeOn = Instant.now().minus(Duration.ofHours(1));
		Instant intervalTimeOff = Instant.now().minus(Duration.ofDays(7));
		for(ConnectedObject object : listObjects) {
			lastTime = cache.getLastReport(object.getId()).getTime();
			if(object.isOn()) {
				if(lastTime.before(Date.from(intervalTimeOn))){
					addMalfunction(object.getId(), "The object is inactive");
				}
			}
			else {
				if(lastTime.before(Date.from(intervalTimeOff))){
					addMalfunction(object.getId(), "The object is inactive");
				}
			}
		}
	}

	public void refreshListObjects() throws IOException {
		listObjects = new ArrayList<ConnectedObject>();
		Connection connection = ConfigurationDaemon.connectionPool.getConnection();
		try {
			// We retrieve the objects
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Objects");
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				listObjects.add(new ConnectedObject(result));
			}
			ConfigurationDaemon.connectionPool.closeConnection(connection);
		}
		catch(Exception e) {
			ConfigurationDaemon.connectionPool.closeConnection(connection);
			throw new IOException("An error occured while retrieving the objects : " + e.getMessage());
		}
	}
	
	private void addMalfunction(Integer id, String message) throws IOException {
		Connection connection = ConfigurationDaemon.connectionPool.getConnection();
		try {
			// We insert the alert
			PreparedStatement stmt = connection.prepareStatement("insert into Malfunctions(State_Malfunction, Date_Malfunction,Message_Malfunction,ID_Object) values(1,NOW(),?,?);");
			stmt.setString(1, message);
			stmt.setInt(2, id);
			stmt.executeUpdate();
			ConfigurationDaemon.connectionPool.closeConnection(connection);
		}
		catch(Exception e) {
			ConfigurationDaemon.connectionPool.closeConnection(connection);
			throw new IOException("An error occured while inserting the malfunction : " + e.getMessage());
		}
	}

}
