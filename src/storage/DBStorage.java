package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DBStorage implements Storage {

	private final static String DATE = "date";
	private final static String GAME_ON_DAY = "game_on_day";
	private final static String WINNERS = "winners";
	private final static String LOSERS = "losers";

	private final static String TABLE = "aoestats";

	public DBStorage(String url, String user, String pswd) {
		this.url = url;
		this.user = user;
		this.pswd = pswd;
	}

	private final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;
	private final static String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" //
			+ DATE + " DATE NOT NULL, "//
			+ GAME_ON_DAY + " INT NOT NULL, "//
			+ WINNERS + " VARCHAR(256) NOT NULL, "//
			+ LOSERS + " VARCHAR(256) NOT NULL, "//
			+ "PRIMARY KEY(" + DATE + ", " + GAME_ON_DAY + ")" + ")";

	public void recreate() throws StorageException {
		try (Connection con = connect()) {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(DROP_TABLE);
			stmt.executeUpdate(CREATE_TABLE);
		} catch (SQLException e) {
			throw new StorageException("recreateTable", e);
		}
	}

	private final static String SELECT_ALL_GAMES = "SELECT "
			+ String.join(",", DATE, GAME_ON_DAY, WINNERS, LOSERS) + " FROM "
			+ TABLE;

	/**
	 * read all games
	 * 
	 * @return
	 * @throws StorageException
	 */
	@Override
	public List<Game> readAllGames() throws StorageException {
		try (Connection con = connect()) {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_GAMES);

			List<Game> games = new ArrayList<>();

			while (rs.next()) {
				games.add(new Game(rs.getDate(DATE), rs.getInt(GAME_ON_DAY), rs
						.getString(WINNERS).split(","), rs.getString(LOSERS)
						.split(",")));
			}
			return games;
		} catch (SQLException e) {
			throw new StorageException("readAllGames", e);
		}
	}
	/**
	 * read all games and extract all players
	 * 
	 * @return
	 * @throws StorageException
	 */
	@Override
	public List<String> readAllPlayers() throws StorageException {
		List<Game> games = readAllGames();
		Set<String> players = new TreeSet<String>();

		games.stream().flatMap(Game::playersStream).forEach(players::add);

		List<String> playerrList = new ArrayList<String>();
		players.forEach(playerrList::add);
		return playerrList;
	}

	private final static String DELETE_GAME = "DELETE FROM " + TABLE
			+ " WHERE " + DATE + "=? AND " + GAME_ON_DAY + "=?";

	/**
	 * delete {number}th game at date
	 * 
	 * @param date
	 * @param number
	 * @throws StorageException
	 */
	@Override
	public void deleteGame(Date date, int number) throws StorageException {
		try (Connection con = connect()) {
			PreparedStatement stmt = con.prepareStatement(DELETE_GAME);
			stmt.setString(1, packDate(date));
			stmt.setInt(2, number);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new StorageException("delete", e);
		}
	}

	private final static String COUNT_GAMES = "SELECT COUNT(*) FROM " + TABLE;

	@Override
	public int countGames() throws StorageException {
		try (Connection con = connect()) {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(COUNT_GAMES);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new StorageException("countGames", e);
		}
	}

	private final static String COUNT_GAMES_DATE = "SELECT COUNT(*) FROM "
			+ TABLE + " WHERE " + DATE + "=?";

	@Override
	public int countGamesAtDate(Date date) throws StorageException {
		try (Connection con = connect()) {
			PreparedStatement stmt = con.prepareStatement(COUNT_GAMES_DATE);
			stmt.setString(1, packDate(date));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new StorageException("countGamesAtDate", e);
		}
	}

	private final static String UPDATE_GAME = "UPDATE " + TABLE + " SET "//
			+ WINNERS + "=?, " + LOSERS + "=? "//
			+ " WHERE " + DATE + "=? AND " + GAME_ON_DAY + "=?";

	/**
	 * update game
	 * 
	 * @param con
	 * @param g
	 * @return
	 * @throws SQLException
	 */
	public void updateGame(Game g) throws StorageException {
		try (Connection con = connect()) {
			PreparedStatement stmt = con.prepareStatement(UPDATE_GAME);
			stmt.setString(1, packPlayerList(g.winners));
			stmt.setString(2, packPlayerList(g.losers));
			stmt.setString(3, packDate(g.date));
			stmt.setInt(4, g.number);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new StorageException("updateGame", e);
		}
	}

	private final static String INSERT_GAME = "INSERT INTO " + TABLE + "("
			+ String.join(",", DATE, GAME_ON_DAY, WINNERS, LOSERS) + ")"
			+ " VALUES(?,?,?,?)";

	/**
	 * insert a new game at date with winners and losers
	 * 
	 * @param con
	 * @param date
	 * @param winners
	 * @param losers
	 * @return
	 * @throws SQLException
	 * @throws StorageException
	 */
	public void insertGame(Date date, String[] winners, String[] losers)
			throws StorageException, StorageException {
		try (Connection con = connect()) {
			PreparedStatement stmt = con.prepareStatement(INSERT_GAME);
			stmt.setString(1, packDate(date));
			stmt.setInt(2, nextNumberAtDate(date));
			stmt.setString(3, packPlayerList(winners));
			stmt.setString(4, packPlayerList(losers));
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new StorageException("insertGame", e);
		}
	}

	private final static String NEXT_NUM_DATE = "SELECT " + GAME_ON_DAY
			+ " FROM " + TABLE + " WHERE " + DATE + "=? ORDER BY "
			+ GAME_ON_DAY + " DESC LIMIT 1";

	private int nextNumberAtDate(Date date) throws StorageException {
		try (Connection con = connect()) {
			PreparedStatement stmt = con.prepareStatement(NEXT_NUM_DATE);
			stmt.setString(1, packDate(date));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) return rs.getInt(1) + 1;
			else return 1;
		} catch (SQLException e) {
			throw new StorageException("nextNumberAtDate", e);
		}
	}

	private String packPlayerList(String[] playerList) {
		return String.join(",", playerList);
	}
	private String packDate(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	private Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + url, user, pswd);
	}

	private String url;
	private String user;
	private String pswd;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {}
	}
}
