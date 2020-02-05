package common.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectedObject {
	
	private Integer id;
	private String type;
	private Boolean state;
	private Integer apartmentId;
	private String macAddress;
	private String nickname;
	private Integer watchMinutesOn;
	private Integer watchMinutesOff;
	
	public ConnectedObject (ResultSet result) throws SQLException{
		id = result.getInt(1);
		type = result.getString(2);
		state = result.getBoolean(3);
		apartmentId = result.getInt(4);
		macAddress = result.getString(5);
		nickname = result.getString(6);
		watchMinutesOn=result.getInt(7);
		watchMinutesOff=result.getInt(8);
	}
	
	public ConnectedObject(Integer id, String type, Boolean state, Integer apartmentId, String macAddress,
			Integer watchMinutesOn, Integer watchMinutesOff) {
		this.id = id;
		this.type = type;
		this.state = state;
		this.apartmentId = apartmentId;
		this.macAddress = macAddress;
		this.watchMinutesOn = watchMinutesOn;
		this.watchMinutesOff = watchMinutesOff;
	}

	public Integer getId() {
		return id;
	}
	
	public Integer getWatchTime() {
		if(state)
			return watchMinutesOn;
		else
			return watchMinutesOff;
	}
	
	public String getInactivityMessage() {
		return "Inactivity exceed (state "+ (state? "ON" : "OFF") + " : " + (state? watchMinutesOn : watchMinutesOff)
				+ "mn) : ";
	}
}
