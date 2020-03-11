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
	public static List<CandidateRanksAndIDs> candidateList;

	public static FileWriter fileWriter;
	public static PrintWriter printWriter;

	public static void main(String[] args) {
		ballotScanner();
		candidateScanner();

		for(int i = 0; i < voteList.size(); i++) {
			voteList.get(i).printBallotInfo();
			System.out.println("----------------------------------------");
		}
		candidateBallotArrayOfSets(1);
		CountingRounds();

		try{
			fileWriter = new FileWriter("results.txt");
			printWriter = new PrintWriter(fileWriter);

			printWriter.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
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

				System.out.println("----starts here-----");
				for(String i : ballotStringArray) {
					System.out.println(i);
				}
				System.out.println("-----ends here-----");
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
		candidateList = new ArrayList<>();

		try {
			candidatesSC = new BufferedReader(new FileReader("candidates.csv"));
			while((candidateString = candidatesSC.readLine()) != null) {
				String[] candidateStringArray = candidateString.split(",");
				candidateList.add(new CandidateRanksAndIDs(candidateStringArray));
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



	/*
	 * [candidate 1 set which it's #1{b1,b2,b3},
	 * candidate 2 set which it's #1{}, 
	 * candidate 3 set which it's #1{},
	 * candidate 4 set which it's #1{},
	 * candidate 5 set which it's #1{}] = candidateBallotArraySet
	 */

	public static List<Set<String>> candidateBallotArrayOfSets(int num) {

		List<Set<String>> numberOneBallot = new ArrayList<Set<String>>(DEFAULT_SIZE); 
		int counter = 1;
		for(int i = 0; i < voteList.size(); i++) {
			Set<String> newSets = new DynamicSet<String>(1);
			Ballot newBallot = voteList.get(i);
			System.out.println("i: " + i);
			System.out.println("Candidate: " + counter);
			System.out.println("Rank: " + newBallot.getRankByCandidate(counter));
			System.out.println("-----------------------");

			if(newBallot.getRankByCandidate(counter) == num) {
				newSets.add(Integer.toString(newBallot.getBallotNum()));
				numberOneBallot.add(newSets);
			}

			if(i == voteList.size() - 1 && counter < voteList.size()) {
				i = -1;
				counter++;
			}
		}
		return numberOneBallot;
	}

	public static void CountingRounds() {
		int sum = 0;
		int someCounter = 1;
		int average = 0;
		int candidateNum = candidateList.size();
		List<Set<String>> somelist = candidateBallotArrayOfSets(someCounter);

		for(Set<String> n : somelist) {
			for(String m : n) {
				sum++;
			}
		}
		average = sum / candidateNum;

		for(int i = 0; i < voteList.size(); i++) {
			if(voteList.get(i).isValidBallot()) {
				if(somelist.get(i).size() > average) {
					if(i < voteList.size() -1) {
						if(somelist.get(i).size() == somelist.get(i + 1).size()) {
							someCounter++;
							somelist = candidateBallotArrayOfSets(someCounter);
						}
					}
				}
				
				else if(somelist.get(i).size() < average && somelist.get(i).size() > 0){
					if(i < voteList.size() -1) {
						if(somelist.get(i).size() == somelist.get(i + 1).size()) {
							someCounter++;
							somelist = candidateBallotArrayOfSets(someCounter);
						}
					}
				}
				
				else {
					int min = 0;
					List<Set<String>> baloot = new ArrayList<Set<String>>(DEFAULT_SIZE); 
					for(int m = 0; m < somelist.size(); m++) {
						if (min > somelist.get(m).size()) {
							baloot.clear();
							baloot.add(somelist.get(m));
						}
						if (min == somelist.get(m).size()) {
							baloot.add(somelist.get(m));
						}
						min = Math.min(somelist.get(m).size(), min);

					}
				}
			}
		}
	}
}
