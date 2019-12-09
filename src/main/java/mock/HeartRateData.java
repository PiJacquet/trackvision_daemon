package mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class HeartRateData {
	
    private double [] myTable= new double[2];
		
			public HeartRateData() throws InterruptedException
			{
				new ConfigurationMock();
				
					while(true) {
					for (int i=0 ; i<8 ; i++) {
					normalData();
					}
					anormalData();
					}	
			}
			
			public void normalData() throws InterruptedException 
			{
				     double min = 60;
				     double max = 70;
		     
		        	
		        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		            LocalDateTime now = LocalDateTime.now();  
		        	   
					 double random = Math.random() * (max - min) + 60;
					 double d = (double) Math.round(random * 1) / 1; 
					System.out.print(dtf.format(now) + "  :   "  + d+"\n");
					Thread.sleep(ConfigurationMock.heart_Rate_freq_send);

			}
			public void anormalData() throws InterruptedException
			{
				     double min1 = 50;
				     double max1 = 60;
				     double min2 = 70;
			         double max2= 80;
				   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	     	       LocalDateTime now = LocalDateTime.now(); 
				     double random1 = (Math.random() * (max1 - min1) + 50) ;
				     double random2 = (double)(Math.random() * (max2 - min2) + 70) ;
					 double d1 = (double) Math.round(random1 * 1) / 1; 
					 double d2 = (double) Math.round(random2 * 1) / 1;
				   myTable[0]= d1;
				   myTable[1]=d2;
					 double val = myTable[(int)(Math.random()*myTable.length)];
				   System.err.print(dtf.format(now) + "  :   "   + val+"\n");
				   Thread.sleep(ConfigurationMock.heart_Rate_freq_send);
			}
				
	}
