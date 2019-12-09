package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MsgReportFurnace extends Message {

	protected Integer id;
	
	@JsonCreator
	public MsgReportFurnace(@JsonProperty("id") Integer id) {
		super(MessageType.REPORTFURNACE);
		this.id=id;
	}

	public Integer getId() {
		return id;
	}

}
