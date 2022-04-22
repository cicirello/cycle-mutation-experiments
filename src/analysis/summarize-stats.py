# Experiments for article in the journal MONE 2022.
# Copyright (C) 2022  Vincent A. Cicirello
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

import sys
import statistics
import matplotlib.pyplot
import scipy.stats

def parse(filename, first = 1) :
    """Computes average costs for each number of generations
    and mutation operator combination across all runs.

    Keyword arguments:
    filename - the name of the data file to summarize
    first - first instance index
    """
    with open(filename, "r") as f :
        headings = None
        data = {}
        lengths = []
        for line in f:
            if headings == None :
                headings = line.split()
            else :
                row = line.split()
                if len(row) > 2 :
                    generations = row[1]
                    if int(row[0]) == first :
                        lengths.append(generations)
                    for i in range(2, len(row)) :
                        cost = int(row[i])
                        head = headings[i]
                        if (generations, head) in data :
                            data[(generations, head)].append(cost)
                        else :
                            data[(generations, head)] = []
        return data, headings, lengths
                        
        

if __name__ == "__main__" :
    datafile = sys.argv[1]
    generateGraphs = len(sys.argv) > 2 and sys.argv[2] == "graph"
    firstInstance = int(sys.argv[3]) if len(sys.argv) > 3 else 1
    firstToGraph = int(sys.argv[4]) if len(sys.argv) > 4 else 2
    baseline = "Swap"
    figureFilename = datafile[:-4] + ".svg"
    epsFilename = datafile[:-4] + ".eps"
    xLabel = 'number of evaluations (log scale)'
    #xLabel = 'number of {0} (log scale)'.format("generations" if datafile.find("SA") < 0 else "evaluations")
    data, headings, lengths = parse(datafile, firstInstance)
    means = { headings[i] : [] for i in range(2, len(headings)) }
    devs = { headings[i] : [] for i in range(2, len(headings)) }
    pValues = { headings[i] : [] for i in range(2, len(headings)) }
    for gens in lengths :
        for i in range(2, len(headings)) :
            head = headings[i]
            means[head].append(statistics.mean(data[(gens, head)]))
            devs[head].append(statistics.stdev(data[(gens, head)]))
            pValues[head].append(scipy.stats.ranksums(data[(gens, head)], data[(gens, baseline)]).pvalue)
    print("MEANS")
    print(headings[1], end="")
    for i in range(2, len(headings)) :
        print("\t" + headings[i], end="")
    print()
    for i, gens in enumerate(lengths) :
        print(gens, end="")
        for j in range(2, len(headings)) :
            head = headings[j]
            print("\t{0:.2f}".format(means[head][i]), end="")
        print()
    print()
    print("STDEVS")
    print(headings[1], end="")
    for i in range(2, len(headings)) :
        print("\t" + headings[i], end="")
    print()
    for i, gens in enumerate(lengths) :
        print(gens, end="")
        for j in range(2, len(headings)) :
            head = headings[j]
            print("\t{0:.2f}".format(devs[head][i]), end="")
        print()
    print()
    print("Wilcoxon ranksum P-val for column compared to", baseline)
    print(headings[1], end="")
    for i in range(2, len(headings)) :
        print("\t" + headings[i], end="")
    print()
    for i, gens in enumerate(lengths) :
        print(gens, end="")
        for j in range(2, len(headings)) :
            head = headings[j]
            print("\t{0:.4f}".format(pValues[head][i]), end="")
        print()

    if generateGraphs :
        w = 3.47 #4.2 #3.3 #2.98
        h = w
        matplotlib.pyplot.rc('font', size=8)
        matplotlib.pyplot.rc('text', usetex=True)
        fig, ax = matplotlib.pyplot.subplots(figsize=(w,h), constrained_layout=True)
        matplotlib.pyplot.xlabel(xLabel)
        matplotlib.pyplot.ylabel('average solution cost')
        for i in range(firstToGraph, len(headings)) :
            line, = ax.plot(lengths,
                            means[headings[i]],
                            #styles[i],
                            label = headings[i])
        ax.legend()
        matplotlib.pyplot.savefig(figureFilename)
        matplotlib.pyplot.savefig(epsFilename)
