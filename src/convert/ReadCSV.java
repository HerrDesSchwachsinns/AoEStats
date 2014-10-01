package convert;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ReadCSV {
	public static void main(String[] args) throws IOException {
		ReadCSV csv = new ReadCSV("AoE Stats.csv", ',');

		String one = csv.header()[1];
		String two = csv.header()[3];
		System.out.println(csv.header()[0] + "\t" + one + "\t" + two);
		for (Line elems = csv.readLine(); elems != null; elems = csv.readLine()) {
			System.out.print(elems.get(0) + "\t");
			System.out.print(elems.get(one) + "\t");
			System.out.println(elems.get(two));
		}
	}

	public static class Line {
		public final String[] data;
		public final ReadCSV csv;

		public String get(int i) {
			return data[i];
		}
		public String get(String row) {
			int i = csv.headerName2Index(row);
			if (i < 0)
				throw new ArrayIndexOutOfBoundsException(row);
			return get(i);
		}
		private Line(String[] data, ReadCSV csv) {
			this.data = data;
			this.csv = csv;
		}

	}

	public static char DEFAULT_DELIM = ',';

	private BufferedReader reader;
	private char delim;
	private String[] header;

	public ReadCSV(String filename, char delim) throws IOException {
		reader = new BufferedReader(new FileReader(filename));
		this.delim = delim;
		readHeader();
	}
	public ReadCSV(String filename) throws IOException {
		this(filename, DEFAULT_DELIM);
	}
	public Line readLine() throws IOException {
		String elems[] = readLine_noHeader();
		if (elems == null)
			return null;
		return new Line(adjust2HeaderSize(elems), this);
	}
	public String[] header() {
		return header;
	}

	private int headerName2Index(String headerName) {
		for (int i = 0; i < header.length; ++i) {
			if (header[i].equals(headerName))
				return i;
		}
		return -1;
	}

	private String[] readLine_noHeader() throws IOException {
		String line = reader.readLine();
		if (line == null)
			return null;
		String[] elems = line.split(String.valueOf(delim));
		return elems;
	}
	private String[] adjust2HeaderSize(String[] elems) {
		if (elems.length < header.length) {
			String[] tmp = new String[header.length];
			System.arraycopy(elems, 0, tmp, 0, elems.length);
			Arrays.fill(tmp, elems.length, header.length, "");
			elems = tmp;
		}
		return elems;
	}
	private void readHeader() throws IOException {
		header = readLine_noHeader();
	}
}
