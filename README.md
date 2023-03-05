# cycle-mutation-experiments

Copyright &copy; 2022 Vincent A. Cicirello

This repository contains code to reproduce the experiments, and analysis of 
experimental data, from the following paper:

> Vincent A. Cicirello. 2022. [Cycle Mutation: Evolving Permutations via Cycle Induction](https://www.cicirello.org/publications/applsci-12-05506.pdf), *Applied Sciences*, 12(11), Article 5506 (June 2022). doi:[10.3390/app12115506](https://doi.org/10.3390/app12115506)

| __Related Publication__ | [![DOI](doi.svg)](https://doi.org/10.3390/app12115506) |
| :--- | :--- |
| __License__ | [![GitHub](https://img.shields.io/github/license/cicirello/cycle-mutation-experiments)](LICENSE) |
| __Packages and Releases__ | [![Maven Central](https://img.shields.io/maven-central/v/org.cicirello/cycle-mutation-experiments.svg?label=Maven%20Central)](https://central.sonatype.com/search?namespace=org.cicirello&q=cycle-mutation-experiments) [![GitHub release (latest by date)](https://img.shields.io/github/v/release/cicirello/cycle-mutation-experiments?logo=GitHub)](https://github.com/cicirello/cycle-mutation-experiments/releases) |

## Dependencies

The experiments depend upon the following libraries, and in some cases this research has 
also contributed to these libraries:
* [Chips-n-Salsa](https://chips-n-salsa.cicirello.org/)
* [JavaPermutationTools](https://jpt.cicirello.org)
* [&rho;&mu;](https://rho-mu.cicirello.org)
* [org.cicirello.core](https://core.cicirello.org)

## Requirements to Build and Run the Experiments

To build and run the experiments on your own machine, you will need the following:
* __JDK 11__: I used OpenJDK 11, but other distributions should be fine. 
* __Apache Maven__: In the root of the repository, there is a `pom.xml` 
  for building the Java programs for the experiments. Using this `pom.xml`, 
  Maven will take care of downloading the exact versions of 
  [Chips-n-Salsa](https://chips-n-salsa.cicirello.org/) (release 4.7.0), 
  [JavaPermutationTools](https://jpt.cicirello.org) (release 3.1.1), and
  [&rho;&mu;](https://rho-mu.cicirello.org) (release 1.2.0)  that were 
  used in the experiments. 
* __Python 3__: The repository contains Python programs that were used to 
  compute summary statistics, and to generate
  graphs for the figures of the paper. If you want to run the Python programs, 
  you will need Python 3. I specifically used Python 3.9.6. You also need  
  matplotlib installed.
* __Make__: The repository contains a Makefile to simplify running the build, 
  running the experiment's Java programs, and running the Python program to 
  analyze the data. If you are familiar with using the Maven build tool, 
  and running Python programs, then you can just run these directly, although 
  the Makefile may be useful to see the specific commands needed.

## Building the Java Programs (Option 1)

The source code of the Java programs implementing the experiments
is in the [src/main](src/main) directory.  You can build the experiment 
programs in one of the following ways.

__Using Maven__: Execute the following from the root of the
repository.

```shell
mvn clean package
```

__Using Make__: Or, you can execute the following from the root
of the repository.

```shell
make build
```

This produces a jar file containing several Java programs for running 
different parts of the experiments and analysis. The jar also contains all
dependencies, including [Chips-n-Salsa](https://chips-n-salsa.cicirello.org/), 
[JavaPermutationTools](https://jpt.cicirello.org), and 
[&rho;&mu;](https://rho-mu.cicirello.org).
If you are unfamiliar with the usual structure of the directories of 
a Java project built with Maven, the `.class` files, the `.jar` file, 
etc will be found in a `target` directory that is created by the 
build process.

## Downloading a prebuilt jar (Option 2)

As an alternative to building the jar (see above), you can choose to instead
download a prebuilt jar of the experiments from the Maven Central repository.
The Makefile contains a target that will do this for you, provided that you have
curl installed on your system. To download the jar of the precompiled code of 
the experiments, run the following from the root of the repository:

```shell
make download
```

The jar that it downloads contains the compiled code of the experiments as well
as all dependencies within a single jar file.

## Running the Experiments

If you just want to inspect the data from my runs, then you can find that output
in the [/data](data) directory. If you instead want to run the experiments yourself,
you must first either follow the build instructions or download a prebuilt jar (see above
sections). Once the jar of the experiments is either built or downloaded, you can then run 
the experiments with the following executed at the root of the repository:

```shell
make experiments
```

If you don't want to overwrite my original data files, then first change the variable
`pathToDataFiles` in the `Makefile` before running the above command.

This will run each of the experiment programs in sequence, 
with the results piped to text files in the [/data](data) directory. Note that
this directory already contains the output from my runs, so if you execute this command,
you will overwrite the data that was used in the paper. Some parts of this will not
change, but certain parts, due to randomization may not be exactly the same, although should
be statistically consistent. 

There are also several other targets in the Makefile if you wish to 
run only some of the experiments from the paper. See the Makefile for
details.

One of the other make targets will execute the program that computes the
fitness distance correlation (FDC) results for the three small instances of the
different problems. To tun the FDC program, execute the following:

```shell
make fdc
```

## Analyzing the Experimental Data

To run the Python program that I used to generate summary statistics,  
and generate the graphs for the figures from the paper,
you need Python 3 installed. The source code of the Python programs is 
found in the [src/analysis](src/analysis) directory.  To run the analysis
execute the following at the root of the repository:

```shell
make analysis
```

If you don't want to overwrite my original data files, and figures, then change the variable
`pathToDataFiles` in the `Makefile` before running the above command.

This will analyze the data from the [/data](data) directory. It will also 
generate the figures in that directory, as well as output a few txt files with
summary statistics into that directory. This make command will also take
care of installing any required Python packages if you don't already have them
installed, such as matplotlib.

To convert the `eps` versions of the figures to `pdf`, then after running the above
analysis, run the following (this assumes that you have epstopdf installed):

```shell
make epstopdf
```

## Other Files in the Repository

There are a few other files, potentially of interest, in the repository,
which include:
* `system-stats.txt`: This file contains details of the system I 
  used to run the experiments, such as operating system, processor 
  specs, Java JDK and VM. It is in the [/data](data) directory.

## License

The code to replicate the experiments from the paper, as well as the
Chips-n-Salsa, JavaPermutationTools, and &rho;&mu; libraries are licensed 
under the [GNU General Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
