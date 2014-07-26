package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Utils {
	private static String pattern = "yyyy-MM-dd";
	private static Random random = new Random();
	
	public static String getDateToday(){
		Calendar curDate = getDateWithOffset(0);
		return dateToString(curDate);
	}
	
	public static Calendar getDateWithOffset(int offset){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, offset);
		return cal;
	}
	
	public static String dateToString(Calendar cal){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(cal.getTime());
	}
	
	public static String generateCode(int length){
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    return new String(digits);
	}
	
	public static String removeQuotesAround(String str){
		return str.substring(1, str.length()-1);
	}
}
