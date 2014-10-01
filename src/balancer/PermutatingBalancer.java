package balancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ratio.Rating;

public class PermutatingBalancer implements Balancer {

	public PermutatingBalancer(List<Rating> ratings) {
		ratings.sort(null);
		createTeams(ratings);
	}
	@Override
	public List<String> getTeam(int i) {
		if (i == 0) return team1;
		else if (i == 1) return team2;
		else throw new IndexOutOfBoundsException("" + i);
	}

	@Override
	public int countTeams() {
		return 2;
	}

	@Override
	public double getRating(int i) {
		if (i == 0) return ratingTeam1;
		else if (i == 1) return ratingTeam2;
		else throw new IndexOutOfBoundsException("" + i);
	}
	/**
	 * 
	 * @param ratings
	 *            sorted list of players with ratings
	 */
	private void createTeams(List<Rating> ratings) {
		int[] indizes = new int[ratings.size()];
		for (int i = 0; i < ratings.size(); ++i)
			indizes[i] = i;
		Permutation p = new Permutation(indizes);
		int[] maxPerm = Arrays.copyOf(indizes, indizes.length);
		double maxDiff = Double.POSITIVE_INFINITY;
		for (int[] perm : p) {
			double diff = diff(ratings, perm);
			if (diff < maxDiff) {
				maxPerm = Arrays.copyOf(perm, perm.length);
				maxDiff = diff;
			}
		}
		for (int i = 0; i < maxPerm.length / 2; ++i) {
			team1.add(ratings.get(maxPerm[i]).player);
		}
		ratingTeam1 = evaluate(ratings, Arrays.copyOfRange(maxPerm, 0, maxPerm.length / 2));
		for (int i = maxPerm.length / 2; i < maxPerm.length; ++i) {
			team2.add(ratings.get(maxPerm[i]).player);
		}
		ratingTeam2 = evaluate(ratings, Arrays.copyOfRange(maxPerm, maxPerm.length / 2, maxPerm.length));
	}
	private double diff(List<Rating> ratings, int[] indizes) {
		double e1 = evaluate(ratings, Arrays.copyOfRange(indizes, 0, indizes.length / 2));
		double e2 = evaluate(ratings, Arrays.copyOfRange(indizes, indizes.length / 2, indizes.length));
		return Math.abs(e1 - e2);
	}
	private double evaluate(List<Rating> ratings, int[] indizes) {
		double sum = 0;
		for (int i : indizes) {
			sum += ratings.get(i).rating;
		}
		return sum;
	}

	private List<String> team1 = new ArrayList<String>();
	private List<String> team2 = new ArrayList<String>();
	private double ratingTeam1;
	private double ratingTeam2;
}
