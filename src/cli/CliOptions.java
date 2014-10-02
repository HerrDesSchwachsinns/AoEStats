package cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

@SuppressWarnings("serial")
public class CliOptions extends Options {
	@SuppressWarnings("static-access")
	public CliOptions() {
		addOption(OptionBuilder //
				.withLongOpt("mode") //
				.withDescription("select working mode. This is one of {stats, balance}")//
				.hasArg() //
				.withArgName("MODE") //
				.withValueSeparator() //
				.create('m'));

		addOption(new Option("h", "help", false, "print this help."));
		addOption(new Option("c", "plot", false, "plot data."));
		addOption(new Option("n", "smooth", false, "smooth chart."));
		addOption(new Option("d", "print", false, "print data to cmd."));

		addOption(OptionBuilder //
				.withLongOpt("ratio") //
				.withDescription("select ratio system. This is one of {simple, lratio, randomwalk, movingaverage, longestwin, longestloss, winlossseries}")//
				.hasArg() //
				.withArgName("SYSTEM") //
				.withValueSeparator() //
				.create('r'));
		addOption(OptionBuilder //
				.withLongOpt("balancer") //
				.withDescription("select balancing system. This is one of {}")//
				.hasArg() //
				.withArgName("BALANCER") //
				.withValueSeparator() //
				.create('b'));

		addOption(OptionBuilder //
				.withLongOpt("server") //
				.withDescription("grab games from mysql server at url") //
				.hasArg() //
				.withArgName("URL") //
				.withValueSeparator() //
				.create('s'));
		addOption(OptionBuilder //
				.withLongOpt("user") //
				.withDescription("user name for mysql server") //
				.hasArg() //
				.withArgName("NAME") //
				.withValueSeparator() //
				.create("u"));
		addOption(OptionBuilder //
				.withLongOpt("pswd") //
				.withDescription("password for mysql server") //
				.hasArg() //
				.withArgName("PASSWORD") //
				.withValueSeparator() //
				.create("p"));
	}
}
