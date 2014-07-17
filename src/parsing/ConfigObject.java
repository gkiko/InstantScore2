package parsing;

import java.util.Iterator;
import java.util.List;

public class ConfigObject implements Iterable<ConfigElement> {
	private List<ConfigElement> items;
	
	public String getFileName(String fileParameter){
		String fileName = "";
		for(ConfigElement elem : items){
			if(elem.getParameter().equals(fileParameter)){
				fileName = elem.getFileName();
			}
		}
		return fileName;
	}
	
	@Override
	public Iterator<ConfigElement> iterator() {
		return items.iterator();
	}
}
