package parsing;
import java.io.IOException;
import java.util.List;

import model.League;


public interface Parser {
	public List<League> parse(String url) throws IOException;
}
