package ratio;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;

import storage.Game;

/*package*/class WinLoss {

	static Collection<WinLoss> getWinLoss(Collection<Game> games, Collection<String> players) {
		TreeMap<String, WinLoss> wl = new TreeMap<>();
		//initialize all players
		for (String player : players) {
			wl.put(player, new WinLoss(player));
		}
		//dummy WinLoss
		WinLoss dummy = new WinLoss("");
		//populate map (player entries not mentioned in players are discarded)
		for (Game g : games) {
			Arrays.stream(g.winners).map(p -> wl.getOrDefault(p, dummy))
					.forEach(WinLoss::addWin);
			Arrays.stream(g.losers).map(p -> wl.getOrDefault(p, dummy))
					.forEach(WinLoss::addLoss);
		}
		return wl.values();
	}

	String player;
	int wins;
	int losses;

	WinLoss(String player) {
		this.player = player;
	}
	int wins() {
		return wins;
	}
	int losses() {
		return losses;
	}
	int games() {
		return wins + losses;
	}

	void addWin() {
		++wins;
	}
	void addLoss() {
		++losses;
	}
	@Override
	public String toString() {
		return player + "[wins=" + wins + ", losses=" + losses + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + losses;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + wins;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WinLoss other = (WinLoss) obj;
		if (losses != other.losses)
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (wins != other.wins)
			return false;
		return true;
	}
}