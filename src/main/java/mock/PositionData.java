package mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import common.MsgReportHeartBeat;
import common.MsgReportPosition;
import common.ToolSerialize;

public class PositionData {



	public void run() {
		System.out.println("TrackVision Mock Position :  launched!\n");
		new ConfigurationMock();

		while(true) {
			for (int i=0 ; i<8 ; i++) {
				try {
					normalData();
					Thread.sleep(ConfigurationMock.posX_freq_send);
					Thread.sleep(ConfigurationMock.posY_freq_send);
				}
				catch (InterruptedException e) {

				}
			}
		}
	}

	public void normalData() throws InterruptedException 
	{
		float x = 50;
		float y = 70;


		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  

	//	float random = (float)Math.random() * (max - min) + 60;
	//	float d = (float) Math.round(random * 1) / 1; 
		MsgReportPosition request = new MsgReportPosition(dtf.format(now), x,y);
		   Connector.contactServer(ToolSerialize.messageToJSON(request));
		System.out.print(dtf.format(now) + "  :   "  + x+" "+y+"\n");


	}
}