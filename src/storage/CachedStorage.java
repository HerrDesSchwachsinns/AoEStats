package storage;

import java.util.Date;
import java.util.List;

/**
 * Storage adapter that caches data from a storage engine in memory for
 * efficiency TODO implement!!!
 * 
 * @author vogl
 */
public class CachedStorage implements Storage {

	public CachedStorage(Storage storage) {
		this.storage = storage;
	}
	@Override
	public void recreate() throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Game> readAllGames() throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> readAllPlayers() throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertGame(Date date, String[] winners, String[] losers)
			throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateGame(Game g) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteGame(Date date, int number) throws StorageException {
		// TODO Auto-generated method stub

	}

	@Override
	public int countGames() throws StorageException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countGamesAtDate(Date d) throws StorageException {
		// TODO Auto-generated method stub
		return 0;
	}

	private Storage storage;
}
