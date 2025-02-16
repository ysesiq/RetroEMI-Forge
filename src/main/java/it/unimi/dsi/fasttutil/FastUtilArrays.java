/*
 * Copyright (C) 2002-2023 Sebastiano Vigna
 *
 * Modified by the BTW Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unimi.dsi.fasttutil;


import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

/**
 * A class providing static methods and objects that do useful things with arrays.
 *
 * <p>In addition to commodity methods, this class contains {link it.unimi.dsi.fastutil.Swapper}-based implementations
 * of {@linkplain #quickSort(int, int, Comparator, Swapper)}  quicksort}
 * These generic sorting methods can be used to sort any kind of list, but they find their natural
 * usage, for instance, in sorting arrays in parallel.
 *
 * <p>Some algorithms provide a parallel version that will by default use the
 * {@linkplain ForkJoinPool#commonPool() common pool}, but this can be overridden by calling the
 * function in a task already in the {@link ForkJoinPool} that the operation should run in. For example,
 * something along the lines of "{@code poolToParallelSortIn.invoke(() -> parallelQuickSort(arrayToSort))}"
 * will run the parallel sort in {@code poolToParallelSortIn} instead of the default pool.
 *
 * see Arrays
 */

public class FastUtilArrays {

	private static final int QUICKSORT_NO_REC = 16;
	private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
	private static final int QUICKSORT_MEDIAN_OF_9 = 128;

	/**
	 * Swaps two sequences of elements using a provided swapper.
	 *
	 * @param swapper the swapper.
	 * @param a       a position in {@code x}.
	 * @param b       another position in {@code x}.
	 * @param n       the number of elements to exchange starting at {@code a} and {@code b}.
	 */
	protected static void swap(final Swapper swapper, int a, int b, final int n) {
		for (int i = 0; i < n; i++, a++, b++) {
			swapper.swap(a, b);
		}
	}

	/**
	 * Returns the index of the median of the three indexed chars.
	 */
	private static int med3(final int a, final int b, final int c, final Comparator comp) {
		final int ab = comp.compare(a, b);
		final int ac = comp.compare(a, c);
		final int bc = comp.compare(b, c);
		return (ab < 0 ? (bc < 0 ? b : ac < 0 ? c : a) : (bc > 0 ? b : ac > 0 ? c : a));
	}

	/**
	 * Sorts the specified range of elements using the specified swapper and according to the order induced by the specified
	 * comparator using parallel quicksort.
	 *
	 * <p>The sorting algorithm is a tuned quicksort adapted from Jon L. Bentley and M. Douglas
	 * McIlroy, &ldquo;Engineering a Sort Function&rdquo;, <i>Software: Practice and Experience</i>, 23(11), pages
	 * 1249&minus;1265, 1993.
	 *
	 * @param from    the index of the first element (inclusive) to be sorted.
	 * @param to      the index of the last element (exclusive) to be sorted.
	 * @param comp    the comparator to determine the order of the generic data.
	 * @param swapper an object that knows how to swap the elements at any two positions.
	 */
	public static void quickSort(final int from, final int to, final Comparator comp, final Swapper swapper) {
		final int len = to - from;
		// Insertion sort on smallest arrays
		if (len < QUICKSORT_NO_REC) {
			for (int i = from; i < to; i++) {
				for (int j = i; j > from && (comp.compare(j - 1, j) > 0); j--) {
					swapper.swap(j, j - 1);
				}
			}
			return;
		}

		// Choose a partition element, v
		int m = from + len / 2; // Small arrays, middle element
		int l = from;
		int n = to - 1;
		if (len > QUICKSORT_MEDIAN_OF_9) { // Big arrays, pseudomedian of 9
			final int s = len / 8;
			l = med3(l, l + s, l + 2 * s, comp);
			m = med3(m - s, m, m + s, comp);
			n = med3(n - 2 * s, n - s, n, comp);
		}
		m = med3(l, m, n, comp); // Mid-size, med of 3
		// int v = x[m];

		int a = from;
		int b = a;
		int c = to - 1;
		// Establish Invariant: v* (<v)* (>v)* v*
		int d = c;
		while (true) {
			int comparison;
			while (b <= c && ((comparison = comp.compare(b, m)) <= 0)) {
				if (comparison == 0) {
					// Fix reference to pivot if necessary
					if (a == m) {
						m = b;
					}
					else if (b == m) {
						m = a;
					}
					swapper.swap(a++, b);
				}
				b++;
			}
			while (c >= b && ((comparison = comp.compare(c, m)) >= 0)) {
				if (comparison == 0) {
					// Fix reference to pivot if necessary
					if (c == m) {
						m = d;
					}
					else if (d == m) {
						m = c;
					}
					swapper.swap(c, d--);
				}
				c--;
			}
			if (b > c) {
				break;
			}
			// Fix reference to pivot if necessary
			if (b == m) {
				m = d;
			}
			else if (c == m) {
				m = c;
			}
			swapper.swap(b++, c--);
		}

		// Swap partition elements back to middle
		int s;
		s = Math.min(a - from, b - a);
		swap(swapper, from, b - s, s);
		s = Math.min(d - c, to - d - 1);
		swap(swapper, b, to - s, s);

		// Recursively sort non-partition-elements
		if ((s = b - a) > 1) {
			quickSort(from, from + s, comp, swapper);
		}
		if ((s = d - c) > 1) {
			quickSort(to - s, to, comp, swapper);
		}
	}
}
