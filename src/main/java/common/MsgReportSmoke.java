package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MsgReportSmoke extends Message {
	
	protected Integer id;
	// We represent the smoke concentration by a value between 0 and 100, if >50, fire.
	protected Integer smokeValue;
	
	@JsonCreator
	public MsgReportSmoke(@JsonProperty("id") Integer id, @JsonProperty("smokeValue") Integer smokeValue) {
		super(MessageType.REPORTSMOKE);
		this.id=id;
		this.smokeValue=smokeValue;
	}

	public Integer getId() {
		return id;
	}

	public Integer getSmokeValue() {
		return smokeValue;
	}

}
