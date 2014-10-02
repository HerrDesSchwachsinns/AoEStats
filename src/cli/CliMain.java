package cli;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jfree.chart.ChartPanel;
import org.jfree.ui.ApplicationFrame;

import ratio.Ratios;
import ratio.WinLossSeries;
import ratio.LRatio;
import ratio.LongestLossSeries;
import ratio.LongestWinSeries;
import ratio.MovingAverage;
import ratio.NotSoRandomWalk;
import ratio.Rating;
import ratio.Ratio;
import ratio.SimpleRatio;
import storage.DBStorage;
import storage.Game;
import storage.Storage;
import storage.StorageException;
import chart.RatioChartPanel;

public class CliMain {
	private static final String DEFAULT_MODE = "stats";
	private static final Ratio DEFAULT_RATIO = new LRatio();
	private static final String DEFAULT_BALANCER = "serpentine-twist";
	private static final String DEFAULT_SERVER = "localhost/test";
	private static final String DEFAULT_USER = "";
	private static final String DEFAULT_PSWD = "";

	public static void main(String[] args) {
		Options options = new CliOptions();
		try {
			CommandLine cmd = readArgs(args, options);

			if (cmd.hasOption("help")) {
				printHelp(options);
				System.exit(0);
			}
			String mode;
			if (!cmd.hasOption("mode")) mode = DEFAULT_MODE;
			else mode = cmd.getOptionValue("mode");
			switch (mode) {
			default:
				System.out.println("Note: unkown mode. using default mode");
			case "stats":
				statsMain(cmd);
				break;
			case "balance":
				balancerMain(cmd);
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			printHelp(options);
			System.exit(1);
		} catch (StorageException e) {
			System.err.println(e.getMessage());
			System.exit(2);
		}
	}
	/*================ STATS MODE ================*/
	private static void statsMain(CommandLine cmd) throws StorageException {
		Storage storage = getStorage(cmd);
		Ratio ratio = getRatio(cmd);
		List<Game> games = storage.readAllGames();
		List<String> players = getCliPlayers(cmd);
		if (players.size() == 0) players = storage.readAllPlayers();

		printStats(players, games, ratio);
		if (cmd.hasOption("plot"))
			showPlot(players, games, ratio, cmd.hasOption("smooth"));
	}
	private static void printStats(List<String> players, List<Game> games, Ratio ratio) {
		List<Rating> rating = ratio.get(games, players);
		rating.sort(Collections.reverseOrder());
		int maxLength = rating.stream().mapToInt(r -> r.player.length()).max()
				.orElse(0);
		rating.forEach(r -> printRating(r, maxLength));
	}
	private static void showPlot(List<String> players, List<Game> games, Ratio ratio, boolean smoothPlot) {
		ChartPanel chartPanel = new RatioChartPanel(players, games, ratio,
				smoothPlot);
		ApplicationFrame frame = new ApplicationFrame("Ratio Development");
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) frame.dispose();
			}
		});
		frame.setVisible(true);
	}
	private static void printRating(Rating r, int length) {
		System.out
				.format(Locale.ROOT, "%1$-" + (length + 1) + "s%2$.2f%n", r.player, r.rating);
	}
	/*================ BALANCER MODE ================*/
	private static void balancerMain(CommandLine cmd) {
		//Storage storage = getStorage(cmd);
		//Ratio ratio = getRatio(cmd);
		System.out.println("Balancer not implemented yet");
		//Balancer balancer = getBalancer(cmd);
		//TODO implement
	}

	/*================ HELPER ================*/
	private static CommandLine readArgs(String[] args, Options options)
			throws ParseException {
		CommandLineParser parser = new GnuParser();
		return parser.parse(options, args);
	}
	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("aoestats [options] [players...]", options);
	}
	private static Storage getStorage(CommandLine cmd) {
		return new DBStorage(cmd.getOptionValue("server", DEFAULT_SERVER),//
				cmd.getOptionValue("user", DEFAULT_USER),//
				cmd.getOptionValue("paswd", DEFAULT_PSWD));
	}
	private static Ratio getRatio(CommandLine cmd) {
		if (!cmd.hasOption("ratio")) return DEFAULT_RATIO;
		String ratioSystem = cmd.getOptionValue("ratio");
		Ratio ratio = Ratios.getRatio(ratioSystem);
		if (ratio == null) {
			System.out.println("Note: unknown ratio system: " + ratioSystem
					+ ". Using default");
			ratio = DEFAULT_RATIO;
		}
		return ratio;
	}
	private static List<String> getCliPlayers(CommandLine cmd) {
		return Arrays.asList(cmd.getArgs());
	}
}
