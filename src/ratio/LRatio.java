package ratio;

public class LRatio extends SimpleRatio {

	public LRatio(double lcurve_steepness, double lcurve_bending,
			int zlc_NoM_boundary, double zlc_leverage, double zwc_leverage) {
		this.lcurve_steepness = lcurve_steepness;
		this.lcurve_bending = lcurve_bending;
		this.zlc_NoM_boundary = zlc_NoM_boundary;
		this.zlc_leverage = zlc_leverage;
		this.zwc_leverage = zwc_leverage;
	}
	public LRatio() {
		this(0.3, 20, 4, 6, 0.05);
	}

	@Override
	public double ratio(int wins, int losses) {
		return lratio(wins, losses);
	}

	private double lratio(int wins, int losses) {
		int games = wins + losses;
		if (wins == 0 && losses == 0) return 0;
		else if (wins == 0 && losses != 0) return zwc_leverage
				/ (double) losses;
		else if (wins != 0 && losses == 0 && wins < zlc_NoM_boundary) return logistic_curve(games);
		else if (wins != 0 && losses == 0 && wins >= zlc_NoM_boundary) return (double) wins
				/ zlc_leverage;
		else return wlratio(wins, losses) * logistic_curve(games);
	}

	private double wlratio(int wins, int losses) {
		return (double) wins / (double) losses;
	}

	private double logistic_curve(int games) {
		return 1.0 / (1.0 + lcurve_bending
				* Math.exp(-lcurve_steepness * (double) games));
	}

	private double lcurve_steepness;// = 0.3;
	private double lcurve_bending;// = 20;
	private int zlc_NoM_boundary;// = 4;
	private double zlc_leverage;// = 6;
	private double zwc_leverage;// = 0.05;
}
