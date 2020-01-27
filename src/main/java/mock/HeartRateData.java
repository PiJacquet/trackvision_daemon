package mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import common.MsgReportHeartBeat;
import common.ToolSerialize;


public class HeartRateData implements Runnable {
	
    private float [] myTable= new float[2];
    private int [] Table= new int [3];
			
			public void run() {
				System.out.println("TrackVision Mock HeartRate :  launched!\n");
				new ConfigurationMock();
				
					while(true) {
					for (int i=0 ; i<8 ; i++) {
						try {
					normalData();
					Thread.sleep(ConfigurationMock.heart_Rate_freq_send);
						}
						catch (InterruptedException e) {
							
						}
					}
					try {
						Table[0]=1;
						Table[1]=2;
						Table[2]=5;
							 float val = Table[(int)(Math.random()*Table.length)];
							 for (int i=0; i<val ; i++){		 
					anormalData();
					Thread.sleep(ConfigurationMock.heart_Rate_freq_send);
							 }
					}
					catch (InterruptedException e) {
						
					}
			}
				
			}
			public void normalData() throws InterruptedException 
			{
				     float min = 60;
				     float max = 70;
		     
		        	
		        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		            LocalDateTime now = LocalDateTime.now();  
		        	   
		            float random = (float)Math.random() * (max - min) + 60;
					 float d = (float) Math.round(random * 1) / 1; 
					 MsgReportHeartBeat request = new MsgReportHeartBeat(dtf.format(now), d);
				  //   Connector.contactServer(ToolSerialize.messageToJSON(request));
					 System.out.print(dtf.format(now) + "  :   "  + d+"\n");
				

			}
			public void anormalData() throws InterruptedException
			{
				     float min1 = 50;
				     float max1 = 60;
				     float min2 = 70;
				     float max2= 80;
				   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
	     	       LocalDateTime now = LocalDateTime.now(); 
	     	         float random1 = (float)(Math.random() * (max1 - min1) + 50) ;
				     float random2 = (float)(Math.random() * (max2 - min2) + 70) ;
				     float d1 = (float) Math.round(random1 * 1) / 1; 
					 float d2 = (float) Math.round(random2 * 1) / 1;
				   myTable[0]= d1;
				   myTable[1]=d2;
					 float val = myTable[(int)(Math.random()*myTable.length)];
					 
					 MsgReportHeartBeat request = new MsgReportHeartBeat(dtf.format(now), val);
					// Connector.contactServer(ToolSerialize.messageToJSON(request));
				     System.err.print(dtf.format(now) + "  :   "   + val+"\n");
				  
			}
			
				
	}
