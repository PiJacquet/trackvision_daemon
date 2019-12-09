package mock;

import java.util.List;

import common.MsgReportTemperature;
import common.ToolSerialize;

public class CategoryTemperature extends CategoryObject{

	public CategoryTemperature(String referencedName, List<String> listObjects) {
		super(referencedName, listObjects);
	}

	public CategoryTemperature() {
		super();
	}

	public void launchAlert(Integer id) {
		float temperature = (float) 20.0;
		MsgReportTemperature update = new MsgReportTemperature(id, temperature);
		Connector.contactServer(ToolSerialize.messageToJSON(update));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
	}

}
