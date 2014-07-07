package notifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgTextGenerator {
	static final Pattern pattern = Pattern.compile("[0-9]*");

	public String getMsgText(DiffData data) {
		String team1, score, team2;
		team1 = data.getNewMatch().getTeam1();
		team2 = data.getNewMatch().getTeam2();
		score = addBracketToScoreIfNeeded(data.getNewMatch().getScore(), data.getOldMatch().getScore());
		String timeUpdateString = getTimeUpdateText(data);
		return new StringBuilder(team1).append(" vs ").append(team2).append("  ").append(score).append(timeUpdateString).toString();
	}

	public String getTimeUpdateText(DiffData data) {
		String currentTime = data.getNewMatch().getTime();
		if(currentTime == null) {
			return ""; // This should never happen
		}
		if(currentTime.equals("FT")) {
			return "The match has finished.";
		}
		if(currentTime.equals("AET")) {
			return "The match has finished in extra time.";
		}
		if(currentTime.contains("p")) {
			return "The match has been postponed.";
		}
		if(currentTime.contains("b")) {
			return "The match has been abandoned.";
		}
		// case for penalties TODO
		return "";
	}
	
	public String addBracketToScoreIfNeeded(String sNew, String sOld) {
		String[] scoresNew = parseScore(sNew);
		String[] scoresOld = parseScore(sOld);
		
		StringBuilder sb = new StringBuilder();
		addBraketIfNeeded(scoresNew[0], scoresOld[0], sb);
		sb.append(" - ");
		addBraketIfNeeded(scoresNew[1], scoresOld[1], sb);
		return sb.toString();
	}

	public String[] parseScore(String score) {
		int index = 0;
		String[] arr = new String[2];
		String matched;
		Matcher matcher = pattern.matcher(score);
		while (matcher.find()) {
			matched = matcher.group();
			if(!matched.isEmpty()){
				arr[index] = matched;
				index = 1;
			}
		}
		return arr;
	}
	
	private void addBraketIfNeeded(String scoreNew, String scoreOld, StringBuilder sb){
		if(!scoreNew.equals(scoreOld)){
			sb.append("[").append(scoreNew).append("]");
		}else{
			sb.append(scoreNew);
		}
	}
	
	public static void main(String[] args) {
		MsgTextGenerator asd = new MsgTextGenerator();
		System.out.println(asd.addBracketToScoreIfNeeded("2-1", "1-0"));
	}
}
