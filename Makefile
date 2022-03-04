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
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/qap.100.txt graph 1 4 > ${pathToDataFiles}/summary.qap.100.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/qap.SA100.txt graph 1 2 > ${pathToDataFiles}/summary.qap.SA100.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/lcs.100.txt graph 1 2 > ${pathToDataFiles}/summary.lcs.100.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/lcs.SUS100.txt graph 1 2 > ${pathToDataFiles}/summary.lcs.SUS100.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/lcs.SA100.txt graph 1 2 > ${pathToDataFiles}/summary.lcs.SA100.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/lcs.SA100v2.txt graph 1 2 ${pathToDataFiles}/lcs.SA100.txt 8 > ${pathToDataFiles}/summary.lcs.SA100v2.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/lcs.100v2.txt graph 1 2 ${pathToDataFiles}/lcs.100.txt 8 > ${pathToDataFiles}/summary.lcs.100v2.txt

# Runs all experiments

.PHONY: experiments
experiments: qap lcs

.PHONY: lcs
lcs:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsSA 100 > ${pathToDataFiles}/lcs.SA100.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperiments 100 > ${pathToDataFiles}/lcs.100.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsSUS 100 > ${pathToDataFiles}/lcs.SUS100.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsVersionTwoSA 100 > ${pathToDataFiles}/lcs.SA100v2.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsVersionTwo 100 > ${pathToDataFiles}/lcs.100v2.txt
	
.PHONY: qap
qap:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperiments 100 > ${pathToDataFiles}/qap.100.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsSA 100 > ${pathToDataFiles}/qap.SA100.txt

.PHONY: binpack
binpack:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.BinPackingTripletExperiments 33 > ${pathToDataFiles}/bin.triplet.99.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.BinPackingTripletExperiments 67 > ${pathToDataFiles}/bin.triplet.201.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.BinPackingTripletExperiments 100 > ${pathToDataFiles}/bin.triplet.300.txt

# Tune tournament size for tournament selection and truncation threshold for truncation selection

.PHONY: tunelcs
tunelcs:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 1 > ${pathToDataFiles}/tuninglcs.T1.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 2 > ${pathToDataFiles}/tuninglcs.T2.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 3 > ${pathToDataFiles}/tuninglcs.T3.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 4 > ${pathToDataFiles}/tuninglcs.T4.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 5 > ${pathToDataFiles}/tuninglcs.T5.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 10 > ${pathToDataFiles}/tuninglcs.T10.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 20 > ${pathToDataFiles}/tuninglcs.T20.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 6 > ${pathToDataFiles}/tuninglcs.T6.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 7 > ${pathToDataFiles}/tuninglcs.T7.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningOnlyTruncation 100 8 > ${pathToDataFiles}/tuninglcs.T8.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T1.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T1.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T2.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T2.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T3.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T3.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T4.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T4.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T5.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T5.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T10.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T10.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T20.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T20.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T6.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T6.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T7.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T7.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuninglcs.T8.txt no 1000 > ${pathToDataFiles}/summary.tuninglcs.T8.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningV2 100 1 > ${pathToDataFiles}/tuninglcs-V2.T1.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/tuninglcs-V2.T1.txt no 1000 2 ${pathToDataFiles}/tuninglcs.T1.txt 9 > ${pathToDataFiles}/summary.tuninglcs-V2.T1.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningV2 100 2 > ${pathToDataFiles}/tuninglcs-V2.T2.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/tuninglcs-V2.T2.txt no 1000 2 ${pathToDataFiles}/tuninglcs.T2.txt 9 > ${pathToDataFiles}/summary.tuninglcs-V2.T2.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningV2 100 3 > ${pathToDataFiles}/tuninglcs-V2.T3.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/tuninglcs-V2.T3.txt no 1000 2 ${pathToDataFiles}/tuninglcs.T3.txt 9 > ${pathToDataFiles}/summary.tuninglcs-V2.T3.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningV2 100 4 > ${pathToDataFiles}/tuninglcs-V2.T4.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/tuninglcs-V2.T4.txt no 1000 2 ${pathToDataFiles}/tuninglcs.T4.txt 9 > ${pathToDataFiles}/summary.tuninglcs-V2.T4.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningV2 100 5 > ${pathToDataFiles}/tuninglcs-V2.T5.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/tuninglcs-V2.T5.txt no 1000 2 ${pathToDataFiles}/tuninglcs.T5.txt 9 > ${pathToDataFiles}/summary.tuninglcs-V2.T5.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningV2 100 10 > ${pathToDataFiles}/tuninglcs-V2.T10.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/tuninglcs-V2.T10.txt no 1000 2 ${pathToDataFiles}/tuninglcs.T10.txt 9 > ${pathToDataFiles}/summary.tuninglcs-V2.T10.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.LCSExperimentsTuningV2 100 20 > ${pathToDataFiles}/tuninglcs-V2.T20.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/tuninglcs-V2.T20.txt no 1000 2 ${pathToDataFiles}/tuninglcs.T20.txt 9 > ${pathToDataFiles}/summary.tuninglcs-V2.T20.txt
	
.PHONY: tuneqap
tuneqap:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 2 > ${pathToDataFiles}/tuningqap.2.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 3 > ${pathToDataFiles}/tuningqap.3.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 4 > ${pathToDataFiles}/tuningqap.4.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 8 > ${pathToDataFiles}/tuningqap.8.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 12 > ${pathToDataFiles}/tuningqap.12.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 16 > ${pathToDataFiles}/tuningqap.16.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 20 > ${pathToDataFiles}/tuningqap.20.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 24 > ${pathToDataFiles}/tuningqap.24.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 28 > ${pathToDataFiles}/tuningqap.28.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 32 > ${pathToDataFiles}/tuningqap.32.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 1 > ${pathToDataFiles}/tuningqap.T1.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 2 > ${pathToDataFiles}/tuningqap.T2.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 3 > ${pathToDataFiles}/tuningqap.T3.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 4 > ${pathToDataFiles}/tuningqap.T4.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 5 > ${pathToDataFiles}/tuningqap.T5.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 6 > ${pathToDataFiles}/tuningqap.T6.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 7 > ${pathToDataFiles}/tuningqap.T7.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 10 > ${pathToDataFiles}/tuningqap.T10.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 20 > ${pathToDataFiles}/tuningqap.T20.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnlyTruncation 100 30 > ${pathToDataFiles}/tuningqap.T30.txt
	
.PHONY: tuningdata
tuningdata:
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.2.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.2.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.3.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.3.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.4.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.4.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.8.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.8.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.12.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.12.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.16.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.16.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.20.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.20.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.24.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.24.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.28.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.28.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.32.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.32.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T1.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T1.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T2.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T2.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T3.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T3.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T4.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T4.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T5.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T5.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T6.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T6.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T7.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T7.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T10.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T10.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T20.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T20.txt
	$(py) src/analysis/summarize-stats.py ${pathToDataFiles}/tuningqap.T30.txt no 1000 > ${pathToDataFiles}/summary.tuningqap.T30.txt
	
.PHONY: more
more:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsVersionTwoSA 100 > ${pathToDataFiles}/qap.SA100v2.txt
	$(py) src/analysis/summarize-stats-v2.py ${pathToDataFiles}/qap.SA100v2.txt graph 1 2 ${pathToDataFiles}/qap.SA100.txt 8 > ${pathToDataFiles}/summary.qap.SA100v2.txt
	