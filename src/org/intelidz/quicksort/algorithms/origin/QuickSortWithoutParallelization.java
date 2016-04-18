package org.intelidz.quicksort.algorithms.origin;

import org.intelidz.quicksort.Sorting;

import java.util.Random;

/**
 * Created by ctretyak on 08.04.2016.
 */
public class QuickSortWithoutParallelization implements Sorting {
	private final Random random = new Random();

	public QuickSortWithoutParallelization() {
	}

	@Override
	public int[] sort(int[] unsortedArray) {
		qsort(unsortedArray, 0, unsortedArray.length - 1);
		return unsortedArray;
	}

	public void qsort(int[] array, int left, int right) {
		if (left < right) {
			int pivot = selectPivot(array, left, right);
			qsort(array, left, pivot);
			qsort(array, pivot + 1, right);
		}
	}

	private int selectPivot(int[] array, int left, int right) {
		int swapIndex = left + random.nextInt(right - left) + 1;
		Sorting.swap(array, left, swapIndex);
		return partition(array, left, right);
	}

	private static int partition(int[] arr, int left, int right) {
		int pivot = arr[left];
		int i = left - 1;
		int j = right + 1;
		while (true) {
			do {
				j--;
			}
			while (arr[j] > pivot);

			do {
				i++;
			} while (arr[i] < pivot);

			if (i < j) {
				Sorting.swap(arr, i, j);
			} else {
				return j;
			}
		}
	}

}
