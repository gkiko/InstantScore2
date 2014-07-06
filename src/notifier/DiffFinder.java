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
				if(mOld.equals(mNew) && scoreChanged(mOld, mNew)){
					logger.debug("diff found "+mOld.getTeam1()+" vs "+mOld.getTeam2()+" score old-new "+mOld.getScore()+"/"+mNew.getScore());
					diffs.add(new DiffData(mNew, mOld.getScore(), mNew.getScore()));
				}
			}
		}
	}
	
	private boolean scoreChanged(Match m1, Match m2){
		return !m1.getScore().equals(m2.getScore());
	}
	
}
