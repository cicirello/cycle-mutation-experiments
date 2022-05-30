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

/**
 * <p>Code to reproduce the experiments from the following article:</p>
 *
 * <p>Vincent A. Cicirello. 2022. <a href="https://www.cicirello.org/publications/applsci-12-05506.pdf">Cycle 
 * Mutation: Evolving Permutations via Cycle Induction</a>, <i>Applied Sciences</i>, 12(11), Article 5506 (June 2022). 
 * doi:<a href="https://doi.org/10.3390/app12115506">10.3390/app12115506</a></p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
module org.cicirello.cycle_mutation_experiments {
	exports org.cicirello.experiments.cyclemutation;
	
	requires org.cicirello.chips_n_salsa;
	requires org.cicirello.jpt;
	requires org.cicirello.rho_mu;
	requires org.cicirello.core;
}
