package parsing;


class ConfigElement {
	String fileName, parser, period, timeUnit;
	java.util.List<String> listOfUrls;

	public ConfigElement(String f, java.util.List<String> list, String p, String period, String timeUnit) {
		fileName = f;
		listOfUrls = list;
		parser = p;
		this.period = period;
		this.timeUnit = timeUnit;
	}
}