package parsing;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


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
		JsonArray array = new JsonArray();
		for(String url : listOfUrls) {
			JsonArray jsonArr = p.parse(url);
			for(JsonElement obj : jsonArr) {
				array.add(obj);
			}
		}
		fileWrite(array);
	}
	
	private void fileWrite(JsonArray jsonObj) throws IOException{
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
