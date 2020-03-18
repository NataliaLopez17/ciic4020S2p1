import java.util.Arrays;

import classes.ArrayList;
import interfaces.DynamicSet;

public class Ballot {

	private int ballotNumber;
	DynamicSet<Integer> idList;
	DynamicSet<Integer> rankList;
	private int candidateCounter;

	public Ballot(String[] ballotStringArray, int candidateCounter) {
		this.candidateCounter = candidateCounter;
		for (int i = 0; i < ballotStringArray.length; i++) {
			if (i == 0) {
				this.ballotNumber = Integer.parseInt(ballotStringArray[0]);
			} else {
				String[] temp = ballotStringArray[i].split(":");
				if (temp[0].isEmpty()) {
					Election.blanksCounter++;
				}
				idList.add(Integer.parseInt(temp[0]));
				rankList.add(Integer.parseInt(temp[1]));
			}
		}
	}

	// ---------------------------------methods-----------------------------------

	public void setBallotNumber(int ballotNumber) {
		this.ballotNumber = ballotNumber;
	}

	public DynamicSet<Integer> getIdList() {
		return idList;
	}

	public void setIdList(DynamicSet<Integer> idList) {
		this.idList = idList;
	}

	public DynamicSet<Integer> getRankList() {
		return rankList;
	}

	public void setRankList(DynamicSet<Integer> rankList) {
		this.rankList = rankList;
	}

	public int getCandidateCounter() {
		return candidateCounter;
	}

	public void setCandidateCounter(int candidateCounter) {
		this.candidateCounter = candidateCounter;
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
		for (int i = 0; i < idList.size(); i++) {
			if (idList.get(i) == candidateId) {
				return idList.get(i);
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
		for (int i = 0; i < rankList.size(); i++) {
			if (rankList.get(i) == rank) {
				return rankList.get(i);
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

	public boolean eliminate(int candidateId) {
		for (int i = 0; i < idList.size(); i++) {
			if (idList.get(i) == candidateId) {
				idList.remove(i);
				rankList.remove(i);
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
	public boolean isInvalidBallot() {
		ArrayList<Integer> tempRank = new ArrayList<>();
		ArrayList<Integer> tempCandidate = new ArrayList<>();
		for (int i = 0; i < rankList.size(); i++) {
			if (tempRank.contains(rankList.get(i)) || rankList.get(i) > candidateCounter) {

				Election.invalidCounter++;
				return true;
			} else {
				tempRank.add(rankList.get(i));
			}
		}
		for (int i = 0; i < idList.size(); i++) {
			if (tempCandidate.contains(idList.get(i)) || idList.get(i) > candidateCounter) {
				Election.invalidCounter++;
				return true;
			} else {
				tempCandidate.add(idList.get(i));
			}
		}
		return checkRankSequence();
	}

	private boolean checkRankSequence() {
		Integer[] intArray = new Integer[rankList.size()];
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = rankList.get(i);
		}
		Arrays.sort(intArray);
		for (int i = 1; i < intArray.length; i++) {
			if (intArray[i - 1] == intArray[i] - 1) {
				continue;
			} else {
				return true;
			}
		}
		return false;
	}

}
