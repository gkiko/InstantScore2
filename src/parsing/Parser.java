package parsing;
import java.io.IOException;

import com.google.gson.JsonArray;


public interface Parser {
	public JsonArray parse(String url) throws IOException;
}
