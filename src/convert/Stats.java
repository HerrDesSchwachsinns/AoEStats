package convert;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import storage.Game;

public class Stats {
	public static final String WON = "1";
	public static final String LOST = "-1";
	public static final String DATE_FIELD = "Match";

	public Stats(String filename) throws IOException, ParseException {
		csv = new ReadCSV(filename, ',');
		readPlayers();
		readGames();
	}
	public String[] players() {
		return players;
	}
	public List<Game> games() {
		return games;
	}

	private void readPlayers() {
		String[] header = csv.header();
		players = Arrays.copyOfRange(header, 1, header.length - 1);
	}
	private void readGames() throws IOException, ParseException {
		for (ReadCSV.Line line = csv.readLine(); line != null; line = csv
				.readLine()) {
			if (line.get(0).equals("") || line.get(0).equals("Checksum"))
				continue;
			games.add(readGame(line));
		}
	}

	private Game readGame(ReadCSV.Line values) throws ParseException {
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
		return new Game(format.parse(date), number,
				(String[]) (winners.toArray(new String[0])),
				(String[]) (loosers.toArray(new String[0])));
	}

	private DateFormat format = new SimpleDateFormat("dd.MM.yy");
	private ReadCSV csv;
	private String[] players;
	private List<Game> games = new ArrayList<Game>();
}
