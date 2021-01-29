# IMSP's Data parser

Parse raw data from NCBI Blastp into pair-wise similarity matrices

## Installation

- Clone the repository
- Make sure you have JDK 1.7 or above installed

## Usage

- Collect protein FASTA files as shown in file ```FASTA.txt```
- Obtain ```XXX-Alignment-hitTable.csv```from either online BLASTp or Local Blast
- Put ```XXX-Alignment-hitTable.csv``` in ```Parser/protein data/layer_name/protein_name/```
- Navigate to ```src/Parser/driver.java```
- Run the main method, the result will be in ```Parser/Result/layer_name/protein_name-group_name.csv```
