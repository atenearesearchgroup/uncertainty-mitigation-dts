# Companion repository for "*Quantifying and combining uncertainty for improving the behavior of Digital Twins*"

## Overview
This is the GitHub repository for the code accompanying our paper on uncertainty-aware Digital Twins for adaptive systems. Uncertainty is an inherent property of any complex system, and our approach proposes an explicit representation of the sources of uncertainty of both the system and the models by means of random variables, making uncertainty a first-class citizen.

This allows for a more accurate comparison of the behaviors of the physical system and the digital twin, assessing their validity and determining when they are consistent or diverge. Our proposed approach is illustrated and validated through an exemplary incubator system. This repository contains the code used to implement and test our proposed approach.

## Requirements and Installation

### Requirements

- Java 18 or higher
- Eclipse IDE
- Maven

### Installation

1. Clone the repository:

   ```git clone https://github.com/your/repository.git```

2. Open Eclipse and import the project:

- Click on File > Import.
- Choose _Existing Maven Projects_ and click _Next_.
- Browse to the project directory (_incubatorDigitalTwinForUncertainty_) and click _Finish_.

3. Eclipse will automatically resolve and download the Maven dependencies specified in the pom.xml file.


4. Once the dependencies are successfully downloaded, you can run the desired files in Eclipse:

- Navigate to the file you want to execute.
- Right-click on the any of the main files under the directory /main.java.fr.univcotedeazur/comparison.to and select Run As > Java Application.
- It will generate and show the graphics corresponding to the analysis.


## Repository Structure

The repository has the following structure:

```
incubatorDigitalTwinForUncertainty
├── dependency-reduced-pom.xml
├── pom.xml
└── src
    ├── main.java.fr.univcotedeazur
    │    ├── SimulationConstants.java
    │    ├── comparison.to 
    │    │    ├── eachother
    │    │    │   ├── MitigatedCon trolComparisonsRun.java
    │    │    │   ├── MitigatedControlFailureComparisonRun.java
    │    │    │   ├── ModelPlantControlComparisonsRun.java
    │    │    │   ├── PhysicalPlantControlComparisonsForZoom.java
    │    │    │   └── PhysicalPlantControlComparisonsRun.java
    │    │    └── measurand
    │    │       ├── MitigatedControlComparisonsToMeasurand.java
    │    │       ├── ModelPlantUncertaintyControlComparisonsToMeasurand.java
    │    │       ├── PhysicalPlantClassicalControlComparisonsToMeasurand.java
    │    │       └── PhysicalPlantUncertaintyControlComparisonsToMeasurand.java
    │    ├── models
    │    │   ├── ClassicalPlantModel.java
    │    │   ├── ControllerModel.java
    │    │   ├── ControllerModelUncertaintyAware.java
    │    │   ├── NoisyPhysicalPlantClassicalMock.java
    │    │   ├── NoisyPhysicalPlantMock.java
    │    │   ├── PerfectPlantModel.java
    │    │   ├── PlantModel.java
    │    │   ├── PlantSnapshot.java
    │    │   ├── UncertainPhysicalPlantMock.java
    │    │   └── UncertainPlantModel.java
    │    ├── simple
    │    │   └── runs
    │    │       ├── NoisyPhysicalPlantRun.java
    │    │       ├── NoisyUncertainSystemRunMock.java
    │    │       ├── UncertainModelRun.java
    │    │       └── UncertainPhysicalPlantRun.java
    │    └── utils
    └── uDataTypes
```

The repository consists of the following key components:

- `dependency-reduced-pom.xml`: The reduced version of the POM file that excludes unnecessary dependencies.
- `pom.xml`: The Maven Project Object Model (POM) file that contains the project configuration and dependencies.
- `src`: 
  - `main.java.fr.univcotedeazur`: This package includes all the main files with the comparisons between different models and measurands in a set of scenarios.
    - `SimulationConstants.java`: A Java file containing the simulation constants shared by all the comparisons in this package.
    - `comparison.to`
      - `eachother`: A package containing classes for comparisons between models in different scenarios.
        - `MitigatedControlComparisonsRun.java`
        - `MitigatedControlFailureComparisonRun.java`
        - `ModelPlantControlComparisonsRun.java`
        - `PhysicalPlantControlComparisonsForZoom.java`
        - `PhysicalPlantControlComparisonsRun.java`
      - `measurand`: A package containing classes for comparisons to measurands.
        - `MitigatedControlComparisonsToMeasurand.java`
        - `ModelPlantUncertaintyControlComparisonsToMeasurand.java`
        - `PhysicalPlantClassicalControlComparisonsToMeasurand.java`
        - `PhysicalPlantUncertaintyControlComparisonsToMeasurand.java`
    - `models`: A package containing  the different incubators models mentioned in the paper.
      - `ClassicalPlantModel.java`:
      - `ControllerModel.java`
      - `ControllerModelUncertaintyAware.java`
      - `NoisyPhysicalPlantClassicalMock.java`
      - `NoisyPhysicalPlantMock.java`
      - `PerfectPlantModel.java`
      - `PlantModel.java`
      - `PlantSnapshot.java`
      - `UncertainPhysicalPlantMock.java`
      - `UncertainPlantModel.java`
    - `simple.runs`: A package containing different run scenarios for the incubator models.
        - `NoisyPhysicalPlantRun.java`
        - `NoisyUncertainSystemRunMock.java`
        - `UncertainModelRun.java`
        - `UncertainPhysicalPlantRun.java`
    - `utils`: A package containing utility classes.
  - `uDataTypes`: A package containing uncertain data types used for defining the uncertainty as first-class citizend and performing the calculations.

## Usage

To use this project, follow these steps:

1. Open Eclipse and import the project as described in the [Installation](#installation) section.

2. Navigate to the package `main.java.fr.univcotedeazur.comparisons.to`.

3. Run the main files of the comparisons to execute specific evaluations and obtain the results presented in the research paper.

4. After running a specific comparison, it will show a graphic with the results for the different models involved.
