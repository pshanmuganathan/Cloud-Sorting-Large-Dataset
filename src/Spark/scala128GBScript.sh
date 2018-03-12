#!/bin/bash

echo'Download Spark Package and extract it to a local machine'
wget www-eu.apache.org/dist/spark/spark-1.6.0/spark-1.6.0-bin-hadoop2.6.tgz
tar -xzvf spark-1.6.0-bin-hadoop2.6.tgz
cd spark-1.6.0-bin-hadoop2.6/ec2

echo'Export root key from amazon account and set it.'
export AWS_ACCESS_KEY_ID="XXXX"
export AWS_SECRET_ACCESS_KEY="XXXX"

echo'Create a spark cluster'
./spark-ec2 -k adityaSparkKey -i /home/aditya/Downloads/spark/adityaSparkKey.pem -s 1 -t c3.large --spot-price=0.02 launch spark_cluster
echo'for 8 nodes'
echo'login'
./spark-ec2 -k PA2.pem -i /home/Downloads/spark/PA2.pem login spark_cluster


echo'Mount EBS Volume'
lsblk
sudo file -s /dev/xvdb
sudo mkfs -t ext4 /dev/xvdb
sudo mkdir /data
sudo mount /dev/xvdb /data
sudo chmod 777 /data/

echo'Generate a 100GB file using gensort'
cd data
wget http://www.ordinal.com/try.cgi/gensort-linux-1.5.tar.gz
tar -xzvf gensort-linux-1.5.tar.gz
cd 64
./gensort -a 1280000000 input128GB

echo'Create a hdfs Directory on hadoop'
cd ephemeral-hdfs/bin
bin/hadoop fs -mkdir /hdfs_aditya
bin/hadoop fs -Ddfs.replication=1 -put /data/64/input128GB /input
bin/hadoop dfs -ls /input/

cd ~
cd spark/bin

echo'Create a Scaala'

./spark-shell -i /root/SparkSort.scala


echo'Get generated sorted file output from hdfs to mounted drive'

bin/hadoop dfs -getmerge /output /data/output128GB

echo'validate using valsort'
cd /data
./64/valsort /data/output128GB
head output128GB
tail /data/output128GB
