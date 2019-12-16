package mock;

import java.io.InputStream;
import java.util.Properties;

import common.JDBCConnectionPool;

public class ConfigurationMock {
	
	public static Boolean startup = true;
	public static JDBCConnectionPool connectionPool;
	
	public static String server_url;
	public static Integer server_port;
	public static Integer heart_Rate_freq_send;
	public static Integer sugarLevel_freq_send;
	public ConfigurationMock() {
		
		Properties properties=new Properties();
		try {
			InputStream input = getClass().getClassLoader().getResourceAsStream("configMock.properties");
			properties.load(input);
			server_url = properties.getProperty("server_url");
			server_port = Integer.parseInt(properties.getProperty("client_port"));
			heart_Rate_freq_send= Integer.parseInt(properties.getProperty("heart_Rate_freq_send"));
			sugarLevel_freq_send= Integer.parseInt(properties.getProperty("sugarLevel_freq_send"));
		} catch (Exception e) {
			System.out.println("#Error while loading the config file : " + e.getMessage());
		}
		

	}

}
