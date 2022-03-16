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

import java.util.ArrayList;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.operators.UndoableMutationOperator;
import org.cicirello.search.operators.permutations.SwapMutation;
import org.cicirello.search.operators.permutations.InsertionMutation;
import org.cicirello.search.operators.permutations.ReversalMutation;
import org.cicirello.search.operators.permutations.CycleMutation;
import org.cicirello.search.operators.permutations.UndoableScrambleMutation;
import org.cicirello.search.problems.LargestCommonSubgraph;
import org.cicirello.search.sa.SimulatedAnnealing;
import org.cicirello.search.ProgressTracker;
import org.cicirello.search.operators.permutations.PermutationInitializer;

/**
 * Experiments with the Largest Common Subgraph Problem and with Simulated Annealing,
 * with a strongly regular instance of the LCS.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class LCSExperimentsStronglyRegularSA {
	
	/**
	 * Runs the experiments.
	 * @param args No command line arguments.
	 */
	public static void main(String[] args) {
		
		// First graph is the Generalized Petersen Graph G(25, 2), which has 50 vertexes and 75 edges.
		// Second graph is a relabeling of the first to derive a pair if isomorphic graphs.
		final int N = 25;
		final int K = 2;
		final int PERMUTATION_LENGTH = N * 2;
		
		final int NUM_INSTANCES = 50;
		
		final int MIN_EVALUATIONS = 100;
		final int MAX_EVALUATIONS = 10000000;
		
		ArrayList<UndoableMutationOperator<Permutation>> mutationOps = new ArrayList<UndoableMutationOperator<Permutation>>();
		ArrayList<String> columnLabels = new ArrayList<String>();
		
		mutationOps.add(new CycleMutationExperimental(0.75));
		columnLabels.add("Cycle(0.75)");
		
		mutationOps.add(new CycleMutationExperimental(0.5));
		columnLabels.add("Cycle(0.5)");
		
		mutationOps.add(new CycleMutationExperimental(0.25));
		columnLabels.add("Cycle(0.25)");
		
		mutationOps.add(new CycleMutation(5));
		columnLabels.add("Cycle(5)");
		
		mutationOps.add(new CycleMutation(4));
		columnLabels.add("Cycle(4)");
		
		mutationOps.add(new CycleMutation(3));
		columnLabels.add("Cycle(3)");
		
		mutationOps.add(new SwapMutation());
		columnLabels.add("Swap");
		
		mutationOps.add(new InsertionMutation());
		columnLabels.add("Insertion");
		
		mutationOps.add(new ReversalMutation());
		columnLabels.add("Reversal");
		
		mutationOps.add(new UndoableScrambleMutation());
		columnLabels.add("Scramble");
		
		System.out.print("Instance\tEvaluations");
		for (String label : columnLabels) {
			System.out.print("\t" + label);
		}
		System.out.println();
		for (int seed = 1; seed <= NUM_INSTANCES; seed++) {
			LargestCommonSubgraph problem = LargestCommonSubgraph.createInstanceGeneralizedPetersenGraph(N, K, seed);
			
			ArrayList<SimulatedAnnealing<Permutation>> sas = new ArrayList<SimulatedAnnealing<Permutation>>();
			for (UndoableMutationOperator<Permutation> mutation : mutationOps) {
				sas.add(
					new SimulatedAnnealing<Permutation>(
						problem,
						mutation.split(),
						new PermutationInitializer(PERMUTATION_LENGTH)
					)
				);
			}
			
			for (int totalEvals = MIN_EVALUATIONS; totalEvals <= MAX_EVALUATIONS; totalEvals *= 10) {
				System.out.print(seed + "\t" + totalEvals);
				for (SimulatedAnnealing<Permutation> sa : sas) {
					sa.optimize(totalEvals);
					System.out.print("\t" + sa.getProgressTracker().getCost());
					sa.setProgressTracker(new ProgressTracker<Permutation>());
				}
				System.out.println();
				System.out.flush();
			}
		}
	}
}
