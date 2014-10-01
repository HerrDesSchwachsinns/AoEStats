package balancer;

/*
 * based on algorithm found here:
 * http://nayuki.eigenstate.org/page/next-lexicographical-permutation-algorithm
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class Permutation implements Iterator<int[]>, Iterable<int[]> {
	private int[] values;
	private int nextPivot;
	private boolean isFirst = true;

	public Permutation(int[] values) {
		this.values = values;
		nextPivot = pivot();
	}
	@Override
	public int[] next() {
		if (isFirst) {
			isFirst = false;
			return values;
		}
		if (!hasNext())
			throw new NoSuchElementException("there is no next permutation");
		advance();
		return values;
	}
	@Override
	public boolean hasNext() {
		return nextPivot >= 0;
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	@Override
	public Iterator<int[]> iterator() {
		return this;
	}

	private void advance() {
		int thisPivot = nextPivot;
		int j = pivotSuccessor(thisPivot);

		swap(thisPivot, j);

		reverse(thisPivot + 1, values.length - 1);

		nextPivot = pivot();

		//return true;
	}
	private int pivot() {
		int i = values.length - 1;
		while (i > 0 && values[i - 1] >= values[i])
			--i;
		return i - 1;
	}
	private int pivotSuccessor(int pivot) {
		int j = values.length - 1;
		while (values[j] <= values[pivot])
			--j;
		return j;
	}
	private void swap(int i, int j) {
		int tmp = values[i];
		values[i] = values[j];
		values[j] = tmp;
	}
	private void reverse(int i, int j) {
		while (i < j) {
			swap(i, j);
			++i;
			--j;
		}
	}
	public static void main(String[] args) {
		//should be sorted to get all permutations
		Permutation p = new Permutation(new int[] { 1, 2, 3, 4 });
		int i = 1;
		for (int[] perm : p) {
			System.out.println(i + " " + Arrays.toString(perm));
			++i;
		}
	}
}