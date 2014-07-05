package parsing;

import java.util.List;

public class ConfigElement {
	String fileName;
	String parameter;
	String parser;
	String period;
	String timeUnit;
	boolean observable;

	List<String> url;

	public boolean isObservable() {
		return observable;
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

	public String getParameter() {
		return parameter;
	}

	public java.util.List<String> getUrl() {
		return url;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public java.util.List<String> getListOfUrls() {
		return url;
	}

	public void setListOfUrls(java.util.List<String> listOfUrls) {
		this.url = listOfUrls;
	}

}