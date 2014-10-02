package ratio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import storage.Game;

public class WinLossSeries implements Ratio {

	@Override
	public List<Rating> get(List<Game> games, List<String> players) {
		return getHistory(games, players)
				.entrySet()
				.stream()
				.map(e -> new Rating(e.getKey(), e.getValue().get(e.getValue()
						.size() - 1))) //
				.collect(Collectors.toList());
	}
	@Override
	public Map<String, List<Double>> getHistory(List<Game> games, List<String> players) {
		Map<String, List<Double>> history = new HashMap<>();
		players.forEach(p -> history.put(p, new ArrayList<>()));
		players.forEach(p -> history.get(p).add(0d));
		for (Game g : games) {
			for (String p : players) {
				List<Double> playerHistory = history.get(p);
				if (Arrays.asList(g.winners).contains(p)) appendIncrementOrSwitch(playerHistory, true);
				else if (Arrays.asList(g.losers).contains(p)) appendIncrementOrSwitch(playerHistory, false);
				else playerHistory
						.add(playerHistory.get(playerHistory.size() - 1));
			}
		}
		return history;
	}
	private void appendIncrementOrSwitch(List<Double> list, boolean won) {
		double last = list.get(list.size() - 1);
		double next;
		if (won) next = last >= 0 ? last + 1 : 1;
		else next = last <= 0 ? last - 1 : -1;
		list.add(next);
	}
}
