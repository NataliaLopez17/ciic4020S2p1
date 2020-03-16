import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import classes.ArrayList;
import classes.DynamicSet;

public class Election {

	public static ArrayList<Ballot> voteList;
	public static ArrayList<DynamicSet<Ballot>> sameRanks;
	public static ArrayList<Candidate> candidateList;
	public static int candidateCounter;
	public static int invalidCounter = 0;
	public static int blanksCounter = 0;
	public static int receivedBallots = 0;
	public static int numRound = 0;
	public static int numberOfOnes = 0;
	public static String winnerName = "";
	public static String loserName = "";

	public static FileWriter fileWriter;

	public static void main(String[] args) throws IOException {

		fileWriter = new FileWriter("results.txt");
		candidateScanner();
		ballotScanner();
		setManager();
		roundsManager();

		fileWriter.close();
	}

	private static void roundsManager() {
		int rounds = candidateList.size() - 1;
		while (rounds-- > 0) {
			decideWhoToEliminate();
		}
	}

	private static void decideWhoToEliminate() {
		int target = 1;
		ArrayList<Candidate> minCandidates = findMin(candidateList, target);

		if (minCandidates.size() > 1) {
			while (minCandidates.size() > 1) {
				target++;
				minCandidates = findMin(minCandidates, target);
			}

		}
		for (int i = 0; i < voteList.size(); i++) {
			voteList.get(i).eliminate(minCandidates.get(0).getId());
		}

	}

	public static void setManager() {
		for (Candidate c : candidateList) {
			DynamicSet<Integer> intSet = new DynamicSet<Integer>(1);

			for (Ballot b : voteList) {
				for (int i = 0; i < b.idList.size(); i++) {
					if (b.getIdList().get(i) == c.getId()) {
						intSet.add(b.rankList.get(i));
					}
				}
			}
			c.setRankSet(intSet);
		}

	}

	/**
	 * creates an array of all the ballots
	 * 
	 * @param none
	 * 
	 * @return none
	 */
	public static void ballotScanner() {
		String ballotString = "";
		BufferedReader ballotsSC = null;
		voteList = new ArrayList<Ballot>();

		try {
			ballotsSC = new BufferedReader(new FileReader("ballots.csv"));
			while ((ballotString = ballotsSC.readLine()) != null) {
				String[] ballotStringArray = ballotString.split(",");
				voteList.add(new Ballot(ballotStringArray, candidateCounter));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ballotsSC != null) {
				try {
					ballotsSC.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		receivedBallots = voteList.size();
	}

	/**
	 * creates an array of all the candidates
	 * 
	 * @param none
	 * 
	 * @return none
	 */
	public static void candidateScanner() {
		String candidateString = "";
		BufferedReader candidatesSC = null;
		candidateCounter = 0;
		candidateList = new ArrayList<Candidate>();

		try {
			candidatesSC = new BufferedReader(new FileReader("candidates.csv"));
			while ((candidateString = candidatesSC.readLine()) != null) {
				String[] candidateStringArray = candidateString.split(",");
				candidateList.add(new Candidate(candidateStringArray[0], Integer.parseInt(candidateStringArray[1])));
				candidateCounter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (candidatesSC != null) {
				try {
					candidatesSC.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Creates a list of sets where each index is a candidate and it contains the
	 * ballots that were voted #1, #2, #3 ,etc.
	 * 
	 * @param rank int where it takes a rank specified
	 * 
	 * @return returns a list of sets where each index is a candidate and it
	 *         contains the ballots that were voted #1
	 */
	public static ArrayList<DynamicSet<Ballot>> candidateBallotArrayOfSets(int rank) {

		ArrayList<DynamicSet<Ballot>> numberOneBallot = new ArrayList<DynamicSet<Ballot>>();
		int candidateNum = 1;
		return numberOneBallot;

	}

	/**
	 * finds the candidates that are tied
	 * 
	 * @param sameRanks list where each index is a candidate and it contains the
	 *                  ballots that were voted #1 or #2, etc. candidateLocation
	 *                  list of the candidates which are tied
	 * @return index of the loser candidate
	 */
	public static int tieBreaker(DynamicSet<DynamicSet<Ballot>> sameRanks, DynamicSet<Integer> candidateLocation) {
		return blanksCounter;

	}

	/**
	 * Runs the voting rounds
	 * 
	 * @param sameRanks list where each index is a candidate and it contains the
	 *                  ballots that were voted #1 or #2, etc.
	 * @return none
	 * 
	 * 
	 */
	public static void CountingRounds(DynamicSet<DynamicSet<Ballot>> sameRanks) {

		try {
			fileWriter.write(String.format("Number of ballots received: %s\n", receivedBallots));
			fileWriter.write(String.format("Number of blank ballots: %s\n", blanksCounter));
			fileWriter.write(String.format("Number of invalid ballots: %s\n", invalidCounter));
			fileWriter.write(String.format("Round: %1$s, %2$s was eliminated with %3$s #1's\n", numRound, loserName,
					numberOfOnes));
			fileWriter.write(String.format("Winner: %1$s, wins with %2$s #1's\n", winnerName, numberOfOnes));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Finds the candidate with the least amount of #1 ballots.
	 * 
	 * @param sameRanks list where each index is a candidate and it contains the
	 *                  ballots that were voted #1.
	 * @return the Candidate with the least rank
	 */
	public static ArrayList<Candidate> findMin(ArrayList<Candidate> candidateList, int targetRank) {
		ArrayList<Candidate> theArray = new ArrayList<Candidate>();
		Candidate minCandidate = candidateList.get(0);
		for (int i = 1; i < candidateList.size(); i++) {
			if (minCandidate.count(targetRank) > candidateList.get(i).count(targetRank)) {
				minCandidate = candidateList.get(i);
				theArray.clear();
				theArray.add(minCandidate);
			} else if (minCandidate.count(targetRank) == candidateList.get(i).count(targetRank)) {
				theArray.add(minCandidate);
			}
		}
		return theArray;

	}
}
