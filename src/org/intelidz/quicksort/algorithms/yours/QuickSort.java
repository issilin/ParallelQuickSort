package org.intelidz.quicksort.algorithms.yours;

import org.intelidz.quicksort.Sorting;

import java.util.*;
//import java.util.concurrent.
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by ctretyak on 08.04.2016.
 */
public class QuickSort implements Sorting {

    private final Random random = new Random();

    public QuickSort() {
    }

    private void arraySeparation(int[] unsortedArray, int processors, List<int[]> arrays) {
        int subArraySize = unsortedArray.length / processors;

        for (int i = 0; i < processors - 1; i++) {
            int[] arrayPart = new int[subArraySize];
            System.arraycopy(unsortedArray, subArraySize * i, arrayPart, 0, subArraySize);
            arrays.add(arrayPart);
        }

        int[] lastPart = new int[subArraySize + (unsortedArray.length % processors)];
        System.arraycopy(unsortedArray, subArraySize * (processors - 1), lastPart, 0, lastPart.length);
        arrays.add(lastPart);
    }

    @Override
    public int[] sort(int[] unsortedArray) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        List<int[]> arrays = new ArrayList<>();

        arraySeparation(unsortedArray, processors, arrays);

        List<Future> futures = parallelSort(arrays, executor);

        futures.forEach(this::wait);

        Future mergeFuture = executor.submit(() -> startMerge(unsortedArray, arrays));
        Future finishFuture = executor.submit(() -> endMerge(unsortedArray, arrays));

        wait(mergeFuture);
        wait(finishFuture);

        return unsortedArray;
    }

    private void startMerge(int[] resultArray, List<int[]> arrays) {
        int[] min = arrays.get(0);

        int[] indexes = new int[arrays.size()];

        for (int i = 0; i < arrays.size(); i++) {
            indexes[i] = 0;
        }

        int minNumber = 0;

        for (int i = 0; i < resultArray.length / 2; i++) {
            for (int j = 0; j < arrays.size(); j++) {
                if (arrays.get(j)[indexes[j]] <= min[indexes[minNumber]]) {
                    min = arrays.get(j);
                    minNumber = j;
                }
            }

            resultArray[i] = min[indexes[minNumber]];
            if (indexes[minNumber] == min.length - 1) {
                min[indexes[minNumber]] = Integer.MAX_VALUE;
            } else {
                indexes[minNumber]++;
            }
        }
    }

    private void endMerge(int[] resultArray, List<int[]> arrays) {
        int[] max = arrays.get(0);

        int[] indexes = new int[arrays.size()];
        for (int i = 0; i < arrays.size(); i++) {
            indexes[i] = arrays.get(i).length - 1;
        }

        int maxNumber = arrays.indexOf(max);

        for (int i = resultArray.length - 1; i >= resultArray.length / 2; i--) {
            for (int j = 0; j < arrays.size(); j++) {
                if (arrays.get(j)[indexes[j]] >= max[indexes[maxNumber]]) {
                    max = arrays.get(j);
                    maxNumber = j;
                }
            }

            resultArray[i] = max[indexes[maxNumber]];
            if (indexes[maxNumber] == 0) {
                max[indexes[maxNumber]] = -1;
            } else {
                indexes[maxNumber]--;
            }
        }
    }

    private void wait(Future future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private List<Future> parallelSort(List<int[]> arrays, ExecutorService executor) {
        return arrays.stream().map(arrayPart -> executor.submit(() -> qsort(arrayPart, 0, arrayPart.length - 1))).collect(Collectors.toList());
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