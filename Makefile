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

# Runs all experiments

.PHONY: experiments
experiments: qap

.PHONY: qap
qap:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperiments 100 > ${pathToDataFiles}/qap.100.txt

.PHONY: binpack
binpack:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.BinPackingTripletExperiments 33 > ${pathToDataFiles}/bin.triplet.99.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.BinPackingTripletExperiments 67 > ${pathToDataFiles}/bin.triplet.201.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.BinPackingTripletExperiments 100 > ${pathToDataFiles}/bin.triplet.300.txt

# Tune tournament size

.PHONY: tuneqap
tuneqap:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 2 > ${pathToDataFiles}/tuningqap.2.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 3 > ${pathToDataFiles}/tuningqap.3.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 4 > ${pathToDataFiles}/tuningqap.4.txt
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.QAPExperimentsTuningOnly 100 8 > ${pathToDataFiles}/tuningqap.8.txt
