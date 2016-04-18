package org.intelidz.quicksort;

/**
 * Created by ctretyak on 08.04.2016.
 */
public interface Sorting {
	int[] sort(int[] unsortedArray);

	static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
}
