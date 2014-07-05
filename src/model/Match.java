package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkiko on 6/29/14.
 */
public class Match {
	@SerializedName("time")
	String time;

	@SerializedName("t1")
	String team1;

	@SerializedName("score")
	String score;

	@SerializedName("t2")
	String team2;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Match)){
			return false;
		}
		Match match = (Match) obj;
		return team1.equals(match.team1) && team2.equals(match.team2);
	}
}
