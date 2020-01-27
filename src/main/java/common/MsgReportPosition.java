package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MsgReportPosition extends Message{

	protected String date;
	protected float positionX, positionY;
	
	@JsonCreator
	public MsgReportPosition(@JsonProperty("Date") String date, @JsonProperty("PositionX") float positionX, @JsonProperty("PositionY") float positionY) {
		super(MessageType.REPORTPOSITION);
		this.date=date;
		this.positionX=positionX;
		this.positionY=positionY;
	}

		public String getDate() {
			return date;
		}

		public float getPositionX() {
			return positionX;
		}
		
		public float getPositionY() {
			return positionY;
		}
	
}

