ifeq ($(OS),Windows_NT)
	py = "python"
else
	py = "python3"
endif

JARFILE = "target/cycle-mutation-experiments-1.0.0-jar-with-dependencies.jar"
pathToDataFiles = "data"

.PHONY: build
build:
	mvn clean package

# Summarizes the experimental data, such as statistics, graphs, etc

.PHONY: analysis
analysis:
	$(py) -m pip install --user pycairo
	$(py) -m pip install --user matplotlib
	$(py) -m pip install --user scipy
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/lcs.SA50.txt graph 1 2 > ${pathToDataFiles}/summary.lcs.SA50.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/lcs.50.txt graph 1 2 > ${pathToDataFiles}/summary.lcs.50.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/qap.SA50.txt graph 1 2 > ${pathToDataFiles}/summary.qap.SA50.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/qap.50.txt graph 1 2 > ${pathToDataFiles}/summary.qap.50.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tsp.SA100.txt graph 1 2 > ${pathToDataFiles}/summary.tsp.SA100.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tsp.100.txt graph 1 2 > ${pathToDataFiles}/summary.tsp.100.txt

# Calculates FDC for a few small instances of TSP, LCS, and QAP

.PHONY: fdc
fdc:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.FDC > ${pathToDataFiles}/fdc.txt

# Runs all experiments

.PHONY: experiments
experiments: qap lcs tsp

.PHONY: lcs
lcs:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsSA 50 > ${pathToDataFiles}/lcs.SA50.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperiments 50 > ${pathToDataFiles}/lcs.50.txt
	
.PHONY: qap
qap:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsSA 50 > ${pathToDataFiles}/qap.SA50.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperiments 50 > ${pathToDataFiles}/qap.50.txt

.PHONY: tsp
tsp:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.TSPExperimentsSA 100 > ${pathToDataFiles}/tsp.SA100.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.TSPExperiments 100 > ${pathToDataFiles}/tsp.100.txt

.PHONY: srg
srg:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsStronglyRegularSA > ${pathToDataFiles}/lcs.SAsrg.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/lcs.SAsrg.txt graph 1 2 > ${pathToDataFiles}/summary.lcs.SAsrg.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsStronglyRegular > ${pathToDataFiles}/lcs.srg.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/lcs.srg.txt graph 1 2 > ${pathToDataFiles}/summary.lcs.srg.txt
