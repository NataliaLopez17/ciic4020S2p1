import classes.DynamicSet;

public class Candidate {

	private int id;
	DynamicSet<Integer> rankSet;
	private String name;

	public Candidate(String name, int id) {
		this.id = id;
		this.name = name;
		rankSet = new DynamicSet<Integer>(1);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DynamicSet<Integer> getRankSet() {
		return rankSet;
	}

	public void setRankSet(DynamicSet<Integer> rankSet) {
		this.rankSet = rankSet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * counts how many ranks there is on the rankSet
	 * 
	 * @param targetRank
	 * 
	 * @return the number of ranks in that set
	 */
	public int count(int targetRank) {
		int counter = 0;
		for (Integer i : rankSet) {
			if (i == targetRank) {
				counter++;
			}
		}
		return counter;
	}

}
