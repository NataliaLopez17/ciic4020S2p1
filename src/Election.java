import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import classes.ArrayList;
import classes.DynamicSet;
import interfaces.List;

public class Election {

	public static List<Ballot> voteList;
	public static List<CandidateRanksAndIDs> candidateIdList;
	public static List<String> candidateNameList;
	public static List<DynamicSet<Ballot>> sameRanks;

	public static FileWriter fileWriter;

	public static void main(String[] args) throws IOException {

		fileWriter = new FileWriter("results.txt");
		ballotScanner();
		candidateScanner();
		CountingRounds(sameRanks);
		fileWriter.close();
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
		voteList = new ArrayList<>();

		try {
			ballotsSC = new BufferedReader(new FileReader("ballots.csv"));
			while ((ballotString = ballotsSC.readLine()) != null) {
				String[] ballotStringArray = ballotString.split(",");
				voteList.add(new Ballot(ballotStringArray));
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
		candidateIdList = new ArrayList<>();
		candidateNameList = new ArrayList<>();

		try {
			candidatesSC = new BufferedReader(new FileReader("candidates.csv"));
			while ((candidateString = candidatesSC.readLine()) != null) {
				String[] candidateStringArray = candidateString.split(",");
				candidateIdList.add(new CandidateRanksAndIDs(candidateStringArray));
				candidateNameList.add(candidateStringArray[0]);
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
	 * ballots that were voted #1.
	 * 
	 * @param rank int where it takes a rank specified
	 * 
	 * @return returns a list of sets where each index is a candidate and it
	 *         contains the ballots that were voted #1
	 */
	public static List<DynamicSet<Ballot>> candidateBallotArrayOfSets(int rank) {

		List<DynamicSet<Ballot>> numberOneBallot = new ArrayList<DynamicSet<Ballot>>();
		int candidateNum = 1;
		DynamicSet<Ballot> newSets = new DynamicSet<Ballot>(1);
		for (int i = 0; i < voteList.size(); i++) {

			Ballot newBallot = voteList.get(i);
			if (newBallot == null) {
				continue;
			}
			if (newBallot.getRankByCandidate(candidateNum) == rank) {
				newSets.add(voteList.get(i));

			}
			if (i == voteList.size() - 1 && candidateNum < voteList.size()) {
				i = -1;
				candidateNum++;
				numberOneBallot.add(newSets);
				newSets = new DynamicSet<Ballot>(1);

				if (candidateNum > candidateIdList.size()) {
					break;
				} else {
					continue;
				}
			}
		}

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
	public static int tieBreaker(List<DynamicSet<Ballot>> sameRanks, List<Integer> candidateLocation) {
		int pos = 2;
		int location = 1;
		boolean flag = true;
		sameRanks = candidateBallotArrayOfSets(pos);
		if (candidateLocation.size() == pos) {
			for (int i = 0; i < sameRanks.size(); i++) {
				if (sameRanks.get(candidateLocation.get(i)).size() > sameRanks.get(candidateLocation.get(location))
						.size()) {
					location++;
					return candidateLocation.get(i);
				}
				if (sameRanks.get(candidateLocation.get(i)).size() < sameRanks.get(candidateLocation.get(location))
						.size()) {
					location++;
					return candidateLocation.get(location);
				}
				if (sameRanks.get(candidateLocation.get(i)).size() == sameRanks.get(candidateLocation.get(location))
						.size()) {
					while (flag) {
						i++;
						location++;
						if (sameRanks.get(candidateLocation.get(i)).size() > sameRanks
								.get(candidateLocation.get(location)).size()) {
							location++;
							flag = false;
							return candidateLocation.get(i);
						}
						if (sameRanks.get(candidateLocation.get(i)).size() < sameRanks
								.get(candidateLocation.get(location)).size()) {
							location++;
							flag = false;
							return candidateLocation.get(location);
						}
					}
					flag = false;
				}
			}
		}

		return location;
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
	public static void CountingRounds(List<DynamicSet<Ballot>> sameRanks) {

		int invalidCounter = 0;
		int blanksCounter = 0;
		int receivedBallots = voteList.size();
		int numRound = 0;
		int numberOfOnes = 0;
		String winnerName = "";
		String loserName = "";

		for (int i = 0; i < voteList.size(); i++) {
			if (voteList.get(i).getCandidateList().isEmpty()) {
				blanksCounter++;

			}
		}
		List<Integer> invalidIndexes = new ArrayList<>();
		for (int i = 0; i < voteList.size(); i++) {
			if (!voteList.get(i).isValidBallot()) {
				invalidIndexes.add(i);
				invalidCounter++;

			}
		}
		for (Integer i : invalidIndexes) {
			voteList.remove(i);
		}

		List<Integer> candidateLocation = new ArrayList<Integer>();
		int maxRankIndex = 0;
		int maxCount = 0;
		sameRanks = candidateBallotArrayOfSets(maxRankIndex);
		for (int i = 1; i < sameRanks.size(); i++) {
			if (sameRanks.get(maxRankIndex).size() >= sameRanks.get(i).size()) {
				maxRankIndex = i;
				candidateLocation.add(maxCount);
				maxCount++;

			}
			numberOfOnes++;

		}
		if (maxRankIndex < Math.round(sameRanks.size() / 2)) {

			int loser = tieBreaker(sameRanks, candidateLocation);
			for (int i = 0; i < voteList.size(); i++) {
				voteList.get(i).eliminate(loser);
				numberOfOnes = i;
				numRound++;
				loserName = candidateNameList.get(loser);
			}
		}
		if (maxRankIndex > Math.round(sameRanks.size() / 2)) {

			winnerName = candidateNameList.get(candidateLocation.get(maxRankIndex - 1));
		}
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
	 * @return index of the candidate
	 */
	public static int findMin(List<DynamicSet<Ballot>> sameRanks) {
		int minRankIndex = 0;
		for (int i = 1; i < sameRanks.size(); i++) {
			if (sameRanks.get(minRankIndex).size() >= sameRanks.get(i).size()) {
				minRankIndex = i;
			}
		}
		return minRankIndex;

	}
}
