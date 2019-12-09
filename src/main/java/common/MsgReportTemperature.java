package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MsgReportTemperature extends Message {

	protected Integer id;
	protected float temperature;
	
	@JsonCreator
	public MsgReportTemperature(@JsonProperty("id")Integer id, @JsonProperty("temperature")float temperature) {
		super(MessageType.REPORTTEMPERATURE);
		this.id=id;
		this.temperature=temperature;
	}

	public Integer getId() {
		return id;
	}

	public float getTemperature() {
		return temperature;
	}

}
