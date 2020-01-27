package common;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

	protected MessageType type;
	protected Timestamp time;
	
	@JsonCreator
	public Message(@JsonProperty("type") MessageType type) {
		this.type=type;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Timestamp time) {
		this.time=time;
	}
}
