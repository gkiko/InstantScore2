package notifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.League;
import model.Match;

public class DiffFinder {
	static final Logger logger = LoggerFactory.getLogger(DiffFinder.class);
	
	static final Pattern onlyUppercaseLetters = Pattern.compile("[A-Z]*");
	static final Pattern matchFirstUpdatePattern = Pattern.compile("[0|\\?][^0-9\\?]*[0|\\?]");
	
	public List<DiffData> getDiffs(List<League> newList, List<League> oldList){
		List<DiffData> leaguePairs = findDiffs(newList, oldList);
		return leaguePairs;
	}
	
	private List<DiffData> findDiffs(List<League> newList, List<League> oldList){
		List<DiffData> diffs = new ArrayList<DiffData>();
		for(League lNew : newList){
			for(League lOld : oldList){
				if(lNew.equals(lOld)){
					addMatchDiffs(lNew, lOld, diffs);
				}
			}
		}
		return diffs;
	}
	
	private void addMatchDiffs(League lNew, League lOLd, List<DiffData> diffs){
		for(Match mNew : lNew.getMatches()){
			for(Match mOld : lOLd.getMatches()){
				boolean sameMatch = mOld.equals(mNew);
				if(!sameMatch) {
					continue;
				}
				boolean scoreChanged = scoreChanged(mOld, mNew);
				boolean timeChanged = timeChanged(mOld, mNew);
				boolean anyChange = (scoreChanged || timeChanged);
				if(!anyChange) {
					continue;
				}
				diffs.add(new DiffData(mOld, mNew));
				if(scoreChanged && timeChanged){
					logger.debug("diff found "+mOld.getTeam1()+" vs "+mOld.getTeam2()+": score old-new "+mOld.getScore()+"/"+mNew.getScore()+" "
							+ " old time -> "+mOld.getTime()+", new time -> "+mNew.getTime());
				}
				else if(scoreChanged) {
					logger.debug("diff found "+mOld.getTeam1()+" vs "+mOld.getTeam2()+": score old-new "+mOld.getScore()+"/"+mNew.getScore());
				}
				else {
					logger.debug("diff found on time for match "+mNew+": old -> "+mOld.getTime()+", new -> "+mNew.getTime());
				}
			}
		}
	}
	
	/*
	 * Checks that it's not the ?-? -> 0-0  case, which is an update corresponding a match start.
	 */
	private boolean noGoal(Match m2) {
		Matcher matcher = matchFirstUpdatePattern.matcher(m2.getScore());
		return !matcher.find();
	}
	
	private boolean scoreChanged(Match m1, Match m2){
		return !m1.getScore().equals(m2.getScore()) && noGoal(m2);
	}
	
	// checks whether a string consists of just non-digits. If it's the case, then the time status has changed (it's either HT, FT or AET)
	private boolean isOnlyLetters(String currentTime) {
		Matcher matcher = onlyUppercaseLetters.matcher(currentTime);
		return matcher.find();
	}
	
	private boolean timeChanged(Match m1, Match m2) {
		if(m1.getTime()==null || m2.getTime()==null) {
			return !(m1.getTime()==null && m2.getTime()==null);
		}
		return !m1.getTime().equals(m2.getTime()) && !isOnlyLetters(m2.getTime());
	}
	
	public static void main(String[] args) {
		DiffFinder asd = new DiffFinder();
		Match m1 = new Match();
		m1.setScore("?-?");
		Match m2 = new Match();
		m2.setScore("0-1");
		System.out.println(asd.scoreChanged(m1, m2));
	}
		
}