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

import org.cicirello.permutations.Permutation;
import org.cicirello.permutations.distance.NormalizedPermutationDistanceMeasurer;

/**
 * <p>K-Cycle distance is the count of the number of non-singleton permutation cycles
 * of length at most K.</p>
 *
 * <p>Runtime: O(n), where n is the permutation length.</p>
 * 
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class KCycleDistance implements NormalizedPermutationDistanceMeasurer {
	
	private final int maxCycleLength;
	
	/**
	 * Constructs the distance measurer as specified in the class documentation.
	 * @param k The maximum length cycle that is considered an atomic edit operation
	 */
	public KCycleDistance(int k) {
		this.maxCycleLength = k;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if p1.length() is not equal to p2.length().
	 */
    @Override
	public int distance(Permutation p1, Permutation p2) {
		if (p1.length() != p2.length()) {
			throw new IllegalArgumentException("Permutations must be the same length");
		}
        boolean[] used = new boolean[p1.length()];
		for (int k = 0; k < used.length; k++) {
			if (p1.get(k) == p2.get(k)) {
				used[p1.get(k)] = true;
			}
		}
		int i = 0;
		for (i = 0; i < used.length; i++) {
			if (!used[p1.get(i)]) { 
				break; 
			}  
        }
		
		int[] invP1 = p1.getInverse();
		int cycleCount = 0;
		int iLast = i;
		
		while (i < used.length) {
			int j = p1.get(i);
			int cycleLength = 0;
			while (!used[j]) {
				used[j] = true;
				cycleLength++;
				j = p2.get(i);
				i = invP1[j];
            }
			
			if (cycleLength > maxCycleLength) {
				cycleCount += (cycleLength - maxCycleLength) / (maxCycleLength - 1);
				if (((cycleLength - maxCycleLength) % (maxCycleLength - 1)) > 0) {
					cycleCount += 2;
				} else {
					cycleCount++;
				}
			} else {
				cycleCount++;
			}
			
			for (i = iLast + 1; i < used.length; i++) {
				if (!used[p1.get(i)]) {  
					break; 
				}
			}
			iLast = i;
		}
		return cycleCount;
	}
	
	@Override
	public int max(int length) {
		return length >> 1;
	}
}
