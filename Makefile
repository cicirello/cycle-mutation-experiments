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
experiments: binpack

.PHONY: binpack
binpack:
	java -cp ${JARFILE} org.cicirello.experiments.cyclemutation.BinPackingTripletExperiments 333 > ${pathToDataFiles}/bin.triplet.333.txt