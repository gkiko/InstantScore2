package parsing;


class ConfigElement {
	String fileName, parser;
	java.util.List<String> listOfUrls;

	public ConfigElement(String f, java.util.List<String> list, String p) {
		fileName = f;
		listOfUrls = list;
		parser = p;
	}
}