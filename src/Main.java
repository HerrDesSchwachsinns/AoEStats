import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jfree.chart.ChartPanel;
import org.jfree.ui.ApplicationFrame;

import ratio.LRatio;
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

/**
 * This is a big fat mess and should be tidied up (more structure less
 * spaghetti)
 * 
 * @author vogl
 */
public class Main {
	public static void main(String[] args) {
		args = new String[] { "-c", "-rlratio", "GrandJM", "Joe", "HDS",
				"Nesto", "Alex", "Der Bayer" };
		Options options = createOptions();
		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("help")) throw new ParseException("");
			Storage storage = new DBStorage(
					cmd.getOptionValue("server", "localhost/test"),
					cmd.getOptionValue("user", ""),
					cmd.getOptionValue("paswd", ""));
			try {
				List<Game> games = storage.readAllGames();

				List<String> players;
				String[] playerArgs = cmd.getArgs();
				if (playerArgs.length > 0) {
					players = new ArrayList<>();
					Collections.addAll(players, playerArgs);
				} else players = storage.readAllPlayers();

				Ratio ratio;
				switch (cmd.getOptionValue("ratio", "lratio")) {
				case "simple":
					ratio = new SimpleRatio();
					break;
				default:
					System.out
							.println("Note: unkown ratio system. using lratio");
				case "lratio":
					ratio = new LRatio();
					break;
				case "randomwalk":
					ratio = new NotSoRandomWalk();
					break;
				case "movingaverage":
					ratio = new MovingAverage(new LRatio());
					break;
				case "longestwin":
					ratio = new LongestWinSeries();
					break;
				}

				printRatio(players, games, ratio);

				//				Balancer balancer = new PermutatingBalancer(
				//						ratio.get(games, players));
				//				System.out.println("Team 1");
				//				System.out.print(balancer.getTeam(0) + " ");
				//				System.out.format("%1$.2f%n", balancer.getRating(0));
				//				System.out.print(balancer.getTeam(1) + " ");
				//				System.out.format("%1$.2f%n", balancer.getRating(1));
				//				System.out.format("%1$.2f%n", Math.abs(balancer.getRating(0)
				//						- balancer.getRating(1)));

				if (cmd.hasOption("chart"))
					createChartFrame(players, games, ratio, cmd.hasOption("smooth"));
			} catch (StorageException e) {
				e.printStackTrace();
			}
		} catch (ParseException e1) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("aoestats [options] [players...]", options);
			System.exit(0);
		}
	}
	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options options = new Options();
		options.addOption(new Option("h", "help", false, "print this help."));
		options.addOption(new Option("c", "chart", false, "plot data to chart."));
		options.addOption(new Option("m", "smooth", false, "smooth chart."));
		options.addOption(OptionBuilder //
				.withLongOpt("ratio") //
				.withDescription("select ratio system. This is one of {simple, lratio, randomwalk, movingaverage, longestwin}")//
				.hasArg() //
				.withArgName("SYSTEM") //
				.withValueSeparator() //
				.create('r'));
		options.addOption(OptionBuilder //
				.withLongOpt("server") //
				.withDescription("grab games from mysql server at url") //
				.hasArg() //
				.withArgName("URL") //
				.withValueSeparator() //
				.create('s'));
		options.addOption(OptionBuilder //
				.withLongOpt("user") //
				.withDescription("user name for mysql server") //
				.hasArg() //
				.withArgName("NAME") //
				.withValueSeparator() //
				.create("u"));
		options.addOption(OptionBuilder //
				.withLongOpt("pswd") //
				.withDescription("password for mysql server") //
				.hasArg() //
				.withArgName("PASSWORD") //
				.withValueSeparator() //
				.create("p"));
		return options;
	}
	private static Frame createChartFrame(List<String> players, List<Game> games, Ratio ratio, boolean smooth) {
		ChartPanel chartPanel = new RatioChartPanel(players, games, ratio,
				smooth);
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
		return frame;
	}
	private static void printRatio(List<String> players, List<Game> games, Ratio ratio) {
		List<Rating> rating = ratio.get(games, players);
		rating.sort(Collections.reverseOrder());
		int maxLength = rating.stream().mapToInt(r -> r.player.length()).max()
				.orElse(0);
		rating.forEach(r -> printRating(r, maxLength));
	}
	private static void printRating(Rating r, int maxLength) {
		System.out
				.format(Locale.ROOT, "%1$-" + (maxLength + 1) + "s%2$.2f%n", r.player, r.rating);
	}
}
