# Cloud-Sorting-Large-Dataset
TeraSort Using SharedMemory,Hadoop &amp; Spark
## SHARED MEMORY SORT:
* The main aim of this code is to perform sorting on large datasets, considering this I have opted for Quick Sort and k-way external merge sort.
* The program performs sorting for 100GB and 1TB datasets, where key is of 10 Bytes and value of 90 Bytes.
* Sorting is performed based on the key’s ASCII value. 
* I have divided the original dataset into 80 Chunks and applied Quick Sort on these chunks, which provides me with sorted chunks as output. This part of the program has threads applied to it to parallelize the process of sorting. 
* Then using k-way external merge sort, I have merged all the Sorted Chunks of earlier phase into a final sorted output file.
