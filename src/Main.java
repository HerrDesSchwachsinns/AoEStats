import cli.CliMain;

public class Main {
	public static void main(String[] args) {
		args = new String[] { "--plot", "--mode=stats", "-rwinlossseries",
				"Joe" };
		CliMain.main(args);
	}
}
