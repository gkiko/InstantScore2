package utils;

import java.util.Calendar;
import java.util.List;

import parsing.ConfigElement;
import parsing.ConfigObject;

public class ConfigUtils {
	public static void modifyUrlFields(ConfigObject config){
		int dayOffsetCounter = 0;
		List<String> listOfUrl;
		for(ConfigElement elem : config){
			listOfUrl = elem.getListOfUrls();
			for(int i=0;i<listOfUrl.size();i++){
				String url = listOfUrl.get(i);
				url = appendDate(url, dayOffsetCounter);
				listOfUrl.set(i, url);
				dayOffsetCounter++;
			}
		}
	}
	
	private static String appendDate(String str, int offset){
		Calendar cal = Utils.getDateWithOffset(offset);
		String dateStr = Utils.dateToString(cal);
		return str + dateStr;
	}
}
