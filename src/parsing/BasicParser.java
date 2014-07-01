package parsing;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class BasicParser implements Parser {

	@Override
	public JsonArray parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();

		Elements leagues = doc.select(".league-table:not(.mtn)");

		JsonArray jsonArrMain = new JsonArray();
		for (Element league : leagues) {
			Elements leagueData = league.select("tr");

			JsonObject jsonLeagueObj = new JsonObject();
			JsonArray jsonLeagueMatches = new JsonArray();

			Element header = leagueData.first();
			String headerText = header.select(".league").text();
			String date = header.select(".date").text();
			jsonLeagueObj.addProperty("header", headerText);
			jsonLeagueObj.addProperty("date", date);

			for (int i = 1; i < leagueData.size(); i++) {
				Element element = leagueData.get(i);
				JsonObject jsonMatchData = new JsonObject();

				String time = element.select(".fd").text();
				String t1 = element.select(".fh").text();
				String score = element.select(".fs").text();
				String t2 = element.select(".fa").text();

				jsonMatchData.addProperty("time", time);
				jsonMatchData.addProperty("t1", t1);
				jsonMatchData.addProperty("score", score);
				jsonMatchData.addProperty("t2", t2);
				jsonLeagueMatches.add(jsonMatchData);
			}
			jsonLeagueObj.add("matches", jsonLeagueMatches);
			jsonArrMain.add(jsonLeagueObj);
		}

		return jsonArrMain;
	}
}
