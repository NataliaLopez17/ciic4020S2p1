import classes.ArrayList;
import interfaces.List;

public class Ballot {
	private int ballotNumber;

	public List<Ballot> voteList2 = Election.voteList;

	private List<CandidateRanksAndIDs> candidateList;

	public List<CandidateRanksAndIDs> getCandidateList() {
		return candidateList;
	}

	public Ballot(String[] ballotStringArray) {
		this.ballotNumber = Integer.valueOf(ballotStringArray[0]);
		candidateIdAndRank(ballotStringArray);
	}

	// ---------------------------------methods-----------------------------------

	public void candidateIdAndRank(String[] ballotStringArray) {
		candidateList = new ArrayList<>();
		for (int i = 1; i < ballotStringArray.length; i++) {
			String ID = ballotStringArray[i].substring(0, ballotStringArray[i].indexOf(":"));
			String rank = ballotStringArray[i].substring(ballotStringArray[i].indexOf(":") + 1);
			candidateList.add(new CandidateRanksAndIDs(Integer.parseInt(ID), Integer.parseInt(rank)));
		}
	}

	/**
	 * gets the ballot number
	 * 
	 * @param none
	 * 
	 * @return number of the ballot
	 */
	public int getBallotNum() {
		return ballotNumber;
	}

	/**
	 * gets the rank indicated a candidate
	 * 
	 * @param candidateId
	 * 
	 * @return returns the rank if found, -1 otherwise
	 */
	public int getRankByCandidate(int candidateId) {
		for (int i = 0; i < candidateList.size(); i++) {
			if (candidateList.get(i).getCandidateID() == candidateId) {
				return candidateList.get(i).getCandidateRank();
			}

		}
		return -1;
	}

	/**
	 * gets the candidate given a rank
	 * 
	 * @param candidate rank
	 * 
	 * @return the rank of the candidate, -1 otherwise
	 */
	public int getCandidateByRank(int rank) {
		for (int i = 0; i < candidateList.size(); i++) {
			if (candidateList.get(i).getCandidateRank() == rank) {
				return candidateList.get(i).getCandidateID();
			}

		}
		return -1;
	}

	/**
	 * eliminates ballots with lowest #1 or #2, etc
	 * 
	 * @param candidateId
	 * 
	 * @return true if eliminated, false otherwise
	 */
	int newRank = 0;

	public boolean eliminate(int candidateId) {
		for (int i = 0; i < candidateList.size(); i++) {
			if (candidateList.get(i).getCandidateID() == candidateId) {
				for (int j = 0; j < candidateList.size(); j++) {

					if (i != j) {

						if (candidateList.get(i).getCandidateRank() < candidateList.get(j).getCandidateRank()) {
							candidateList.get(j).setCandidateRank(candidateList.get(j).getCandidateRank() - 1);
						}
					}
				}
				candidateList.remove(candidateList.get(i));

				return true;
			}
		}
		return false;
	}

	/**
	 * checks to see if a ballot is valid
	 * 
	 * @param none
	 * 
	 * @return returns true if is valid, false otherwise
	 */
	public boolean isValidBallot() {

		if (candidateList.isEmpty()) {
			return false;
		}
		for (int i = 0; i < candidateList.size(); i++) {
			for (int j = i + 1; j < candidateList.size(); j++) {

				if (candidateList.get(i).getCandidateID() == candidateList.get(j).getCandidateID()) {
					return false;
				}
				if (candidateList.get(i).getCandidateRank() == candidateList.get(j).getCandidateRank()) {
					return false;
				}
				if (candidateList.get(i).getCandidateRank() > Election.candidateIdList.size()) {
					return false;
				}
			}
		}
		return true;
	}

}
