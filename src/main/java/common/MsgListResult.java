package common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MsgListResult extends Message {
	
	protected List<List<String>> listResult;
	
	@JsonCreator
	public MsgListResult(@JsonProperty("listResult") List<List<String>> listResult) {
		super(MessageType.LISTRESULT);
		this.listResult = listResult;
	}

	public List<List<String>> getListResult() {
		return listResult;
	}

}
