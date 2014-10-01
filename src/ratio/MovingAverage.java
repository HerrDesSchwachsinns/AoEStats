package ratio;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import storage.Game;

public class MovingAverage implements Ratio {
	public MovingAverage(Ratio ratio) {
		this.ratio = ratio;
	}
	@Override
	public List<Rating> get(List<Game> games, List<String> players) {
		Map<String, List<Double>> history = ratio.getHistory(games, players);
		return history
				.entrySet()
				.stream()
				.map(e -> new Rating(e.getKey(),
						weightedMovingAverage(e.getValue())))
				.collect(Collectors.toList());

	}
	//private double movingAverage(List<Double> list) {
	//	return list.stream().mapToDouble(Double::doubleValue).average()
	//			.orElse(0);
	//}
	private double weightedMovingAverage(List<Double> list) {
		i = 1;
		return list.stream().mapToDouble(Double::doubleValue)
				.reduce(0, (sum, x) -> sum + x * (i++ / (double) list.size()))
				/ list.size();
	}

	private int i;

	private Ratio ratio;
}
