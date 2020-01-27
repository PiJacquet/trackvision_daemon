package common.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectedObject {
	
	private Integer id;
	private String type;
	private Boolean state;
	private Integer apartmentId;
	private String macAddress;
	
	public ConnectedObject (ResultSet result) throws SQLException{
		id = result.getInt(1);
		type = result.getString(2);
		state = result.getBoolean(3);
		apartmentId = result.getInt(4);
		macAddress = result.getString(5);
	}
	
	public Integer getId() {
		return id;
	}
	
	public Boolean isOn() {
		return state;
	}

}
