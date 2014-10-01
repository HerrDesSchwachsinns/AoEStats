package storage;

import java.util.Date;
import java.util.List;

/**
 * Storage is the unified interface for storage device implementations such as
 * databases or files
 * 
 * @author vogl
 */
public interface Storage {
	/**
	 * recreate the structure for storing games. This method deletes existing
	 * data!
	 * 
	 * @throws StorageException
	 *             if something goes wrong
	 */
	public void recreate() throws StorageException;
	/**
	 * grab all stored games into a list
	 * 
	 * @return list of all stored games
	 * @throws StorageException
	 *             if something goes wrong
	 */
	public List<Game> readAllGames() throws StorageException;
	/**
	 * grab all stored players into a list
	 * 
	 * @return list of all stored players
	 * @throws StorageException
	 */
	public List<String> readAllPlayers() throws StorageException;
	/**
	 * insert new game at date with winners and losers as specified
	 * 
	 * @param date
	 * @param winners
	 * @param losers
	 * @throws StorageException
	 *             if something goes wrong
	 */
	public void insertGame(Date date, String[] winners, String[] losers)
			throws StorageException;
	public void updateGame(Game g) throws StorageException;
	/**
	 * delete game at date with number
	 * 
	 * @param date
	 * @param number
	 * @throws StorageException
	 *             if something goes wrong
	 */
	public void deleteGame(Date date, int number) throws StorageException;
	/**
	 * count number of played games
	 * 
	 * @return number of games
	 * @throws StorageException
	 *             if something goes wrong
	 */
	public int countGames() throws StorageException;
	/**
	 * count number of played games at date
	 * 
	 * @param date
	 * @return number of games at date
	 * @throws StorageException
	 *             if something goes wrong
	 */
	public int countGamesAtDate(Date date) throws StorageException;
}
