#!/bin/sh

echo 'Generating File'
./gensort -a 1280000000 InputFile.txt
echo 'Creating file Chunks'
split -n 80 -d --additional-suffix=.txt InputFile.txt chunk- 
echo 'File Chunks Created generated'
javac SharedMem.java
java -Xmx12g SharedMem 1
echo 'FINISHED SORTING'
echo 'Checking Sorted File using Valsort'
./valsort MergedOutput.txt
rm chunk*