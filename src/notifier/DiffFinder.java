package notifier;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.League;
import model.Match;

public class DiffFinder {
	static final Logger logger = LoggerFactory.getLogger(DiffFinder.class);
	
	public List<DiffData> getDiffs(List<League> newList, List<League> oldList){
		List<DiffData> leaguePairs = findDiffs(newList, oldList);
		
//		Match tst = new Match();
//		tst.setTeam1("sir");
//		tst.setTeam2("trak");
//		leaguePairs.add(new DiffData(tst, "0-0", "0-1"));
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
				boolean noChange = (scoreChanged || timeChanged);
				if(noChange) {
					continue;
				}
				diffs.add(new DiffData(mOld, mNew));
				if(scoreChanged && timeChanged){
					logger.debug("diff found "+mOld.getTeam1()+" vs "+mOld.getTeam2()+": score old-new "+mOld.getScore()+"/"+mNew.getScore()+" "
							+ " old time -> "+mOld.getTime()+", new time -> "+mNew.getTime());
					diffs.add(new DiffData(mOld, mNew));
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
	private boolean isFirstUpdateOfMatchStart(Match m1, Match m2) {
		return m1.getScore().indexOf("?")!=-1 && m2.getScore().indexOf("?")==-1 && m2.getScore().indexOf("1")==-1;
	}
	
	private boolean scoreChanged(Match m1, Match m2){
		return !m1.getScore().equals(m2.getScore()) && !isFirstUpdateOfMatchStart(m1, m2);
	}
	
	private boolean isTimeStatusChange(String time) {
		for(char digit='0'; digit<='9'; digit++) {
			if(time.indexOf(digit) != -1) {
				return false;
			}
		}
		return true;
	}
	
	private boolean timeChanged(Match m1, Match m2) {
		if(m1.getTime()==null || m2.getTime()==null) {
			return !(m1.getTime()==null && m2.getTime()==null);
		}
		return !m1.getTime().equals(m2.getTime()) && isTimeStatusChange(m2.getTime());
	}
	
}
