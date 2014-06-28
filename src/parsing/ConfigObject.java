package parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ConfigObject implements Iterable<ConfigElement> {
	private ArrayList<ConfigElement> list = new ArrayList<ConfigElement>();

	public void createFrom(String configFilePath) throws FileNotFoundException {
		JsonReader reader = Json.createReader(new FileReader(new File(configFilePath)));
		JsonObject dataList = reader.readObject();

		String fileName, parser, period, timeUnit;
		JsonArray arr = dataList.getJsonArray("items");
		for (int i = 0; i < arr.size(); i++) {
			JsonObject obj = arr.getJsonObject(i);
			fileName = obj.getString("fileName");
			parser = obj.getString("parser");
			period = obj.getString("period");
			timeUnit = obj.getString("timeUnit");
			String[] url = createArrayFrom(obj.getJsonArray("url"));
			list.add(new ConfigElement(fileName, Arrays.asList(url), parser, period, timeUnit));
		}
	}
	
	private String[] createArrayFrom(JsonArray jsonArr){
		String[] arr = new String[jsonArr.size()];
		for(int i=0;i<arr.length;i++){
			arr[i] = jsonArr.getString(i);
		}
		return arr;
	}
	
	public void setAt(ConfigElement elem, int i){
		list.set(i, elem);
	}
	
	@Override
	public Iterator<ConfigElement> iterator() {
		return list.iterator();
	}
}
