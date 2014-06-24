package parsing;
import java.io.IOException;

import javax.json.JsonObject;


public interface Parser {
	public JsonObject parse(String url) throws IOException;
}
