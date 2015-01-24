package parsing;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.League;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;


public class DataSaver extends Observable{
	private static final Logger logger = LoggerFactory.getLogger(DataSaver.class);
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
		
		setChanged();
		notifyObservers(mainList);
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
	
	@Override
	public synchronized void addObserver(Observer o) {
		try{
			super.addObserver(o);
		}catch (Throwable t){
			logger.warn("error no observer set for "+ listOfUrls.toString());
		}
	}
	
}
