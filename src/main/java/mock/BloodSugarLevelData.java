package mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BloodSugarLevelData {
	
    private double [] myTable= new double[2];
    
    public BloodSugarLevelData() throws InterruptedException {
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
		     double min =0.5 ;
		     double max = 1.26;
     
        	
        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
        	   
			 double random = Math.random() * (max - min) + 0.5;
			 double d = (double) Math.round(random * 100) / 100; 
			System.out.print(dtf.format(now) + "  :   "  + d+"\n");
			Thread.sleep(ConfigurationMock.sugarLevel_freq_send);

	}
	public void anormalData2() throws InterruptedException
	{
		     double min1 = 0.3;
		     double max1 = 0.5;
		     double min2 = 1.26;
	         double max2= 2;
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
 	       LocalDateTime now = LocalDateTime.now(); 
		     double random1 = (Math.random() * (max1 - min1) + 0.3) ;
		     double random2 = (double)(Math.random() * (max2 - min2) + 1.26) ;
			 double d1 = (double) Math.round(random1 * 100) / 100; 
			 double d2 = (double) Math.round(random2 * 100) / 100;
		   myTable[0]= d1;
		   myTable[1]=d2;
			 double val = myTable[(int)(Math.random()*myTable.length)];
		   System.err.print(dtf.format(now) + "  :   "   + val+"\n");
		   Thread.sleep(ConfigurationMock.sugarLevel_freq_send);
	}
    
    
}
