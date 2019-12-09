package mock;

import java.util.List;

import common.MsgReportFurnace;
import common.ToolSerialize;

public class CategoryFurnace extends CategoryObject{

	public CategoryFurnace(String referencedName, List<String> listObjects) {
		super(referencedName, listObjects);
	}
	
	public CategoryFurnace() {
		super();
	}
	
	public void launchAlert(Integer id) {	
		MsgReportFurnace update = new MsgReportFurnace(id);
		Connector.contactServer(ToolSerialize.messageToJSON(update));
	}

}
