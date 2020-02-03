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

/*
 * The goal of the inactivity detective is to spot the inactive objects based on their last emission
 * It used the cache which contains all the latest reports received.
 */
public class InactivityDetective{

	private CacheReport cache;
	private List<ConnectedObject> listObjects;

	public InactivityDetective(CacheReport cache) {
		this.cache=cache;
	}

	// To be periodically called by the Detective Scheduler
	public Integer investiguateAll() throws IOException {
		Integer numberOfAlert = 0;
		for(ConnectedObject object : listObjects) {
			if(isActive(object, true))
				numberOfAlert++;
		}
		return numberOfAlert;
	}


	public boolean isActive(ConnectedObject object, Boolean launchAlert) throws IOException {
		Timestamp lastTime;
		if(cache.getLastReport(object.getId())==null && cache.getLaunchDate().before(Date.from(Instant.now().minus(Duration.ofMinutes(object.getWatchTime()))))) {
			//if the object was never seen and the cache is launched for a greater amount of time than the tolerated one the db : we launch an alert
			//To consider the cache launch time avoid to launch inactivity alerts at the beginning of live of the cache
			if(launchAlert)
				addMalfunction(object.getId(), "The object was never seen");
			return false;
		}
		else {
			lastTime = cache.getLastReport(object.getId()).getTime();
			if(lastTime.before(Date.from(Instant.now().minus(Duration.ofMinutes(object.getWatchTime()))))){
				// If the object was last seen for a greater amount of time that the specified on DB, we raise an alert
				if(launchAlert)
					addMalfunction(object.getId(), object.getInactivityMessage() + " : "  + Duration.between(lastTime.toInstant(), Instant.now()));
				return false;
			}
		}
		return true;
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
			// First, we check if an alert was not already launched for this inactivity
			PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Malfunctions WHERE ID_Object=? AND Type_Malfunction=1 AND State_Malfunction=1;");
			stmt.setInt(1, id);
			ResultSet result = stmt.executeQuery();
			result.next();
			if(result.getInt(1)==0) {
				// We insert the alert if there is none previous one active
				stmt = connection.prepareStatement("INSERT INTO Malfunctions(State_Malfunction, Date_Malfunction,Message_Malfunction,ID_Object) values(1,NOW(),?,?);");
				stmt.setString(1, message);
				stmt.setInt(2, id);
				stmt.executeUpdate();
			}
			ConfigurationDaemon.connectionPool.closeConnection(connection);
		}
		catch(Exception e) {
			ConfigurationDaemon.connectionPool.closeConnection(connection);
			throw new IOException("An error occured while inserting the malfunction : " + e.getMessage());
		}
	}

}
