package ratio;

public class Rating implements Comparable<Rating> {
	public final String player;
	public final double rating;

	public Rating(String player, double rating) {
		this.player = player;
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Rating [player=" + player + ", rating=" + rating + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rating);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Rating other = (Rating) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (Double.doubleToLongBits(rating) != Double
				.doubleToLongBits(other.rating))
			return false;
		return true;
	}

	@Override
	public int compareTo(Rating that) {
		return Double.compare(this.rating, that.rating);
	}

}
