package notifier;

import model.Match;

public class DiffData {
	private Match match;
	private String oldScore;
	private String newScore;
	
	public DiffData(Match matchName, String oldScore, String newsScore){
		this.match = matchName;
		this.oldScore = oldScore;
		this.newScore = newsScore;
	}

	public Match getMatch() {
		return match;
	}

	public String getOldScore() {
		return oldScore;
	}

	public String getNewScore() {
		return newScore;
	}
}
