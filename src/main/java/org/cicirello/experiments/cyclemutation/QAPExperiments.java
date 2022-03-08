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
import org.cicirello.search.operators.MutationOperator;
import org.cicirello.search.operators.permutations.SwapMutation;
import org.cicirello.search.operators.permutations.InsertionMutation;
import org.cicirello.search.operators.permutations.ReversalMutation;
import org.cicirello.search.operators.permutations.CycleMutation;
import org.cicirello.search.operators.permutations.ScrambleMutation;
import org.cicirello.search.problems.QuadraticAssignmentProblem;
import org.cicirello.search.evo.GenerationalMutationOnlyEvolutionaryAlgorithm;
import org.cicirello.search.evo.NegativeIntegerCostFitnessFunction;
import org.cicirello.search.evo.TruncationSelection;
import org.cicirello.search.operators.permutations.PermutationInitializer;

/**
 * Experiments with the Quadratic Assignment Problem.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class QAPExperiments {
	
	/**
	 * Runs the experiments.
	 * @param args The args[0] is the size of the instance, which defaults to 100.
	 */
	public static void main(String[] args) {
		final int N = args.length > 0 ? Integer.parseInt(args[0]) : 100;
		
		final int[] COST_RANGE = {1, 50};
		final int[] DISTANCE_RANGE = {1, 50};
		
		final int NUM_INSTANCES = 50;
		
		// Chips-n-Salsa doesn't currently have a (mu + lambda)-EA.
		// and thus no (1+1)-EA. However, we can get the equivalent
		// with population size of 2, elitism of 1 (keeping the best unaltered),
		// and truncation selection with k=1. Thus creating exactly one mutant of
		// best each generation, which becomes the elite is better or otherwise will
		// be replaced during next iteration.
		
		final int POPULATION_SIZE = 2;
		
		// truncation selection parameter
		final int K = 1;
		
		final int MIN_GENERATIONS = 100;
		final int MAX_GENERATIONS = 10000000;
		
		ArrayList<MutationOperator<Permutation>> mutationOps = new ArrayList<MutationOperator<Permutation>>();
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
		
		mutationOps.add(new ScrambleMutation());
		columnLabels.add("Scramble");
		
		System.out.print("Instance\tGenerations");
		for (String label : columnLabels) {
			System.out.print("\t" + label);
		}
		System.out.println();
		for (int seed = 1; seed <= NUM_INSTANCES; seed++) {
			QuadraticAssignmentProblem problem = QuadraticAssignmentProblem.createUniformRandomInstance(
				N, 
				COST_RANGE[0], 
				COST_RANGE[1],
				DISTANCE_RANGE[0],
				DISTANCE_RANGE[1],
				seed
			);
			
			ArrayList<GenerationalMutationOnlyEvolutionaryAlgorithm<Permutation>> evos = new ArrayList<GenerationalMutationOnlyEvolutionaryAlgorithm<Permutation>>();
			
			for (MutationOperator<Permutation> mutation : mutationOps) {
				evos.add(
					new GenerationalMutationOnlyEvolutionaryAlgorithm<Permutation>(
						POPULATION_SIZE,
						mutation.split(),
						1.0,
						new PermutationInitializer(N),
						new NegativeIntegerCostFitnessFunction<Permutation>(problem),
						new TruncationSelection(K),
						1
					)
				);
			}
			
			int totalGenerations = MIN_GENERATIONS;
			System.out.print(seed + "\t" + totalGenerations);
			for (GenerationalMutationOnlyEvolutionaryAlgorithm<Permutation> ea : evos) {
				ea.optimize(totalGenerations);
				System.out.print("\t" + ea.getProgressTracker().getCost());
			}
			System.out.println();
			int prevTotalGens = totalGenerations;
			for (totalGenerations *= 10; totalGenerations <= MAX_GENERATIONS; totalGenerations *= 10) {
				System.out.print(seed + "\t" + totalGenerations);
				for (GenerationalMutationOnlyEvolutionaryAlgorithm<Permutation> ea : evos) {
					ea.reoptimize(totalGenerations - prevTotalGens);
					System.out.print("\t" + ea.getProgressTracker().getCost());
				}
				prevTotalGens = totalGenerations;
				System.out.println();
				System.out.flush();
			}
		}
	}
}
