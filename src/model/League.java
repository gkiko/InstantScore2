package model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkiko on 6/29/14.
 */
public class League {
	@SerializedName("header")
	String name;

	@SerializedName("date")
	String date;

	@SerializedName("matches")
	List<Match> matches;

	public void setName(String name) {
		this.name = name;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public List<Match> getMatches() {
		return matches;
	}

}
