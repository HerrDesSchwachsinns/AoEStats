package convert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stats {
	public static void main(String[] args) throws IOException {
		Stats stats = new Stats("AoE Stats.csv");
		System.out.println(Arrays.toString(stats.players()));

		for (Game g : stats.games()) {
			System.out.println(g);
		}
	}

	public static class Game {
		public final String date;
		public final int number;
		public final String[] winners;
		public final String[] loosers;

		public Game(String date, int number, String[] winners, String[] loosers) {
			this.date = date;
			this.number = number;
			this.winners = winners;
			this.loosers = loosers;
		}
		public String[] players() {
			String[] players = new String[winners.length + loosers.length];
			System.arraycopy(winners, 0, players, 0, winners.length);
			System.arraycopy(loosers, 0, players, winners.length, loosers.length);
			return players;
		}
		public String encode() {
			return String.join(",", date, String.join(":", winners), String
					.join(":", loosers));
		}
		@Override
		public String toString() {
			return "{"
					+ String.join(",", date, Integer.toString(number), "won("
							+ String.join(":", winners) + ")", "lost("
							+ String.join(":", loosers) + ")") + "}";
		}
	}

	public static final String WON = "1";
	public static final String LOST = "-1";
	public static final String DATE_FIELD = "Match";

	public Stats(String filename) throws IOException {
		csv = new ReadCSV(filename, ',');
		readPlayers();
		readGames();
	}
	public String[] players() {
		return players;
	}
	public List<Stats.Game> games() {
		return games;
	}

	private void readPlayers() {
		String[] header = csv.header();
		players = Arrays.copyOfRange(header, 1, header.length - 1);
	}
	private void readGames() throws IOException {
		for (ReadCSV.Line line = csv.readLine(); line != null; line = csv
				.readLine()) {
			if (line.get(0).equals("") || line.get(0).equals("Checksum"))
				continue;
			games.add(readGame(line));
		}
	}

	private Game readGame(ReadCSV.Line values) {
		ArrayList<String> winners = new ArrayList<String>();
		ArrayList<String> loosers = new ArrayList<String>();

		for (int i = 1; i < values.data.length - 1; ++i) {
			switch (values.get(i)) {
			case WON:
				winners.add(players[i - 1]);
				break;
			case LOST:
				loosers.add(players[i - 1]);
				break;
			default:
				//did not play
				break;
			}
		}
		String date = values.get(0).substring(0, 8);
		int number = Integer.parseInt(values.get(0).substring(11).trim());
		return new Game(date, number,
				(String[]) (winners.toArray(new String[0])),
				(String[]) (loosers.toArray(new String[0])));
	}

	private ReadCSV csv;
	private String[] players;
	private List<Stats.Game> games = new ArrayList<Stats.Game>();
}
