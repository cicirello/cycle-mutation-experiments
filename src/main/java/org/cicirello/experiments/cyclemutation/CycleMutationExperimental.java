/*
 * Experiments with the new cycle mutation operator.
 * Copyright (C) 2022 Vincent A. Cicirello
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
 
package org.cicirello.experiments.cyclemutation;

import org.cicirello.search.operators.UndoableMutationOperator;
import org.cicirello.permutations.Permutation;
import org.cicirello.math.rand.RandomIndexer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>This class implements the Cycle(&alpha;) form of cycle mutation on permutations, where one mutation
 * generates a random permutation cycle. Given the original parent permutation and
 * its mutant, a permutation cycle can be defined as follows. Imagine a graph with
 * n vertexes, where n is the permutation length. Now consider that for each index
 * i, we define an edge in that graph
 * between vertex parent[i] and vertex mutant[i]. A permutation cycle consists of
 * all of the elements from one of the cycles in that graph. The length of a cycle
 * is the number of elements in it. Consider an example permutation, p1 = [0, 1, 2, 3, 4],
 * and another permutation, p2 = [0, 3, 2, 1, 4]. This pair of permutations has a
 * 2-cycle (i.e., a cycle of length 2) consisting of elements 1 and 3.  Consider
 * a second example, p1 = [0, 1, 2, 3, 4], and p2 = [0, 4, 2, 1, 3]. This example has
 * a 3-cycle consisting of elements 1, 3, and 4.  Notice that position 1 has elements 1 and 4,
 * position 4 has elements 4 and 3, and position 3 has elements 3 and 1, so in the
 * hypothetical graph described above, there would be edges from 1 to 4, 4 to 3, and 3 to 1,
 * a cycle of length 3.</p>
 *
 * <p>The Cycle(&alpha;) version of cycle mutation chooses the cycle size randomly from {2, 3, ..., n} where
 * cycle length k is chosen with probability proportional to &alpha;<sup>k-2</sup>. It then generates
 * a random permutation cycle of length k. The combination of k elements is chosen uniformly at 
 * random from all possible combinations
 * of k elements. Note that a 2-cycle is simply a swap.</p>
 *
 * <p>The worst case runtime of a single call to 
 * the {@link #mutate(Permutation) mutate} method is O(n), which occurs when the randomly
 * chosen cycle length is n. However, this is a very low probability event. Lower cycle lengths
 * are given significantly higher probability.
 * The average case runtime of a single call to the 
 * {@link #mutate(Permutation) mutate} method is O(min(n, ((2-&alpha;)/(1-&alpha;))<sup>2</sup>)).
 * Thus, provided &alpha; is not close to 1, the average runtime is a constant depending upon 
 * the value of &alpha;.</p>
 *
 * <p>Cycle mutation in both of its forms, including Cycle(&alpha;), was introduced in the following article:</p>
 *
 * <p>Vincent A. Cicirello. 2022. Cycle Mutation: Evolving Permutations via Cycle Induction. 
 * <i>Applied Sciences</i>, 12, 11, Article 5506 (May 2022). 
 * <a href="https://doi.org/10.3390/app12115506">https://doi.org/10.3390/app12115506</a></p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class CycleMutationExperimental implements UndoableMutationOperator<Permutation> {
	
	private int[] indexes;
	private final double logAlpha;
	private final double alpha;
	
	// only recompute if permutation length different than last call to mutate
	private int lastN;
	private double term;
	
	/**
	 * Constructs an CycleMutationExperimental mutation operator.
	 * @param alpha The alpha parameter of the mutation operator (see class documentation).
	 * @throws IllegalArgumentException if alpha is less than or equal to 0 or greater than 1.
	 */
	public CycleMutationExperimental(double alpha) {
		if (alpha <= 0 || alpha > 1) throw new IllegalArgumentException("alpha is outside allowed range");
		logAlpha = Math.log(alpha);
		this.alpha = alpha;
	}
	
	@Override
	public final void mutate(Permutation c) {
		if (c.length() >= 2) {
			int k = computeK(c.length(), ThreadLocalRandom.current().nextDouble());
			indexes = RandomIndexer.sample(
				c.length(),
				k,
				(int[])null
			);
			if (indexes.length > 2) {
				for (int j = indexes.length - 1; j > 0; j--) {
					int i = RandomIndexer.nextInt(j+1);
					if (i != j) {
						int temp = indexes[i];
						indexes[i] = indexes[j];
						indexes[j] = temp;
					}
				}
			}
			c.cycle(indexes);
		}
	}
	
	@Override
	public final void undo(Permutation c) {
		if (c.length() >= 2) {
			if (indexes.length > 2) {
				for (int i = 0, j = indexes.length-1; i < j; i++, j--) {
					int temp = indexes[i];
					indexes[i] = indexes[j];
					indexes[j] = temp;
				}
			}
			c.cycle(indexes);
		}
	}
	
	@Override
	public CycleMutationExperimental split() {
		return new CycleMutationExperimental(alpha);
	}
	
	/*
	 * package access to support testing
	 */
	final int computeK(int n, double u) {
		if (n != lastN) {
			term = 1 - Math.pow(alpha, n-1);
			lastN = n;
		}
		return (int)(Math.log(1 - u * term) / logAlpha) + 2;
	}
}

