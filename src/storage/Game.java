package storage;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Game is the key data storage class. It contains information about when the
 * game was played and who won/lost. Games are considered equal if they are
 * played at the same date with the same number. Winners and losers are not
 * considered for comparison.
 * 
 * @author vogl
 */
public class Game implements Comparable<Game> {
	public final Date date;
	public final int number;
	public final String[] winners;
	public final String[] losers;

	/**
	 * Value constructor
	 * 
	 * @param date
	 * @param number
	 *            negative says not specified
	 * @param winners
	 * @param losers
	 */
	public Game(Date date, int number, String[] winners, String[] losers) {
		this.date = date;
		this.number = number;
		this.winners = winners;
		this.losers = losers;
	}
	/**
	 * Value constructor, use this if you create a new Game with unspecified
	 * number.
	 * 
	 * @param date
	 * @param winners
	 * @param losers
	 */
	public Game(Date date, String[] winners, String[] losers) {
		this.number = -1;
		this.date = date;
		this.winners = winners;
		this.losers = losers;
	}
	public String[] players() {
		String[] players = new String[winners.length + losers.length];
		System.arraycopy(winners, 0, players, 0, winners.length);
		System.arraycopy(losers, 0, players, winners.length, losers.length);
		return players;
	}
	public Stream<String> playersStream() {
		return Arrays.stream(players());
	}
	@Override
	public String toString() {
		return "( "//
				+ String.join(" ",//
						date.toString() + " - " + number,//
						"won[" + String.join(", ", winners) + "]",//
						"lost[" + String.join(", ", losers) + "]")//
				+ " )";
	}
	@Override
	public int compareTo(Game that) {
		int date = this.date.compareTo(that.date);
		if (date == 0)
			return this.number - that.number;
		return date;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + number;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

}
