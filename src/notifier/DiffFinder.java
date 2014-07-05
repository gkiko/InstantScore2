package notifier;

import java.util.ArrayList;
import java.util.List;

import model.League;
import model.Match;

public class DiffFinder {
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
				if(mOld.equals(mNew) && scoreChanged(mOld, mNew)){
					System.out.println("diff found "+mOld.getTeam1()+" vs "+mOld.getTeam2()+" score old-new "+mOld.getScore()+"/"+mNew.getScore());
					diffs.add(new DiffData(mNew, mOld.getScore(), mNew.getScore()));
				}
			}
		}
	}
	
	private boolean scoreChanged(Match m1, Match m2){
		return !m1.getScore().equals(m2.getScore());
	}
	
}
