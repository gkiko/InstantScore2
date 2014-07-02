package parsing;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.League;

import com.google.gson.Gson;


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
		Gson gson = new Gson();
		List<League> list;
		List<League> mainList = new ArrayList<League>();
		for(String url : listOfUrls){
			list = p.parse(url);
			mainList.addAll(list);
		}
		String json = gson.toJson(mainList);
		fileWrite(json);
	}
	
	private void fileWrite(String jsonstr) throws IOException{
		FileWriter writer = new FileWriter(file);
		try {
			writer.write(jsonstr);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			writer.flush();
			writer.close();
		}
	}
	
}
