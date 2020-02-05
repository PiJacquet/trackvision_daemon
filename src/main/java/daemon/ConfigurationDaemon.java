package daemon;

import java.io.InputStream;
import java.util.Properties;

import common.JDBCConnectionPool;

public class ConfigurationDaemon {
	
	public static Boolean startup = true;
	
	public static JDBCConnectionPool connectionPool;
	
	public static String db_url;
	public static String db_name;
	public static String db_user;
	public static String db_pwd;
	public static Integer server_port;
	
	public static Integer inactivityscheduler;
	public static Integer cacheSize;
	
	public static CacheReport cache;
	
	public ConfigurationDaemon() {
		
		Properties properties=new Properties();
		try {
			InputStream input = getClass().getClassLoader().getResourceAsStream("configDaemon.properties");
			properties.load(input);
			server_port = Integer.parseInt(properties.getProperty("server_port"));
			db_url = properties.getProperty("db_url");
			db_name = properties.getProperty("db_name");
			db_user = properties.getProperty("db_user");
			db_pwd = properties.getProperty("db_pwd");
			inactivityscheduler = Integer.parseInt(properties.getProperty("inactivityscheduler"));
			cacheSize = Integer.parseInt(properties.getProperty("cachesize"));
		} catch (Exception e) {
			System.out.println();
			System.out.println("#Error while loading the config file : " + e.getMessage());
		}
		
		cache = new CacheReport(cacheSize);
		connectionPool = new JDBCConnectionPool();
		new Thread(connectionPool).start();
	}

}
