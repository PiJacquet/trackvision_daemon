package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MsgReportSugarLevel extends Message {
	protected String date;
	protected float sugarLevelValue;
	
	@JsonCreator
	public MsgReportSugarLevel(@JsonProperty("date") String date, @JsonProperty("sugarLevelValue") float sugarLevelValue) {
		super(MessageType.REPORTSUGARLEVEL);
		this.date=date;
		this.sugarLevelValue=sugarLevelValue;
	}

		public String getDate() {
			return date;
		}

		public float getSugarLevelValue() {
			return sugarLevelValue;
		}
	
}
