package common;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Object_Attribut {


			private Integer id;
			private String type;
			private Integer state;
			private Integer id_apartment;
			private String mac;
			
			public Object_Attribut(Integer id) {
				this.id=id;
			}  
			
			public Object_Attribut(ResultSet result) throws SQLException {
				id=result.getInt(1);
				type=result.getString(2);
				state=result.getInt(3);
				id_apartment=result.getInt(4);
				mac=result.getString(5);
			}
			
		
			public int getId() {
				return id;
			}
			
	}
