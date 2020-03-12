import classes.ArrayList;
import interfaces.List;


public class Ballot {
	private int ballotNumber;

	private List<CandidateRanksAndIDs> candidateList;

	public List<CandidateRanksAndIDs> getCandidateList() {
		return candidateList;
	}

	public Ballot(String[] ballotStringArray) {
		this.ballotNumber = Integer.valueOf(ballotStringArray[0]);
		candidateIdAndRank(ballotStringArray);
	}


	//---------------------------------methods-----------------------------------

	public void candidateIdAndRank(String[] ballotStringArray) {
		candidateList = new ArrayList<>();
		for(int i = 1; i < ballotStringArray.length; i++) {
			String ID = ballotStringArray[i].substring(0, ballotStringArray[i].indexOf(":"));
			String rank = ballotStringArray[i].substring(ballotStringArray[i].indexOf(":") +1 );
			candidateList.add(new CandidateRanksAndIDs(Integer.parseInt(ID), Integer.parseInt(rank)));
		}
	}

	// returns the ballot number
	public int getBallotNum() {
		return ballotNumber;
	} 

	// rank for that candidate
	//return -1 if not found
	public int getRankByCandidate(int candidateId) {
		for(int i = 0; i < candidateList.size(); i++) {
			if(candidateList.get(i).getCandidateID() == candidateId) {
				return candidateList.get(i).getCandidateRank();
			}

		}
		return -1;
	} 


	// candidate with that rank
	//return -1 if not found
	public int getCandidateByRank(int rank) {
		for(int i = 0; i < candidateList.size(); i++) {
			if(candidateList.get(i).getCandidateRank() == rank) {
				return candidateList.get(i).getCandidateID();
			}

		}
		return -1;
	} 

	// eliminates a candidate

	int newRank = 0;
	public boolean eliminate(int candidateId) {
		for(int i = 0; i < candidateList.size(); i++) {
			if(candidateList.get(i).getCandidateID() == candidateId) { 
				for(int j = 0; j < candidateList.size(); j++) {

					
					if(i != j) {
						
						if(candidateList.get(i).getCandidateRank() < candidateList.get(j).getCandidateRank()) {
							System.out.println("---------------------");
							System.out.println("Before: " + candidateList.get(j).getCandidateID() + " "+ candidateList.get(j).getCandidateRank());
							candidateList.get(j).setCandidateRank(candidateList.get(j).getCandidateRank()-1);

							System.out.println("After: " + candidateList.get(j).getCandidateID()+ " " + candidateList.get(j).getCandidateRank());
							System.out.println("---------------------");
						}
					}

				}

				candidateList.remove(candidateList.get(i));



				return true;
			}
		}
		return false;
	} 

	/*
	 * is valid if not empty
	 * if ranks are not repeated
	 * if votes are < candidateNum
	 * if candidateID is not repeated
	 * 
	 */
	public boolean isValidBallot() {

		if(candidateList.isEmpty()) {
			return false;
		}
		for(int i = 0; i < candidateList.size(); i++) {
			for(int j = i + 1; j < candidateList.size(); j++) {

				if(candidateList.get(i).getCandidateID() == candidateList.get(j).getCandidateID()) {
					return false;
				}
				if(candidateList.get(i).getCandidateRank() == candidateList.get(j).getCandidateRank()) {
					return false;
				}
				if(candidateList.get(i).getCandidateRank() > Election.candidateIdList.size()) {
					return false;
				}
			}
		}
		return true;
	}

	public void printBallotInfo() {
		System.out.println("{Ballot " + getBallotNum() + "}");
		for (CandidateRanksAndIDs votes : candidateList) {
			System.out.println("\t" + "Candidate: " + votes.getCandidateID() + "," + " Rank: " + votes.getCandidateRank());
		}
	}

}
