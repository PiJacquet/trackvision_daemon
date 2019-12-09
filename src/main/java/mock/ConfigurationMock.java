package mock;

import java.io.InputStream;
import java.util.Properties;

import common.JDBCConnectionPool;

public class ConfigurationMock {
	
	public static Boolean startup = true;
	
	public static JDBCConnectionPool connectionPool;
	
	public static String server_url;
	public static Integer client_port;
	
	public ConfigurationMock() {
		
		Properties properties=new Properties();
		try {
			InputStream input = getClass().getClassLoader().getResourceAsStream("configMock.properties");
			properties.load(input);
			server_url = properties.getProperty("server_url");
			client_port = Integer.parseInt(properties.getProperty("client_port"));
		} catch (Exception e) {
			System.out.println("#Error while loading the config file : " + e.getMessage());
		}
		
		connectionPool = new JDBCConnectionPool();
	}

}
