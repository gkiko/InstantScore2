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
	public int hashCode() {
		final int prime = 31;
		int result1 = 1;
		int result2 = 1;
		result1 = prime * result1 + ((team1 == null) ? 0 : team1.hashCode());
		result2 = prime * result2 + ((team2 == null) ? 0 : team2.hashCode());
		return result1+result2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Match)){
			return false;
		}
		Match match = (Match) obj;
		return (team1.equals(match.team1) && team2.equals(match.team2)) || (team1.equals(match.team2) && team2.equals(match.team1));
	}
}
