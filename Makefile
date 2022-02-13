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
