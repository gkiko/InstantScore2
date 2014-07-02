package notifier;

public class DiffData {
	private String matchId;
	private String oldScore;
	private String newScore;
	
	public DiffData(String matchName, String oldScore, String newsScore){
		this.matchId = matchName;
		this.oldScore = oldScore;
		this.newScore = newsScore;
	}

	public String getMatchId() {
		return matchId;
	}

	public String getOldScore() {
		return oldScore;
	}

	public String getNewScore() {
		return newScore;
	}
}
