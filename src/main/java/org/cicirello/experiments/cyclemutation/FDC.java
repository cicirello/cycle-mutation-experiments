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
import org.cicirello.search.problems.IntegerCostOptimizationProblem;
import org.cicirello.search.problems.tsp.TSP;
import org.cicirello.search.problems.LargestCommonSubgraph;
import org.cicirello.search.problems.QuadraticAssignmentProblem;
import org.cicirello.permutations.distance.PermutationDistanceMeasurer;
import org.cicirello.permutations.distance.InterchangeDistance;
import org.cicirello.permutations.distance.ReinsertionDistance;
import org.cicirello.permutations.distance.CyclicEdgeDistance;
import org.cicirello.permutations.distance.ScrambleDistance;
import org.cicirello.math.stats.Statistics;
import org.cicirello.math.rand.RandomIndexer;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SplittableRandom;

/**
 * Computes FDC for one small instance of each of the TSP, QAP, and LCS problems,
 * for a distance metric corresponding to each of the mutation operators Swap,
 * Insertion, Reversal, and Cycle.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class FDC {
	
	/**
	 * Computes FDC for one small instance of each problem.
	 *
	 * @param args Pass permutation length as args[0], which must be small
	 * due to brute-force computation of FDC over space of all permutations of this length.
	 */
	public static void main(String[] args) {
		// problem instance size, which must be small since our FDC is
		// brute-force enumerating the space of permutations of length N.
		final int N = args.length > 0 ? Integer.parseInt(args[0]) : 10;
		
		ArrayList<PermutationDistanceMeasurer> distances = new ArrayList<PermutationDistanceMeasurer>();
		ArrayList<String> editOperationNames = new ArrayList<String>(); 
		
		distances.add(new CycleDistance());
		editOperationNames.add("Cycle(inf)");
		
		distances.add(new KCycleDistance(5));
		editOperationNames.add("Cycle(5)");
		
		distances.add(new KCycleDistance(4));
		editOperationNames.add("Cycle(4)");
		
		distances.add(new KCycleDistance(3));
		editOperationNames.add("Cycle(3)");
		
		distances.add(new InterchangeDistance());
		editOperationNames.add("Swap");
		
		distances.add(new ReinsertionDistance());
		editOperationNames.add("Insertion");
		
		distances.add(new CyclicEdgeDistance());
		editOperationNames.add("Reversal");
		
		distances.add(new ScrambleDistance());
		editOperationNames.add("Scramble");
		
		TSP.Integer tsp = createSimpleCircularTSPInstance(N);
		
		HashSet<Permutation> allBest = computeSetOfBestPermutations(N, tsp);
		double[][] data = calculateDataForFDC(N, allBest, tsp, distances);
		double[] r = correlationsToLastRow(data);
		printResults("TSP", r, editOperationNames);
		
		LargestCommonSubgraph lcs = createLCSInstancePetersen(42);
		
		allBest = computeSetOfBestPermutations(N, lcs);
		data = calculateDataForFDC(N, allBest, lcs, distances);
		r = correlationsToLastRow(data);
		printResults("LCS", r, editOperationNames);

		QuadraticAssignmentProblem qap = createQAPInstanceWithKnownOptimal(N, 42);
		
		allBest = computeSetOfBestPermutations(N, qap);
		data = calculateDataForFDC(N, allBest, qap, distances);
		r = correlationsToLastRow(data);
		printResults("QAP", r, editOperationNames);
	}
	
	private static TSP.Integer createSimpleCircularTSPInstance(int n) {
		// Generate city locations: on a circle centered at (0,0).
		final double RADIUS = 10;
		double[] x = new double[n];
		double[] y = new double[n];
		double angle = 0.0;
		final double DELTA_A = 2.0 * Math.PI / n;
		for (int i = 0; i < n; i++) {
			x[i] = RADIUS * Math.cos(angle);
			y[i] = RADIUS * Math.sin(angle);
			angle += DELTA_A;
		}
		
		// 2N optimal solutions to this... N starting cities, * 2 directions of travel around circle
		return new TSP.Integer(x, y);
	}
	
	private static LargestCommonSubgraph createLCSInstancePetersen(long seed) {
		// The Petersen graph is strongly regular with 120 automorphisms
		int n = 10;
		ArrayList<LargestCommonSubgraph.Edge> edges1 = new ArrayList<LargestCommonSubgraph.Edge>();
		edges1.add(new LargestCommonSubgraph.Edge(0, 1));
		edges1.add(new LargestCommonSubgraph.Edge(1, 2));
		edges1.add(new LargestCommonSubgraph.Edge(2, 3));
		edges1.add(new LargestCommonSubgraph.Edge(3, 4));
		edges1.add(new LargestCommonSubgraph.Edge(4, 0));
		edges1.add(new LargestCommonSubgraph.Edge(0, 5));
		edges1.add(new LargestCommonSubgraph.Edge(1, 6));
		edges1.add(new LargestCommonSubgraph.Edge(2, 7));
		edges1.add(new LargestCommonSubgraph.Edge(3, 8));
		edges1.add(new LargestCommonSubgraph.Edge(4, 9));
		edges1.add(new LargestCommonSubgraph.Edge(5, 7));
		edges1.add(new LargestCommonSubgraph.Edge(6, 8));
		edges1.add(new LargestCommonSubgraph.Edge(7, 9));
		edges1.add(new LargestCommonSubgraph.Edge(8, 5));
		edges1.add(new LargestCommonSubgraph.Edge(9, 6));
		
		SplittableRandom gen = new SplittableRandom(seed);
		Permutation p = new Permutation(n, gen);
		ArrayList<LargestCommonSubgraph.Edge> edges2 = new ArrayList<LargestCommonSubgraph.Edge>();
		for (LargestCommonSubgraph.Edge e : edges1) {
			edges2.add(new LargestCommonSubgraph.Edge(p.get(e.getU()), p.get(e.getV())));
		}
		
		return new LargestCommonSubgraph(n, n, edges1, edges2);
	}
	
	private static QuadraticAssignmentProblem createQAPInstanceWithKnownOptimal(int n, long seed) {
		int[][] cost = new int[n][n];
		int[][] distance = new int[n][n];
		SplittableRandom gen = new SplittableRandom(seed);
		Permutation p = new Permutation(n, gen);
		int max = n * n - n;
		int[][] values = new int[2][max];
		for (int i = 0; i < max; i++) {
			values[1][max-1-i] = values[0][i] = i + 1;
		}
		for (int i = max - 1; i > 0; i--) {
			int j = gen.nextInt(i+1);
			if (i != j) {
				int temp = values[0][i];
				values[0][i] = values[0][j];
				values[0][j] = temp;
				temp = values[1][i];
				values[1][i] = values[1][j];
				values[1][j] = temp;
			}
		}
		
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					cost[i][j] = values[0][k];
					distance[p.get(i)][p.get(j)] = values[1][k];
					k++;
				}
			}
		}
		return QuadraticAssignmentProblem.createInstance(cost, distance);
	}
	
	private static void printResults(String problemName, double[] r, ArrayList<String> editOperationNames) {
		System.out.println("------------------------------");
		System.out.println("FDC Results for " + problemName);
		System.out.println("------------------------------");
		System.out.println("Note: Computed using costs for minimization problems rather than fitness,");
		System.out.println("so sign may be opposite what should normally be expected.");
		System.out.println();
		for (int i = 0; i < r.length; i++) {
			System.out.printf("%9s\t%.4f\n", editOperationNames.get(i), r[i]);
		}
		System.out.println("------------------------------");
		System.out.println();
		System.out.flush();
	}
	
	private static double[] correlationsToLastRow(double[][] data) {
		double[] r = new double[data.length-1];
		for (int i = 0; i < r.length; i++) {
			r[i] = Statistics.correlation(data[i], data[r.length]);
		}
		return r;
	}
	
	private static double[][] calculateDataForFDC(int n, HashSet<Permutation> allBest, IntegerCostOptimizationProblem<Permutation> problem, ArrayList<PermutationDistanceMeasurer> distances) {
		double[][] data = new double[distances.size()+1][fact(n)];
		Permutation p0 = new Permutation(n, 0);
		int i = 0;
		for (Permutation p : p0) {
			for (int j = 0; j < distances.size(); j++) {
				PermutationDistanceMeasurer d = distances.get(j);
				data[j][i] = minDistanceTo(d, p, allBest);
			}
			data[distances.size()][i] = problem.cost(p);
			i++;
		}
		return data;
	}
	
	private static int minDistanceTo(PermutationDistanceMeasurer d, Permutation p, HashSet<Permutation> allBest) {
		int min = Integer.MAX_VALUE;
		for (Permutation other : allBest) {
			int dist = d.distance(p, other);
			if (dist < min) {
				min = dist;
			}
		}
		return min;
	}
	
	private static HashSet<Permutation> computeSetOfBestPermutations(int n, IntegerCostOptimizationProblem<Permutation> problem) {
		HashSet<Permutation> set = new HashSet<Permutation>();
		int minCost = Integer.MAX_VALUE;
		Permutation p0 = new Permutation(n, 0);
		for (Permutation p : p0) {
			int cost = problem.cost(p);
			if (cost < minCost) {
				set.clear();
				minCost = cost;
				set.add(p);
			} else if (cost == minCost) {
				set.add(p);
			}
		}
		return set;
	}
	
	private static int fact(int n) {
		int f = 1;
		for (int i = 2; i <= n; i++) {
			f *= i;
		}
		return f;
	}
}
