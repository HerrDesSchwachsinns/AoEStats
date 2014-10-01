package convert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import storage.DBStorage;
import storage.Storage;
import storage.StorageException;

public class Convert {
	private static String csvFile = "AoE Stats.csv";
	private static String db = "localhost/test";
	private static String user = "";
	private static String pswd = "";

	public static void main(String[] args) throws IOException, ParseException,
			StorageException, SQLException {

		try {
			csvFile = args[0];
			db = args[1];
			user = args[2];
			pswd = args[3];
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("usage: csvFile db user pswd");
			System.exit(1);
		}
		System.out.println(" ---- start converting " + csvFile + " ----");
		convert(csvFile);
		System.out.println("finished.");
		System.out.println(" ---- data in db ----");
		read();
	}
	public static void convert(String inputCSV) throws IOException,
			ParseException, StorageException {

		Storage storage = new DBStorage(db, user, pswd);
		System.out.println(" ---- deleting old db ----");
		storage.recreate();

		Stats stats = new Stats(inputCSV);
		DateFormat f = new SimpleDateFormat("dd.MM.yy");
		for (Stats.Game g : stats.games()) {
			System.out.println("writing " + g);
			storage.insertGame(f.parse(g.date), g.winners, g.loosers);
		}
	}
	public static void read() throws SQLException {
		try (Connection con = DriverManager
				.getConnection("jdbc:mysql://" + db, user, pswd)) {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM aoestats");

			while (rs.next()) {
				System.out.print(rs.getDate(1));
				System.out.print(" ");
				System.out.print(rs.getInt(2));
				System.out.print(" ");
				System.out.print(rs.getString(3));
				System.out.print(" ");
				System.out.print(rs.getString(4));
				System.out.println();
			}
		}
	}
}
