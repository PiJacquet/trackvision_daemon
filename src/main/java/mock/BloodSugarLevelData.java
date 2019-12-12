package mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import common.MsgReportSugarLevel;
import common.ToolSerialize;

public class BloodSugarLevelData {
	
    private float[] myTable= new float[2];
    
    public BloodSugarLevelData() throws InterruptedException {
    
    	System.out.println("TrackVision Mock :  launched!\n");
   	        new ConfigurationMock();
   		while(true) {
   			for(int v =0 ; v<2; v++) {
   			normalData2();
   			}
   			anormalData2(); 
   		}
    }
    
    public void normalData2() throws InterruptedException 
	{
    	        float min =(float)0.5 ;
    	       float max = (float)1.26;
     
        	
        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
        	   
			 float random = (float) (Math.random() * (max - min) + 0.5);
			 float d = (float) Math.round(random * 100) / 100; 
			 System.out.println(dtf.format(now) + "  :   "  + d);
			 MsgReportSugarLevel request = new MsgReportSugarLevel(dtf.format(now), d);
			 Connector.contactServer(ToolSerialize.messageToJSON(request));
			
			Thread.sleep(ConfigurationMock.sugarLevel_freq_send);

	}
	public void anormalData2() throws InterruptedException
	{
		float min1 =(float) 0.3;
		float max1 = (float)0.5;
		float min2 = (float)1.26;
		float max2= (float)2;
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
 	       LocalDateTime now = LocalDateTime.now(); 
		     float random1 = (float) (Math.random() * (max1 - min1) + 0.3) ;
		     float random2 = (float)(Math.random() * (max2 - min2) + 1.26) ;
			 float d1 = (float) Math.round(random1 * 100) / 100; 
			 float d2 = (float) Math.round(random2 * 100) / 100;
		   myTable[0]= d1;
		   myTable[1]=d2;
			 float val = myTable[(int)(Math.random()*myTable.length)];
			 MsgReportSugarLevel request = new MsgReportSugarLevel(dtf.format(now), val);
			 Connector.contactServer(ToolSerialize.messageToJSON(request));
		   System.err.println(dtf.format(now) + "  :   "   + val);
		   Thread.sleep(ConfigurationMock.sugarLevel_freq_send);
	}
    
    
}
