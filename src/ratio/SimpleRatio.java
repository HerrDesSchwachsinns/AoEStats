package ratio;

import java.util.List;
import java.util.stream.Collectors;

import storage.Game;

public class SimpleRatio implements Ratio {

	@Override
	public List<Rating> get(List<Game> games, List<String> players) {
		return WinLoss.getWinLoss(games, players).stream().map(this::cast)
				.collect(Collectors.toList());
	}
	/**
	 * calculate wins/losses
	 * @param wins
	 * @param losses
	 * @return
	 */
	public double ratio(int wins, int losses) {
		if (wins == 0)
			return 0;
		if (losses == 0)
			return 1 / Math.exp(wins);
		return (double) wins / (double) losses;
	}
	private Rating cast(WinLoss wl) {
		return new Rating(wl.player, ratio(wl.wins, wl.losses));
	}
}
