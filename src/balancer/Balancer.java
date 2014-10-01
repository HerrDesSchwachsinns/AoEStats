package balancer;

import java.util.List;

import ratio.Rating;

public interface Balancer {
	public List<String> getTeam(int i);
	public int countTeams();
	public double getRating(int i);
	public static double evalTeam(List<String> team, List<Rating> ratings) {
		return ratings.stream().filter(r -> team.contains(r.player))
				.mapToDouble(r -> r.rating).sum();
	}
}
