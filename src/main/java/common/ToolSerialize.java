package common;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ToolSerialize {
	
	public static Message jsonToMessage(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> map  = mapper.readValue(json, new TypeReference<Map<String,Object>>(){});
			if(map.get("type").toString().equalsIgnoreCase(MessageType.REPORTFURNACE.toString())) {
				return mapper.readValue(json, MsgReportFurnace.class); 
			}
			if(map.get("type").toString().equalsIgnoreCase(MessageType.REPORTSMOKE.toString())) {
				return mapper.readValue(json, MsgReportSmoke.class); 
			}
			if(map.get("type").toString().equalsIgnoreCase(MessageType.REPORTTEMPERATURE.toString())) {
				return mapper.readValue(json, MsgReportTemperature.class); 
			}
			
			// Don't forget messages without a specific class!
			return mapper.readValue(json, Message.class);
		} 
		catch (JsonParseException e) {e.printStackTrace();} 
		catch (JsonMappingException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		return null;
	}
	
	public static String messageToJSON(Message object) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(object);
		}
		catch (JsonParseException e) { e.printStackTrace();}
		catch (JsonMappingException e) {e.printStackTrace();}
		catch (IOException e) { e.printStackTrace(); }
		return jsonString;
	}
	
		
}

