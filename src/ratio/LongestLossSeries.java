package ratio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import storage.Game;

public class LongestLossSeries implements Ratio {
	@Override
	public List<Rating> get(List<Game> games, List<String> players) {
		Map<String, Integer> series = new HashMap<>();
		players.forEach(p -> series.put(p, new Integer(0)));
		Map<String, Integer> tempSeries = new HashMap<>();
		players.forEach(p -> tempSeries.put(p, new Integer(0)));
		for (Game g : games) {
			for (String p : g.losers) {
				if (!series.containsKey(p)) continue;
				int newMax = tempSeries.get(p) + 1;
				tempSeries.put(p, newMax);
				if (series.get(p) < newMax) series.put(p, newMax);
			}
			for (String p : g.winners) {
				if (!series.containsKey(p)) continue;
				tempSeries.put(p, 0);
			}
		}
		return series.entrySet().stream()
				.map(e -> new Rating(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}
}
