package mock;

import java.util.List;

import common.MsgReportSmoke;
import common.ToolSerialize;

public class CategorySmoke extends CategoryObject{

	public CategorySmoke(String referencedName, List<String> listObjects) {
		super(referencedName, listObjects);
	}

	public CategorySmoke() {
		super();
	}

	@Override
	public void launchAlert(Integer id) {
		int smokeValue = 20;
		MsgReportSmoke update = new MsgReportSmoke(id, smokeValue);
		Connector.contactServer(ToolSerialize.messageToJSON(update));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
	}

}
