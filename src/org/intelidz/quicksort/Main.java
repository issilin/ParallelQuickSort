package org.intelidz.quicksort;

import org.intelidz.quicksort.algorithms.origin.QuickSortWithoutParallelization;
import org.intelidz.quicksort.algorithms.yours.QuickSort;
import org.intelidz.quicksort.algorithms.yours.WithoutParallelMerge;

import java.util.Arrays;
import java.util.Random;

public class Main {

	private static final Random random = new Random();

	private static int minSize = Integer.MAX_VALUE / 75;
	private static int maxSize = minSize;

	public static void main(String[] args) {
		int[] array = createAndFillArray(randomInt(minSize, maxSize));
		int[] sorted = array.clone();
		Arrays.sort(sorted);
		measureTime(new QuickSortWithoutParallelization(), array, sorted);
		measureTime(new QuickSort(), array, sorted);
		measureTime(new WithoutParallelMerge(), array, sorted);
	}

	private static int[] createAndFillArray(int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = randomInt(0, size);
		}
		return result;
	}

	private static int randomInt(int min, int max) {
		int randomNum = random.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	private static void measureTime(Sorting sorting, int[] array, int[] sorted) {
		int[] cloned = array.clone();
		long start = System.currentTimeMillis();
		int[] result = sorting.sort(cloned);
		long end = System.currentTimeMillis();

		if (!Arrays.equals(result, sorted)) {
			System.out.println("RESULT: " + sorting.getClass().getName() + ": " + " Sorting is incorrect.");
			return;
		}
		System.out.println("RESULT: " + sorting.getClass().getName() + ": " + (end - start) + " milliseconds");
	}
}
