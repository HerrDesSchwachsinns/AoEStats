package ratio;

import java.util.HashMap;
import java.util.Map;

public class Ratios {
	public static Ratio getRatio(String name) {
		return get(name).ratio;
	}
	public static String getLongName(Ratio ratio) {
		return get(ratio).longName;
	}
	public static String getLongName(String name) {
		return get(name).longName;
	}
	public static double getZeroMarkerValue(Ratio ratio) {
		return get(ratio).zeroMarker;
	}
	public static double getZeroMarkerValue(String name) {
		return get(name).zeroMarker;
	}
	public static String[] getAllNames() {
		return info.keySet().toArray(new String[0]);
	}

	private static class RatioInfo {
		String name;
		String longName;
		double zeroMarker;
		Ratio ratio;

		public RatioInfo(String name, String longName, double zeroMarker,
				Ratio ratio) {
			this.name = name;
			this.longName = longName;
			this.zeroMarker = zeroMarker;
			this.ratio = ratio;
		}
	}

	private static HashMap<String, RatioInfo> info = new HashMap<>();
	private static RatioInfo NO_INFO;

	private static void addInfo(String name, String longName, Double zeroMarker, Ratio ratio) {
		info.put(name, new RatioInfo(name, longName, zeroMarker, ratio));
	}
	//get ratio info by name or NO_INFO
	private static RatioInfo get(String name) {
		return info.getOrDefault(name, NO_INFO);
	}
	//get ratio info by ratio class or NO_INFO
	private static RatioInfo get(Ratio ratio) {
		for (Map.Entry<String, RatioInfo> e : info.entrySet()) {
			if (e.getValue().ratio.getClass().equals(ratio.getClass()))
				return get(e.getKey());
		}
		return NO_INFO;
	}

	static {
		NO_INFO = new RatioInfo(null, null, 0, null);
		addInfo("simple", "Simple Win-Loss Ratio", 1d, new SimpleRatio());
		addInfo("lratio", "LRatio", 1d, new LRatio());
		addInfo("randomwalk", "(Not S) Random Walk", 1d, new NotSoRandomWalk());
		addInfo("movingaverage", "Moving Average", 1d, new MovingAverage(
				new LRatio()));
		addInfo("longestwin", "Longest Win Series", 0d, new LongestWinSeries());
		addInfo("longestloss", "Longest Loss Series", 0d, new LongestLossSeries());
		addInfo("winlossseries", "Win-Loss Series", 0d, new WinLossSeries());
	}
}
