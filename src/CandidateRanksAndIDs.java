
public class CandidateRanksAndIDs {
	
	private int candidateID;
	private int candidateRank;
	
	public CandidateRanksAndIDs(int id, int rank) {
		this.candidateID = id;
		this.candidateRank = rank;
	}
	
	
	public CandidateRanksAndIDs(String[] candidateStringArray) {
		this.candidateID = Integer.valueOf(candidateStringArray[1]);
	}

	public int getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(int candidateID) {
		this.candidateID = candidateID;
	}

	public int getCandidateRank() {
		return candidateRank;
	}

	public void setCandidateRank(int candidateRank) {
		this.candidateRank = candidateRank;
	}


	
	
	

}
