package ratio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;

import storage.Game;

public class NotSoRandomWalk implements Ratio {
	public NotSoRandomWalk(UnaryOperator<Integer> f_won,
			UnaryOperator<Integer> f_lost) {
		this.f_won = f_won;
		this.f_lost = f_lost;
	}

	public NotSoRandomWalk() {
		this(NotSoRandomWalk::fplus1, NotSoRandomWalk::fminus1);
	}

	@Override
	public List<Rating> get(List<Game> games, List<String> players) {
		Map<String, Integer> values = new HashMap<>();
		players.forEach(p -> values.put(p, 0));

		for (Game g : games) {
			for (String p : g.winners) {
				if (values.containsKey(p)) {
					values.put(p, f_won.apply(values.get(p)));
				}
			}
			for (String p : g.losers) {
				if (values.containsKey(p)) {
					values.put(p, f_lost.apply(values.get(p)));
				}
			}
		}
		List<Rating> ratings = new ArrayList<>();
		for (Entry<String, Integer> e : values.entrySet()) {
			ratings.add(new Rating(e.getKey(), e.getValue()));
		}
		return ratings;
	}

	private UnaryOperator<Integer> f_won;
	private UnaryOperator<Integer> f_lost;

	private static int fplus1(int x) {
		return x + 1;
	}
	private static int fminus1(int x) {
		return x - 1;
	}
}
