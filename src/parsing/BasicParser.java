package parsing;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BasicParser implements Parser {

	@Override
	public JsonObject parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();

		Elements leagues = doc.select(".league-table:not(.mtn)");

		JsonArrayBuilder jsonArrMain = Json.createArrayBuilder();
		for (Element league : leagues) {
			Elements leagueData = league.select("tr");

			JsonObjectBuilder jsonLeagueObj = Json.createObjectBuilder();
			JsonArrayBuilder jsonLeagueMatches = Json.createArrayBuilder();

			Element header = leagueData.first();
			String headerText = header.select(".league").text();
			String date = header.select(".date").text();
			jsonLeagueObj.add("header", headerText);
			jsonLeagueObj.add("date", date);

			for (int i = 1; i < leagueData.size(); i++) {
				Element element = leagueData.get(i);
				JsonObjectBuilder jsonMatchData = Json.createObjectBuilder();

				String time = element.select(".fd").text();
				String t1 = element.select(".fh").text();
				String score = element.select(".fs").text();
				String t2 = element.select(".fa").text();

				jsonMatchData.add("time", time);
				jsonMatchData.add("t1", t1);
				jsonMatchData.add("score", score);
				jsonMatchData.add("t2", t2);
				jsonLeagueMatches.add(jsonMatchData);
				jsonLeagueObj.add("matches", jsonLeagueMatches);
			}
			jsonArrMain.add(jsonLeagueObj);
		}

		return Json.createObjectBuilder().add("data", jsonArrMain).build();
	}
}
