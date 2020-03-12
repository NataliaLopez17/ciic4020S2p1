import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import classes.ArrayList;
import classes.DynamicSet;
import interfaces.List;
import interfaces.Set;

public class Election {

	private static final int DEFAULT_SIZE = 10;
	public static List<Ballot> voteList;
	public static List<CandidateRanksAndIDs> candidateIdList;
	public static List<String> candidateNameList;

	public static FileWriter fileWriter;

	public static void main(String[] args) throws IOException {

		ballotScanner();
		candidateScanner();

		//for(int i = 0; i < voteList.size(); i++) {
		//for(int j = 0; j < voteList.size(); j++) {

		//voteList.get(i).printBallotInfo();
		//System.out.println(voteList.get(i).toString());
		//System.out.println("----------------------------------------");
		//}
		//}


		//candidateBallotArrayOfSets(1);

		//Ballot b = voteList.get(0);
		//b.eliminate(1);

		fileWriter = new FileWriter("results.txt");
		CountingRounds();
		fileWriter.close();
	}


	public static void ballotScanner(){
		String ballotString ="";	
		BufferedReader ballotsSC = null;
		voteList = new ArrayList<>();


		try {
			ballotsSC = new BufferedReader(new FileReader("ballots.csv"));
			while((ballotString = ballotsSC.readLine()) != null) {
				String[] ballotStringArray = ballotString.split(",");
				voteList.add(new Ballot(ballotStringArray));

				//System.out.println("----starts here-----");
				//for(String i : ballotStringArray) {
				//System.out.println(i);
				//}
				//System.out.println("-----ends here-----");
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(ballotsSC != null) {
				try {
					ballotsSC.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static void candidateScanner(){
		String candidateString ="";	
		BufferedReader candidatesSC = null;
		candidateIdList = new ArrayList<>();
		candidateNameList = new ArrayList<>();

		try {
			candidatesSC = new BufferedReader(new FileReader("candidates.csv"));
			while((candidateString = candidatesSC.readLine()) != null) {
				String[] candidateStringArray = candidateString.split(",");
				candidateIdList.add(new CandidateRanksAndIDs(candidateStringArray));
				candidateNameList.add(candidateStringArray[0]);	
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(candidatesSC != null) {
				try {
					candidatesSC.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static List<Set<String>> candidateBallotArrayOfSets(int rank) {

		List<Set<String>> numberOneBallot = new ArrayList<Set<String>>(DEFAULT_SIZE); 
		int candidateNum = 1;
		Set<String> newSets = new DynamicSet<String>(1);
		for(int i = 0; i < voteList.size(); i++) {

			Ballot newBallot = voteList.get(i);

			if(newBallot == null) {
				continue;
			}

			if(newBallot.getRankByCandidate(candidateNum) == rank) {
				newSets.add(Integer.toString(newBallot.getBallotNum()));
				//newSets.add(voteList.get(i).toString());

			}

			if(i == voteList.size() - 1 && candidateNum < voteList.size()) {
				i = -1;
				candidateNum++;
				numberOneBallot.add(newSets);
				newSets = new DynamicSet<String>(1);

				if(candidateNum > candidateIdList.size()) {
					break;
				}
				else {
					continue;
				}
			}
		}
		//for(Set<String> sets : numberOneBallot) {
		//for(String s : sets) {
		//System.out.println(s);

		//}
		//System.out.println("-----------------");
		//}

		return numberOneBallot;
	}

	public static void CountingRounds() {

		int someCounter = 1;
		int invalidCounter = 0;
		int blanksCounter = 0;
		int receivedBallots = voteList.size();
		int numRound = 0;
		int numberOfOnes = 0;
		String winnerName = "";

		for(int i = 0; i < voteList.size(); i++) {
			if(voteList.get(i).getCandidateList().isEmpty()) {
				blanksCounter++;
			}
		}
		List<Integer> invalidIndexes = new ArrayList<Integer>();
		for(int i = 0; i < voteList.size(); i++) {
			if(!voteList.get(i).isValidBallot()) {
				invalidIndexes.add(i);
				invalidCounter++;
			}
		}
		for(Integer i : invalidIndexes) {
			voteList.remove(i);
		}
		List<Set<String>> sameRanks = candidateBallotArrayOfSets(someCounter);

		for(int i = 0; i< sameRanks.size();i++) {
			if(sameRanks.get(i).size() > Math.round(voteList.size()/2)) {
				winnerName = candidateNameList.get(i);
				numberOfOnes = sameRanks.get(i).size();
			}
			if(i < sameRanks.size() -1){
				if(sameRanks.get(i).size() == sameRanks.get(i + 1).size()) {
					int minCandidate = findMin(sameRanks)+1;
					for(int j = 0; j < voteList.size(); j++) {
						voteList.get(j).eliminate(minCandidate);
						numRound = i;
					}
				}
			}
		}
		//someCounter++;
		//sameRanks = candidateBallotArrayOfSets(someCounter);	

		try {
			fileWriter.write(String.format("Number of ballots received: %s\n", receivedBallots));
			fileWriter.write(String.format("Number of blank ballots: %s\n", blanksCounter));
			fileWriter.write(String.format("Number of invalid ballots: %s\n", invalidCounter));
			fileWriter.write(String.format("Round: %1$s, %2$s was eliminated with %3$s #1's\n",
					numRound, winnerName, numberOfOnes));
			fileWriter.write(String.format("Winner: %1$s, wins with %2$s\n",
					winnerName, numberOfOnes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	/**
	 * Finds the candidate with the less number of ballots.
	 * 
	 * @param sameRanks list where each index is a candidate and it contains the ballots that voted for them.
	 * @return index of the candidate
	 */
	public static int findMin(List<Set<String>> sameRanks) {
		int minRankIndex = 1;
		for(int i = 2; i < sameRanks.size(); i++) {
			if(sameRanks.get(minRankIndex).size() >= sameRanks.get(i).size()) {
				minRankIndex = i;
			}
		}
		return minRankIndex;

	}
}
