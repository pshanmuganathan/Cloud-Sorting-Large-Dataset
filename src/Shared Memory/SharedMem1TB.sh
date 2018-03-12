#!/bin/sh

echo 'Generating File'
./gensort -a 10000000000 InputFile.txt
echo 'Creating file Chunks'
split -n 80 -d --additional-suffix=.txt InputFile.txt chunk- 
echo 'File Chunks Created generated'
javac SharedMem.java
java -Xmx100g SharedMem 1
echo 'FINISHED SORTING'
echo 'Checking Sorted File using Valsort'
./valsort MergedOutput.txt
rm chunk*