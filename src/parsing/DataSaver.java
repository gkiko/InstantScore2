package parsing;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;


public class DataSaver {
	private File file;
	private List<String> listOfUrls;
	private Parser p;
	
	public DataSaver(File file, List<String> listOfUrls, Parser p){
		this.file = file;
		this.listOfUrls = listOfUrls;
		this.p = p;
	}
	
	public void downloadParseSave() throws IOException{
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for(String url : listOfUrls) {
			JsonObject jsonObj = p.parse(url);
			JsonArray jsonArr = jsonObj.getJsonArray("data");
			for(JsonValue obj : jsonArr) {
				builder.add(obj);
			}
		}
		JsonArray resultArray = builder.build();
		JsonObject obj = Json.createObjectBuilder().add("data", resultArray).build();
		fileWrite(obj);
//		System.out.println(resultArray.toString());
//		System.out.println(jsonObj.toString());
	}
	
	private void fileWrite(JsonObject jsonObj) throws IOException{
		FileWriter writer = new FileWriter(file);
		try {
			writer.write(jsonObj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			writer.flush();
			writer.close();
		}
	}
}
