package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MsgReportHeartBeat extends Message{

	protected String date;
	protected float hearBeatValue;
	
	@JsonCreator
	public MsgReportHeartBeat(@JsonProperty("Date") String date, @JsonProperty("HearBeatValue") float hearBeatValue) {
		super(MessageType.REPORTHEARTBEAT);
		this.date=date;
		this.hearBeatValue=hearBeatValue;
	}

		public String getDate() {
			return date;
		}

		public float getHearBeatValue() {
			return hearBeatValue;
		}
	
}
