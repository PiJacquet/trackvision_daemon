package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

	protected MessageType type;
	
	@JsonCreator
	public Message(@JsonProperty("type") MessageType type) {
		this.type=type;
	}
	
	public MessageType getType() {
		return type;
	}
}
