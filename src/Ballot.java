import classes.ArrayList;
import interfaces.List;


public class Ballot {
	private int ballotNumber;

	private List<CandidateRanksAndIDs> voteList;

	public Ballot(String[] ballotStringArray) {
		this.ballotNumber = Integer.valueOf(ballotStringArray[0]);
		candidateIdAndRank(ballotStringArray);
	}


	//---------------------------------methods-----------------------------------

	public void candidateIdAndRank(String[] ballotStringArray) {
		voteList = new ArrayList<>();
		for(int i = 1; i < ballotStringArray.length; i++) {
			String ID = ballotStringArray[i].substring(0, ballotStringArray[i].indexOf(":"));
			String rank = ballotStringArray[i].substring(ballotStringArray[i].indexOf(":") +1 );
			voteList.add(new CandidateRanksAndIDs(Integer.parseInt(ID), Integer.parseInt(rank)));
		}
	}

	// returns the ballot number
	public int getBallotNum() {
		return ballotNumber;
	} 

	// rank for that candidate
	//return -1 if not found
	public int getRankByCandidate(int candidateId) {
		for(int i = 0; i < voteList.size(); i++) {
			if(voteList.get(i).getCandidateID() == candidateId) {
				return voteList.get(i).getCandidateRank();
			}
			
		}
		return -1;
	} 
	
	public static void main(String[] args) {
		
	}

	// candidate with that rank
	//return -1 if not found
	public int getCandidateByRank(int rank) {
		for(int i = 1; i < voteList.size(); i++) {
			if(voteList.get(i).getCandidateRank() == rank) {
				return voteList.get(i).getCandidateID();
			}
			
		}
		return -1;
	} 

	// eliminates a candidate
	public boolean eliminate(int candidateId) {
		for(int i = 0; i < voteList.size(); i++) {
			if(voteList.get(i).getCandidateID() == candidateId) {
				voteList.remove(voteList.get(i));
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

		if(voteList.isEmpty()) {
			return false;
		}
		for(int i = 0; i < voteList.size(); i++) {
			for(int j = i + 1; j < voteList.size(); j++) {

				if(voteList.get(i).getCandidateID() == voteList.get(j).getCandidateID()) {
					return false;
				}
				if(voteList.get(i).getCandidateRank() == voteList.get(j).getCandidateRank()) {
					return false;
				}
				if(voteList.get(i).getCandidateRank() > Election.candidateList.size()) {
					return false;
				}
			}
		}
		return true;
	}

	public void printBallotInfo() {
		System.out.println("{Ballot " + getBallotNum() + "}");
		for (CandidateRanksAndIDs votes : voteList) {
			System.out.println("\t" + "Candidate: " + votes.getCandidateID() + "," + " Rank: " + votes.getCandidateRank());
		}
	}

}
