package parsing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.League;
import model.Match;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BasicParser implements Parser {

	@Override
	public List<League> parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).timeout(5000).get();

		Elements leagues = doc.select(".league-table:not(.mtn)");

		List<League> leagueList = new ArrayList<League>();
		for (Element league : leagues) {
			Elements leagueData = league.select("tr");

			League leagueObj = new League();
			List<Match> matcheList = new ArrayList<Match>();

			Element header = leagueData.first();
			String headerText = header.select(".league").text();
			String date = header.select(".date").text();
			leagueObj.setName(headerText);
			leagueObj.setDate(date);

			for (int i = 1; i < leagueData.size(); i++) {
				Element element = leagueData.get(i);
				Match matchData = new Match();

				String time = element.select(".fd").text();
				String t1 = element.select(".fh").text();
				String score = element.select(".fs").text();
				String t2 = element.select(".fa").text();

				matchData.setTime(time);
				matchData.setTeam1(t1);
				matchData.setScore(score);
				matchData.setTeam2(t2);
				matcheList.add(matchData);
			}
			leagueObj.setMatches(matcheList);
			leagueList.add(leagueObj);
		}

		return leagueList;
	}
}
