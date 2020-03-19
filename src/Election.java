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
		roundsManager();
		moreThan50Winner();
		CountingRounds();

		fileWriter.close();
	}

	/**
	 * runs the rounds while there are still candidates
	 * 
	 * @param none
	 * 
	 * @return none
	 */
	public static void roundsManager() throws IOException {
		int rounds = candidateList.size() - 1;
		while (rounds-- > 0) {
			decideWhoToEliminate();
		}
	}

	/**
	 * decides who the winner is if they have more than half the votes
	 * 
	 * @param none
	 * 
	 * @return none
	 */
	public static void moreThan50Winner() throws IOException {
		int target = 1;
		ArrayList<Candidate> maxCandidates = findMax(candidateList, target);
		if (maxCandidates.get(target).getRankSet().size() > Math.round(voteList.size() / 2)) {
			winnerName = candidateList.get(target).getName();
			numberOfOnes = maxCandidates.get(target).count(target);
			target++;
		}
		decideWhoToEliminate();
	}

	/**
	 * eliminates and declares the winner
	 * 
	 * @param none
	 * 
	 * @return none
	 */
	public static void decideWhoToEliminate() throws IOException {
		int target = 1;
		ArrayList<Candidate> minCandidates = findMin(candidateList, target);

		if (minCandidates.size() > 1) {
			while (minCandidates.size() > 1) {
				target++;
				minCandidates = findMin(minCandidates, target);
			}
		}
		for (int i = 0; i < voteList.size(); i++) {
			if (i < candidateList.size() - 1) {
				if (i < minCandidates.size() - 1) {
					voteList.get(i).eliminate(minCandidates.get(i).getId());
					target++;
				}
				loserName = candidateList.get(i).getName();
				numberOfOnes = candidateList.get(i).count(i + 1);
				numRound = i + 1;
				fileWriter.write(String.format("Round: %1$s, %2$s was eliminated with %3$s #1's\n", numRound, loserName,
						numberOfOnes));
			}

		}
		winnerName = candidateList.get(target).getName();
		numberOfOnes = candidateList.get(target).count(target);

	}

	/**
	 * creates sets with the #1s
	 * 
	 * @param none
	 * 
	 * @return none
	 */

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
				if (ballotStringArray[1].matches("")) {
					blanksCounter++;
				}
				voteList.add(new Ballot(ballotStringArray, candidateCounter));
			}
			receivedBallots = voteList.size();
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
	 * Runs the prints
	 * 
	 * @param none
	 * @return none
	 * 
	 * 
	 */
	public static void CountingRounds() {

		try {
			fileWriter.write(String.format("Number of ballots received: %s\n", receivedBallots));
			fileWriter.write(String.format("Number of blank ballots: %s\n", blanksCounter));
			fileWriter.write(String.format("Number of invalid ballots: %s\n", invalidCounter));

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

	/**
	 * Finds the candidate with the most amount of #1 ballots.
	 * 
	 * @param sameRanks list where each index is a candidate and it contains the
	 *                  ballots that were voted #1.
	 * @return the Candidate with the highest rank
	 */
	public static ArrayList<Candidate> findMax(ArrayList<Candidate> candidateList, int targetRank) {
		ArrayList<Candidate> theArray = new ArrayList<Candidate>();
		Candidate maxCandidate = candidateList.get(0);
		for (int i = 1; i < candidateList.size(); i++) {
			if (maxCandidate.count(targetRank) > candidateList.get(i).count(targetRank)) {
				maxCandidate = candidateList.get(i);
				theArray.clear();
				theArray.add(maxCandidate);
			} else if (maxCandidate.count(targetRank) == candidateList.get(i).count(targetRank)) {
				theArray.add(maxCandidate);
			}
		}
		return theArray;

	}
}
