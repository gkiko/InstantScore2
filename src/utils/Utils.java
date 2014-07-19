package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
	private static String pattern = "yyyy-MM-dd";
	
	public static Calendar getDateWithOffset(int offset){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, offset);
		return cal;
	}
	
	public static String dateToString(Calendar cal){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(cal.getTime());
	}
}
