package ratio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import storage.Game;

public interface Ratio {
	public List<Rating> get(List<Game> games, List<String> players);
	//TODO insert method for efficient calculation of rating series maybe with defaulted method
	public default Map<String, List<Double>> getHistory(List<Game> games, List<String> players) {
		Map<String, List<Double>> history = new HashMap<>();
		players.forEach(player -> history.put(player, new ArrayList<>()));

		for (int i = 1; i < games.size(); ++i) {
			for (Rating r : get(games.subList(0, i), players)) {
				history.get(r.player).add(r.rating);
			}
		}
		return history;
	}
}
