#!/bin/sh

echo 'mount the storage'
sudo apt-get install mdadm
lsblk
sudo file -s /dev/nvme0n1
sudo mkfs -t ext4 /dev/nvme0n1
sudo mkdir /data
sudo mount /dev/nvme0n1 /data
time mv Hadoop /data

echo 'Generating Un-Sorted file of 1TB'
sudo -i
cd /root/64
sudo source ~/.bashrc
time ./gensort -a 10000000000 /data/input128.txt

echo 'run hadoop'
cd /data/hadoop/bin
time rm -r /data/hadoop/hadoop_data/hdfs/datanode/current
time hdfs namenode -format
time start-all.sh 
time jps

echo 'move data to hdfs'
time hdfs dfs -mkdir /input
time hdfs dfs -mkdir /output
time hdfs dfs  -put /data/input128.txt /input

echo 'Compile the program'
time hadoop com.sun.tools.javac.Main TeraSort.java

echo 'Make jar file'
time jar cf.jar TeraSort*.class

echo 'Run the hadoop code'
time Hadoop jar cf.jar TeraSort /input /output

echo 'Get output file'
time hdfs dfs -get /output /data

echo 'validate the sorted code'
cd /root/64
time ./valsort /data/output/part-r-00000


