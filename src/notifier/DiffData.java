package notifier;

import model.Match;

public class DiffData {
	private Match oldMatch;
	private Match newMatch;
	
	public DiffData(Match oldMatch, Match newMatch){
		this.oldMatch = oldMatch;
		this.newMatch = newMatch;
	}

	public Match getOldMatch() {
		return oldMatch;
	}

	public Match getNewMatch() {
		return newMatch;
	}
	
	

}
