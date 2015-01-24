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

public class BasicParser2 implements Parser {

	@Override
	public List<League> parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).timeout(5000).get();

		Element table = doc.select(".content").get(0);
		Elements leaguesAndMatches = table.select("div:not(.cal-wrap)");
		
		List<League> leagueList = new ArrayList<League>();
		for(Element leagueOrMatch : leaguesAndMatches){
			if(leagueOrMatch.hasClass("mt4")){
				League leagueObj = new League();
				String name = leagueOrMatch.select(".left").text();
				String date = leagueOrMatch.select(".right").text();
				leagueObj.setName(name);
				leagueObj.setDate(date);
				leagueObj.setMatches(new ArrayList<Match>());
				
				leagueList.add(leagueObj);
			}else if(leagueOrMatch.hasClass("row-gray")){
				Match matchData = new Match();
				String team1 = leagueOrMatch.select(".name").get(0).text();
				String team2 = leagueOrMatch.select(".name").get(1).text();
				String time = leagueOrMatch.select(".min").text();
				String score = leagueOrMatch.select(".sco").text();
				
				matchData.setTime(time);
				matchData.setTeam1(team1);
				matchData.setScore(score);
				matchData.setTeam2(team2);
				
				League lastInserted = leagueList.get(leagueList.size()-1);
				lastInserted.getMatches().add(matchData);
			}
		}
		return leagueList;
	}

}
