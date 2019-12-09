package mock;

public class MockerLaunch {

	public static void main(String[] args) throws InterruptedException{
		//new Mocker();
		
		Heart_Rate_Data data= new Heart_Rate_Data();
		
		for( int j=0 ; j<1441;  j++) {
		for (int i=0 ; i<8 ; i++) {
		data.normalData();
		Thread.sleep(ConfigurationMock.freq_send);
		}
			data.anormalData();
			Thread.sleep(ConfigurationMock.freq_send);
		}
	}
	}



