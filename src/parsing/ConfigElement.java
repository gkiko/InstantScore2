package parsing;


public class ConfigElement {
	private String fileName, parser, period, timeUnit;
	private java.util.List<String> listOfUrls;

	public ConfigElement(String f, java.util.List<String> list, String p, String period, String timeUnit) {
		fileName = f;
		listOfUrls = list;
		parser = p;
		this.period = period;
		this.timeUnit = timeUnit;
	}

	public String getFileName() {
		return fileName;
	}

	public String getParser() {
		return parser;
	}

	public String getPeriod() {
		return period;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public java.util.List<String> getListOfUrls() {
		return listOfUrls;
	}

	public void setListOfUrls(java.util.List<String> listOfUrls) {
		this.listOfUrls = listOfUrls;
	}
	
	
}