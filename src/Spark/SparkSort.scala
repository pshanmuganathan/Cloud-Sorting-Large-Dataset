//Spark also uses the same Map-Reduce based implementation as Hadoop does.
//When installing Spark it installs various features and language packages, like Scala, R-Studio, Python, specifically for Spark.
//Implemented sorting using scala’s sortByKey(), functionality, which sorts the data based on Key value (10 Bytes). The sorted data obtained using spark, is divided into chunks.

import scala.collection.immutable.ListMap
import java.io.{File, BufferedWriter, FileWriter}
import org.apache.spark.SparkContext._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SparkSort {

  def main(args: Array[String]) 
  {
	//Should be some file on your system
    val logFile = "spark-1.6.1-bin-hadoop2.6/input" 
	
	//configur the spark
    val conf = new SparkConf().setAppName("File Sort").set("spark.executor.memory", "2g")
    val sc = new SparkContext(conf)
    val file = new File("ouput")
    val bw = new BufferedWriter(new FileWriter(file))
    val data = sc.textFile(logFile)
	
	//It will split the file
    val splitData = data.flatMap(line => line.split("\n"))

    val arrayFromCollectedData = splitData.flatMap(line => Map(line.substring(0,10) -> line.substring(10,line.length)))
    val mapFromArray = arrayFromCollectedData.map(line => line._1 -> line._2)
    val sortedData = mapFromArray.sortBy(_._1)
    val collectedData = sortedData.collect()
    collectedData.foreach(line => bw.write(line._1 + line._2 + "\r\n"))

  }
}